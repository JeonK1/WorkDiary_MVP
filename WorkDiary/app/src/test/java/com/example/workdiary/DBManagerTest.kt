package com.example.workdiary

import android.os.Build
import com.example.workdiary.SQLite.DBManager
import junit.framework.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = intArrayOf(Build.VERSION_CODES.O_MR1), packageName = "com.example.workdiary")
class DBManagerTest {
    lateinit var dbmanager:DBManager

    @Before
    fun setUp(){
        dbmanager = DBManager(RuntimeEnvironment.application)
        dbmanager.clear() // delete all data in table
    }

    @Test
    fun DBManager_AddWork_Test(){
        /*** DBManager Add 잘 작동하는지 테스트 ***/
        //Given
        val title = "work1"
        val setName = "set1"
        val date = "$2021/${"%02d".format(3)}/${"%02d".format(15)}"
        val startTime = "07:00"
        val endTime = "09:30"
        val money = 8590

        // When
        dbmanager.addWork(
            title = title,
            setName = setName,
            date = date,
            startTime = startTime,
            endTime = endTime,
            money = money
        )

        // Then
        assertEquals(1, (dbmanager.getWorkAll()).size)
    }

    @Test
    fun DBManager_DeleteWork_Test(){
        /*** DBManager Delete 잘 작동하는지 테스트 ***/
        //Given
        dbmanager.addWork(
            title = "work1",
            setName = "set1",
            date = "$2021/${"%02d".format(3)}/${"%02d".format(15)}",
            startTime = "07:00",
            endTime = "09:30",
            money = 8590
        )

        // When
        dbmanager.deleteWork(1)

        // Then
        assertEquals(0, (dbmanager.getWorkAll()).size)
    }

    @Test
    fun DBManager_DeleteWork_ErrorCheckIfEmpty(){
        /*** DBManager 존재하지 않는 wId Delete에도 오류없이 작동하는지 테스트 ***/
        //Given

        // When
        dbmanager.deleteWork(0)
        dbmanager.deleteWork(1)

        // Then
        assertEquals(0, (dbmanager.getWorkAll()).size)
    }

    @Test
    fun DBManager_getWorkNameAll_Test(){
        /*** DBManager 모든 work 정보 가져오는 것 테스트 ***/
        //Given
        val count = (dbmanager.getWorkAll()).size
        val title = "work1"
        val title2 = "work2"
        val setName = "set1"
        val date = "$2021/${"%02d".format(3)}/${"%02d".format(15)}"
        val startTime = "07:00"
        val endTime = "09:30"
        val endTime2 = "13:30"
        val money = 8590
        dbmanager.addWork(
            title = title,
            setName = setName,
            date = date,
            startTime = startTime,
            endTime = endTime,
            money = money
        )
        dbmanager.addWork(
            title = title2,
            setName = setName,
            date = date,
            startTime = startTime,
            endTime = endTime2,
            money = money
        )
        dbmanager.addWork(
            title = title,
            setName = setName,
            date = date,
            startTime = startTime,
            endTime = endTime,
            money = money
        )


        // When
        val workNameList = dbmanager.getWorkNameAll()

        // Then
        assertEquals(2, workNameList.size)
        assertEquals(workNameList[0], title) // 가져온 workNameList는 오름차순으로 되어있다.
        assertEquals(workNameList[1], title2) // 가져온 workNameList는 오름차순으로 되어있다.
    }
}
