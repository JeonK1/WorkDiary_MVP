package com.example.workdiary.Fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.workdiary.Activity.Presenter.MainPresenter
import com.example.workdiary.Adapter.WorkAdapter
import com.example.workdiary.Fragment.Presenter.WorkContract
import com.example.workdiary.Fragment.Presenter.WorkPresenter
import com.example.workdiary.R
import com.example.workdiary.SQLite.DBManager
import kotlinx.android.synthetic.main.dialog_box.view.*
import kotlinx.android.synthetic.main.fragment_work.*
import kotlinx.android.synthetic.main.item_work.*
import java.util.*

/**
 * A simple [Fragment] subclass.
 */
class WorkFragment : Fragment(), WorkContract.View {

    lateinit var presenter: WorkPresenter
    lateinit var workRecyclerView: RecyclerView
    lateinit var workAdapter: WorkAdapter

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
        presenter = WorkPresenter(this, workAdapter, workAdapter)
    }

    private fun recyclerViewInit() {
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
                presenter.clickItem(view.findViewById(R.id.ll_itemwork_btnlayout))
            }
        }
        workAdapter.delBtnClickListener = object : WorkAdapter.OnDelBtnClickListener{
            override fun OnDeleteBtnClick(
                holder: WorkAdapter.MyViewHolder,
                view: View,
                position: Int
            ) {
                // DialogView 생성
                presenter.clickDelBtn(context!!, position, holder.date.text.toString(), holder.title.text.toString())
            }
        }
        workAdapter.okBtnClickListener = object : WorkAdapter.OnOKBtnClickListener{
            override fun OnOkBtnClick(holder: WorkAdapter.MyViewHolder, view: View, position: Int) {
                // DialogView 생성
                presenter.clickOkBtn(context!!, position)
            }
        }
        workRecyclerView.adapter = workAdapter
        if(workAdapter.items.size!=0) {
            activity!!.findViewById<TextView>(R.id.tv_main_comment).visibility = View.INVISIBLE
        } else {
            activity!!.findViewById<TextView>(R.id.tv_main_comment).visibility = View.VISIBLE
        }
    }

    override fun showLayout(layout:LinearLayout) {
        layout.visibility = View.VISIBLE
    }

    override fun hideLayout(layout:LinearLayout) {
        layout.visibility = View.GONE
    }

    override fun crateDialog(context: Context, position:Int, action: String, title: String, contents: String) {
        val mDialogView = LayoutInflater.from(this@WorkFragment.context!!).inflate(R.layout.dialog_box, null)
        val mBuilder = AlertDialog.Builder(this@WorkFragment.context!!).setView(mDialogView)
        val mAlertDialog = mBuilder.show()
        mAlertDialog.window!!.setGravity(Gravity.CENTER)
        mDialogView.tv_dialog_title.text = title
        mDialogView.tv_dialog_context.text = contents
        mDialogView.tv_dialog_ok.text = "예"
        mDialogView.tv_dialog_no.text = "아니오"
        mDialogView.tv_dialog_ok.setOnClickListener {
            // 확인 버튼 누름
            when(action){
                "DEL" -> {
                    presenter.deleteWork(context, position)
                }
                "OK" -> {
                    presenter.checkWork(context, position)
                }
            }
            mAlertDialog.dismiss()
        }
        mDialogView.tv_dialog_no.setOnClickListener{
            // 취소 버튼 누름
            mAlertDialog.dismiss()
        }
    }
}
