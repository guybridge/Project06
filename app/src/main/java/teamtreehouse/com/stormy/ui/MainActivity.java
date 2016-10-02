package teamtreehouse.com.stormy.ui;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
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

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import teamtreehouse.com.stormy.R;
import teamtreehouse.com.stormy.ui.fragments.DailyForecastFragment;
import teamtreehouse.com.stormy.ui.fragments.HourlyForecastFragment;
import teamtreehouse.com.stormy.ui.fragments.MainFragment;
import teamtreehouse.com.stormy.utils.JsonUtils;
import teamtreehouse.com.stormy.utils.Network;
import teamtreehouse.com.stormy.weather.Current;
import teamtreehouse.com.stormy.weather.Day;
import teamtreehouse.com.stormy.weather.Forecast;
import teamtreehouse.com.stormy.weather.Hour;


public class MainActivity extends ActionBarActivity
{

    public static final String TAG = MainActivity.class.getSimpleName();
    private Forecast mForecast;
    private final double latitude = -31.9505;
    private final double longitude = 115.8605;


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
                    Log.i(TAG, "Large layout found");
                    DailyForecastFragment dailyForecastFragment = new DailyForecastFragment(MainActivity.this, mForecast.getDailyForecast());
                    FragmentManager fragmentManager = getFragmentManager();
                    FragmentTransaction transaction = fragmentManager.beginTransaction();
                    transaction.add(R.id.container, dailyForecastFragment);
                    transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                    transaction.commit();
                }
                else
                {
                    Log.i(TAG, "Using single pane layout");
                    // Setup to load into the single frame
                    MainFragment mainFragment = new MainFragment();
                    FragmentManager fragmentManager = getFragmentManager();
                    FragmentTransaction transaction = fragmentManager.beginTransaction();
                    transaction.add(R.id.container, mainFragment);
                    transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                    transaction.commit();
                }
            }
        });


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
            call.enqueue(new Callback() {
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














