package s2m.soundcheck.spectogramusecase.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.Bind;
import butterknife.ButterKnife;
import s2m.soundcheck.FragmentWithTitle;
import s2m.soundcheck.R;

/**
 * Created by cta on 18/09/15.
 */
public class SpectrogramFragment extends FragmentWithTitle
{
    private static final String TAG = SpectrogramFragment.class.getSimpleName();

    private ViewEventListener eventListener;

    @Bind(R.id.spectrogram_plot_view)
    SpectrogramView spectrogramView;

    @Override
    public int getTitle()
    {
        return R.string.navigation_spectrogram;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_spectrogram, container, false);
        ButterKnife.bind(this, rootView);

        eventListener = new SpectrogramPresenter();
        eventListener.setSpectrogramView(spectrogramView);

        return rootView;
    }

    @Override
    public void onResume()
    {
        eventListener.viewVisible(this.getActivity());
        super.onResume();
    }

    @Override
    public void onPause()
    {
        eventListener.viewGone();
        super.onPause();
    }
}
