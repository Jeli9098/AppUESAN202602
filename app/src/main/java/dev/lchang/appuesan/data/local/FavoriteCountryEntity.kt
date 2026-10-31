package dev.lchang.appuesan.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_countries")
data class FavoriteCountryEntity(
    @PrimaryKey val name: String,
    val ranking: Int,
    val imageUrl: String
)
