package s2m.soundcheck.frequencyplotusecase.view;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.schedulers.Schedulers;
import s2m.soundcheck.utils.FileUtils;

/**
 * Created by cta on 18/09/15.
 */
public class FrequencyPresenter implements ViewEventListener
{
    public static final String SERVER_URL = "http://192.168.178.15:8080/fourier-transform/fft";

    private static String TAG = FrequencyPresenter.class.getSimpleName();

    private FrequencyPlotView frequencyPlotView;

    private Subscription readFileSubscription;

    @Override
    public void viewVisible(@NonNull final Activity activity)
    {
        readFileSubscription = Observable.just(FileUtils.readAsset(activity)).subscribeOn(Schedulers.io()).observeOn(Schedulers.io()).subscribe(new Observer<byte[]>()
        {
            List<Double> outputFFT = new ArrayList<>();

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
                outputFFT = FrequencyPresenter.requestFFT(sampleArray);
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

    public static List<Double> requestFFT(byte[] sampleArray)
    {
        List<Double> outputFFT = new ArrayList<>();
        try
        {
            System.setProperty("http.keepAlive", "false");
            URL url = new URL(SERVER_URL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Connection", "Keep-Alive");
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;");
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

            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            String inputLine;
            while ((inputLine = in.readLine()) != null)
            {
                outputFFT.add(Double.parseDouble(inputLine));
            }
            in.close();

            connection.disconnect();
        }
        catch (IOException e)
        {
            Log.e(TAG, e.getMessage(), e);
        }
        return outputFFT;
    }
}
