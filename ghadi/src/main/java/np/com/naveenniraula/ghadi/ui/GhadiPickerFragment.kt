package np.com.naveenniraula.ghadi.ui

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import np.com.naveenniraula.ghadi.R
import np.com.naveenniraula.ghadi.data.DateItem
import np.com.naveenniraula.ghadi.miti.Date
import np.com.naveenniraula.ghadi.miti.DateUtils
import java.util.*

class GhadiPickerFragment : DialogFragment() {

    companion object {
        fun newInstance() = GhadiPickerFragment()
    }

    private lateinit var viewModel: GhadiPickerViewModel

    private lateinit var alertDialog: AlertDialog
    private lateinit var rootView: View

    override fun onCreate(savedInstanceState: Bundle?) {
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.Theme_AppCompat_Dialog_Alert)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        context?.let {
            val inflater = LayoutInflater.from(it)
            rootView = inflater.inflate(R.layout.ghadi_picker_fragment, null, false)

            setupNepaliDate()

            val builder = AlertDialog.Builder(it)
            builder.setView(rootView)
            //builder.setPositiveButton("Done") { _, _ ->
            //    dismiss()
            //}
            //builder.setNegativeButton("Cancel") { _, _ ->
            //    dismiss()
            //}

            val height = activity?.window?.decorView?.height ?: 0
            val width = activity?.window?.decorView?.width ?: 0

            alertDialog = builder.create()
            alertDialog.window?.setLayout(width, 400)

            return alertDialog
        } ?: return super.onCreateDialog(savedInstanceState)
    }

    private fun setupNepaliDate() {

        val adapter = NepaliDateAdapter<DateItem>()
        adapter.setDataList(prepareFakeData())

        val recyclerView = getRootView().findViewById<RecyclerView>(R.id.nepaliDateList)
        recyclerView.setHasFixedSize(false)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = GridLayoutManager(context, 7)

        identifyNepaliDateStart()

    }

    private fun identifyNepaliDateStart() {


    }

    private fun prepareFakeData(): ArrayList<DateItem> {

        val date = Date(Calendar.getInstance())

        val list = ArrayList<DateItem>()
        list.add(DateItem("S"))
        list.add(DateItem("M"))
        list.add(DateItem("T"))
        list.add(DateItem("W"))
        list.add(DateItem("T"))
        list.add(DateItem("F"))
        list.add(DateItem("S"))

        // today's date in ad
        val englishDate = Date(Calendar.getInstance())
        // today's date in bs
        val nepaliDate = englishDate.convertToNepali()
        // 1 gatey in ad
        val nepMonthSuruVayekoEnglishDate = Date(nepaliDate.year, nepaliDate.month, 1).convertToEnglish()
        // number of days this month in bs
        val numberOfDaysInMonth = DateUtils.getNumDays(nepaliDate.year, nepaliDate.month)

        val daysCounter = 1 - (7 - nepMonthSuruVayekoEnglishDate.weekDayNum)

        Log.i(
            "BQ7CH72",
            "Month start on :: ${DateUtils.getDayName(nepMonthSuruVayekoEnglishDate.weekDayNum)} ${nepMonthSuruVayekoEnglishDate.weekDayNum}"
        )

        for (i in (2 - nepMonthSuruVayekoEnglishDate.weekDayNum)..numberOfDaysInMonth) {

            val model = DateItem("$i")

            if (i >= 1) {
                // clickable only if the model contains actual date
                model.isClickable = true
            }

            model.isToday = (nepaliDate.day == i)

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

}
