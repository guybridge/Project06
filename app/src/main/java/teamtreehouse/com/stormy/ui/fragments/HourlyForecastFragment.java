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
import android.widget.RelativeLayout;

import teamtreehouse.com.stormy.R;
import teamtreehouse.com.stormy.adapters.HourAdapter;
import teamtreehouse.com.stormy.utils.FragmentHelper;
import teamtreehouse.com.stormy.utils.StormyConstants;
import teamtreehouse.com.stormy.utils.Temperature;
import teamtreehouse.com.stormy.weather.Forecast;
import teamtreehouse.com.stormy.weather.Hour;

/**
 * Created by guyb on 30/09/16.
 */
public class HourlyForecastFragment extends Fragment
{
    private Hour[] mHours;
    private Forecast mForecast;
    private FragmentHelper fragmentHelper;

    private RecyclerView mRecyclerView;
    private RelativeLayout mLayout;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_hourly_forecast, container, false);

        fragmentHelper = new FragmentHelper(getActivity());

        Bundle bundle = getArguments();
        mForecast = (Forecast) bundle.getSerializable(StormyConstants.FORECAST_DATA);
        mHours = mForecast.getHourlyForecast();

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.reyclerView);

        HourAdapter adapter = new HourAdapter(getActivity(), mHours);
        mRecyclerView.setAdapter(adapter);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);

        mRecyclerView.setHasFixedSize(true);

        mRecyclerView.setVerticalScrollbarPosition(fragmentHelper.getScrollPosition());

        mLayout = (RelativeLayout) rootView.findViewById(R.id.hourlyFragment);

        Temperature.setBackground(getActivity(), mLayout, mForecast.getCurrent().getTemperature());

        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        fragmentHelper.setScrollPosition(mRecyclerView.getVerticalScrollbarPosition());
    }



}
