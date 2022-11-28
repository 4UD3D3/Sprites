package fr.ensibs.sprites;

import fr.ensibs.util.graphic.Image;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


/**
 * Unit test for the class that implements the {@link Sprite} interface
 *
 * @author Grégoire Truhé
 */
public class SpriteImplTest
{
    /**
     * The instance to be tested
     */
    private Sprite<ImageMock> instance;

    /**
     * A  list of images
     */
    private static List<ImageMock> IMAGES;

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
        IMAGES = new ArrayList<>();
        ImageMock image = new ImageMock();
        image.setName("image");
        IMAGES.add(image);
        image = new ImageMock();
        image.setName("other_image");
        IMAGES.add(image);
        image = new ImageMock();
        image.setName("yet_another_image");
        IMAGES.add(image);
    }

    /**
     * Initialize the instance to be tested (called before running each test)
     */
    @BeforeEach
    public void initialize()
    {
        this.instance = new SpriteImpl("sprite", 1000, 0, 0, true);
        this.instance.addAll(IMAGES);
    }

    //------------------------------------------------------------------------
    // Tests of the getCurrentImage method
    //------------------------------------------------------------------------

    @Test
    public void testGetCurrentImage0()
    {
        testGetCurrentImage(0, IMAGES.get(0));
    }

    @Test
    public void testGetCurrentImage1200()
    {
        testGetCurrentImage(1200, IMAGES.get(0));
    }

    @Test
    public void testGetCurrentImage400()
    {
        testGetCurrentImage(400, IMAGES.get(1));
    }

    @Test
    public void testGetCurrentImage999()
    {
        testGetCurrentImage(999, IMAGES.get(2));
    }

    private void testGetCurrentImage(int time, ImageMock expected)
    {
        instance.setCurrentTime(time);
        ImageMock actual = instance.getCurrentImage();

        // check the method results
        assertNotNull(actual);
        assertEquals(expected, actual);
    }
}

/**
 * A class that implements the {@link Image} interface for testing purposes
 *
 * @author Pascale Launay
 */
class ImageMock implements Image
{
    /**
     * The image name
     */
    private String name;

    @Override
    public String getName()
    {
        return name;
    }

    @Override
    public void setName(String name)
    {
        this.name = name;
    }

    @Override
    public int getWidth()
    {
        return 100;
    }

    @Override
    public int getHeight()
    {
        return 100;
    }
}