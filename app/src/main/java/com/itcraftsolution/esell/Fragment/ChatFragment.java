package com.itcraftsolution.esell.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.itcraftsolution.esell.Adapter.UserAdapter;
import com.itcraftsolution.esell.Extra.LoadingDialog;
import com.itcraftsolution.esell.Model.Chatlist;
import com.itcraftsolution.esell.Model.User;
import com.itcraftsolution.esell.R;
import com.itcraftsolution.esell.databinding.FragmentChatBinding;

import java.util.ArrayList;
import java.util.List;


public class ChatFragment extends Fragment {


    public ChatFragment() {
        // Required empty public constructor
    }


    private FragmentChatBinding binding;
    private UserAdapter userAdapter;
    private List<User> mUsers;
    private FirebaseUser fuser;
    private DatabaseReference reference;
    private List<Chatlist> usersList;
    private LoadingDialog loadingDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentChatBinding.inflate(getLayoutInflater());

        loadingDialog = new LoadingDialog(requireActivity());
        binding.igBackToHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction();
                fragmentTransaction.remove(ChatFragment.this);
                fragmentTransaction.setCustomAnimations(R.anim.enter_from_rigth, R.anim.enter_from_rigth);
                fragmentTransaction.replace(R.id.frMainContainer, new HomeFragment())
                        .addToBackStack(null).commit();

            }
        });
        binding.rvChatBuying.setHasFixedSize(true);
        binding.rvChatBuying.setLayoutManager(new LinearLayoutManager(getContext()));
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL);
        binding.rvChatBuying.addItemDecoration(dividerItemDecoration);


        fuser = FirebaseAuth.getInstance().getCurrentUser();

        usersList = new ArrayList<>();

        reference = FirebaseDatabase.getInstance().getReference("Chatlist").child(fuser.getUid());
        loadingDialog.StartLoadingDialog();
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                usersList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Chatlist chatlist = snapshot.getValue(Chatlist.class);
                    usersList.add(chatlist);
                }
                chatList();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                loadingDialog.StopLoadingDialog();
            }
        });

//        updateToken(FirebaseInstanceId.getInstance().getToken());


        return binding.getRoot();
    }

//    private void updateToken(String token){
//        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Tokens");
//        Token token1 = new Token(token);
//        reference.child(fuser.getUid()).setValue(token1);
//    }

    private void chatList() {
        mUsers = new ArrayList<>();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mUsers.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    User user = snapshot.getValue(User.class);
                    for (Chatlist chatlist : usersList) {
                        if (user != null && user.getId() != null && chatlist != null && chatlist.getId() != null &&
                                user.getId().equals(chatlist.getId())) {
                            mUsers.add(user);

                        }
                    }
                }

                if (mUsers.isEmpty()) {
                    loadingDialog.StopLoadingDialog();
                    binding.rvChatBuying.setVisibility(View.GONE);
                    binding.llNoDataFound.setVisibility(View.VISIBLE);
                } else {
                    userAdapter = new UserAdapter(getContext(), mUsers, true);
                    loadingDialog.StopLoadingDialog();
                    binding.rvChatBuying.setAdapter(userAdapter);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                loadingDialog.StopLoadingDialog();
            }
        });
    }
}