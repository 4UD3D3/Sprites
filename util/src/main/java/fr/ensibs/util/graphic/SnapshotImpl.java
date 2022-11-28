package fr.ensibs.util.graphic;

import java.util.List;
import java.util.ArrayList;

/**
 * A snapshot composed of different images in overlapped layers.
 *
 * @param <I> the type of images in the snapshot layers
 * @author Pascale Launay, Nassim
 */
public class SnapshotImpl<I extends Image> extends ArrayList<SnapshotLayer<I>> implements Snapshot<I> {
}
