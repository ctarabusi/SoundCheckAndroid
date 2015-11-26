package s2m.soundcheck.spectogramusecase.view;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.Arrays;
import java.util.List;

import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.schedulers.Schedulers;
import s2m.soundcheck.frequencyplotusecase.view.FrequencyPresenter;
import s2m.soundcheck.utils.FileUtils;

/**
 * Created by cta on 18/09/15.
 */
public class SpectrogramPresenter implements ViewEventListener
{
    private static String TAG = SpectrogramPresenter.class.getSimpleName();

    private static int CHUNK_SIZE = 4096;

    private SpectrogramView spectrogramView;

    private Subscription readFileSubscription;

    @Override
    public void viewVisible(@NonNull final Activity activity)
    {
        readFileSubscription = Observable.just(FileUtils.readAsset(activity)).subscribeOn(Schedulers.io()).observeOn(Schedulers.io()).subscribe(new Observer<byte[]>()
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
                final int totalSize = bytesRead.length;

                int amountPossible = totalSize / CHUNK_SIZE;

                outputSpectrogram = new double[amountPossible][];

                for (int times = 0; times < amountPossible; times++)
                {
                    int baseIndex = times * CHUNK_SIZE;
                    List<Double> outputFFT = FrequencyPresenter.requestFFT(Arrays.copyOfRange(bytesRead, baseIndex, baseIndex + CHUNK_SIZE));

                    outputSpectrogram[times] = convertDoubles(outputFFT);
                }
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

    public static double[] convertDoubles(List<Double> doubles)
    {
        double[] ret = new double[doubles.size()];
        for (int i=0; i < ret.length; i++)
        {
            ret[i] = doubles.get(i);
        }
        return ret;
    }

}
