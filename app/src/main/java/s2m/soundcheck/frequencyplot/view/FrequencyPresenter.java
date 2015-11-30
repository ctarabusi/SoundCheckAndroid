package s2m.soundcheck.frequencyplot.view;

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
import s2m.soundcheck.utils.Helper;

/**
 * Created by cta on 18/09/15.
 */
public class FrequencyPresenter implements ViewEventListener
{
    private static String TAG = FrequencyPresenter.class.getSimpleName();

    public static final String SERVER_URL = "http://192.168.178.15:8080/fourier-transform/fft";

    private FrequencyPlotView frequencyPlotView;

    private Subscription readFileSubscription;

    @Override
    public void viewVisible(@NonNull final Activity activity)
    {
        readFileSubscription = Observable.just(Helper.readAsset(activity)).subscribeOn(Schedulers.io()).observeOn(Schedulers.io()).subscribe(new Observer<byte[]>()
        {
            double[] outputFFT = null;

            @Override
            public void onCompleted()
            {
                activity.runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        frequencyPlotView.setSamples(outputFFT);
                    }
                });
            }

            @Override
            public void onError(Throwable e)
            {
                Log.e(TAG, e.getMessage(), e);
            }

            @Override
            public void onNext(byte[] sampleArray)
            {
                outputFFT = requestFFT(Arrays.copyOfRange(sampleArray, 44, sampleArray.length));
            }
        });
    }

    @Override
    public void viewGone()
    {
        readFileSubscription.unsubscribe();
    }

    @Override
    public void setFrequencyPlot(@NonNull FrequencyPlotView frequencyPlotView)
    {
        this.frequencyPlotView = frequencyPlotView;
    }

    public static double[] requestFFT(byte[] sampleArray)
    {
        double[] outputFFT = null;
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

            outputFFT = (double[]) in.readObject();

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
