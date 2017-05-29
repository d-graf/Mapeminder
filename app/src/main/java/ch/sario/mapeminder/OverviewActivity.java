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

    @Override
    public boolean onCreateOptionsMenu(Menu menu_home) {
        getMenuInflater().inflate(R.menu.menu_home, menu_home);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        clickMaps();
        return true;
    }

    private void clickMaps(){
        Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    private void addNotesToList(){

        notelist = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);

        note.createOpenDB(this);
        ArrayList<String> output = note.getAllNotes();

        for(int i = 0; i < output.size(); i++) {
            String str = output.get(i);
            if (str.length() > 50){
                notelist.add(str.substring(0, 45) + "...");
            }else {
                notelist.add(str);
            }

        }

        note.closeNote();

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

    }
}
