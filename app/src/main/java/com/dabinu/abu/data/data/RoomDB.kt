package com.dabinu.abu.data.data

import androidx.annotation.WorkerThread
import com.dabinu.abu.data.models.Account
import com.dabinu.abu.data.room.dao.AccountDao
import kotlinx.coroutines.flow.Flow

class RoomDB(
    private val accountDao: AccountDao) {


    val getAccount: Flow<List<Account>> = accountDao.getProfile()


    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(word: Account) {
        accountDao.saveProfile(word)
    }
}