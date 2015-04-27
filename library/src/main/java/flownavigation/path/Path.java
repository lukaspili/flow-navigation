package flownavigation.path;

import android.content.Context;
import android.content.ContextWrapper;
import android.view.LayoutInflater;

/**
 * Inspired from flow-path
 * More simplified version of the Path class from Flow, without the List<Path> elements
 *
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
public abstract class Path {

    static final Path ROOT = new Path() {
    };

    public static PathContextFactory contextFactory() {
        return new ContextFactory();
    }

    public static PathContextFactory contextFactory(PathContextFactory delegate) {
        return new ContextFactory(delegate);
    }

    private static final class LocalPathWrapper extends ContextWrapper {
        static final String LOCAL_WRAPPER_SERVICE = "flow_local_screen_context_wrapper";
        private LayoutInflater inflater;

        final Object localScreen;

        LocalPathWrapper(Context base, Object localScreen) {
            super(base);
            this.localScreen = localScreen;
        }

        @Override
        public Object getSystemService(String name) {
            if (LOCAL_WRAPPER_SERVICE.equals(name)) {
                return this;
            }
            if (LAYOUT_INFLATER_SERVICE.equals(name)) {
                if (inflater == null) {
                    inflater = LayoutInflater.from(getBaseContext()).cloneInContext(this);
                }
                return inflater;
            }
            return super.getSystemService(name);
        }
    }

    private static final class ContextFactory implements PathContextFactory {
        // May be null.
        private final PathContextFactory delegate;

        public ContextFactory() {
            delegate = null;
        }

        public ContextFactory(PathContextFactory delegate) {
            this.delegate = delegate;
        }

        @Override
        public Context setUpContext(Path path, Context parentContext) {
            if (delegate != null) {
                parentContext = delegate.setUpContext(path, parentContext);
            }
            return new LocalPathWrapper(parentContext, path);
        }

        @Override
        public void tearDownContext(Context context) {
            if (delegate != null) {
                delegate.tearDownContext(context);
            }
        }
    }
}
