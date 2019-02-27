package np.com.naveenniraula.ghadi.utils

import android.os.Build

class System {

    companion object {

        fun isAtLeastLolliop(): Boolean {
            return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP
        }

    }

}