package edu.jcookeakindiana.flashcards;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * TypeAdapter.java
 * Created by Jonathon Cooke-Akaiwa
 * Created on 2/25/17
 * Last Modified By: Jonathon Cooke-Akaiwa
 * Last Modified On: 2/26/17
 * Handles ListView layout and inflation of this view.
 */

public class TypeAdapter extends ArrayAdapter<TypeObject> {

    private String TAG = "TypeAdapter";

    public TypeAdapter(Context context, ArrayList<TypeObject> type_list) {
        super(context, 0, type_list);
    }

    static class ViewHolder {
        //hold the objects layout in this class
        public TextView field_type;
        public EditText field_title;
        public CheckBox checkBox;
        public ImageButton delete_button;
        //textWatcher for edit of field_title
        public TextWatcher textWatcher;

    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        //get specific field we are working on
        final TypeObject field = getItem(position);

        if(convertView == null) {
            convertView = LayoutInflater.from(getContext())
                    //inflate from layout resource
                    .inflate(R.layout.field_layout,parent, false);

            //structure holds content of row
            ViewHolder viewHolder = new ViewHolder();
            //get layout resource
            viewHolder.field_type = (TextView) convertView.findViewById(R.id.category_field_label);
            viewHolder.field_title = (EditText) convertView.findViewById(R.id.category_field_title);
            viewHolder.delete_button = (ImageButton) convertView.findViewById(R.id.category_field_delete);
            convertView.setTag(viewHolder);
        }

        ViewHolder holder = (ViewHolder) convertView.getTag();
        //remove other textWatchers
        if (holder.textWatcher != null) {
            holder.field_title.removeTextChangedListener(holder.textWatcher);
        }

        //update displayed data and underlying object on edit
        holder.textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //update object with new name
                field.setName(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };

        //add textWatcher from ViewHolder to the field_title
        holder.field_title.addTextChangedListener(holder.textWatcher);

        //delete field button
        holder.delete_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(getContext())
                        .setTitle(R.string.remove_field_title)
                        .setMessage(R.string.remove_field_message)
                        .setNegativeButton("Cancel", null)
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                remove(getItem(position));
                            }
                        })
                        .show();

            }
        });

        //set values for this resource
        holder.field_type.setText(field.getType());
        holder.field_title.setText(field.getName());

        return convertView;
    }
}
