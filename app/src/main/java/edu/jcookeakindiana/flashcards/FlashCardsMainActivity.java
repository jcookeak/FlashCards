package edu.jcookeakindiana.flashcards;

/*
* FlashCardsMainActivity
* Created By: Jonathon Cooke-Akaiwa
* Created On: 2/22/2017
* Last Modified By: Jonathon Cooke-Akaiwa
* Last Modified On: 2/28/2017
* loads list of categories and facilitates creation and removal of these categories
* */

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;


//my imports
import android.util.Log;

import java.util.ArrayList;

public class FlashCardsMainActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private static String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flash_cards_main);
        Log.d(TAG, "set content view");
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerList);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new MasterListRecyclerView(getDataSet(), getApplicationContext());
        mRecyclerView.setAdapter(mAdapter);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        Drawable fab_draw = getResources().getDrawable(android.R.drawable.ic_input_add)
                .getConstantState().newDrawable();
        fab_draw.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
        fab.setImageDrawable(fab_draw);

        final Intent createActivity = new Intent(getApplicationContext(), ModifyCategoryActivity.class);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(createActivity);
            }
        });
    }

//

    @Override
    protected void onResume() {
        super.onResume();
        mAdapter = new MasterListRecyclerView(getDataSet(), FlashCardsMainActivity.this);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
        //re-add onclick listener
        final Intent flashcardActivity = new Intent(getApplicationContext(), FlashcardListActivity.class);
        ((MasterListRecyclerView) mAdapter).setOnItemClickListener(new MasterListRecyclerView.MyClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                Log.i(TAG, "Clicked on Item" + position);
                Log.d(TAG, "clicked on object's id " + getDataSet().get(position).getId());
                //add selected category as parent in bundle
                flashcardActivity.putExtra("parent", getDataSet().get(position).getId().toString());
                //Load Individual Card Activity
                startActivity(flashcardActivity);

            }
        });
    }

    private ArrayList<CategoriesObject> getDataSet() {
        //read master_list from file
        ArrayList<CategoriesObject> results
                = IOHandler.getMasterList(getApplicationContext());

        return results;
    }


}