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

/** TODO:   Add a listener of each button inside the recyclerView
 *      1) modify the ViewHolder, add the listener inside of the bind method
 */

/** TODO:    Add a splash screen
 *      1) create a theme for your splash screen in styles.xml: be aware it should not have action bar in the current theme:  NoActionBar
 *      2) add it to your AndroidManifest.xml
 *      3) in MainActitivy.java restore the old theme, otherwise you will see the splash screen as a background image
 */

/**
 *  TODO:   set a new Material theme for your app
 *      1) go to any of these websites to select a pre defined set of colors, or create your own one
 *          https://www.materialpalette.com/
 *          https://material.io/resources/color/#!/?view.left=0&view.right=0
 *          https://codecrafted.net/randommaterial
 *          additional info: https://material.io/develop/android
 *      2) Export the colors as xml files
 *      3) replace those colors in your colors.xml file ( be aware that android uses as color names:
 *          colorPrimary, colorPrimaryDark and colorAccent
 */

/** TODO:  Internationalisation
 *      1) Remove all hardcoded text in the app, create a key for every text in strings.xml
 *      2) right click on the file -> Open Translation Editor, click the globe icon, select the
 *          language you want to translate the file to, and fill up the translations
 *      3) Finally change the language of your device to test the internationalisation:
 *          settings -> Language & Input -> Language
 *          and set the new language for your device.
 *          Run your app and check the new language of your app
 */

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
                if(position >= 0){
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
