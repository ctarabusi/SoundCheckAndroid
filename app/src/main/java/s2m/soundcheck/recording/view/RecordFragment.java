package s2m.soundcheck.recording.view;

import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Chronometer;
import android.widget.ImageView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import s2m.soundcheck.FragmentWithTitle;
import s2m.soundcheck.R;

/**
 * Created by cta on 17/09/15.
 */
public class RecordFragment extends FragmentWithTitle implements UpdateViewInterface
{
    private ViewEventListener eventListener;

    @Bind(R.id.chronometer)
    Chronometer chronometer;

    @Bind(R.id.start_record_button)
    ImageView startRecordButton;

    @Bind(R.id.stop_record_button)
    ImageView stopRecordButton;

    @Override
    public int getTitle()
    {
        return R.string.navigation_record;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_record, container, false);
        ButterKnife.bind(this, rootView);

        eventListener = new RecordPresenter(getContext().getApplicationContext());
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

    @OnClick(R.id.start_record_button)
    public void startRecordingButtonClicked()
    {
        eventListener.startRecordingButtonClicked();
    }

    @OnClick(R.id.stop_record_button)
    public void stopRecordingButtonClicked()
    {
        eventListener.stopRecordingButtonClicked();
    }

    @Override
    public void showRecordingSnackbar(@StringRes int snackBarContentId)
    {
        String snackBarContent = getResources().getString(snackBarContentId);
        Snackbar.make(startRecordButton, snackBarContent, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void startChronometer()
    {
        chronometer.setBase(SystemClock.elapsedRealtime());
        chronometer.start();
        startRecordButton.setVisibility(View.GONE);
        stopRecordButton.setVisibility(View.VISIBLE);
    }

    @Override
    public void stopChronometer()
    {
        chronometer.stop();
        startRecordButton.setVisibility(View.VISIBLE);
        stopRecordButton.setVisibility(View.GONE);
    }
}
