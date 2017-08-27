package edu.jcookeakindiana.flashcards;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    private ArrayList<CategoriesObject> categories = new ArrayList<CategoriesObject>();

    @Test
    public void categories_creation() throws Exception {
        for(int i = 0; i < 3; i++) {
            String name = "name " + i;
            CategoriesObject cat = new CategoriesObject(name);
            categories.add(cat);
        }
        assertEquals(3, categories.size());
    }

    @Test
    public void saveJson_test() throws Exception {
        IOHandler.saveJson(categories);
        assertEquals(1,1);
    }
}