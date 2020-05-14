package com.blueradix.android.monstersrecyclerviewwithsqlite.recyclerview;


import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.blueradix.android.monstersrecyclerviewwithsqlite.R;
import com.blueradix.android.monstersrecyclerviewwithsqlite.entities.Monster;

/**
 * We create this view holder representing the recycler_item_view.xml
 * The idea of this class is to create a class that can manipulate the view
 */
public class MonsterViewHolder extends RecyclerView.ViewHolder {

    //bind the recycler_item_view.xml elements
    public final ImageView monsterImageView;
    public final TextView monsterNameEditText;
    public final TextView monsterDescriptionEditText;
    public final TextView monsterTotalVotesTextView;
    public RatingBar ratingBar = null;
    public Button actionButton1;

    private OnMonsterListener onMonsterListener;

    public MonsterViewHolder(@NonNull View itemView, OnMonsterListener onMonsterListener) {
        super(itemView);

        monsterImageView = itemView.findViewById(R.id.monsterImageView);
        monsterNameEditText = itemView.findViewById(R.id.monsterNameEditText);
        monsterDescriptionEditText = itemView.findViewById(R.id.monsterDescriptionEditText);
        monsterTotalVotesTextView = itemView.findViewById(R.id.totalVotesTextView);
        ratingBar = itemView.findViewById(R.id.monsterRatingBar);
        actionButton1 = itemView.findViewById(R.id.actionButton1);

        this.onMonsterListener = onMonsterListener;
    }



    public void updateMonster(Monster monster){
//        Difference between res and assets:
//        https://www.concretepage.com/questions/413
//        Put images in drawable folder not assets folder. Assets folder is used to keep other types of file like pdf, js, txt, etc, preferable zipped

//        This is the reason why is better to use xml instead of png files
//        https://medium.com/the-android-guy/stop-using-pngs-use-vector-drawable-why-8ca68bed5335

//        Understanding vector image format: vector drawable
//        https://medium.com/androiddevelopers/understanding-androids-vector-image-format-vectordrawable-ab09e41d5c68

        View rootView = monsterImageView.getRootView();
        int resID = rootView.getResources().getIdentifier(monster.imageFileName , "drawable" , rootView.getContext().getPackageName()) ;
        monsterImageView.setImageResource(resID);
        this.monsterNameEditText.setText(monster.getName());
        this.monsterDescriptionEditText.setText(monster.getDescription());
        this.monsterTotalVotesTextView.setText(monster.getVotes() + " Votes");
        float rate;
        if(monster.getVotes() > 0){
            rate = 1.0f * monster.getStars() / monster.getVotes();
        }else{
            rate = 0.0f;
        }
        this.ratingBar.setRating(rate);
    }

    /**
     * Bind every monster with a listener, to be used when the user clicks a particular monster in the recycler view
     *
     */
    public void bind(final Monster monster, final OnMonsterListener onMonsterListener){
        this.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("booo", "click using the interface");
                onMonsterListener.onMonsterClick(monster);
            }
        });


    }

}
