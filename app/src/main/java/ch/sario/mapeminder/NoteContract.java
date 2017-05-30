package ch.sario.mapeminder;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

/**
 * All db functions to insert, select and delete data from the db.
 *
 * @version 1.0
 */

public class NoteContract {

    private SQLiteDatabase db;
    FeedReaderContract.FeedReaderDbHelper mDbHelper;

    /**
     * Check with the DBHelper if db exists and open it or
     * create the db.
     * @param activity, the current activity which the method called.
     */
    public void createOpenDB(Activity activity) {

        mDbHelper = new FeedReaderContract.FeedReaderDbHelper(activity.getApplicationContext());
        db = mDbHelper.getWritableDatabase();

    }

    /**
     * Insert data with the note object in db.
     * @param note, store content for column note, xcoords and ycoords.
     */
    public void insertNote(Note note){

        ContentValues values = new ContentValues();
        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_NOTE,note.getNote());
        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_XCOORDS, note.getLongitude());
        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_YCOORDS, note.getLatitude());
         /*Inserts Values */
        db.insert(FeedReaderContract.FeedEntry.TABLE_NAME, null, values);
    }

    /**
     * Developer function to delete all notes at once.
     */
    public void deleteAllNotes(){
        db.delete(FeedReaderContract.FeedEntry.TABLE_NAME, null, null);
    }


    /**
     * Read and show all notes from db.
     * @return note, ArrayList to handle data.
     */
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

    /**
     * Read and show note by an specific id.
     * @param idNote, unique id of a note to get one data row.
     * @return noteData, note object to handle the data.
     */
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

    /**
     * Delete a specific note from db selected by id.
     * @param idNote, unique id.
     */
    public void deleteNoteById(String idNote){
        String selection = FeedReaderContract.FeedEntry._ID + " = ?";
        String[] selectionArgs = { idNote };
        db.delete(FeedReaderContract.FeedEntry.TABLE_NAME, selection, selectionArgs);
    }

    /**
     * Close db connection.
     */
    public void closeNote(){
        mDbHelper.close();
    }

}
