package edu.jcookeakindiana.flashcards;


//my imports
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * CategoriesObject
 * Created by Jonathon Cooke-Akaiwa
 * Created on 2/22/17
 * Last Modified By: Jonathon Cooke-Akaiwa
 * Last Modified On: 2/26/17
 * Object representation of Category
 */

public class CategoriesObject {
    private String id;
    private String name;
    private ArrayList<TypeObject> categories = new ArrayList<TypeObject>();

    CategoriesObject(String categoryName){
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd_HHmmss");
        name = categoryName;
        id = (name + df.format(new Date()).toString())
                .replaceAll(" ", "_").toLowerCase();
    }

    //build new instance
    CategoriesObject(String name, ArrayList<TypeObject> types) {
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd_HHmmss");
        this.name = name;
        id = (name + df.format(new Date()).toString())
                .replaceAll(" ", "_").toLowerCase();
        for (int i = 0; i < types.size(); i++) {
            addType(types.get(i));
        }
    }

    //rebuild from JSON
    CategoriesObject(String id, String name, ArrayList<TypeObject> types) {
        this.id  = id;
        this.name = name;
        for(int i = 0; i < types.size(); i++) {
            addType(types.get(i));
        }
    }


    public String getId() {return id;}

    public String getName(){
        return name;
    }

    //update name
    public void setName(String newName){
        this.name = newName;
    }

    public void addType(TypeObject type) {
        categories.add(type);
    }

    //see TypeObject for how this is initialized
    public void addType(String name, String type) {
        TypeObject addedType = new TypeObject(name, type);
        categories.add(addedType);
    }

    public void setType(String name, String type) {
        int loc = categories.indexOf(name);
        categories.get(loc).setType(type);
    }

    public void setId(String id) {
        this.id = id;
    }

    //remove type by name
    public void removeType(String name) {
        int loc = categories.indexOf(name);
        categories.remove(loc);
    }

    @Override
    public String toString() {
        if (categories.size() > 0) {
            return "Category [id= " + id + ",name = " + name + "," + categories.toString() + "]";
        }
        else {
            return "Category [id = "+ id + ",name = " + name + "]";
        }
    }

    //returns list of fields in object
    public ArrayList<TypeObject> getFields() {
        return categories;
    }

    // get number of categories for master list cardView
    public int getCategoryCount() {
        return categories.size();
    }
}
