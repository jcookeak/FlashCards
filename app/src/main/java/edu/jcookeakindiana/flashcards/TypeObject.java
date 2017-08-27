package edu.jcookeakindiana.flashcards;

/**
 * Created by Jonathon Cooke-Akaiwa
 * Created on 2/22/17
 * Last Modified By: Jonathon Cooke-Akaiwa
 * Last Modified On: 2/22/17
 * holds the data format of each card
 */


public class TypeObject {
    private String name;
    private String type;


    TypeObject(String name, String type) {
        this.name = name;
        this.type = type;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    @Override
    public String toString() {
        return "Field [name=" + name + ",type=" + type + "]";
    }
}
