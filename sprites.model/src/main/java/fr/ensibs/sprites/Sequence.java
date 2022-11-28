package fr.ensibs.sprites;

import fr.ensibs.sprites.actions.SpriteAction;
import fr.ensibs.util.graphic.Image;

import java.util.List;

/**
 * A sequence having a background image, sprites and actions over the sprites.
 * Playing a sequence consists in applying the actions to the sprites over time
 * and updating the snapshot displayed consequently
 *
 * @param <I> the type of images that compose the sequence
 * @author Pascale Launay
 * @inv {@code getDuration() >= 0}
 * @inv {@code getActions() != null}
 */
public interface Sequence<I extends Image> extends Scene<I>
{
    /**
     * Give the sequence total duration (in ms)
     *
     * @return the sequence duration
     */
    int getDuration();

    /**
     * Give the actions over the sequence sprites
     *
     * @return the actions
     */
    List<SpriteAction<I>> getActions();
}
