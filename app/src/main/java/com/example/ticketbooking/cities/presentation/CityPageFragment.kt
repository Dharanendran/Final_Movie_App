package com.example.ticketbooking.cities.presentation

import android.content.Context
import android.content.res.Resources.Theme
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.appcompat.widget.SearchView
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ticketbooking.MainActivity
import com.example.ticketbooking.R
import com.example.ticketbooking.databinding.FragmentCitiesBinding
import com.example.ticketbooking.dependencyInjection.DependencyFactory
import com.example.ticketbooking.sharedPreference
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CityPageFragment : Fragment() {

    interface Navigator {
        fun itemOnClickNavigate()
    }

    enum class SharedPreferencesKey(val key:String)
    {
        IS_POPULAR_CITY_INSERTED("isPopularCitiesInserted");
    }

    private lateinit var context:Context
    var navigator:Navigator? = null

    private lateinit var binding: FragmentCitiesBinding
    private lateinit var viewModel: CitiesPageViewModel

    private lateinit var popularCitiesRecyclerView: RecyclerView
    private lateinit var citiesRecyclerView: RecyclerView

    private lateinit var popularCitiesAdapter: PopularCitiesAdapter
    private lateinit var citiesAdapter: CitiesAdapter

    private lateinit var popularCitiesDummyData: MutableList<PopularCitiesItemViewModel>



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        binding = FragmentCitiesBinding.inflate(inflater, container, false)

        popularCitiesRecyclerView = binding.popularCities
        citiesRecyclerView = binding.cities

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let {
                    popularCitiesAdapter.searchQuery(it)
                    citiesAdapter.searchQuery(it)
                }
                return true
            }

        })

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        context = activity?.applicationContext!!


        viewModel = ViewModelProvider(this)[CitiesPageViewModel::class.java].apply {
            loadCitiesUseCase =
                DependencyFactory.getInstance(activity?.applicationContext as Context)
                    .getLoadCitiesUseCase()
            loadPopularCitiesUseCase =
                DependencyFactory.getInstance(activity?.applicationContext as Context)
                    .getLoadPopularCitesUseCase()
            insertPopularCitiesUseCase =
                DependencyFactory.getInstance(activity?.applicationContext as Context)
                    .getInsertPopularCitiesUseCase()
        }

        setPopularCitiesData()
        setObservers()

        citiesAdapter = CitiesAdapter(
            viewModel.cities().value ?: mutableListOf<CitiesItemViewModel>(),
            this.viewLifecycleOwner
        )
        citiesRecyclerView.layoutManager = LinearLayoutManager(this.context, RecyclerView.VERTICAL, false)
        citiesRecyclerView.adapter = citiesAdapter


        popularCitiesAdapter = PopularCitiesAdapter(
            viewModel.popularCities().value ?: mutableListOf(),
            this.viewLifecycleOwner
        )
        popularCitiesRecyclerView.layoutManager = GridLayoutManager(this.context, 4)
        popularCitiesRecyclerView.adapter = popularCitiesAdapter



        viewModel.loadCities()

        if(sharedPreference?.getBoolean(SharedPreferencesKey.IS_POPULAR_CITY_INSERTED.key, false) == false){
            viewModel.insertPopularCities(popularCitiesDummyData) //have to set loading
            sharedPreference!!.edit().putBoolean(SharedPreferencesKey.IS_POPULAR_CITY_INSERTED.key, true).apply()
            Log.v("asdd","ss")
        }
        viewModel.loadPopularCities()

        binding.viewModel = viewModel



    }

    override fun onResume() {
        viewModel.startSearchBarHintAnim()
        (activity as MainActivity).let {
            it.activityMainBinding.headerLayout.visibility = View.GONE
            it.activityMainBinding.bottomNavigationView.visibility = View.GONE
            it.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        }
        super.onResume()
    }


    override fun onPause() {
        viewModel.loadingAnimJob?.cancel()
        (activity as MainActivity).let {
            it.activityMainBinding.headerLayout.visibility = View.VISIBLE
            it.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        }
        super.onPause()
    }


    private fun setObservers() {

        viewModel.isProgressBarForLoadCityVisible().observe(this.viewLifecycleOwner)
        {

            if (it)
                binding.citiesLoadingProgressBar.visibility = View.VISIBLE
            else
                binding.citiesLoadingProgressBar.visibility = View.GONE
        }

        viewModel.isProgressBarForLoadPopularCityVisible().observe(this.viewLifecycleOwner)
        {

            if (it)
                binding.popularCitiesLoadingProgressBar.visibility = View.VISIBLE
            else
                binding.popularCitiesLoadingProgressBar.visibility = View.GONE
        }

        viewModel.searchViewHint().observe(viewLifecycleOwner) {
            binding.searchView.queryHint = it
        }

        viewModel.cities().observe(this.viewLifecycleOwner) {
            it?.let { citiesAdapter.updateCities(it) }
        }
        viewModel.popularCities().observe(this.viewLifecycleOwner) {
            it?.let { popularCitiesAdapter.updateCities(it) }
        }

    }

    private fun setPopularCitiesData() {

        popularCitiesDummyData = mutableListOf<PopularCitiesItemViewModel>().apply {

            add(PopularCitiesItemViewModel().apply {
                name.value = "Bangalore"
                image.value = context.resources.getDrawable(R.drawable.bangalore_image)
//                image.value = ResourcesCompat.getDrawable((activity as MainActivity).resources, R.drawable.bangalore_image, null )

            })
            add(PopularCitiesItemViewModel().apply {
                name.value = "Chennai"
                image.value = context.resources.getDrawable(R.drawable.chennai_image)
//                image.value = ResourcesCompat.getDrawable((activity as MainActivity).resources, R.drawable.chennai_image, null )
            })
            add(PopularCitiesItemViewModel().apply {
                name.value = "Chandigar"
                image.value = context.resources.getDrawable(R.drawable.chandigar_image)
//                image.value = ResourcesCompat.getDrawable((activity as MainActivity).resources, R.drawable.chandigar_image, null ) })
            })
            add(PopularCitiesItemViewModel().apply {
                name.value = "Delhi"
                image.value = context.resources.getDrawable(R.drawable.delhi_image)
//                image.value = ResourcesCompat.getDrawable((activity as MainActivity).resources, R.drawable.delhi_image, null )
            })
            add(PopularCitiesItemViewModel().apply {
                name.value = "Hyderabad"
                image.value = context.resources.getDrawable(R.drawable.hyderabad_image)
//                image.value = ResourcesCompat.getDrawable((activity as MainActivity).resources, R.drawable.hyderabad_image, null )
            })
            add(PopularCitiesItemViewModel().apply {
                name.value = "Kochin"
                image.value = context.resources.getDrawable(R.drawable.kochin_image)
//                image.value = ResourcesCompat.getDrawable((activity as MainActivity).resources, R.drawable.kochin_image, null )
            })
            add(PopularCitiesItemViewModel().apply {
                name.value = "Mumbai"
                image.value = context.resources.getDrawable(R.drawable. mumbai_image)
//                image.value = ResourcesCompat.getDrawable((activity as MainActivity).resources, R.drawable.mumbai_image, null )
            })

        }
    }

}

