package com.example.workdiary

import android.os.Build.VERSION_CODES.LOLLIPOP
import com.example.workdiary.SQLite.DBManager
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricGradleTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config

@RunWith(RobolectricGradleTestRunner::class)
@Config(constants = BuildConfig::class, sdk = intArrayOf(LOLLIPOP), packageName = "com.example.workdiary")
class DBManagerTest {
    lateinit var dbmanager:DBManager

    @Before
    fun setUp(){
        dbmanager = DBManager(RuntimeEnvironment.application)
        dbmanager.clear() // delete all data in table
    }

    @Test
    fun helloworld(){
        //Given

        // When

        // Then
    }
}
