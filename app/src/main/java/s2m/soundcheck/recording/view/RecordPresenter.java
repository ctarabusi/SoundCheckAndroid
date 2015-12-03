package s2m.soundcheck.recording.view;

import android.content.Context;
import android.support.annotation.NonNull;

import javax.inject.Inject;

import s2m.soundcheck.R;
import s2m.soundcheck.recording.interactor.RecordInteractor;

/**
 * Created by cta on 17/09/15.
 */
public class RecordPresenter implements ViewEventListener
{
    private UpdateViewInterface output;

    private RecordInteractor recordInteractor;

    private boolean isRecording;

    @Inject
    public RecordPresenter(Context applicationContext)
    {
        recordInteractor = new RecordInteractor(applicationContext);
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
    public void startRecordingButtonClicked()
    {
        if (!isRecording)
        {
            recordInteractor.startRecording();

            isRecording = true;

            output.startChronometer();
            output.showRecordingSnackbar(R.string.record_start);
        }
    }

    @Override
    public void stopRecordingButtonClicked()
    {
        if (isRecording)
        {
            recordInteractor.stopRecording();

            isRecording = false;
            output.stopChronometer();
            output.showRecordingSnackbar(R.string.record_stop);
        }
    }
}
