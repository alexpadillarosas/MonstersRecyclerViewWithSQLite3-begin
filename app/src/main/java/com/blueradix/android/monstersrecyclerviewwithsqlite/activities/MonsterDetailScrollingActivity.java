package com.blueradix.android.monstersrecyclerviewwithsqlite.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.blueradix.android.monstersrecyclerviewwithsqlite.R;
import com.blueradix.android.monstersrecyclerviewwithsqlite.entities.Monster;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

public class MonsterDetailScrollingActivity extends AppCompatActivity {

    RatingBar ratingBar;
    Monster monster;
    Integer rate = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monster_detail_scrolling);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setIntentWithData();
            }
        });


        ImageView monsterImageView = findViewById(R.id.monsterImageViewDetailActivity);
        TextView monsterNameTextView = findViewById(R.id.monsterNameTextViewDetailActivity);
        ratingBar = findViewById(R.id.monsterRatingBarDetailActivity);

        Intent intentThatCalled = getIntent();
        if(intentThatCalled.hasExtra(Monster.MONSTER_KEY)){
            monster = (Monster) intentThatCalled.getSerializableExtra(Monster.MONSTER_KEY);
        }

        monsterNameTextView.setText(monster.getName());
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                rate = (int) ratingBar.getRating();
            }
        });
        View rootView = monsterImageView.getRootView();
        int resID = rootView.getResources().getIdentifier(monster.imageFileName , "drawable" , rootView.getContext().getPackageName()) ;
        monsterImageView.setImageResource(resID);

    }

    @Override
    public void onBackPressed() {
        //before you go back do something
        setIntentWithData();
        super.onBackPressed();
    }

    private void setIntentWithData() {
        Intent goingBack = new Intent();
        goingBack.putExtra(Monster.MONSTER_KEY, monster);
        goingBack.putExtra(Monster.MONSTER_STARS,rate );
        goingBack.putExtra(Monster.MONSTER_ID, monster.getId());

        setResult(RESULT_OK, goingBack);
        finish();
    }
}
