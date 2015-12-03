package s2m.soundcheck.spectogram.view;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.util.Log;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.util.Arrays;

import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.schedulers.Schedulers;
import s2m.soundcheck.ViperApplication;
import s2m.soundcheck.utils.Helper;

/**
 * Created by cta on 18/09/15.
 */
public class SpectrogramPresenter implements ViewEventListener
{
    private static String TAG = SpectrogramPresenter.class.getSimpleName();

    public static final String SERVER_URL = ViperApplication.BASE_SERVER_URL + "fourier-transform/spectrogram";

    private SpectrogramView spectrogramView;

    private Subscription readFileSubscription;

    @Override
    public void viewVisible(@NonNull final Activity activity)
    {
        readFileSubscription = Observable.just(Helper.readAsset()).subscribeOn(Schedulers.io()).observeOn(Schedulers.io()).subscribe(new Observer<byte[]>()
        {
            double[][] outputSpectrogram = new double[0][0];

            @Override
            public void onCompleted()
            {
                activity.runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        spectrogramView.setSamples(outputSpectrogram);
                    }
                });
            }

            @Override
            public void onError(Throwable e)
            {
                Log.e(TAG, e.getMessage());
            }

            @Override
            public void onNext(byte[] bytesRead)
            {
                outputSpectrogram = requestSpectrogram(Arrays.copyOfRange(bytesRead, 44, bytesRead.length));
            }
        });
    }

    @Override
    public void viewGone()
    {
        readFileSubscription.unsubscribe();
    }

    @Override
    public void setSpectrogramView(@NonNull SpectrogramView spectrogramView)
    {
        this.spectrogramView = spectrogramView;
    }

    public static double[][] requestSpectrogram(byte[] sampleArray)
    {
        double[][] outputFFT = null;
        try
        {
            HttpURLConnection connection = Helper.buildURLConnection(SERVER_URL);
            OutputStream output = null;
            try
            {
                output = connection.getOutputStream();
                output.write(sampleArray);
            }
            catch (Exception e)
            {
                Log.e(TAG, e.getMessage(), e);
            }
            finally
            {
                if (output != null) try
                {
                    output.close();
                }
                catch (IOException e)
                {
                    Log.e(TAG, e.getMessage(), e);
                }
            }

            int status = connection.getResponseCode();
            Log.d(TAG, "Status : " + status);

            ObjectInputStream in = new ObjectInputStream(connection.getInputStream());

            outputFFT = (double[][]) in.readObject();

            in.close();

            connection.disconnect();
        }
        catch (IOException e)
        {
            Log.e(TAG, e.getMessage(), e);
        }
        catch (ClassNotFoundException e)
        {
            e.printStackTrace();
        }

        return outputFFT;
    }


}
