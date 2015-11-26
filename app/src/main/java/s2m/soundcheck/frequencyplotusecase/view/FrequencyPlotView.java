package s2m.soundcheck.frequencyplotusecase.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.List;

/**
 * Created by cta on 23/11/15.
 */
public class FrequencyPlotView extends View
{
    private static String TAG = FrequencyPlotView.class.getSimpleName();

    private Paint        mFrequencyPaint;
    private List<Double> samplesList;

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

    public void setSamples(List<Double> samplesList)
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

        if (samplesList == null)
        {
            return;
        }

        Log.d(TAG, "samplesList size " + samplesList.size());

        // Finding max value
        double maxValue = 0;
        for (double sample : samplesList)
        {
            if (maxValue < sample)
            {
                maxValue = sample;
            }
        }

        int i = 0;
        while (i < measuredWidth)
        {
            float progress = (float) i / measuredWidth;
            int normalizeTimeAxis = (int) (progress * samplesList.size());
            
            canvas.drawLine(i, measuredHeight - (float) (samplesList.get(normalizeTimeAxis) / maxValue * measuredHeight), i, measuredHeight, mFrequencyPaint);
            i++;
        }
    }
}
