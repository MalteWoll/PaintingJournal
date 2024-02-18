package com.example.paintingjournal.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.paintingjournal.model.Miniature
import kotlinx.coroutines.flow.Flow

@Dao
interface MiniatureDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(miniature: Miniature)

    @Update
    suspend fun update(miniature: Miniature)

    @Delete
    suspend fun delete(miniature: Miniature)

    @Query("SELECT * from miniatures WHERE miniatureId = :id")
    fun getMiniature(id: Int): Flow<Miniature>

    @Query("SELECT * from miniatures ORDER BY name ASC")
    fun getAllMiniatures(): Flow<List<Miniature>>
}