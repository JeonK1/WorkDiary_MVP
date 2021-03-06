package com.example.workdiary.Adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.workdiary.Adapter.Contract.WorkAdapterContract
import com.example.workdiary.R
import com.example.workdiary.Model.WorkInfo
import com.example.workdiary.SQLite.DBManager
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class WorkAdapter(val items:ArrayList<WorkInfo>): RecyclerView.Adapter<WorkAdapter.MyViewHolder>(), WorkAdapterContract.View, WorkAdapterContract.Model {

    interface  OnItemClickListener{
        fun OnItemClick(holder: MyViewHolder, view: View, position: Int)
    }
    interface OnDelBtnClickListener{
        fun OnDeleteBtnClick(holder: MyViewHolder, view: View, position: Int)
    }
    interface OnOKBtnClickListener{
        fun OnOkBtnClick(holder: MyViewHolder, view: View, position: Int)
    }

    var itemClickListener: OnItemClickListener?=null
    var delBtnClickListener: OnDelBtnClickListener?=null
    var okBtnClickListener: OnOKBtnClickListener?= null

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var title: TextView = itemView.findViewById(R.id.tv_itemwork_title)
        var setName: TextView = itemView.findViewById(R.id.tv_itemwork_set)
        var date: TextView = itemView.findViewById(R.id.tv_itemwork_date)
        var dday: TextView = itemView.findViewById(R.id.tv_itemwork_dday)
        var workStartNEnd: TextView = itemView.findViewById(R.id.tv_itemwork_startNend)
        var workTime: TextView = itemView.findViewById(R.id.tv_itemwork_workTime)
        var wrapper: LinearLayout = itemView.findViewById(R.id.ll_itemwork_wrapper)
        var deleteBtn: TextView = itemView.findViewById(R.id.tv_itemwork_delete)
        var okBtn: TextView = itemView.findViewById(R.id.tv_itemwork_okBtn)

        init {
            itemView.setOnClickListener {
                itemClickListener?.OnItemClick(this, it, adapterPosition)
            }
            deleteBtn.setOnClickListener {
                delBtnClickListener?.OnDeleteBtnClick(this, it, adapterPosition)
            }
            okBtn.setOnClickListener {
                okBtnClickListener?.OnOkBtnClick(this, it, adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_work, parent, false)
        return MyViewHolder(v)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        //dday 구하기
        val sf = SimpleDateFormat("yyyy/MM/dd")
        val today = Calendar.getInstance()
        val workDate = sf.parse(items[position].workDate)!!
        val workDday = if(today.time.time < workDate.time) "D-"+((workDate.time - today.time.time) / (60*60*24*1000)).toString() else "기한이 지났습니다"

        //노동시간 구하기
        val startTimeHour = items[position].workStartTime.split(":")[0].toInt()
        val startTimeMin = items[position].workStartTime.split(":")[1].toInt()
        val startTimeStamp = startTimeHour*60 +startTimeMin
        val endTimeHour = items[position].workEndTime.split(":")[0].toInt()
        val endTimeMin = items[position].workEndTime.split(":")[1].toInt()
        val endTimeStamp = endTimeHour*60 + endTimeMin

        val workTimeHour = (endTimeStamp-startTimeStamp)/60
        val workTimeMin = (endTimeStamp-startTimeStamp)%60

        //xml에 적용하기
        holder.title.text = items[position].workTitle
        holder.setName.text = items[position].workSetName
        holder.date.text = """${items[position].workDate.split("/")[1]}월 ${items[position].workDate.split("/")[2]}일"""
        holder.dday.text = workDday
        holder.workStartNEnd.text = """${"%02d".format(startTimeHour)}:${"%02d".format(startTimeMin)} ~ ${"%02d".format(endTimeHour)}:${"%02d".format(endTimeMin)}"""
        holder.workTime.text = """( ${workTimeHour}h ${"%02d".format(workTimeMin)}m )"""
    }

    /*** WorkAdapterContract.View ***/
    override fun notifyAdapter() {
        notifyDataSetChanged()
    }

    /*** WorkAdapterContract.Model ***/
    override fun getItems(position: Int): WorkInfo {
        return items[position]
    }

    override fun deleteItems(position: Int) {
        items.removeAt(position)
    }

    override fun addItems(workInfo: WorkInfo) {
        items.add(workInfo)
    }
}