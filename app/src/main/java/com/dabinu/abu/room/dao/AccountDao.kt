package com.dabinu.abu.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dabinu.abu.models.Account
import kotlinx.coroutines.flow.Flow

@Dao
interface AccountDao {

    @Query("SELECT * FROM Account ORDER BY email ASC")
    fun getProfile(): Flow<List<Account>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveProfile(account: Account)

    @Query("DELETE FROM Account")
    suspend fun deleteProfile()
}