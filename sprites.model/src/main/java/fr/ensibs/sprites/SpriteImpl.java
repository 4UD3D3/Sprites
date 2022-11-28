package fr.ensibs.sprites;

import java.util.ArrayList;
import java.lang.Math.*;

import fr.ensibs.util.graphic.Image;

/**
 * Sprite representation (animation composed of successive images)
 * @author Grégoire Truhé
 */
public class SpriteImpl<I extends Image> extends ArrayList<I> implements Sprite<I>{

    /**
     * Sprite's name
     */
    private String name;

    /**
     * Sprite's horizontal position
     */
    private int x;

    /**
     * Sprite's vertical position
     */
    private int y;

    /**
     * Sprite's visibility
     */
    private boolean visible;

    /**
     * Sprite's current time that determines its image to display
     */
    private int currentTime;

    /**
     * Sprite's total duration
     */
    private int duration;

    /**
     * Construct a new Sprites (empty, should add image with add method)
     * @param name of the sprite
     * @param duration of the animation
     * @param x position
     * @param y position
     * @param visible define the visibility of the sprite
     */
    public SpriteImpl(String name, int duration, int x, int y, boolean visible){
        super();
        this.name = name;
        this.duration = duration;
        this.currentTime = 0;
        this.currentTime = 0;
        this.x = x;
        this.y = y;
        this.visible = visible;
    }

    @Override
    public I getCurrentImage() {
        int index = (int)Math.floor(((double)currentTime/duration)*this.size());
        return this.get(index%this.size());
    }

    @Override
    public int getDuration() {
        return this.duration;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public ArrayList<String> getImageNames() {
        ArrayList<String> imgNames = new ArrayList();
        for(I img: this){
            imgNames.add(img.getName());
        }
        return imgNames;
    }

    @Override
    public int getX() {
        return this.x;
    }

    @Override
    public int getY() {
        return this.y;
    }

    @Override
    public boolean isVisible() {
        return this.visible;
    }

    @Override
    public void setCurrentTime(int time) {
        this.currentTime = time;
    }

    @Override
    public void setLocation(int x, int y) {
        if(x >= 0 && y >= 0) {
            this.x = x;
            this.y = y;
        }
    }

    @Override
    public void setVisible(boolean visible) {
        this.visible = visible;
    }
}
