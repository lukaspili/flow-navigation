package flownavigation.path;

import android.content.Context;

/**
 * Copy paste from flow-path
 *
 * @author lukasz piliszczuk - lukasz.pili@gmail.com
 */
public interface PathContextFactory {
    /**
     * Set up any services defined by this screen, and make them accessible via the context.
     * Typically this means returning a new context that wraps the given one.
     */
    Context setUpContext(Path path, Context parentContext);

    /**
     * Tear down any services previously started by {@link #setUpContext(Path, Context)}. Note that
     * the Context instance given here may be a wrapper around an instance that this factory
     * created.
     */
    void tearDownContext(Context context);
}