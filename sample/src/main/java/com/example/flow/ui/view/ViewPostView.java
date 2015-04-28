package com.example.flow.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.flow.R;
import com.example.flow.mortar.DaggerService;
import com.example.flow.ui.screen.ViewPostScreen;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * @author Lukasz Piliszczuk <lukasz.pili@gmail.com>
 */
public class ViewPostView extends LinearLayout {

    @InjectView(R.id.title)
    public TextView titleTextView;

    @InjectView(R.id.content)
    public TextView contentTextView;

    @Inject
    protected ViewPostScreen.Presenter presenter;

    public ViewPostView(Context context) {
        super(context);
        init(context);
    }

    public ViewPostView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ViewPostView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    protected void init(Context context) {
        ((ViewPostScreen.Component) context.getSystemService(DaggerService.SERVICE_NAME)).inject(this);
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
