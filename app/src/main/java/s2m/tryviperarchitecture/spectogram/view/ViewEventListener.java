package s2m.tryviperarchitecture.spectogram.view;

import android.app.Activity;
import android.support.annotation.NonNull;

import s2m.tryviperarchitecture.waveformplotusecase.view.*;
import s2m.tryviperarchitecture.waveformplotusecase.view.FrequencyPlotView;

/**
 * Created by cta on 18/09/15.
 */
public interface ViewEventListener
{

    void setWaveformPlot(@NonNull s2m.tryviperarchitecture.waveformplotusecase.view.WaveformPlotView waveformPlotView);

    void setSpectrogramView(@NonNull SpectrogramView spectrogramView);

    void viewVisible(@NonNull Activity activity);

    void viewGone();
}
