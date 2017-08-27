package edu.jcookeakindiana.flashcards;

/**
 * ModifyCategoryActivity
 * Created by Jonathon Cooke-Akaiwa
 * Created on 2/22/17
 * Last Modified By: Jonathon Cooke-Akaiwa
 * Last Modified On: 3/3/17
 * Update/Create category for masterList
 */

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

//my imports
import android.util.Log;

import java.util.ArrayList;

public class ModifyCategoryActivity extends AppCompatActivity {

    private String TAG = "ModifyCategoryActivty";
    private ArrayList<TypeObject> fields = new ArrayList<>();
    private ListView list;
    private ArrayAdapter<TypeObject> adapter;
    private String u_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_create_category);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //make appbar back button behave like system back button
        toolbar.setNavigationOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        //get parameters passed from activity
        Bundle extra = getIntent().getExtras();
        if (extra != null) {
            Log.d(TAG, "created from existing object");
            //store unique category name and tell activity to update existing
            //category instead of making a new one
            u_name = extra.getString("u_name");
            Log.d(TAG, "object name " + u_name);

            ArrayList<CategoriesObject> results
                    = IOHandler.getMasterList(getApplicationContext());
            Log.d(TAG, "results size " + Integer.toString(results.size()));
            for(int i = 0; i < results.size(); i++) {
                CategoriesObject temp = results.get(i);
                Log.d(TAG, "id=" + temp.getId());
                if(temp.getId().equals(u_name)) {
                    Log.d(TAG, "object found");
                    //set title
                    EditText mEdit = (EditText) findViewById(R.id.category_title);
                    mEdit.setText(temp.getName());
                    //populate fields
                    fields = temp.getFields();
                    //break;
                }
            }
        }

        adapter = new TypeAdapter(ModifyCategoryActivity.this, fields);
        list = (ListView) findViewById(R.id.create_category_fields_listview);
        list.setAdapter(adapter);

        //handle creation of new text field
        findViewById(R.id.fab_text).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "text fab clicked");

                //build data entry dialog for field
                AlertDialog.Builder builder = new AlertDialog.Builder(ModifyCategoryActivity.this);
                builder.setTitle("Text Category Name");
                final EditText input = new EditText(getApplicationContext());
                input.setTextColor(Color.BLACK);
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setView(input);

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        TypeObject typeObj =
                                new TypeObject(input.getText().toString(), "text");
                        //add new field to view and storage array
                        addToList(typeObj);
                        Log.d(TAG, typeObj.toString());
                        Log.d(TAG, Integer.toString(fields.size()));
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();

                adapter.notifyDataSetChanged();
            }
        });

        findViewById(R.id.fab_image).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //add image picker and save image to system
                Log.d(TAG, "image fab clicked");

                AlertDialog.Builder builder = new AlertDialog.Builder(ModifyCategoryActivity.this);
                builder.setTitle("Text Category Name");
                final EditText input = new EditText(getApplicationContext());
                input.setTextColor(Color.BLACK);
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setView(input);

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        TypeObject typeObj =
                                new TypeObject(input.getText().toString(), "image");
                        //add new field to view and storage array
                        addToList(typeObj);
                        Log.d(TAG, typeObj.toString());
                        Log.d(TAG, Integer.toString(fields.size()));
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();

                adapter.notifyDataSetChanged();

            }
        });

        //save to json and write to unique file
        findViewById(R.id.categories_save_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                boolean ok_to_write = false;

                //min # of categories is 1
                if (fields.size() >= 1) {
                    ok_to_write = true;
                }

                if (ok_to_write) {
                    EditText mEdit = (EditText) findViewById(R.id.category_title);

                    CategoriesObject cat =
                            new CategoriesObject(mEdit.getText().toString(), fields);

                    ArrayList<CategoriesObject> results
                            = IOHandler.getMasterList(getApplicationContext());

                    if(u_name != null) {
                        for(int i = 0; i < results.size(); i++) {
                            CategoriesObject temp = results.get(i);
                            if(temp.getId().equals(u_name)) {
                                //remove old version
                                results.remove(i);
                                break;
                            }
                        }
                        //set id of new version to that of old (keeps files paths legal)
                        cat.setId(u_name);
                    }else {}
                    //add new categoryObject and save
                    results.add(cat);
                    IOHandler.writeMasterList(results, getApplicationContext());
                    finish();
                } else { //need minimum of 1 category
                    AlertDialog.Builder write_alert = new AlertDialog.Builder(ModifyCategoryActivity.this)
                            .setTitle("Error Saving")
                            .setMessage("Please create at least one category")
                            .setPositiveButton("OK", null);
                    write_alert.create();
                    write_alert.show();
                }


            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    //present dialog asking to discard changes
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.discard_changes_title)
                .setMessage(R.string.discard_changes_message)
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .show();
    }

    public void addToList(TypeObject field) {
        fields.add(field);
        adapter.notifyDataSetChanged();
    }

}
