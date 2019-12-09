package np.com.naveenniraula.ghadi

import android.graphics.Color
import androidx.fragment.app.FragmentManager
import np.com.naveenniraula.ghadi.listeners.DatePickCompleteListener
import np.com.naveenniraula.ghadi.miti.Date
import np.com.naveenniraula.ghadi.ui.GhadiPickerFragment
import java.util.*

abstract class Ghadi {

    class Builder(private val fragmentManager: FragmentManager) {

        private lateinit var date: Date
        private lateinit var datePickCompleteListener: DatePickCompleteListener
        private var bg: Int = Color.BLACK
        private var fg: Int = Color.WHITE
        private var bgFgColor: Pair<Int, Int> = Pair(bg, fg)

        fun fromNepali(year: Int, month: Int, day: Int): Builder {
            date = Date(year, month, day)
            return this
        }

        fun fromEnglish(year: Int, month: Int, day: Int): Builder {
            date = Date(year, month, day).convertToNepali()
            return this
        }

        fun fromEnglish(timestamp: Long): Builder {

            val cal = Calendar.getInstance().apply {
                timeInMillis = timestamp
            }

            return fromEnglish(
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH).inc(),
                cal.get(Calendar.DAY_OF_MONTH)
            )
        }

        fun setBackgroundColor(color: Int): Builder {
            bg = color
            bgFgColor = Pair(bg, fg)
            return this
        }

        fun setForegroundColor(color: Int): Builder {
            fg = color
            bgFgColor = Pair(bg, fg)
            return this
        }

        fun withCallback(datePickCompleteListener: DatePickCompleteListener): Builder {
            this.datePickCompleteListener = datePickCompleteListener
            return this
        }

        fun build(): GhadiPickerFragment {
            val ghadiPickerFragment: GhadiPickerFragment =
                GhadiPickerFragment.newInstance()
            ghadiPickerFragment.bgFgColor = bgFgColor
            ghadiPickerFragment.setDatePickCompleteListener(datePickCompleteListener)
            ghadiPickerFragment.setFragmentManager(fragmentManager)
            return ghadiPickerFragment
        }

    }

}