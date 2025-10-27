package com.saif.posttest4pmob

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "citizen")
data class CitizenEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val nama: String,
    val nik: Long,
    val kabupaten: String,
    val kecamatan: String,
    val desa: String,
    val rt: Int,
    val rw: Int,
    val gender: String,
    val statusPernikahan: String
)

