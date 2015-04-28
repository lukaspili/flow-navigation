package com.example.flow.ui.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.example.flow.R;
import com.example.flow.mortar.DaggerService;
import com.example.flow.ui.screen.PostsScreen;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * @author Lukasz Piliszczuk <lukasz.pili@gmail.com>
 */
public class PostsView extends FrameLayout {

    @InjectView(R.id.recycler_view)
    public RecyclerView recyclerView;

    @Inject
    protected PostsScreen.Presenter presenter;

    public PostsView(Context context) {
        super(context);
        init(context);
    }

    public PostsView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public PostsView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    protected void init(Context context) {
        ((PostsScreen.Component) context.getSystemService(DaggerService.SERVICE_NAME)).inject(this);
    }

    @Override
    public void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.inject(this);
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        presenter.takeView(this);
    }

    @Override
    public void onDetachedFromWindow() {
        presenter.dropView(this);
        super.onDetachedFromWindow();
    }
}
