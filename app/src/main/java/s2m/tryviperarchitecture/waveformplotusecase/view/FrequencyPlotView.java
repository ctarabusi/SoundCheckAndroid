package s2m.tryviperarchitecture.waveformplotusecase.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import org.apache.commons.math.complex.Complex;

/**
 * Created by cta on 23/11/15.
 */
public class FrequencyPlotView extends View
{
    private static String TAG = FrequencyPlotView.class.getSimpleName();

    private Paint     mGridPaint;
    private Paint     mTimecodePaint;
    private Complex[] samplesList;


    public FrequencyPlotView(Context context)
    {
        super(context);
        initView();
    }

    public FrequencyPlotView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        initView();
    }

    public FrequencyPlotView(Context context, AttributeSet attrs, int defStyleAttr)
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

    public void setSamples(Complex[] samplesList)
    {
        this.samplesList = samplesList;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);

        int measuredWidth = getMeasuredWidth();
        int measuredHeight = getMeasuredHeight();
        int centerHeight = measuredHeight / 2;

        if (samplesList == null)
        {
            return;
        }

        Log.d(TAG, "samplesList size " + samplesList.length);

        // Finding max value
        double maxValue = 64000;
        for (Complex sample : samplesList)
        {
            double magnitude = sample.abs();
            if (maxValue < magnitude)
            {
                maxValue = magnitude;
            }
        }

        Log.d(TAG, "maxValue " + maxValue);

        for (int i = 0; i < samplesList.length; i++)
        {
            Complex sample = samplesList[i];

            double magnitude = sample.abs();

            float valueNormalized = (float) (magnitude / maxValue * measuredHeight);
            canvas.drawLine(i, measuredHeight - valueNormalized, i, measuredHeight, mGridPaint);
        }
    }
}
