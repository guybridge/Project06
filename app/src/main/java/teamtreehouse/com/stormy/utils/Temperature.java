package teamtreehouse.com.stormy.utils;

/**
 * Created by guyb on 6/10/16.
 */
public class Temperature
{
    public static int convert(double farenheit)
    {
        int newTemp = Integer.valueOf((int) farenheit);
        return (newTemp - 32) * 5 / 9;
    }
}
