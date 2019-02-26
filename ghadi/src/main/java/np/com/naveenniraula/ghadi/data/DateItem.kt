package np.com.naveenniraula.ghadi.data

data class DateItem(
    val date: String,
    var month: String = "",
    var year: String = "",
    var isToday: Boolean = false,
    var isSelected: Boolean = false,
    var isClickable: Boolean = false
)