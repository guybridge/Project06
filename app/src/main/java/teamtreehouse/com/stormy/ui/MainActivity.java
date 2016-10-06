package teamtreehouse.com.stormy.ui;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;

import teamtreehouse.com.stormy.R;
import teamtreehouse.com.stormy.utils.FragmentHelper;


public class MainActivity extends ActionBarActivity
{

    private static final String TAG = MainActivity.class.getSimpleName();
    private Boolean isTablet;
    private FragmentHelper fragmentHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragmentHelper = new FragmentHelper(MainActivity.this);

        // Check if we are using a tablet
        if(findViewById(R.id.largeLayout) != null)
        {
            isTablet = true;
            Log.i(TAG, "We are on a tab");
            fragmentHelper.setIsTablet(true);
        }
        else
        {
            isTablet = false;
            Log.i(TAG, "We are not on a tab");
            fragmentHelper.setIsTablet(false);
        }

    }





}














