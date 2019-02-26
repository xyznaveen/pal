package np.com.naveenniraula.ghadi.data

data class DateItem(
    val date: String,
    var isToday: Boolean = false,
    var isSelected: Boolean = false,
    var isClickable: Boolean = false
)