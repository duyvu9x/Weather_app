package edu.uw.tcss450.dmfriedrich.team_5_tcss_450.ui.home;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Objects;

import edu.uw.tcss450.dmfriedrich.team_5_tcss_450.R;
import edu.uw.tcss450.dmfriedrich.team_5_tcss_450.databinding.FragmentHomeBinding;
import edu.uw.tcss450.dmfriedrich.team_5_tcss_450.model.UserInfoViewModel;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    @SuppressLint("StringFormatInvalid")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {


        ViewModelProvider provider = new ViewModelProvider(getActivity());
        UserInfoViewModel userInfo = provider.get(UserInfoViewModel.class);

        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        //Comment for pushing
        binding = FragmentHomeBinding.bind(requireView());
        View root = binding.getRoot();
        //final TextView textView = binding.homeWeather;
        final ImageView logo = binding.homeLogo;
        final HorizontalScrollView recentChats = binding.recentChatScroller;
        final RecyclerView homeNotifications = binding.homeNotifications;
        final TextView textHello = binding.textHello;

        textHello.setText("Welcome " + userInfo.getEmail());

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}