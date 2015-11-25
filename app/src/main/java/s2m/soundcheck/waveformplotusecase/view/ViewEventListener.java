package s2m.soundcheck.waveformplotusecase.view;

import android.app.Activity;
import android.support.annotation.NonNull;

/**
 * Created by cta on 18/09/15.
 */
public interface ViewEventListener
{

    void setWaveformPlot(@NonNull WaveformPlotView waveformPlotView);

    void viewVisible(@NonNull Activity activity);

    void viewGone();
}
