package np.com.naveenniraula.ghadi.ui

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import np.com.naveenniraula.ghadi.data.DateItem
import np.com.naveenniraula.ghadi.data.UiProperty
import np.com.naveenniraula.ghadi.miti.Date
import np.com.naveenniraula.ghadi.miti.DateUtils
import np.com.naveenniraula.ghadi.ui.CalendarDialogFragment.Companion.DAYS_IN_A_WEEK
import java.util.*
import java.util.concurrent.Executors
import kotlin.collections.ArrayList

class CalendarDialogViewModel : ViewModel() {

    val ui = UiProperty()

    private val mThisMonth = Calendar.getInstance()
    var currentEnglishDate: Date = Date(Calendar.getInstance()).convertToEnglish()
        private set

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

                if (model.isToday) {
                    currentEnglishDate =
                        Date(model.year.toInt(), model.month.toInt(), i).convertToEnglish()
                }

                list.add(model)

                calendarData.postValue(list)
            }
        }

    }

    private val months = arrayOf(
        "January",
        "February",
        "March",
        "April",
        "May",
        "June",
        "July",
        "August",
        "September",
        "October",
        "November",
        "December"
    )

    /**
     * Prepare the date for english and get it.
     *
     * @param date
     * @param markPassedDay
     *
     */
    fun getAdDate(
        date: Date? = null,
        markPassedDay: Boolean = false
    ) {
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

            // first get total number of days this month
            val calendar = currentEnglishDate.calendar

            val _year = calendar.get(Calendar.YEAR)

            // getting details of the month before we move any further
            val _month = calendar.get(Calendar.MONTH)
            val _maxDays = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)

            // this will be required to mark current day in the UI
            val _originallySetDayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)

            // move the date back to start of the month to find out what day was 1 on
            // eg: September 1, 2020 was on Thursday ( 3 )
            calendar.set(Calendar.DAY_OF_MONTH, 1)

            // here we will get that Thursday ( 3 ); but only 3 for the constant THURSDAY
            val _dayStartOffset = calendar.get(Calendar.DAY_OF_WEEK)

            log("we have _dayStartOffset : $_dayStartOffset")

            // pad the number of days as offset fill -1 until the actual day starts
            // but pad only if the day is greater than 1 i.e sunday ; else we will have an extra row
            // can be improved to include previous month's details; but maybe later.
            for (index in 1 until _dayStartOffset) {
                val item = DateItem.getDefault()
                list.add(item)
            }

            // today
            val thisYear = mThisMonth.get(Calendar.YEAR)
            val thisMonth = mThisMonth.get(Calendar.MONTH)
            val thisDay = mThisMonth.get(Calendar.DAY_OF_MONTH)

            // we will calculate actual date and assign it
            for (day in 1.._maxDays) {
                val item = DateItem.getDefault()

                item.date = day.toString()
                item.year = _year.toString()
                item.month = _month.toString()
                item.isClickable = true

                if ((_originallySetDayOfMonth == day)
                        .and(_year == thisYear)
                        .and(_month == thisMonth)
                ) {
                    item.isToday = true
                }

                list.add(item)
            }

            ui.adMonth = months[_month]
            ui.adYear = _year.toString()

            calendarData.postValue(list)
        }
    }

    private fun log(msg: String) {
        Log.i("RVHEIGHT", msg)
    }

}
