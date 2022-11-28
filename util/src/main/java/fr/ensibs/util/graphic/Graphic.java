package fr.ensibs.util.graphic;

/**
 * A generic graphic system to draw images and snapshots.
 *
 * @param <I> the type of images displayed on the graphic system
 * @author Pascale Launay
 * @inv {@code getWidth() >= 0 && getHeight() >= 0}
 */
public interface Graphic<I extends Image>
{
    /**
     * Give the width of the graphic area in pixels.
     *
     * @return the width of the graphic area
     */
    int getWidth();

    /**
     * Give the height of the graphic area in pixels.
     *
     * @return the height of the graphic area
     */
    int getHeight();

    /**
     * Draw the given image.
     *
     * @param image the image to be drawn
     * @pre {@code image != null}
     */
    void drawImage(I image);

    /**
     * Draw the given snapshot.
     *
     * @param snapshot the snapshot to be drawn
     * @pre {@code snapshot != null}
     */
    void drawSnapshot(Snapshot<I> snapshot);

    /**
     * Clear the previously displayed image or snapshot
     */
    void clear();
}
