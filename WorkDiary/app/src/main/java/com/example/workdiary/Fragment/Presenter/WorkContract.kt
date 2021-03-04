package com.example.workdiary.Fragment.Presenter

import android.content.Context
import android.widget.LinearLayout

interface WorkContract {
    interface View {
        fun showLayout(layout:LinearLayout)
        fun hideLayout(layout:LinearLayout)
        fun crateDialog(context: Context, position:Int, action:String, title:String, contents:String)
    }

    interface Presenter {
        fun clickItem(layout:LinearLayout)
        fun clickDelBtn(context: Context, position:Int, workDate:String, workTitle:String)
        fun clickOkBtn(context: Context, position:Int)
        fun deleteWork(context: Context, position:Int)
        fun checkWork(context: Context, position:Int)
    }
}