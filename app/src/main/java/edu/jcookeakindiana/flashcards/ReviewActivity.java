package edu.jcookeakindiana.flashcards;

/*
* ReviewActivity
* Created By: Jonathon Cooke-Akaiwa
* Created On: 3/2/2017
* Last Modified By: Jonathon Cooke-Akaiwa
* Last Modified On: 3/3/2017
* Activity controls reviewing of selected cards
* */

import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;

public class ReviewActivity extends AppCompatActivity {

    private String TAG ="ReviewActivity";

    private CategoriesObject parent;
    private ArrayList<CardObject> card_list = new ArrayList<>();
    private ArrayList<CardTypeObject> selectedTypes = new ArrayList<>();
    private Queue<CardObject> queue = new LinkedList<CardObject>();
    private CardObject currentCard = new CardObject("");
    private Bitmap img;
    private ReviewAdapter adapter;
    private ListView list;
    private CardTypeObject answer_type;
    ArrayList<CardTypeObject> answer_list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //make appbar back button behave like system back button
        toolbar.setNavigationOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        Bundle extra = getIntent().getExtras();
        if (extra != null) {
            String u_name = extra.getString("parent");
            String json = extra.getString("types");

            //find parent object and store it
            ArrayList<CategoriesObject> results
                    = IOHandler.getMasterList(getApplicationContext());
            for(int i=0; i < results.size(); i++) {
                CategoriesObject temp = results.get(i);
                if (temp.getId().equals(u_name)) {
                    parent = temp;
                    break;
                }
            }

            //take json list of
            Gson gson = new Gson();
            Type type = new TypeToken<List<CardTypeObject>>() {}.getType();
            selectedTypes = gson.fromJson(json, type);
            Log.d(TAG, "selectedTypes: " + selectedTypes.size());
        }

        //populate card_list
        card_list = IOHandler.getCardList(getApplicationContext(), parent.getId());
        //add name of card as category to be tested
        for(int x=0; x < card_list.size(); x++) {
            CardObject temp = card_list.get(x);
            temp.addType(new CardTypeObject("name", "text",temp.getName()));
        }
        selectedTypes.add(new CardTypeObject("name","text")); //add card names as possible choices
        Log.d(TAG, "card_list " + card_list);
        //randomize card list and set this as the queue
        Collections.shuffle(card_list, new Random(System.nanoTime()));
        Log.d(TAG, "card_list after shuffle " + card_list);
        //populate queue
        queue.addAll(card_list);

        adapter = new ReviewAdapter(ReviewActivity.this, answer_list);

        //build graphical view
        next();



    }

    public CardTypeObject getRandomType(ArrayList<CardTypeObject> types) {
        Random random = new Random();
        int n = random.nextInt(types.size());
        Log.d(TAG, "random selection: " + n);
        Log.d(TAG, "selectedTypes: " + types.size());
        return types.get(n);
    }


    public void next() {
        Log.d(TAG, "queue: " + queue.size());
        if (queue.size() == 0) {
            AlertDialog.Builder finish = new AlertDialog.Builder(ReviewActivity.this)
                    .setTitle("Finished Reviewing")
                    .setMessage("You have reviewed all of the cards.")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();

                        }
                    });
            finish.create();
            finish.show();
            return; //kill activity
        }

        //graphics
        //set cards remaining count
        Resources res = getResources();
        String cards_remaining = String.format(res.getString(R.string.review_cards_remaining), queue.size());
        TextView remaining = (TextView) findViewById(R.id.review_cards_remaining);

        remaining.setText(cards_remaining);

        //Set hint (top item on screen)
        TextView hintText = (TextView) findViewById(R.id.review_hint_text);
        ImageView hintImage = (ImageView) findViewById(R.id.review_hint_image);
        currentCard = queue.poll(); //pop head of queue
        Log.d(TAG, "currentcard: " + currentCard.getName());
        CardTypeObject hintType = getRandomType(selectedTypes);
        Log.d(TAG, "hintType: " + hintType.getName());
        if (hintType.getType().equals("text")) {
            hintText.setText(currentCard.getType(hintType.getName()).getContent());
            //change visibility
            hintImage.setVisibility(View.GONE);
            hintText.setVisibility(View.VISIBLE);
        } else { //image

            String content = currentCard.getType(hintType.getName()).getContent();
            Log.d(TAG, "content: " + content);
            img = IOHandler.loadImage(content);
            hintImage.setImageBitmap(img);
            Log.d(TAG, "img max: " + hintImage.getMaxHeight());

            //set which type is visible
            hintText.setVisibility(View.GONE);
            //hintImage.setVisibility(View.GONE);
            //list.setVisibility(View.GONE);
            hintImage.setVisibility(View.VISIBLE);
            //list.setVisibility(View.VISIBLE);
            Log.d(TAG, "img vis: " + hintImage.getVisibility());
        }


        //adapter for answer list

        //random type for answers
        ArrayList<CardTypeObject> temp_list = new ArrayList<>();
        temp_list.addAll(selectedTypes);
        temp_list.remove(hintType); //don't want hint and answer to be same type
        answer_type = getRandomType(temp_list);
        answer_list = new ArrayList<>();
        answer_list.add(currentCard.getType(answer_type.getName())); //add correct answer to list
        ArrayList<CardObject> answer_cards = new ArrayList<>();
        answer_cards.addAll(card_list);
        answer_cards.remove(currentCard); //don't pick from answer
        Collections.shuffle(answer_cards);

        for (int i = 0; i < answer_cards.size() && i < 5; i ++) {
            CardObject temp = answer_cards.get(i);
            answer_list.add(temp.getType(answer_type.getName())); //add type matching random selection
        }
        //shuffle up answers
        Collections.shuffle(answer_list);
        adapter.clear();
        adapter.addAll(answer_list);
        adapter.notifyDataSetChanged();

        list = (ListView) findViewById(R.id.review_answer_list);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Log.d(TAG, "item clicked: " + position);
                View temp = adapter.getView(position, view,parent);
                //check current cards answer against selection
                if (currentCard.getType(answer_type.getName()).getContent()
                        .equals(adapter.getItem(position).getContent())) {
                    //correct answer
                    Toast.makeText(ReviewActivity.this, "Correct!",Toast.LENGTH_SHORT).show();
                    //temp.setBackgroundColor(ContextCompat.getColor(ReviewActivity.this, R.color.green));
                } else {
                    //wrong answer
                    Toast.makeText(ReviewActivity.this, "Wrong!",Toast.LENGTH_SHORT).show();
                    //temp.setBackgroundColor(ContextCompat.getColor(ReviewActivity.this,R.color.pink_pressed));
                    queue.offer(currentCard);
                }
                next();


            }
        });
    }

}
