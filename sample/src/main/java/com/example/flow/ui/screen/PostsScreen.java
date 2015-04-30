package com.example.flow.ui.screen;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;

import com.example.flow.R;
import com.example.flow.app.adapter.PostAdapter;
import com.example.flow.dagger.ScreenScope;
import com.example.flow.model.Post;
import com.example.flow.mortar.ComponentFactory;
import com.example.flow.rest.RestClient;
import com.example.flow.ui.activity.RootActivity;
import com.example.flow.ui.view.PostsView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import flow.Flow;
import flownavigation.common.flow.Layout;
import flownavigation.path.Path;
import mortar.ViewPresenter;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import timber.log.Timber;

/**
 * @author Lukasz Piliszczuk <lukasz.pili@gmail.com>
 */
@Layout(R.layout.screen_posts)
public class PostsScreen extends Path implements ComponentFactory<RootActivity.Component> {

    @Override
    public Object createComponent(RootActivity.Component component) {
        return DaggerPostsScreen_Component.builder()
                .component(component)
                .build();
    }

    @dagger.Component(dependencies = RootActivity.Component.class)
    @ScreenScope(Component.class)
    public interface Component extends RootActivity.Component {

        void inject(PostsView view);
    }

    @ScreenScope(Component.class)
    public static class Presenter extends ViewPresenter<PostsView> implements PostAdapter.Listener {

        private final RestClient restClient;

        private PostAdapter adapter;
        private List<Post> posts = new ArrayList<>();

        @Inject
        public Presenter(RestClient restClient) {
            this.restClient = restClient;
        }

        @Override
        protected void onLoad(Bundle savedInstanceState) {
            LinearLayoutManager layoutManager = new LinearLayoutManager(getView().getContext());
            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            getView().recyclerView.setLayoutManager(layoutManager);

            adapter = new PostAdapter(getView().getContext(), posts, this);
            getView().recyclerView.setAdapter(adapter);

            if (posts.isEmpty()) {
                load();
            }
        }

        private void load() {

            restClient.getService().getPosts(new Callback<List<Post>>() {
                @Override
                public void success(List<Post> loadedPosts, Response response) {
                    if (!hasView()) return;
                    Timber.d("Success loaded %s", loadedPosts.size());

                    posts.clear();
                    posts.addAll(loadedPosts);
                    adapter.notifyDataSetChanged();
                }

                @Override
                public void failure(RetrofitError error) {
                    if (!hasView()) return;
                    Timber.d("Failure %s", error.getMessage());
                }
            });
        }

        @Override
        protected void onSave(Bundle outState) {
            super.onSave(outState);


        }

        @Override
        public void onItemClick(int position) {
            if (!hasView()) return;

            Post post = posts.get(position);
            Flow.get(getView()).set(new ViewPostScreen(post));
        }
    }
}
