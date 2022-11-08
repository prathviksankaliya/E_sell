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
import com.itcraftsolution.esell.databinding.FragmentRegisterBinding
import com.itcraftsolution.esell.databinding.FragmentSignInBinding
import com.itcraftsolution.esell.spf.SpfUserData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterFragment : Fragment() {

    fun registerFragment() {
        // Required empty public constructor
    }
    var binding : FragmentRegisterBinding? = null
    private lateinit var auth: FirebaseAuth


    private fun signUpWithEmail(email: String, password: String){
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                StoreUserDetails(email)
                val fragmentTransaction = parentFragmentManager.beginTransaction()
                fragmentTransaction.remove(this@RegisterFragment)
                fragmentTransaction.setCustomAnimations(R.anim.enter_from_rigth, R.anim.enter_from_rigth)
                val fragment = UserProfileFragment()
                val bundle = Bundle()
                bundle.putString("mail", email)
                fragment.arguments = bundle
                fragmentTransaction.replace(R.id.frUserDetailsContainer, fragment)
                fragmentTransaction.addToBackStack(null).commit()
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
        binding = FragmentRegisterBinding.inflate(layoutInflater)

        auth = FirebaseAuth.getInstance()

        binding!!.btnSSigned.setOnClickListener {
            if (binding!!.etSEmailAddress.text.toString().isNotEmpty() && binding!!.etSPassword.text.toString().isNotEmpty()){
                var email = binding!!.etSEmailAddress.text.toString()
                var password = binding!!.etSPassword.text.toString()
                signUpWithEmail(email, password)
            }
        }


        return binding!!.root
    }

}