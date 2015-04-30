/*
 * Copyright 2014 Square Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package flownavigation.common.flow;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;

import java.util.LinkedHashMap;
import java.util.Map;

import flow.Flow;
import flownavigation.path.Path;
import flownavigation.path.PathContainer;
import flownavigation.path.PathContext;
import flownavigation.path.PathContextFactory;

import static flow.Flow.Direction.BACKWARD;
import static flow.Flow.Direction.FORWARD;
import static flow.Flow.Direction.REPLACE;

/**
 * Provides basic right-to-left transitions. Saves and restores view state.
 * Uses {@link PathContext} to allow customized sub-containers.
 */
public class SimplePathContainer extends PathContainer {

    private static final Map<Class, Integer> PATH_LAYOUT_CACHE = new LinkedHashMap<>();

    private final PathContextFactory contextFactory;

    public SimplePathContainer(int tagKey, PathContextFactory contextFactory) {
        super(tagKey);
        this.contextFactory = contextFactory;
    }

    @Override
    protected void performTraversal(final ViewGroup containerView,
                                    final TraversalState traversalState, final Flow.Direction direction,
                                    final Flow.TraversalCallback callback) {

        // old path context or null
        final PathContext oldPathContext = containerView.getChildCount() > 0 ?
                PathContext.get(containerView.getChildAt(0).getContext()) : null;

        PathContext rootPathContext = PathContext.root(containerView.getContext());
        Path newPath = traversalState.toPath();
        PathContext newPathContext = PathContext.create(newPath, rootPathContext, contextFactory);

        int layout = getLayout(newPath);
        View newView = LayoutInflater.from(newPathContext)
                .cloneInContext(newPathContext)
                .inflate(layout, containerView, false);

        View fromView = null;
        if (traversalState.fromPath() != null) {
            fromView = containerView.getChildAt(0);

            // save the state of the from view only if going forward
            if (direction == FORWARD) {
                traversalState.saveViewState(fromView);
            }
        }

        traversalState.restoreViewState(newView);

        if (fromView == null || direction == REPLACE) {
            containerView.removeAllViews();
            containerView.addView(newView);

            if (oldPathContext != null) {
                contextFactory.tearDownContext(oldPathContext);
            }

            callback.onTraversalCompleted();
        } else {
            containerView.addView(newView);
            final View finalFromView = fromView;
            PathUtils.waitForMeasure(newView, new PathUtils.OnMeasuredCallback() {
                @Override
                public void onMeasured(View view, int width, int height) {
                    runAnimation(containerView, finalFromView, view, direction, new Flow.TraversalCallback() {
                        @Override
                        public void onTraversalCompleted() {
                            containerView.removeView(finalFromView);

                            if (direction == BACKWARD) {
                                contextFactory.tearDownContext(oldPathContext);
                            }

                            callback.onTraversalCompleted();
                        }
                    });
                }
            });
        }
    }

    private void runAnimation(final ViewGroup container, final View from, final View to,
                              Flow.Direction direction, final Flow.TraversalCallback callback) {
        Animator animator = createSegue(from, to, direction);
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                container.removeView(from);
                callback.onTraversalCompleted();
            }
        });
        animator.start();
    }

    private Animator createSegue(View from, View to, Flow.Direction direction) {
        boolean backward = direction == Flow.Direction.BACKWARD;
        int fromTranslation = backward ? from.getWidth() : -from.getWidth();
        int toTranslation = backward ? -to.getWidth() : to.getWidth();

        AnimatorSet set = new AnimatorSet();

        set.play(ObjectAnimator.ofFloat(from, View.TRANSLATION_X, fromTranslation));
        set.play(ObjectAnimator.ofFloat(to, View.TRANSLATION_X, toTranslation, 0));
        set.setInterpolator(new AccelerateDecelerateInterpolator());

        return set;
    }

    protected int getLayout(Path path) {
        Class pathType = path.getClass();
        Integer layoutResId = PATH_LAYOUT_CACHE.get(pathType);
        if (layoutResId == null) {
            Layout layout = (Layout) pathType.getAnnotation(Layout.class);
            Preconditions.checkNotNull(layout, "@%s annotation not found on class %s", Layout.class.getSimpleName(), pathType.getName());
            layoutResId = layout.value();
            PATH_LAYOUT_CACHE.put(pathType, layoutResId);
        }
        return layoutResId;
    }
}