package flownavigation.path;

import android.view.View;
import android.view.ViewGroup;

import java.util.LinkedHashMap;
import java.util.Map;

import flow.Flow;
import flow.ViewState;

/**
 * Handles swapping paths within a container view, as well as flow mechanics, allowing supported
 * container views to be largely declarative.
 * <p/>
 * Copy paste from flow-path
 *
 * @author lukasz piliszczuk - lukasz.pili@gmail.com
 */
public abstract class PathContainer {

    /**
     * Provides information about the current or most recent Traversal handled by the container.
     */
    protected static final class TraversalState {
        private Path fromPath;
        private ViewState fromViewState;
        private Path toPath;
        private ViewState toViewState;

        public void setNextEntry(Path path, ViewState viewState) {
            this.fromPath = this.toPath;
            this.fromViewState = this.toViewState;
            this.toPath = path;
            this.toViewState = viewState;
        }

        public Path fromPath() {
            return fromPath;
        }

        public Path toPath() {
            return toPath;
        }

        public void saveViewState(View view) {
            fromViewState.save(view);
        }

        public void restoreViewState(View view) {
            toViewState.restore(view);
        }
    }

    private static final Map<Class, Integer> PATH_LAYOUT_CACHE = new LinkedHashMap<>();

    private final int tagKey;

    protected PathContainer(int tagKey) {
        this.tagKey = tagKey;
    }

    public final void executeTraversal(PathContainerView view, Flow.Traversal traversal,
                                       final Flow.TraversalCallback callback) {

        final View oldChild = view.getCurrentChild();

        Path path = traversal.destination.top();
        ViewState viewState = traversal.destination.currentViewState();

        Path oldPath;
        ViewGroup containerView = view.getContainerView();
        TraversalState traversalState = ensureTag(containerView);

        // See if we already have the direct child we want, and if so short circuit the traversal.
        if (oldChild != null) {
            oldPath = Preconditions.checkNotNull(traversalState.toPath, "Container view has child %s with no path", oldChild.toString());
            if (oldPath.equals(path)) {
                callback.onTraversalCompleted();
                return;
            }
        }

        traversalState.setNextEntry(path, viewState);
        performTraversal(containerView, traversalState, traversal.direction, callback);
    }

    protected abstract void performTraversal(ViewGroup container, TraversalState traversalState,
                                             Flow.Direction direction, Flow.TraversalCallback callback);

    private TraversalState ensureTag(ViewGroup container) {
        TraversalState traversalState = (TraversalState) container.getTag(tagKey);
        if (traversalState == null) {
            traversalState = new TraversalState();
            container.setTag(tagKey, traversalState);
        }
        return traversalState;
    }
}