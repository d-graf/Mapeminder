package ch.sario.mapeminder;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

public class Note{

    private SQLiteDatabase db;
    FeedReaderContract.FeedReaderDbHelper mDbHelper;

    public void createOpenDB(Activity activity) {

        mDbHelper = new FeedReaderContract.FeedReaderDbHelper(activity.getApplicationContext());
        db = mDbHelper.getWritableDatabase();

    }

    public void insertNote(String str, String x, String y){

        ContentValues values = new ContentValues();
        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_NOTE,str);
        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_XCOORDS, x);
        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_YCOORDS, y);

        long newRowId = db.insert(FeedReaderContract.FeedEntry.TABLE_NAME, null, values);
    }


    public void deleteAllNotes(){
        db.delete(FeedReaderContract.FeedEntry.TABLE_NAME, null, null);
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

    public ArrayList getNoteById(String idNote) {

        ArrayList<String> output = new ArrayList<>();

        String[] projection = {
                FeedReaderContract.FeedEntry.COLUMN_NAME_NOTE,
                FeedReaderContract.FeedEntry.COLUMN_NAME_XCOORDS,
                FeedReaderContract.FeedEntry.COLUMN_NAME_XCOORDS
        };

        String selection = FeedReaderContract.FeedEntry._ID + " = ?";
        String[] selectionArgs = { "1" };

         Cursor cursor = db.query(
                FeedReaderContract.FeedEntry.TABLE_NAME,        // The table to query
                projection,                                     // The columns to return
                selection,                                      // The columns for the WHERE clause
                null ,                                  // The values for the WHERE clause
                null,                                           // don't group the rows
                null,                                           // don't filter by row groups
                null                                            // The sort order
        );

        String note = cursor.getString(cursor.getColumnIndex(FeedReaderContract.FeedEntry.COLUMN_NAME_NOTE));
        String xcoords = cursor.getString(cursor.getColumnIndex(FeedReaderContract.FeedEntry.COLUMN_NAME_XCOORDS));
        String ycoords = cursor.getString(cursor.getColumnIndex(FeedReaderContract.FeedEntry.COLUMN_NAME_YCOORDS));

        output.add(note);
        output.add(xcoords);
        output.add(ycoords);

        cursor.close();

        return output;

    }

    public ArrayList getAllCoords(){

        ArrayList<String> output = new ArrayList<>();

        String[] projection = {
                FeedReaderContract.FeedEntry.COLUMN_NAME_XCOORDS,
                FeedReaderContract.FeedEntry.COLUMN_NAME_YCOORDS
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
            String x = cursor.getString(cursor.getColumnIndex(FeedReaderContract.FeedEntry.COLUMN_NAME_XCOORDS));
            String y = cursor.getString(cursor.getColumnIndex(FeedReaderContract.FeedEntry.COLUMN_NAME_YCOORDS));

            output.add(x + ":" + y);
        }
        cursor.close();

        return output;
    }

    /**
     *
     *
     * notelist = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
       note.createOpenDB(this);
       ArrayList<String> output = note.getAllCoords();

       for(int i = 0; i < output.size(); i++) {
         String[] selectedParts = output.split(":");
         String x = selectedParts[0];
         String y = selectedParts[0];
         addMarker(x,y);
     }

     note.closeNote();
     *
     */



    public void closeNote(){
        mDbHelper.close();
    }

}
