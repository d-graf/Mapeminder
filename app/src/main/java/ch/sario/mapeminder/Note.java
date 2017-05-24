package ch.sario.mapeminder;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class Note{

    private SQLiteDatabase db;
    FeedReaderContract.FeedReaderDbHelper mDbHelper;

    public void createOpenDB(Activity activity) {

        mDbHelper = new FeedReaderContract.FeedReaderDbHelper(activity.getApplicationContext());
        db = mDbHelper.getWritableDatabase();

    }

    public void insertNote(String str, int x, int y){

        ContentValues values = new ContentValues();
        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_NOTE,str);
        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_XCOORDS, x);
        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_XCOORDS, y);

        long newRowId = db.insert(FeedReaderContract.FeedEntry.TABLE_NAME, null, values);
    }

    public ArrayList getAllNotes(){

        ArrayList<String> output = new ArrayList<>();

        String[] projection = {
                FeedReaderContract.FeedEntry._ID,
                FeedReaderContract.FeedEntry.COLUMN_NAME_NOTE
        };

        Cursor cursor = db.query(
                FeedReaderContract.FeedEntry.TABLE_NAME,        // The table to query
                projection,                                     // The columns to return
                null,                                           // The columns for the WHERE clause
                null,                                           // The values for the WHERE clause
                null,                                           // don't group the rows
                null,                                           // don't filter by row groups
                null                                            // The sort order
        );

        while(cursor.moveToNext()) {
            String id = cursor.getString(cursor.getColumnIndex(FeedReaderContract.FeedEntry._ID));
            String note = cursor.getString(cursor.getColumnIndex(FeedReaderContract.FeedEntry.COLUMN_NAME_NOTE));

            output.add(id + ": " + note);
        }
        cursor.close();

        return output;
    }

    public void closeNote(){
        mDbHelper.close();
    }

}
