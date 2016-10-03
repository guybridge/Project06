package teamtreehouse.com.stormy.ui;

import android.annotation.SuppressLint;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.widget.Toast;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONException;

import java.io.IOException;

import teamtreehouse.com.stormy.R;
import teamtreehouse.com.stormy.ui.fragments.DailyForecastFragment;
import teamtreehouse.com.stormy.ui.fragments.MainFragment;
import teamtreehouse.com.stormy.utils.HttpUtils;
import teamtreehouse.com.stormy.utils.JsonUtils;
import teamtreehouse.com.stormy.utils.Network;
import teamtreehouse.com.stormy.weather.Forecast;


public class MainActivity extends ActionBarActivity
{

    public static final String TAG = MainActivity.class.getSimpleName();
    private Forecast mForecast;
    private final double latitude = -31.9505;
    private final double longitude = 115.8605;


    @SuppressLint("MissingSuperCall")
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getForecastData(latitude, longitude);
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
                    dualPaneForecast();

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
        // Setup to load into the single frame
        MainFragment mainFragment = new MainFragment(mForecast);
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.container, mainFragment);
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        transaction.commit();
    }

    private void dualPaneForecast()
    {

        try
        {
            // Setup to load into the single frame
            MainFragment mainFragment = new MainFragment(mForecast);
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.add(R.id.main_container, mainFragment);
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            transaction.commit();

            DailyForecastFragment dailyForecastFragment = new DailyForecastFragment(MainActivity.this, mForecast.getDailyForecast());
            FragmentManager fragmentManager1 = getFragmentManager();
            FragmentTransaction transaction1 = fragmentManager1.beginTransaction();
            transaction1.add(R.id.container, dailyForecastFragment);
            transaction1.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            transaction1.commit();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }



    }



    private void getForecastData(double latitude, double longitude)
    {
        String apiKey = "b790a4b581ee6d93d0049964b590fe6e";
        String forecastUrl = "https://api.forecast.io/forecast/" + apiKey +
                "/" + latitude + "," + longitude;

        Log.i(TAG, "HttpUtils URL is: " + forecastUrl);

        if (Network.isNetworkAvailable(MainActivity.this))
        {

            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(forecastUrl)
                    .build();

            Call call = client.newCall(request);
            call.enqueue(new com.squareup.okhttp.Callback() {
                @Override
                public void onFailure(Request request, IOException e)
                {
                    alertUserAboutError();
                }

                @Override
                public void onResponse(Response response) throws IOException
                {
                    try
                    {
                        String jsonData = response.body().string();
                        Log.v(TAG, jsonData);
                        if (response.isSuccessful())
                        {
                            mForecast = JsonUtils.parseForecastDetails(jsonData);
                            setupUI();
                        }
                        else
                        {
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
            Toast.makeText(MainActivity.this, getString(R.string.network_unavailable_message), Toast.LENGTH_LONG).show();
        }
    }

    private void alertUserAboutError()
    {
        Toast.makeText(MainActivity.this, "Error loading data", Toast.LENGTH_LONG).show();
    }




}














