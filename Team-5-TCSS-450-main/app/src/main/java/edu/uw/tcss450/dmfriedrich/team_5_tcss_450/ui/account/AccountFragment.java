package edu.uw.tcss450.dmfriedrich.team_5_tcss_450.ui.account;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintLayoutStates;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import edu.uw.tcss450.dmfriedrich.team_5_tcss_450.R;
import edu.uw.tcss450.dmfriedrich.team_5_tcss_450.databinding.FragmentAccountBinding;
import edu.uw.tcss450.dmfriedrich.team_5_tcss_450.model.UserInfoViewModel;

public class AccountFragment extends Fragment {

    private ViewModelProvider provider;
    private RecyclerView mRecyclerView;
    private AccountAdapter mAdapter;
    private AccountViewModel mAccountModel;
    private TextView mContact;
    private ConstraintLayout addLayout;
    private Button contactButton;
    private Button addButton;
    private Button cancelButton;
    private EditText inputText;

    private List<AccountProfile> mDataset = new ArrayList<>();

    private FragmentAccountBinding binding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_account, container, false);

        provider = new ViewModelProvider(getActivity());
        addLayout = rootView.findViewById(R.id.AddLayout);
        addLayout.setVisibility(View.GONE);
        addButton = rootView.findViewById(R.id.addButton);
        cancelButton = rootView.findViewById(R.id.cancelButton);
        contactButton = rootView.findViewById(R.id.contactButton);
        mAccountModel = provider.get(AccountViewModel.class);
        inputText = rootView.findViewById(R.id.edit_email3);

        mRecyclerView = rootView.findViewById(R.id.recyclerView);
        mContact = rootView.findViewById(R.id.text_contact);



        contactButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {addLayout.setVisibility(View.VISIBLE);}
                                         });

        cancelButton.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View view) {addLayout.setVisibility(View.GONE);}
                                                 });


        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        UserInfoViewModel userInfo = provider.get(UserInfoViewModel.class);
        //mAccountModel.getContacts(userInfo.getUsername(), userInfo.getmJwt());
        addButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String username = inputText.getText().toString();
                mAccountModel.sendContact(userInfo.getUsername(),
                        userInfo.getmJwt(), username);
                        mDataset.add(new AccountProfile(username, 1));
                mAdapter.notifyItemInserted(mDataset.size() - 1);
                inputText.setText("");
                addLayout.setVisibility(View.GONE);
            }
        });
        mContact.setText("Contacts for " + userInfo.getEmail());

        mAdapter = new AccountAdapter(mDataset);
        mRecyclerView.setAdapter(mAdapter);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}
