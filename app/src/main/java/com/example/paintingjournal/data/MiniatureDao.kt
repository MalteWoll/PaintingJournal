package com.example.paintingjournal.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.paintingjournal.model.Image
import com.example.paintingjournal.model.Miniature
import com.example.paintingjournal.model.MiniatureImageMappingTable
import com.example.paintingjournal.model.MiniaturePaint
import com.example.paintingjournal.model.MiniaturePaintMappingTable
import kotlinx.coroutines.flow.Flow

@Dao
interface MiniatureDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(miniature: Miniature) : Long

    @Update
    suspend fun update(miniature: Miniature)

    @Delete
    suspend fun delete(miniature: Miniature)

    @Query("SELECT * from miniatures WHERE miniatureId = :id")
    fun getMiniature(id: Int): Flow<Miniature>

    @Query("SELECT * from miniatures ORDER BY name ASC")
    fun getAllMiniatures(): Flow<List<Miniature>>

    @Query("SELECT imageId,imageUri,saveState from images " +
            "left join miniatureImageMapping map on images.imageId = map.imageIdRef " +
            "where miniatureIdRef = :id")
    fun getImagesForMiniature(id: Int): Flow<List<Image>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addMiniatureImageMap(miniatureImageMappingTable: MiniatureImageMappingTable)

    @Delete
    suspend fun deleteMiniatureImageMap(miniatureImageMappingTable: MiniatureImageMappingTable)

    @Query("SELECT * from paints " +
            "left join miniaturePaintMapping map on paints.paintId = map.paintIdRef " +
            "where miniatureIdRef = :id")
    fun getPaintsForMiniature(id: Long): Flow<List<MiniaturePaint>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addPaintToMiniature(miniaturePaintMappingTable: MiniaturePaintMappingTable)

    @Delete
    suspend fun deletePaintForMiniature(miniaturePaintMappingTable: MiniaturePaintMappingTable)
}