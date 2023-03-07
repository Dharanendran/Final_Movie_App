package com.example.ticketbooking.signup.domain

import android.util.Log
import com.example.ticketbooking.dataRepository.roomDatabase.entities.User
import com.example.ticketbooking.signup.domain.IcreateUserAccountRepository

class CreateUserAccountUseCase(private val repository:IcreateUserAccountRepository)
{
    fun createAccount(user:User, userName:String, password:String, wantToInsertUserCredential: Boolean, callback: (id:Long)->Unit)
    {
        repository.createUserAccount(user, userName, password,wantToInsertUserCredential, callback)
    }

}






