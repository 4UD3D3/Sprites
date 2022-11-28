package fr.ensibs.util.io;

import fr.ensibs.util.graphic.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.Map;

/**
 * Class to handle snapshot in json format
 *
 * @author Grégoire, Mehdi, Nassim
 */
public class SnapshotConverter<I extends Image> implements JsonConverter<Snapshot<I>>{
    /**
     * Permet la conversion de chaque calque du snapshot
     */
    private SnapshotLayerConverter<I> snapLayerConv;

    /**
     * Crée un convertisseur de de snapshot
     *
     * @param mapImage clés : nom du fichier image, valeurs : images du snapshot
     */
    public SnapshotConverter(Map<String, I> mapImage){
        snapLayerConv = new SnapshotLayerConverter<>(mapImage);
    }

    /**
     * Lit l'array "layers" d'un JSONObject, convertit chaque objet de l'array en SnapshotLayer, et l'ajoute à la liste de Snapshot
     *
     * @author Gregoire and Mehdi
     * @param obj a JSON object
     * @return a Snapshot table of layers
     * @throws ParseException parse errors from JSON input
     */
    @Override
    public Snapshot<I> fromJson(JSONObject obj) throws ParseException {
        // List initialisation that will contain all snapshot layers
        Snapshot<I> layersList = new SnapshotImpl<>();

        if(!(obj.has("layers") && obj.get("layers") instanceof JSONArray)){
            throw new ParseException("L'objet JSON est invalide", 0);
        }

        JSONArray jsonArray = obj.getJSONArray("layers");

        for (int i = 0; i < jsonArray.length(); i++) {
            SnapshotLayer<I> snapLayer = snapLayerConv.fromJson(jsonArray.getJSONObject(i));
            layersList.add(snapLayer);
        }

        return layersList;
    }

    /**
     * Lit une liste de snapshot, crée un array layers dans lequel les objets du snapshot sont ajoutés (après conversion d'un snapshotLayer en JSONObject),
     * puis ajoute l'array layers dans un JSONObject
     *
     * @author Nassim
     * @param snap a Snapshot table of layers
     * @return a JSON object containing all snapshot layers
     */
    @Override
    public JSONObject toJson(Snapshot<I> snap) {
        JSONObject jsonSnap = new JSONObject();
        JSONArray jsonArray = new JSONArray();

        for(SnapshotLayer<I> snapLayer : snap){
            jsonArray.put(snapLayerConv.toJson(snapLayer)); // put un JSONObject
        }

        jsonSnap.put("layers", jsonArray);
        return jsonSnap;
    }
}