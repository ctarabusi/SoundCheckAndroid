package s2m.soundcheck.waveformplotusecase.view;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.util.Log;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.ShortBuffer;

import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import s2m.soundcheck.utils.Helper;

/**
 * Created by cta on 18/09/15.
 */
public class WaveformPresenter implements ViewEventListener
{
    private static String TAG = WaveformPresenter.class.getSimpleName();

    private WaveformPlotView  waveformPlotView;

    private Subscription readFileSubscription;

    @Override
    public void viewVisible(@NonNull Activity activity)
    {
        readFileSubscription = Observable.just(Helper.readAsset()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<byte[]>()
        {
            @Override
            public void onCompleted()
            {
            }

            @Override
            public void onError(Throwable e)
            {
                Log.e(TAG, e.getMessage());
            }

            @Override
            public void onNext(byte[] bytesRead)
            {
                short[] sampleArray = new short[bytesRead.length / 2];

                ShortBuffer shortBuffer = ByteBuffer.wrap(bytesRead).order(ByteOrder.LITTLE_ENDIAN).asShortBuffer();
                shortBuffer.get(sampleArray);

                waveformPlotView.setSamples(sampleArray);
            }
        });
    }

    @Override
    public void viewGone()
    {
        readFileSubscription.unsubscribe();
    }

    @Override
    public void setWaveformPlot(@NonNull WaveformPlotView waveformPlotView)
    {
        this.waveformPlotView = waveformPlotView;
    }
}
