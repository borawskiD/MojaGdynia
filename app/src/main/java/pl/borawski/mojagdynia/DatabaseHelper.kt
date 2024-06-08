package pl.borawski.mojagdynia

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

data class Beach(val id: Long, val name: String, val latitude: Double, val longitude: Double, val isFree: Boolean)

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(CREATE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    companion object {
        const val DATABASE_NAME = "mojaGdynia.db"
        const val DATABASE_VERSION = 1
        const val TABLE_NAME = "beaches"
        const val COLUMN_ID = "id"
        const val COLUMN_NAME = "name"
        const val COLUMN_LATITUDE = "latitude"
        const val COLUMN_LONGITUDE = "longitude"
        const val COLUMN_IS_FREE = "isFree"

        private const val CREATE_TABLE = "CREATE TABLE $TABLE_NAME (" +
                "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$COLUMN_NAME TEXT NOT NULL, " +
                "$COLUMN_LATITUDE REAL NOT NULL, " +
                "$COLUMN_LONGITUDE REAL NOT NULL, " +
                "$COLUMN_IS_FREE INTEGER NOT NULL)"
    }

    fun insertData(context: Context, name: String, latitude: Double, longitude: Double, isFree: Boolean): Long {
        val dbHelper = DatabaseHelper(context)
        val db = dbHelper.writableDatabase

        val values = ContentValues().apply {
            put(COLUMN_NAME, name)
            put(COLUMN_LATITUDE, latitude)
            put(COLUMN_LONGITUDE, longitude)
            put(COLUMN_IS_FREE, if (isFree) 1 else 0)
        }

        return db.insert(TABLE_NAME, null, values)
    }


    fun readData(context: Context): List<Beach> {
        val dbHelper = DatabaseHelper(context)
        val db = dbHelper.readableDatabase

        val projection = arrayOf(
            COLUMN_ID,
            COLUMN_NAME,
            COLUMN_LATITUDE,
            COLUMN_LONGITUDE,
            COLUMN_IS_FREE
        )
        val sortOrder = "$COLUMN_NAME ASC"

        val cursor: Cursor = db.query(
            DatabaseHelper.TABLE_NAME,
            projection,
            null,
            null,
            null,
            null,
            sortOrder
        )

        val beaches = mutableListOf<Beach>()
        with(cursor) {
            while (moveToNext()) {
                val id = getLong(getColumnIndexOrThrow(DatabaseHelper.COLUMN_ID))
                val name = getString(getColumnIndexOrThrow(COLUMN_NAME))
                val latitude = getDouble(getColumnIndexOrThrow(DatabaseHelper.COLUMN_LATITUDE))
                val longitude = getDouble(getColumnIndexOrThrow(DatabaseHelper.COLUMN_LONGITUDE))
                val isFree = getInt(getColumnIndexOrThrow(DatabaseHelper.COLUMN_IS_FREE)) == 1
                beaches.add(Beach(id, name, latitude, longitude, isFree))
            }
        }
        cursor.close()

        return beaches
    }

}