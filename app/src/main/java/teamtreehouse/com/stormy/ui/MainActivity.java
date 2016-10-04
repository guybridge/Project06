package teamtreehouse.com.stormy.ui;

import android.annotation.SuppressLint;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.widget.Toast;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONException;

import java.io.IOException;
import java.net.SocketTimeoutException;

import teamtreehouse.com.stormy.R;
import teamtreehouse.com.stormy.ui.fragments.DailyForecastFragment;
import teamtreehouse.com.stormy.ui.fragments.HourlyForecastFragment;
import teamtreehouse.com.stormy.ui.fragments.MainFragment;
import teamtreehouse.com.stormy.utils.FragmentHelper;
import teamtreehouse.com.stormy.utils.HttpUtils;
import teamtreehouse.com.stormy.utils.JsonUtils;
import teamtreehouse.com.stormy.utils.Network;
import teamtreehouse.com.stormy.utils.StormyConstants;
import teamtreehouse.com.stormy.weather.Day;
import teamtreehouse.com.stormy.weather.Forecast;
import teamtreehouse.com.stormy.weather.Hour;


public class MainActivity extends ActionBarActivity
{

    public static final String TAG = MainActivity.class.getSimpleName();
    private Forecast mForecast;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getForecast();

    }

    public void setupUI()
    {

        runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                // When the large container exists then we are using the large layout
                if(findViewById(R.id.largeLayout) != null)
                {
                    currentForcast();

                    // Check which detail to display
                    FragmentHelper helper = new FragmentHelper(MainActivity.this);
                    if(helper.getCurrentFragment().equals(StormyConstants.DAILY_FRAGMENT))
                    {
                        dailyForecast();
                    }
                    else
                    {
                        hourlyForecast();
                    }


                }
                else
                {
                    singlePaneForecast();
                }
            }
        });


    }

    private void singlePaneForecast()
    {
        Bundle bundle = new Bundle();
        bundle.putSerializable(StormyConstants.FORECAST_DATA, mForecast);
        // Setup to load into the single frame
        MainFragment mainFragment = new MainFragment();
        mainFragment.setArguments(bundle);
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.container, mainFragment);
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        transaction.commit();
    }

    public void currentForcast()
    {
        Bundle bundle = new Bundle();
        bundle.putSerializable(StormyConstants.FORECAST_DATA, mForecast);

        // Setup to load into the single frame
        MainFragment mainFragment = new MainFragment();
        mainFragment.setArguments(bundle);
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.main_container, mainFragment);
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        transaction.commit();
    }

    public void dailyForecast()
    {
        // Set we are using hourly
        FragmentHelper helper = new FragmentHelper(MainActivity.this);
        helper.setCurrentFragment(StormyConstants.DAILY_FRAGMENT);

        Bundle bundle = new Bundle();
        bundle.putSerializable(StormyConstants.FORECAST_DATA, mForecast);

        DailyForecastFragment dailyForecastFragment = new DailyForecastFragment();
        dailyForecastFragment.setArguments(bundle);
        FragmentManager fragmentManager1 = getFragmentManager();
        FragmentTransaction transaction1 = fragmentManager1.beginTransaction();
        transaction1.add(R.id.container, dailyForecastFragment);
        transaction1.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        transaction1.commit();
    }

    public void hourlyForecast()
    {

        // Set we are using hourly
        FragmentHelper helper = new FragmentHelper(MainActivity.this);
        helper.setCurrentFragment(StormyConstants.HOURLY_FRAGMENT);
        Bundle bundle = new Bundle();
        bundle.putSerializable(StormyConstants.FORECAST_DATA, mForecast);

        HourlyForecastFragment hourlyForecastFragment = new HourlyForecastFragment();
        hourlyForecastFragment.setArguments(bundle);
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.container, hourlyForecastFragment);
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        transaction.commit();
    }


    private void getForecast()
    {
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.getForecast(new HttpUtils.Callback()
        {
            @Override
            public void done(Forecast forecast)
            {
                mForecast = forecast;
                setupUI();
            }
        });
    }




}














