package np.com.naveenniraula.ghadi.utils

import android.content.res.ColorStateList
import android.widget.ImageButton

class Ui {

    companion object {
        /**
         * For devices greater than LOLLIPOP only.
         * Changes the color of the image button's image.
         */
        fun tintButtonImage(btn: ImageButton, colorId: Int) {
            if (SystemUtil.isAtLeastLolliop()) {
                btn.imageTintList = ColorStateList.valueOf(btn.context.resources.getColor(colorId))
            }
        }
    }
}