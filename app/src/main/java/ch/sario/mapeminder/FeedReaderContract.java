package ch.sario.mapeminder;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

/**
 * Handle db, define column and create/open db.
 *
 * @version 1.0
 */
public class FeedReaderContract {

    private FeedReaderContract() {}

    /**
     * Inner class that defines the table contents.
     */
    public static class FeedEntry implements BaseColumns {
        public static final String TABLE_NAME = "mm_notes";
        public static final String COLUMN_NAME_NOTE = "note";
        public static final String COLUMN_NAME_XCOORDS = "xcoords";
        public static final String COLUMN_NAME_YCOORDS = "ycoords";
    }

    /**
     * Create Table with 4 Columns (id, note, xccords, ycoords).
     */
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + FeedEntry.TABLE_NAME + " (" +
                    FeedEntry._ID + " INTEGER PRIMARY KEY," +
                    FeedEntry.COLUMN_NAME_NOTE + " TEXT," +
                    FeedEntry.COLUMN_NAME_XCOORDS + " TEXT," +
                    FeedEntry.COLUMN_NAME_YCOORDS + " TEXT)";

    /**
     * Delete the Table if it exists.
     */
    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + FeedEntry.TABLE_NAME;

    /**
     * SQLite Helper, create or open a db if exists or create a db.
     * If the table not exits, the helper create a table based on  SQL_CREATE_ENTRIES.
     * DB update automatically if new version is available.
     */
    public static class FeedReaderDbHelper extends SQLiteOpenHelper {
        // If you change the database schema, you must increment the database version.
        public static final int DATABASE_VERSION = 1;
        public static final String DATABASE_NAME = "mapeminder.db";

        public FeedReaderDbHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(SQL_CREATE_ENTRIES);
        }
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // This database is only a cache for online data, so its upgrade policy is
            // to simply to discard the data and start over
            db.execSQL(SQL_DELETE_ENTRIES);
            onCreate(db);
        }
        public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            onUpgrade(db, oldVersion, newVersion);
        }

    }

}
