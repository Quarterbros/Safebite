package com.example.safebitecapstone.pages

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.safebitecapstone.R
import com.example.safebitecapstone.SessionPreferences
import com.example.safebitecapstone.databinding.FragmentHistoryBinding
import com.example.safebitecapstone.databinding.FragmentHomeBinding
import com.example.safebitecapstone.dummyData.Alergen
import com.example.safebitecapstone.helper.HistoryAdapter
import com.example.safebitecapstone.model.HistoryViewModel
import com.example.safebitecapstone.model.MainViewModel
import com.example.safebitecapstone.model.factory.HistoryViewModelFactory
import com.example.safebitecapstone.model.factory.MainViewModelFactory

class HistoryFragment : Fragment() {

    private lateinit var binding: FragmentHistoryBinding
    private lateinit var adapter: HistoryAdapter

    private val historyViewModel: HistoryViewModel by viewModels {
        HistoryViewModelFactory.getInstance(requireActivity().application)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity?)!!.supportActionBar!!.hide()

        binding.historyItems?.layoutManager = LinearLayoutManager(requireContext())
        binding.historyItems?.setHasFixedSize(true)
        binding.historyItems?.adapter = adapter
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = HistoryAdapter()
        binding.historyItems.visibility = View.VISIBLE
        binding.noDetectionText.visibility = View.GONE
        historyViewModel.getAllDetection().observe(viewLifecycleOwner){items ->
            if(items.isNotEmpty()){
                println("nilai dari items : $items")

                binding.historyItems.visibility = View.VISIBLE
                binding.noDetectionText.visibility = View.GONE
                adapter.setListDetection(items)
            }
            else{
                println("Kosong")
                binding.historyItems.visibility = View.GONE
                binding.noDetectionText.visibility = View.VISIBLE
            }
        }


        binding.historyItems?.layoutManager = LinearLayoutManager(requireContext())
        binding.historyItems?.setHasFixedSize(true)
        binding.historyItems?.adapter = adapter
    }


}