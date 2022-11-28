package fr.ensibs.util.io;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test for the classes that implements the {@link Loader}
 * interfaces. After the unit tests of each class, this test
 * checks the integration of those classes
 *
 * @author Pascale Launay
 */
public class LoadersImplTest
{
    /**
     * A short text
     */
    private static final String SHORT_TXT = "This is a very short text";

    /**
     * A simple map
     */
    private static Map<String, Object> SIMPLE_MAP;

    /**
     * A complex map
     */
    private static Map<String, Object> COMPLEX_MAP;

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
     * The {@link ZipLoader} instance to be tested
     */
    private ZipLoader instance;

    //------------------------------------------------------------------------
    // Initializations
    //------------------------------------------------------------------------

    /**
     * Initialize the objects used in the following tests (called once before
     * running all tests)
     */
    @BeforeAll
    public static void setup()
    {
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
        // simple map
        SIMPLE_MAP = new HashMap<>();
        SIMPLE_MAP.put("simple.json", SIMPLE_OBJ);
        // complex map
        COMPLEX_MAP = new HashMap<>();
        COMPLEX_MAP.put("complex.json", COMPLEX_OBJ);
        COMPLEX_MAP.put("array.json", ARRAY_OBJ);
        COMPLEX_MAP.put("short.txt", SHORT_TXT);
    }

    /**
     * Initialize the instance to be tested (called before running each test)
     */
    @BeforeEach
    public void initialize()
    {
        // TODO initialize the instances to be tested
        Loader<JSONObject> jsonLoader = null; // new JsonLoaderImpl();
        Loader<String> textLoader = null; // new TextLoaderImpl();
        this.instance = null; // new ZipLoaderImpl(jsonLoader, textLoader);
    }

    //------------------------------------------------------------------------
    // Tests of the load method
    //------------------------------------------------------------------------

    /**
     * Test the {@link ZipLoader#load(ZipInputStream)} method with a simple map
     */
    @Test
    public void testLoadSimple()
    {
        testLoad("/zip/simple.zip", SIMPLE_MAP);
    }

    /**
     * Test the {@link ZipLoader#load(ZipInputStream)} method with a simple map
     */
    @Test
    public void testLoadComplex()
    {
        testLoad("/zip/complex.zip", COMPLEX_MAP);
    }

    /**
     * Test the {@link ZipLoader#load(ZipInputStream)} method with the given arguments
     *
     * @param inFile   the name of the input file in the test resources
     * @param expected the expected result
     */
    private void testLoad(String inFile, Map<String, Object> expected)
    {

        // initialize the parameter
        try (ZipInputStream in = new ZipInputStream(Objects.requireNonNull(ZipLoaderImplTest.class.getResourceAsStream(inFile)))) {

            // invoke the method to be tested
            Map<String, Object> actual = instance.load(in);

            // check the method results
            assertNotNull(actual);
            compareMaps(expected, actual);

        } catch (IOException | ParseException e) {
            fail("Exception " + e.getClass() + " should not occur: " + e.getMessage());
        }
    }

    //------------------------------------------------------------------------
    // Tests of the save method
    //------------------------------------------------------------------------

    /**
     * Test the {@link ZipLoader#save(Map, ZipOutputStream)} method with a simple map
     */
    @Test
    public void testSaveSimple()
    {
        testSave(SIMPLE_MAP);
    }

    /**
     * Test the {@link ZipLoader#save(Map, ZipOutputStream)} method with a complex map
     */
    @Test
    public void testSaveComplex()
    {
        testSave(COMPLEX_MAP);
    }

    /**
     * Test the {@link ZipLoader#save(Map, ZipOutputStream)} method with the given argument
     *
     * @param resources the object to be saved and then reloaded
     */
    private void testSave(Map<String, Object> resources)
    {

        try {
            byte[] buffer;
            // initialize the parameters
            try (ByteArrayOutputStream out = new ByteArrayOutputStream();
                 ZipOutputStream zout = new ZipOutputStream(out)) {

                // invoke the method to be tested
                instance.save(resources, zout);
                buffer = out.toByteArray();
            }

            // check the method results by loading the saved object
            try (ZipInputStream in = new ZipInputStream(new ByteArrayInputStream(buffer))) {
                Map<String, Object> actual = instance.load(in);
                compareMaps(resources, actual);
            }

        } catch (IOException | ParseException e) {
            fail("Exception " + e.getClass() + " should not occur: " + e.getMessage());
        }
    }

    //------------------------------------------------------------------------
    // Comparisons
    //------------------------------------------------------------------------

    /**
     * Assert the given maps contain the same entries
     *
     * @param expected the expected map
     * @param actual   the actual map
     */
    private void compareMaps(Map<String, Object> expected, Map<String, Object> actual)
    {
        assertEquals(expected.size(), actual.size());
        for (String key : expected.keySet()) {
            Object value = expected.get(key);
            if (value instanceof JSONObject) {
                assertInstanceOf(JSONObject.class, actual.get(key));
                JSONAssert.assertEquals((JSONObject) value, (JSONObject) actual.get(key), JSONCompareMode.STRICT);
            } else {
                assertEquals(value, actual.get(key));
            }
        }
    }
}
