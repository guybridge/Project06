package teamtreehouse.com.stormy.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by guyb on 4/10/16.
 */
public class FragmentHelper
{

    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;
    private Context mContext;
    private Boolean isTablet;

    public FragmentHelper(Context context)
    {
        mContext = context;
        mSharedPreferences = mContext.getSharedPreferences(StormyConstants.PREFERENCES_FILE, Context.MODE_PRIVATE);
        mEditor = mSharedPreferences.edit();
    }

    public void setCurrentFragment(String type)
    {
        mEditor.putString(StormyConstants.KEY_CURRENT_FRAGMENT, type);
        mEditor.apply();
    }

    public String getCurrentFragment()
    {
        return  mSharedPreferences.getString(StormyConstants.KEY_CURRENT_FRAGMENT, StormyConstants.DAILY_FRAGMENT);
    }

    public Boolean getIsTablet()
    {
        return mSharedPreferences.getBoolean(StormyConstants.KEY_IS_TABLET, false);
    }

    public void setIsTablet(Boolean value)
    {
        mEditor.putBoolean(StormyConstants.KEY_IS_TABLET, value);
        mEditor.apply();
    }

    public void setScrollPosition(int value)
    {
        mEditor.putInt(StormyConstants.KEY_SCROLL_POSITION, value);
        mEditor.apply();
    }

    public int getScrollPosition()
    {
        return mSharedPreferences.getInt(StormyConstants.KEY_SCROLL_POSITION, 0);
    }

    public void clearState()
    {
        mEditor.clear();
        mEditor.apply();
    }
}
