package fr.ensibs.sprites;

import fr.ensibs.util.graphic.Image;

import java.util.ArrayList;
import java.util.List;

/**
 * A sprite composed of a list of images and characterized by a duration. The
 * sprite can be played by displaying the images one after the other in a cyclic way.
 * The duration represents the time needed to display all the images in the list once.
 *
 * @param <I> the type of images that compose the sprite
 * @author Pascale Launay
 * @inv all the images that compose the sprite have the same width and height
 * @inv {@code getName() != null}
 * @inv {@code getDuration() >= 0}
 */
public interface Sprite<I extends Image> extends List<I>
{
    /**
     * Give the image corresponding to the current time.
     *
     * @return the image
     */
    I getCurrentImage();

    /**
     * Give the total duration to display the images (in ms).
     *
     * @return total duration to display the images
     */
    int getDuration();

    /**
     * Give the name of the sprite.
     *
     * @return the name of the sprite
     */
    String getName();

    /**
     * @return image names that composed the sprite
     */
    ArrayList<String> getImageNames();

    /**
     * Give the x coordinate of the location of the sprite.
     *
     * @return x coordinate of the sprite location
     */
    int getX();

    /**
     * Give the y coordinate of the location of the sprite.
     *
     * @return y coordinate of the sprite location
     */
    int getY();

    /**
     * Return true if the sprite is currently visible.
     *
     * @return true if the sprite is currently visible
     */
    boolean isVisible();

    /**
     * Change the current time.
     *
     * @param time the new current time
     * @pre {@code time >= 0}
     */
    void setCurrentTime(int time);

    /**
     * Set the sprite location
     *
     * @param x x coordinate of the location of the sprite (in pixels)
     * @param y y coordinate of the location of the sprite (in pixels)
     */
    void setLocation(int x, int y);

    /**
     * Change the visibility of the sprite.
     *
     * @param visible true if the sprite is visible
     */
    void setVisible(boolean visible);

}
