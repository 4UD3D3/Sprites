package fr.ensibs.util.graphic;

import java.util.List;

/**
 * A snapshot composed of different images in overlapped layers.
 *
 * @param <I> the type of images in the snapshot layers
 * @author Nassim
 */
public interface Snapshot<I extends Image> extends List<SnapshotLayer<I>> {
}
