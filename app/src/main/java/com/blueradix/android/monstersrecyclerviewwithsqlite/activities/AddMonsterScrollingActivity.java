package com.blueradix.android.monstersrecyclerviewwithsqlite.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.SeekBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.blueradix.android.monstersrecyclerviewwithsqlite.R;
import com.blueradix.android.monstersrecyclerviewwithsqlite.entities.Monster;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

public class AddMonsterScrollingActivity extends AppCompatActivity {

    private EditText monsterNameEditText;
    private EditText monsterDescriptionEditText;
    private SeekBar scarinessSeekBar;
    private MaterialButton cancelMonsterButton;
    private MaterialButton addMonsterButton;

    private Monster monster;
    private Integer scarinessValue = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_monster_scrolling);
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

        monsterNameEditText = findViewById(R.id.monsterNameEditText);
        monsterDescriptionEditText = findViewById(R.id.monsterDescriptionEditText);
        scarinessSeekBar = findViewById(R.id.scarinessSeekBar);
        cancelMonsterButton = findViewById(R.id.cancelMonsterButton);
        addMonsterButton = findViewById(R.id.addMonsterButton);

        cancelMonsterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancel(v);
            }
        });

        addMonsterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add(v);
            }
        });

        scarinessSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                scarinessValue = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private void add(View v) {
        //get values from the layout
        String name = monsterNameEditText.getText().toString();
        if(name.trim().isEmpty()){
            Snackbar.make(v, "Name is required", Snackbar.LENGTH_SHORT).show();
            monsterNameEditText.getText().clear();
            monsterNameEditText.requestFocus();
            return;
        }
        String description = monsterDescriptionEditText.getText().toString().trim();
        monster = new Monster();
        monster.setName(name);
        monster.setDescription(description);
        monster.setScariness(scarinessValue);

        //set the intent to return the monster to the caller activity
        Intent goingBack = new Intent();
        goingBack.putExtra(Monster.MONSTER_KEY, monster);
        setResult(RESULT_OK, goingBack);
        finish();
    }

    private void cancel(View v) {
        setResult(RESULT_CANCELED); // you can omit it, as if you don't set it, will be canceled by default
        finish();
    }
}
