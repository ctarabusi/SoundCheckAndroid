package s2m.soundcheck.spectogram.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by cta on 23/11/15.
 */
public class SpectrogramView extends View
{
    private static String TAG = SpectrogramView.class.getSimpleName();

    private Paint      mGridPaint;
    private double[][] samplesList;


    public SpectrogramView(Context context)
    {
        super(context);
        initView();
    }

    public SpectrogramView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        initView();
    }

    public SpectrogramView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView()
    {
        mGridPaint = new Paint();
        mGridPaint.setAntiAlias(false);
        mGridPaint.setColor(Color.RED);
    }

    public void setSamples(double[][] samplesList)
    {
        this.samplesList = samplesList;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);

        if (samplesList == null)
        {
            return;
        }

        Log.d(TAG, "samplesList size " + samplesList.length);

        short blockSizeX = 20;
        short blockSizeY = 20;
        double magnitude;

        double maxMagnitude = 0;
        for (int i = 0; i < samplesList.length; i++)
        {
            int freq = 0;
            int size = samplesList[i].length - 1;
            for (int line = 1; line < size; line++)
            {
                // To get the magnitude of the sound at a given frequency slice
                // get the abs() from the complex number.
                // In this case I use Math.log to get a more managable number (used for color)
                magnitude = samplesList[i][freq];
                if (maxMagnitude < magnitude)
                {
                    maxMagnitude = magnitude;
                }

                freq++;
            }
        }
        Log.d(TAG, "magnitude max is " + maxMagnitude);

        for (int i = 0; i < samplesList.length; i++)
        {
            int freq = 0;
            int size = samplesList[i].length - 1;
            for (int line = 1; line < size; line++)
            {
                // To get the magnitude of the sound at a given frequency slice
                // get the abs() from the complex number.
                // In this case I use Math.log to get a more managable number (used for color)
                magnitude = samplesList[i][freq] / maxMagnitude;

                // The more blue in the color the more intensity for a given frequency point:
                mGridPaint.setColor(Color.rgb(0, (int) (magnitude * 255 * 100), (int) (magnitude * 255 * 100)));

                canvas.drawRect(i * blockSizeX, (size - line) * blockSizeY, (i + 1) * blockSizeX, (size - line) * blockSizeY + blockSizeY, mGridPaint);

                freq++;
            }
        }
    }
}
