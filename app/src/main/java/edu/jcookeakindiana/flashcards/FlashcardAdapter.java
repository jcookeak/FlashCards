package edu.jcookeakindiana.flashcards;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * FlashcardAdapter.java
 * Created by Jonathon Cooke-Akaiwa
 * Created on 2/28/17
 * Last Modified By: Jonathon Cooke-Akaiwa
 * Last Modified On: 2/28/17
 */

public class FlashcardAdapter extends ArrayAdapter<CardObject> {

    private String TAG = "FlashcardAdapter";

    public FlashcardAdapter(Context context, ArrayList<CardObject> card) {
        super(context, 0, card);
    }

    static class CardViewHolder {
        //hold the objects layout in this class
        public TextView name;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        final CardObject card = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.flashcard_list_item, parent, false);
            //class structure holds content of row
            CardViewHolder viewHolder = new CardViewHolder();
            //get visual elements from layout
            viewHolder.name = (TextView) convertView.findViewById(R.id.flashcard_list_row_name);
            //set this viewholder so it can be accessed
            convertView.setTag(viewHolder);
        }

        CardViewHolder holder = (CardViewHolder) convertView.getTag();
        //set values for visual elements
        holder.name.setText(card.getName());

        return convertView;
    }
}
