package edu.jcookeakindiana.flashcards;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/*
* ReviewAdapter
* Created By: Jonathon Cooke-Akaiwa
* Created On: 3/3/2017
* Last Modified By: Jonathon Cooke-Akaiwa
* Last Modified On: 3/3/2017
* Adapter inflates answer list for ReviewActivity
* */

public class ReviewAdapter extends ArrayAdapter<CardTypeObject> {

    private Context context;
    private ArrayList<CardTypeObject> answer_list;
    private Bitmap img;

    private String TAG ="ReviewAdapter";

    public ReviewAdapter(Context context, ArrayList<CardTypeObject> answer_list) {
        super(context, 0, answer_list);
        this.context = context;
        this.answer_list = answer_list;
    }

    static class ViewHolder {
        //hold the objects layout in this class
        public TextView text;
        public ImageView image;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        final CardTypeObject type = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.review_row, parent, false);
            //class structure holds content of row
            ReviewAdapter.ViewHolder viewHolder = new ReviewAdapter.ViewHolder();
            //get visual elements from layout
            viewHolder.text = (TextView) convertView.findViewById(R.id.review_row_text);
            viewHolder.image = (ImageView) convertView.findViewById(R.id.review_row_image);
            //set this viewholder so it can be accessed
            convertView.setTag(viewHolder);
        }

        ReviewAdapter.ViewHolder holder = (ReviewAdapter.ViewHolder) convertView.getTag();
        //set values for visual elements
        if (type.getType().equals("text")) {
            holder.text.setText(getItem(position).getContent());
            holder.image.setVisibility(View.INVISIBLE);
        } else {
            //set image
            Log.d(TAG, "type " + type.getType());
            Log.d(TAG, "img content: " + type.getContent());
            img = IOHandler.loadImage(type.getContent());
            holder.image.setImageBitmap(img);
            holder.text.setVisibility(View.INVISIBLE);
        }


        return convertView;
    }

}
