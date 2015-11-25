package s2m.tryviperarchitecture.utils;

import android.app.Activity;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import s2m.tryviperarchitecture.R;

/**
 * Created by cta on 25/11/15.
 */
public class FileUtils
{
    private static String TAG = FileUtils.class.getSimpleName();


    public static Byte[] readAsset(@NonNull Activity activity)
    {
        byte[] byteArray = null;
        InputStream inputStream = null;
        Resources res = activity.getResources();
        try
        {
            // File recordedFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), RecordInteractor.RECORDED_FILE_NAME);
            // inputStream = new FileInputStream(recordedFile);
            inputStream = res.openRawResource(R.raw.whistle);

            byteArray = convertStreamToByteArray(inputStream);
        }
        catch (IOException e)
        {
            Log.e(TAG, e.getMessage(), e);
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
                    Log.e(TAG, e.getMessage(), e);
                }
            }
        }

        Byte[] byteObjects = new Byte[byteArray.length];

        int i = 0;
        for (byte b : byteArray)
        {
            byteObjects[i++] = b;
        }
        return byteObjects;
    }

    public static byte[] convertStreamToByteArray(InputStream is) throws IOException
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        // Ignoring header data
        is.read(new byte[44], 0, 44);

        byte[] buff = new byte[10240];
        int i = Integer.MAX_VALUE;
        while ((i = is.read(buff, 0, buff.length)) > 0)
        {
            baos.write(buff, 0, i);
        }
        return baos.toByteArray(); // be sure to close InputStream in calling function
    }

    public static Short[] addZeroPaddingToPowerTwo(Short[] sampleArray)
    {
        int currentSize = sampleArray.length;
        int totalSize = 2;
        while (currentSize > totalSize)
        {
            totalSize = totalSize * 2;
        }

        Short[] sampleArrayWithZero = new Short[totalSize];
        Arrays.fill(sampleArrayWithZero, (short) 0);
        int i = 0;
        for (Short sample : sampleArray)
        {
            sampleArrayWithZero[i] = sample;
            i++;
        }

        return sampleArrayWithZero;
    }

    public static float hammingWindow(int length, int index)
    {
        return 0.54f - 0.46f * (float) Math.cos(Math.PI * 2 * index / (length - 1));
    }
}
