package s2m.soundcheck;

import android.app.Application;
import android.content.Context;

import com.squareup.leakcanary.LeakCanary;

/**
 * Created by cta on 14/09/15.
 */
public class ViperApplication extends Application
{
    public static final String BASE_SERVER_URL = "http://192.168.178.15:8080/";

    private static ViperApplication instance;

    public ViperApplication()
    {
        instance = this;
    }

    public static Context getContext()
    {
        return instance;
    }

    @Override
    public void onCreate()
    {
        super.onCreate();
        LeakCanary.install(this);
    }
}
