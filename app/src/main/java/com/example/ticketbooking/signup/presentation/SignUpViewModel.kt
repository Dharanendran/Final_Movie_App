package com.example.ticketbooking.signup.presentation

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.ticketbooking.signup.domain.ValidationUseCase
import com.example.ticketbooking.dataRepository.roomDatabase.entities.User
import com.example.ticketbooking.signup.domain.CreateUserAccountUseCase
import com.example.ticketbooking.signup.domain.UserNameExistCheckingUseCase

class SignUpPageViewModel : ViewModel() {

    interface ToastMaker {
        fun makeToast(message: String)
    }


    val profilePicture:MutableLiveData<Bitmap?> by lazy{ MutableLiveData(null) }
    val name: MutableLiveData<String> by lazy { MutableLiveData("dharani") }
    val mobileNo: MutableLiveData<String> by lazy { MutableLiveData("8123456789") }
    val emailId: MutableLiveData<String> by lazy { MutableLiveData("q@gmail.com") }
    val password:MutableLiveData<String> by lazy { MutableLiveData("Dharani@1") }
    val reEnterPassword:MutableLiveData<String> by lazy{ MutableLiveData("Dharani@1")} // not private because two way binding

    private val isAccountCreated:MutableLiveData<Boolean> by lazy{ MutableLiveData()}


    var toastMaker: ToastMaker? = null
    var userNameExistCheckingUseCase: UserNameExistCheckingUseCase? = null
    var createUserAccountUseCase: CreateUserAccountUseCase? = null
    var validationUseCase: ValidationUseCase? = null

    fun isAccountCreated(): LiveData<Boolean> = isAccountCreated

    fun onClickSubmit() {

        if(name.value=="" || mobileNo.value=="" || emailId.value=="" || password.value=="" || reEnterPassword.value=="")
            toastMaker?.makeToast("Required Fields Not be Empty !")

        else if (!validationUseCase?.isEmailValid((emailId.value).toString())!!)
            toastMaker?.makeToast(" Enter valid Email Id !")

        else if (!validationUseCase?.isPhoneNumberValid((mobileNo.value).toString())!!)
            toastMaker?.makeToast(" Enter Valid Mobile No !")

        else if(password.value != reEnterPassword.value )
            toastMaker?.makeToast(" Re-Entered Password is not Equal to Password, Recheck it !")

        else if (!validationUseCase?.isPasswordStrong((password.value).toString())!!)
            toastMaker?.makeToast(" Password Must contains minimum(2 uppercase, 1 special, 3 lower, 2 digit!)")

        else
        {
            userNameExistCheckingUseCase?.isUserNameExist(emailId.value.toString()) {
                if (!it)
                    createUserAccountUseCase?.createAccount(
                        User(
                            name.value.toString(),
                            mobileNo.value.toString(),
                            emailId.value.toString(),
                            null ),
                        emailId.value.toString(), password.value.toString(), true)
                    {
                        isAccountCreated.value = true
                    }

                else
                    toastMaker?.makeToast("EmailID You Entered Is Already Exist !")
            }
        }
    }

}