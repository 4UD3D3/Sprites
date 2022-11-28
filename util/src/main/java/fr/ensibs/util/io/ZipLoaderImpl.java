package fr.ensibs.util.io;

import java.io.*;

import org.json.JSONObject;

import java.lang.invoke.SwitchPoint;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import javax.xml.stream.events.Comment;

import fr.ensibs.util.graphic.Image;

/**
 * Class ZipLoader that read and write JSON and Text files into zip archive
 * Class that handle the load and save of zip file
 *
 * @author Grégoire, Nassim
 */
public class ZipLoaderImpl implements ZipLoader {
    /**
       The json loader to load and save json file
     */
    private Loader<JSONObject> jsonLoader;

    /**
       The text loader to load and save text file
     */
    private Loader<String> textLoader;

    /**
       The image loader to load and save image
     */
    private Loader<Image> imageLoader;

    /**
     * Constructor of ZipLoaderImpl class
     *
     * @param jsonLoader  allows zipping JSON files
     * @param textLoader  allows zipping .txt files
     * @param imageLoader allows zipping images.
     */
    public ZipLoaderImpl(Loader<JSONObject> jsonLoader, Loader<String> textLoader, Loader<Image> imageLoader) {
        this.jsonLoader = jsonLoader;
        this.textLoader = textLoader;
        this.imageLoader = imageLoader;
    }

    /**
     * Secondary constructor to only load/save json or text file
     *
     * @param jsonLoader allows zipping JSON files
     * @param textLoader allows zipping .txt files
     */
    public ZipLoaderImpl(Loader<JSONObject> jsonLoader, Loader<String> textLoader) {
        this.jsonLoader = jsonLoader;
        this.textLoader = textLoader;
        this.imageLoader = null;
    }

    /**
     * Extracts JSON, text files and images from an archive into a Map
     *
     * @param in the ZipOutputStream used to read into
     * @return a map containing entries (with name of file as key, and content as value)
     */
    @Override
    public Map<String, Object> load(ZipInputStream in) throws IOException, ParseException {
        // Map and entry initialisation
        Map<String, Object> resources = new HashMap<>();

        ZipEntry entry;

        while((entry = in.getNextEntry()) != null && !entry.isDirectory()){
            String filename = entry.getName();
            String fileExtension = ZipLoader.getExtension(filename);

            // Load it in the map
            switch (fileExtension){
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

    /**
     * Permet l'affichage du contenu d'un fichier
     *
     * @param filename fichier au format texte, json ou image
     * @return le contenu du fichier retranscrit par son loader respectif
     */
    public Object loadFile(String filename) throws IOException, ParseException {
        FileInputStream fin = new FileInputStream(filename);

        switch(ZipLoader.getExtension(filename)){
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

    /**
     * Adds JSON, text files and images from a Map into a ZipOutputStream
     *
     * @param resources the map in which files are located
     * @param out the ZipOutputStream used to write into
     */
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
                System.out.println(entry.getValue());
                imageLoader.save((Image) entry.getValue(), out);
            }
            else {
                System.out.println("Entrée fichier non-reconnue");
            }
            out.closeEntry();
        }
        out.close();
    }
}
