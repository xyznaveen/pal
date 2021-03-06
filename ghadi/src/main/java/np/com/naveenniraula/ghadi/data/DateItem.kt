package np.com.naveenniraula.ghadi.data

import np.com.naveenniraula.ghadi.miti.Date
import java.util.*

data class DateItem(
    var date: String,
    var dateEnd: String = "",
    var month: String = "",
    var year: String = "",
    var adDate: String = "",
    var adMonth: String = "",
    var adYear: String = "",
    var isToday: Boolean = false,
    var isSelected: Boolean = false,
    var isClickable: Boolean = false,
    var isHoliday: Boolean = false
) {

    fun isAd(): Boolean {
        return adDate == "-1" && adMonth == "-1" && adYear == "-1"
    }

    override fun toString(): String {
        return String.format(
            "{ date: %s, dateEnd: %s, month: %s, year: %s, adDate: %s, adMonth: %s, adYear: %s, isToday: %s, isSelected: %s, isClickable: %s, isHoliday: %s }",
            date,
            dateEnd,
            month,
            year,
            adDate,
            adMonth,
            adYear,
            isToday,
            isSelected,
            isClickable,
            isHoliday
        )
    }

    companion object {
        fun getTodayNepali(): DateItem {
            val cal = Calendar.getInstance()
            val today = Date(
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH) + 1,
                cal.get(Calendar.DAY_OF_MONTH)
            ).convertToNepali()
            return DateItem(
                date = "${today.day}",
                month = "${today.month}",
                year = "${today.year}",
                isClickable = false,
                isSelected = true,
                isHoliday = false,
                isToday = true
            )
        }

        fun getDefault(): DateItem {
            return DateItem(
                date = "-1",
                dateEnd = "-1",
                month = "-1",
                year = "-1",
                adDate = "-1",
                adMonth = "-1",
                adYear = "-1",
                isToday = false,
                isSelected = false,
                isClickable = false,
                isHoliday = false
            )
        }
    }

}