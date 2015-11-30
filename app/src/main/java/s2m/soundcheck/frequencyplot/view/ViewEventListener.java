package s2m.soundcheck.frequencyplot.view;

import android.app.Activity;
import android.support.annotation.NonNull;

/**
 * Created by cta on 18/09/15.
 */
public interface ViewEventListener
{

    void setFrequencyPlot(@NonNull FrequencyPlotView frequencyPlotView);

    void viewVisible(@NonNull Activity activity);

    void viewGone();
}
