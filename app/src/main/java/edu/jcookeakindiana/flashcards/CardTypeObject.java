package edu.jcookeakindiana.flashcards;

/**
 * Created by Jonathon Cooke-Akaiwa
 * Created on 3/1/17
 * Last Modified By: Jonathon Cooke-Akaiwa
 * Last Modified On: 3/1/17
 * holds the data of each flashcard's typed defined in Category
 */

public class CardTypeObject extends TypeObject {

    private String content;

    CardTypeObject(String name, String type) {
        super(name, type);
    }

    CardTypeObject(String name, String type, String content) {
        super(name, type);
        this.content = content;
    }

    CardTypeObject(TypeObject type) {
        super(type.getName(), type.getType());
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    //used for deserializing object into JSON
    @Override
    public String toString() {
        return "Field [name=" + getName()
                + ",type=" + getType() + ", content=" + content + "]";
    }
}
