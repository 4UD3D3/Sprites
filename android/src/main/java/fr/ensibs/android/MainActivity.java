package fr.ensibs.android;

import android.annotation.SuppressLint;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.zip.ZipInputStream;

import fr.ensibs.util.graphic.Snapshot;
import fr.ensibs.util.io.JsonLoaderImpl;
import fr.ensibs.util.io.SnapshotConverter;
import fr.ensibs.util.io.TextLoaderImpl;
import fr.ensibs.util.io.ZipLoader;
import fr.ensibs.util.io.Loader;

/**
 * Displays content text of files which are loaded on the device
 *
 * @author Andréa Gainche
 */
public class MainActivity extends AppCompatActivity {
    /**
     * Name of the activity
     */
    private static final String TAG = "MainActivity";

    /**
     * Content of files which is displayed
     */
    private TextView messageView;

    private ViewImage viewImage;

    private Map<String, Object> resources;

    /**
     * Instanciates the class by displaying a text area
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewImage = new ViewImage(this); //set view image
        this.messageView = findViewById(R.id.messageView);
    }

    /**
     * Opens a JSON or Text file and displays its content
     *
     * @param view default parameter
     * @throws IOException    errors of file reading
     * @throws ParseException errors of JSON table reading
     */
    @SuppressLint("SetTextI18n")
    public void open(View view) throws IOException, ParseException, JSONException {
        resources = new HashMap<>();
        getResources().getIdentifier("examples", "raw", getPackageName());
        InputStream inputs = getResources().openRawResource(getResources().getIdentifier("examples",
                "raw", getPackageName()));
        ZipInputStream myZip = new ZipInputStream(inputs);

        Loader<String> textLoader = new TextLoaderImpl();
        Loader<JSONObject> jsonLoader = new JsonLoaderImpl();
        Loader<AndroidImage> imageLoader = new AndroidImageLoader();

        ZipLoader zip = new ZipLoaderImplAndroid(jsonLoader, textLoader, imageLoader);
        try {
            resources = zip.load(myZip);
            for (Map.Entry mapentry : resources.entrySet()) {
                if (mapentry.getKey().toString().equals("snapshot.json") && mapentry.getValue().getClass() == JSONObject.class) { // if load object is a JSONObject
                    Snapshot<AndroidImage> snapshot = getSnapshot("snapshot.json");
                    viewImage.setSnapshot(snapshot); //met a jour la version de snapshot de viewImage
                    setContentView(viewImage); // affiche viewImage
                }
            }
        } catch (IOException ex) {
            System.out.println(ex.toString());
        }
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
    public Snapshot<AndroidImage> getSnapshot(String filename) throws JSONException, ParseException, FileNotFoundException {
        JSONObject obj = (JSONObject) resources.get(filename);
        Map<String, AndroidImage> mapImage = new HashMap<>();

        assert obj != null;
        if (obj.has("layers")) {
            // Each layer in json are added to the list
            JSONArray jsonArray = obj.getJSONArray("layers");

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject objImg = jsonArray.getJSONObject(i);
                String objName = objImg.getString("image");

                if (!resources.containsKey(objName)) { // la map image ne peut pas être construite si les fichiers images n'ont pas été chargés en amont
                    throw new FileNotFoundException("Les fichiers image-source n'ont pas ete charges, impossible de construire les calques. Veuillez les ajouter a votre archive zip.");
                }
                if (Objects.requireNonNull(resources.get(objName)).getClass() == AndroidImage.class) {
                    mapImage.put(objName, (AndroidImage) resources.get(objName));
                } else {
                    throw new ClassCastException("Le fichier n'est pas a un format image compatible (.jpg ou .png)");
                }
            }
        }
        return new SnapshotConverter<>(mapImage).fromJson(obj);
    }
}