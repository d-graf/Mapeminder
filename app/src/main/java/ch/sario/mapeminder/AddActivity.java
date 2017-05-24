package ch.sario.mapeminder;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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

    }

    private void saveNote(){

        EditText noteTxt = (EditText) findViewById(R.id.taNote);
        int x = 0, y = 0;
        String str = noteTxt.getText().toString();

        note.createOpenDB(this);
        note.insertNote(str, x, y);
        note.closeNote();

    }


}
