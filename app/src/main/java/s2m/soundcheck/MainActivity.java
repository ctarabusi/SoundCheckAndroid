package s2m.soundcheck;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends ActionBarActivity
{
    @Bind(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;

    @Bind(R.id.navigation_view)
    NavigationView navigationView;

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener()
        {

            // This method will trigger on item Click of navigation menu
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem)
            {
                //Checking if the item is in checked state or not, if not make it in checked state
                if (menuItem.isChecked()) menuItem.setChecked(false);
                else menuItem.setChecked(true);

                //Closing drawer on item click
                mDrawerLayout.closeDrawers();
                navigateTo(menuItem.getItemId());
                return true;
            }
        });

        if (savedInstanceState == null)
        {
            navigateTo(Router.NavigationPaths.RECORD);
        }
    }

    public void navigateTo(Router.NavigationPaths navigation)
    {
        Router.getInstance().navigateFromDrawer(this, navigation);
    }

    public void navigateTo(int menuId)
    {
        if (menuId == R.id.navDrawerSpectrogram)
        {
            navigateTo(Router.NavigationPaths.SPECTOGRAM);
        }
        else if (menuId == R.id.navDrawerFrequency)
        {
            navigateTo(Router.NavigationPaths.FREQUENCY);
        }
        else if (menuId == R.id.navDrawerRecord)
        {
            navigateTo(Router.NavigationPaths.RECORD);
        }
        else if (menuId == R.id.navDrawerCompare)
        {
            navigateTo(Router.NavigationPaths.COMPARE);
        }
        else if (menuId == R.id.navDrawerWaveform)
        {
            navigateTo(Router.NavigationPaths.WAVEFORM);
        }
    }

    public void closeDrawer()
    {
        mDrawerLayout.closeDrawer(Gravity.LEFT);
    }
}
