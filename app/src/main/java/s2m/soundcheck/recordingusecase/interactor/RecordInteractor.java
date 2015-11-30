package s2m.soundcheck.recordingusecase.interactor;

import android.content.Context;
import android.content.Intent;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;

import javax.inject.Inject;

import rx.Observable;
import rx.Observer;
import rx.schedulers.Schedulers;
import s2m.soundcheck.utils.Helper;

/**
 * Created by cta on 17/09/15.
 */
public class RecordInteractor
{
    private static final String TAG = RecordInteractor.class.getSimpleName();

    public static final String SERVER_URL = "http://192.168.178.15:8080/fourier-transform/checksound";

    public final static String RECORDED_FILE_NAME      = "Recording.wav";
    public final static String RECORDED_TEMP_FILE_NAME = "RecordingTemp.wav";

    private MediaRecorder audioRecorder;

    private DataChangeListener dataChangeListener;

    private Context applicationContext;
    private File    output;

    private static final int RECORDER_BPP            = 16;
    private static final int RECORDER_SAMPLERATE     = 44100;
    private static final int RECORDER_CHANNELS       = AudioFormat.CHANNEL_IN_MONO;
    private static final int RECORDER_AUDIO_ENCODING = AudioFormat.ENCODING_PCM_16BIT;

    private AudioRecord recorder        = null;
    private int         bufferSize      = 4096;
    private Thread      recordingThread = null;
    private boolean     isRecording     = false;

    @Inject
    public RecordInteractor(Context applicationContext)
    {
        this.applicationContext = applicationContext;
    }

    public void setOutput(DataChangeListener dataChangeListener)
    {
        this.dataChangeListener = dataChangeListener;
    }

//    public void initRecorder()
//    {
//        output = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), RECORDED_FILE_NAME);
//    }

