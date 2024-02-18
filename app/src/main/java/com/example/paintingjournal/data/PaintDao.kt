package com.example.paintingjournal.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.paintingjournal.model.Image
import com.example.paintingjournal.model.MiniaturePaint
import com.example.paintingjournal.model.PaintImageMappingTable
import kotlinx.coroutines.flow.Flow

@Dao
interface PaintDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(miniaturePaint: MiniaturePaint)

    @Update
    suspend fun update(miniaturePaint: MiniaturePaint)

    @Delete
    suspend fun delete(miniaturePaint: MiniaturePaint)

    @Query("SELECT * from paints WHERE paintId = :id")
    fun getPaint(id: Int): Flow<MiniaturePaint>

    @Query("SELECT * from paints ORDER BY name ASC")
    fun getAllPaints(): Flow<List<MiniaturePaint>>

    @Query("SELECT imageId,imageUri from images " +
            "left join paintImageMapping map on images.imageid = map.imageIdRef " +
            "where paintIdRef = :id")
    fun getImagesForPaint(id: Int): Flow<List<Image>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addPaintImageMap(paintImageMappingTable: PaintImageMappingTable)

    @Delete
    suspend fun deletePaintImageMap(paintImageMappingTable: PaintImageMappingTable)
}