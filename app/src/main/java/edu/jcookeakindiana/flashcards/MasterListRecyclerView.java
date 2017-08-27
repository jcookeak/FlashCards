package edu.jcookeakindiana.flashcards;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;

//added libraries
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import android.widget.PopupMenu;

/**
 * Created by Jonathon Cooke-Akaiwa
 * Created on 2/22/17
 * Last Modified By: Jonathon Cooke-Akaiwa
 * Last Modified On: 3/3/17
 * Controls and populates list of cards for master_list
 */

public class MasterListRecyclerView extends RecyclerView
.Adapter<MasterListRecyclerView
        .CategoriesObjectHolder>
{
    private static String TAG = "MasterListRecyclerView";
    private ArrayList<CategoriesObject> mDataset;
    private static MyClickListener myClickListener;
    private Context context;

    public static class CategoriesObjectHolder extends RecyclerView.ViewHolder
            implements View
            .OnClickListener {
        TextView categoryName;
        TextView categorySize;
        TextView buttonMenu;

        public CategoriesObjectHolder(View itemView) {
            super(itemView);
            categoryName = (TextView) itemView.findViewById(R.id.category_name);
            categorySize = (TextView) itemView.findViewById(R.id.category_size);
            buttonMenu = (TextView) itemView.findViewById(R.id.btn_menu);
            Log.i(TAG, "Adding Listener");
            itemView.setOnClickListener(this);


        }

        @Override
        public void onClick(View v) {
            myClickListener.onItemClick(getAdapterPosition(), v);
        }
    }

    public void setOnItemClickListener(MyClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }

    public MasterListRecyclerView(ArrayList<CategoriesObject> myDataset, Context context) {
        this.context = context;
        mDataset = myDataset;
//
    }

    //inflate card with data and implement layout design
    @Override
    public MasterListRecyclerView.CategoriesObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.master_list__card_layout, parent, false);

        CategoriesObjectHolder categoriesObjectHolder = new CategoriesObjectHolder(view);
        return categoriesObjectHolder;
    }

    @Override
    public void onBindViewHolder(MasterListRecyclerView.CategoriesObjectHolder holder, final int position) {
        final CategoriesObjectHolder holder1 = holder;

        holder1.categoryName.setText(mDataset.get(position).getName());
        int fields_count = mDataset.get(position).getCategoryCount();
        //handle wording for categories with only one field
        if(fields_count != 1) {
            holder1.categorySize.setText(Integer.toString(fields_count) + " fields");
        }else {
            holder1.categorySize.setText(Integer.toString(fields_count) + " field");
        }



        //set popup menu listener
        holder1.buttonMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //popup menu to delete or edit cards
                PopupMenu popup = new PopupMenu(context, holder1.buttonMenu);

                //inflate menu with custom menu
                popup.inflate(R.menu.menu_category_card);

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.action_edit:
                                //open edit Actvity
                                Intent i = new Intent(context, ModifyCategoryActivity.class);

                                //pass in unique id
                                i.putExtra("u_name", mDataset.get(position).getId());
                                context.startActivity(i);
                                return true;
                            case R.id.action_delete:
                                //delete this card
                                new AlertDialog.Builder(context)
                                        .setTitle(R.string.remove_card_title)
                                        .setMessage(R.string.remove_card_message)
                                        .setNegativeButton("Cancel", null)
                                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                mDataset.remove(mDataset.get(position));
                                                IOHandler.writeMasterList(mDataset, context);
                                                notifyDataSetChanged();
                                            }
                                        })
                                        .show();
                                return true;
                            default:
                                return true;
                        }
                    }
                });

                popup.show();
            }
        });
//
//        });

    }

    public void addItem(CategoriesObject dataObj, int index) {
        mDataset.add(index, dataObj);
        IOHandler.writeMasterList(mDataset,context);
        notifyItemInserted(index);

    }

    public void deleteItem(int index) {
        mDataset.remove(index);
        IOHandler.writeMasterList(mDataset, context);
        notifyItemRemoved(index);
    }



    // need to know number of items to display
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    // return click position and associated view
    public interface MyClickListener {
        public void onItemClick(int position, View v);
    }
}


