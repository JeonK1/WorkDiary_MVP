package com.example.workdiary

import android.app.Activity
import android.os.Build
import com.example.workdiary.Activity.Presenter.AddWorkContract
import com.example.workdiary.Activity.Presenter.AddWorkPresenter
import com.example.workdiary.SQLite.DBManager
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = intArrayOf(Build.VERSION_CODES.O_MR1), packageName = "com.example.workdiary")
class AddWorkPresenterTest {
    @Mock
    private lateinit var view: AddWorkContract.View

    private lateinit var presenter: AddWorkPresenter
    lateinit var dbmanager: DBManager

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        //presenter setting
        presenter = AddWorkPresenter(RuntimeEnvironment.application, view)
        //dbmanager setting
        dbmanager = DBManager(RuntimeEnvironment.application)
        dbmanager.clear() // delete all data in table
    }

    @Test
    fun AddWorkPresenter_clickLowestMoney_Test() {
        //Given

        //When
        presenter.clickLowestMoney()

        //Then
        verify(view, times(1)).setMoney(8590)
    }

    @Test
    fun AddWorkPresenter_clickBackBtn_Test() {
        //Given

        //When
        presenter.clickBackBtn()

        //Then
        verify(view, times(1)).finishView(Activity.RESULT_CANCELED)
    }

    @Test
    fun AddWorkPresenter_titleChanged_Test() {
        //Given
        dbmanager.addWork(
            title = "work1",
            setName = "set1",
            date = "$2021/${"%02d".format(3)}/${"%02d".format(15)}",
            startTime = "07:00",
            endTime = "09:30",
            money = 8590
        )

        //When
        presenter.titleChanged("work1")

        //Then
        verify(view, times(1)).setSetACTV(arrayListOf("set1"))
    }

    @Test
    fun AddWorkPresenter_titleChanged_Test2() {
        //Given
        dbmanager.addWork(
            title = "work1",
            setName = "set1",
            date = "$2021/${"%02d".format(3)}/${"%02d".format(15)}",
            startTime = "07:00",
            endTime = "09:30",
            money = 8590
        )
        dbmanager.addWork(
            title = "work1",
            setName = "set2",
            date = "$2021/${"%02d".format(3)}/${"%02d".format(15)}",
            startTime = "07:00",
            endTime = "09:30",
            money = 8590
        )

        //When
        presenter.titleChanged("work1")

        //Then
        verify(view, times(1)).setSetACTV(arrayListOf("set1", "set2"))
    }

    @Test
    fun AddWorkPresenter_titleChanged_TestASC() {
        //Given
        dbmanager.addWork(
            title = "work1",
            setName = "set2",
            date = "$2021/${"%02d".format(3)}/${"%02d".format(15)}",
            startTime = "07:00",
            endTime = "09:30",
            money = 8590
        )
        dbmanager.addWork(
            title = "work1",
            setName = "set1",
            date = "$2021/${"%02d".format(3)}/${"%02d".format(15)}",
            startTime = "07:00",
            endTime = "09:30",
            money = 8590
        )

        //When
        presenter.titleChanged("work1")

        //Then
        verify(view, times(1)).setSetACTV(arrayListOf("set1", "set2"))
    }

    @Test
    fun AddWorkPresenter_titleChanged_NoTitle() {
        //Given
        val tmpTitle = "sadfasdfawefsdfwefwe"

        //When
        presenter.titleChanged(tmpTitle)

        //Then
        verify(view, times(1)).setSetACTV(ArrayList())
    }
}