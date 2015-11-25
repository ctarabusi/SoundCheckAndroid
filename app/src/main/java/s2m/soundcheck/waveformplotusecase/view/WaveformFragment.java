package s2m.soundcheck.waveformplotusecase.view;

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
public class WaveformFragment extends FragmentWithTitle
{
    private static final String TAG = WaveformFragment.class.getSimpleName();

    private ViewEventListener eventListener;

    @Bind(R.id.viewform_plot_view)
    WaveformPlotView waveformPlotView;

    @Override
    public int getTitle()
    {
        return R.string.navigation_waveform;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_waveform, container, false);
        ButterKnife.bind(this, rootView);

        eventListener = new WaveformPresenter();
        eventListener.setWaveformPlot(waveformPlotView);

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
