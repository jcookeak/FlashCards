package edu.jcookeakindiana.flashcards;

/*
* FlashcardListActivity
* Created By: Jonathon Cooke-Akaiwa
* Created On: 2/29/2017
* Last Modified By: Jonathon Cooke-Akaiwa
* Last Modified On: 3/3/2017
* loads list of flashcards for the parent Category
* */

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class FlashcardListActivity extends AppCompatActivity {

    private String TAG = "FlashcardListActivity";
    private CategoriesObject parent;
    private String u_name;

    private ArrayList<CardObject> flashcards = new ArrayList<>();
    private ListView list;
    private ArrayAdapter<CardObject> adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flashcard_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //get parameters for parent category
        Bundle extra = getIntent().getExtras();
        if (extra != null) {
            Log.d(TAG, "getting parent category");
            u_name = extra.getString("parent");

            ArrayList<CategoriesObject> results
                    = IOHandler.getMasterList(getApplicationContext());
            Log.d(TAG, "results size " + Integer.toString(results.size()));
            for(int i = 0; i < results.size(); i++) {
                CategoriesObject temp = results.get(i);
                Log.d(TAG, "id=" + temp.getId());
                if(temp.getId().equals(u_name)) {
                    //store categories object as parent
                    parent = results.get(i);
                    //break;
                }
            }
            //grab matching flashcards
            flashcards = IOHandler.getCardList(getApplicationContext(), parent.getId());
        }

        //populate ListView
        adapter = new FlashcardAdapter(FlashcardListActivity.this, flashcards);
        list = (ListView) findViewById(R.id.flashcard_list);
        list.setAdapter(adapter);


        //open modifyFlashcardActivity on item click
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            final Intent modifyIntent = new Intent(getApplicationContext(), ModifyFlashcardActivity.class);
            @Override
            public void onItemClick(AdapterView<?> parentAdapterView, View view, int position, long id) {

                modifyIntent.putExtra("parent", parent.getId());
                modifyIntent.putExtra("card", flashcards.get(position).getName());
                startActivity(modifyIntent);
            }
        });

        //intent to add new card
        final Intent createCard = new Intent(getApplicationContext(), ModifyFlashcardActivity.class);

        //fab to add new card to list

        findViewById(R.id.flashcard_fab_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "fab add pressed");
                //start intent to add cards
                createCard.putExtra("parent", parent.getId());
                startActivity(createCard);
            }
        });

        //intent to review cards
        final Intent reviewCards = new Intent(getApplicationContext(), ReviewActivity.class);

        //list to pass to review Activity
        final ArrayList<TypeObject> selected_types = new ArrayList<TypeObject>();
        //builder to select categories to review
        final AlertDialog.Builder review_builder = new AlertDialog.Builder(FlashcardListActivity.this);
        final ArrayList<TypeObject> type_list = parent.getFields();
        String[] fields = new String[type_list.size()]; //array of type choices
        final boolean[] fieldsChecked = new boolean[type_list.size()]; //holds selections
        for(int i=0; i < type_list.size(); i++) {
            fields[i] = type_list.get(i).getName(); //populate type list
        }
        //set action buttons
        review_builder.setTitle("Select categories for review")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //need to select at least one category
                        if (selected_types.size() > 0) {
                            //start intent to review cards
                            reviewCards.putExtra("parent", parent.getId());
                            //pass in selection
                            Gson gson = new Gson();
                            Type type = new TypeToken<List<CardTypeObject>>() {}.getType();
                            String json = gson.toJson(selected_types, type);
                            reviewCards.putExtra("types", json); //passed as json
                            startActivity(reviewCards);
                        }
                        else {
                            AlertDialog.Builder error_alert = new AlertDialog.Builder(FlashcardListActivity.this)
                                    .setTitle("Error")
                                    .setMessage("Must select at least 1 category to review")
                                    .setPositiveButton("OK", null);
                            error_alert.create();
                            error_alert.show();
                        }


                    }
                });
        review_builder.setMultiChoiceItems(fields, fieldsChecked, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                Log.d(TAG, "selected ");
                if(fieldsChecked[which]) {
                    Log.d(TAG, "added type");
                    selected_types.add(type_list.get(which));
                }else {
                    Log.d(TAG, "removed type");
                    selected_types.remove(type_list.get(which));
                }
            }
        });
        review_builder.create();

        findViewById(R.id.flashcard_fab_review).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //display dialog to select categories to review
                Log.d(TAG, "fab add pressed");
                review_builder.show();

            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        //grab matching flashcards
        flashcards = IOHandler.getCardList(getApplicationContext(), parent.getId());
        //tell adapter about updated dataset
        adapter.clear();
        adapter.addAll(flashcards);
        adapter.notifyDataSetChanged();
    }

    private void getDataSet() {

    }
}
