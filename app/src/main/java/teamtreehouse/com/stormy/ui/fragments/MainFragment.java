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
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.nio.BufferUnderflowException;

import teamtreehouse.com.stormy.R;

import teamtreehouse.com.stormy.utils.FragmentHelper;
import teamtreehouse.com.stormy.utils.HttpUtils;

import teamtreehouse.com.stormy.utils.StormyConstants;
import teamtreehouse.com.stormy.utils.Temperature;
import teamtreehouse.com.stormy.weather.Current;

import teamtreehouse.com.stormy.weather.Forecast;


/**
 * Created by guyb on 1/10/16.
 */

public class MainFragment extends Fragment
{

    public static final String TAG = MainFragment.class.getSimpleName();

    private Forecast mForecast;

    private RelativeLayout mLayout;
    private TextView mTimeLabel;
    private TextView mTemperatureLabel;
    private TextView mHumidityValue;
    private TextView mPrecipValue;
    private TextView mSummaryLabel;
    private TextView mLocationLabel;
    private ImageView mIconImageView;
    private ImageView mRefreshImageView;
    private ProgressBar mProgressBar;
    private Button mHourlyButton;
    private Button mDailyButton;

    private FragmentHelper fragmentHelper;


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
        mLocationLabel = (TextView) rootView.findViewById(R.id.locationLabel);
        mLayout = (RelativeLayout) rootView.findViewById(R.id.main_layout);

        mProgressBar.setVisibility(View.INVISIBLE);


        // Setup the fragment history helper for tablets
        fragmentHelper = new FragmentHelper(getActivity());


        getForecast();

        setupButtons();

        return rootView;
    }


    private void getForecast()
    {

        toggleRefresh();

        HttpUtils forecast = new HttpUtils();
        forecast.getForecast(new HttpUtils.Callback()
        {
            @Override
            public void done(Forecast forecast)
            {
                toggleRefresh();
                mForecast = forecast;

                updateDisplay();
            }
        });
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

                Temperature.setBackground(getActivity(), mLayout, current.getTemperature());

                mTemperatureLabel.setText(current.getTemperature() + "");
                mTimeLabel.setText("At " + current.getFormattedTime() + " it will be");
                mHumidityValue.setText(current.getHumidity() + "");
                mPrecipValue.setText(current.getPrecipChance() + "%");
                mSummaryLabel.setText(current.getSummary());
                mLocationLabel.setText(current.getTimeZone());

                Drawable drawable = getResources().getDrawable(current.getIconId());
                mIconImageView.setImageDrawable(drawable);
            }
        });

        // Set the second fragment up if we are on a tab
        if(fragmentHelper.getIsTablet())
        {
            if(fragmentHelper.getCurrentFragment().equals(StormyConstants.DAILY_FRAGMENT))
            {
                startDailyFragment();
            }
            else
            {
                startHourlyFragment();
            }

        }

    }

    public void startHourlyFragment()
    {

            fragmentHelper.setCurrentFragment(StormyConstants.HOURLY_FRAGMENT);

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

    public void startDailyFragment()
    {
            // Set we are using daily

            fragmentHelper.setCurrentFragment(StormyConstants.DAILY_FRAGMENT);

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

    private void setupButtons()
    {
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

        mRefreshImageView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                getForecast();
            }
        });
    }


}
