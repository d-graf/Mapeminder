package ch.sario.mapeminder;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;

public class AddActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
    }

    private void saveNote(){
        Database db = new Database();

        EditText noteTxt = (EditText) findViewById(R.id.taNote);
        int x = 0, y = 0;

        String str = noteTxt.getText().toString();
        db.addNote(str, x, y );
    }


}
