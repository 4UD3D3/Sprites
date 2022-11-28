package fr.ensibs.android;

import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import fr.ensibs.util.io.EmptyImage;
import fr.ensibs.util.io.Loader;
import fr.ensibs.util.io.ZipLoader;

/**
 * @author Andréa Gainche
 */

public class ZipLoaderImplAndroid implements ZipLoader {

    /**
     * The json loader to load and save json file
     */
    private Loader<JSONObject> jsonLoader;

    /**
     * The text loader to load and save text file
     */
    private Loader<String> textLoader;

    /**
     * The image loader to load and save image
     */
    private Loader<AndroidImage> imageLoader;

    /**
     * Constructor of ZipLoaderImpl class
     *
     * @param jsonLoader  allows zipping JSON files
     * @param textLoader  allows zipping .txt files
     * @param imageLoader allows zipping images.
     */
    public ZipLoaderImplAndroid(Loader<JSONObject> jsonLoader, Loader<String> textLoader, Loader<AndroidImage> imageLoader) {
        this.jsonLoader = jsonLoader;
        this.textLoader = textLoader;
        this.imageLoader = imageLoader;
    }

    @Override
    public Map<String, Object> load(ZipInputStream in) throws IOException, ParseException {
        Map<String, Object> resources = new HashMap<>();

        ZipEntry entry;
        while ((entry = in.getNextEntry()) != null && !entry.isDirectory()) {
            String filename = entry.getName();
            String fileExtension = ZipLoader.getExtension(filename);
            // Load it in the map
            switch (fileExtension) {
                case "txt":
                case "TXT":
                    resources.put(filename, textLoader.load(in));
                    break;
                case "json":
                case "JSON":
                    resources.put(filename, jsonLoader.load(in));
                    break;
                case "png":
                case "jpg":
                    resources.put(filename, imageLoader.load(in));
                    break;
                default:
                    System.out.println("Type de fichier non pris en charge : " + fileExtension);
                    break;
            }
        }

        return resources;
    }

    @Override
    public void save(Map<String, Object> resources, ZipOutputStream out) throws IOException {
        for (Map.Entry<String, Object> entry : resources.entrySet()) {
            ZipEntry zipEntry = new ZipEntry(entry.getKey());
            out.putNextEntry(zipEntry);

            if (entry.getValue() instanceof String) { // si l'entrée contient un texte (fichier .txt)
                textLoader.save((String) entry.getValue(), out);
            }
            else if (entry.getValue() instanceof JSONObject) {
                jsonLoader.save((JSONObject) entry.getValue(), out);
            }
            else if (entry.getValue() instanceof EmptyImage) {
                imageLoader.save((AndroidImage) entry.getValue(), out);
            }
            else {
                System.out.println("Entrée fichier non-reconnue");
            }
            out.closeEntry();
        }
        out.close();
    }


    @Override
    public Object loadFile(String absolutePath) throws IOException, ParseException {
        FileInputStream fin = new FileInputStream(absolutePath);

        switch(ZipLoader.getExtension(absolutePath)){
            case "txt":
            case "TXT":
                return textLoader.load(fin);
            case "json":
            case "JSON":
                return jsonLoader.load(fin);
            case "png": // ne peut pas gérer des fichiers ".PNG"
            case "jpg" :
                return imageLoader.load(fin);
            default:
                return "Format non-lisible";
        }
    }
}
