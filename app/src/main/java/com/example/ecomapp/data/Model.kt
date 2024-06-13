package com.example.ecomapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "model_tbl")
data class Model(
    @PrimaryKey val id: Int, val name: String,
    val link: String, val action: String,
    val hard: Int, val medium: Int, val easy: Int
)