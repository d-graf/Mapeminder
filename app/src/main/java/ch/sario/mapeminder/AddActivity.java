package ch.sario.mapeminder;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class AddActivity extends AppCompatActivity {

    private Note note = new Note();

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
        String x = intent.getStringExtra("x");
        String y = intent.getStringExtra("y");

        TextView tx = (TextView) findViewById(R.id.longitudeX);
        tx.setText("x: " + x);

        TextView ty = (TextView) findViewById(R.id.latitudeY);
        ty.setText("y: " + y);

    }

    private void saveNote(){
        Intent intent = getIntent();
        String x = intent.getStringExtra("x");
        String y = intent.getStringExtra("y");

        EditText noteTxt = (EditText) findViewById(R.id.taNote);
        String str = noteTxt.getText().toString();

        note.createOpenDB(this);
        note.insertNote(str, x, y);
        note.closeNote();

    }


}
