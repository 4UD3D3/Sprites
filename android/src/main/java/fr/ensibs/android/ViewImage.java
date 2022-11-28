package fr.ensibs.android;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import fr.ensibs.util.graphic.Snapshot;
import fr.ensibs.util.graphic.SnapshotLayer;

/**
 * @author Andréa Gainche
 */

public class ViewImage extends View {

    private Snapshot<AndroidImage> snapshot;

    public ViewImage(Context context) {
        super(context);
    }

    /**
     *
     * @param canvas : canvas to display image
     */

    @Override
    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);
        for (SnapshotLayer<AndroidImage> snapshotLayer : snapshot) {
            canvas.drawBitmap(snapshotLayer.getImage().getBitmap(), snapshotLayer.getX(), snapshotLayer.getY(), null);
        }
    }

    /**
     *
     * @param snapshot : snapshot of android image to be displayed
     */

    public void setSnapshot(Snapshot<AndroidImage> snapshot) {
        this.snapshot = snapshot;
    }
}
