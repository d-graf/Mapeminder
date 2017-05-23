package ch.sario.mapeminder;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import static android.database.sqlite.SQLiteDatabase.openOrCreateDatabase;

public class Database {

    SQLiteDatabase mydatabase = openOrCreateDatabase("mapeminder",null);

    public void addNote(String note, double xCoords, double yCoords){

        mydatabase.execSQL("CREATE TABLE IF NOT EXISTS mm_notes(id INT AUTO_INCREMENT, note VARCHAR,xcoords NUMERIC, ycoords NUMERIC);");
        mydatabase.execSQL("INSERT INTO mm_notes VALUES('"+ note + "','"+ xCoords +"','"+ yCoords +"');");

    }

    public void showAllNotes(){
        Cursor resultSet = mydatabase.rawQuery("SELECT id, note FROM mm_notes",null);
        resultSet.moveToFirst();
        String id = resultSet.getString(0);
        String note = resultSet.getString(1);
    }

    public void showNote(){
        Cursor resultSet = mydatabase.rawQuery("SELECT note, xcoords, ycoords FROM mm_notes",null);
        resultSet.moveToFirst();
        String note = resultSet.getString(0);
        String xcoords = resultSet.getString(1);
        String ycoords = resultSet.getString(2);
    }


}
