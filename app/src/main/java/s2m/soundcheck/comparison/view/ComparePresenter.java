package s2m.soundcheck.comparison.view;

import android.content.Context;
import android.support.annotation.NonNull;

import javax.inject.Inject;

import s2m.soundcheck.R;
import s2m.soundcheck.comparison.interactor.CheckSoundInteractor;
import s2m.soundcheck.comparison.interactor.DataChangeListener;

/**
 * Created by cta on 17/09/15.
 */
public class ComparePresenter implements ViewEventListener, DataChangeListener
{
    private UpdateViewInterface output;

    private CheckSoundInteractor checkSoundInteractor;

    private boolean isRecording;

    @Inject
    public ComparePresenter(Context applicationContext)
    {
        checkSoundInteractor = new CheckSoundInteractor(applicationContext);

        this.checkSoundInteractor.setOutput(this);
    }

    @Override
    public void viewVisible()
    {
    }

    @Override
    public void viewGone()
    {
    }

    @Override
    public void setOutput(@NonNull UpdateViewInterface output)
    {
        this.output = output;
    }

    @Override
    public void startCheckSoundButtonClicked()
    {
        if (!isRecording)
        {
            checkSoundInteractor.startRecording();

            isRecording = true;

            output.startChronometer();
            output.showRecordingSnackbar(R.string.record_start);
        }
    }

    @Override
    public void stopCheckSoundButtonClicked()
    {
        if (isRecording)
        {
            checkSoundInteractor.stopRecording();

            isRecording = false;

            output.stopChronometer();
            output.showRecordingSnackbar(R.string.record_stop);
        }
    }

    @Override
    public void showReturnedValue(String value)
    {
        output.showReturnedValue(value);
    }
}
