package s2m.tryviperarchitecture.waveformplotusecase.view;

import android.app.Activity;
import android.content.res.Resources;
import android.support.annotation.NonNull;

import org.apache.commons.math.complex.Complex;
import org.apache.commons.math.transform.FastFourierTransformer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

import s2m.tryviperarchitecture.R;

/**
 * Created by cta on 18/09/15.
 */
public class WaveformPresenter implements ViewEventListener
{
    private static String TAG = WaveformPresenter.class.getSimpleName();

    private WaveformPlotView  waveformPlotView;
    private WaveformPlotView  reconvertedWaveformPlotView;
    private FrequencyPlotView frequencyPlotView;

    private short[] samplesList;

    private byte[] byteArrayHeader;

    private short channelsNumber;
    private int   samplingFrequency;
    private int   bytesPerSecond;

    public WaveformPresenter()
    {

    }

    @Override
    public void viewVisible(@NonNull Activity activity)
    {
        byte[] sampleBytes = readAsset(activity);
        setByteStreamFromWav(sampleBytes);

        waveformPlotView.setSamples(samplesList);

        final int totalSize = 131072; //sampleBytes.length;

        //When turning into frequency domain we'll need complex numbers:
        Complex[] inputFFT = new Complex[totalSize];

        //For all the chunks:
        for (int times = 0; times < totalSize; times++)
        {
            double sampleWithWindowing = hammingWindow(totalSize, times) * sampleBytes[times];
            Complex complex = new Complex(sampleWithWindowing, 0);
            //Perform FFT analysis on the chunk:
            inputFFT[times] = complex;
        }

        Complex[] results = new FastFourierTransformer().transform(inputFFT);
        frequencyPlotView.setSamples(results);


        displayInvertedFFT(results);
    }

    private void displayInvertedFFT(Complex[] results)
    {
        // Inverting the FFT, just half of the sample can be used being simmetrical
        Complex[] resultTimePlot = new FastFourierTransformer().inversetransform(Arrays.copyOfRange(results, 0, results.length / 2));
        short[] reconvertedToTime = new short[resultTimePlot.length];
        int i = 0;
        short minValue = 0;
        short maxValue = 0;
        for (Complex complex : resultTimePlot)
        {
            reconvertedToTime[i] = (short) (complex.abs() * 100);
            if (reconvertedToTime[i] > maxValue)
            {
                maxValue = reconvertedToTime[i];
            }
            if (reconvertedToTime[i] < minValue)
            {
                minValue = reconvertedToTime[i];
            }
            i++;
        }

        // removing average
        short average = (short) ((maxValue - minValue) / 2);
        i = 0;
        for (short sample : reconvertedToTime)
        {
            reconvertedToTime[i] = (short) (sample - average);
            i++;
        }
        reconvertedWaveformPlotView.setSamples(reconvertedToTime);
    }

    public void setByteStreamFromWav(byte[] audioByteArray)
    {
        parseHeader(audioByteArray);
        parseData(audioByteArray);
    }

    private void parseData(byte[] audioByteArray)
    {
        byte[] dataAudioByteArray = Arrays.copyOfRange(audioByteArray, 44, audioByteArray.length);
        samplesList = new short[dataAudioByteArray.length / 2];
        // to turn bytes to shorts as either big endian or little endian.
        ByteBuffer.wrap(dataAudioByteArray).order(ByteOrder.LITTLE_ENDIAN).asShortBuffer().get(samplesList);
    }

    private void parseHeader(byte[] audioByteArray)
    {
        // See https://de.wikipedia.org/wiki/RIFF_WAVE
        byteArrayHeader = Arrays.copyOfRange(audioByteArray, 0, 44);

        channelsNumber = ByteBuffer.wrap(new byte[]{byteArrayHeader[22], byteArrayHeader[23]}).order(ByteOrder.LITTLE_ENDIAN).getShort();
        samplingFrequency = ByteBuffer.wrap(new byte[]{byteArrayHeader[24], byteArrayHeader[25], byteArrayHeader[26], byteArrayHeader[27]}).order(ByteOrder.LITTLE_ENDIAN).getInt();
        bytesPerSecond = ByteBuffer.wrap(new byte[]{byteArrayHeader[28], byteArrayHeader[29], byteArrayHeader[30], byteArrayHeader[31]}).order(ByteOrder.LITTLE_ENDIAN).getInt();
    }

    private float hammingWindow(int length, int index)
    {
        return 0.54f - 0.46f * (float) Math.cos(Math.PI * 2 * index / (length - 1));
    }

    private byte[] readAsset(@NonNull Activity activity)
    {
        byte[] byteArray = null;
        InputStream inputStream = null;
        Resources res = activity.getResources();
        try
        {
            inputStream = res.openRawResource(R.raw.whistle);
            byteArray = convertStreamToByteArray(inputStream);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            if (inputStream != null)
            {
                try
                {
                    inputStream.close();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }
        return byteArray;
    }

    public static byte[] convertStreamToByteArray(InputStream is) throws IOException
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buff = new byte[10240];
        int i = Integer.MAX_VALUE;
        while ((i = is.read(buff, 0, buff.length)) > 0)
        {
            baos.write(buff, 0, i);
        }
        return baos.toByteArray(); // be sure to close InputStream in calling function
    }


    @Override
    public void viewGone()
    {

    }

    @Override
    public void setWaveformPlot(@NonNull WaveformPlotView waveformPlotView)
    {
        this.waveformPlotView = waveformPlotView;
    }

    @Override
    public void setReconvertedWaveformPlot(@NonNull WaveformPlotView waveformPlotView)
    {
        this.reconvertedWaveformPlotView = waveformPlotView;
    }

    @Override
    public void setFrequencyPlot(@NonNull FrequencyPlotView frequencyPlotView)
    {
        this.frequencyPlotView = frequencyPlotView;
    }
}
