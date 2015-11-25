package s2m.soundcheck.frequencyplotusecase.view;

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
import s2m.soundcheck.utils.FileUtils;

/**
 * Created by cta on 18/09/15.
 */
public class FrequencyPresenter implements ViewEventListener
{
    private static String TAG = FrequencyPresenter.class.getSimpleName();

    private FrequencyPlotView frequencyPlotView;

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
                samples.toArray(sampleArray);

                sampleArray = FileUtils.addZeroPaddingToPowerTwo(sampleArray);

                final int totalSize = sampleArray.length;

                //When turning into frequency domain we'll need complex numbers:
                Complex[] inputFFT = new Complex[totalSize];

                //For all the chunks:
                for (int times = 0; times < totalSize; times++)
                {
                    double sampleWithWindowing = FileUtils.hammingWindow(totalSize, times) * sampleArray[times];
                    Complex complex = new Complex(sampleWithWindowing, 0);
                    //Perform FFT analysis on the chunk:
                    inputFFT[times] = complex;
                }

                Complex[] results = new FastFourierTransformer().transform(inputFFT);
                frequencyPlotView.setSamples(results);
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
    public void setFrequencyPlot(@NonNull FrequencyPlotView frequencyPlotView)
    {
        this.frequencyPlotView = frequencyPlotView;
    }
}