    private File getFilename()
    {

        return new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), RECORDED_FILE_NAME);
    }

    private File getTempFilename()
    {
        return new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), RECORDED_TEMP_FILE_NAME);
    }

    public void startRecording()
    {
//        audioRecorder = new MediaRecorder();
//        audioRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
//        audioRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
//        audioRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
//        audioRecorder.setOutputFile(output.getAbsolutePath());
//
//        try
//        {
//            audioRecorder.prepare();
//            audioRecorder.start();
//        } catch (IllegalStateException | IOException e)
//        {
//            Log.e(TAG, e.getMessage(), e);
//            dataChangeListener.exceptionFromInteractor();
//        }

        bufferSize = AudioRecord.getMinBufferSize(44100, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT);

        recorder = new AudioRecord(MediaRecorder.AudioSource.MIC, RECORDER_SAMPLERATE, RECORDER_CHANNELS, RECORDER_AUDIO_ENCODING, bufferSize);

        int i = recorder.getState();
        if (i == 1)
        {
            recorder.startRecording();
        }

        isRecording = true;

        recordingThread = new Thread(new Runnable()
        {

            @Override
            public void run()
            {
                writeAudioDataToFile();
            }
        }, "AudioRecorder Thread");

        recordingThread.start();

    }

    public void stopRecording()
    {
        if (null != recorder)
        {
            isRecording = false;

            int i = recorder.getState();
            if (i == 1) recorder.stop();
            recorder.release();

            recorder = null;
            recordingThread = null;
        }

        Observable.empty().subscribeOn(Schedulers.io()).observeOn(Schedulers.io()).subscribe(new Observer()
        {

            @Override
            public void onCompleted()
            {
                copyWaveFile(getTempFilename(), getFilename());
                deleteTempFile();
            }

            @Override
            public void onError(Throwable e)
            {

            }

            @Override
            public void onNext(Object o)
            {

            }
        });

//
//        try
//        {
//            audioRecorder.stop();
//            audioRecorder.reset();
//            audioRecorder.release();
//            audioRecorder = null;
//        } catch (IllegalStateException e)
//        {
//            Log.e(TAG, e.getMessage(), e);
//            dataChangeListener.exceptionFromInteractor();
//        }

        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        intent.setData(Uri.fromFile(getFilename()));
        applicationContext.sendBroadcast(intent);
    }


    private void writeAudioDataToFile()
    {
        byte data[] = new byte[bufferSize];
        FileOutputStream os = null;

        try
        {
            os = new FileOutputStream(getTempFilename());
        }
        catch (FileNotFoundException e)
        {
// TODO Auto-generated catch block
            e.printStackTrace();
        }

        int read = 0;

        if (null != os)
        {
            while (isRecording)
            {
                read = recorder.read(data, 0, bufferSize);

                if (AudioRecord.ERROR_INVALID_OPERATION != read)
                {
                    try
                    {
                        os.write(data);
                    }
                    catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                }
            }

            try
            {
                os.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    private void deleteTempFile()
    {
        File file = getTempFilename();

        file.delete();
    }

    private void copyWaveFile(File inFilename, File outFilename)
    {
        FileInputStream in = null;
        FileOutputStream outFile = null;
        OutputStream outputStream = null;
        long totalAudioLen = 0;
        long totalDataLen = totalAudioLen + 36;
        long longSampleRate = RECORDER_SAMPLERATE;
        int channels = 1;
        long byteRate = RECORDER_BPP * RECORDER_SAMPLERATE * channels / 8;

        byte[] data = new byte[bufferSize];

        try
        {
            in = new FileInputStream(inFilename);
            outFile = new FileOutputStream(outFilename);
            totalAudioLen = in.getChannel().size();
            totalDataLen = totalAudioLen + 36;

            Log.d(TAG, "File size: " + totalDataLen);

            writeWaveFileHeader(outFile, totalAudioLen, totalDataLen, longSampleRate, channels, byteRate);

            HttpURLConnection connection = Helper.buildURLConnection(SERVER_URL);

            outputStream = connection.getOutputStream();

            while (in.read(data) != -1)
            {
                outFile.write(data);
                outputStream.write(data);
            }

            int status = connection.getResponseCode();
            Log.d(TAG, "Status : " + status);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            try
            {
                in.close();
                outFile.close();
                outputStream.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    private void writeWaveFileHeader(FileOutputStream out, long totalAudioLen, long totalDataLen, long longSampleRate, int channels, long byteRate) throws IOException
    {

        byte[] header = new byte[44];

        header[0] = 'R'; // RIFF/WAVE header
        header[1] = 'I';
        header[2] = 'F';
        header[3] = 'F';
        header[4] = (byte) (totalDataLen & 0xff);
        header[5] = (byte) ((totalDataLen >> 8) & 0xff);
        header[6] = (byte) ((totalDataLen >> 16) & 0xff);
        header[7] = (byte) ((totalDataLen >> 24) & 0xff);
        header[8] = 'W';
        header[9] = 'A';
        header[10] = 'V';
        header[11] = 'E';
        header[12] = 'f'; // 'fmt ' chunk
        header[13] = 'm';
        header[14] = 't';
        header[15] = ' ';
        header[16] = 16; // 4 bytes: size of 'fmt ' chunk
        header[17] = 0;
        header[18] = 0;
        header[19] = 0;
        header[20] = 1; // format = 1
        header[21] = 0;
        header[22] = (byte) channels;
        header[23] = 0;
        header[24] = (byte) (longSampleRate & 0xff);
        header[25] = (byte) ((longSampleRate >> 8) & 0xff);
        header[26] = (byte) ((longSampleRate >> 16) & 0xff);
        header[27] = (byte) ((longSampleRate >> 24) & 0xff);
        header[28] = (byte) (byteRate & 0xff);
        header[29] = (byte) ((byteRate >> 8) & 0xff);
        header[30] = (byte) ((byteRate >> 16) & 0xff);
        header[31] = (byte) ((byteRate >> 24) & 0xff);
        header[32] = (byte) (2 * 16 / 8); // block align
        header[33] = 0;
        header[34] = RECORDER_BPP; // bits per sample
        header[35] = 0;
        header[36] = 'd';
        header[37] = 'a';
        header[38] = 't';
        header[39] = 'a';
        header[40] = (byte) (totalAudioLen & 0xff);
        header[41] = (byte) ((totalAudioLen >> 8) & 0xff);
        header[42] = (byte) ((totalAudioLen >> 16) & 0xff);
        header[43] = (byte) ((totalAudioLen >> 24) & 0xff);

        out.write(header, 0, 44);
    }
}
