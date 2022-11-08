package com.itcraftsolution.esell.Fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.itcraftsolution.esell.Api.ApiUtilities
import com.itcraftsolution.esell.MainActivity
import com.itcraftsolution.esell.Model.UserModel
import com.itcraftsolution.esell.R
import com.itcraftsolution.esell.databinding.FragmentForgotPasswordBinding
import com.itcraftsolution.esell.databinding.FragmentSignInBinding
import com.itcraftsolution.esell.spf.SpfUserData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ForgotPassword : Fragment() {

    var binding : FragmentForgotPasswordBinding? = null
    private lateinit var auth: FirebaseAuth


    private fun forgotPassword(email: String){
        auth.sendPasswordResetEmail(email).addOnCompleteListener { task ->
            if (task.isComplete){
                Toast.makeText(activity, "Rest Password Email Sent", Toast.LENGTH_SHORT).show();
                requireActivity().onBackPressed()
            }
        }.addOnFailureListener {
            Toast.makeText(activity, it.message, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentForgotPasswordBinding.inflate(layoutInflater)

        auth = FirebaseAuth.getInstance()

        binding!!.btnForgot.setOnClickListener {
            if (binding!!.etSEmailAddress.text.toString().isNotEmpty()){
                var email = binding!!.etSEmailAddress.text.toString()
                forgotPassword(email)
            }
        }

        binding!!.tvRegister.setOnClickListener {
            val fragmentTransaction = parentFragmentManager.beginTransaction()
            fragmentTransaction.setCustomAnimations(R.anim.enter_from_rigth,
                R.anim.enter_from_rigth)
            fragmentTransaction.replace(R.id.frUserDetailsContainer, signInFragment())
                .addToBackStack(null).commit()
        }


        return binding!!.root
    }

}