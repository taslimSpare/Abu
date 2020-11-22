package com.dabinu.abu.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class Account(
    @PrimaryKey val email: String = "",
    val name: String = "",
    val hasSubscribed: Boolean = false
)