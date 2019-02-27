package np.com.naveenniraula.ghadi.data

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
    var humanReadableBsDate: String = INVALID_STRING,
    var humanReadableAdDate: String = INVALID_STRING
)