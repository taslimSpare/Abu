package com.dabinu.abu.data.data.session

import android.content.SharedPreferences
import com.dabinu.abu.data.models.Account
import com.google.gson.Gson


const val accountKey = "x_account_pref_key"

class Session(private val pref: SharedPreferences) {

    fun saveAccount(account: Account) {
        val s = Gson().toJson(account)
        pref.edit().putString(accountKey, s).apply()
    }

    fun getAccount(): Account {
        val s = pref.getString(accountKey, "") ?: ""
        return if (s == "") Account()
        else Gson().fromJson(s, Account::class.java)
    }

    fun destroyAccount() {
        pref.edit().apply {
            putString(accountKey, "")
            apply()
        }
    }

}