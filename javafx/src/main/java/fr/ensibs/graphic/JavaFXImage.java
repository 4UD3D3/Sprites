package fr.ensibs.graphic;

import javafx.scene.image.Image;

/**
 * Javafx class to represent image that can be display on UI
 *
 * @author Grégoire, Mehdi, Andréa
 */
public class JavaFXImage implements fr.ensibs.util.graphic.Image {
    /**
     * Image that can be displayed
     */
    private Image img;

    /**
     * Image's name
     */
    private String name;

    /**
     * Image's dimension
     */
    private int width;
    private int height;

    /**
     * Build image from stream constructor
     *
     * @param img stream containing the image
     */
    public JavaFXImage(Image img){
        this.img = img;
        this.width = (int)img.getWidth();
        this.height = (int)img.getHeight();
    }

    /**
     * Get the image initialised from stream
     * @return image loaded
     */
    public Image getImage(){
        return this.img;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int getWidth() {
        return this.width;
    }

    @Override
    public int getHeight() {
        return this.height;
    }
}
