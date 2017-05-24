package ch.sario.mapeminder;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class OverviewActivity extends AppCompatActivity {

    ArrayAdapter notelist;
    private ListView notes;
    private Note note = new Note();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview);

        notes = (ListView) findViewById(R.id.notelist);

        addNotesToList();
    }

    private void addNotesToList(){

        notelist = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);

        note.createOpenDB(this);
        ArrayList<String> output = note.getAllNotes();

        for(int i = 0; i < output.size(); i++) {
            notelist.add(output.get(i));
        }

        note.closeNote();

        notes.setAdapter(notelist);

        AdapterView.OnItemClickListener mListClickedHandler = new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), DetailActivity.class);

                String selectedFromList = (notes.getItemAtPosition(position).toString());
                String[] selectedParts = selectedFromList.split(".");
                String selectedId = selectedParts[0];


                Toast.makeText(OverviewActivity.this, selectedId, Toast.LENGTH_SHORT).show();

                intent.putExtra("id",id);
                startActivity(intent);
            }
        };
        notes.setOnItemClickListener(mListClickedHandler);

    }
}
