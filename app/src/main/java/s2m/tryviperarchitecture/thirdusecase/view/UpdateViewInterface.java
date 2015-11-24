package s2m.tryviperarchitecture.thirdusecase.view;

import android.support.annotation.StringRes;

/**
 * Created by cta on 15/09/15.
 */
public interface UpdateViewInterface
{
     void showRecordingSnackbar(@StringRes int snackbarTextId);

     void startChronometer();

     void stopChronometer();
}
