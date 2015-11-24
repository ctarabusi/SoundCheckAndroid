package s2m.tryviperarchitecture;

import android.support.annotation.NonNull;
import android.support.annotation.UiThread;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import s2m.tryviperarchitecture.spectogram.view.SpectrogramFragment;
import s2m.tryviperarchitecture.thirdusecase.view.RecordFragment;
import s2m.tryviperarchitecture.waveformplotusecase.view.WaveformFragment;

/**
 * Created by cta on 17/09/15.
 */
public class Router
{
    public enum NavigationPaths
    {
        RECORD, WAVEFORM, SPECTOGRAM
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
            case SPECTOGRAM:
                fragment = new SpectrogramFragment();
                break;

            case RECORD:
                fragment = new RecordFragment();
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
