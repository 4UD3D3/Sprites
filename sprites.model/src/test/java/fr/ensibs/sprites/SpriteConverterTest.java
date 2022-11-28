package fr.ensibs.sprites;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

import org.json.JSONObject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

import fr.ensibs.util.io.JsonConverter;

/**
 * Unit test for the SpriteConverter class
 *
 * @author Grégoire Truhé
 */
public class SpriteConverterTest {

    /**
     * A JSON object that represents a valid sprite
     */
    private static final String SPRITE_OBJ = "{\"name\": \"test\", " +
            "\"duration\": 1000,\"images\": [\"image\", \"other_image\"], " +
            "\"x\": 0, \"y\": 0, \"visible\": true }";

    /**
     * A JSON object that represents an invalid sprite
     */
    //private static final String INVALID_IMAGE_LAYER_OBJ = "{ \"image\": \"invalid_image\", \"x\": 0, \"y\": 0, \"width\": 100, \"height\": 100 }";

    /**
     * A JSON object that represents a layer having an invalid image
     */
    //private static final String INVALID_LAYER_OBJ = "{ \"image\": \"image\", \"x\": 0, \"width\": 100, \"height\": 100 }";

    /**
     * A  map of images
     */
    private static Map<String, ImageMock> IMAGES;

    /**
     * A sprite object
     */
    private static Sprite<ImageMock> SPRITE;

    /**
     * The instance to be tested
     */
    private JsonConverter<Sprite<ImageMock>> instance;

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
        IMAGES = new HashMap<>();
        ImageMock image = new ImageMock();
        image.setName("image");
        IMAGES.put("image", image);
        image = new ImageMock();
        image.setName("other_image");
        IMAGES.put("other_image", image);

        //Create and fill the sprite
        SPRITE = new SpriteImpl("test",1000,0,0,true);
        SPRITE.add(IMAGES.get("image"));
        SPRITE.add(IMAGES.get("other_image"));
    }

    /**
     * Initialize the instance to be tested (called before running each test)
     */
    @BeforeEach
    public void initialize()
    {
        this.instance = new SpriteConverter(IMAGES);
    }

    //------------------------------------------------------------------------
    // Tests of the fromJson method
    //------------------------------------------------------------------------

    @Test
    public void testFromJson()
    {
        testFromJson(new JSONObject(SPRITE_OBJ), SPRITE);
    }

    private void testFromJson(JSONObject obj, Sprite<ImageMock> expected)
    {
        try {
            // invoke the method to be tested
            Sprite<ImageMock> actual = instance.fromJson(obj);

            // check the method results
            assertNotNull(actual);
            assertEquals(expected, actual);

        } catch (ParseException e) {
            fail("Exception " + e.getClass() + " should not occur: " + e.getMessage());
        }
    }

    /**
    @Test
    public void testFromInvalidJson()
    {
        testFromInvalidJson(new JSONObject(INVALID_LAYER_OBJ));
    }

    @Test
    public void testFromInvalidImageJson()
    {
        testFromInvalidJson(new JSONObject(INVALID_IMAGE_LAYER_OBJ));
    }

    private void testFromInvalidJson(JSONObject obj)
    {
        try {
            // invoke the method to be tested
            instance.fromJson(obj);

            // check the method results
            fail("ParseException should be thrown for this invalid layer JSON object");

        } catch (ParseException e) {
            assertNotNull(e);
        }
    }
    */
    //------------------------------------------------------------------------
    // Tests of the toJson method
    //------------------------------------------------------------------------

    @Test
    public void testToJson()
    {
        // invoke the method to be tested
        JSONObject actual = instance.toJson(SPRITE);
        // check the method results
        JSONAssert.assertEquals(SPRITE_OBJ, actual, JSONCompareMode.STRICT);
    }
}
