package ch.sario.mapeminder;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class DetailActivity extends AppCompatActivity {

    private Note note = new Note();
    private TextView txtNote, txtXCoords, txtYCoords;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent intent = getIntent();
        final String idNote = intent.getStringExtra("id");
        addNoteToTextView(idNote);
    }

    protected void onLongClick(View v){


    }

    private void addNoteToTextView(String idNote){

        note.createOpenDB(this);

        ArrayList<String> output = note.getNoteById(idNote);

        TextView xCoords = (TextView) findViewById(R.id.txtXCoords);
        xCoords.setText("x: " + output.get(0));

        TextView yCoords = (TextView) findViewById(R.id.txtYCoords);
        yCoords.setText("y: " +  output.get(1));

        TextView noteBox = (TextView) findViewById(R.id.txtNote);
        noteBox.setText(output.get(2));

        note.closeNote();


    }
}
