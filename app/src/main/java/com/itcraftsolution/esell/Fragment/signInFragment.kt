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
import com.itcraftsolution.esell.databinding.FragmentSignInBinding
import com.itcraftsolution.esell.spf.SpfUserData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class signInFragment : Fragment() {

    fun signInFragment() {
        // Required empty public constructor
    }
    var binding : FragmentSignInBinding? = null
    private lateinit var auth: FirebaseAuth


    private fun signInWithEmail(email: String, password: String){
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                StoreUserDetails(email)
                val intent = Intent(context, MainActivity::class.java)
                startActivity(intent)
                requireActivity().finishAffinity()
            }
        }.addOnFailureListener {
            Toast.makeText(activity, it.message, Toast.LENGTH_SHORT).show()
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
        binding = FragmentSignInBinding.inflate(layoutInflater)

        auth = FirebaseAuth.getInstance()

        binding!!.btnSSigned.setOnClickListener {
            if (binding!!.etSEmailAddress.text.toString().isNotEmpty() && binding!!.etSPassword.text.toString().isNotEmpty()){
                var email = binding!!.etSEmailAddress.text.toString()
                var password = binding!!.etSPassword.text.toString()
                signInWithEmail(email, password)
            }
        }

        binding!!.tvRegister.setOnClickListener {
            val fragmentTransaction = parentFragmentManager.beginTransaction()
            fragmentTransaction.setCustomAnimations(R.anim.enter_from_rigth,
                R.anim.enter_from_rigth)
            fragmentTransaction.replace(R.id.frUserDetailsContainer, RegisterFragment())
                .addToBackStack(null).commit()
        }

        binding!!.forgotPass.setOnClickListener {
            val fragmentTransaction = parentFragmentManager.beginTransaction()
            fragmentTransaction.setCustomAnimations(R.anim.enter_from_rigth,
                R.anim.enter_from_rigth)
            fragmentTransaction.replace(R.id.frUserDetailsContainer, ForgotPassword())
                .addToBackStack(null).commit()
        }


        return binding!!.root
    }

}