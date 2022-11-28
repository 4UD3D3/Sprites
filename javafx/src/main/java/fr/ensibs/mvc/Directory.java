package fr.ensibs.mvc;

import java.io.*;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import fr.ensibs.graphic.JavaFXImage;
import fr.ensibs.graphic.JavaFXImageLoader;
import fr.ensibs.util.graphic.Image;
import fr.ensibs.util.graphic.Snapshot;
import fr.ensibs.util.io.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * A simple directory composed of a list of names
 *
 * @author Nassim
 *
 * @inv getNames() != null
 */
public class Directory {
    /**
     * Map de fichiers contenant les fichiers et leur contenu
     */
    private Map<String, Object> resources;
    /**
     * Map de fichiers permettant de comparer une ressource modifiée à l'archive de base (utile pour reset)
     */
    private Map<String, Object> baseArchive;

    /**
     * Permet de lire et écrire dans une archive zip
     */
    private ZipLoader zipLoader;

    /**
     * The names in the directory
     */
    private List<String> names;

    /**
     * Constructor. Initialize the default values
     */
    public Directory() {
        names = new ArrayList<>();
        resources = new HashMap<>();
        baseArchive = new HashMap<>();

        Loader<String> textLoader = new TextLoaderImpl();
        Loader<JSONObject> jsonLoader = new JsonLoaderImpl();
        Loader<Image> imageLoader = new JavaFXImageLoader();
        zipLoader = new ZipLoaderImpl(jsonLoader, textLoader, imageLoader);
    }

    /**
     * Clears the directory
     */
    public void clear() {
        names.clear();
        resources.clear();
        baseArchive.clear();
    }

    /**
     * Reset the directory to its initial values
     */
    public void reset() {
        names.clear();
        resources.clear();
        for (Map.Entry<String, Object> entry : baseArchive.entrySet()) {
            names.add(entry.getKey());
        }
        resources.putAll(baseArchive); // vide les ressources pour les re-remplir avec les fichiers d'archive de base
    }

    /**
     * Add a new name to the directory
     *
     * @param name the new name
     * @pre name != null
     */
    public void addName(String name) {
        names.add(name);
    }

    /**
     * Give the names in the directory
     *
     * @return the directory names
     */
    public List<String> getNames() {
        return names;
    }

    /**
     * Ajoute un fichier zip dans les ressources, ajoute dans la liste les fichiers contenus dans l'archive
     *
     * @param file fichier zip
     * @throws IOException    source introuvable ou invalide
     * @throws ParseException table JSON non-parsable
     */
    public void addFile(File file) throws IOException, ParseException {
        if (file != null) {
            // Met à jour la map resources avec les fichiers contenus dans l'archive zip
            ZipInputStream in = new ZipInputStream(new FileInputStream(file.getAbsolutePath()));
            resources = zipLoader.load(in);
            baseArchive = zipLoader.load(in);

            names.addAll(resources.keySet());
        }
    }

    /**
     * Retourne le contenu du fichier (pour pouvoir l'afficher)
     *
     * @param filename nom du fichier
     * @return le contenu sous forme affichable (dans TextArea ou ImageArea)
     */
    public Object displayContent(String filename) {
        Object content = resources.get(filename);

        switch (ZipLoader.getExtension(filename)) {
            case "json":
            case "JSON":
                return content.toString();
            case "txt":
            case "TXT":
            case "jpg":
            case "png":
                return content;
            default:
                return "Format non-lisible";
        }
    }

    /**
     * Ajoute un fichier dans la liste
     *
     * @param file nom du fichier (json, img, text...)
     * @throws IOException    source introuvable ou invalide
     * @throws ParseException table JSON non-parsable
     */
    public void addFileTo(File file) throws IOException, ParseException {
        resources.put(file.getName(), zipLoader.loadFile(file.getAbsolutePath()));
    }

    /**
     * Enregistre les fichiers de la map dans l'archive
     *
     * @param zipname nom de l'archive zip
     * @throws IOException source introuvable ou invalide
     */
    public void saveArchive(String zipname) throws IOException {
        FileOutputStream fout = new FileOutputStream(zipname);
        ZipOutputStream zout = new ZipOutputStream(fout);

        zipLoader.save(resources, zout);
        fout.close();
    }

    /**
     * Retourne la snapshot correspondante au fichier JSON d'entrée
     *
     * @param filename nom du fichier JSON
     * @return snapshot composée des layers image
     * @throws JSONException         lecture erronée des JSON
     * @throws ParseException        erreurs de syntaxe des JSON
     * @throws FileNotFoundException fichiers image non-chargés
     */
    public Snapshot<JavaFXImage> getSnapshot(String filename) throws JSONException, ParseException, FileNotFoundException {
        JSONObject obj = (JSONObject) resources.get(filename);
        Map<String, JavaFXImage> mapImage = new HashMap<>();

        if (obj.has("layers")) {
            // Each layer in json are added to the list
            JSONArray jsonArray = obj.getJSONArray("layers");

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject objImg = jsonArray.getJSONObject(i);
                String objName = objImg.getString("image");

                if(!resources.containsKey(objName)){ // la map image ne peut pas être construite si les fichiers images n'ont pas été chargés en amont
                    throw new FileNotFoundException("Les fichiers image-source n'ont pas ete charges, impossible de construire les calques. Veuillez les ajouter a votre archive zip.");
                }
                if (resources.get(objName) instanceof JavaFXImage) {
                    mapImage.put(objName, (JavaFXImage) resources.get(objName));
                } else {
                    throw new ClassCastException("Le fichier n'est pas a un format image compatible (.jpg ou .png)");
                }
            }
        }
        return new SnapshotConverter<>(mapImage).fromJson(obj);
    }
}