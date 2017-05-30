package ch.sario.mapeminder;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Activity to show one specific note.
 *
 * @version 1.0
 */
public class OverviewActivity extends AppCompatActivity {

    ArrayAdapter notelist;
    /**
     * List all row from db.
     */
    private ListView notes;
    /**
     * instantiate NoteContract to handle db functions.
     */
    private NoteContract noteContract = new NoteContract();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview);

        notes = (ListView) findViewById(R.id.notelist);
        addNotesToList();
    }

    /**
     * integrate the menu menu_home.xml
     * @param menu_home, menu xml to go back.
     * @return true.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu_home) {
        getMenuInflater().inflate(R.menu.menu_home, menu_home);
        return true;
    }

    /**
     * on item click call clickMaps().
     * @param item, item of the menu in the right corner.
     * @return true.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        clickMaps();
        return true;
    }

    /**
     * Go back to the MapsActivity with a fade animation.
     */
    private void clickMaps(){
        Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    /**
     * Delete a note by an specific id.
     * @param idNote, unique id.
     * @return true.
     */
    private boolean deleteNote(String idNote) {
        noteContract.createOpenDB(this);
        noteContract.deleteNoteById(idNote);
        noteContract.closeNote();

        return true;
    }

    /**
     * Select all notes from db and add per row a new listitem.
     * On long click the item will be deleted.
     */
    private void addNotesToList(){

        notelist = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);

        noteContract.createOpenDB(this);
        ArrayList<Note> allNotes = noteContract.getAllNotes();

        for(Note note : allNotes) {

            String noteText = note.getId() + ": " + note.getNote();

            if (noteText.length() > 45){
                notelist.add(noteText.substring(0, 45) + "...");
            }else {
                notelist.add(noteText);
            }
        }

        noteContract.closeNote();

        notes.setAdapter(notelist);

        AdapterView.OnItemClickListener mListClickedHandler = new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), DetailActivity.class);

                String selectedFromList = (notes.getItemAtPosition(position).toString());
                String[] selectedParts = selectedFromList.split(":");
                String selectedId = selectedParts[0];

                Toast.makeText(OverviewActivity.this, selectedId, Toast.LENGTH_SHORT).show();

                intent.putExtra("id",selectedId);
                startActivity(intent);
            }
        };
        notes.setOnItemClickListener(mListClickedHandler);

        AdapterView.OnItemLongClickListener mOnItemLongClickListener = new AdapterView.OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView parent, View v, int position, long id) {
                String selectedFromList = (notes.getItemAtPosition(position).toString());
                String[] selectedParts = selectedFromList.split(":");
                String selectedId = selectedParts[0];

                deleteNote(selectedId);

                Toast.makeText(OverviewActivity.this, "Notiz wurde gelöscht...", Toast.LENGTH_SHORT).show();
                finish();
                startActivity(getIntent());
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

                return true;
            }
        };
        notes.setOnItemLongClickListener(mOnItemLongClickListener);
    }
}
