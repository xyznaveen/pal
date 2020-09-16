package np.com.naveenniraula.ghadi.utils

import android.os.Build

class SystemUtil {

    companion object {

        fun isAtLeastLolliop(): Boolean {
            return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP
        }

    }

}