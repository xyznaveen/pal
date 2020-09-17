package np.com.naveenniraula.ghadi.ui

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import np.com.naveenniraula.ghadi.R
import np.com.naveenniraula.ghadi.data.DateItem
import np.com.naveenniraula.ghadi.miti.Date
import np.com.naveenniraula.ghadi.miti.DateUtils
import np.com.naveenniraula.ghadi.ui.CalendarDialogFragment.Companion.DAYS_IN_A_WEEK
import np.com.naveenniraula.ghadi.ui.CalendarDialogFragment.Companion.DAYS_START_NUM
import java.util.*

class NepaliDateAdapter<T> : RecyclerView.Adapter<NepaliDateAdapter.Vh>() {

    private lateinit var mBgDrawable: Drawable
    private lateinit var mBgDrawableToday: Drawable

    private var dataList: ArrayList<T> = arrayListOf()
    private lateinit var selectedDate: DateItem
    private val color = Color.parseColor("#7f8c8d")

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vh {

        if (!::mBgDrawable.isInitialized) {
            mBgDrawableToday =
                ContextCompat.getDrawable(parent.context, R.drawable.bg_circle_padding)!!
            mBgDrawableToday.setBounds(8, 8, 8, 8)

            mBgDrawable = ContextCompat.getDrawable(parent.context, R.drawable.bg_circle_padding)!!
            mBgDrawable.setBounds(8, 8, 8, 8)
        }

        val inflater = LayoutInflater.from(parent.context)
        val vh = Vh(inflater.inflate(R.layout.item_date_cell, parent, false))
        vh.root.setOnClickListener {
            val di = (dataList[vh.adapterPosition] as DateItem)
            if (di.isClickable && lastCellPosition != vh.adapterPosition) {

                // check if there were any last cells
                if (lastCellPosition != RecyclerView.NO_POSITION) {
                    try {
                        val lastCell = dataList[lastCellPosition] as DateItem
                        lastCell.isSelected = false
                        dataList[lastCellPosition]
                    } catch (ex: IndexOutOfBoundsException) {
                        // like the printed message this is non fatal.
                        Log.e(
                            "RVHEIGHT",
                            "This is a non fatal crash. I have not handled the AD / BS toggle properly."
                        )
                    }
                }

                val currentCell = dataList[vh.adapterPosition] as DateItem
                currentCell.isSelected = true
                dataList[vh.adapterPosition] = currentCell as T

                notifyItemChanged(lastCellPosition)
                notifyItemChanged(vh.adapterPosition)

                // this is the last position
                lastCellPosition = vh.adapterPosition

                // check if current cell is ad date
                if (currentCell.isAd()) {
                    currentCell.adDate = currentCell.date
                    currentCell.adYear = currentCell.year
                    currentCell.adMonth = currentCell.month
                }
                Log.i("RVHEIGHT", "$currentCell")

                selectedDate = currentCell
            }
        }
        return vh
    }

    fun adBsToggled() {
        lastCellPosition = Adapter.NO_SELECTION
    }

    fun getSelectedDate(): DateItem {

        // if the variable is not initialized no date was select
        // so, return today's date
        if (!::selectedDate.isInitialized) {
            selectedDate = DateItem.getTodayNepali().apply {
                val tempDate = Calendar.getInstance()
                adYear = tempDate.get(Calendar.YEAR).toString()
                adMonth = tempDate.get(Calendar.MONTH).inc().toString()
                adDate = tempDate.get(Calendar.DAY_OF_MONTH).toString()
            }
        }

        /*Log.i("RVHEIGHT","we have $selectedDate")

        // adjustment for AD because we have integrated both AD and BS
        if (selectedDate.isAd()) {
            val nepDate = Date(
                selectedDate.adYear.toInt(),
                selectedDate.adMonth.toInt(),
                selectedDate.adDate.toInt()
            ).convertToNepali()
            val totalDays = DateUtils.getNumDays(nepDate.year, nepDate.month)

            // copy everything from BS to AD
            selectedDate.adYear = selectedDate.year
            selectedDate.adMonth = selectedDate.month
            selectedDate.adDate = selectedDate.date

            // fill in the nepali date details
            selectedDate.year = nepDate.year.toString()
            selectedDate.month = nepDate.month.toString()
            selectedDate.date = nepDate.day.toString()
            selectedDate.dateEnd = totalDays.toString()
        }*/

        return selectedDate.apply {
            dateEnd = DateUtils.getNumDays(year.toInt(), month.toInt()).toString()
        }
    }

    private fun selectTodaysDate() {
        val today: Int = Date(Calendar.getInstance()).convertToNepali().day
        val todayPos = today + 8

        val currentCell = dataList[todayPos] as DateItem
        currentCell.isSelected = !currentCell.isSelected
        dataList[todayPos] = currentCell as T
        lastCellPosition = todayPos
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: Vh, position: Int) {
        val dt = (dataList[position] as DateItem)
        holder.adjust(dt)

        if (position >= DAYS_IN_A_WEEK && dt.date.toInt() < DAYS_START_NUM) {
            holder.test.text = EMPTY_STRING
            return
        }
    }

    fun setDataList(data: ArrayList<T>) {

        dataList.clear()
        notifyDataSetChanged()

        dataList.addAll(data)
        notifyItemRangeInserted(0, data.size)
    }

    fun addData(data: T) {
        dataList.add(data)
    }

    fun getData(position: Int): T {
        return dataList[position]
    }

    class Vh(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val root: ConstraintLayout = itemView.findViewById(R.id.idcRoot)
        val test: TextView = itemView.findViewById(R.id.tesss)
        val engDate: TextView = itemView.findViewById(R.id.english_date)

        fun adjust(di: DateItem) {

            // make clickable or un-clickable
            root.isClickable = di.isClickable

            if (di.adDate == "-1" || di.adDate == di.date) {
                engDate.visibility = View.GONE
            }

            if (di.isToday) setTodayColor()
            else setNormalColor(di)

            if (di.isHoliday) setHolidayColor()

            test.text = di.date
            engDate.text = di.adDate

            if (!di.isClickable) {
                engDate.setTextColor(Color.WHITE)
            }

        }

        private fun setNormalColor(di: DateItem) {

            if (di.isSelected) {
                root.setBackgroundResource(R.drawable.bg_circle_padding)
                test.setTextColor(Color.WHITE)
            } else {
                test.setTextColor(Color.BLACK)
                root.setBackgroundColor(Color.WHITE)
            }
        }

        private fun setTodayColor() {
            test.setTextColor(Color.WHITE)
            root.setBackgroundResource(R.drawable.bg_circle_padding)
        }

        private fun setHolidayColor() {
            test.setTextColor(Color.RED)
        }

    }

    companion object {
        private var lastCellPosition: Int = RecyclerView.NO_POSITION
        private const val EMPTY_STRING = ""
    }

}