package ch.sario.mapeminder;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

public class NoteContract {

    private SQLiteDatabase db;
    FeedReaderContract.FeedReaderDbHelper mDbHelper;

    /*Check if db exists*/
    public void createOpenDB(Activity activity) {

        mDbHelper = new FeedReaderContract.FeedReaderDbHelper(activity.getApplicationContext());
        db = mDbHelper.getWritableDatabase();

    }

    /*Insert Content in db*/
    public void insertNote(String str, String x, String y){

        ContentValues values = new ContentValues();
        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_NOTE,str);
        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_XCOORDS, x);
        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_YCOORDS, y);
         /*Inserts Values */
        db.insert(FeedReaderContract.FeedEntry.TABLE_NAME, null, values);
    }

    public void deleteAllNotes(){
        db.delete(FeedReaderContract.FeedEntry.TABLE_NAME, null, null);
    }

    public ArrayList<Note> getAllNotes(){

        ArrayList<Note> notes = new ArrayList<>();

        String[] projection = {
                FeedReaderContract.FeedEntry._ID,
                FeedReaderContract.FeedEntry.COLUMN_NAME_XCOORDS,
                FeedReaderContract.FeedEntry.COLUMN_NAME_YCOORDS,
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
            String x = cursor.getString(cursor.getColumnIndex(FeedReaderContract.FeedEntry.COLUMN_NAME_XCOORDS));
            String y = cursor.getString(cursor.getColumnIndex(FeedReaderContract.FeedEntry.COLUMN_NAME_YCOORDS));
            String note = cursor.getString(cursor.getColumnIndex(FeedReaderContract.FeedEntry.COLUMN_NAME_NOTE));

            Note noteData = new Note(id, y, x, note);
            notes.add(noteData);

        }
        cursor.close();

        return notes;
    }

    public Note getNoteById(String idNote) {

        String[] projection = {
                FeedReaderContract.FeedEntry._ID,
                FeedReaderContract.FeedEntry.COLUMN_NAME_XCOORDS,
                FeedReaderContract.FeedEntry.COLUMN_NAME_YCOORDS,
                FeedReaderContract.FeedEntry.COLUMN_NAME_NOTE
        };

        String selection = FeedReaderContract.FeedEntry._ID + " = ?";
        String[] selectionArgs = { idNote };

        Cursor cursor = db.query(
                FeedReaderContract.FeedEntry.TABLE_NAME,        // The table to query
                projection,                                     // The columns to return
                selection,                                           // The columns for the WHERE clause
                selectionArgs,                                           // The values for the WHERE clause
                null,                                           // don't group the rows
                null,                                           // don't filter by row groups
                null                                            // The sort order
        );

        cursor.moveToFirst();
        String id = cursor.getString(cursor.getColumnIndex(FeedReaderContract.FeedEntry._ID));
        String x = cursor.getString(cursor.getColumnIndex(FeedReaderContract.FeedEntry.COLUMN_NAME_XCOORDS));
        String y = cursor.getString(cursor.getColumnIndex(FeedReaderContract.FeedEntry.COLUMN_NAME_YCOORDS));
        String note = cursor.getString(cursor.getColumnIndex(FeedReaderContract.FeedEntry.COLUMN_NAME_NOTE));

        Note noteData = new Note(id, y, x, note);

        cursor.close();

        return noteData;

    }



    public void closeNote(){
        mDbHelper.close();
    }

}
