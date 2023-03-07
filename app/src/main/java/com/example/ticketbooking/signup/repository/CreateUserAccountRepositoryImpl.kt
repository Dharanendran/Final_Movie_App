package com.example.ticketbooking.signup.repository

import android.content.Context
import android.util.Log
import com.example.ticketbooking.dataRepository.roomDatabase.entities.User
import com.example.ticketbooking.dataRepository.roomDatabase.entities.UserCredential
import com.example.ticketbooking.dependencyInjection.DependencyFactory
import com.example.ticketbooking.signup.domain.IcreateUserAccountRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CreateUserAccountRepositoryImpl(private val context: Context): IcreateUserAccountRepository
{
    override fun createUserAccount(user: User, username:String, password:String, wantToInsertUserCredential: Boolean, callback: (id: Long) -> Unit)
    {
        CoroutineScope(Dispatchers.IO).launch {
            val id:Long
            DependencyFactory.getInstance(context).let{
                 id = it.getDataBaseObject().getUserDao().insertUser(user)
                if(wantToInsertUserCredential)
                    it.getDataBaseObject().getUserCredentialDao().insertUserAccount( UserCredential(id, username, password ))
            }
            CoroutineScope(Dispatchers.Main).launch { callback(id) }
        }
    }

}
