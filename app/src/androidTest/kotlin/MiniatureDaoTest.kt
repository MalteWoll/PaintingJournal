import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.paintingjournal.data.MiniatureDao
import com.example.paintingjournal.data.MiniatureDatabase
import com.example.paintingjournal.model.Miniature
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.runner.RunWith
import java.io.IOException
import kotlin.jvm.Throws

@RunWith(AndroidJUnit4::class)
class MiniatureDaoTest {
    private lateinit var miniatureDao: MiniatureDao
    private lateinit var miniatureDatabase: MiniatureDatabase

    private var miniature1 = Miniature(1, "Nurgling", "Games Workshop", "Chaos")
    private var miniature2 = Miniature(2, "Primaris Captain", "Games Workshop", "Ultramarines")

    private suspend fun addOneMiniatureToDb() {
        miniatureDao.insert(miniature1)
    }

    private suspend fun addTwoMiniaturesToDb() {
        miniatureDao.insert(miniature1)
        miniatureDao.insert(miniature2)
    }

    @Before
    fun createDb() {
        val context: Context = ApplicationProvider.getApplicationContext()
        miniatureDatabase = Room.inMemoryDatabaseBuilder(context, MiniatureDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        miniatureDao = miniatureDatabase.miniatureDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        miniatureDatabase.close()
    }

    @Test
    @Throws(Exception::class)
    fun daoInsert_insertsMiniatureIntoDb() = runBlocking {
        addOneMiniatureToDb()
        val allMiniatures = miniatureDao.getAllMiniatures().first()
        assertEquals(allMiniatures[0], miniature1)
    }

    @Test
    @Throws(Exception::class)
    fun daoGetAllMiniatures_returnsAllMiniaturesFromDb() = runBlocking {
        addTwoMiniaturesToDb()
        val allMiniatures = miniatureDao.getAllMiniatures().first()
        assertEquals(allMiniatures[0], miniature1)
        assertEquals(allMiniatures[1], miniature2)
    }

    @Test
    @Throws(Exception::class)
    fun daoUpdateItems_updatesItemsInDb() = runBlocking {
        addTwoMiniaturesToDb()
        miniatureDao.update(Miniature(1, "Nurgling", "GW", "Nurgle"))
        miniatureDao.update(Miniature(2, "Primaris Captain", "GW", "Dark Angels"))

        val allMiniatures = miniatureDao.getAllMiniatures().first()
        assertEquals(allMiniatures[0], Miniature(1, "Nurgling", "GW", "Nurgle"))
        assertEquals(allMiniatures[1], Miniature(2, "Primaris Captain", "GW", "Dark Angels"))
    }

    @Test
    @Throws(Exception::class)
    fun daoDeleteItems_deleteAllItemsFromDb() = runBlocking {
        addTwoMiniaturesToDb()
        miniatureDao.delete(miniature1)
        miniatureDao.delete(miniature2)

        val allMiniatures = miniatureDao.getAllMiniatures().first()
        assertTrue(allMiniatures.isEmpty())
    }

    @Test
    @Throws(Exception::class)
    fun daoGetItem_returnsItemFromDb() = runBlocking {
        addOneMiniatureToDb()
        val miniature = miniatureDao.getMiniature(1)
        assertEquals(miniature.first(), miniature1)
    }
}