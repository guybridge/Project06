package teamtreehouse.com.stormy.ui.fragments;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import teamtreehouse.com.stormy.R;
import teamtreehouse.com.stormy.adapters.HourAdapter;
import teamtreehouse.com.stormy.utils.StormyConstants;
import teamtreehouse.com.stormy.weather.Forecast;
import teamtreehouse.com.stormy.weather.Hour;

/**
 * Created by guyb on 30/09/16.
 */
@SuppressLint("ValidFragment")
public class HourlyForecastFragment extends Fragment
{
    private Hour[] mHours;
    private Forecast mForecast;

    RecyclerView mRecyclerView;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_hourly_forecast, container, false);

        Bundle bundle = getArguments();
        mForecast = (Forecast) bundle.getSerializable(StormyConstants.FORECAST_DATA);
        mHours = mForecast.getHourlyForecast();

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.reyclerView);

        HourAdapter adapter = new HourAdapter(getActivity(), mHours);
        mRecyclerView.setAdapter(adapter);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);

        mRecyclerView.setHasFixedSize(true);

        return rootView;
    }
}
