package com.example.workdiary.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.workdiary.Adapter.DiaryAdapter
import com.example.workdiary.Fragment.Presenter.DiaryContract
import com.example.workdiary.Fragment.Presenter.DiaryPresenter
import com.example.workdiary.R
import com.example.workdiary.SQLite.DBManager

/**
 * A simple [Fragment] subclass.
 */
class DiaryFragment : Fragment(), DiaryContract.View {

    lateinit var presenter: DiaryPresenter
    lateinit var diaryRecyclerView: RecyclerView
    lateinit var diaryAdapter: DiaryAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var root = inflater.inflate(R.layout.fragment_work, container, false)
        diaryRecyclerView = root.findViewById(R.id.rv_work_recyclerView)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter = DiaryPresenter(this)
        recyclerViewInit()
        textViewInit()
    }

    private fun recyclerViewInit() {
        val dbManager = DBManager(context!!)
        val workList = dbManager.getDiaryAll()
        diaryRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        diaryAdapter = DiaryAdapter(workList)
        diaryRecyclerView.adapter = diaryAdapter
    }

    private fun textViewInit() {
        if(diaryAdapter.items.size!=0) {
            presenter.isNoItems(false)
        } else {
            presenter.isNoItems(true)
        }
    }

    override fun showNoItems() {
        activity!!.findViewById<TextView>(R.id.tv_main_comment).text = "아직 완료된 노동일정이 없어요"
        activity!!.findViewById<TextView>(R.id.tv_main_comment).visibility = View.VISIBLE
    }

    override fun hideNoItems() {
        activity!!.findViewById<TextView>(R.id.tv_main_comment).text = "아직 완료된 노동일정이 없어요"
        activity!!.findViewById<TextView>(R.id.tv_main_comment).visibility = View.INVISIBLE
    }


}
