package s2m.soundcheck.utils;

import android.app.Activity;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import s2m.soundcheck.R;

/**
 * Created by cta on 25/11/15.
 */
public class Helper
{
    private static String TAG = Helper.class.getSimpleName();


    public static byte[] readAsset(@NonNull Activity activity)
    {
        byte[] byteArray = null;
        InputStream inputStream = null;
        Resources res = activity.getResources();
        try
        {
            // File recordedFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), RecordInteractor.RECORDED_FILE_NAME);
            // inputStream = new FileInputStream(recordedFile);
            inputStream = res.openRawResource(R.raw.piano_converted);

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

        return byteArray;
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

    @NonNull
    public static HttpURLConnection buildURLConnection(String serverURL) throws IOException
    {
        URL url = new URL(serverURL);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Connection", "Keep-Alive");
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;");
        return connection;
    }
}
