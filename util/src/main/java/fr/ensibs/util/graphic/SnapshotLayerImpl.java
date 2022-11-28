package fr.ensibs.util.graphic;

/**
 * Class representing a snapshot layer composed of an image laying at a given location
 *
 * @author Grégoire, Nassim
 */
public class SnapshotLayerImpl<I extends Image> implements SnapshotLayer<I>{

    /**
     * Layer's image
     */
    private I imgLayer;

    /**
     * Defines the image's coordinates
     */
    private int x, y, width, height;

    /**
     * Define new snapshot layer with specific coordinates
     * @param img inside layer
     * @param x coordinate
     * @param y coordinate
     * @param width length
     * @param height length
     */
    public SnapshotLayerImpl(I img, int x, int y, int width, int height){
        this.imgLayer = img;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public I getImage() {
        return this.imgLayer;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof SnapshotLayer<?>) {
            return this.getImage() == ((SnapshotLayer<?>) o).getImage() &&
                    this.getX() == ((SnapshotLayer<?>) o).getX() &&
                    this.getY() == ((SnapshotLayer<?>) o).getY() &&
                    this.getWidth() == ((SnapshotLayer<?>) o).getWidth() &&
                    this.getHeight() == ((SnapshotLayer<?>) o).getHeight();
        }
        return false;
    }
}