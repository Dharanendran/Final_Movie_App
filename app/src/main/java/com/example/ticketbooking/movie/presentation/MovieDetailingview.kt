package com.example.ticketbooking.movie.presentation

import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.palette.graphics.Palette
import com.example.ticketbooking.MainActivity
import com.example.ticketbooking.R
import com.example.ticketbooking.databinding.FragmentMovieDetailViewBinding
import com.example.ticketbooking.databinding.FragmentMovieRecyclerViewItemBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*

class MovieDetailingView: Fragment() {

    interface Navigator{
        fun onClickBookNavigate()
    }

    private lateinit var fragmentMovieDetailViewBinding:FragmentMovieDetailViewBinding
    private lateinit var viewModel:MovieItemViewModel

    private lateinit var dominantColorFromPoster:ColorDrawable

    var navigator:Navigator? = null



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentMovieDetailViewBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_movie_detail_view, container, false)
        this.viewModel = arguments?.getSerializable("MovieViewViewModel") as MovieItemViewModel
        fragmentMovieDetailViewBinding.viewModel = viewModel
        setObservers(fragmentMovieDetailViewBinding)
        fragmentMovieDetailViewBinding.detailViewButtonBook.setOnClickListener {
            Toast.makeText(activity, "booked", Toast.LENGTH_SHORT).show()
        }
        return fragmentMovieDetailViewBinding.root
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        viewModel.poster.observe(viewLifecycleOwner) { bitmap ->

            Palette.from(bitmap).generate { it ->
                val dominantColor = it?.getVibrantColor(resources.getColor(R.color.black))
                val dominantHexColorFromPoster = String.format("#%06X", 0xFFFFFF and (dominantColor ?: 0))
                this@MovieDetailingView.dominantColorFromPoster = ColorDrawable(Color.parseColor(dominantHexColorFromPoster))
                fragmentMovieDetailViewBinding.view.setBackgroundColor(dominantColorFromPoster.color)

                (activity as MainActivity).let{ activity ->
                    activity.header_layout.visibility = View.GONE
                    activity.supportActionBar?.show()
                    activity.supportActionBar?.setBackgroundDrawable(this@MovieDetailingView.dominantColorFromPoster)
                    activity.supportActionBar?.elevation = 0f
                }

            }

        }

    }

    override fun onResume() {
        (activity as MainActivity).let{ it ->
            it.supportActionBar?.title = ""
            it.supportActionBar?.setDisplayShowHomeEnabled(true)
            it.findViewById<BottomNavigationView>(R.id.bottomNavigationView).visibility = View.GONE
        }
        super.onResume()
    }

    override fun onStop() {
        (activity as MainActivity).let{
            it.supportActionBar?.hide()
            it.header_layout.visibility = View.VISIBLE
            it.findViewById<BottomNavigationView>(R.id.bottomNavigationView).visibility = View.VISIBLE
        }
        super.onStop()

    }

    private fun setObservers(binding: FragmentMovieDetailViewBinding)
    {
        binding.viewModel?.poster?.observe(viewLifecycleOwner){
            binding.moviePoster.setImageDrawable(BitmapDrawable(resources, it))
        }

        binding.viewModel?.title?.observe(viewLifecycleOwner){
            binding.movieName.text = it
        }

        binding.viewModel?.heroName?.observe(viewLifecycleOwner){
            binding.castContent.text = it
        }

        binding.viewModel?.heroineName?.observe(viewLifecycleOwner){
            val oldText = binding.castContent.text
            val text = "$oldText | $it"
            binding.castContent.text = text
        }

        binding.viewModel?.villainName?.observe(viewLifecycleOwner){
            val oldText = binding.castContent.text
            val text = "$oldText | $it"
            binding.castContent.text = text
        }

        binding.viewModel?.synopsis?.observe(viewLifecycleOwner){
            binding.synopsisContent.text = it
        }

        binding.viewModel?.language?.observe(viewLifecycleOwner){
            binding.language.text = it
        }

        binding.viewModel?.director?.observe(viewLifecycleOwner){
            binding.directorContent.text = it
        }

        binding.viewModel?.musicDirector?.observe(viewLifecycleOwner){
            binding.musicDirectorContent.text = it
        }


    }
}