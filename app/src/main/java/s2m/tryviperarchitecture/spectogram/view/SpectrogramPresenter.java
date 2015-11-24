package s2m.tryviperarchitecture.spectogram.view;

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

    private short[] samplesList;

    private byte[] byteArrayHeader;

    private short channelsNumber;
    private int   samplingFrequency;
    private int   bytesPerSecond;

    public SpectrogramPresenter()
    {

    }

    @Override
    public void viewVisible(@NonNull Activity activity)
    {
        byte[] sampleBytes = readAsset(activity);
        setByteStreamFromWav(sampleBytes);

        waveformPlotView.setSamples(samplesList);

        final int totalSize = sampleBytes.length;

        int amountPossible = totalSize / CHUNK_SIZE;

        Complex[][] results = new Complex[amountPossible][];

        for (int times = 0; times < amountPossible; times++)
        {
            Complex[] complex = new Complex[CHUNK_SIZE];
            for (int i = 0; i < CHUNK_SIZE; i++)
            {
                //Put the time domain data into a complex number with imaginary part as 0:
                complex[i] = new Complex(sampleBytes[(times * CHUNK_SIZE) + i], 0);
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

    public void setByteStreamFromWav(byte[] audioByteArray)
    {
        parseHeader(audioByteArray);
        parseData(audioByteArray);
    }

    private void parseData(byte[] audioByteArray)
    {
       // byte[] dataAudioByteArray = Arrays.copyOfRange(audioByteArray, 44, audioByteArray.length);
        samplesList = new short[audioByteArray.length / 2];
        // to turn bytes to shorts as either big endian or little endian.
        ByteBuffer.wrap(audioByteArray).order(ByteOrder.LITTLE_ENDIAN).asShortBuffer().get(samplesList);
    }

    private void parseHeader(byte[] audioByteArray)
    {
        // See https://de.wikipedia.org/wiki/RIFF_WAVE
        byteArrayHeader = Arrays.copyOfRange(audioByteArray, 0, 44);

        channelsNumber = ByteBuffer.wrap(new byte[]{byteArrayHeader[22], byteArrayHeader[23]}).order(ByteOrder.LITTLE_ENDIAN).getShort();
        samplingFrequency = ByteBuffer.wrap(new byte[]{byteArrayHeader[24], byteArrayHeader[25], byteArrayHeader[26], byteArrayHeader[27]}).order(ByteOrder.LITTLE_ENDIAN).getInt();
        bytesPerSecond = ByteBuffer.wrap(new byte[]{byteArrayHeader[28], byteArrayHeader[29], byteArrayHeader[30], byteArrayHeader[31]}).order(ByteOrder.LITTLE_ENDIAN).getInt();
    }

    private byte[] readAsset(@NonNull Activity activity)
    {
        byte[] byteArray = null;
        InputStream inputStream = null;
        Resources res = activity.getResources();
        try
        {
            inputStream = res.openRawResource(R.raw.piano_converted);
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
        int i;
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
