package edu.uw.tcss450.dmfriedrich.team_5_tcss_450.ui.roomlist;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import edu.uw.tcss450.dmfriedrich.team_5_tcss_450.R;
import edu.uw.tcss450.dmfriedrich.team_5_tcss_450.databinding.ChatroomCardBinding;

import edu.uw.tcss450.dmfriedrich.team_5_tcss_450.ui.chat.ChatFragment;

public class RoomListAdapter extends RecyclerView.Adapter<RoomListAdapter.RoomViewHolder> {

    private List<ChatFragment> mRooms;

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textView;
        private final Button button;
        public ChatroomCardBinding binding;

        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View
            binding = ChatroomCardBinding.bind(view);
            button = view.findViewById(R.id.button_view_room);
            textView = view.findViewById(R.id.text_title);;
        }

        public TextView getTextView() {
            return textView;
        }
    }

    public RoomListAdapter(List<ChatFragment> dataset) {
        mRooms = dataset;
    }


    // Create new views (invoked by the layout manager)
    @Override
    public RoomViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        return new RoomViewHolder(LayoutInflater
                .from(viewGroup.getContext())
                .inflate(R.layout.chatroom_card, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RoomViewHolder holder, int position) {
        holder.binding.textTitle.setText("Chatroom #" + String.valueOf((mRooms.get(position).getChatId())));
        holder.setRoom(mRooms.get(position).getChatId());

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mRooms.size();
    }




    public class RoomViewHolder extends RecyclerView.ViewHolder {

        public final View mView;
        public ChatroomCardBinding binding;
        private ChatFragment mChat;
        public RoomViewHolder(View view) {
            super(view);
            mView = view;
            binding = ChatroomCardBinding.bind(view);
        }

        void setRoom(final int chatId) {
            binding.buttonViewRoom.setOnClickListener(view -> {
                Navigation.findNavController(mView).navigate(
                        (NavDirections) RoomListFragmentDirections
                                .actionNavigationRoomListToNavigationMessaging());

            });

        }

    }

}
