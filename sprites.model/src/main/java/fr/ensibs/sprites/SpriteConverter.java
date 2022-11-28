package fr.ensibs.sprites;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.ensibs.util.graphic.Image;
import fr.ensibs.util.io.JsonConverter;

/**
 * Handle conversion Json from/to SpriteImpl
 * @author Grégoire Truhé
 */
public class SpriteConverter<I extends Image> implements JsonConverter<Sprite<I>> {

    /**
     * Map that associates image names to image object
     */
    private Map<String, I> images;

    /**
     *
     * @param map_images containing all needed images
     */
    public SpriteConverter(Map<String, I> map_images){
        this.images = map_images;
    }

    @Override
    public SpriteImpl fromJson(JSONObject obj) throws ParseException {
        /**
         * Retrieve values from json
         */
        String name = (String) obj.get("name");
        int duration = (int) obj.get("duration");
        int x = (int) obj.get("x");
        int y = (int) obj.get("y");
        boolean visible = (boolean) obj.get("visible");
        JSONArray sprite_images = obj.getJSONArray("images");

        /**
         * Sprite initialisation
         */
        SpriteImpl sprite = new SpriteImpl(name,duration,x,y,visible);

        /**
         * Filling sprite
         */
        for(int i = 0; i<sprite_images.length(); i++){
            sprite.add(images.get(sprite_images.get(i)));
        }

        return sprite;
    }

    /**
     * Return the Json object corresponding to the sprite given
     * @param obj a Sprite object
     * @return its json representation
     */
    @Override
    public JSONObject toJson(Sprite obj) {
        JSONObject json_form = new JSONObject();

        /**
         * Adding all <key,obj> to the json form object
         */
        json_form.put("name",obj.getName());
        json_form.put("duration",obj.getDuration());
        json_form.put("x",obj.getX());
        json_form.put("y",obj.getY());
        json_form.put("visible",obj.isVisible());
        json_form.put("images", obj.getImageNames());

        return json_form;
    }
}
