package fr.ensibs.sprites;

import fr.ensibs.util.graphic.Image;
import fr.ensibs.util.graphic.Snapshot;

import java.util.Map;

/**
 * Represents objects that can be played and displayed: produce snapshots relative to a given current
 * time
 *
 * @param <I> the type of images that compose the snapshots
 */
public interface Playable<I extends Image>
{
    /**
     * Give the snapshot corresponding to the current time.
     *
     * @return the current snapshot
     * @post {@code result != null}
     */
    Snapshot<I> getCurrentSnapshot();

    /**
     * Change the current time.
     *
     * @param time the new current time
     * @pre {@code time >= 0}
     */
    void setCurrentTime(int time);

    /**
     * Give the images that compose the playable instance, associated to their names
     *
     * @return a map composed of pairs (image name, image instance)
     */
    Map<String, I> getImages();
}