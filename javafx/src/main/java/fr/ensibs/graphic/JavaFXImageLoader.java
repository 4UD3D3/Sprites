package fr.ensibs.graphic;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.imageio.ImageIO;

import fr.ensibs.util.io.Loader;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;

/**
 * Class to load/save javafx Image
 *
 * @author Grégoire Truhé
 */
public class JavaFXImageLoader implements Loader<fr.ensibs.util.graphic.Image> {

    /**
     * Get an image from stream
     * @param in input stream
     * @return Image loaded from stream
     */
    @Override
    public fr.ensibs.util.graphic.Image load(InputStream in){
        return new JavaFXImage(new Image(in));
    }

    /**
     * Save an image
     * @param img the image to be written to the output stream
     * @param out the output stream
     * @throws IOException erreurs de lecture d'image
     */
    @Override
    public void save(fr.ensibs.util.graphic.Image img, OutputStream out) throws IOException {
        var fximg = (JavaFXImage) img;
        var bufferImage = SwingFXUtils.fromFXImage(fximg.getImage(), null);
        ImageIO.write(bufferImage, "PNG", out);
    }
}