package s2m.tryviperarchitecture.waveformplotusecase.view;

import android.app.Activity;
import android.support.annotation.NonNull;

/**
 * Created by cta on 18/09/15.
 */
public interface ViewEventListener
{

    void setWaveformPlot(@NonNull WaveformPlotView waveformPlotView);

    void setFrequencyPlot(@NonNull FrequencyPlotView frequencyPlotView);

    void viewVisible(@NonNull Activity activity);

    void viewGone();
}
