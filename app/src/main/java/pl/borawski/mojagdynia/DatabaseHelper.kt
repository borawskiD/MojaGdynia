import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

data class Place(
    val id: Long,
    val name: String,
    val address: String,
    val latitude: Double,
    val longitude: Double,
    val isFree: Boolean
)

class DatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(CREATE_TABLE_BEACHES)
        db.execSQL(CREATE_TABLE_ATTRACTIONS)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME_BEACHES")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME_ATTRACTIONS")
        onCreate(db)
    }

    fun insertBeach(context: Context, name: String, address: String, latitude: Double, longitude: Double, isFree: Boolean): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NAME, name)
            put(COLUMN_ADDRESS, address)
            put(COLUMN_LATITUDE, latitude)
            put(COLUMN_LONGITUDE, longitude)
            put(COLUMN_IS_FREE, if (isFree) 1 else 0)
        }
        return db.insert(TABLE_NAME_BEACHES, null, values)
    }

    fun insertAttraction(context: Context, name: String, address: String, latitude: Double, longitude: Double, isFree: Boolean): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NAME, name)
            put(COLUMN_ADDRESS, address)
            put(COLUMN_LATITUDE, latitude)
            put(COLUMN_LONGITUDE, longitude)
            put(COLUMN_IS_FREE, if (isFree) 1 else 0)
        }
        return db.insert(TABLE_NAME_ATTRACTIONS, null, values)
    }

    fun clearAndPopulateDatabase() {
        val db = writableDatabase
        db.execSQL("DELETE FROM $TABLE_NAME_BEACHES")
        db.execSQL("DELETE FROM $TABLE_NAME_ATTRACTIONS")

        val insertBeaches = """
            INSERT INTO $TABLE_NAME_BEACHES ($COLUMN_NAME, $COLUMN_ADDRESS, $COLUMN_LATITUDE, $COLUMN_LONGITUDE, $COLUMN_IS_FREE) VALUES 
            ('Babie doły', 'Oksywie', 54.578936, 18.544514, 1),
            ('Osada Rybacka', 'Oksywie', 54.559003, 18.552608, 1),
            ('Plaża na Redłowie', 'Redłowo', 54.489928, 18.566247, 1),
            ('Plaża miejska', 'Centrum', 54.516211, 18.548667, 1),
            ('Plaża Kamienna Góra', 'Kamienna Góra', 54.508208, 18.554214, 1),
            ('Plaża Orłowo', 'Orłowo', 54.479839, 18.563764, 1),
            ('Plaża przy Sopocie', 'Orłowo', 54.464161, 18.561353, 1)
        """
        db.execSQL(insertBeaches)

        val insertAttractions = """
            INSERT INTO $TABLE_NAME_ATTRACTIONS ($COLUMN_NAME, $COLUMN_ADDRESS, $COLUMN_LATITUDE, $COLUMN_LONGITUDE, $COLUMN_IS_FREE) VALUES 
            ('Muzeum Emigracji', 'Port', 54.533048, 18.548012, 0),
            ('Muzeum Marynarki Wojennej', 'Centrum', 54.515464, 18.547990, 0),
            ('Marina', 'Centrum', 54.516971, 18.552636, 1),
            ('Skwer Kościuszki', 'Centrum', 54.519058, 18.547947, 1),
            ('Klif Orłowski', 'Orłowo', 54.484832, 18.567217, 1),
            ('Molo w Orłowie', 'Orłowo', 54.480928, 18.563909, 1)
        """
        db.execSQL(insertAttractions)
    }

    fun readBeachData(context: Context): List<Place> {
        val db = readableDatabase
        val projection = arrayOf(
            COLUMN_ID,
            COLUMN_NAME,
            COLUMN_ADDRESS,
            COLUMN_LATITUDE,
            COLUMN_LONGITUDE,
            COLUMN_IS_FREE
        )
        val sortOrder = "$COLUMN_NAME ASC"
        val cursor: Cursor = db.query(
            TABLE_NAME_BEACHES,
            projection,
            null,
            null,
            null,
            null,
            sortOrder
        )
        return parseCursor(cursor)
    }

    fun readAttractionData(context: Context): List<Place> {
        val db = readableDatabase
        val projection = arrayOf(
            COLUMN_ID,
            COLUMN_NAME,
            COLUMN_ADDRESS,
            COLUMN_LATITUDE,
            COLUMN_LONGITUDE,
            COLUMN_IS_FREE
        )
        val sortOrder = "$COLUMN_NAME ASC"
        val cursor: Cursor = db.query(
            TABLE_NAME_ATTRACTIONS,
            projection,
            null,
            null,
            null,
            null,
            sortOrder
        )
        return parseCursor(cursor)
    }

    private fun parseCursor(cursor: Cursor): List<Place> {
        val places = mutableListOf<Place>()
        with(cursor) {
            while (moveToNext()) {
                val id = getLong(getColumnIndexOrThrow(COLUMN_ID))
                val name = getString(getColumnIndexOrThrow(COLUMN_NAME))
                val address = getString(getColumnIndexOrThrow(COLUMN_ADDRESS))
                val latitude = getDouble(getColumnIndexOrThrow(COLUMN_LATITUDE))
                val longitude = getDouble(getColumnIndexOrThrow(COLUMN_LONGITUDE))
                val isFree = getInt(getColumnIndexOrThrow(COLUMN_IS_FREE)) == 1
                places.add(Place(id, name, address, latitude, longitude, isFree))
            }
        }
        cursor.close()
        return places
    }

    companion object {
        private const val DATABASE_NAME = "mojaGdynia.db"
        private const val DATABASE_VERSION = 3
        private const val TABLE_NAME_BEACHES = "beaches"
        private const val TABLE_NAME_ATTRACTIONS = "attractions"
        private const val COLUMN_ID = "id"
        private const val COLUMN_NAME = "name"
        private const val COLUMN_ADDRESS = "address"
        private const val COLUMN_LATITUDE = "latitude"
        private const val COLUMN_LONGITUDE = "longitude"
        private const val COLUMN_IS_FREE = "isFree"

        private const val CREATE_TABLE_BEACHES = "CREATE TABLE $TABLE_NAME_BEACHES (" +
                "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$COLUMN_NAME TEXT NOT NULL, " +
                "$COLUMN_ADDRESS TEXT NOT NULL, " +
                "$COLUMN_LATITUDE REAL NOT NULL, " +
                "$COLUMN_LONGITUDE REAL NOT NULL, " +
                "$COLUMN_IS_FREE INTEGER NOT NULL)"

    private const val CREATE_TABLE_ATTRACTIONS = "CREATE TABLE $TABLE_NAME_ATTRACTIONS (" +
                    "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "$COLUMN_NAME TEXT NOT NULL, " +
                    "$COLUMN_ADDRESS TEXT NOT NULL, " +
                    "$COLUMN_LATITUDE REAL NOT NULL, " +
                    "$COLUMN_LONGITUDE REAL NOT NULL, " +
                    "$COLUMN_IS_FREE INTEGER NOT NULL)"
    }
}
