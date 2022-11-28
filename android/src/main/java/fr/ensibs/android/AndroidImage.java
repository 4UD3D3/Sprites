package fr.ensibs.android;

import android.graphics.Bitmap;

import fr.ensibs.util.graphic.Image;

/**
 * Represented a Image for android
 *
 * @author Andréa Gainche
 */

public class AndroidImage implements fr.ensibs.util.graphic.Image{

    /**
     * bitmap of the image
     */

    private Bitmap bitmap;

    /**
     * Image's name
     */
    private String name;

    /**
     * Image's dimension
     */
    private final int width;
    private final int height;

    public AndroidImage(Bitmap bitmap){
        this.bitmap = bitmap;
        this.width = (int)bitmap.getWidth();
        this.height = (int)bitmap.getHeight();
    }

    public Bitmap getBitmap() {
        return bitmap;
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
