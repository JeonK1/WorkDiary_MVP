package com.example.workdiary.Activity

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.*
import com.example.workdiary.Activity.Presenter.AddWorkContract
import com.example.workdiary.Activity.Presenter.AddWorkPresenter
import com.example.workdiary.R
import com.example.workdiary.SQLite.DBManager
import kotlinx.android.synthetic.main.activity_add_work.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class AddWorkActivity : AppCompatActivity(), AddWorkContract.View {

    private val presenter: AddWorkPresenter by lazy {
        AddWorkPresenter(applicationContext, this)
    }

    private val DAY_OF_WEEK: ArrayList<String> = arrayListOf("", "일", "월", "화", "수", "목", "금", "토")
    lateinit var dateSetListener:DatePickerDialog.OnDateSetListener
    lateinit var startTimeSetListener:TimePickerDialog.OnTimeSetListener
    lateinit var endTimeSetListener:TimePickerDialog.OnTimeSetListener
    var workYear=""
    var workMonth=""
    var workDay=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_work)
        calendarInit()
        listenerInit()
        initAutoCompleteText()
        pickerInit()
        buttonInit()
    }

    private fun calendarInit() {
        val cal = Calendar.getInstance()
        updateDate(
            year = cal.get(Calendar.YEAR),
            month = (cal.get(Calendar.MONTH)+1),
            day = cal.get(Calendar.DAY_OF_MONTH)
        )
        setDateTextView(
            month = cal.get(Calendar.MONTH)+1,
            day = cal.get(Calendar.DAY_OF_MONTH),
            dayofweek = cal.get(Calendar.DAY_OF_WEEK)
        )
        if(cal.get(Calendar.AM_PM)==1){
            // PM일때
            setStartTime(
                hour = cal.get(Calendar.HOUR)+12,
                minute = 0
            )
            setEndTime(
                hour = cal.get(Calendar.HOUR)+12+1,
                minute = 0
            )
        } else {
            // AM일때
            setStartTime(
                hour = cal.get(Calendar.HOUR),
                minute = 0
            )
            setEndTime(
                hour = cal.get(Calendar.HOUR)+1,
                minute = 0
            )
        }
    }

    private fun listenerInit() {
        act_addwork_title.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                // 세트입력 autoCompleteListener에 List 추가
                presenter.titleChanged(
                    curTitle =  act_addwork_title.text.toString()
                )
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })
        act_addwork_set.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(s: Editable?) {
                presenter.setChanged(
                    curTitle = act_addwork_title.text.toString(),
                    curSet = act_addwork_set.text.toString()
                )
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
        })
    }

    private fun initAutoCompleteText() {
        val dbManager = DBManager(applicationContext)
        val workNameList = dbManager.getWorkNameAll()
        setTitleACTV(workNameList)
    }

    private fun pickerInit() {
        dateSetListener =
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                presenter.setDatePicker(
                    year = year,
                    monthOfYear = monthOfYear,
                    dayOfMonth = dayOfMonth
                )
            }
        startTimeSetListener =
            TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                presenter.setStartTimePicker(
                    hourOfDay = hourOfDay,
                    minute = minute
                )
            }
        endTimeSetListener =
            TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                presenter.setEndTimePicker(
                    hourOfDay = hourOfDay,
                    minute = minute
                )
            }
    }

    private fun buttonInit() {
        ll_addwork_pickStartTime.setOnClickListener {
            presenter.clickTimePicker(
                listener = startTimeSetListener,
                hour = tv_addwork_startTime.text.toString().split(":")[0].toInt(),
                minute = tv_addwork_startTime.text.toString().split(":")[1].toInt()
            )
        }
        ll_addwork_pickEndTime.setOnClickListener {
            presenter.clickTimePicker(
                listener = endTimeSetListener,
                hour = tv_addwork_startTime.text.toString().split(":")[0].toInt(),
                minute = tv_addwork_startTime.text.toString().split(":")[1].toInt()
            )
        }
        ll_addwork_pickDate.setOnClickListener {
            presenter.clickDatePicker(
                listener = dateSetListener
            )
        }
        btn_addwork_inputLowestMoney.setOnClickListener {
            presenter.clickLowestMoney()
        }
        tv_addwork_saveBtn.setOnClickListener {
            presenter.clickSaveBtn(
                title = act_addwork_title.text.toString(),
                set = act_addwork_set.text.toString(),
                year = workYear.toInt(),
                month = workMonth.toInt(),
                day = workDay.toInt(),
                startTime = tv_addwork_startTime.text.toString(),
                endTime = tv_addwork_endTime.text.toString(),
                money = et_addwork_money.text.toString().toInt()
            )
        }
        ib_addwork_backBtn.setOnClickListener {
            presenter.clickBackBtn()
        }
    }

    override fun setDateTextView(month: Int, day: Int, dayofweek: Int) {
        tv_addwork_mon.text = "${"%02d".format(month)}월"
        tv_addwork_day.text = "${"%02d".format(day)}일"
        tv_addwork_dayofweek.text = "(${DAY_OF_WEEK[dayofweek]})"
    }

    override fun setMoney(money: Int) {
        et_addwork_money.setText(money.toString())
    }

    override fun setStartTime(hour: Int, minute: Int) {
        tv_addwork_startTime.text = "${"%02d".format(hour)}:${"%02d".format(minute)}"
    }

    override fun setEndTime(hour: Int, minute: Int) {
        tv_addwork_endTime.text = "${"%02d".format(hour)}:${"%02d".format(minute)}"
    }

    override fun setSetACTV(stringList: ArrayList<String>) {
        act_addwork_set.setAdapter(
            ArrayAdapter(
                applicationContext,
                android.R.layout.simple_dropdown_item_1line,
                stringList
            )
        )
    }

    override fun setTitleACTV(stringList: ArrayList<String>) {
        act_addwork_title.setAdapter(
            ArrayAdapter(
                applicationContext,
                android.R.layout.simple_dropdown_item_1line,
                stringList
            )
        )
    }

    override fun showTimePicker(listener: TimePickerDialog.OnTimeSetListener, hour: Int, minute: Int) {
        TimePickerDialog(this,
            android.R.style.Theme_Holo_Light_Dialog,
            listener,
            hour,
            minute,
            android.text.format.DateFormat.is24HourFormat(this)
        ).show()
    }

    override fun showDatePicker(listener: DatePickerDialog.OnDateSetListener) {
        DatePickerDialog(this,
            listener,
            Calendar.getInstance().get(Calendar.YEAR),
            Calendar.getInstance().get(Calendar.MONTH),
            Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    override fun finishView(resultCode:Int) {
        setResult(resultCode)
        finish()
    }

    override fun updateDate(year: Int, month: Int, day: Int) {
        workYear = year.toString()
        workMonth = month.toString()
        workDay = day.toString()
    }
}