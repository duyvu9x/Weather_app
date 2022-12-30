package edu.uw.tcss450.dmfriedrich.team_5_tcss_450.ui.roomlist;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import edu.uw.tcss450.dmfriedrich.team_5_tcss_450.R;
import edu.uw.tcss450.dmfriedrich.team_5_tcss_450.model.UserInfoViewModel;
import edu.uw.tcss450.dmfriedrich.team_5_tcss_450.ui.account.AccountAdapter;
import edu.uw.tcss450.dmfriedrich.team_5_tcss_450.ui.account.AccountProfile;
import edu.uw.tcss450.dmfriedrich.team_5_tcss_450.ui.account.AccountViewModel;
import edu.uw.tcss450.dmfriedrich.team_5_tcss_450.ui.chat.ChatFragment;

public class RoomListFragment extends Fragment {

    private RoomListViewModel mRoomModel;
    private ViewModelProvider provider;
    private RecyclerView mRecyclerView;
    private RoomListAdapter mAdapter;

    private List<ChatFragment> mDataset = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDataset.add(new ChatFragment(1));
        mDataset.add(new ChatFragment(2));
        mDataset.add(new ChatFragment(3));

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_room_list, container, false);
        provider = new ViewModelProvider(getActivity());

        mRoomModel = provider.get(RoomListViewModel.class);

        mRecyclerView = rootView.findViewById(R.id.roomCycler);

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mAdapter = new RoomListAdapter(mDataset);
        mRecyclerView.setAdapter(mAdapter);

    }

}