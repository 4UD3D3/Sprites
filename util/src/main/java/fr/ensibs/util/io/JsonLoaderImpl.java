package fr.ensibs.util.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.text.ParseException;

import org.json.JSONObject;

/**
 * Class that implements loader for JsonObject
 * @author Andréa Gainche
 */
public class JsonLoaderImpl implements Loader<JSONObject> {
    /**
     * Load a JSONObject from an input stream
     *
     * @author Andréa GAINCHE
     * @param in input stream of the stream that we wanted to load
     * @return a jsonObject of the input stream
     * @pre {@code in != null}
     * @post {@code result != null}
     */
        
    @Override
    public JSONObject load(InputStream in) throws IOException, ParseException {
        try {
            BufferedReader streamReader = new BufferedReader(new InputStreamReader(in, "UTF-8")); //transforme en buffer reader
            StringBuilder responseStrBuilder = new StringBuilder();
            String inputStr;
            while ((inputStr = streamReader.readLine()) != null) // parcourt du buffer reader
                responseStrBuilder.append(inputStr);
            return new JSONObject(responseStrBuilder.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Save a JSONObject to an output stream
     *
     * @param obj the instance to be written to the output stream
     * @param out the output stream
     * @pre {@code obj && out != null}
     */

    @Override
    public void save(JSONObject obj, OutputStream out) throws IOException {
        try {
            out.write(obj.toString().getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
