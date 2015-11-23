package s2m.tryviperarchitecture.waveformplotusecase.view;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.view.ViewGroup;

import javax.inject.Inject;

import s2m.tryviperarchitecture.R;

/**
 * Created by cta on 18/09/15.
 */
public class WaveformPresenter implements ViewEventListener
{
    private WaveformPlotView waveformPlotView;

    public WaveformPresenter()
    {

    }

    @Override
    public void viewVisible(@NonNull Activity activity)
    {

    }

    @Override
    public void viewGone()
    {

    }

    @Override
    public void setWaveformPlot(@NonNull WaveformPlotView waveformPlotView)
    {
        this.waveformPlotView = waveformPlotView;
    }
}
