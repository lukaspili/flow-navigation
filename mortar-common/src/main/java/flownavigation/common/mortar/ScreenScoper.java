package flownavigation.common.mortar;

import android.content.Context;

import flownavigation.path.Path;
import mortar.MortarScope;

/**
 * @author Lukasz Piliszczuk <lukasz.pili@gmail.com>
 */
public interface ScreenScoper {

    MortarScope getScreenScope(Context context, String name, Path path);
}
