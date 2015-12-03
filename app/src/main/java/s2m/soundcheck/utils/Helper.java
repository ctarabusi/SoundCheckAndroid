package s2m.soundcheck.utils;

import android.os.Environment;
import android.support.annotation.NonNull;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import s2m.soundcheck.recording.interactor.RecordInteractor;

/**
 * Created by cta on 25/11/15.
 */
public class Helper
{
    private static String TAG = Helper.class.getSimpleName();

    public static byte[] readAsset()
    {
        File recordedFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), RecordInteractor.RECORDED_FILE_NAME);
        return convertStreamToByteArray(recordedFile);
    }

    private static byte[] convertStreamToByteArray(File recordedFile)
    {
        InputStream inputStream = null;
        try
        {
            inputStream = new FileInputStream(recordedFile);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            // Ignoring wav header bytes
            inputStream.read(new byte[44], 0, 44);

            byte[] buffer = new byte[2048];
            for (int length = inputStream.read(buffer); length != -1; length = inputStream.read(buffer))
            {
                baos.write(buffer, 0, length);
            }
            baos.flush();
            baos.close();

            return baos.toByteArray();
        }
        catch (IOException e)
        {
            Log.d(TAG, e.getMessage(), e);
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
                    Log.d(TAG, e.getMessage(), e);
                }
            }
        }
        return new byte[0];
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
