package teamtreehouse.com.stormy.ui.fragments;

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
import teamtreehouse.com.stormy.utils.JsonUtils;
import teamtreehouse.com.stormy.utils.Network;
import teamtreehouse.com.stormy.weather.Current;
import teamtreehouse.com.stormy.weather.Day;
import teamtreehouse.com.stormy.weather.Forecast;
import teamtreehouse.com.stormy.weather.Hour;

/**
 * Created by guyb on 1/10/16.
 */
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

        final double latitude = 37.8267;
        final double longitude = -122.423;

        mRefreshImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getForecast(latitude, longitude);
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


        getForecast(latitude, longitude);

        Log.d(TAG, "Main UI code is running!");

        return rootView;
    }

    private void getForecast(double latitude, double longitude)
    {
        String apiKey = "b790a4b581ee6d93d0049964b590fe6e";
        String forecastUrl = "https://api.forecast.io/forecast/" + apiKey +
                "/" + latitude + "," + longitude;

        Log.i(TAG, "HttpUtils URL is: " + forecastUrl);

        if (Network.isNetworkAvailable(getActivity())) {
            toggleRefresh();

            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(forecastUrl)
                    .build();

            Call call = client.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            toggleRefresh();
                        }
                    });
                    alertUserAboutError();
                }

                @Override
                public void onResponse(Response response) throws IOException {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            toggleRefresh();
                        }
                    });

                    try {
                        String jsonData = response.body().string();
                        Log.v(TAG, jsonData);
                        if (response.isSuccessful()) {
                            mForecast = JsonUtils.parseForecastDetails(jsonData);
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    updateDisplay();
                                }
                            });
                        } else {
                            alertUserAboutError();
                        }
                    }
                    catch (IOException e) {
                        Log.e(TAG, "Exception caught: ", e);
                    }
                    catch (JSONException e) {
                        Log.e(TAG, "Exception caught: ", e);
                    }
                }
            });
        }
        else
        {
            Toast.makeText(getActivity(), getString(R.string.network_unavailable_message), Toast.LENGTH_LONG).show();
        }
    }

    private void toggleRefresh() {
        if (mProgressBar.getVisibility() == View.INVISIBLE) {
            mProgressBar.setVisibility(View.VISIBLE);
            mRefreshImageView.setVisibility(View.INVISIBLE);
        }
        else {
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

    private void alertUserAboutError()
    {
        AlertDialogFragment dialog = new AlertDialogFragment();
        dialog.show(getFragmentManager(), "error_dialog");
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
