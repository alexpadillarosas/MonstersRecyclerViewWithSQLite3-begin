package com.blueradix.android.monstersrecyclerviewwithsqlite;

import android.content.Intent;
import android.os.Bundle;

import com.blueradix.android.monstersrecyclerviewwithsqlite.activities.AddMonsterScrollingActivity;
import com.blueradix.android.monstersrecyclerviewwithsqlite.activities.MonsterDetailScrollingActivity;
import com.blueradix.android.monstersrecyclerviewwithsqlite.entities.Monster;
import com.blueradix.android.monstersrecyclerviewwithsqlite.recyclerview.MonsterRecyclerViewAdapter;
import com.blueradix.android.monstersrecyclerviewwithsqlite.recyclerview.OnMonsterListener;
import com.blueradix.android.monstersrecyclerviewwithsqlite.service.DataService;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import java.util.List;

import static com.blueradix.android.monstersrecyclerviewwithsqlite.entities.Constants.ADD_MONSTER_ACTIVITY_CODE;
import static com.blueradix.android.monstersrecyclerviewwithsqlite.entities.Constants.VIEW_DETAILS_ACTIVITY_CODE;

public class MainActivity extends AppCompatActivity implements OnMonsterListener {

    private List<Monster> monsters;
    private MonsterRecyclerViewAdapter adapter;
    private DataService monsterDataService;
    private View rootView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //https://developer.android.com/topic/libraries/view-binding?utm_medium=studio-assistant-stable&utm_source=android-studio-3-6
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addNewMonster();
            }
        });
        rootView = findViewById(android.R.id.content).getRootView();

        RecyclerView monstersRecyclerView = findViewById(R.id.monstersRecyclerView);

        //set the layout manager
        //monstersRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);

//        monstersRecyclerView.setLayoutManager(gridLayoutManager);
        monstersRecyclerView.setLayoutManager(linearLayoutManager);

        monsterDataService = new DataService();
        monsterDataService.init(this);
        //once your database is created, you can find it using Device File Explorer
        //go to: data/data/app_package_name/databases there you will find your databases

        //Load Data from the database
        monsters = monsterDataService.getMonsters();
        //create adapter passing the data, and the context
        adapter = new MonsterRecyclerViewAdapter(monsters, this, this);
        //attach the adapter to the Recyclerview
        monstersRecyclerView.setAdapter(adapter);

    }

    private void addNewMonster() {
        Intent goToAddCreateMonster = new Intent(this, AddMonsterScrollingActivity.class);
        startActivityForResult(goToAddCreateMonster, ADD_MONSTER_ACTIVITY_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == ADD_MONSTER_ACTIVITY_CODE){
            if(resultCode == RESULT_OK){
                addMonster(data);
            }
        }
        if( requestCode == VIEW_DETAILS_ACTIVITY_CODE){
            if(resultCode == RESULT_OK){
                modifyMonster(data);
            }
        }
    }

    private void modifyMonster(Intent data) {
        Integer stars;
        Long id;
        if(data.hasExtra(Monster.MONSTER_KEY) && data.hasExtra(Monster.MONSTER_STARS)){
            Monster monster = (Monster)data.getSerializableExtra(Monster.MONSTER_KEY);
            stars = data.getExtras().getInt(Monster.MONSTER_STARS);
            id = data.getExtras().getLong(Monster.MONSTER_ID);
            if(stars > 0){
                boolean result = monsterDataService.rateMonster(id, stars);
                //find the monster in the list
                int position = adapter.getMonsters().indexOf(monster);
                if(position > 0){
                    monster = monsterDataService.getMonster(id);
                    adapter.replaceItem(position, monster);
                }
            }
        }
    }

    private void addMonster(Intent data) {
        String message;
        Monster monster = (Monster) data.getSerializableExtra(Monster.MONSTER_KEY);
        //insert your monster into the DB
        Long result = monsterDataService.add(monster);
        //result holds the autogenerated id in the table
        if(result > 0){

            Monster monster1 = monsterDataService.getMonster(result);
            adapter.addItem(monster1);

            message = "Your monster was created with id: "+ result;
        }else{
            message = "We couldn't create your monster, try again";
        }
        Snackbar.make(rootView, message, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMonsterClick(Monster monster) {
        showMonsterDetail(monster);
    }

    private void showMonsterDetail(Monster monster) {
        Intent goToMonsterDetail = new Intent(this, MonsterDetailScrollingActivity.class);
        goToMonsterDetail.putExtra(Monster.MONSTER_KEY, monster);

        startActivityForResult(goToMonsterDetail, VIEW_DETAILS_ACTIVITY_CODE);
    }
}
