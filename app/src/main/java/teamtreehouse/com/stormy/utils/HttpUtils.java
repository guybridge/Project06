package teamtreehouse.com.stormy.utils;

import teamtreehouse.com.stormy.weather.Forecast;

/**
 * Created by guyb on 2/10/16.
 */
public class HttpUtils
{

    

    public HttpUtils()
    {

    }

    public interface Listener
    {
        void done(Forecast forecast);
    }


}
