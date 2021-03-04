package com.example.workdiary.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.workdiary.Activity.Presenter.MainContract
import com.example.workdiary.Activity.Presenter.MainPresenter
import com.example.workdiary.Fragment.DiaryFragment
import com.example.workdiary.R
import com.example.workdiary.Fragment.WorkFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), MainContract.View {

    private val presenter: MainPresenter by lazy { // by lazy는 var 에서 lateinit을 사용하는 것 처럼 val에서 늦은 초기화를 위해 사용하는 문법이다.
        MainPresenter(this)
    }

    val diaryFragment = DiaryFragment()
    val workFragment = WorkFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        fragmentInit()
        buttonInit()
    }

    private fun fragmentInit() {
        // fragment 초기 화면을 workFragment로 선택
        setFragment(workFragment)
    }

    private fun buttonInit() {
        // button listener 모음
        val diaryBtn:TextView = tv_main_diaryBtn
        val workBtn:TextView = tv_main_workBtn
        tv_main_diaryBtn.setOnClickListener {
            presenter.clickDiaryBtn(diaryBtn, workBtn, diaryFragment)
        }
        tv_main_workBtn.setOnClickListener {
            presenter.clickWorkBtn(diaryBtn, workBtn, workFragment)
        }
        tv_main_addBtn.setOnClickListener {
            presenter.clickAddWorkBtn()
        }
    }

    override fun setFragment(fragment: Fragment){
        // fragment 세팅
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.replace(R.id.fl_main_fragment, fragment)
        fragmentTransaction.commit()
    }

    override fun btnOn(textView:TextView){
        // TextView 버튼 ON의 디자인으로 바꿔주기
        textView.setBackgroundResource(R.drawable.top_rounded_rectangle_white)
        textView.setTextColor(resources.getColor(R.color.colorBlack))
    }

    override fun btnOff(textView: TextView){
        // TextView 버튼 OFF의 디자인으로 바꿔주기
        textView.setBackgroundResource(R.drawable.top_rounded_rectangle_black)
        textView.setTextColor(resources.getColor(R.color.colorWhite))
    }

    override fun hideAddWorkBtn(){
        // AddWorkActivity로 이동하는 버튼 안보이게 하기
        tv_main_addBtn.visibility = View.GONE
    }

    override fun showAddWorkBtn(){
        // AddWorkActivity로 이동하는 버튼 보이게 하기
        tv_main_addBtn.visibility = View.VISIBLE
    }

    override fun showAddWorkActivity() {
        // AddWorkActivity 실행
        val intent = Intent(this, AddWorkActivity::class.java)
        startActivity(intent)
    }

}
