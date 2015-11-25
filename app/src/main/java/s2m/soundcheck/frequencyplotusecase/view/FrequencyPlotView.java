package s2m.soundcheck.frequencyplotusecase.view;

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

        int measuredHeight = getMeasuredHeight();

        if (samplesList == null)
        {
            return;
        }

        Log.d(TAG, "samplesList size " + samplesList.length);

        // Finding max value
        double maxValue = 0;
        for (Complex sample : samplesList)
        {
            if (maxValue < sample.abs())
            {
                maxValue = sample.abs();
            }
        }

        for (int i = 0; i < samplesList.length; i++)
        {
            canvas.drawLine(i, measuredHeight - (float) (samplesList[i].abs() / maxValue * measuredHeight), i, measuredHeight, mGridPaint);
        }
    }
}
