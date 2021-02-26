package com.example.workdiary.Fragment

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.workdiary.Activity.AddWorkActivity
import com.example.workdiary.Activity.MainActivity
import com.example.workdiary.Adapter.WorkAdapter
import com.example.workdiary.R
import com.example.workdiary.SQLite.DBManager
import kotlinx.android.synthetic.main.dialog_box.view.*
import kotlinx.android.synthetic.main.fragment_work.*
import java.util.*

/**
 * A simple [Fragment] subclass.
 */
class WorkFragment : Fragment() {

    lateinit var workRecyclerView: RecyclerView
    lateinit var workAdapter: WorkAdapter

    //for dialog
    val DAY_OF_WEEK: ArrayList<String> = arrayListOf("", "일", "월", "화", "수", "목", "금", "토")
    lateinit var dateSetListener: DatePickerDialog.OnDateSetListener
    lateinit var startTimeSetListener: TimePickerDialog.OnTimeSetListener
    lateinit var endTimeSetListener: TimePickerDialog.OnTimeSetListener
    lateinit var workYear: String
    lateinit var workMonth: String
    lateinit var workDay: String

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        var root = inflater.inflate(R.layout.fragment_work, container, false)
        workRecyclerView = root.findViewById(R.id.rv_work_recyclerView)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerViewInit()
    }

    private fun textViewInit() {
        if(workAdapter.items.size!=0) {
            activity!!.findViewById<TextView>(R.id.tv_main_comment).text = "새로운 노동일정을 추가해주세요"
            activity!!.findViewById<TextView>(R.id.tv_main_comment).visibility = View.INVISIBLE
        } else {
            activity!!.findViewById<TextView>(R.id.tv_main_comment).text = "새로운 노동일정을 추가해주세요"
            activity!!.findViewById<TextView>(R.id.tv_main_comment).visibility = View.VISIBLE
        }
    }

    override fun onResume() {
        super.onResume()
        recyclerViewInit()
    }

    private fun recyclerViewInit() {
        //dummy data
        val dbManager = DBManager(context!!)
        val workList = dbManager.getWorkAll()
        workRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        workAdapter = WorkAdapter(workList)
        workAdapter.itemClickListener = object : WorkAdapter.OnItemClickListener {
            override fun OnItemClick(
                holder: WorkAdapter.MyViewHolder,
                view: View,
                position: Int
            ) {
                // 클릭하면 삭제/확인 버튼 show/hide 하기
                val btnLayout = view.findViewById<LinearLayout>(R.id.ll_itemwork_btnlayout)
                if(btnLayout.visibility == View.VISIBLE){
                    btnLayout.visibility = View.GONE
                } else {
                    btnLayout.visibility = View.VISIBLE
                }
            }
        }
        workAdapter.delBtnClickListener = object : WorkAdapter.OnDelBtnClickListener{
            override fun OnDeleteBtnClick(
                holder: WorkAdapter.MyViewHolder,
                view: View,
                position: Int
            ) {
                // DialogView 생성
                val mDialogView = LayoutInflater.from(this@WorkFragment.context!!).inflate(R.layout.dialog_box, null)
                val mBuilder = AlertDialog.Builder(this@WorkFragment.context!!).setView(mDialogView)
                val mAlertDialog = mBuilder.show()
                mAlertDialog.window!!.setGravity(Gravity.CENTER)
                mDialogView.tv_dialog_title.setText("노동일정 제거")
                mDialogView.tv_dialog_context.setText("${holder.date.text.toString()}의 ${holder.title.text.toString()} 노동 일정을 제거할까요??")
                mDialogView.tv_dialog_ok.setText("예")
                mDialogView.tv_dialog_no.setText("아니오")
                mDialogView.tv_dialog_ok.setOnClickListener {
                    // 확인 버튼 누름
                    dbManager.deleteWork(workAdapter.items[position].wId)
                    recyclerViewInit()
                    mAlertDialog.dismiss()
                }
                mDialogView.tv_dialog_no.setOnClickListener{
                    // 취소 버튼 누름
                    mAlertDialog.dismiss()
                }
            }
        }
        workAdapter.okBtnClickListener = object : WorkAdapter.OnOKBtnClickListener{
            override fun OnOkBtnClick(holder: WorkAdapter.MyViewHolder, view: View, position: Int) {
                // DialogView 생성
                val mDialogView = LayoutInflater.from(this@WorkFragment.context!!).inflate(R.layout.dialog_box, null)
                val mBuilder = AlertDialog.Builder(this@WorkFragment.context!!).setView(mDialogView)
                val mAlertDialog = mBuilder.show()
                mAlertDialog.window!!.setGravity(Gravity.CENTER)
                mDialogView.tv_dialog_title.setText("노동 완료")
                mDialogView.tv_dialog_context.setText("노동 기록을 일지로 옮길까요??")
                mDialogView.tv_dialog_ok.setText("예")
                mDialogView.tv_dialog_no.setText("아니오")
                mDialogView.tv_dialog_ok.setOnClickListener {
                    // 확인 버튼 누름
                    dbManager.setWorkCheck(workAdapter.items[position].wId)
                    recyclerViewInit()
                    mAlertDialog.dismiss()
                }
                mDialogView.tv_dialog_no.setOnClickListener{
                    // 취소 버튼 누름
                    mAlertDialog.dismiss()
                }
            }
        }
        workRecyclerView.adapter = workAdapter
        textViewInit()
    }

}
