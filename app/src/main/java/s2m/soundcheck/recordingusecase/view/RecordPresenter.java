package s2m.soundcheck.recordingusecase.view;

import android.support.annotation.NonNull;
import android.util.Log;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;

import javax.inject.Inject;

import s2m.soundcheck.R;
import s2m.soundcheck.recordingusecase.interactor.DataChangeListener;
import s2m.soundcheck.recordingusecase.interactor.RecordInteractor;
import s2m.soundcheck.utils.Helper;

/**
 * Created by cta on 17/09/15.
 */
public class RecordPresenter implements ViewEventListener, DataChangeListener
{
    private UpdateViewInterface output;

    private RecordInteractor interactor;

    private boolean isRecording;

    @Inject
    public RecordPresenter(RecordInteractor interactor)
    {
        this.interactor = interactor;

        this.interactor.setOutput(this);
    }

    @Override
    public void viewVisible()
    {
    //    interactor.initRecorder();
    }

    @Override
    public void viewGone()
    {
    //    interactor.releaseRecorder();
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
            interactor.startRecording();

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
            interactor.stopRecording();

            isRecording = false;
            output.stopChronometer();
            output.showRecordingSnackbar(R.string.record_stop);
        }
    }

    @Override
    public void exceptionFromInteractor()
    {
        output.showRecordingSnackbar(R.string.record_error);
    }
}
