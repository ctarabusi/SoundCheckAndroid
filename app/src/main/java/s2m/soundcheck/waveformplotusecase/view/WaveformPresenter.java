package s2m.soundcheck.waveformplotusecase.view;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.util.Log;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import s2m.soundcheck.utils.FileUtils;

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
        readFileSubscription = Observable.from(FileUtils.readAsset(activity)).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<Byte>()
        {
            private Byte firstByte;

            private List<Short> samples = new ArrayList<>();

            @Override
            public void onCompleted()
            {
                Short[] sampleArray = new Short[samples.size()];

                waveformPlotView.setSamples(samples.toArray(sampleArray));
            }

            @Override
            public void onError(Throwable e)
            {
                Log.e(TAG, e.getMessage());
            }

            @Override
            public void onNext(Byte byteRead)
            {
                if (firstByte == null)
                {
                    firstByte = byteRead;
                }
                else
                {
                    short sample = ByteBuffer.wrap(new byte[]{firstByte, byteRead}).order(ByteOrder.LITTLE_ENDIAN).getShort();
                    firstByte = null;
                    samples.add(sample);
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
    public void setWaveformPlot(@NonNull WaveformPlotView waveformPlotView)
    {
        this.waveformPlotView = waveformPlotView;
    }
}
