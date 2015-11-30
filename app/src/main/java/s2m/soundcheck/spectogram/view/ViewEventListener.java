package s2m.soundcheck.spectogram.view;

import android.app.Activity;
import android.support.annotation.NonNull;

/**
 * Created by cta on 18/09/15.
 */
public interface ViewEventListener
{
    void setSpectrogramView(@NonNull SpectrogramView spectrogramView);

    void viewVisible(@NonNull Activity activity);

    void viewGone();
}
