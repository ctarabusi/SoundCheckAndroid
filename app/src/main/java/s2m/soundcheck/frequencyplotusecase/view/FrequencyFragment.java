package s2m.soundcheck.frequencyplotusecase.view;

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
public class FrequencyFragment extends FragmentWithTitle
{
    private static final String TAG = FrequencyFragment.class.getSimpleName();

    private ViewEventListener eventListener;

    @Bind(R.id.frequency_plot_view)
    FrequencyPlotView frequencyPlotView;

    @Override
    public int getTitle()
    {
        return R.string.navigation_frequency;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_frequency, container, false);
        ButterKnife.bind(this, rootView);

        eventListener = new FrequencyPresenter();
        eventListener.setFrequencyPlot(frequencyPlotView);

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
