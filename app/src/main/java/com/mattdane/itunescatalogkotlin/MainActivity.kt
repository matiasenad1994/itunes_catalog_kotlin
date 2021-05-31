package com.mattdane.itunescatalogkotlin

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.mattdane.itunescatalogkotlin.adapter.ArtistRecyclerView
import com.mattdane.itunescatalogkotlin.databinding.ActivityMainBinding
import com.mattdane.itunescatalogkotlin.model.ArtistModel
import com.mattdane.itunescatalogkotlin.model.ListViewModel
import com.mattdane.itunescatalogkotlin.model.SearchResponse
import java.util.*

class MainActivity : AppCompatActivity(), ArtistRecyclerView.Callback{

    // view binding for the activity
    private var _binding : ActivityMainBinding? = null
    private val binding get() = _binding!!
    private val artistAdapter = ArtistRecyclerView(arrayListOf(), this, this)
    lateinit var viewModel: ListViewModel
    var mediaData : String? = null
    var countryData : String? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

    

        val categories: MutableList<String> = ArrayList()
        categories.add("all")
        categories.add("movie")
        categories.add("podcast")
        categories.add("music")
        categories.add("musicVideo")
        categories.add("audiobook")
        categories.add("shortFilm")
        categories.add("tvShow")
        categories.add("software")
        categories.add("ebook")


        val country: MutableList<String> = ArrayList()
        country.add("au")
        country.add("nz")
        country.add("ph")
        country.add("us")


        val mediaAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categories)
        binding.mediaSpinner.adapter = mediaAdapter

        val countryAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, country)
        binding.countrySpinner.adapter = countryAdapter


        binding.mediaSpinner.onItemSelectedListener = object :AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                mediaData = parent?.getItemAtPosition(position).toString()

            }

        }
        binding.countrySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                 countryData = parent?.getItemAtPosition(position).toString()
            }

        }


        viewModel = ViewModelProvider(this).get(ListViewModel::class.java)

        binding.searchBTN.setOnClickListener(View.OnClickListener {
            viewModel.searchArtist(binding.searchEDT.text.toString().trim(), mediaData.toString().trim(), countryData.toString().trim())
        })




        binding.artistRV.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = artistAdapter

        }

        viewModel.itunes.observe(this, Observer{itunes ->
            itunes?.let { artistAdapter.updateData(it.results) }

      })

    }

    override fun onItemClick(model: ArtistModel) {
        var previewURL:String? = model.previewUrl
        var trackName:String? = model.trackName
        var artisName:String? = model.artistName
        var description:String? = model.longDescription

        val i = Intent(this, ArtistDetailsActivity::class.java)
        i.putExtra("video", previewURL)
        i.putExtra("name", artisName)
        i.putExtra("trackName", trackName)
        i.putExtra("desc", description)
        startActivity(i)
    }




}