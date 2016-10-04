package teamtreehouse.com.stormy.ui.fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;

import android.graphics.drawable.Drawable;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.nio.BufferUnderflowException;

import teamtreehouse.com.stormy.R;

import teamtreehouse.com.stormy.utils.FragmentHelper;
import teamtreehouse.com.stormy.utils.HttpUtils;

import teamtreehouse.com.stormy.utils.StormyConstants;
import teamtreehouse.com.stormy.weather.Current;

import teamtreehouse.com.stormy.weather.Forecast;


/**
 * Created by guyb on 1/10/16.
 */

public class MainFragment extends Fragment
{

    public static final String TAG = MainFragment.class.getSimpleName();

    private Forecast mForecast;

    private TextView mTimeLabel;
    private TextView mTemperatureLabel;
    private TextView mHumidityValue;
    private TextView mPrecipValue;
    private TextView mSummaryLabel;
    private ImageView mIconImageView;
    private ImageView mRefreshImageView;
    private ProgressBar mProgressBar;
    private Button mHourlyButton;
    private Button mDailyButton;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        mTimeLabel = (TextView) rootView.findViewById(R.id.timeLabel);
        mTemperatureLabel = (TextView) rootView.findViewById(R.id.temperatureLabel);
        mHumidityValue = (TextView) rootView.findViewById(R.id.humidityValue);
        mPrecipValue = (TextView) rootView.findViewById(R.id.precipValue);
        mSummaryLabel = (TextView) rootView.findViewById(R.id.summaryLabel);
        mIconImageView = (ImageView) rootView.findViewById(R.id.iconImageView);
        mRefreshImageView = (ImageView) rootView.findViewById(R.id.refreshImageView);
        mProgressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);
        mHourlyButton = (Button) rootView.findViewById(R.id.hourlyButton);
        mDailyButton = (Button) rootView.findViewById(R.id.dailyButton);

        mProgressBar.setVisibility(View.INVISIBLE);

        Bundle bundle = getArguments();
        mForecast = (Forecast) bundle.getSerializable(StormyConstants.FORECAST_DATA);

        mRefreshImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                toggleRefresh();

                HttpUtils httpUtils = new HttpUtils();
                httpUtils.getForecast(new HttpUtils.Callback()
                {

                    @Override
                    public void done(Forecast forecast)
                    {

                        mForecast = forecast;
                        toggleRefresh();
                        updateDisplay();

                    }
                });
            }
        });

        mHourlyButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                startHourlyFragment();
            }
        });

        mDailyButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                startDailyFragment();
            }
        });

        updateDisplay();


        Log.d(TAG, "Main UI code is running!");

        return rootView;
    }

    private void toggleRefresh()
    {

        getActivity().runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                if (mProgressBar.getVisibility() == View.INVISIBLE)
                {
                    mProgressBar.setVisibility(View.VISIBLE);
                    mRefreshImageView.setVisibility(View.INVISIBLE);
                }
                else
                {
                    mProgressBar.setVisibility(View.INVISIBLE);
                    mRefreshImageView.setVisibility(View.VISIBLE);
                }
            }
        });

    }

    private void updateDisplay()
    {

        getActivity().runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                Current current = mForecast.getCurrent();

                mTemperatureLabel.setText(current.getTemperature() + "");
                mTimeLabel.setText("At " + current.getFormattedTime() + " it will be");
                mHumidityValue.setText(current.getHumidity() + "");
                mPrecipValue.setText(current.getPrecipChance() + "%");
                mSummaryLabel.setText(current.getSummary());

                Drawable drawable = getResources().getDrawable(current.getIconId());
                mIconImageView.setImageDrawable(drawable);
            }
        });


    }


    public void startHourlyFragment()
    {
        try
        {
            // Set we are using hourly
            FragmentHelper helper = new FragmentHelper(getActivity());
            helper.setCurrentFragment(StormyConstants.HOURLY_FRAGMENT);

            Bundle bundle = new Bundle();
            bundle.putSerializable(StormyConstants.FORECAST_DATA, mForecast);
            // Setup fragment instance
            Fragment hourlyFragment = new HourlyForecastFragment();
            hourlyFragment.setArguments(bundle);

            // Setup transition
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.add(R.id.container, hourlyFragment);
            transaction.addToBackStack("HourlyFragment");
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            transaction.commit();
        }
        catch(NullPointerException e)
        {
            e.printStackTrace();
        }


    }

    public void startDailyFragment()
    {

        try
        {

            // Set we are using hourly
            FragmentHelper helper = new FragmentHelper(getActivity());
            helper.setCurrentFragment(StormyConstants.DAILY_FRAGMENT);

            Bundle bundle = new Bundle();
            bundle.putSerializable(StormyConstants.FORECAST_DATA, mForecast);
            Fragment dailyFragment = new DailyForecastFragment();
            dailyFragment.setArguments(bundle);
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.add(R.id.container, dailyFragment);
            transaction.addToBackStack("DailyFragment");
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            transaction.commit();
        }
        catch(NullPointerException e)
        {
            e.printStackTrace();
        }


    }


}
