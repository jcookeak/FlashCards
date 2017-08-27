package edu.jcookeakindiana.flashcards;

/**
 * Created by Jonathon Cooke-Akaiwa
 * Created on 2/23/17
 * Last Modified By: Jonathon Cooke-Akaiwa
 * Last Modified On: 2/26/17
 * Convert between Object representation and JSON of CategoryObject
 */

//my imports
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import junit.framework.Assert;


public class IOHandler {
    private static String TAG = "IOHandler";
    private static String img_path = "/data/user/0/edu.jcookeakindiana.flashcards/app_img";

    private ArrayList<CategoriesObject> masterList = new ArrayList<CategoriesObject>();

    //convert ArrayList to json and store in master_list.txt
    public static void saveJson(ArrayList<CategoriesObject> categories) {
        //List<CategoriesObject> list = categories;

        Gson gson = new Gson();
        Type type = new TypeToken<List<CategoriesObject>>() {}.getType();
        String json = gson.toJson(categories, type);
        System.out.println(json);
        List<CategoriesObject> fromJson = gson.fromJson(json, type);
        for (CategoriesObject category : fromJson) {
            System.out.println(category);
        }
    }

    public static void writeMasterList(ArrayList<CategoriesObject> categories, Context context) {
        Gson gson = new Gson();
        Type type = new TypeToken<List<CategoriesObject>>() {}.getType();
        String filename = "master_list";
        String json = gson.toJson(categories, type);
        try {
            FileOutputStream fos = context.openFileOutput(filename, context.MODE_PRIVATE);
            fos.write(json.getBytes());
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<CategoriesObject> getMasterList(Context context) {
        ArrayList<CategoriesObject> results = new ArrayList<>();
        try {
            String filename = "master_list";
            FileInputStream fis = context.openFileInput(filename);
//            Resources res = getResources();
//            InputStream in = res.openRawResource(R.raw.master_list);
            byte[] b = new byte[fis.available()];
            fis.read(b);
            String json = new String(b);
            Log.d("TAG", json);

            if(json != "") {

                Log.d(TAG, "creating data");
                Gson gson = new Gson();
                Type type = new TypeToken<List<CategoriesObject>>() {}.getType();
                results = gson.fromJson(json, type);

                for(CategoriesObject category : results) {
                    Log.d("JSON", category.toString());
                }
                //return results;
            }

            //return null;
            //return results;
        } catch(Exception e) {
            //if we are here, no categories have been added
            //return an empty arrayList to prevent crash
            Log.d(TAG, "error reading file");
        }
        return results;
    }

    public static ArrayList<CardObject> getCardList(Context context, String filename) {
        ArrayList<CardObject> results = new ArrayList<>();
        try {
            FileInputStream fis = context.openFileInput(filename);
            byte[] b = new byte[fis.available()];
            fis.read(b);
            String json = new String(b);

            //no items were ever added to file, but it exists
            if (json.equals("null")) {
                return results;
            }
            else if ((json != "")) {
                Log.d(TAG, "grabbing data for Cards " + filename);
                Gson gson = new Gson();
                Type type = new TypeToken<List<CardObject>>() {}.getType();
                results = gson.fromJson(json,type);
            } else {
                Log.d(TAG, "failing here");
            }

        } catch (Exception e) {
            Log.d(TAG, "error reading file");
            //create file if it does not exist
            //writes empty ArrayList
            writeCardList(new ArrayList<CardObject>(), context, filename);
        }
        return results;
    }

    public static void writeCardList(ArrayList<CardObject> cards, Context context, String filename) {
        Gson gson = new Gson();
        Type type = new TypeToken<List<CardObject>>() {}.getType();
        String json = gson.toJson(cards, type);
        try {
            FileOutputStream fos = context.openFileOutput(filename, context.MODE_PRIVATE);
            fos.write(json.getBytes());
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writeImage(Bitmap image, Context context, String filename) {
        File dir = context.getDir("img", context.MODE_PRIVATE);
        img_path = dir.getAbsolutePath(); //set absolute path for dir
        File path = new File(dir, filename);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(path);
            //compress image and write it
            image.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //get the image located at path
    public static Bitmap loadImage(String filename) {
        Bitmap img = null;
        try {
            Log.d(TAG, "img path" + img_path);
            File file = new File(img_path, filename);
            img = BitmapFactory.decodeStream(new FileInputStream(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return img;
    }



}
