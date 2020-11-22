package com.dabinu.abu.data

import androidx.annotation.WorkerThread
import com.dabinu.abu.models.Account
import com.dabinu.abu.room.dao.AccountDao
import kotlinx.coroutines.flow.Flow

class RoomDB(
    private val accountDao: AccountDao) {


    val getAccount: Flow<List<Account>> = accountDao.getProfile()


    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(word: Account) {
        accountDao.saveProfile(word)
    }


    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun updateSubscriptionStatus(hasSubscribed: Boolean, email: String) {
        accountDao.updateSubscriptionStatus(hasSubscribed, email)
    }


    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun deleteProfile() {
        accountDao.deleteProfile()
    }


}