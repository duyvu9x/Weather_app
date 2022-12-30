package edu.uw.tcss450.dmfriedrich.team_5_tcss_450.ui.chat;

import android.app.Activity;
import android.app.Notification;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;


import edu.uw.tcss450.dmfriedrich.team_5_tcss_450.R;
import edu.uw.tcss450.dmfriedrich.team_5_tcss_450.databinding.FragmentChatBinding;
import edu.uw.tcss450.dmfriedrich.team_5_tcss_450.model.UserInfoViewModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChatFragment extends Fragment {

    //The chat ID for "global" chat
    private int chatId;
    private static final int HARD_CODED_CHAT_ID = 1;

    private ChatViewModel mChatModel;
    private UserInfoViewModel mUserModel;
    private ChatSendViewModel mSendModel;

    private ChatFragment frag;

    public ChatFragment() {
        chatId= -1;
    }

    public ChatFragment(int chatId) {
        this.chatId = chatId;
    }

    public int getChatId() {
        return chatId;
    }



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewModelProvider provider = new ViewModelProvider(getActivity());
        mUserModel = provider.get(UserInfoViewModel.class);
        mChatModel = provider.get(ChatViewModel.class);
        mChatModel.getFirstMessages(HARD_CODED_CHAT_ID, mUserModel.getmJwt());
        mSendModel = provider.get(ChatSendViewModel.class);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chat, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (chatId == -1) {
            ChatFragmentArgs args = ChatFragmentArgs.fromBundle(getActivity().getIntent().getExtras());
            chatId = args.getChatId();
        }
        FragmentChatBinding binding = FragmentChatBinding.bind(getView());

        //SetRefreshing shows the internal Swiper view progress bar. Show this until messages load
        binding.swipeContainer.setRefreshing(true);

        final RecyclerView rv = binding.recyclerMessages;
        //Set the Adapter to hold a reference to the list FOR THIS chat ID that the ViewModel
        //holds.
        rv.setAdapter(new ChatRecyclerViewAdapter(
                mChatModel.getMessageListByChatId(HARD_CODED_CHAT_ID),
                mUserModel.getEmail()));
        //Send button was clicked. Send the message via the SendViewModel
        binding.buttonSend.setOnClickListener(button -> {
            Log.d("BUTTON", "BUTTON CHECK");
            mSendModel.sendMessage(HARD_CODED_CHAT_ID,
                    mUserModel.getmJwt(),
                    binding.editMessage.getText().toString());
        });
        //when we get the response back from the server, clear the edittext
        mSendModel.addResponseObserver(getViewLifecycleOwner(), response ->
                binding.editMessage.setText(""));

        //When the user scrolls to the top of the RV, the swiper list will "refresh"
        //The user is out of messages, go out to the service and get more
        binding.swipeContainer.setOnRefreshListener(() -> {
            mChatModel.getNextMessages(HARD_CODED_CHAT_ID, mUserModel.getmJwt());
        });

        mChatModel.addMessageObserver(HARD_CODED_CHAT_ID, getViewLifecycleOwner(),
                list -> {
                    /*
                     * This solution needs work on the scroll position. As a group,
                     * you will need to come up with some solution to manage the
                     * recyclerview scroll position. You also should consider a
                     * solution for when the keyboard is on the screen.
                     */
                    //inform the RV that the underlying list has (possibly) changed
                    rv.getAdapter().notifyDataSetChanged();
                    rv.scrollToPosition(rv.getAdapter().getItemCount() - 1);
                    binding.swipeContainer.setRefreshing(false);
                });

    }

}