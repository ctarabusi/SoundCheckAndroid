package s2m.soundcheck;

import android.support.annotation.NonNull;
import android.support.annotation.UiThread;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import s2m.soundcheck.comparison.view.CompareFragment;
import s2m.soundcheck.frequencyplot.view.FrequencyFragment;
import s2m.soundcheck.recording.view.RecordFragment;
import s2m.soundcheck.spectogram.view.SpectrogramFragment;
import s2m.soundcheck.waveformplotusecase.view.WaveformFragment;

/**
 * Created by cta on 17/09/15.
 */
public class Router
{
    public enum NavigationPaths
    {
        RECORD, COMPARE, WAVEFORM, FREQUENCY, SPECTOGRAM
    }

    private static Router instance = null;

    @UiThread
    public static Router getInstance()
    {
        // We should always call this from the UI Thread so no need of synchronization
        if (instance == null)
        {
            instance = new Router();
        }
        return instance;
    }

    public void navigateFromDrawer(@NonNull MainActivity activity, NavigationPaths navigation)
    {
        FragmentWithTitle fragment;
        switch (navigation)
        {
            case RECORD:
                fragment = new RecordFragment();
                break;

            case COMPARE:
                fragment = new CompareFragment();
                break;

            case FREQUENCY:
                fragment = new FrequencyFragment();
                break;

            case SPECTOGRAM:
                fragment = new SpectrogramFragment();
                break;

            default:
                fragment = new WaveformFragment();
                break;

        }
        replaceFragment(activity, fragment);
    }

    private void replaceFragment(@NonNull MainActivity activity, @NonNull FragmentWithTitle fragment)
    {
        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.replace(R.id.content_frame, fragment);
        ft.commit();

        activity.setTitle(activity.getResources().getString(fragment.getTitle()));
        activity.closeDrawer();
    }
}
