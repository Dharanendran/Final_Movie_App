package com.example.ticketbooking.signIn.presentation

import android.util.Log
import androidx.lifecycle.*
import com.example.ticketbooking.signIn.domain.IsUserExistUseCase
import com.example.ticketbooking.signup.domain.CreateUserAccountUseCase
import com.example.ticketbooking.signup.domain.UserNameExistCheckingUseCase
import kotlinx.coroutines.*
import java.lang.StringBuilder

class SignInPageViewModel() : ViewModel() {

    interface ToastMaker {
        fun makeToast(message: String)
    }


    var toastMaker: ToastMaker? = null
    var isUserExistUseCase: IsUserExistUseCase? = null
    var createUserAccountUseCase: CreateUserAccountUseCase? = null
    var userNameExistCheckingUseCase: UserNameExistCheckingUseCase? = null

    val userName: MutableLiveData<String> by lazy { MutableLiveData("") }
    val passWord: MutableLiveData<String> by lazy { MutableLiveData("") }
    private val mIsLoginSuccessFull by lazy { MutableLiveData(false) }
    private val mIsProgressBarStarted by lazy { MutableLiveData(false) }
    private val mLoadingText by lazy { MutableLiveData("Loading") }

    private var loadingCoroutineJob: Job? = null
    fun isLoginSuccessFull(): LiveData<Boolean> = mIsLoginSuccessFull
    fun isProgressBarStarted(): LiveData<Boolean> = mIsProgressBarStarted
    fun loadingText(): LiveData<String> = mLoadingText
    fun signInOnClick() {
        if ((userName.value as String).isEmpty() || (passWord.value as String).isEmpty())
            toastMaker?.makeToast("Username or Password can't be Empty !")

        else {

            startLoadingAnim()

            isUserExistUseCase?.let { it ->

                mIsProgressBarStarted.value = true

                it.isUserExist(userName.value as String, passWord.value as String) { result ->
                    if (result)
                        mIsLoginSuccessFull.value = true.also { toastMaker?.makeToast("Loginned Successfully") }
                    else
                        toastMaker?.makeToast("UserName ,Password not Present ")
                    mIsProgressBarStarted.value = false

                }
            }
        }
    }

    fun setIsLoginSuccessFull(value: Boolean) {
        this.mIsLoginSuccessFull.value = value
    }

    fun startLoadingAnim() {
        loadingCoroutineJob =
            CoroutineScope(Dispatchers.Main).launch {
                withTimeout(10000) {
                    while (true) {
                        for (i in 0..3) {
                            delay(1000)
                            mLoadingText.value = mLoadingText.value + "."
                            println(mLoadingText.value)
                        }
                        delay(2000)
                        mLoadingText.value = "Loading"
                    }
                }
            }
    }

}

