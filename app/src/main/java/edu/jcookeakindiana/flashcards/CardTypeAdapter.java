package edu.jcookeakindiana.flashcards;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

//my imports

import java.util.ArrayList;

import static android.view.ViewGroup.FOCUS_BLOCK_DESCENDANTS;

/**
 * CardTypeAdapter.java
 * Created by Jonathon Cooke-Akaiwa
 * Created on 3/1/17
 * Last Modified By: Jonathon Cooke-Akaiwa
 * Last Modified On: 3/2/17
 * Handles ListView layout and inflation for modifying a flashcard.
 */

public class CardTypeAdapter extends ArrayAdapter<CardTypeObject> {

    private String TAG = "CardTypeAdapter";
    private Context context;
    private Uri image_uri;
    private Bitmap img;

    public CardTypeAdapter(Context context, ArrayList<CardTypeObject> type_list) {
        super(context, 0, type_list);
        this.context = context;
    }

    static class ViewHolder {
        //hold the objects layout in this class
        public TextView label;
        public TextView text;
        public ImageView image;
        //textWatcher for edit of field_title
        public TextWatcher textWatcher;

    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        Log.d(TAG, "Setting view");

        final CardTypeObject type = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.flashcard_row,parent, false);

            //struct holds items in row
            ViewHolder viewHolder = new ViewHolder();
            //get layout resources
            viewHolder.label = (TextView) convertView.findViewById(R.id.flashcard_row_label);

            viewHolder.text = (TextView) convertView.findViewById(R.id.flashcard_row_text);
            viewHolder.image = (ImageView) convertView.findViewById(R.id.flashcard_row_imageview);
            if(type.getType().equals("text")) { //type is text
                viewHolder.text.setVisibility(View.VISIBLE);
                viewHolder.image.setMaxHeight(0); //set image height to zero
            }else { //type is image
                viewHolder.text.setMaxHeight(0); //set text height to zero
                viewHolder.image.setVisibility(View.VISIBLE);
            }

            convertView.setTag(viewHolder);
        }

        ViewHolder holder = (ViewHolder) convertView.getTag();
        //remove other text watchers
        if (holder.textWatcher != null) {
            holder.text.removeTextChangedListener(holder.textWatcher);
        }
        //remove onclick listener for image
//        if (holder.onClickWatcher) {
//            holder.image.removeOnCli
//        }

        //update display of data for EditText
        holder.textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                type.setContent(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };

        //set label
        holder.label.setText(type.getName());

        //add listener and set visible content
        if (type.getType().equals("text")) {
            //holder.text.addTextChangedListener(holder.textWatcher);
            holder.text.setText(type.getContent());
        }

        //set image correctly
        if(type.getContent() != null
                && type.getContent()!= "null"
                && type.getType().equals("image")) {
            Log.d(TAG, "setting image");
            Log.d(TAG, "uri: " + type.getContent());
            img = IOHandler.loadImage(type.getContent());
            holder.image.setImageBitmap(img);
            //holder.image.setImageURI(Uri.parse(type.getContent()));
        }



        return convertView;
    }

    //handle getting an image
    public  void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("MyAdapter", "onActivityResult");
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                Uri uri = data.getData();
                image_uri = uri;
                Log.d(TAG, "uri set to: " + image_uri);
            }
        }

    }

}
