package fr.ensibs.util.io;

import org.json.JSONObject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;

import java.io.*;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit test for the class that implements the {@link ZipLoader} interface
 * that relies on mocks for the {@link Loader}
 * implementations
 *
 * @author Pascale Launay
 */
public class ZipLoaderImplTest
{
    /**
     * An empty map
     */
    private static Map<String, Object> EMPTY_MAP;

    /**
     * A simple map
     */
    private static Map<String, Object> SIMPLE_MAP;

    /**
     * A complex map
     */
    private static Map<String, Object> COMPLEX_MAP;

    /**
     * The instance to be tested
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
        // empty map
        EMPTY_MAP = new HashMap<>();
        // simple map
        SIMPLE_MAP = new HashMap<>();
        SIMPLE_MAP.put("simple.json", new JSONObject());
        // complex map
        COMPLEX_MAP = new HashMap<>();
        COMPLEX_MAP.put("complex.json", new JSONObject());
        COMPLEX_MAP.put("array.json", new JSONObject());
        COMPLEX_MAP.put("short.txt", "mockText");
    }

    /**
     * Initialize the instance to be tested (called before running each test)
     */
    @BeforeEach
    public void initialize()
    {
        // TODO initialize the instance to be tested
        Loader<JSONObject> jsonLoader = new JsonLoaderMock();
        Loader<String> textLoader = new TextLoaderMock();

        this.instance = new ZipLoaderImpl(jsonLoader, textLoader);
    }

    //------------------------------------------------------------------------
    // Tests of the load method
    //------------------------------------------------------------------------

    /**
     * Test the {@link ZipLoader#load(ZipInputStream)} method with an empty map
     */
    @Test
    public void testLoadEmpty()
    {
        testLoad("/zip/empty.zip", EMPTY_MAP);
    }

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
        try (ZipInputStream in = new ZipInputStream(ZipLoaderImplTest.class.getResourceAsStream(inFile))) {

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
     * Test the {@link ZipLoader#save(Map, ZipOutputStream)} method with an empty map
     */
    @Test
    public void testSaveEmpty()
    {
        testSave(EMPTY_MAP);
    }

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

//----------------------------------------------------------------------------
// Mock classes
//----------------------------------------------------------------------------

/**
 * A class that implements the {@link Loader} interface for tests purposes
 * and returns arbitrary results
 *
 * @author Pascale Launay
 */
class JsonLoaderMock implements Loader<JSONObject>
{
    @Override
    public JSONObject load(InputStream in)
    {
        return new JSONObject();
    }

    @Override
    public void save(JSONObject obj, OutputStream out) throws IOException
    {
        out.write("{}".getBytes());
    }
}

/**
 * A class that implements the {@link Loader} interface for tests purposes
 * and returns arbitrary results
 *
 * @author Pascale Launay
 */
class TextLoaderMock implements Loader<String> {
    public String load(InputStream in)
    {
        return "mockText";
    }

    public void save(String text, OutputStream out) throws IOException
    {
        out.write("mockText".getBytes());
    }
}