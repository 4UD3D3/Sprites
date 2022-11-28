package fr.ensibs.util.io;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;

import java.io.*;
import java.text.ParseException;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Unit test for the class that implements the {@link Loader} interface
 *
 * @author Pascale Launay
 */
public class JsonLoaderImplTest
{
    /**
     * An empty JSON object
     */
    private static JSONObject EMPTY_OBJ;

    /**
     * A simple JSON object
     */
    private static JSONObject SIMPLE_OBJ;

    /**
     * A complex JSON object
     */
    private static JSONObject COMPLEX_OBJ;

    /**
     * A JSON object that contains an array
     */
    private static JSONObject ARRAY_OBJ;

    /**
     * The instance to be tested
     */
    private Loader<JSONObject> instance;

    //------------------------------------------------------------------------
    // Initializations
    //------------------------------------------------------------------------

    /**
     * Initialize the JSON objects used in the following tests (called once before
     * running all tests)
     */
    @BeforeAll
    public static void setup()
    {
        // empty object
        EMPTY_OBJ = new JSONObject();
        // simple object
        SIMPLE_OBJ = new JSONObject();
        SIMPLE_OBJ.put("id", 123);
        SIMPLE_OBJ.put("name", "John");
        // complex object
        JSONObject person = new JSONObject();
        person.put("name", "John");
        person.put("surname", "Doe");
        COMPLEX_OBJ = new JSONObject();
        COMPLEX_OBJ.put("person", person);
        // object that contains an array
        JSONArray values = new JSONArray();
        for (int i = 0; i < 5; i++) {
            values.put(i);
        }
        ARRAY_OBJ = new JSONObject();
        ARRAY_OBJ.put("values", values);
    }

    /**
     * Initialize the instance to be tested (called before running each test)
     */
    @BeforeEach
    public void initialize()
    {
        // TODO initialize the instance to be tested
        this.instance = new JsonLoaderImpl();
    }

    //------------------------------------------------------------------------
    // Tests of the load method
    //------------------------------------------------------------------------

    /**
     * Test the {@link Loader#load(InputStream)} method with an empty JSON object
     */
    @Test
    public void testLoadEmpty()
    {
        testLoad("/json/empty.json", EMPTY_OBJ);
    }

    /**
     * Test the {@link Loader#load(InputStream)} method with a simple JSON object
     */
    @Test
    public void testLoadSimple()
    {
        testLoad("/json/simple.json", SIMPLE_OBJ);
    }

    /**
     * Test the {@link Loader#load(InputStream)} method with a complex JSON object
     */
    @Test
    public void testLoadComplex()
    {
        testLoad("/json/complex.json", COMPLEX_OBJ);
    }

    /**
     * Test the {@link Loader#load(InputStream)} method with a JSON object that contains an array
     */
    @Test
    public void testLoadArray()
    {
        testLoad("/json/array.json", ARRAY_OBJ);
    }

    /**
     * Test the {@link Loader#load(InputStream)} method with the given arguments
     *
     * @param inFile   the name of the input file in the test resources
     * @param expected the expected result
     */
    private void testLoad(String inFile, JSONObject expected)
    {

        // initialize the parameter
        try (InputStream in = JsonLoaderImplTest.class.getResourceAsStream(inFile)) {

            // invoke the method to be tested
            JSONObject actual = instance.load(in);

            // check the method results
            assertNotNull(actual);
            JSONAssert.assertEquals(expected, actual, JSONCompareMode.STRICT);

        } catch (IOException | ParseException e) {
            fail("Exception " + e.getClass() + " should not occur: " + e.getMessage());
        }
    }

    //------------------------------------------------------------------------
    // Tests of the save method
    //------------------------------------------------------------------------

    /**
     * Test the {@link Loader<JSONObject>#save(JSONObject, OutputStream)} method with an empty JSON object
     */
    @Test
    public void testSaveEmpty()
    {
        testSave(EMPTY_OBJ);
    }

    /**
     * Test the {@link Loader<JSONObject>#save(JSONObject, OutputStream)} method with a simple JSON object
     */
    @Test
    public void testSaveSimple()
    {
        testSave(SIMPLE_OBJ);
    }

    /**
     * Test the {@link Loader<JSONObject>#save(JSONObject, OutputStream)} method with a complex JSON object
     */
    @Test
    public void testSaveComplex()
    {
        testSave(COMPLEX_OBJ);
    }

    /**
     * Test the {@link Loader<JSONObject>#save(JSONObject, OutputStream)} method with a JSON object that contains an array
     */
    @Test
    public void testSaveArray()
    {
        testSave(ARRAY_OBJ);
    }

    /**
     * Test the {@link Loader<JSONObject>#save(JSONObject, OutputStream)} method with the given argument
     *
     * @param obj the object to be saved and then reloaded
     */
    private void testSave(JSONObject obj)
    {

        try {
            byte[] buffer;

            // initialize the parameters
            try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {

                // invoke the method to be tested
                instance.save(obj, out);
                buffer = out.toByteArray();
            }
            // check the method results by loading the saved object
            try (InputStream in = new ByteArrayInputStream(buffer)) {
                JSONObject actual = instance.load(in);
                JSONAssert.assertEquals(obj, actual, JSONCompareMode.STRICT);
            }

        } catch (IOException | ParseException e) {
            fail("Exception " + e.getClass() + " should not occur: " + e.getMessage());
        }
    }
}
