package teamtreehouse.com.stormy.utils;

import android.content.Context;
import android.widget.RelativeLayout;

import teamtreehouse.com.stormy.R;

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

    public static void setBackground(Context mContext, RelativeLayout mLayout, int temperature)
    {
        if(temperature > 30)
        {
            mLayout.setBackground(mContext.getResources().getDrawable(R.drawable.bg_gradient_hot));
        }
        else if(temperature > 23)
        {
            mLayout.setBackground(mContext.getResources().getDrawable(R.drawable.bg_gradient));
        }
        else if(temperature < 20)
        {
            mLayout.setBackground(mContext.getResources().getDrawable(R.drawable.bg_gradient_cold));
        }
        else if(temperature < 9)
        {
            mLayout.setBackground(mContext.getResources().getDrawable(R.drawable.bg_gradient_freezing));
        }
    }
}
