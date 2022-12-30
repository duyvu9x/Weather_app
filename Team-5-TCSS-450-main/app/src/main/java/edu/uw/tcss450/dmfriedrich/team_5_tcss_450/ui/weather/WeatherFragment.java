package edu.uw.tcss450.dmfriedrich.team_5_tcss_450.ui.weather;

import android.annotation.SuppressLint;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.method.BaseMovementMethod;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.Scroller;
import android.widget.TableRow;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import edu.uw.tcss450.dmfriedrich.team_5_tcss_450.R;
import edu.uw.tcss450.dmfriedrich.team_5_tcss_450.databinding.FragmentWeatherBinding;
import edu.uw.tcss450.dmfriedrich.team_5_tcss_450.ui.auth.signin.SignInFragmentDirections;
import edu.uw.tcss450.dmfriedrich.team_5_tcss_450.ui.location.MapViewModel;

public class WeatherFragment extends Fragment  {
    private WeatherViewModel mWeatherModel;
    private MapViewModel mMapModel;
    private FragmentWeatherBinding binding;
    private MediaPlayer player;
    private MediaPlayer player2;
    TabLayout tabLayout;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mWeatherModel = new ViewModelProvider(getActivity())
                .get(WeatherViewModel.class);
        mMapModel = new ViewModelProvider(getActivity())
                .get(MapViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentWeatherBinding.inflate(inflater);
        //play(container);
        return binding.getRoot();

    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tabLayout = binding.days;
        TableRow t = new TableRow(getContext());
        Calendar Day1 = Calendar.getInstance();
        Calendar Day2 = Calendar.getInstance();
        Calendar Day3 = Calendar.getInstance();
        Calendar Day4 = Calendar.getInstance();
        Calendar Day5 = Calendar.getInstance();
        Calendar Day6 = Calendar.getInstance();
        Calendar Day7 = Calendar.getInstance();
        Date day = new Date();
        Day1.setTime(day);
        Day2.setTime(day);
        Day3.setTime(day);
        Day4.setTime(day);
        Day5.setTime(day);
        Day6.setTime(day);
        Day7.setTime(day);

        Day2.add(Calendar.DAY_OF_YEAR, 1);
        Day3.add(Calendar.DAY_OF_YEAR, 2);
        Day4.add(Calendar.DAY_OF_YEAR, 3);
        Day5.add(Calendar.DAY_OF_YEAR, 4);
        Day6.add(Calendar.DAY_OF_YEAR, 5);
        Day7.add(Calendar.DAY_OF_YEAR, 6);

        DateFormat Date_Formatter = new SimpleDateFormat("EEEE");
        String Day_Name = Date_Formatter.format(Day1.getTime());
        tabLayout.addTab(tabLayout.newTab().setText("Current"));
        tabLayout.addTab(tabLayout.newTab().setText("Hourly"));
        tabLayout.addTab(tabLayout.newTab().setText(Day_Name));
        Day_Name = Date_Formatter.format(Day2.getTime());
        tabLayout.addTab(tabLayout.newTab().setText(Day_Name));
        Day_Name = Date_Formatter.format(Day3.getTime());
        tabLayout.addTab(tabLayout.newTab().setText(Day_Name));
        Day_Name = Date_Formatter.format(Day4.getTime());
        tabLayout.addTab(tabLayout.newTab().setText(Day_Name));
        Day_Name = Date_Formatter.format(Day5.getTime());
        tabLayout.addTab(tabLayout.newTab().setText(Day_Name));
        Day_Name = Date_Formatter.format(Day6.getTime());
        tabLayout.addTab(tabLayout.newTab().setText(Day_Name));
        Day_Name = Date_Formatter.format(Day7.getTime());
        tabLayout.addTab(tabLayout.newTab().setText(Day_Name));
        tabLayout.addTab(tabLayout.newTab().setText("Map"));
        mWeatherModel.connect(mMapModel.getCurrentLocation().getLatitude(),mMapModel.getCurrentLocation().getLongitude(), mMapModel.getZip());
        mWeatherModel.addResponseObserverDaily(getViewLifecycleOwner(),
                this::observeResponse);
        mWeatherModel.connect2(mMapModel.getCurrentLocation().getLatitude(),mMapModel.getCurrentLocation().getLongitude(), mMapModel.getZip());
        mWeatherModel.addResponseObserver24(getViewLifecycleOwner(),
                this::observeResponse2);
        mWeatherModel.connect3(mMapModel.getCurrentLocation().getLatitude(),mMapModel.getCurrentLocation().getLongitude(), mMapModel.getZip());
        mWeatherModel.addResponseObserverCurrent(getViewLifecycleOwner(),
                this::observeResponse3);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                String tabN = (String) tab.getText();
                if(tabN.equalsIgnoreCase("Map")){
                    Navigation.findNavController(getView()).navigate(
                            WeatherFragmentDirections.actionNavigationWeatherToMapFargment()
                    );
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        NavController navController = NavHostFragment.findNavController(this);
       // navController.navigate(R.id.nav_print);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //stop();
        binding = null;
    }

    /**
     * An observer on the HTTP Response from the web server. This observer should be
     * attached to SignInViewModel.
     *
     * @param response the Response from the server
     */
    private void observeResponse(final JSONObject response) {
        if (response.length() > 0) {
            try {
                JSONArray days = response.getJSONObject("message").getJSONArray("days");
                String day1Date = days.getJSONObject(0).getString("date");
                String day2Date = days.getJSONObject(1).getString("date");
                String day3Date = days.getJSONObject(2).getString("date");
                String day4Date = days.getJSONObject(3).getString("date");
                String day5Date = days.getJSONObject(4).getString("date");
                String day6Date = days.getJSONObject(5).getString("date");
                String day7Date = days.getJSONObject(6).getString("date");
                String day1MaxMin = days.getJSONObject(0).getString("max")+ "°C"+"     "+days.getJSONObject(0).getString("min")+ "°C";
                String day1Weather = days.getJSONObject(0).getString("description");
                String day2MaxMin = days.getJSONObject(1).getString("max")+ "°C"+"     "+days.getJSONObject(0).getString("min")+ "°C";
                String day2Weather = days.getJSONObject(1).getString("description");
                String day3MaxMin = days.getJSONObject(2).getString("max")+ "°C"+"     "+days.getJSONObject(0).getString("min")+ "°C";
                String day3Weather = days.getJSONObject(2).getString("description");
                String day4MaxMin = days.getJSONObject(3).getString("max")+ "°C"+"     "+days.getJSONObject(0).getString("min")+ "°C";
                String day4Weather = days.getJSONObject(3).getString("description");
                String day5MaxMin = days.getJSONObject(4).getString("max")+ "°C"+"     "+days.getJSONObject(0).getString("min")+ "°C";
                String day5Weather = days.getJSONObject(4).getString("description");
                String day6MaxMin = days.getJSONObject(5).getString("max")+ "°C"+"     "+days.getJSONObject(0).getString("min")+ "°C";
                String day6Weather = days.getJSONObject(5).getString("description");
                String day7MaxMin = days.getJSONObject(6).getString("max")+ "°C"+"     "+days.getJSONObject(0).getString("min")+ "°C";
                String day7Weather = days.getJSONObject(6).getString("description");
                String output = "Max     Min" + "\n" + day1MaxMin + "\n" + "Weather for the day\n" + day1Weather;
                String output2 = "Max     Min" + "\n" + day2MaxMin + "\n" + "Weather for the day\n" + day2Weather;
                String output3 = "Max     Min" + "\n" + day3MaxMin + "\n" + "Weather for the day\n" + day3Weather;
                String output4 = "Max     Min" + "\n" + day4MaxMin + "\n" + "Weather for the day\n" + day4Weather;
                String output5 = "Max     Min" + "\n" + day5MaxMin + "\n" + "Weather for the day\n" + day5Weather;
                String output6 = "Max     Min" + "\n" + day6MaxMin + "\n" + "Weather for the day\n" + day6Weather;
                String output7 = "Max     Min" + "\n" + day7MaxMin + "\n" + "Weather for the day\n" + day7Weather;

                tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                    @SuppressLint("NewApi")
                    @Override
                    public void onTabSelected(TabLayout.Tab tab) {
                        String tabN = (String) tab.getText();

                        if(tabN.equalsIgnoreCase("Monday")){
                            binding.textWeather.setText(output);
                            binding.textWeather1.setText("");
                            binding.textWeather2.setText("");
                            binding.textWeather3.setText("");
                            binding.textWeather4.setText("");
                            binding.textWeather5.setText("");
                            binding.textWeather6.setText("");
                            binding.textWeather7.setText("");
                            binding.textWeather8.setText("");
                            binding.textWeather9.setText("");
                            binding.textWeather10.setText("");
                            binding.textWeather11.setText("");
                            binding.textWeather12.setText("");
                            binding.textWeather13.setText("");
                            binding.textWeather14.setText("");
                            binding.textWeather15.setText("");
                            binding.textWeather16.setText("");
                            binding.textWeather17.setText("");
                            binding.textWeather18.setText("");
                            binding.textWeather19.setText("");
                            binding.textWeather20.setText("");
                            binding.textWeather21.setText("");
                            binding.textWeather22.setText("");
                            binding.textWeather23.setText("");
                            binding.textWeather24.setText("");
                            binding.horizontalScrollView.scrollTo(0,0);
                        }
                    }

                    @Override
                    public void onTabUnselected(TabLayout.Tab tab) {

                    }

                    @Override
                    public void onTabReselected(TabLayout.Tab tab) {

                    }
                });
                tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                    @Override
                    public void onTabSelected(TabLayout.Tab tab) {
                        String tabN = (String) tab.getText();
                        if(tabN.equalsIgnoreCase("Tuesday")){
                            binding.textWeather.setText(output2);
                            binding.textWeather1.setText("");
                            binding.textWeather2.setText("");
                            binding.textWeather3.setText("");
                            binding.textWeather4.setText("");
                            binding.textWeather5.setText("");
                            binding.textWeather6.setText("");
                            binding.textWeather7.setText("");
                            binding.textWeather8.setText("");
                            binding.textWeather9.setText("");
                            binding.textWeather10.setText("");
                            binding.textWeather11.setText("");
                            binding.textWeather12.setText("");
                            binding.textWeather13.setText("");
                            binding.textWeather14.setText("");
                            binding.textWeather15.setText("");
                            binding.textWeather16.setText("");
                            binding.textWeather17.setText("");
                            binding.textWeather18.setText("");
                            binding.textWeather19.setText("");
                            binding.textWeather20.setText("");
                            binding.textWeather21.setText("");
                            binding.textWeather22.setText("");
                            binding.textWeather23.setText("");
                            binding.textWeather24.setText("");
                            binding.horizontalScrollView.scrollTo(0,0);
                        }
                    }

                    @Override
                    public void onTabUnselected(TabLayout.Tab tab) {

                    }

                    @Override
                    public void onTabReselected(TabLayout.Tab tab) {

                    }
                });
                tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                    @Override
                    public void onTabSelected(TabLayout.Tab tab) {
                        String tabN = (String) tab.getText();
                        if(tabN.equalsIgnoreCase("Wednesday")){
                            binding.textWeather.setText(output3);
                            binding.textWeather1.setText("");
                            binding.textWeather2.setText("");
                            binding.textWeather3.setText("");
                            binding.textWeather4.setText("");
                            binding.textWeather5.setText("");
                            binding.textWeather6.setText("");
                            binding.textWeather7.setText("");
                            binding.textWeather8.setText("");
                            binding.textWeather9.setText("");
                            binding.textWeather10.setText("");
                            binding.textWeather11.setText("");
                            binding.textWeather12.setText("");
                            binding.textWeather13.setText("");
                            binding.textWeather14.setText("");
                            binding.textWeather15.setText("");
                            binding.textWeather16.setText("");
                            binding.textWeather17.setText("");
                            binding.textWeather18.setText("");
                            binding.textWeather19.setText("");
                            binding.textWeather20.setText("");
                            binding.textWeather21.setText("");
                            binding.textWeather22.setText("");
                            binding.textWeather23.setText("");
                            binding.textWeather24.setText("");
                            binding.horizontalScrollView.scrollTo(0,0);
                        }
                    }

