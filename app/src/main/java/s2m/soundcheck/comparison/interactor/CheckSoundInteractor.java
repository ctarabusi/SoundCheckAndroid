package s2m.soundcheck.comparison.interactor;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;

import javax.inject.Inject;

import rx.Observable;
import rx.Observer;
import rx.schedulers.Schedulers;
import s2m.soundcheck.ViperApplication;
import s2m.soundcheck.recording.interactor.RecordInteractor;
import s2m.soundcheck.utils.Helper;

/**
 * Created by cta on 17/09/15.
 */
public class CheckSoundInteractor extends RecordInteractor
{
    private static final String TAG = CheckSoundInteractor.class.getSimpleName();

    public static final String SERVER_URL = ViperApplication.BASE_SERVER_URL + "fourier-transform/checksound";

    private DataChangeListener dataChangeListener;

    @Inject
    public CheckSoundInteractor(Context applicationContext)
    {
        super(applicationContext);
        this.applicationContext = applicationContext;
    }

    public void setOutput(DataChangeListener dataChangeListener)
    {
        this.dataChangeListener = dataChangeListener;
    }

    @Override
    protected File getFilename()
    {

        return new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), RECORDED_FILE_NAME);
    }

    @Override
    protected File getTempFilename()
    {
        return new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), RECORDED_TEMP_FILE_NAME);
    }

    public void stopRecording()
    {
        if (null != recorder)
        {
            isRecording = false;

            int i = recorder.getState();
            if (i == 1) recorder.stop();
            recorder.release();

            recorder = null;
            recordingThread = null;
        }

        Observable.empty().subscribeOn(Schedulers.io()).observeOn(Schedulers.io()).subscribe(new Observer()
        {

            @Override
            public void onCompleted()
            {
                writeOutputStream(getTempFilename());
                deleteTempFile();
            }

            @Override
            public void onError(Throwable e)
            {

            }

            @Override
            public void onNext(Object o)
            {

            }
        });
    }

    private void writeOutputStream(File inFilename)
    {
        FileInputStream in = null;
        OutputStream outputStream = null;

        byte[] buffer = new byte[bufferSize];

        try
        {
            in = new FileInputStream(inFilename);

            HttpURLConnection connection = Helper.buildURLConnection(SERVER_URL);

            outputStream = connection.getOutputStream();
            for (int length = in.read(buffer); length != -1; length = in.read(buffer))
            {
                outputStream.write(buffer);
            }
            outputStream.flush();

            int status = connection.getResponseCode();
            Log.d(TAG, "Status : " + status);

            in.close();

            ObjectInputStream objectInputStream = new ObjectInputStream(connection.getInputStream());

            String outputCompare = (String) objectInputStream.readObject();
            objectInputStream.close();

            Log.d(TAG, "outputCompare : " + outputCompare);

            dataChangeListener.showReturnedValue(outputCompare);

            connection.disconnect();
        }
        catch (IOException e)
        {
            Log.e(TAG, e.getMessage(), e);
        }
        catch (ClassNotFoundException e)
        {
            Log.e(TAG, e.getMessage(), e);
        }
        finally
        {
            try
            {
                if (in != null)
                {
                    in.close();
                }
                if (outputStream != null)
                {
                    outputStream.close();
                }
            }
            catch (IOException e)
            {
                Log.e(TAG, e.getMessage(), e);
            }
        }
    }
}
