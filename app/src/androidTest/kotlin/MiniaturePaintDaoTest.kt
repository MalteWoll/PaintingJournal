import android.content.Context
import android.net.Uri
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.paintingjournal.data.MiniatureDatabase
import com.example.paintingjournal.data.PaintDao
import com.example.paintingjournal.model.MiniaturePaint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException
import java.util.Date
import kotlin.jvm.Throws

@RunWith(AndroidJUnit4::class)
class MiniaturePaintDaoTest {
    private lateinit var paintDao: PaintDao
    private lateinit var miniatureDatabase: MiniatureDatabase

    private var paint1 = MiniaturePaint(1, "Gloomgitz Yellow", "Games Workshop", "Bright yellow", "Yellow", Uri.parse("uriString"), Date(1))
    private var paint2 = MiniaturePaint(2, "Wild Rider Red", "Games Workshop", "Bright red", "red", Uri.parse("uriString"), Date(1))

    private suspend fun addOnePaintToDb() {
        paintDao.insert(paint1)
    }

    private suspend fun addTwoPaintsToDb() {
        paintDao.insert(paint1)
        paintDao.insert(paint2)
    }

    @Before
    fun createDb() {
        val context: Context = ApplicationProvider.getApplicationContext()
        miniatureDatabase = Room.inMemoryDatabaseBuilder(context, MiniatureDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        paintDao = miniatureDatabase.paintDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        miniatureDatabase.close()
    }

    @Test
    @Throws(Exception::class)
    fun daoGetAllPaints_returnsAllPaintsFromDb() = runBlocking {
        addTwoPaintsToDb()
        val allPaints = paintDao.getAllPaints().first()
        Assert.assertEquals(allPaints[0], paint1)
        Assert.assertEquals(allPaints[1], paint2)
    }
}