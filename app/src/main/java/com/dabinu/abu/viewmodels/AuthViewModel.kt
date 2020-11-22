package com.dabinu.abu.viewmodels

import androidx.lifecycle.*
import com.dabinu.abu.data.FirebaseHelper
import com.dabinu.abu.data.RoomDB
import com.dabinu.abu.models.Account
import com.dabinu.abu.models.ResponseBody
import com.dabinu.abu.models.STATE_FAILED
import com.dabinu.abu.models.STATE_SUCCESSFUL
import kotlinx.coroutines.launch
import java.lang.Exception

class AuthViewModel(

    private val firebase: FirebaseHelper,
    private val roomDB: RoomDB) : ViewModel() {



    private val authStatus = MutableLiveData<Boolean>()
    private val signInWithEmailAndPassword = MutableLiveData<ResponseBody<Account>>()
    private val createWithEmailAndPassword = MutableLiveData<ResponseBody<Account>>()



    // Room

    val getProfileFromRoom: LiveData<List<Account>> = roomDB.getAccount.asLiveData()

    private fun saveProfileToRoom(account: Account) = viewModelScope.launch { roomDB.insert(account) }

    fun updateSubscriptionStatusOnRoom(hasSubscribed: Boolean, email: String) = viewModelScope.launch { roomDB.updateSubscriptionStatus(hasSubscribed, email) }

    private fun deleteProfileFromRoom() = viewModelScope.launch { roomDB.deleteProfile() }



    fun checkAuthentication() {
        authStatus.postValue((firebase.isAuthenticated()))
    }


    fun signInWithEmailPassword(email: String, password: String) {

        try {

            signInWithEmailAndPassword.postValue(ResponseBody<Account>().loading())

            firebase.signInWithEmailAndPassword(
                email,
                password,
                {
                    fetchUserData()
                },
                {
                    logout()
                    signInWithEmailAndPassword.postValue(ResponseBody(STATE_FAILED, it.message.toString(), null))
                })
        }
        catch (e: Exception) {
            logout()
            signInWithEmailAndPassword.postValue(ResponseBody(STATE_FAILED, e.message.toString(), null))
        }
    }


    private fun fetchUserData() {

        try {

            signInWithEmailAndPassword.postValue(ResponseBody<Account>().loading())

            firebase.fetchUserData(
                {
                    saveProfileToRoom(it.toObject(Account::class.java) as Account)
                    authStatus.postValue((firebase.isAuthenticated()))
                    signInWithEmailAndPassword.postValue(ResponseBody(STATE_SUCCESSFUL, "", it.toObject(Account::class.java)))

                },
                {
                    logout()
                    signInWithEmailAndPassword.postValue(ResponseBody(STATE_FAILED, it.message.toString(), null))
                })
        }
        catch (e: Exception) {
            logout()
            signInWithEmailAndPassword.postValue(ResponseBody(STATE_FAILED, e.message.toString(), null))
        }
    }


    fun createAccountWithEmailPassword(profile: Account, password : String) {

        try {

            createWithEmailAndPassword.postValue(ResponseBody<Account>().loading())

            firebase.createUserWithEmailAndPassword(
                profile.email,
                password,
                {
                    postUserData(Account(name = profile.name, email = profile.email, hasSubscribed = profile.hasSubscribed))
                },
                {
                    logout()
                    createWithEmailAndPassword.postValue(ResponseBody(STATE_FAILED, it.message.toString(), null))
                })
        }
        catch (e: Exception) {
            logout()
            createWithEmailAndPassword.postValue(ResponseBody(STATE_FAILED, e.message.toString(), null))
        }
    }


    private fun postUserData(userProfile: Account) {

        try {

            createWithEmailAndPassword.postValue(ResponseBody<Account>().loading())

            firebase.uploadUserData(
                userProfile,
                {
                    saveProfileToRoom(userProfile)
                    authStatus.postValue((firebase.isAuthenticated()))
                    createWithEmailAndPassword.postValue(ResponseBody(STATE_SUCCESSFUL, "", null))
                },
                {
                    firebase.signOut()
                    createWithEmailAndPassword.postValue(ResponseBody(STATE_FAILED, it.message.toString(), null))
                })
        }
        catch (e: Exception) {
            logout()
            createWithEmailAndPassword.postValue(ResponseBody(STATE_FAILED, e.message.toString(), null))
        }
    }


    fun updateSubscriptionStatusToFirebase(bool: Boolean) {
        firebase.updateSubscriptionStatus(bool)
    }


    fun logout() {
        firebase.signOut()
        deleteProfileFromRoom()
        checkAuthentication()
    }


    fun getAuthStatusLiveData() = authStatus
    fun getLoginInWithEmailLiveData() = signInWithEmailAndPassword
    fun getCreateAccountWithEmailLiveData() = createWithEmailAndPassword



}
