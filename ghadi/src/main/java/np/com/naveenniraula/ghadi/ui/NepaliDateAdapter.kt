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
import java.util.*

class NepaliDateAdapter<T> : RecyclerView.Adapter<NepaliDateAdapter.Vh>() {

    private var dataList: ArrayList<T> = arrayListOf()
    private lateinit var selectedDate: DateItem

    private val ghadiCellInteractionListener: GhadiCellInteractionListener = object : GhadiCellInteractionListener {
        override fun OnCellClicked(position: Int) {
            changeClickState(position)
            selectedDate = dataList[position] as DateItem
        }
    }

    private fun changeClickState(position: Int) {

        // last cell
        if (NepaliDateAdapter.lastCellPosition != RecyclerView.NO_POSITION) {
            val lastCell = dataList[NepaliDateAdapter.lastCellPosition] as DateItem
            lastCell.isSelected = false
            dataList[NepaliDateAdapter.lastCellPosition] = lastCell as T
            notifyItemChanged(NepaliDateAdapter.lastCellPosition)
        }

        // current cell
        val currentCell = dataList[position] as DateItem
        currentCell.isSelected = true
        dataList[position] = currentCell as T
        notifyItemChanged(position)

        NepaliDateAdapter.lastCellPosition = position
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vh {

        val inflater = LayoutInflater.from(parent.context)
        return Vh(inflater.inflate(R.layout.item_date_cell, parent, false))
    }

    fun getSelectedDate(): DateItem {

        if (!::selectedDate.isInitialized) {
            dataList.forEach {
                val item = it as DateItem
                if (item.isToday) {
                    selectedDate = item
                }
            }
        }

        return selectedDate
    }

    private fun selectTodaysDate() {
        val today: Int = Date(Calendar.getInstance()).convertToNepali().day
        val todayPos = today + 8

        val currentCell = dataList[todayPos] as DateItem
        currentCell.isSelected = !currentCell.isSelected
        dataList[todayPos] = currentCell as T
        NepaliDateAdapter.lastCellPosition = todayPos
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
                    Color.parseColor("#7f8c8d")
                } else {
                    holder.test.setTextColor(Color.BLACK)
                    Color.WHITE
                }
            )
        }

        if (dt.isHoliday) {
            holder.test.setTextColor(Color.RED)
        }

        if (position >= 7 && dt.date.toInt() < 1) {
            holder.test.text = ""
            return
        }
        holder.test.text = dt.date
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
    }

}