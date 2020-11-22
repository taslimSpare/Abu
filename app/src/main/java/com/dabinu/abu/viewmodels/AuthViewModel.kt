package com.dabinu.abu.viewmodels

import androidx.lifecycle.*
import com.dabinu.abu.data.FirebaseHelper
import com.dabinu.abu.data.RoomDB
import com.dabinu.abu.models.Account
import kotlinx.coroutines.launch

class AuthViewModel(
    private val firebaseHelper: FirebaseHelper,
    private val roomDB: RoomDB) : ViewModel() {


    val getProfileFromRoom: LiveData<List<Account>> = roomDB.getAccount.asLiveData()

    fun saveProfileToRoom(account: Account) = viewModelScope.launch { roomDB.insert(account) }


}
