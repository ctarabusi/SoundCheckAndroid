package s2m.soundcheck.frequencyplotusecase.view;

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
public class FrequencyPlotView extends View
{
    private static String TAG = FrequencyPlotView.class.getSimpleName();

    private Paint    mFrequencyPaint;
    private double[] samplesList;

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
        mFrequencyPaint = new Paint();
        mFrequencyPaint.setAntiAlias(false);
        mFrequencyPaint.setColor(Color.RED);
    }

    public void setSamples(double[] samplesList)
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

        if (samplesList == null || samplesList.length == 0)
        {
            return;
        }

        Log.d(TAG, "samplesList size " + samplesList.length);

        // Finding max value
        double maxValue = 0;
        int index = 0;
        while (index < measuredWidth)
        {
            float progress = (float) index / measuredWidth;
            int normalizeTimeAxis = (int) (progress * samplesList.length);

            double partialValue = samplesList[normalizeTimeAxis];
            if (maxValue < partialValue)
            {
                maxValue = partialValue;
            }
            index++;
        }

        index = 0;
        while (index < measuredWidth)
        {
            float progress = (float) index / measuredWidth;
            int normalizeTimeAxis = (int) (progress * samplesList.length);

            double partialValue = samplesList[normalizeTimeAxis]  * measuredHeight;
            canvas.drawLine(index, measuredHeight - (float) (partialValue  / maxValue ), index, measuredHeight, mFrequencyPaint);
            index++;
        }
    }
}
