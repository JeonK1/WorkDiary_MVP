package com.example.workdiary.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.example.workdiary.Fragment.DiaryFragment
import com.example.workdiary.R
import com.example.workdiary.Fragment.WorkFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_work.*

class MainActivity : AppCompatActivity() {

    val diaryFragment = DiaryFragment()
    val workFragment = WorkFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        fragmentInit()
        buttonInit()
    }

    private fun buttonInit() {
        tv_main_diaryBtn.setOnClickListener {
            val fragment = supportFragmentManager.beginTransaction()
            fragment.addToBackStack(null)
            fragment.replace(R.id.fl_main_fragment, diaryFragment)
            fragment.commit()
            tv_main_diaryBtn.setBackgroundResource(R.drawable.top_rounded_rectangle_white)
            tv_main_diaryBtn.setTextColor(resources.getColor(R.color.colorBlack))
            tv_main_workBtn.setBackgroundResource(R.drawable.top_rounded_rectangle_black)
            tv_main_workBtn.setTextColor(resources.getColor(R.color.colorWhite))
            tv_main_addBtn.visibility = View.GONE
        }
        tv_main_workBtn.setOnClickListener {
            val fragment = supportFragmentManager.beginTransaction()
            fragment.addToBackStack(null)
            fragment.replace(R.id.fl_main_fragment, workFragment)
            fragment.commit()
            tv_main_diaryBtn.setBackgroundResource(R.drawable.top_rounded_rectangle_black)
            tv_main_diaryBtn.setTextColor(resources.getColor(R.color.colorWhite))
            tv_main_workBtn.setBackgroundResource(R.drawable.top_rounded_rectangle_white)
            tv_main_workBtn.setTextColor(resources.getColor(R.color.colorBlack))
            tv_main_addBtn.visibility = View.VISIBLE
        }
        tv_main_addBtn.setOnClickListener {
            val intent = Intent(this, AddWorkActivity::class.java)
            startActivity(intent)
        }
    }

    private fun fragmentInit() {
        val fragment = supportFragmentManager.beginTransaction()
        fragment.addToBackStack(null)
        fragment.replace(R.id.fl_main_fragment, workFragment)
        fragment.commit()
    }
}
