package fr.ensibs.util.graphic.json;

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

import fr.ensibs.util.graphic.SnapshotLayer;
import fr.ensibs.util.graphic.SnapshotLayerImpl;
import fr.ensibs.util.io.JsonConverter;
import fr.ensibs.util.io.SnapshotLayerConverter;

/**
 * Unit test for the {@link JsonConverter<SnapshotLayer>} class
 *
 * @author Pascale Launay
 */
public class SnapshotLayerConverterTest
{
    /**
     * A JSON object that represents a valid layer
     */
    private static final String LAYER_OBJ = "{ \"image\": \"image\", \"x\": 0, \"y\": 0, \"width\": 100, \"height\": 100 }";

    /**
     * Another JSON object that represents a valid layer
     */
    private static final String OTHER_LAYER_OBJ = "{ \"image\": \"other_image\", \"x\": 50, \"y\": 50 }";

    /**
     * A JSON object that represents an invalid layer
     */
    private static final String INVALID_IMAGE_LAYER_OBJ = "{ \"image\": \"invalid_image\", \"x\": 0, \"y\": 0, \"width\": 100, \"height\": 100 }";

    /**
     * A JSON object that represents a layer having an invalid image
     */
    private static final String INVALID_LAYER_OBJ = "{ \"image\": \"image\", \"x\": 0, \"width\": 100, \"height\": 100 }";

    /**
     * A  map of images
     */
    private static Map<String, ImageMock> IMAGES;

    /**
     * A snapshot layer
     */
    private static SnapshotLayer<ImageMock> LAYER;

    /**
     * Another snapshot layer
     */
    private static SnapshotLayer<ImageMock> OTHER_LAYER;

    /**
     * The instance to be tested
     */
    private JsonConverter<SnapshotLayer<ImageMock>> instance;

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

        LAYER = new SnapshotLayerImpl<>(IMAGES.get("image"), 0, 0, 100, 100);
        OTHER_LAYER = new SnapshotLayerImpl<>(IMAGES.get("other_image"), 50, 50, 100, 100);
    }

    /**
     * Initialize the instance to be tested (called before running each test)
     */
    @BeforeEach
    public void initialize()
    {
        this.instance = new SnapshotLayerConverter<>(IMAGES);
    }

    //------------------------------------------------------------------------
    // Tests of the fromJson method
    //------------------------------------------------------------------------

    @Test
    public void testFromJson()
    {
        testFromJson(new JSONObject(LAYER_OBJ), LAYER);
    }

    @Test
    public void testFromOtherJson()
    {
        testFromJson(new JSONObject(OTHER_LAYER_OBJ), OTHER_LAYER);
    }

    private void testFromJson(JSONObject obj, SnapshotLayer<ImageMock> expected)
    {
        try {
            // invoke the method to be tested
            SnapshotLayer<ImageMock> actual = instance.fromJson(obj);

            // check the method results
            assertNotNull(actual);
            assertEquals(expected, actual);

        } catch (ParseException e) {
            fail("Exception " + e.getClass() + " should not occur: " + e.getMessage());
        }
    }

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

    //------------------------------------------------------------------------
    // Tests of the toJson method
    //------------------------------------------------------------------------

    @Test
    public void testToJson()
    {
        // invoke the method to be tested
        JSONObject actual = instance.toJson(LAYER);

        // check the method results
        JSONAssert.assertEquals(LAYER_OBJ, actual, JSONCompareMode.STRICT);
    }
}
