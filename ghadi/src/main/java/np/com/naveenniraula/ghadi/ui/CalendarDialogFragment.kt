package np.com.naveenniraula.ghadi.ui

import android.annotation.SuppressLint
import android.app.Dialog
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.databinding.Observable
import androidx.databinding.library.baseAdapters.BR
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import np.com.naveenniraula.ghadi.R
import np.com.naveenniraula.ghadi.data.DateItem
import np.com.naveenniraula.ghadi.data.GhadiResult
import np.com.naveenniraula.ghadi.databinding.CalendarDialogFragmentBinding
import np.com.naveenniraula.ghadi.listeners.DatePickCompleteListener
import np.com.naveenniraula.ghadi.miti.Date
import np.com.naveenniraula.ghadi.miti.DateUtils
import np.com.naveenniraula.ghadi.utils.ConversionUtil
import np.com.naveenniraula.ghadi.utils.Ui
import java.util.*

class CalendarDialogFragment : DialogFragment() {

    companion object {

        fun newInstance() = CalendarDialogFragment()

        fun newInstance(date: Date): CalendarDialogFragment {
            val ghadiPickerFragment = CalendarDialogFragment()
            ghadiPickerFragment.requestedDate = date.convertToEnglish()
            return ghadiPickerFragment
        }

        @Deprecated("Don't use this method. Pass in the Date instance instead.")
        fun newInstance(year: Int, month: Int, day: Int): CalendarDialogFragment {
            val ghadiPickerFragment = CalendarDialogFragment()
            ghadiPickerFragment.requestedDate = Date(year, month, day).convertToEnglish()
            return ghadiPickerFragment
        }

        const val DAYS_IN_A_WEEK = 7
        const val DAYS_START_NUM = 1
    }

    private lateinit var mBinding: CalendarDialogFragmentBinding

    var bgFgColor: Pair<Int, Int> = Pair(Color.BLACK, Color.WHITE)

    private val adapter = NepaliDateAdapter<DateItem>()

    private lateinit var mFragmentManager: FragmentManager

    private lateinit var requestedDate: Date
    private val viewModel: CalendarDialogViewModel by lazy {
        ViewModelProvider(this).get(CalendarDialogViewModel::class.java)
    }
    private lateinit var datePickCompleteListener: DatePickCompleteListener

    /**
     * This block of variable are for dynamically adjusting the recycler view's height each time new
     * data is prepared.
     */
    private var minHeight = 0
    private var lastHeight = 0
    private var addedHeight = 0

    private lateinit var alertDialog: AlertDialog

    private val currentDateInNepali = Date(Calendar.getInstance()).convertToNepali()

    private val listenerException =
        IllegalAccessException("DatePickCompleteListener has not been implemented. Please implement this to return result when action is completed.")

    override fun onCreate(savedInstanceState: Bundle?) {
        setStyle(STYLE_NO_TITLE, R.style.Theme_AppCompat_Dialog_Alert)
        super.onCreate(savedInstanceState)

    }

