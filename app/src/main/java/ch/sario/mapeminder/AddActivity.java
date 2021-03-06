package ch.sario.mapeminder;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Activity to add a note.
 *
 * @version 1.0
 */

public class AddActivity extends AppCompatActivity {

    private Double x, y;

    private NoteContract noteContract = new NoteContract();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        Button button = (Button) findViewById(R.id.btnSave);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                saveNote();
            }
        });

        Intent intent = getIntent();
        Double x = intent.getDoubleExtra("x", 0.0);
        Double y = intent.getDoubleExtra("y", 0.0);

        TextView tx = (TextView) findViewById(R.id.longitudeX);
        tx.setText("x: " + x);

        TextView ty = (TextView) findViewById(R.id.latitudeY);
        ty.setText("y: " + y);
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
     * on item click call clickMaps()
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
     * instantiate a note and call insert function from
     * NoteContract.
     */
    private void saveNote(){
        Intent intent = getIntent();
        x = intent.getDoubleExtra("x", 0.0);
        y = intent.getDoubleExtra("y", 0.0);

        EditText noteTxt = (EditText) findViewById(R.id.taNote);
        String note = noteTxt.getText().toString();

        Note noteData = new Note(Double.toString(y), Double.toString(x), note);
        noteContract.createOpenDB(this);
        noteContract.insertNote(noteData);
        noteContract.closeNote();

        goToMap();
    }

    /**
     *  Go back to the MapsActivity and send a toast.
     */
    private void goToMap(){
        Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
        Toast.makeText(AddActivity.this, "Notiz gespeichert...", Toast.LENGTH_SHORT).show();
        intent.putExtra("x", x);
        intent.putExtra("y", y);
        setResult(RESULT_OK, intent);
        startActivity(intent);
    }
}
