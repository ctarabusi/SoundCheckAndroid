package s2m.soundcheck.comparison.view;

import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import s2m.soundcheck.FragmentWithTitle;
import s2m.soundcheck.R;

/**
 * Created by cta on 17/09/15.
 */
public class CompareFragment extends FragmentWithTitle implements UpdateViewInterface
{
    private ViewEventListener eventListener;

    @Bind(R.id.chronometer)
    Chronometer chronometer;

    @Bind(R.id.start_checksound_button)
    ImageView startCheckSoundButton;

    @Bind(R.id.stop_checksound_button)
    ImageView stopCheckSoundButton;

    @Bind(R.id.compare_textview)
    TextView compareResultTextView;

    @Override
    public int getTitle()
    {
        return R.string.navigation_compare;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_compare, container, false);
        ButterKnife.bind(this, rootView);

        eventListener = new ComparePresenter(getContext().getApplicationContext());
        eventListener.setOutput(this);

        return rootView;
    }

    @Override
    public void onResume()
    {
        super.onResume();
        eventListener.viewVisible();
    }

    @Override
    public void onPause()
    {
        eventListener.viewGone();
        super.onPause();
    }

    @OnClick(R.id.start_checksound_button)
    public void startCheckSoundButtonClicked()
    {
        eventListener.startCheckSoundButtonClicked();
    }

    @OnClick(R.id.stop_checksound_button)
    public void setStopCheckSoundButtonClicked()
    {
        eventListener.stopCheckSoundButtonClicked();
    }

    @Override
    public void showRecordingSnackbar(@StringRes int snackBarContentId)
    {
        String snackBarContent = getResources().getString(snackBarContentId);
        Snackbar.make(startCheckSoundButton, snackBarContent, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void startChronometer()
    {
        chronometer.setBase(SystemClock.elapsedRealtime());
        chronometer.start();
        startCheckSoundButton.setVisibility(View.GONE);
        stopCheckSoundButton.setVisibility(View.VISIBLE);
    }

    @Override
    public void stopChronometer()
    {
        chronometer.stop();
        startCheckSoundButton.setVisibility(View.VISIBLE);
        stopCheckSoundButton.setVisibility(View.GONE);
    }

    @Override
    public void showReturnedValue(String value)
    {
        compareResultTextView.setText(value);
    }
}
