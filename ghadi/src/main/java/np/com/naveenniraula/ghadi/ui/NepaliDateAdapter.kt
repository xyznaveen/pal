package np.com.naveenniraula.ghadi.ui

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import np.com.naveenniraula.ghadi.R
import np.com.naveenniraula.ghadi.data.DateItem
import np.com.naveenniraula.ghadi.listeners.GhadiCellInteractionListener
import np.com.naveenniraula.ghadi.miti.Date
import np.com.naveenniraula.ghadi.miti.DateUtils
import np.com.naveenniraula.ghadi.ui.GhadiPickerFragment.Companion.DAYS_IN_A_WEEK
import np.com.naveenniraula.ghadi.ui.GhadiPickerFragment.Companion.DAYS_START_NUM
import java.lang.Exception
import java.util.*

class NepaliDateAdapter<T> : RecyclerView.Adapter<NepaliDateAdapter.Vh>() {

    private var dataList: ArrayList<T> = arrayListOf()
    private lateinit var selectedDate: DateItem
    private val color by lazy {
        Color.parseColor("#7f8c8d")
    }

    private val ghadiCellInteractionListener: GhadiCellInteractionListener =
        object : GhadiCellInteractionListener {
            override fun OnCellClicked(position: Int) {
                changeClickState(position)
                selectedDate = (dataList[position] as DateItem)
            }
        }

    private fun changeClickState(position: Int) {
        try {

            // last cell
            if (lastCellPosition != RecyclerView.NO_POSITION) {
                val lastCell = dataList[lastCellPosition] as DateItem
                lastCell.isSelected = false
                dataList[lastCellPosition] = lastCell as T
                notifyItemChanged(lastCellPosition)
            }

            // current cell
            val currentCell = dataList[position] as DateItem
            currentCell.isSelected = true
            dataList[position] = currentCell as T
            notifyItemChanged(position)

            lastCellPosition = position

        } catch (exception: Exception) {

            exception.printStackTrace()

            Log.d("NepaliDateAdapter", "We got this exception : ${exception.localizedMessage}")

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vh {

        val inflater = LayoutInflater.from(parent.context)
        return Vh(inflater.inflate(R.layout.item_date_cell, parent, false))
    }

    fun getSelectedDate(): DateItem {

        // if the variable is not initialized no date was select
        // so, return today's date
        if (!::selectedDate.isInitialized) selectedDate = DateItem.getTodayNepali()
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

        holder.root.isClickable = dt.isClickable
        holder.setGhadiCellInteractionListener(if (dt.isClickable) ghadiCellInteractionListener else null)

        if (dt.isToday) {
            // won't change today's color
            holder.root.setBackgroundColor(Color.BLACK)
            holder.test.setTextColor(Color.WHITE)
        } else {
            // if not today change color accordingly
            holder.root.setBackgroundColor(
                if (dt.isSelected) {
                    holder.test.setTextColor(Color.WHITE)
                    color
                } else {
                    holder.test.setTextColor(Color.BLACK)
                    Color.WHITE
                }
            )
            holder.engDate.setBackgroundColor(
                if (dt.isSelected) {
                    holder.test.setTextColor(Color.WHITE)
                    color
                } else {
                    holder.test.setTextColor(Color.BLACK)
                    Color.WHITE
                }
            )
        }

        if (dt.isHoliday) {
            holder.test.setTextColor(Color.RED)
        }

        if (position >= DAYS_IN_A_WEEK && dt.date.toInt() < DAYS_START_NUM) {
            holder.test.text = EMPTY_STRING
            return
        }
        holder.test.text = dt.date
        holder.engDate.text = dt.adDate
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

    class Vh(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        private lateinit var ghadiCellInteractionListener: GhadiCellInteractionListener
        val root: ConstraintLayout = itemView.findViewById(R.id.idcRoot)
        val test: TextView = itemView.findViewById(R.id.tesss)
        val engDate: TextView = itemView.findViewById(R.id.english_date)

        init {
            root.setOnClickListener(this)
        }

        fun setGhadiCellInteractionListener(ghadiCellInteractionListener: GhadiCellInteractionListener?) {
            ghadiCellInteractionListener?.let {
                this.ghadiCellInteractionListener = ghadiCellInteractionListener
            }
        }

        override fun onClick(v: View?) {
            if (
                ::ghadiCellInteractionListener.isInitialized
                && adapterPosition != RecyclerView.NO_POSITION
            ) {
                ghadiCellInteractionListener.OnCellClicked(adapterPosition)
            }
        }
    }

    companion object {
        private var lastCellPosition: Int = RecyclerView.NO_POSITION
        private const val EMPTY_STRING = ""
    }

}