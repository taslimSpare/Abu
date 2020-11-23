package com.dabinu.abu.data

import androidx.annotation.WorkerThread
import com.dabinu.abu.models.Account
import com.dabinu.abu.room.dao.AccountDao
import kotlinx.coroutines.flow.Flow

/*
This class stores all the local database (Room) operations.
As represented here, this class focuses on creating, fetching, updating and deleting a user's profile
 */
class RoomDB(
    private val accountDao: AccountDao) {



    /* This returns a LiveData of the current account(s) stored in the DB.
        I chose to use a LiveData because of the advantage of always getting real-time data
     */
    val getAccount: Flow<List<Account>> = accountDao.getProfile()



    // this function saves an a user's account to local storage (Room)
    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(word: Account) {
        accountDao.saveProfile(word)
    }



    // this function updates the 'hasSubscribed' field in the user's profile
    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun updateSubscriptionStatus(hasSubscribed: Boolean, email: String) {
        accountDao.updateSubscriptionStatus(hasSubscribed, email)
    }



    // this function deletes a/all user profiles from the local storage
    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun deleteProfile() {
        accountDao.deleteProfile()
    }


}