                    @Override
                    public void onTabUnselected(TabLayout.Tab tab) {

                    }

                    @Override
                    public void onTabReselected(TabLayout.Tab tab) {

                    }
                });
                tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                    @Override
                    public void onTabSelected(TabLayout.Tab tab) {
                        String tabN = (String) tab.getText();
                        if(tabN.equalsIgnoreCase("Thursday")){
                            binding.textWeather.setText(output4);
                            binding.textWeather1.setText("");
                            binding.textWeather2.setText("");
                            binding.textWeather3.setText("");
                            binding.textWeather4.setText("");
                            binding.textWeather5.setText("");
                            binding.textWeather6.setText("");
                            binding.textWeather7.setText("");
                            binding.textWeather8.setText("");
                            binding.textWeather9.setText("");
                            binding.textWeather10.setText("");
                            binding.textWeather11.setText("");
                            binding.textWeather12.setText("");
                            binding.textWeather13.setText("");
                            binding.textWeather14.setText("");
                            binding.textWeather15.setText("");
                            binding.textWeather16.setText("");
                            binding.textWeather17.setText("");
                            binding.textWeather18.setText("");
                            binding.textWeather19.setText("");
                            binding.textWeather20.setText("");
                            binding.textWeather21.setText("");
                            binding.textWeather22.setText("");
                            binding.textWeather23.setText("");
                            binding.textWeather24.setText("");
                            binding.horizontalScrollView.scrollTo(0,0);
                        }
                    }

                    @Override
                    public void onTabUnselected(TabLayout.Tab tab) {

                    }

                    @Override
                    public void onTabReselected(TabLayout.Tab tab) {

                    }
                });
                tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                    @Override
                    public void onTabSelected(TabLayout.Tab tab) {
                        String tabN = (String) tab.getText();
                        if(tabN.equalsIgnoreCase("Friday")){
                            binding.textWeather.setText(output5);
                            binding.textWeather1.setText("");
                            binding.textWeather2.setText("");
                            binding.textWeather3.setText("");
                            binding.textWeather4.setText("");
                            binding.textWeather5.setText("");
                            binding.textWeather6.setText("");
                            binding.textWeather7.setText("");
                            binding.textWeather8.setText("");
                            binding.textWeather9.setText("");
                            binding.textWeather10.setText("");
                            binding.textWeather11.setText("");
                            binding.textWeather12.setText("");
                            binding.textWeather13.setText("");
                            binding.textWeather14.setText("");
                            binding.textWeather15.setText("");
                            binding.textWeather16.setText("");
                            binding.textWeather17.setText("");
                            binding.textWeather18.setText("");
                            binding.textWeather19.setText("");
                            binding.textWeather20.setText("");
                            binding.textWeather21.setText("");
                            binding.textWeather22.setText("");
                            binding.textWeather23.setText("");
                            binding.textWeather24.setText("");
                            binding.horizontalScrollView.scrollTo(0,0);
                        }
                    }

                    @Override
                    public void onTabUnselected(TabLayout.Tab tab) {

                    }

                    @Override
                    public void onTabReselected(TabLayout.Tab tab) {

                    }
                });
                tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                    @Override
                    public void onTabSelected(TabLayout.Tab tab) {
                        String tabN = (String) tab.getText();
                        if(tabN.equalsIgnoreCase("Saturday")){
                            binding.textWeather.setText(output6);
                            binding.textWeather1.setText("");
                            binding.textWeather2.setText("");
                            binding.textWeather3.setText("");
                            binding.textWeather4.setText("");
                            binding.textWeather5.setText("");
                            binding.textWeather6.setText("");
                            binding.textWeather7.setText("");
                            binding.textWeather8.setText("");
                            binding.textWeather9.setText("");
                            binding.textWeather10.setText("");
                            binding.textWeather11.setText("");
                            binding.textWeather12.setText("");
                            binding.textWeather13.setText("");
                            binding.textWeather14.setText("");
                            binding.textWeather15.setText("");
                            binding.textWeather16.setText("");
                            binding.textWeather17.setText("");
                            binding.textWeather18.setText("");
                            binding.textWeather19.setText("");
                            binding.textWeather20.setText("");
                            binding.textWeather21.setText("");
                            binding.textWeather22.setText("");
                            binding.textWeather23.setText("");
                            binding.textWeather24.setText("");
                            binding.horizontalScrollView.scrollTo(0,0);
                        }
                    }

                    @Override
                    public void onTabUnselected(TabLayout.Tab tab) {

                    }

                    @Override
                    public void onTabReselected(TabLayout.Tab tab) {

                    }
                });
                tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                    @Override
                    public void onTabSelected(TabLayout.Tab tab) {
                        String tabN = (String) tab.getText();
                        if(tabN.equalsIgnoreCase("Sunday")){
                            binding.textWeather.setText(output7);
                            binding.textWeather1.setText("");
                            binding.textWeather2.setText("");
                            binding.textWeather3.setText("");
                            binding.textWeather4.setText("");
                            binding.textWeather5.setText("");
                            binding.textWeather6.setText("");
                            binding.textWeather7.setText("");
                            binding.textWeather8.setText("");
                            binding.textWeather9.setText("");
                            binding.textWeather10.setText("");
                            binding.textWeather11.setText("");
                            binding.textWeather12.setText("");
                            binding.textWeather13.setText("");
                            binding.textWeather14.setText("");
                            binding.textWeather15.setText("");
                            binding.textWeather16.setText("");
                            binding.textWeather17.setText("");
                            binding.textWeather18.setText("");
                            binding.textWeather19.setText("");
                            binding.textWeather20.setText("");
                            binding.textWeather21.setText("");
                            binding.textWeather22.setText("");
                            binding.textWeather23.setText("");
                            binding.textWeather24.setText("");
                            binding.horizontalScrollView.scrollTo(0,0);
                        }
                    }

                    @Override
                    public void onTabUnselected(TabLayout.Tab tab) {

                    }

                    @Override
                    public void onTabReselected(TabLayout.Tab tab) {

                    }
                });
            } catch (JSONException e) {
                Log.e("JSON Parse Error", e.getMessage());
            }

        } else {
            Log.d("JSON Response", "No Response");
        }

    }
    private void observeResponse2(final JSONObject response) {
        if (response.length() > 0) {
            try {
                JSONArray hours = response.getJSONObject("message").getJSONArray("hours");
                StringBuilder output = new StringBuilder();
                output.append("12").append(":00:00 A.M.");
                output.append("\n");
                output.append("Temp");
                output.append("\n");
                output.append(hours.getJSONObject(0).getString("temp")).append("°C");
                output.append("\n");
                output.append("Wind Speed");
                output.append("\n");
                output.append(hours.getJSONObject(0).getString("wind_spd")).append(" mph");
                output.append("\n");
                output.append("Weather");
                output.append("\n");
                output.append(hours.getJSONObject(0).getString("description"));

                StringBuilder output2 = new StringBuilder();
                output2.append("1").append(":00:00 A.M.");
                output2.append("\n");
                output2.append("Temp");
                output2.append("\n");
                output2.append(hours.getJSONObject(1).getString("temp")).append("°C");
                output2.append("\n");
                output2.append("Wind Speed");
                output2.append("\n");
                output2.append(hours.getJSONObject(1).getString("wind_spd")).append(" mph");
                output2.append("\n");
                output2.append("Weather");
                output2.append("\n");
                output2.append(hours.getJSONObject(1).getString("description"));

                StringBuilder output3 = new StringBuilder();
                output3.append("2").append(":00:00 A.M.");
                output3.append("\n");
                output3.append("Temp");
                output3.append("\n");
                output3.append(hours.getJSONObject(2).getString("temp")).append("°C");
                output3.append("\n");
                output3.append("Wind Speed");
                output3.append("\n");
                output3.append(hours.getJSONObject(2).getString("wind_spd")).append(" mph");
                output3.append("\n");
                output3.append("Weather");
                output3.append("\n");
                output3.append(hours.getJSONObject(2).getString("description"));

                StringBuilder output4 = new StringBuilder();
                output4.append("3").append(":00:00 A.M.");
                output4.append("\n");
                output4.append("Temp");
                output4.append("\n");
                output4.append(hours.getJSONObject(3).getString("temp")).append("°C");
                output4.append("\n");
                output4.append("Wind Speed");
                output4.append("\n");
                output4.append(hours.getJSONObject(3).getString("wind_spd")).append(" mph");
                output4.append("\n");
                output4.append("Weather");
                output4.append("\n");
                output4.append(hours.getJSONObject(3).getString("description"));

                StringBuilder output5 = new StringBuilder();
                output5.append("4").append(":00:00 A.M.");
                output5.append("\n");
                output5.append("Temp");
                output5.append("\n");
                output5.append(hours.getJSONObject(4).getString("temp")).append("°C");
                output5.append("\n");
                output5.append("Wind Speed");
                output5.append("\n");
                output5.append(hours.getJSONObject(4).getString("wind_spd")).append(" mph");
                output5.append("\n");
                output5.append("Weather");
                output5.append("\n");
                output5.append(hours.getJSONObject(4).getString("description"));

                StringBuilder output6 = new StringBuilder();
                output6.append("5").append(":00:00 A.M.");
                output6.append("\n");
                output6.append("Temp");
                output6.append("\n");
                output6.append(hours.getJSONObject(5).getString("temp")).append("°C");
                output6.append("\n");
                output6.append("Wind Speed");
                output6.append("\n");
                output6.append(hours.getJSONObject(5).getString("wind_spd")).append(" mph");
                output6.append("\n");
                output6.append("Weather");
                output6.append("\n");
                output6.append(hours.getJSONObject(5).getString("description"));

                StringBuilder output7 = new StringBuilder();
                output7.append("6").append(":00:00 A.M.");
                output7.append("\n");
                output7.append("Temp");
                output7.append("\n");
                output7.append(hours.getJSONObject(6).getString("temp")).append("°C");
                output7.append("\n");
                output7.append("Wind Speed");
                output7.append("\n");
                output7.append(hours.getJSONObject(6).getString("wind_spd")).append(" mph");
                output7.append("\n");
                output7.append("Weather");
                output7.append("\n");
                output7.append(hours.getJSONObject(6).getString("description"));

                StringBuilder output8 = new StringBuilder();
                output8.append("7").append(":00:00 A.M.");
                output8.append("\n");
                output8.append("Temp");
                output8.append("\n");
                output8.append(hours.getJSONObject(7).getString("temp")).append("°C");
                output8.append("\n");
                output8.append("Wind Speed");
                output8.append("\n");
                output8.append(hours.getJSONObject(7).getString("wind_spd")).append(" mph");
                output8.append("\n");
                output8.append("Weather");
                output8.append("\n");
                output8.append(hours.getJSONObject(7).getString("description"));

                StringBuilder output9 = new StringBuilder();
                output9.append("8").append(":00:00 A.M.");
                output9.append("\n");
                output9.append("Temp");
                output9.append("\n");
                output9.append(hours.getJSONObject(8).getString("temp")).append("°C");
                output9.append("\n");
                output9.append("Wind Speed");
                output9.append("\n");
                output9.append(hours.getJSONObject(8).getString("wind_spd")).append(" mph");
                output9.append("\n");
                output9.append("Weather");
                output9.append("\n");
                output9.append(hours.getJSONObject(8).getString("description"));

                StringBuilder output10 = new StringBuilder();
                output10.append("9").append(":00:00 A.M.");
                output10.append("\n");
                output10.append("Temp");
                output10.append("\n");
                output10.append(hours.getJSONObject(9).getString("temp")).append("°C");
                output10.append("\n");
                output10.append("Wind Speed");
                output10.append("\n");
                output10.append(hours.getJSONObject(9).getString("wind_spd")).append(" mph");
                output10.append("\n");
                output10.append("Weather");
                output10.append("\n");
                output10.append(hours.getJSONObject(9).getString("description"));

                StringBuilder output11 = new StringBuilder();
                output11.append("10").append(":00:00 A.M.");
                output11.append("\n");
                output11.append("Temp");
                output11.append("\n");
                output11.append(hours.getJSONObject(10).getString("temp")).append("°C");
                output11.append("\n");
                output11.append("Wind Speed");
                output11.append("\n");
                output11.append(hours.getJSONObject(10).getString("wind_spd")).append(" mph");
                output11.append("\n");
                output11.append("Weather");
                output11.append("\n");
                output11.append(hours.getJSONObject(10).getString("description"));

                StringBuilder output12 = new StringBuilder();
                output12.append("11").append(":00:00 A.M.");
                output12.append("\n");
                output12.append("Temp");
                output12.append("\n");
                output12.append(hours.getJSONObject(11).getString("temp")).append("°C");
                output12.append("\n");
                output12.append("Wind Speed");
                output12.append("\n");
                output12.append(hours.getJSONObject(11).getString("wind_spd")).append(" mph");
                output12.append("\n");
                output12.append("Weather");
                output12.append("\n");
                output12.append(hours.getJSONObject(11).getString("description"));

                StringBuilder output13 = new StringBuilder();
                output13.append("12").append(":00:00 P.M.");
                output13.append("\n");
                output13.append("Temp");
                output13.append("\n");
                output13.append(hours.getJSONObject(12).getString("temp")).append("°C");
                output13.append("\n");
                output13.append("Wind Speed");
                output13.append("\n");
                output13.append(hours.getJSONObject(12).getString("wind_spd")).append(" mph");
                output13.append("\n");
                output13.append("Weather");
                output13.append("\n");
                output13.append(hours.getJSONObject(12).getString("description"));

                StringBuilder output14 = new StringBuilder();
                output14.append("1").append(":00:00 P.M.");
                output14.append("\n");
                output14.append("Temp");
                output14.append("\n");
                output14.append(hours.getJSONObject(13).getString("temp")).append("°C");
                output14.append("\n");
                output14.append("Wind Speed");
                output14.append("\n");
                output14.append(hours.getJSONObject(13).getString("wind_spd")).append(" mph");
                output14.append("\n");
                output14.append("Weather");
                output14.append("\n");
                output14.append(hours.getJSONObject(13).getString("description"));

                StringBuilder output15 = new StringBuilder();
                output15.append("2").append(":00:00 P.M.");
                output15.append("\n");
                output15.append("Temp");
                output15.append("\n");
                output15.append(hours.getJSONObject(14).getString("temp")).append("°C");
                output15.append("\n");
                output15.append("Wind Speed");
                output15.append("\n");
                output15.append(hours.getJSONObject(14).getString("wind_spd")).append(" mph");
                output15.append("\n");
                output15.append("Weather");
                output15.append("\n");
                output15.append(hours.getJSONObject(14).getString("description"));

                StringBuilder output16 = new StringBuilder();
                output16.append("3").append(":00:00 P.M.");
                output16.append("\n");
                output16.append("Temp");
                output16.append("\n");
                output16.append(hours.getJSONObject(15).getString("temp")).append("°C");
                output16.append("\n");
                output16.append("Wind Speed");
                output16.append("\n");
                output16.append(hours.getJSONObject(15).getString("wind_spd")).append(" mph");
                output16.append("\n");
                output16.append("Weather");
                output16.append("\n");
                output16.append(hours.getJSONObject(15).getString("description"));

                StringBuilder output17 = new StringBuilder();
                output17.append("4").append(":00:00 P.M.");
                output17.append("\n");
                output17.append("Temp");
                output17.append("\n");
                output17.append(hours.getJSONObject(16).getString("temp")).append("°C");
                output17.append("\n");
                output17.append("Wind Speed");
                output17.append("\n");
                output17.append(hours.getJSONObject(16).getString("wind_spd")).append(" mph");
                output17.append("\n");
                output17.append("Weather");
                output17.append("\n");
                output17.append(hours.getJSONObject(16).getString("description"));

                StringBuilder output18 = new StringBuilder();
                output18.append("5").append(":00:00 P.M.");
                output18.append("\n");
                output18.append("Temp");
                output18.append("\n");
                output18.append(hours.getJSONObject(17).getString("temp")).append("°C");
                output18.append("\n");
                output18.append("Wind Speed");
                output18.append("\n");
                output18.append(hours.getJSONObject(17).getString("wind_spd")).append(" mph");
                output18.append("\n");
                output18.append("Weather");
                output18.append("\n");
                output18.append(hours.getJSONObject(17).getString("description"));

                StringBuilder output19 = new StringBuilder();
                output19.append("6").append(":00:00 P.M.");
                output19.append("\n");
                output19.append("Temp");
                output19.append("\n");
                output19.append(hours.getJSONObject(18).getString("temp")).append("°C");
                output19.append("\n");
                output19.append("Wind Speed");
                output19.append("\n");
                output19.append(hours.getJSONObject(18).getString("wind_spd")).append(" mph");
                output19.append("\n");
                output19.append("Weather");
                output19.append("\n");
                output19.append(hours.getJSONObject(18).getString("description"));

                StringBuilder output20 = new StringBuilder();
                output20.append("7").append(":00:00 P.M.");
                output20.append("\n");
                output20.append("Temp");
                output20.append("\n");
                output20.append(hours.getJSONObject(19).getString("temp")).append("°C");
                output20.append("\n");
                output20.append("Wind Speed");
                output20.append("\n");
                output20.append(hours.getJSONObject(19).getString("wind_spd")).append(" mph");
                output20.append("\n");
                output20.append("Weather");
                output20.append("\n");
                output20.append(hours.getJSONObject(19).getString("description"));

                StringBuilder output21 = new StringBuilder();
                output21.append("8").append(":00:00 P.M.");
                output21.append("\n");
                output21.append("Temp");
                output21.append("\n");
                output21.append(hours.getJSONObject(20).getString("temp")).append("°C");
                output21.append("\n");
                output21.append("Wind Speed");
                output21.append("\n");
                output21.append(hours.getJSONObject(20).getString("wind_spd")).append(" mph");
                output21.append("\n");
                output21.append("Weather");
                output21.append("\n");
                output21.append(hours.getJSONObject(20).getString("description"));

                StringBuilder output22 = new StringBuilder();
                output22.append("9").append(":00:00 P.M.");
                output22.append("\n");
                output22.append("Temp");
                output22.append("\n");
                output22.append(hours.getJSONObject(21).getString("temp")).append("°C");
                output22.append("\n");
                output22.append("Wind Speed");
                output22.append("\n");
                output22.append(hours.getJSONObject(21).getString("wind_spd")).append(" mph");
                output22.append("\n");
                output22.append("Weather");
                output22.append("\n");
                output22.append(hours.getJSONObject(21).getString("description"));

                StringBuilder output23 = new StringBuilder();
                output23.append("10").append(":00:00 P.M.");
                output23.append("\n");
                output23.append("Temp");
                output23.append("\n");
                output23.append(hours.getJSONObject(22).getString("temp")).append("°C");
                output23.append("\n");
                output23.append("Wind Speed");
                output23.append("\n");
                output23.append(hours.getJSONObject(22).getString("wind_spd")).append(" mph");
                output23.append("\n");
                output23.append("Weather");
                output23.append("\n");
                output23.append(hours.getJSONObject(22).getString("description"));

                StringBuilder output24 = new StringBuilder();
                output24.append("11").append(":00:00 P.M.");
                output24.append("\n");
                output24.append("Temp");
                output24.append("\n");
                output24.append(hours.getJSONObject(23).getString("temp")).append("°C");
                output24.append("\n");
                output24.append("Wind Speed");
                output24.append("\n");
                output24.append(hours.getJSONObject(23).getString("wind_spd")).append(" mph");
                output24.append("\n");
                output24.append("Weather");
                output24.append("\n");
                output24.append(hours.getJSONObject(23).getString("description"));

                binding.horizontalScrollView.scrollTo(0,0);
                tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                    @Override
                    public void onTabSelected(TabLayout.Tab tab) {
                        String tabN = (String) tab.getText();
                        if(tabN.equalsIgnoreCase("Hourly")){
                            Date currentDate = new Date();

                            SimpleDateFormat formatter = new SimpleDateFormat("HH");

                            String timeIn24Hours = formatter.format(currentDate);
                            binding.textWeather.setText("");
                            binding.textWeather1.setText(output);
                            binding.textWeather2.setText(output2);
                            binding.textWeather3.setText(output3);
                            binding.textWeather4.setText(output4);
                            binding.textWeather5.setText(output5);
                            binding.textWeather6.setText(output6);
                            binding.textWeather7.setText(output7);
                            binding.textWeather8.setText(output8);
                            binding.textWeather9.setText(output9);
                            binding.textWeather10.setText(output10);
                            binding.textWeather11.setText(output11);
                            binding.textWeather12.setText(output12);
                            binding.textWeather13.setText(output13);
                            binding.textWeather14.setText(output14);
                            binding.textWeather15.setText(output15);
                            binding.textWeather16.setText(output16);
                            binding.textWeather17.setText(output17);
                            binding.textWeather18.setText(output18);
                            binding.textWeather19.setText(output19);
                            binding.textWeather20.setText(output20);
                            binding.textWeather21.setText(output21);
                            binding.textWeather22.setText(output22);
                            binding.textWeather23.setText(output23);
                            binding.textWeather24.setText(output24);
                            binding.horizontalScrollView.scrollTo(0,0);
                            if(timeIn24Hours.equals("01")){
                                binding.textWeather1.setVisibility(View.GONE);
                            }else if(timeIn24Hours.equals("02")){
                                binding.textWeather1.setVisibility(View.GONE);
                                binding.textWeather2.setVisibility(View.GONE);
                            }else if(timeIn24Hours.equals("03")){
                                binding.textWeather1.setVisibility(View.GONE);
                                binding.textWeather2.setVisibility(View.GONE);
                                binding.textWeather3.setVisibility(View.GONE);
                            }else if(timeIn24Hours.equals("04")){
                                binding.textWeather1.setVisibility(View.GONE);
                                binding.textWeather2.setVisibility(View.GONE);
                                binding.textWeather3.setVisibility(View.GONE);
                                binding.textWeather4.setVisibility(View.GONE);
                            }else if(timeIn24Hours.equals("05")){
                                binding.textWeather1.setVisibility(View.GONE);
                                binding.textWeather2.setVisibility(View.GONE);
                                binding.textWeather3.setVisibility(View.GONE);
                                binding.textWeather4.setVisibility(View.GONE);
                                binding.textWeather5.setVisibility(View.GONE);
                            }else if(timeIn24Hours.equals("06")){
                                binding.textWeather1.setVisibility(View.GONE);
                                binding.textWeather2.setVisibility(View.GONE);
                                binding.textWeather3.setVisibility(View.GONE);
                                binding.textWeather4.setVisibility(View.GONE);
                                binding.textWeather5.setVisibility(View.GONE);
                                binding.textWeather6.setVisibility(View.GONE);
                            }else if(timeIn24Hours.equals("07")){
                                binding.textWeather1.setVisibility(View.GONE);
                                binding.textWeather2.setVisibility(View.GONE);
                                binding.textWeather3.setVisibility(View.GONE);
                                binding.textWeather4.setVisibility(View.GONE);
                                binding.textWeather5.setVisibility(View.GONE);
                                binding.textWeather6.setVisibility(View.GONE);
                                binding.textWeather7.setVisibility(View.GONE);
                            }else if(timeIn24Hours.equals("08")){
                                binding.textWeather1.setVisibility(View.GONE);
                                binding.textWeather2.setVisibility(View.GONE);
                                binding.textWeather3.setVisibility(View.GONE);
                                binding.textWeather4.setVisibility(View.GONE);
                                binding.textWeather5.setVisibility(View.GONE);
                                binding.textWeather6.setVisibility(View.GONE);
                                binding.textWeather7.setVisibility(View.GONE);
                                binding.textWeather8.setVisibility(View.GONE);

                            }else if(timeIn24Hours.equals("09")){
                                binding.textWeather1.setVisibility(View.GONE);
                                binding.textWeather2.setVisibility(View.GONE);
                                binding.textWeather3.setVisibility(View.GONE);
                                binding.textWeather4.setVisibility(View.GONE);
                                binding.textWeather5.setVisibility(View.GONE);
                                binding.textWeather6.setVisibility(View.GONE);
                                binding.textWeather7.setVisibility(View.GONE);
                                binding.textWeather8.setVisibility(View.GONE);
                                binding.textWeather9.setVisibility(View.GONE);

                            }else if(timeIn24Hours.equals("10")){
                                binding.textWeather1.setVisibility(View.GONE);
                                binding.textWeather2.setVisibility(View.GONE);
                                binding.textWeather3.setVisibility(View.GONE);
                                binding.textWeather4.setVisibility(View.GONE);
                                binding.textWeather5.setVisibility(View.GONE);
                                binding.textWeather6.setVisibility(View.GONE);
                                binding.textWeather7.setVisibility(View.GONE);
                                binding.textWeather8.setVisibility(View.GONE);
                                binding.textWeather9.setVisibility(View.GONE);
                                binding.textWeather10.setVisibility(View.GONE);
                            }else if(timeIn24Hours.equals("11")){
                                binding.textWeather1.setVisibility(View.GONE);
                                binding.textWeather2.setVisibility(View.GONE);
                                binding.textWeather3.setVisibility(View.GONE);
                                binding.textWeather4.setVisibility(View.GONE);
                                binding.textWeather5.setVisibility(View.GONE);
                                binding.textWeather6.setVisibility(View.GONE);
                                binding.textWeather7.setVisibility(View.GONE);
                                binding.textWeather8.setVisibility(View.GONE);
                                binding.textWeather9.setVisibility(View.GONE);
                                binding.textWeather10.setVisibility(View.GONE);
                                binding.textWeather11.setVisibility(View.GONE);
                            }else if(timeIn24Hours.equals("12")){
                                binding.textWeather1.setVisibility(View.GONE);
                                binding.textWeather2.setVisibility(View.GONE);
                                binding.textWeather3.setVisibility(View.GONE);
                                binding.textWeather4.setVisibility(View.GONE);
                                binding.textWeather5.setVisibility(View.GONE);
                                binding.textWeather6.setVisibility(View.GONE);
                                binding.textWeather7.setVisibility(View.GONE);
                                binding.textWeather8.setVisibility(View.GONE);
                                binding.textWeather9.setVisibility(View.GONE);
                                binding.textWeather10.setVisibility(View.GONE);
                                binding.textWeather11.setVisibility(View.GONE);
                                binding.textWeather12.setVisibility(View.GONE);
                            }else if(timeIn24Hours.equals("13")){
                                binding.textWeather1.setVisibility(View.GONE);
                                binding.textWeather2.setVisibility(View.GONE);
                                binding.textWeather3.setVisibility(View.GONE);
                                binding.textWeather4.setVisibility(View.GONE);
                                binding.textWeather5.setVisibility(View.GONE);
                                binding.textWeather6.setVisibility(View.GONE);
                                binding.textWeather7.setVisibility(View.GONE);
                                binding.textWeather8.setVisibility(View.GONE);
                                binding.textWeather9.setVisibility(View.GONE);
                                binding.textWeather10.setVisibility(View.GONE);
                                binding.textWeather11.setVisibility(View.GONE);
                                binding.textWeather12.setVisibility(View.GONE);
                                binding.textWeather13.setVisibility(View.GONE);
                            }else if(timeIn24Hours.equals("14")){
                                binding.textWeather1.setVisibility(View.GONE);
                                binding.textWeather2.setVisibility(View.GONE);
                                binding.textWeather3.setVisibility(View.GONE);
                                binding.textWeather4.setVisibility(View.GONE);
                                binding.textWeather5.setVisibility(View.GONE);
                                binding.textWeather6.setVisibility(View.GONE);
                                binding.textWeather7.setVisibility(View.GONE);
                                binding.textWeather8.setVisibility(View.GONE);
                                binding.textWeather9.setVisibility(View.GONE);
                                binding.textWeather10.setVisibility(View.GONE);
                                binding.textWeather11.setVisibility(View.GONE);
                                binding.textWeather12.setVisibility(View.GONE);
                                binding.textWeather13.setVisibility(View.GONE);
                                binding.textWeather14.setVisibility(View.GONE);

                            }else if(timeIn24Hours.equals("15")){
                                binding.textWeather1.setVisibility(View.GONE);
                                binding.textWeather2.setVisibility(View.GONE);
                                binding.textWeather3.setVisibility(View.GONE);
                                binding.textWeather4.setVisibility(View.GONE);
                                binding.textWeather5.setVisibility(View.GONE);
                                binding.textWeather6.setVisibility(View.GONE);
                                binding.textWeather7.setVisibility(View.GONE);
                                binding.textWeather8.setVisibility(View.GONE);
                                binding.textWeather9.setVisibility(View.GONE);
                                binding.textWeather10.setVisibility(View.GONE);
                                binding.textWeather11.setVisibility(View.GONE);
                                binding.textWeather12.setVisibility(View.GONE);
                                binding.textWeather13.setVisibility(View.GONE);
                                binding.textWeather14.setVisibility(View.GONE);
                                binding.textWeather15.setVisibility(View.GONE);
                            }else if(timeIn24Hours.equals("16")){
                                binding.textWeather1.setVisibility(View.GONE);
                                binding.textWeather2.setVisibility(View.GONE);
                                binding.textWeather3.setVisibility(View.GONE);
                                binding.textWeather4.setVisibility(View.GONE);
                                binding.textWeather5.setVisibility(View.GONE);
                                binding.textWeather6.setVisibility(View.GONE);
                                binding.textWeather7.setVisibility(View.GONE);
                                binding.textWeather8.setVisibility(View.GONE);
                                binding.textWeather9.setVisibility(View.GONE);
                                binding.textWeather10.setVisibility(View.GONE);
                                binding.textWeather11.setVisibility(View.GONE);
                                binding.textWeather12.setVisibility(View.GONE);
                                binding.textWeather13.setVisibility(View.GONE);
                                binding.textWeather14.setVisibility(View.GONE);
                                binding.textWeather15.setVisibility(View.GONE);
                                binding.textWeather16.setVisibility(View.GONE);
                            }else if(timeIn24Hours.equals("17")){
                                binding.textWeather1.setVisibility(View.GONE);
                                binding.textWeather2.setVisibility(View.GONE);
                                binding.textWeather3.setVisibility(View.GONE);
                                binding.textWeather4.setVisibility(View.GONE);
                                binding.textWeather5.setVisibility(View.GONE);
                                binding.textWeather6.setVisibility(View.GONE);
                                binding.textWeather7.setVisibility(View.GONE);
                                binding.textWeather8.setVisibility(View.GONE);
                                binding.textWeather9.setVisibility(View.GONE);
                                binding.textWeather10.setVisibility(View.GONE);
                                binding.textWeather11.setVisibility(View.GONE);
                                binding.textWeather12.setVisibility(View.GONE);
                                binding.textWeather13.setVisibility(View.GONE);
                                binding.textWeather14.setVisibility(View.GONE);
                                binding.textWeather15.setVisibility(View.GONE);
                                binding.textWeather16.setVisibility(View.GONE);
                                binding.textWeather17.setVisibility(View.GONE);
                            }else if(timeIn24Hours.equals("18")){
                                binding.textWeather1.setVisibility(View.GONE);
                                binding.textWeather2.setVisibility(View.GONE);
                                binding.textWeather3.setVisibility(View.GONE);
                                binding.textWeather4.setVisibility(View.GONE);
                                binding.textWeather5.setVisibility(View.GONE);
                                binding.textWeather6.setVisibility(View.GONE);
                                binding.textWeather7.setVisibility(View.GONE);
                                binding.textWeather8.setVisibility(View.GONE);
                                binding.textWeather9.setVisibility(View.GONE);
                                binding.textWeather10.setVisibility(View.GONE);
                                binding.textWeather11.setVisibility(View.GONE);
                                binding.textWeather12.setVisibility(View.GONE);
                                binding.textWeather13.setVisibility(View.GONE);
                                binding.textWeather14.setVisibility(View.GONE);
                                binding.textWeather15.setVisibility(View.GONE);
                                binding.textWeather16.setVisibility(View.GONE);
                                binding.textWeather17.setVisibility(View.GONE);
                                binding.textWeather18.setVisibility(View.GONE);
                            }else if(timeIn24Hours.equals("19")){
                                binding.textWeather1.setVisibility(View.GONE);
                                binding.textWeather2.setVisibility(View.GONE);
                                binding.textWeather3.setVisibility(View.GONE);
                                binding.textWeather4.setVisibility(View.GONE);
                                binding.textWeather5.setVisibility(View.GONE);
                                binding.textWeather6.setVisibility(View.GONE);
                                binding.textWeather7.setVisibility(View.GONE);
                                binding.textWeather8.setVisibility(View.GONE);
                                binding.textWeather9.setVisibility(View.GONE);
                                binding.textWeather10.setVisibility(View.GONE);
                                binding.textWeather11.setVisibility(View.GONE);
                                binding.textWeather12.setVisibility(View.GONE);
                                binding.textWeather13.setVisibility(View.GONE);
                                binding.textWeather14.setVisibility(View.GONE);
                                binding.textWeather15.setVisibility(View.GONE);
                                binding.textWeather16.setVisibility(View.GONE);
                                binding.textWeather17.setVisibility(View.GONE);
                                binding.textWeather18.setVisibility(View.GONE);
                                binding.textWeather19.setVisibility(View.GONE);
                            }else if(timeIn24Hours.equals("20")){
                                binding.textWeather1.setVisibility(View.GONE);
                                binding.textWeather2.setVisibility(View.GONE);
                                binding.textWeather3.setVisibility(View.GONE);
                                binding.textWeather4.setVisibility(View.GONE);
                                binding.textWeather5.setVisibility(View.GONE);
                                binding.textWeather6.setVisibility(View.GONE);
                                binding.textWeather7.setVisibility(View.GONE);
                                binding.textWeather8.setVisibility(View.GONE);
                                binding.textWeather9.setVisibility(View.GONE);
                                binding.textWeather10.setVisibility(View.GONE);
                                binding.textWeather11.setVisibility(View.GONE);
                                binding.textWeather12.setVisibility(View.GONE);
                                binding.textWeather13.setVisibility(View.GONE);
                                binding.textWeather14.setVisibility(View.GONE);
                                binding.textWeather15.setVisibility(View.GONE);
                                binding.textWeather16.setVisibility(View.GONE);
                                binding.textWeather17.setVisibility(View.GONE);
                                binding.textWeather18.setVisibility(View.GONE);
                                binding.textWeather19.setVisibility(View.GONE);
                                binding.textWeather20.setVisibility(View.GONE);
                            }else if(timeIn24Hours.equals("21")){
                                binding.textWeather1.setVisibility(View.GONE);
                                binding.textWeather2.setVisibility(View.GONE);
                                binding.textWeather3.setVisibility(View.GONE);
                                binding.textWeather4.setVisibility(View.GONE);
                                binding.textWeather5.setVisibility(View.GONE);
                                binding.textWeather6.setVisibility(View.GONE);
                                binding.textWeather7.setVisibility(View.GONE);
                                binding.textWeather8.setVisibility(View.GONE);
                                binding.textWeather9.setVisibility(View.GONE);
                                binding.textWeather10.setVisibility(View.GONE);
                                binding.textWeather11.setVisibility(View.GONE);
                                binding.textWeather12.setVisibility(View.GONE);
                                binding.textWeather13.setVisibility(View.GONE);
                                binding.textWeather14.setVisibility(View.GONE);
                                binding.textWeather15.setVisibility(View.GONE);
                                binding.textWeather16.setVisibility(View.GONE);
                                binding.textWeather17.setVisibility(View.GONE);
                                binding.textWeather18.setVisibility(View.GONE);
                                binding.textWeather19.setVisibility(View.GONE);
                                binding.textWeather20.setVisibility(View.GONE);
                                binding.textWeather21.setVisibility(View.GONE);
                            }else if(timeIn24Hours.equals("22")){
                                binding.textWeather1.setVisibility(View.GONE);
                                binding.textWeather2.setVisibility(View.GONE);
                                binding.textWeather3.setVisibility(View.GONE);
                                binding.textWeather4.setVisibility(View.GONE);
                                binding.textWeather5.setVisibility(View.GONE);
                                binding.textWeather6.setVisibility(View.GONE);
                                binding.textWeather7.setVisibility(View.GONE);
                                binding.textWeather8.setVisibility(View.GONE);
                                binding.textWeather9.setVisibility(View.GONE);
                                binding.textWeather10.setVisibility(View.GONE);
                                binding.textWeather11.setVisibility(View.GONE);
                                binding.textWeather12.setVisibility(View.GONE);
                                binding.textWeather13.setVisibility(View.GONE);
                                binding.textWeather14.setVisibility(View.GONE);
                                binding.textWeather15.setVisibility(View.GONE);
                                binding.textWeather16.setVisibility(View.GONE);
                                binding.textWeather17.setVisibility(View.GONE);
                                binding.textWeather18.setVisibility(View.GONE);
                                binding.textWeather19.setVisibility(View.GONE);
                                binding.textWeather20.setVisibility(View.GONE);
                                binding.textWeather21.setVisibility(View.GONE);
                                binding.textWeather22.setVisibility(View.GONE);
                            }else if(timeIn24Hours.equals("23")){
                                binding.textWeather1.setVisibility(View.GONE);
                                binding.textWeather2.setVisibility(View.GONE);
                                binding.textWeather3.setVisibility(View.GONE);
                                binding.textWeather4.setVisibility(View.GONE);
                                binding.textWeather5.setVisibility(View.GONE);
                                binding.textWeather6.setVisibility(View.GONE);
                                binding.textWeather7.setVisibility(View.GONE);
                                binding.textWeather8.setVisibility(View.GONE);
                                binding.textWeather9.setVisibility(View.GONE);
                                binding.textWeather10.setVisibility(View.GONE);
                                binding.textWeather11.setVisibility(View.GONE);
                                binding.textWeather12.setVisibility(View.GONE);
                                binding.textWeather13.setVisibility(View.GONE);
                                binding.textWeather14.setVisibility(View.GONE);
                                binding.textWeather15.setVisibility(View.GONE);
                                binding.textWeather16.setVisibility(View.GONE);
                                binding.textWeather17.setVisibility(View.GONE);
                                binding.textWeather18.setVisibility(View.GONE);
                                binding.textWeather19.setVisibility(View.GONE);
                                binding.textWeather20.setVisibility(View.GONE);
                                binding.textWeather21.setVisibility(View.GONE);
                                binding.textWeather22.setVisibility(View.GONE);
                                binding.textWeather23.setVisibility(View.GONE);
                            }
                        }
                    }

                    @Override
                    public void onTabUnselected(TabLayout.Tab tab) {

                    }

                    @Override
                    public void onTabReselected(TabLayout.Tab tab) {

                    }
                });
            } catch (JSONException e) {
                Log.e("JSON Parse Error", e.getMessage());
            }

        } else {
            Log.d("JSON Response", "No Response");
        }
    }
    private void observeResponse3(final JSONObject response) {
        if (response.length() > 0) {
            try {
                StringBuilder output = new StringBuilder();
                output.append("Temp");
                output.append("\n");
                output.append(response.getJSONObject("message").getString("temp")).append("°C");
                output.append("\n");
                output.append("Clouds");
                output.append("\n");
                output.append(response.getJSONObject("message").getString("clouds")).append("%");
                output.append("\n");
                output.append("Air Q uality");
                output.append("\n");
                output.append(response.getJSONObject("message").getString("aqi"));
                output.append("\n");
                output.append("Weather");
                output.append("\n");
                output.append(response.getJSONObject("message").getString("weather"));
                binding.textWeather.setText(output.toString());
                tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                    @Override
                    public void onTabSelected(TabLayout.Tab tab) {
                        String tabN = (String) tab.getText();
                        if(tabN.equalsIgnoreCase("current")){
                            binding.textWeather.setText(output);
                            binding.textWeather1.setText("");
                            binding.textWeather2.setText("");
                            binding.textWeather3.setText("");
                            binding.textWeather4.setText("");
                            binding.textWeather5.setText("");
                            binding.textWeather6.setText("");
                            binding.textWeather7.setText("");
                            binding.textWeather8.setText("");
                            binding.textWeather9.setText("");
                            binding.textWeather10.setText("");
                            binding.textWeather11.setText("");
                            binding.textWeather12.setText("");
                            binding.textWeather13.setText("");
                            binding.textWeather14.setText("");
                            binding.textWeather15.setText("");
                            binding.textWeather16.setText("");
                            binding.textWeather17.setText("");
                            binding.textWeather18.setText("");
                            binding.textWeather19.setText("");
                            binding.textWeather20.setText("");
                            binding.textWeather21.setText("");
                            binding.textWeather22.setText("");
                            binding.textWeather23.setText("");
                            binding.textWeather24.setText("");
                            binding.horizontalScrollView.scrollTo(0,0);
                        }
                    }

                    @Override
                    public void onTabUnselected(TabLayout.Tab tab) {

                    }

                    @Override
                    public void onTabReselected(TabLayout.Tab tab) {

                    }
                });
            } catch (JSONException e) {
                Log.e("JSON Parse Error", e.getMessage());
            }

        } else {
            Log.d("JSON Response", "No Response");
        }
    }

    public void play(View v){
        if(player == null){
            //player = MediaPlayer.create(getContext(),R.raw.shelerks);
            //player2 = MediaPlayer.create(getContext(),R.raw.knocking);
        }
        //player.start();
        //player2.start();
        //player.setLooping(true);

    }
    public void stop(){
        //player.stop();
        //player2.stop();
    }
}