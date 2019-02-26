package np.com.naveenniraula.ghadi.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import np.com.naveenniraula.ghadi.R
import np.com.naveenniraula.ghadi.data.DateItem

class NepaliDateAdapter<T> : RecyclerView.Adapter<NepaliDateAdapter.Vh>() {

    private var dataList: ArrayList<T> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vh {
        val inflater = LayoutInflater.from(parent.context)
        return Vh(inflater.inflate(R.layout.item_date_cell, parent, false))
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: Vh, position: Int) {

        val dt = (dataList[position] as DateItem).date
        if (position >= 7 && dt.toInt() < 1) {
            holder.test.text = ""
            return
        }
        holder.test.text = dt
    }

    fun setDataList(data: ArrayList<T>) {
        dataList.addAll(data)
        notifyItemRangeInserted(0, data.size)
    }

    fun addData(data: T) {
        dataList.add(data)
    }

    fun getData(position: Int): T {
        return dataList.get(position)
    }

    class Vh(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val root: ConstraintLayout = itemView.findViewById(R.id.idcRoot)
        val test: TextView = itemView.findViewById(R.id.tesss)

    }

}