package s2m.tryviperarchitecture.waveformplotusecase.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by cta on 23/11/15.
 */
public class WaveformPlotView extends View
{

    private Paint mGridPaint;
    private Paint mTimecodePaint;
    private float mDensity;

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

    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);

        int measuredWidth = getMeasuredWidth();
        int measuredHeight = getMeasuredHeight();

        // Draw grid
        int i = 0;
        while (i < measuredWidth)
        {
            boolean paintGrid = i % 100 == 0;
            if (paintGrid)
            {
                canvas.drawLine(i, 0, i, measuredHeight, mGridPaint);
            }

            boolean paintTime = i % 200 == 0;
            if (paintTime)
            {
                canvas.drawText(String.valueOf(i), i + 20, measuredHeight - 20, mTimecodePaint);
            }
            i++;
        }
    }
}
