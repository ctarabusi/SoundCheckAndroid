package s2m.tryviperarchitecture.spectogram.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.Bind;
import butterknife.ButterKnife;
import s2m.tryviperarchitecture.FragmentWithTitle;
import s2m.tryviperarchitecture.R;
import s2m.tryviperarchitecture.waveformplotusecase.view.WaveformPlotView;

/**
 * Created by cta on 18/09/15.
 */
public class SpectrogramFragment extends FragmentWithTitle
{
    private static final String TAG = SpectrogramFragment.class.getSimpleName();

    private ViewEventListener eventListener;

    @Bind(R.id.viewform_plot_view)
    WaveformPlotView waveformPlotView;

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
        eventListener.setWaveformPlot(waveformPlotView);
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
