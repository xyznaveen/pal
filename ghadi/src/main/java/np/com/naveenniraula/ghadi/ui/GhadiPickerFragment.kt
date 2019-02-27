package np.com.naveenniraula.ghadi.ui

import android.app.Dialog
import android.content.res.ColorStateList
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import np.com.naveenniraula.ghadi.R
import np.com.naveenniraula.ghadi.data.DateItem
import np.com.naveenniraula.ghadi.data.GhadiResult
import np.com.naveenniraula.ghadi.listeners.GhadiInteractionCompleteListener
import np.com.naveenniraula.ghadi.miti.Date
import np.com.naveenniraula.ghadi.miti.DateUtils
import np.com.naveenniraula.ghadi.utils.System
import java.util.*

class GhadiPickerFragment : DialogFragment() {

    companion object {
        fun newInstance() = GhadiPickerFragment()
    }

    private lateinit var viewModel: GhadiPickerViewModel
    private val adapter = NepaliDateAdapter<DateItem>()
    private lateinit var ghadiInteractionCompleteListener: GhadiInteractionCompleteListener

    private lateinit var alertDialog: AlertDialog
    private lateinit var rootView: View

    private val currentDateInNepali = Date(Calendar.getInstance()).convertToNepali()

    private val listenerException =
        IllegalAccessException("GhadiInteractionCompleteListener has not been implemented. Please implement this to return result when action is completed.")

