package edu.jcookeakindiana.flashcards;

/**
 * ModifyFlashcardActivity
 * Created by Jonathon Cooke-Akaiwa
 * Created on 2/28/17
 * Last Modified By: Jonathon Cooke-Akaiwa
 * Last Modified On: 3/2/17
 */

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class ModifyFlashcardActivity extends AppCompatActivity {


    private String TAG = "ModifyFlashcardActivity";
    private ArrayList<CardTypeObject> type_list = new ArrayList<>();
    private CardObject card = new CardObject("New Card");
    private boolean new_card = true;
    private int index = 0;
    private CardTypeAdapter adapter;
    private ListView list;
    private Uri new_image;
    private String parent_id;
    private ArrayList<CardObject> flashcard_list = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_flashcard);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //make appbar back button behave like system back button
        toolbar.setNavigationOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        //get parameters passed from parent
        Bundle extra = getIntent().getExtras();
        if(extra != null) {

            parent_id = extra.getString("parent");
            ArrayList<CategoriesObject> result =
                    IOHandler.getMasterList(getApplicationContext());
            for(int i = 0; i < result.size(); i++) {
                CategoriesObject temp = result.get(i);
                if(temp.getId().equals(parent_id)) {
                    Log.d(TAG, "parent_id match found");
                    ArrayList<TypeObject> tempFields = temp.getFields();
                    for (int j=0; j < tempFields.size(); j++) {
                        type_list.add(new CardTypeObject(tempFields.get(j)));
                    }
                }
            }

            try {
                String card_id = extra.getString("card");
                flashcard_list = IOHandler.getCardList(getApplicationContext(), parent_id);
                //Get selected card from list
                for (int i=0; i < flashcard_list.size(); i++) {
                    CardObject temp = flashcard_list.get(i);
                    //get card by name (name is unique)
                    if(temp.getName().equals(card_id)) {

                        //set card to be the found card
                        card = temp;
                        new_card = false;
                        index = i;
                        type_list = card.getType_list();
                    }
                }
                //update displayed card_name
                EditText card_name = (EditText) findViewById(R.id.modify_flashcard_name);
                card_name.setText(card.getName());
            } catch (Exception e) {
                Log.d(TAG, "no parent");
                Log.d(TAG, "type_list: " + type_list.toString());
            }

        }


        //setup listview
        adapter = new CardTypeAdapter(ModifyFlashcardActivity.this, type_list);
        list = (ListView) findViewById(R.id.modify_flashcard_type_listview);
        list.setAdapter(adapter); //populates listview with types from flashcard's category

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, "item clicked : " + position);

                final CardTypeObject temp = type_list.get(position);
                //handle an image being clicked

                if(temp.getType().equals("text")) {
                    //start dialog

                    //text field to change
                    final EditText input = new EditText(getApplicationContext());
                    input.setTextColor(Color.BLACK);
                    input.setInputType(InputType.TYPE_CLASS_TEXT);
                    input.setText(temp.getContent());

                    //builder to update text for field of type text
                    //use this b/c can't have setOnItemClickListener with editView
                    AlertDialog.Builder builder = new AlertDialog.Builder(ModifyFlashcardActivity.this)
                            .setTitle(R.string.modify_flashcard_builder_title)
                            .setMessage(R.string.modify_flashcard_builder_message)
                            .setView(input)
                            .setNegativeButton("Cancel", null);

                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            temp.setContent(input.getText().toString());
                            adapter.notifyDataSetChanged();
                        }
                    });
                    builder.show();

                }

                else if (temp.getType().equals("image")) {
                    //get and set image
                    //gets image through default image picker
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_PICK);

                    Log.d(TAG, "position clicked: " + position);

                    //pass position as requestCode
                    startActivityForResult(Intent.createChooser(intent, "Select Picture"), position);
                }
                adapter.notifyDataSetChanged();
            }
        });

        //save changes to json
        findViewById(R.id.modify_flashcard_save_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "parent_id: " + parent_id);

                Boolean ok_to_write = true;
                int error_code = 0;

                EditText cardName = (EditText) findViewById(R.id.modify_flashcard_name);
                card.setName(cardName.getText().toString());
                card.setType_list(type_list);

                //check for unique filename
                if (new_card) {
                    //check for prexisting card with same name
                    for(int x = 0; x < flashcard_list.size(); x++) {
                        CardObject temp = flashcard_list.get(x);
                        if (temp.getName().equals(card.getName())) {
                            ok_to_write = false;
                            error_code = 1;
                        }
                    }
                }

                //check if all fields populated
                for(int i = 0; i < type_list.size(); i ++) {
                    CardTypeObject temp = type_list.get(i);
                    if(temp.getContent() == null) {
                        ok_to_write = false;
                        error_code = 2;
                    }
                }

                //write card to list
                if(ok_to_write) {
                    if (new_card) {
                        flashcard_list.add(card); //append new card
                    }
                    else {
                        flashcard_list.set(index, card); //update existing card
                    }
                    IOHandler.writeCardList(flashcard_list,ModifyFlashcardActivity.this, parent_id);
                    finish();
                } else {
                    Log.d(TAG, "failed checks for writing");
                    AlertDialog.Builder errorDialog = new AlertDialog.Builder(ModifyFlashcardActivity.this)
                            .setTitle(R.string.modify_flashcard_save_title)
                            .setPositiveButton("Ok", null);
                    switch (error_code) {
                        case 1:
                            Log.d(TAG, "filename is not unique");
                            errorDialog.setMessage(R.string.modify_flashcard_save_filename);
                            break;
                        case 2:
                            Log.d(TAG, "empty field exists");
                            errorDialog.setMessage(R.string.modify_flashcard_save_fields);
                            break;
                        default:
                            Log.d(TAG, "unknown error code: " + error_code);
                    }
                    errorDialog.show();
                }

            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        //load fields from category
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

    //pass onActivityResult back into adapter for image picker
    //request code maps to position in adapter
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        if (resultCode == Activity.RESULT_OK) {
            if (data != null) {
                Log.d(TAG, "getting selected image");
                new_image = data.getData();
                try {
                    SimpleDateFormat date_format = new SimpleDateFormat("ddMMyy-hhmmss");
                    Date date = Calendar.getInstance().getTime();
                    String img_name = "img-" + date_format.format(date);
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(),new_image);
                    IOHandler.writeImage(bitmap, ModifyFlashcardActivity.this, img_name);

                    //store filename
                    adapter.getItem(requestCode).setContent(img_name);
                    adapter.notifyDataSetChanged();
                } catch (IOException e) {
                    e.printStackTrace();
                }


            }
        }

        //adapter.onActivityResult(requestCode, resultCode, data);
    }
}
