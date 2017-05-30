package ch.sario.mapeminder;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

/**
 * Activity to show one specific note.
 *
 * @version 1.0
 */
public class DetailActivity extends AppCompatActivity {

    /**
     * instantiate NoteContract to handle db functions.
     */
    private NoteContract noteContract = new NoteContract();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent intent = getIntent();
        final String idNote = intent.getStringExtra("id");
        addNoteToTextView(idNote);
    }

    /**
     * integrate the menu menu_home.xml
     * @param menu_home, menu xml to go back
     * @return true
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu_home) {
        getMenuInflater().inflate(R.menu.menu_home, menu_home);
        return true;
    }

    /**
     * on item click call clickOverview().
     * @param item, item of the menu in the right corner.
     * @return true.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        clickOverview();
        return true;
    }

    /**
     * Go back to the OverviewActivity with a fade animation.
     */
    private void clickOverview(){
        Intent intent = new Intent(getApplicationContext(), OverviewActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    /**
     * Select note by id and add data into textviews.
     * @param idNote, unique id.
     */
    private void addNoteToTextView(String idNote){

        noteContract.createOpenDB(this);

        Note note = noteContract.getNoteById(idNote);

        TextView xCoords = (TextView) findViewById(R.id.txtXCoords);
        xCoords.setText("x: " + note.getLongitude());

        TextView yCoords = (TextView) findViewById(R.id.txtYCoords);
        yCoords.setText("y: " +  note.getLatitude());

        TextView noteBox = (TextView) findViewById(R.id.txtNote);
        noteBox.setText(note.getNote());

        noteContract.closeNote();


    }
}
