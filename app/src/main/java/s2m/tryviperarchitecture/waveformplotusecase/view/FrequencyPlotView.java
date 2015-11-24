package s2m.tryviperarchitecture.waveformplotusecase.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import s2m.tryviperarchitecture.waveformplotusecase.interactor.Complex;

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
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);

        int measuredWidth = getMeasuredWidth();
        int measuredHeight = getMeasuredHeight();
        int centerHeight = measuredHeight / 2;

        Log.d(TAG, "samplesList size " + samplesList.length);

        // Finding max value
        int maxValue = 0;
        for (int i = 0; i < samplesList.length; i++)
        {
            Complex sample = samplesList[i];
            double magnitude = sample.abs();
            if (maxValue < magnitude)
            {
                maxValue = (int) magnitude;
            }
        }

        for (int i = 0; i < samplesList.length; i++)
        {
            Complex sample = samplesList[i];

            // To get the magnitude of the sound at a given frequency slice
            // get the abs() from the complex number.
            double magnitude = sample.abs();

            // The more blue in the color the more intensity for a given frequency point:
           // mGridPaint.setColor(Color.argb(1, 0, (int) magnitude * 10, (int) magnitude * 20));

            float valueNormalized = (float) (magnitude / maxValue * measuredHeight);
            canvas.drawLine(i, measuredHeight - valueNormalized, i, measuredHeight, mGridPaint);
        }
    }
}
