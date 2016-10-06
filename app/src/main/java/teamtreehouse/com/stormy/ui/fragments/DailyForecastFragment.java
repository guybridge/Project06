package teamtreehouse.com.stormy.ui.fragments;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;

import butterknife.ButterKnife;
import butterknife.InjectView;
import teamtreehouse.com.stormy.R;
import teamtreehouse.com.stormy.adapters.DayAdapter;
import teamtreehouse.com.stormy.ui.MainActivity;
import teamtreehouse.com.stormy.utils.FragmentHelper;
import teamtreehouse.com.stormy.utils.StormyConstants;
import teamtreehouse.com.stormy.weather.Day;
import teamtreehouse.com.stormy.weather.Forecast;

/**
 * Created by guyb on 30/09/16.
 */
@SuppressLint("ValidFragment")
public class DailyForecastFragment extends Fragment
{

    private static final String TAG = DailyForecastFragment.class.getSimpleName();
    private Day[] mDays;
    private Forecast mForecast;

    private ListView mListView;
    private TextView mEmptyTextView;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

        View rootView = inflater.inflate(R.layout.fragment_daily_forecast, container, false);



        Bundle bundle = getArguments();
        mForecast = (Forecast) bundle.getSerializable(StormyConstants.FORECAST_DATA);
        mDays = mForecast.getDailyForecast();


        mListView = (ListView) rootView.findViewById(R.id.list);
        mEmptyTextView = (TextView) rootView.findViewById(R.id.empty);

        DayAdapter adapter = new DayAdapter(getActivity(), mDays);
        mListView.setAdapter(adapter);

        mListView.setEmptyView(mEmptyTextView);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String dayOfTheWeek = mDays[position].getDayOfTheWeek();
                String conditions = mDays[position].getSummary();
                String highTemp = mDays[position].getTemperatureMax() + "";
                String message = String.format("On %s the high will be %s and it will be %s",
                        dayOfTheWeek,
                        highTemp,
                        conditions);
                Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
            }
        });

        return rootView;

    }

}
