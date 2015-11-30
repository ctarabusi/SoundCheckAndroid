package s2m.soundcheck.comparison.view;

import android.support.annotation.NonNull;

/**
 * Created by cta on 15/09/15.
 */
public interface ViewEventListener
{
    void viewVisible();

    void viewGone();

    void setOutput(@NonNull final UpdateViewInterface output);

    void startCheckSoundButtonClicked();

    void stopCheckSoundButtonClicked();
}
