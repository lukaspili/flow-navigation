package flownavigation.common.mortar;

import android.content.Context;
import android.util.Log;

import flownavigation.path.Path;
import mortar.MortarScope;

/**
 * @author Lukasz Piliszczuk - lukasz.pili@gmail.com
 */
public abstract class BasicScreenScoper implements ScreenScoper {

    public MortarScope getScreenScope(Context context, String name, Path path) {
        MortarScope parentScope = MortarScope.getScope(context);
        Log.d(getClass().getCanonicalName(), "ScreenScoper - Screen scoper with parent " + parentScope.getName());

        MortarScope childScope = parentScope.findChild(name);
        if (childScope != null) {
            Log.d(getClass().getCanonicalName(), "ScreenScoper - Screen scoper returns existing scope " + name);
            return childScope;
        }

        MortarScope.Builder builder = parentScope.buildChild();
        configureMortarScope(context, name, path, parentScope, builder);

        Log.d(getClass().getCanonicalName(), "ScreenScoper - Screen scoper builds and returns new scope " + name);
        return builder.build(name);
    }

    protected abstract void configureMortarScope(Context context, String name, Path path, MortarScope parentScope, MortarScope.Builder mortarScopeBuilder);
}