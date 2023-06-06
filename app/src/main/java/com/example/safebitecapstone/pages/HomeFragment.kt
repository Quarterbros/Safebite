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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.safebitecapstone.ListAlergenAdapter
import com.example.safebitecapstone.R
import com.example.safebitecapstone.SessionPreferences
import com.example.safebitecapstone.databinding.FragmentHomeBinding
import com.example.safebitecapstone.dummyData.Alergen
import com.example.safebitecapstone.model.LoginViewModel
import com.example.safebitecapstone.model.MainViewModel
import com.example.safebitecapstone.model.factory.LoginViewModelFactory
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

    //    Apus kalo udah ada API
    private lateinit var rvAlergen: RecyclerView
    private val list = ArrayList<Alergen>()

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

        rvAlergen = binding.alergenItems

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

        binding.buttonBanyakAlergen.setOnClickListener {
            val intent = Intent(activity, EditAlergenActivity::class.java)
            startActivity(intent)
        }

        binding.buttonEdit.setOnClickListener {
            val intent = Intent(activity, EditAlergenActivity::class.java)
            startActivity(intent)
        }

        binding.buttonHalal.setOnClickListener {
            val intent = Intent(activity, EditHalalActivity::class.java)
            startActivity(intent)
        }

        list.addAll(getListAlergen())
        showRecyclerList()

        val acct = GoogleSignIn.getLastSignedInAccount(requireContext())
        if (acct != null) {
            val personName = acct.displayName
            val personGivenName = acct.givenName
            val personFamilyName = acct.familyName
            val personEmail = acct.email
            val personId = acct.id
            val personPhoto: Uri? = acct.photoUrl

//            println("personName : $personName")
//            println("personGiven : $personGivenName")
//            println("personPhoto : $personPhoto")

            binding.namaPengguna.text = personGivenName
            Glide.with(requireContext())
                .load(personPhoto)
                .into(binding.imgItemPhoto)
        }
    }

    private fun getListAlergen(): ArrayList<Alergen> {
        val dataTitle = resources.getStringArray(R.array.data_title)
        val dataDescription = resources.getStringArray(R.array.data_description)
        val dataPhoto = resources.obtainTypedArray(R.array.data_photo)

        val listAlergen = ArrayList<Alergen>()

        for (i in dataTitle.indices) {
            val alergen = Alergen(dataTitle[i], dataDescription[i], dataPhoto.getResourceId(i, -1))
            listAlergen.add(alergen)
        }
        return listAlergen
    }

    private fun showRecyclerList() {
        binding.alergenItems.layoutManager = LinearLayoutManager(activity)
        val listAlergenAdapter = ListAlergenAdapter(list)
        binding.alergenItems.adapter = listAlergenAdapter
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