    override fun onCreate(savedInstanceState: Bundle?) {
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.Theme_AppCompat_Dialog_Alert)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        context?.let {
            val inflater = LayoutInflater.from(it)
            rootView = inflater.inflate(R.layout.ghadi_picker_fragment, null, false)

            setupNepaliDate()

            setupActionButtonListeners()
            setupMonthButtonListeners()
            setupYearButtonListeners()

            val builder = AlertDialog.Builder(it)
            builder.setView(rootView)

            val width = activity?.window?.decorView?.width ?: 0

            alertDialog = builder.create()
            alertDialog.window?.setLayout(width, 400)

            return alertDialog
        } ?: return super.onCreateDialog(savedInstanceState)
    }

    private fun setupYearButtonListeners() {

        val year = getRootView().findViewById<TextView>(R.id.gpfYear)
        year.text = currentDateInNepali.year.toString()

        val prev = getRootView().findViewById<ImageButton>(R.id.gpfPrevYear)
        val next = getRootView().findViewById<ImageButton>(R.id.gpfNextYear)

        prev.setOnClickListener {

            val yearNumber = getDisplayedYear() - 1

            if (yearNumber == DateUtils.endNepaliYear - 1) {
                tintButtonImage(prev, android.R.color.holo_red_dark)
            } else {
                tintButtonImage(next, android.R.color.white)
            }

            if (yearNumber <= DateUtils.startNepaliYear) {
                return@setOnClickListener
            }

            val upcomingMonthNumber = DateUtils.getMonthNumber(getDisplayedMonth())
            year.text = yearNumber.toString()
            updateAdapter(Date(yearNumber, upcomingMonthNumber, 1).convertToEnglish())
        }

        next.setOnClickListener {

            val yearNumber = getDisplayedYear() + 1

            if (yearNumber == DateUtils.endNepaliYear - 1) {
                tintButtonImage(next, android.R.color.holo_red_dark)
            } else {
                tintButtonImage(prev, android.R.color.white)
            }

            if (yearNumber >= DateUtils.endNepaliYear) {
                return@setOnClickListener
            }

            val upcomingMonthNumber = DateUtils.getMonthNumber(getDisplayedMonth())
            year.text = yearNumber.toString()

            updateAdapter(Date(yearNumber, upcomingMonthNumber, 1))
        }
    }

    fun tintButtonImage(btn: ImageButton, colorId: Int) {
        if (System.isAtLeastLolliop()) {
            btn.imageTintList = ColorStateList.valueOf(resources.getColor(colorId))
        }
    }

    private fun updateAdapter(date: Date?) {
        date?.convertToEnglish()?.let {
            adapter.setDataList(prepareFakeData(it))
        } ?: showDateUnavailable()
    }

    private fun showDateUnavailable() {

    }

    private fun setupMonthButtonListeners() {

        val month = getRootView().findViewById<TextView>(R.id.gpfMonth)
        month.text = DateUtils.getMonthName(currentDateInNepali.month)

        val next = getRootView().findViewById<ImageButton>(R.id.gpfNextMonth)
        next.setOnClickListener {

            val year = getDisplayedYear()
            val upcomingMonthName = DateUtils.getNextMonthName(getDisplayedMonth())
            val upcomingMonthNumber = DateUtils.getMonthNumber(upcomingMonthName)
            month.text = upcomingMonthName
            updateAdapter(Date(year, upcomingMonthNumber, 1).convertToEnglish())
        }

        val prev = getRootView().findViewById<ImageButton>(R.id.gpfPrevMonth)
        prev.setOnClickListener {

            val year = getDisplayedYear()
            val upcomingMonthName = DateUtils.getPreviousMonthName(getDisplayedMonth())
            val upcomingMonthNumber = DateUtils.getMonthNumber(upcomingMonthName)
            month.text = upcomingMonthName
            updateAdapter(Date(year, upcomingMonthNumber, 1).convertToEnglish())
        }
    }

    private fun setupActionButtonListeners() {

        val confirm = getRootView().findViewById<Button>(R.id.gpfConfirm)
        confirm.setOnClickListener {

            if (!::ghadiInteractionCompleteListener.isInitialized) throw listenerException

            val date = adapter.getSelectedDate()
            val engDate = Date(date.year.toInt(), date.month.toInt(), date.date.toInt()).convertToEnglish()
            val weekDayNumber = engDate.weekDayNum

            Log.i("BQ7CH72", "DATE :: $engDate")

            val humanReadableBs = engDate.convertToNepali().readableBsDate
            val humanReadableAd = engDate.readableAdDate

            val result = GhadiResult(
                date.date.toInt(),
                date.month.toInt(),
                date.year.toInt(),
                engDate.day,
                engDate.month,
                engDate.year,
                weekDayNumber,
                DateUtils.getDayName(weekDayNumber),
                humanReadableBs,
                humanReadableAd
            )

            ghadiInteractionCompleteListener.onDateSelectionComplete(result)
            dismiss()
        }
        val cancel = getRootView().findViewById<Button>(R.id.gpfCancel)
        cancel.setOnClickListener {

            if (!::ghadiInteractionCompleteListener.isInitialized) throw listenerException
            dismiss()
        }
    }

    private fun setupNepaliDate() {
        val recyclerView = getRootView().findViewById<RecyclerView>(R.id.nepaliDateList)
        recyclerView.setHasFixedSize(false)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = GridLayoutManager(context, 7)
        adapter.setDataList(prepareFakeData())
    }

    fun setGhadiInteractionCompleteListener(ghadiInteractionCompleteListener: GhadiInteractionCompleteListener) {
        this.ghadiInteractionCompleteListener = ghadiInteractionCompleteListener
    }

    private fun prepareFakeData(date: Date = Date(Calendar.getInstance())): ArrayList<DateItem> {

        val list = ArrayList<DateItem>()
        list.add(DateItem("S"))
        list.add(DateItem("M"))
        list.add(DateItem("T"))
        list.add(DateItem("W"))
        list.add(DateItem("T"))
        list.add(DateItem("F"))
        list.add(DateItem("S"))

        val todayInBs = Date(Calendar.getInstance()).convertToNepali()
        // today's date in ad
        val englishDate = date
        // today's date in bs
        val nepaliDate = englishDate.convertToNepali()
        // 1 gatey in ad
        val nepMonthSuruVayekoEnglishDate = Date(nepaliDate.year, nepaliDate.month, 1).convertToEnglish()
        // number of days this month in bs
        val numberOfDaysInMonth = DateUtils.getNumDays(nepaliDate.year, nepaliDate.month)

        var saturdayIndex = 8 - nepMonthSuruVayekoEnglishDate.weekDayNum

        for (i in (2 - nepMonthSuruVayekoEnglishDate.weekDayNum)..numberOfDaysInMonth) {

            val model = DateItem("$i", "${nepaliDate.month}", "${nepaliDate.year}")

            model.isHoliday = if (saturdayIndex == i) {
                saturdayIndex += 7
                true
            } else false

            if (i >= 1) {
                // clickable only if the model contains actual date
                model.isClickable = true
                model.month = nepaliDate.month.toString()
                model.year = nepaliDate.year.toString()
            }

            model.isToday = todayInBs.day == i
                    && model.year == todayInBs.year.toString()
                    && model.month == todayInBs.month.toString()

            list.add(model)
        }

        return list
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(GhadiPickerViewModel::class.java)
    }

    fun getRootView(): View {
        return rootView
    }

    fun getDisplayedMonth(): String {
        val month = getRootView().findViewById<TextView>(R.id.gpfMonth)
        return month.text.toString()
    }

    fun getDisplayedYear(): Int {
        val year = getRootView().findViewById<TextView>(R.id.gpfYear)
        return year.text.toString().toInt()
    }

}
