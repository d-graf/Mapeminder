package ch.sario.mapeminder;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.ArrayList;

public class DetailActivity extends AppCompatActivity {

    private NoteContract noteContract = new NoteContract();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent intent = getIntent();
        String idNote = intent.getStringExtra("id");
        addNoteToTextView(idNote);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu_home) {
        getMenuInflater().inflate(R.menu.menu_home, menu_home);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        clickOverview();
        return true;
    }

    private void clickOverview(){
        Intent intent = new Intent(getApplicationContext(), OverviewActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

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
