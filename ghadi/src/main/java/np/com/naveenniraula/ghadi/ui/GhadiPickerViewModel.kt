package np.com.naveenniraula.ghadi.ui

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import np.com.naveenniraula.ghadi.data.DateItem
import np.com.naveenniraula.ghadi.miti.Date
import np.com.naveenniraula.ghadi.miti.DateUtils
import np.com.naveenniraula.ghadi.ui.GhadiPickerFragment.Companion.DAYS_IN_A_WEEK
import java.util.*

class GhadiPickerViewModel : ViewModel() {

    private val calendarData: MutableLiveData<ArrayList<DateItem>> = MutableLiveData()

    fun getCalendarData(): MutableLiveData<ArrayList<DateItem>> {
        return calendarData
    }

    /**
     * Gets the calendar dates for current month.
     * @param date the date instance which can be any custom date.
     */
    fun prepareCalendarData(date: Date) {

        val list = ArrayList<DateItem>()

        // ------------------------------------
        // add headers for days representation
        // ------------------------------------
        list.add(DateItem(DateUtils.HEADER_SUN))
        list.add(DateItem(DateUtils.HEADER_MON))
        list.add(DateItem(DateUtils.HEADER_TUE))
        list.add(DateItem(DateUtils.HEADER_WED))
        list.add(DateItem(DateUtils.HEADER_THU))
        list.add(DateItem(DateUtils.HEADER_FRI))
        list.add(DateItem(DateUtils.HEADER_SAT))

        val todayInBs = Date(Calendar.getInstance()).convertToNepali()
        // today's date in ad
        // today's date in bs
        val nepaliDate = date.convertToNepali()
        // 1 gatey in ad
        val nepMonthSuruVayekoEnglishDate = Date(nepaliDate.year, nepaliDate.month, 1).convertToEnglish()
        // number of days this month in bs
        val numberOfDaysInMonth = DateUtils.getNumDays(nepaliDate.year, nepaliDate.month)

        var saturdayIndex = 8 - nepMonthSuruVayekoEnglishDate.weekDayNum

        for (i in (2 - nepMonthSuruVayekoEnglishDate.weekDayNum)..numberOfDaysInMonth) {

            val model = DateItem("$i", "${nepaliDate.month}", "${nepaliDate.year}")

            // set the holiday of the current date
            // if holiday the date will appear in red
            model.isHoliday = if (saturdayIndex == i) {
                saturdayIndex += DAYS_IN_A_WEEK
                true
            } else false

            if (i >= 1) {
                // clickable only if the model contains actual date
                model.isClickable = true
                model.month = nepaliDate.month.toString()
                model.year = nepaliDate.year.toString()
            }

            // check if the specified date is
            model.isToday = todayInBs.day == i
                    && model.year == todayInBs.year.toString()
                    && model.month == todayInBs.month.toString()

            Log.i("BQ7CH72", "Date Today : ${model.isToday} $todayInBs $model")

            list.add(model)

            calendarData.value = list
        }
    }

}
