package teamtreehouse.com.stormy.utils;

import android.app.Fragment;
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
}
