package flownavigation.path;

import android.content.Context;
import android.view.ViewGroup;

import flow.Flow;

/**
 * Copy paste from flow-path
 *
 * @author lukasz piliszczuk - lukasz.pili@gmail.com
 */
public interface PathContainerView extends Flow.Dispatcher {
    ViewGroup getCurrentChild();

    ViewGroup getContainerView();

    Context getContext();

    void dispatch(Flow.Traversal traversal, Flow.TraversalCallback callback);
}