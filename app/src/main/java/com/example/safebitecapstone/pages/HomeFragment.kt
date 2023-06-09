package com.example.safebitecapstone.pages

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.example.safebitecapstone.R
import com.example.safebitecapstone.SessionPreferences
import com.example.safebitecapstone.databinding.FragmentHomeBinding
import com.example.safebitecapstone.model.MainViewModel
import com.example.safebitecapstone.model.factory.MainViewModelFactory
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "token")
class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient


    private val mainViewModel: MainViewModel by viewModels {
        MainViewModelFactory.getInstance(requireContext(), SessionPreferences.getInstance(requireContext().dataStore))
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity?)!!.supportActionBar!!.hide()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = Firebase.auth

        val builder = AlertDialog.Builder(requireContext())
        builder.setMessage("Are you sure to log out ?")
            .setPositiveButton("Log Out",
                DialogInterface.OnClickListener { dialog, id ->
                    // START THE GAME!
                    signOut()
                    dialog.dismiss()
                })
            .setNegativeButton("Cancel",
                DialogInterface.OnClickListener { dialog, id ->
                    // User cancelled the dialog
                    dialog.cancel()
                })
        // Create the AlertDialog object and return it
        builder.create()


        binding.logoutButton.setOnClickListener {
            val builder = AlertDialog.Builder(requireContext())
            builder.setMessage("Are you sure to log out ?")
                .setPositiveButton("Log Out",
                    DialogInterface.OnClickListener { dialog, id ->
                        // START THE GAME!
                        signOut()
                        mainViewModel.logout()
                        dialog.dismiss()
                    })
                .setNegativeButton("Cancel",
                    DialogInterface.OnClickListener { dialog, id ->
                        // User cancelled the dialog
                        dialog.cancel()
                    })
            // Create the AlertDialog object and return it
            builder.create().show()
        }

        binding.buttonHowItWork.setOnClickListener {
            val intent = Intent(activity, HowItWorksActivity::class.java)
            startActivity(intent)
        }


        val acct = GoogleSignIn.getLastSignedInAccount(requireContext())
        if (acct != null) {
            val personGivenName = acct.givenName
            val personEmail = acct.email
            val personPhoto: Uri? = acct.photoUrl

            binding.namaPengguna.text = personGivenName
            binding.emailPengguna.text = personEmail

            Glide.with(requireContext())
                .load(personPhoto)
                .into(binding.imgItemPhoto)
        }
    }



    private fun signOut() {
        val gso = GoogleSignInOptions
            .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = activity?.let { GoogleSignIn.getClient(it, gso) }!!

        auth.signOut()
        googleSignInClient.signOut()
        val intent = Intent(activity, LoginActivity::class.java)
        startActivity(intent)
    }
}