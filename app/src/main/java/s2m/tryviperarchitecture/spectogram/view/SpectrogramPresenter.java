package s2m.tryviperarchitecture.spectogram.view;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.util.Log;

import org.apache.commons.math.complex.Complex;
import org.apache.commons.math.transform.FastFourierTransformer;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import s2m.tryviperarchitecture.utils.FileUtils;
import s2m.tryviperarchitecture.waveformplotusecase.view.WaveformPlotView;

/**
 * Created by cta on 18/09/15.
 */
public class SpectrogramPresenter implements ViewEventListener
{
    private static String TAG = SpectrogramPresenter.class.getSimpleName();

    private static int CHUNK_SIZE = 4096;

    private WaveformPlotView waveformPlotView;
    private SpectrogramView  spectrogramView;

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

                sampleArray = FileUtils.addZeroPaddingToPowerTwo(sampleArray);
                final int totalSize = sampleArray.length;

                int amountPossible = totalSize / CHUNK_SIZE;

                Complex[][] results = new Complex[amountPossible][];

                for (int times = 0; times < amountPossible; times++)
                {
                    Complex[] complex = new Complex[CHUNK_SIZE];
                    for (int i = 0; i < CHUNK_SIZE; i++)
                    {
                        //Put the time domain data into a complex number with imaginary part as 0:
                        double sampleWithWindowing = FileUtils.hammingWindow(totalSize, times) * sampleArray[(times * CHUNK_SIZE) + i];
                        complex[i] = new Complex(sampleWithWindowing, 0);
                    }
                    //Perform FFT analysis on the chunk:
                    try
                    {
                        results[times] = new FastFourierTransformer().transform(complex);
                    }
                    catch (IllegalArgumentException e)
                    {
                        e.printStackTrace();
                    }
                }

                spectrogramView.setSamples(results);
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
    public void setWaveformPlot(@NonNull s2m.tryviperarchitecture.waveformplotusecase.view.WaveformPlotView waveformPlotView)
    {
        this.waveformPlotView = waveformPlotView;
    }

    @Override
    public void setSpectrogramView(@NonNull SpectrogramView spectrogramView)
    {
        this.spectrogramView = spectrogramView;
    }

}
