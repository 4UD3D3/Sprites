package fr.ensibs.util.graphic;

/**
 * A layer composed of an image laying at a given location.
 *
 * @param <I> the type of image in the layer
 * @author Pascale Launay
 * @inv {@code getImage() != null}
 */
public interface SnapshotLayer<I extends Image>
{
    /**
     * Give the image drawn on the layer.
     *
     * @return the image drawn on the layer
     */
    I getImage();

    /**
     * Give the x coordinate of the upper left bound of the image (in pixels).
     *
     * @return the x coordinate of the upper left bound of the image
     */
    int getX();

    /**
     * Give the y coordinate of the upper left bound of the image (in pixels).
     *
     * @return the y coordinate of the upper left bound of the image
     */
    int getY();

    /**
     * Give the width of the area used to display the image (in pixels).
     *
     * @return the width of the area used to display the image
     */
    int getWidth();

    /**
     * Give the height of the area used to display the image (in pixels).
     *
     * @return the height of the area used to display the image
     */
    int getHeight();

    /**
     * Compare deux snapshotLayers (selon les attributs image, x, y, width, height (si existants))
     *
     * @param o un snapshot layer à comparer avec this
     * @return true si deux objets ont des valeurs identiques
     */
    @Override
    boolean equals(Object o);
}
