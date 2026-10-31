package dev.lchang.appuesan.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteCountryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(country: FavoriteCountryEntity)

    @Delete
    suspend fun delete(country: FavoriteCountryEntity)

    @Query("SELECT * FROM favorite_countries ORDER BY name ASC")
    fun getAllFavorites(): Flow<List<FavoriteCountryEntity>>
}
