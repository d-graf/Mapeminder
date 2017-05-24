package ch.sario.mapeminder;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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

        Intent intent = new Intent();
        String idNote = intent.getStringExtra("id");

        addNoteToTextView(idNote);
    }

    private void addNoteToTextView(String idNote){


        note.createOpenDB(this);

        ArrayList<String> output = note.getNoteById(idNote);

        txtNote.setText(output.get(0));
        txtXCoords.setText(output.get(1));
        txtYCoords.setText(output.get(2));

        note.closeNote();


    }
}
