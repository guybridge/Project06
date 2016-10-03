package teamtreehouse.com.stormy.utils;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONException;

import java.io.IOException;

import teamtreehouse.com.stormy.weather.Forecast;

/**
 * Class to get the forecast
 */
public class HttpUtils
{

    private Forecast mForecast;
    private final double latitude = -31.9505;
    private final double longitude = 115.8605;

    public HttpUtils()
    {

    }

    public interface Callback
    {
        void done(Forecast forecast);
    }

    public void getForecast(final Callback mListener)
    {
        OkHttpClient client = new OkHttpClient();
        final Request request = new Request.Builder().url(buildForecastURL()).build();
        Call call = client.newCall(request);
        call.enqueue(new com.squareup.okhttp.Callback()
        {
            @Override
            public void onFailure(Request request, IOException e)
            {

            }

            @Override
            public void onResponse(Response response) throws IOException
            {

                try
                {
                    String jsonData = response.body().string();
                    mForecast = JsonUtils.parseForecastDetails(jsonData);
                    mListener.done(mForecast);
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }

            }
        });
    }

    public String buildForecastURL()
    {
        String apiKey = "b790a4b581ee6d93d0049964b590fe6e";
        String forecastUrl = "https://api.forecast.io/forecast/" + apiKey +
                "/" + latitude + "," + longitude;

        return forecastUrl;
    }






}
