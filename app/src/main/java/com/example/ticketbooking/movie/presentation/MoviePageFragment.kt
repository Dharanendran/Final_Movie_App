package com.example.ticketbooking.movie.presentation

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ticketbooking.MainActivity
import com.example.ticketbooking.R
import com.example.ticketbooking.databinding.FragmentMovieBinding
import com.example.ticketbooking.dependencyInjection.DependencyFactory
import com.squareup.picasso.Picasso

class MoviePageFragment : Fragment() {

    interface Navigator{
        fun onItemClickNavigator(movieViewModel:MovieItemViewModel?)
    }


    private lateinit var fragmentMovieBinding: FragmentMovieBinding
    private lateinit var viewModel: MovieRecyclerViewModel

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: MovieRecyclerViewAdapter

    var navigator:Navigator? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentMovieBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_movie, container, false)
        viewModel = ViewModelProvider(activity as MainActivity)[MovieRecyclerViewModel::class.java].apply {
                loadMovieUseCase = DependencyFactory.getInstance(this@MoviePageFragment.context as Context).getLoadMoviesUseCase()
                resources = this@MoviePageFragment.resources
                imageLoaderDelegate = { url ->
                    if (url.isNotEmpty())
                        Picasso.with(this@MoviePageFragment.context).load(url).get()
                    else
                        null
                }
            }

        recyclerView = fragmentMovieBinding.movieRecyclerView
        recyclerView.layoutManager = LinearLayoutManager(activity?.applicationContext, RecyclerView.VERTICAL, false)

        val clickEvent = object : MovieRecyclerViewAdapter.ItemClickListener {

            override fun itemOnClick(position: Int) {
                Log.v("fwefewf",navigator.toString())
                navigator?.onItemClickNavigator(viewModel.movies().value?.get(position))
            }

            override fun bookOnClick() {

            }

        }
        recyclerView.adapter = MovieRecyclerViewAdapter(
            viewModel.movies().value,
            clickEvent,
            viewLifecycleOwner,
            resources
        )

        return fragmentMovieBinding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setObservers()
    }


    override fun onResume() {
        (activity as MainActivity).let {
            it.activityMainBinding.headerLayout.visibility = View.VISIBLE
            it.activityMainBinding.bottomNavigationView.visibility = View.VISIBLE
        }
        super.onResume()

    }


    private fun setObservers() {

        viewModel.movies().observe(viewLifecycleOwner) {
            (recyclerView.adapter as MovieRecyclerViewAdapter).dataChanged(it)
        }

        viewModel.progressBarForLoadMovies().observe(viewLifecycleOwner) {
            if (it)
                fragmentMovieBinding.movieProgressBar.visibility = View.VISIBLE
            else
                fragmentMovieBinding.movieProgressBar.visibility = View.GONE
        }

        viewModel.isMovieApiCalled().observe(viewLifecycleOwner){
            if(!it)
                viewModel.loadMovies()
        }

    }


}

