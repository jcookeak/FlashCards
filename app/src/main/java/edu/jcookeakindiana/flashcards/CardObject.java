package edu.jcookeakindiana.flashcards;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 * CardObject
 * Created by Jonathon Cooke-Akaiwa
 * Created on 2/28/17
 * Last Modified By: Jonathon Cooke-Akaiwa
 * Last Modified On: 2/28/17
 * Object representation of individual flashcard
 */

public class CardObject {

    private String name;
    //will be matched with CategoriesObject typelist
    private ArrayList<CardTypeObject> type_list = new ArrayList<>();
    //unique name
    private String id;

    CardObject(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<CardTypeObject> getType_list() {
        return type_list;
    }

    public void setType_list(ArrayList<CardTypeObject> type_list) {
        this.type_list = type_list;
    }

    public void replaceTypeList(ArrayList<CardTypeObject> type_list) {
        this.type_list = type_list;
    }

    public CardTypeObject getType(String type_name) {
        for(int i = 0; i < type_list.size(); i++) {
            if(type_list.get(i).getName().equals(type_name)) {
                return type_list.get(i);
            }
        }
        return null;
    }

    public void addType(CardTypeObject type) {
        type_list.add(type);
    }



}
