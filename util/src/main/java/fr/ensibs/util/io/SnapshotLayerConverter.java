package fr.ensibs.util.io;

import fr.ensibs.util.graphic.Image;
import fr.ensibs.util.graphic.SnapshotLayer;
import fr.ensibs.util.graphic.SnapshotLayerImpl;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.Map;

/**
 * Class to handle snapshot layer in json format
 *
 * @author Grégoire, Mehdi, Nassim
 */
public class SnapshotLayerConverter<I extends Image> implements JsonConverter<SnapshotLayer<I>> {
    /**
     * Associe le nom du fichier image avec son objet Image
     */
    private Map<String, I> mapImage;

    /**
     * Crée un convertisseur de calque de snapshot
     *
     * @param mapImage clés : nom du fichier image, valeurs : images du snapshot
     */
    public SnapshotLayerConverter(Map<String, I> mapImage){
        this.mapImage = mapImage;
    }

    /**
     * Return the corresponding Snapshot layer from JSon description
     *
     * @author Nassim
     * @param obj a JSON object representing a snapshot layer
     * @return a snapshot layer object
     * @throws ParseException parse errors from JSON object
     */
    @Override
    public SnapshotLayer<I> fromJson(JSONObject obj) throws ParseException {
        try{

            int x = obj.getInt("x");
            int y = obj.getInt("y");
            if(!(obj.get("image") instanceof String)){
                throw new ParseException("La source image est manquante dans l'objet JSON definissant l'image.'", 0);
            }
            if(!mapImage.containsKey(obj.get("image").toString())){
                throw new ParseException("L'image a convertir ne se trouve pas dans la map images.", 0);
            }
            I img = mapImage.get(obj.getString("image"));

            int width, height;
            if(obj.has("width")){
                width = obj.getInt("width");
            }else{
                width = img.getWidth();
            }
            if(obj.has("height")){
                height = obj.getInt("height");
            }else{
                height = img.getHeight();
            }

            return new SnapshotLayerImpl<>(img, x, y, width, height);
        }
        catch (JSONException e){
            throw new ParseException("Champs obligatoires : 'image', 'x', 'y' non-spécifiés", 0);
        }
    }

    /**
     * Obtain the json representation of the snapshot layer specified
     *
     * @author Grégoire, Nassim
     * @param snapLayer a snapshot layer
     * @return a json table
     */
    @Override
    public JSONObject toJson(SnapshotLayer<I> snapLayer) {
        //Creating JsonObject and fill its field
        JSONObject jsonLayer = new JSONObject();
        jsonLayer.put("image", snapLayer.getImage().getName());
        jsonLayer.put("x", snapLayer.getX());
        jsonLayer.put("y", snapLayer.getY());
        jsonLayer.put("width", snapLayer.getWidth());
        jsonLayer.put("height", snapLayer.getHeight());

        return jsonLayer;
    }
}