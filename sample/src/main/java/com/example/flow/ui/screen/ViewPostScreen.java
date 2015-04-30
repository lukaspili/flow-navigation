package com.example.flow.ui.screen;

import android.os.Bundle;

import com.example.flow.R;
import com.example.flow.dagger.ScreenScope;
import com.example.flow.model.Post;
import com.example.flow.mortar.ComponentFactory;
import com.example.flow.ui.activity.RootActivity;
import com.example.flow.ui.view.ViewPostView;

import javax.inject.Inject;

import dagger.Provides;
import flownavigation.common.flow.Layout;
import flownavigation.path.Path;
import mortar.ViewPresenter;

/**
 * @author Lukasz Piliszczuk <lukasz.pili@gmail.com>
 */
@Layout(R.layout.screen_view_post)
public class ViewPostScreen extends Path implements ComponentFactory<RootActivity.Component> {

    private Post post;

    public ViewPostScreen(Post post) {
        this.post = post;
    }

    @Override
    public Object createComponent(RootActivity.Component component) {
        return DaggerViewPostScreen_Component.builder()
                .component(component)
                .module(new Module())
                .build();
    }

    @dagger.Component(dependencies = RootActivity.Component.class, modules = Module.class)
    @ScreenScope(Component.class)
    public interface Component extends RootActivity.Component {

        void inject(ViewPostView view);
    }

    public static class Presenter extends ViewPresenter<ViewPostView> {

        private final Post post;

        @Inject
        public Presenter(Post post) {
            this.post = post;
        }

        @Override
        protected void onLoad(Bundle savedInstanceState) {
            getView().titleTextView.setText(post.getTitle());
            getView().contentTextView.setText(post.getBody());
        }

    }

    @dagger.Module
    public class Module {

        @Provides
        @ScreenScope(Component.class)
        public Presenter providePresenter() {
            return new Presenter(post);
        }
    }
}
