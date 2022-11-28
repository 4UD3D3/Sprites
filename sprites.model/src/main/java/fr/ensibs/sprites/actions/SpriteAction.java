package fr.ensibs.sprites.actions;

import fr.ensibs.sprites.Sprite;
import fr.ensibs.util.graphic.Image;

/**
 * An action related to a sprite in a sequence
 *
 * @param <I> the type of images that compose the sprite related to this action
 * @inv {@code getSprite() != null}
 * @inv {@code getStartTime() >= 0}
 * @inv {@code getEndTime() >= getStartTime()}
 */
public interface SpriteAction<I extends Image>
{
    /**
     * Give the sprite this action is related to
     *
     * @return the sprite of this action
     */
    Sprite<I> getSprite();

    /**
     * Give the time when this action starts in the sequence
     *
     * @return the action start time
     */
    int getStartTime();

    /**
     * Give the time when this action ends in the sequence
     *
     * @return the action end time
     */
    int getEndTime();

    /**
     * Execute the current action for the given time
     *
     * @param time the time when the action has to be done
     */
    void doAction(int time);

    /**
     * Check whether this action is finished
     *
     * @return true if this action is finished
     */
    boolean isDone();
}
