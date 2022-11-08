package com.itcraftsolution.esell.Fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.itcraftsolution.esell.R;
import com.itcraftsolution.esell.databinding.FragmentAccountHelpBinding;

// Account Help Fragment
public class AccountHelpFragment extends Fragment {

    public AccountHelpFragment() {
        // Required empty public constructor
    }

    private FragmentAccountHelpBinding binding;
    private String APPNAME = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentAccountHelpBinding.inflate(getLayoutInflater());

        // Account Rate Us
        // Redirect To Play Store E-Sell App
        binding.llAccountRateUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + R.string.app_name)));
            }
        });

        //Account Invite
        //Invite Other To E-Sell
        binding.llAccountInvite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "I'M Using #E-Sell, Buying & Selling Process Much Easier: (playstore link)");
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
            }
        });

        // Account E-Sell Sell Policy
        binding.llEsellPolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction();
                fragmentTransaction.remove(AccountHelpFragment.this);
                fragmentTransaction.setCustomAnimations(R.anim.enter_from_rigth, R.anim.enter_from_rigth);
                fragmentTransaction.replace(R.id.frMainContainer, new SellinEsellFragment());
                fragmentTransaction.addToBackStack(null).commit();
            }
        });

        //Account E-Sell Service Policy
        binding.llEsellServicesPolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction();
                fragmentTransaction.remove(AccountHelpFragment.this);
                fragmentTransaction.setCustomAnimations(R.anim.enter_from_rigth, R.anim.enter_from_rigth);
                fragmentTransaction.replace(R.id.frMainContainer, new EsellPolicyFragment());
                fragmentTransaction.addToBackStack(null).commit();
            }
        });

        //Back Arrow
        // Go To Account Fragment
        binding.igHelpBackToAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction();
                fragmentTransaction.remove(AccountHelpFragment.this);
                fragmentTransaction.setCustomAnimations(R.anim.enter_from_rigth, R.anim.enter_from_rigth);
                fragmentTransaction.replace(R.id.frMainContainer, new AccountFragment());
                fragmentTransaction.addToBackStack(null).commit();
            }
        });
        return binding.getRoot();
    }
}