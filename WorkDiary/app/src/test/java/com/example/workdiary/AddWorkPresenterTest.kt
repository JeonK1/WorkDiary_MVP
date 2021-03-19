package com.example.workdiary

import android.os.Build
import com.example.workdiary.Activity.Presenter.AddWorkContract
import com.example.workdiary.Activity.Presenter.AddWorkPresenter
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations
import org.mockito.Spy
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = intArrayOf(Build.VERSION_CODES.O_MR1), packageName = "com.example.workdiary")
class AddWorkPresenterTest {
    @Mock
    private lateinit var view: AddWorkContract.View

    private lateinit var presenter: AddWorkPresenter

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        presenter = AddWorkPresenter(RuntimeEnvironment.application, view)
    }

    @Test
    fun AddWorkPresenter_clickLowestMoney_Test() {
        Mockito.`when`(presenter.clickLowestMoney()).thenReturn() //TOdo : 여기.. 얘 리턴하는거 없는데 어쩌지?
        verify(view, times(1)).setMoney(3500)
    }
}