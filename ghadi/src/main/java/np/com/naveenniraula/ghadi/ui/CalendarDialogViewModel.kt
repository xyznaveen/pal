package np.com.naveenniraula.ghadi.ui

import android.os.AsyncTask
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import np.com.naveenniraula.ghadi.data.DateItem
import np.com.naveenniraula.ghadi.miti.Date
import np.com.naveenniraula.ghadi.miti.DateUtils
import np.com.naveenniraula.ghadi.ui.CalendarDialogFragment.Companion.DAYS_IN_A_WEEK
import java.util.*
import java.util.concurrent.Executors
import kotlin.collections.ArrayList

class CalendarDialogViewModel : ViewModel() {

    private val mService = Executors.newSingleThreadExecutor()
    private val calendarData: MutableLiveData<ArrayList<DateItem>> = MutableLiveData()

    fun getCalendarData(): MutableLiveData<ArrayList<DateItem>> {
        return calendarData
    }

    /**
     * Gets the calendar dates for current month.
     * @param date the date instance which can be any custom date.
     */
    fun prepareCalendarData(date: Date, markPassedDay: Boolean = false) {

        mService.submit {

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
            val nepMonthSuruVayekoEnglishDate =
                Date(nepaliDate.year, nepaliDate.month, 1).convertToEnglish()
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

                // select passed date's day by default
                if (
                    markPassedDay && date.convertToNepali().day == i
                ) {
                    model.isSelected = true
                }

                // convert to nepali and assign today's day
                // convert it back to english
                val convertedAd = date.convertToNepali().apply {
                    day = i
                }.convertToEnglish()
                convertedAd.apply {
                    model.adYear = this.year.toString()
                    model.adMonth = this.month.toString()
                    model.adDate = this.day.toString()
                }

                Log.d("DAteIIII", "actual : $date converted : $convertedAd")

                // check if the specified date is
                model.isToday = todayInBs.day == i
                        && model.year == todayInBs.year.toString()
                        && model.month == todayInBs.month.toString()

                list.add(model)

                calendarData.postValue(list)
            }
        }

    }

    /**
     * Prepare the date for english and get it.
     *
     * @param date
     * @param markPassedDay
     *
     */
    fun getAdDate(
        date: Date,
        markPassedDay: Boolean = false
    ) {
        AsyncTask.execute {
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

            val cal = date.calendar

            val firstDay = 1
            val lastDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH) + 1
            var dayStartsAt = cal.firstDayOfWeek

            val year = cal.get(Calendar.YEAR)
            val month = cal.get(Calendar.MONTH) + 1

            // pad number of days
            while (dayStartsAt >= 1) {
                val model = DateItem(date = "-1", dateEnd = "-1", month = "-1", isClickable = false)
                list.add(model)
                dayStartsAt--
            }

            for (day in firstDay until lastDay) {
                val model = DateItem("$day", "$month", "$year")
                list.add(model)
            }

            calendarData.postValue(list)

        }
    }

}
