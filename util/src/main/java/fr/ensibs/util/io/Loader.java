package fr.ensibs.util.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.ParseException;

/**
 * Interface used to load/save objects from/to input streams.
 * @param <T> is the object type given during implementation to be loaded/saved
 *
 * @author Grégoire Truhé
 */
public interface Loader<T>
{
    /**
     * Read an object from an input stream.
     *
     * @param in input stream
     * @return the instance read from the input stream
     * @throws IOException    if an error occurs while reading from the input stream
     * @throws ParseException if an error occurs while making the object
     * @pre {@code in != null}
     * @post {@code result != null}
     */
    T load(InputStream in) throws IOException, ParseException;

    /**
     * Save an object to an output stream
     *
     * @param obj the instance to be written to the output stream
     * @param out the output stream
     * @throws IOException if an error occurs while writing to the output stream
     * @pre {@code obj != null}
     * @pre {@code out != null}
     */
    void save(T obj, OutputStream out) throws IOException;
}
