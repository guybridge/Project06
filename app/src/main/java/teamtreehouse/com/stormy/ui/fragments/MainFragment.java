package teamtreehouse.com.stormy.ui.fragments;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import android.widget.Toast;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import teamtreehouse.com.stormy.R;
import teamtreehouse.com.stormy.ui.AlertDialogFragment;
import teamtreehouse.com.stormy.utils.HttpUtils;
import teamtreehouse.com.stormy.utils.JsonUtils;
import teamtreehouse.com.stormy.utils.Network;
import teamtreehouse.com.stormy.weather.Current;
import teamtreehouse.com.stormy.weather.Day;
import teamtreehouse.com.stormy.weather.Forecast;
import teamtreehouse.com.stormy.weather.Hour;

/**
 * Created by guyb on 1/10/16.
 */
@SuppressLint("ValidFragment")
public class MainFragment extends Fragment
{

    public static final String TAG = MainFragment.class.getSimpleName();
    public static final String DAILY_FORECAST = "DAILY_FORECAST";
    public static final String HOURLY_FORECAST = "HOURLY_FORECAST";

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

    @SuppressLint("ValidFragment")
    public MainFragment(Forecast forecast)
    {
        mForecast = forecast;
    }


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

        mRefreshImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                getActivity().runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        toggleRefresh();
                    }
                });


                HttpUtils httpUtils = new HttpUtils();
                httpUtils.getForecast(new HttpUtils.Callback()
                {

                    @Override
                    public void done(Forecast forecast)
                    {

                        mForecast = forecast;
                        getActivity().runOnUiThread(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                toggleRefresh();
                                updateDisplay();
                            }
                        });

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

    private void updateDisplay() {
        Current current = mForecast.getCurrent();

        mTemperatureLabel.setText(current.getTemperature() + "");
        mTimeLabel.setText("At " + current.getFormattedTime() + " it will be");
        mHumidityValue.setText(current.getHumidity() + "");
        mPrecipValue.setText(current.getPrecipChance() + "%");
        mSummaryLabel.setText(current.getSummary());

        Drawable drawable = getResources().getDrawable(current.getIconId());
        mIconImageView.setImageDrawable(drawable);
    }


    public void startHourlyFragment()
    {
        try
        {
            // Setup fragment instance
            Fragment hourlyFragment = new HourlyForecastFragment(getActivity(), mForecast.getHourlyForecast());

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
            Fragment dailyFragment = new DailyForecastFragment(getActivity(), mForecast.getDailyForecast());
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
