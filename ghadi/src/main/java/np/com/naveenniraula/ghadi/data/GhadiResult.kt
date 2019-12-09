package np.com.naveenniraula.ghadi.data

import np.com.naveenniraula.ghadi.miti.Date
import java.util.*

const val INVALID_DATE = -21
const val INVALID_STRING = "GhadiResult.INVALID"

data class GhadiResult(
    var bsDay: Int = INVALID_DATE,
    var bsMonth: Int = INVALID_DATE,
    var bsYear: Int = INVALID_DATE,

    var adDay: Int = INVALID_DATE,
    var adMonth: Int = INVALID_DATE,
    var adYear: Int = INVALID_DATE,

    var dayNumber: Int = INVALID_DATE,
    var dayName: String = INVALID_STRING,

    var humanReadableBs: String = INVALID_STRING,
    var humanReadableAd: String = INVALID_STRING
) {

    fun getTimestamp(): Long {

        val calendar = Calendar.getInstance().apply {
            timeInMillis = 0
            set(Calendar.YEAR, adYear)
            set(Calendar.MONTH, adMonth - 1)
            set(Calendar.DAY_OF_MONTH, adDay)
        }

        return calendar.timeInMillis
    }

    fun getNepaliDate(): Date {
        return Date(bsYear, bsMonth, bsDay)
    }

    fun getEnglishDate(): Date {
        return Date(bsYear, bsMonth, bsDay).convertToEnglish()
    }

}