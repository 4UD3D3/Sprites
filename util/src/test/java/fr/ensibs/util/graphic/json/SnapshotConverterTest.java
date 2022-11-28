package fr.ensibs.util.graphic.json;

import fr.ensibs.util.graphic.*;
import fr.ensibs.util.io.JsonConverter;
import fr.ensibs.util.io.SnapshotConverter;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit test for the {@link JsonConverter<Snapshot>} class
 *
 * @author Pascale Launay
 */
public class SnapshotConverterTest
{

    /**
     * A JSON object that represents a valid snapshot
     */
    private static final String SNAPSHOT_OBJ = "{ \"layers\": [ { \"image\": \"image\", \"x\": 0, \"y\": 0, \"width\": 100, \"height\": 100 }, { \"image\": \"other_image\", \"x\": 50, \"y\": 50 } ] }";

    /**
     * A JSON object that represents an empty snapshot
     */
    private static final String EMPTY_SNAPSHOT_OBJ = "{ \"layers\": [ ] }";

    /**
     * A JSON object that represents an invalid empty snapshot
     */
    private static final String INVALID_EMPTY_SNAPSHOT_OBJ = "{  }";

    /**
     * A JSON object that represents an invalid snapshot
     */
    private static final String INVALID_SNAPSHOT_OBJ = "{  \"layers\": [ { \"image\": \"image\", \"x\": 0, \"width\": 100, \"height\": 100 }, { \"image\": \"invalid_image\", \"x\": 50, \"y\": 50 } ] }";

    /**
     * An  map of images
     */
    private static Map<String, ImageMock> IMAGES;

    /**
     * A snapshot
     */
    private static Snapshot<ImageMock> SNAPSHOT;

    /**
     * An empty snapshot
     */
    private static Snapshot<ImageMock> EMPTY_SNAPSHOT;

    /**
     * The instance to be tested
     */
    private JsonConverter<Snapshot<ImageMock>> instance;

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

        SNAPSHOT = new SnapshotImpl<ImageMock>();
        SNAPSHOT.add(new SnapshotLayerImpl<>(IMAGES.get("image"), 0, 0, 100, 100));
        SNAPSHOT.add(new SnapshotLayerImpl<>(IMAGES.get("other_image"), 50, 50, 100, 100));
        EMPTY_SNAPSHOT = new SnapshotImpl<ImageMock>();
    }

    /**
     * Initialize the instance to be tested (called before running each test)
     */
    @BeforeEach
    public void initialize()
    {
        this.instance = new SnapshotConverter<>(IMAGES);
    }

    //------------------------------------------------------------------------
    // Tests of the fromJson method
    //------------------------------------------------------------------------
    @Test
    public void testFromJson()
    {
        testFromJson(new JSONObject(SNAPSHOT_OBJ), SNAPSHOT);
    }

    @Test
    public void testFromEmptyJson()
    {
        testFromJson(new JSONObject(EMPTY_SNAPSHOT_OBJ), EMPTY_SNAPSHOT);
    }

    private void testFromJson(JSONObject obj, Snapshot<ImageMock> expected)
    {
        try {
            // invoke the method to be tested
            Snapshot<ImageMock> actual = instance.fromJson(obj);

            // check the method results
            assertNotNull(actual);
            assertEquals(expected, actual);

        } catch (ParseException e) {
            fail("Exception " + e.getClass() + " should not occur: " + e.getMessage());
        }
    }

    @Test
    public void testFromInvalidEmptyJson()
    {
        testFromInvalidJson(new JSONObject(INVALID_EMPTY_SNAPSHOT_OBJ));
    }

    @Test
    public void testFromInvalidJson()
    {
        testFromInvalidJson(new JSONObject(INVALID_SNAPSHOT_OBJ));
    }

    private void testFromInvalidJson(JSONObject obj)
    {
        try {
            // invoke the method to be tested
            instance.fromJson(obj);

            // check the method results
            fail("ParseException should be thrown for this invalid snapshot JSON object");

        } catch (ParseException e) {
            assertNotNull(e);
        }
    }
}
