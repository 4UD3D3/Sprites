package fr.ensibs.util.io;

import org.json.JSONObject;

import java.text.ParseException;

/**
 * Interface to convert Java objects to JSON objects and vice versa.
 *
 * @param <T> the type of Java objects converted from/to JSON objects
 * @author Pascale Launay
 */
public interface JsonConverter<T>
{
    /**
     * Give a Java object from a JSON object.
     *
     * @param obj a JSON object
     * @return a Java object
     * @throws ParseException if the given JSON object format doesn't match the generic type
     * @pre {@code obj != null}
     * @post {@code result != null}
     */
    T fromJson(JSONObject obj) throws ParseException;

    /**
     * Give a JSON object from a Java object.
     *
     * @param obj a Java object
     * @return a JSON object
     * @pre {@code obj != null}
     * @post {@code result != null}
     */
    JSONObject toJson(T obj);
}