    @SuppressLint("InflateParams")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        context?.let {
            val inflater = LayoutInflater.from(it)
            mBinding = CalendarDialogFragmentBinding.inflate(inflater)

            setupNepaliDate()

            setupListeners()

            mBinding.materialButtonToggleGroup.addOnButtonCheckedListener { group, checkedId, isChecked ->
                if (isChecked) {
                    when (checkedId) {
                        R.id.btn1 -> viewModel.ui.isBs = false
                        R.id.btn2 -> viewModel.ui.isBs = true
                    }
                }
            }

            val builder = AlertDialog.Builder(it)
            builder.setView(mBinding.root)

            val width = activity?.window?.decorView?.width ?: 0

            alertDialog = builder.create()
            alertDialog.window?.setLayout(width, 400)

            return alertDialog
        } ?: return super.onCreateDialog(savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mBinding.ui = viewModel.ui
        viewModel.ui.addOnPropertyChangedCallback(object : Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                val ui = viewModel.ui
                when (propertyId) {
                    BR.bs -> {
                        if (ui.isBs) {
                            changeDate(currentDateInNepali)
                        } else {
                            changeDate(viewModel.currentEnglishDate.convertToNepali())
                        }

                        // to avoid crash when the data is less than the last index
                        adapter.adBsToggled()
                    }
                }
            }
        })

        viewModel.getCalendarData().observe(this, androidx.lifecycle.Observer {
            viewModel.ui.isCalculating = false
            adapter.setDataList(it)
            adjustRecyclerViewHeight(it.size)
        })

        val _date = if (::requestedDate.isInitialized) {
            requestedDate
        } else {
            Date(Calendar.getInstance())
        }

        if (viewModel.ui.isBs) viewModel.prepareCalendarData(_date, true)
        else viewModel.getAdDate(_date, true)
    }

    /**
     * Dynamically adjusts the height of [RecyclerView] based on the number of data prepared.
     */
    private fun adjustRecyclerViewHeight(itemCount: Int) {
        val currentHeight = mBinding.nepaliDateList.layoutManager?.height ?: 0

        if (lastHeight == 0) {
            // we do this only once after the view has been setup
            // i.e:  if the height is 0 else there is no need to execute this block
            lastHeight = currentHeight

            // should hold reference to minimum height this will not increase or decrease
            minHeight = lastHeight

            // additional row height which will be added or deducted
            addedHeight = currentHeight / 6

        }

        if (itemCount < 43) {
            // if there are 6 ( 7 * 6 = 42 ) rows do not increment more than the minimum height as it will
            // add an extra space after the last row; will look bad
            lastHeight = minHeight
        } else {
            // check if additional row height is added but must be added only once
            if ((lastHeight - addedHeight) == minHeight) {
                // do not add anymore height because we are already at max
            } else {
                // add the additional height
                lastHeight = minHeight + addedHeight
            }
        }

        // finally set the height to recycler view
        val lp = mBinding.nepaliDateList.layoutParams
        lp.height = lastHeight
        mBinding.nepaliDateList.layoutParams = lp

        val plp = mBinding.progressBarParent.layoutParams
        plp.height = lp.height
        mBinding.progressBarParent.layoutParams = plp
    }

    /**
     * Setup listeners of all available actions.
     * Year [next | prev]; Month [next | prev] and [ OK | Cancel]
     */
    private fun setupListeners() {

        // -----------------------
        // year button listeners
        // -----------------------

        mBinding.gpfYear.setTextColor(bgFgColor.first)
        viewModel.ui.bsYear =
            if (::requestedDate.isInitialized) ConversionUtil.toNepali(requestedDate.convertToNepali().year.toString())
                ?: ""
            else ConversionUtil.toNepali(currentDateInNepali.year.toString()) ?: ""

        val yPrev = getRootView().findViewById<ImageButton>(R.id.gpfPrevYear)
        val yNext = getRootView().findViewById<ImageButton>(R.id.gpfNextYear)

        yPrev.setOnClickListener {

            val yearNumber = getDisplayedYear() - 1

            if (yearNumber == DateUtils.endNepaliYear - 1) {
                Ui.tintButtonImage(yPrev, android.R.color.holo_red_dark)
            } else {
                Ui.tintButtonImage(yNext, R.color.collMat)
            }

            if (yearNumber <= DateUtils.startNepaliYear) {
                return@setOnClickListener
            }

            if (!viewModel.ui.isBs) {
                viewModel.currentEnglishDate.year -= 1
                changeDate(null)
            } else {
                val upcomingMonthNumber = DateUtils.getMonthNumber(getDisplayedMonth())
                mBinding.ui!!.bsYear = ConversionUtil.toNepali(yearNumber.toString()) ?: ""
                changeDate(Date(yearNumber, upcomingMonthNumber, 1))
            }
        }
        yNext.setOnClickListener {

            val yearNumber = getDisplayedYear() + 1

            if (yearNumber == DateUtils.endNepaliYear - 1) {
                Ui.tintButtonImage(yNext, android.R.color.holo_red_dark)
            } else {
                Ui.tintButtonImage(yPrev, R.color.collMat)
            }

            if (yearNumber >= DateUtils.endNepaliYear) {
                return@setOnClickListener
            }

            if (!viewModel.ui.isBs) {
                viewModel.currentEnglishDate.year += 1
                changeDate(null)
            } else {
                val upcomingMonthNumber = DateUtils.getMonthNumber(getDisplayedMonth())
                mBinding.ui!!.bsYear = ConversionUtil.toNepali(yearNumber.toString()) ?: ""
                changeDate(Date(yearNumber, upcomingMonthNumber, 1))
            }
        }

        // -----------------------
        // month button listeners
        // -----------------------

        mBinding.gpfMonth.setTextColor(bgFgColor.first)
        var extMonth = if (::requestedDate.isInitialized) {
            DateUtils.MONTH_NAMES_MAPPED[requestedDate.convertToNepali().month]
        } else {
            DateUtils.MONTH_NAMES_MAPPED[currentDateInNepali.month]
        }
        viewModel.ui.bsMonth = if (::requestedDate.isInitialized) {
            DateUtils.getMonthName(requestedDate.convertToNepali().month)
        } else {
            DateUtils.getMonthName(currentDateInNepali.month)
        }

        viewModel.ui.bsMonth = "${viewModel.ui.bsMonth} ( $extMonth )"

        mBinding.gpfNextMonth.setOnClickListener {
            if (viewModel.ui.isBs) {
                val nYear = getDisplayedYear()
                val upcomingMonthName = DateUtils.getNextMonthName(getDisplayedMonth())
                val upcomingMonthNumber = DateUtils.getMonthNumber(upcomingMonthName)
                viewModel.ui.bsMonth = upcomingMonthName
                changeDate(Date(nYear, upcomingMonthNumber, 1))
                extMonth = DateUtils.MONTH_NAMES_MAPPED[upcomingMonthNumber]
                viewModel.ui.bsMonth = "${viewModel.ui.bsMonth} ( $extMonth )"
            } else {
                viewModel.currentEnglishDate.month = viewModel.currentEnglishDate.month + 1
                changeDate(null)
            }
        }

        val prev = getRootView().findViewById<ImageButton>(R.id.gpfPrevMonth)
        prev.setOnClickListener {
            if (viewModel.ui.isBs) {
                val nYear = getDisplayedYear()
                val upcomingMonthName = DateUtils.getPreviousMonthName(getDisplayedMonth())
                val upcomingMonthNumber = DateUtils.getMonthNumber(upcomingMonthName)
                viewModel.ui.bsMonth = upcomingMonthName
                changeDate(Date(nYear, upcomingMonthNumber, 1))

                extMonth = DateUtils.MONTH_NAMES_MAPPED[upcomingMonthNumber]
                viewModel.ui.bsMonth = "${viewModel.ui.bsMonth} ( $extMonth )"
            } else {
                viewModel.currentEnglishDate.month = viewModel.currentEnglishDate.month - 1
                changeDate(null)
            }
        }

        // -----------------------
        // action button listeners
        // -----------------------

        val confirm = getRootView().findViewById<Button>(R.id.gpfConfirm)
        // confirm.setBackgroundColor(bgFgColor.first)
        // confirm.setTextColor(bgFgColor.second)
        confirm.setOnClickListener {

            if (!::datePickCompleteListener.isInitialized) throw listenerException

            val date = adapter.getSelectedDate()

            if (date.adYear == date.year) {
                // we have the same year meaning we will need to convert everything

                // let us start by incrementing the ad month by 1 we don't have index 0 start
                date.adMonth = "${date.adMonth.toInt().inc()}"


            }

            Log.d("jqiu7", "$date")

            val engDate = Date(date.adYear.toInt(), date.adMonth.toInt(), date.adDate.toInt())
            val weekDayNumber = engDate.weekDayNum

            val humanReadableBs = engDate.convertToNepali().readableBsDate
            val humanReadableAd = engDate.readableAdDate

            val result = GhadiResult(
                date.date.toInt(),
                date.dateEnd.toInt(),
                date.month.toInt(),
                date.year.toInt(),
                date.adDate.toInt(),
                date.adMonth.toInt(),
                date.adYear.toInt(),
                weekDayNumber,
                DateUtils.getDayName(weekDayNumber),
                humanReadableBs,
                humanReadableAd
            )

            datePickCompleteListener.onDateSelectionComplete(result)
            Log.d("BQ7CH72", result.toString())
            dismiss()
        }
        val cancel = getRootView().findViewById<Button>(R.id.gpfCancel)
        // cancel.setBackgroundColor(bgFgColor.first)
        // cancel.setTextColor(bgFgColor.second)
        cancel.setOnClickListener {
            if (!::datePickCompleteListener.isInitialized) throw listenerException
            dismiss()
        }
    }

    /**
     * Changes date based on the button click of respective entity; Month or Year.
     */
    private fun changeDate(date: Date?) {
        viewModel.ui.isCalculating = true
        Log.i("RVHEIGHT", "the date is null : ${date == null}")

        if (viewModel.ui.isBs) {
            date?.convertToEnglish()?.let {
                viewModel.prepareCalendarData(it)
            } ?: showDateUnavailable()
        } else {
            viewModel.getAdDate()
        }
    }

    private fun showDateUnavailable() {

    }

    /**
     * Prepare nepali date for current month.
     */
    private fun setupNepaliDate() {
        val recyclerView = getRootView().findViewById<RecyclerView>(R.id.nepaliDateList)
        recyclerView.setHasFixedSize(false)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = GridLayoutManager(context, DAYS_IN_A_WEEK)
    }

    fun setDatePickCompleteListener(datePickCompleteListener: DatePickCompleteListener) {
        this.datePickCompleteListener = datePickCompleteListener
    }

    /**
     * Get the root view of this fragment.
     */
    private fun getRootView(): View {
        return mBinding.root
    }

    /**
     * Get currently visible month name in the header of the calendar view.
     * @return name of the month currently visible.
     */
    private fun getDisplayedMonth(): String {
        return viewModel.ui.bsMonth.split(" ")[0]
    }

    /**
     * Get currently visible year in the header of the calendar view.
     * @return year number as [Int].
     */
    private fun getDisplayedYear(): Int {
        return viewModel.ui.bsYear.toInt()
    }

    fun setFragmentManager(mFragmentManager: FragmentManager) {
        this.mFragmentManager = mFragmentManager
    }

    fun show() {
        show(childFragmentManager, this.tag)
    }

}
