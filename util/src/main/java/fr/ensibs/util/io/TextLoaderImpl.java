package fr.ensibs.util.io;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Class used to load/save text from/to input streams.
 * @author Mehdi
 */

public class TextLoaderImpl implements Loader<String>{
    /**
     * Read a text from an input stream.
     *
     * @param in input stream
     * @return the text read from the input stream
     * @throws IOException if an error occurs while reading from the input stream
     * @pre {@code in != null}
     * @post {@code result != null}
     */
    @Override
    public String load(InputStream in) throws IOException {

        ByteArrayOutputStream toRead = new ByteArrayOutputStream();  //create our array that will contain all bytes of the input stream
        byte[] memoire = new byte[1024];
        for (int length; (length = in.read(memoire)) != -1; ) {
            toRead.write(memoire, 0, length);     //increment the array with all the byte of the input stream
        }
        return toRead.toString("UTF-8"); //we convert the byte into string, UTF-8 is the perfect charset according to Google

    }

    ;

    /**
     * Save a text to an output stream
     *
     * @param text the text to be written to the output stream
     * @param out  the output stream
     * @throws IOException if an error occurs while writing to the output stream
     * @pre {@code text != null}
     * @pre {@code out != null}
     */
    @Override
    public void save(String text, OutputStream out) throws IOException {
        out.write(text.getBytes("UTF-8"));    //we write into the output the byte value of all the character in the text
    };
}
