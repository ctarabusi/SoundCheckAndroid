package s2m.soundcheck.waveformplotusecase.view;

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
public class WaveformPlotView extends View
{
    private static String TAG = WaveformPlotView.class.getSimpleName();

    private Paint   mGridPaint;
    private Paint   mTimecodePaint;
    private Short[] samplesList;

    int maxValue = 0;

    boolean paintGrid;
    boolean paintTime;
    double  valueNormalized;
    int     scaledSample;

    public WaveformPlotView(Context context)
    {
        super(context);
        initView();
    }

    public WaveformPlotView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        initView();
    }

    public WaveformPlotView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView()
    {
        mGridPaint = new Paint();
        mGridPaint.setAntiAlias(false);
        mGridPaint.setColor(Color.RED);

        mTimecodePaint = new Paint();
        mTimecodePaint.setAntiAlias(true);
        mTimecodePaint.setColor(Color.BLUE);
        mTimecodePaint.setTextSize(20f);
    }

    public void setSamples(Short[] samplesList)
    {
        this.samplesList = samplesList;

        for (short sample : samplesList)
        {
            if (maxValue < sample)
            {
                maxValue = sample;
            }
        }

        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);

        if (samplesList == null || samplesList.length == 0)
        {
            return;
        }

        int measuredWidth = getMeasuredWidth();
        int measuredHeight = getMeasuredHeight();
        int centerHeight = measuredHeight / 2;

        Log.d(TAG, "samplesList size " + samplesList.length);

        int i = 0;
        int previous = 0;
        int previousSample = 0;
        while (i < measuredWidth)
        {
            paintGrid = i % 100 == 0;
            if (paintGrid)
            {
                canvas.drawLine(i, 0, i, measuredHeight, mGridPaint);
            }

            paintTime = i % 200 == 0;
            if (paintTime)
            {
                canvas.drawText(String.valueOf(i), i + 20, measuredHeight - 20, mTimecodePaint);
            }

            valueNormalized = (double) samplesList[i] / maxValue;
            scaledSample = centerHeight - (int) (valueNormalized * centerHeight);

            canvas.drawLine(previous, previousSample, i, scaledSample, mGridPaint);

            previous = i;
            previousSample = scaledSample;
            i++;
        }
    }
}
