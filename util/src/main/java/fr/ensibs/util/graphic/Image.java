package fr.ensibs.util.graphic;

/**
 * An image representation for any graphic system.
 *
 * @author Pascale Launay
 * @inv {@code getWidth() >= 0 && getHeight() >= 0}
 */
public interface Image
{
    /**
     * Give the name of the image.
     *
     * @return the name of the image
     */
    String getName();

    /**
     * Set the name of the image.
     *
     * @param name the new name of the image
     */
    void setName(String name);

    /**
     * Give the width of the image in pixels.
     *
     * @return the width of the image
     */
    int getWidth();

    /**
     * Give the height of the image in pixels.
     *
     * @return the height of the image
     */
    int getHeight();
}
