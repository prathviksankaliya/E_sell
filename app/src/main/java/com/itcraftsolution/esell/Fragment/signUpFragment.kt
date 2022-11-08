package com.itcraftsolution.esell.Fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.itcraftsolution.esell.Api.ApiUtilities
import com.itcraftsolution.esell.MainActivity
import com.itcraftsolution.esell.Model.ResponceModel
import com.itcraftsolution.esell.Model.UserModel
import com.itcraftsolution.esell.R
import com.itcraftsolution.esell.databinding.FragmentSignInBinding
import com.itcraftsolution.esell.databinding.FragmentSignUpBinding
import com.itcraftsolution.esell.spf.SpfUserData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.HashMap

class signUpFragment : Fragment() {

    fun signInFragment() {
        // Required empty public constructor
    }
    var binding : FragmentSignUpBinding? = null
    private lateinit var auth: FirebaseAuth


    private fun signInWithEmail(email: String, password: String){
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                StoreUserDetails(email)
                val fragmentTransaction = parentFragmentManager.beginTransaction()
                fragmentTransaction.remove(this@signUpFragment)
                fragmentTransaction.setCustomAnimations(R.anim.enter_from_rigth, R.anim.enter_from_rigth)
                val fragment = UserProfileFragment()
                val bundle = Bundle()
                bundle.putString("mail", email)
                fragment.arguments = bundle
                fragmentTransaction.replace(R.id.frUserDetailsContainer, fragment)
                fragmentTransaction.addToBackStack(null).commit()
            } else {
                Toast.makeText(activity, "Sign in Failed", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun StoreUserDetails(email: String) {
        val spfUserData = SpfUserData(requireContext())
        spfUserData.setSpf(0,null , email, null, null, null, null, null, 0, null)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentSignUpBinding.inflate(layoutInflater)

        auth = FirebaseAuth.getInstance()

        binding!!.btnSSigned.setOnClickListener {
            if (binding!!.etSEmailAddress.text.toString().isNotEmpty() && binding!!.etSPassword.text.toString().isNotEmpty()){
                var email = binding!!.etSEmailAddress.text.toString()
                var password = binding!!.etSPassword.text.toString()
                signInWithEmail(email, password)
            }
        }


        return binding!!.root
    }

}