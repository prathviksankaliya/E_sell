package com.itcraftsolution.esell.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.interfaces.ItemClickListener;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.itcraftsolution.esell.Api.ApiUtilities;
import com.itcraftsolution.esell.Extra.LoadingDialog;
import com.itcraftsolution.esell.Model.Chatlist;
import com.itcraftsolution.esell.R;
import com.itcraftsolution.esell.databinding.FragmentItemDetailsBinding;
import com.itcraftsolution.esell.spf.SpfUserData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ItemDetailsFragment extends Fragment {


    public ItemDetailsFragment() {
        // Required empty public constructor
    }

    private FragmentItemDetailsBinding binding;
    private SpfUserData spf;
    private String ItemImg, ItemPrice, ItemDesc, ItemTitle, ItemLocation, ItemCat, ReceiverUid;
    private int ItemId, UserId, LoginUserId;
    private List<SlideModel> slideModels;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentItemDetailsBinding.inflate(getLayoutInflater());


        // Call Load Method
        LoadData();


        //Product Image Fragment
        binding.isProductImagesSlider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction();
                fragmentTransaction.remove(ItemDetailsFragment.this);
                fragmentTransaction.setCustomAnimations(R.anim.enter_from_rigth, R.anim.enter_from_rigth);
                fragmentTransaction.replace(R.id.frMainContainer, new ProductImageFragment())
                        .addToBackStack(null).commit();
            }
        });


        // Back Arrow
        //Go To HomeFragment
        binding.igItemDetailsBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction();
                fragmentTransaction.remove(ItemDetailsFragment.this);
                fragmentTransaction.setCustomAnimations(R.anim.enter_from_rigth, R.anim.enter_from_rigth);
                fragmentTransaction.replace(R.id.frMainContainer, new HomeFragment())
                        .addToBackStack(null).commit();
            }
        });

        //Button Chat
        binding.btnChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spf.setCreateChat(ReceiverUid);

                FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction();
                fragmentTransaction.remove(ItemDetailsFragment.this);
                fragmentTransaction.setCustomAnimations(R.anim.enter_from_rigth, R.anim.enter_from_rigth);
                fragmentTransaction.replace(R.id.frMainContainer, new ChatScreenFragment())
                        .addToBackStack(null).commit();

            }
        });

        // Button share Product
        binding.igItemDetailsShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "I've found this on #E-Sell. What do you think? (playstore link)");
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
            }
        });

//        binding.btnDiscuss.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                SpfUserData spfUserData = new SpfUserData(requireContext());
//                spfUserData.setItemDetail(ItemImg,ItemPrice,ItemTitle,ItemLocation,ItemDesc,ItemId,UserId,0,0,ItemCat,Auth_id,1);
//                FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction();
//                fragmentTransaction.remove(ItemDetailsFragment.this);
//                fragmentTransaction.setCustomAnimations(R.anim.enter_from_rigth,R.anim.enter_from_rigth);
//                fragmentTransaction.replace(R.id.frMainContainer , new DiscussFragment())
//                        .addToBackStack(null).commit();
//            }
//        });

        return binding.getRoot();
    }

    // LoadData Method
    private void LoadData() {
        spf = new SpfUserData(requireContext());
        UserId = spf.getItemDetails().getInt("UserId", 0);
        ItemImg = spf.getItemDetails().getString("ItemImg", null);
        ItemPrice = spf.getItemDetails().getString("ItemPrice", null);
        ItemTitle = spf.getItemDetails().getString("ItemTitle", null);
        ItemLocation = spf.getItemDetails().getString("ItemLocation", null);
        ItemId = spf.getItemDetails().getInt("ItemId", 0);
        ItemDesc = spf.getItemDetails().getString("ItemDesc", null);
        ItemCat = spf.getItemDetails().getString("Category", null);
        ReceiverUid = spf.getItemDetails().getString("Auth_Id", null);
        LoginUserId = spf.getSpf().getInt("UserId", 0);
        if (LoginUserId == UserId) {
            binding.btnChat.setVisibility(View.GONE);
        }

        List<String> list = new ArrayList<String>(Arrays.asList(ItemImg.split(",")));
        slideModels = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            slideModels.add(new  SlideModel(ApiUtilities.SellItemImage + list.get(i), ScaleTypes.FIT));
        }

        binding.isProductImagesSlider.setImageList(slideModels,ScaleTypes.FIT);

        binding.isProductImagesSlider.setItemClickListener(new ItemClickListener() {
            @Override
            public void onItemSelected(int i) {
                FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction();
                fragmentTransaction.remove(ItemDetailsFragment.this);
                fragmentTransaction.setCustomAnimations(R.anim.enter_from_rigth, R.anim.enter_from_rigth);
                fragmentTransaction.replace(R.id.frMainContainer, new ProductImageFragment())
                        .addToBackStack(null).commit();
            }
        });
//        Glide.with(requireContext()).load(ApiUtilities.SellItemImage + list.get(0)).into(binding.igItemDetails);
        binding.txItemDetailsLocation.setText(ItemLocation);
        binding.txItemDetailsName.setText(ItemTitle);
        binding.txItemDetailsPrice.setText(ItemPrice);
        binding.txItemDesc.setText(ItemDesc);
    }
}