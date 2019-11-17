package labs.com.mdfoto.utils;

import android.app.Activity;
import android.graphics.Point;
import android.os.Build;
import android.support.annotation.NonNull;

/**
 * Created by oktay on 2/8/17.
 */

public class DisplayUtil {
    static Point point =  new Point();
    @NonNull
    public static Point getPoint(Activity context) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            context.getWindowManager().getDefaultDisplay().getRealSize(point);
        }
        return point;
    }
}
