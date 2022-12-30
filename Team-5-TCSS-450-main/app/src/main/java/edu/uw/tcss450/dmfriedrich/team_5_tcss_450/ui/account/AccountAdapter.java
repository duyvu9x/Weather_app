package edu.uw.tcss450.dmfriedrich.team_5_tcss_450.ui.account;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import edu.uw.tcss450.dmfriedrich.team_5_tcss_450.R;
import edu.uw.tcss450.dmfriedrich.team_5_tcss_450.databinding.ContactCardBinding;
import edu.uw.tcss450.dmfriedrich.team_5_tcss_450.model.UserInfoViewModel;

public class AccountAdapter extends RecyclerView.Adapter<AccountAdapter.ViewHolder> {

    private List<AccountProfile> mDataset;

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textView;
        private final Button button;
        public ContactCardBinding binding;
        private ViewModelProvider provider;

        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View
            binding = ContactCardBinding.bind(view);
            button = view.findViewById(R.id.button_full_post);
            textView = view.findViewById(R.id.text_title);
        }

        public TextView getTextView() {
            return textView;
        }
    }

    public AccountAdapter(List<AccountProfile> dataset) {
        mDataset = dataset;
    }


    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.contact_card, viewGroup, false);

        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                mDataset.remove(viewHolder.getAdapterPosition());
                notifyItemRemoved(viewHolder.getAdapterPosition());
                notifyItemRangeChanged(viewHolder.getAdapterPosition(), getItemCount());

            }
        });
        viewHolder.binding.textTitle.setText((mDataset.get(position).getNickname()));
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

}
