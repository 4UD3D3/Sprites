package fr.ensibs.android;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.ImageView;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.ParseException;

import fr.ensibs.util.graphic.Image;
import fr.ensibs.util.io.Loader;

/**
 * represented a loader of image for android
 *
 * @author Andréa Gainche
 */

public class AndroidImageLoader implements Loader<AndroidImage> {
    @Override
    public AndroidImage load(InputStream in) throws IOException, ParseException {
        return new AndroidImage(BitmapFactory.decodeStream(in));
    }

    @Override
    public void save(AndroidImage obj, OutputStream out) throws IOException {

    }
}
