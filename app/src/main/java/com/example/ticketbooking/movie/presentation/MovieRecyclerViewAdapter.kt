package com.example.ticketbooking.movie.presentation

import android.content.res.Resources
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.text.Editable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.example.ticketbooking.R
import com.example.ticketbooking.databinding.FragmentMovieRecyclerViewItemBinding

class MovieRecyclerViewAdapter(private var movieList:List<MovieItemViewModel>?, private val clickListener: ItemClickListener, private val lifecycleOwner: LifecycleOwner, private val resources: Resources): RecyclerView.Adapter<MovieRecyclerViewAdapter.ViewHolder>(){


    interface ItemClickListener{
        fun itemOnClick(position:Int)
        fun bookOnClick()
    }

    private lateinit var binding:FragmentMovieRecyclerViewItemBinding

    inner class ViewHolder(private var  binding:FragmentMovieRecyclerViewItemBinding): RecyclerView.ViewHolder(binding.root)
    {

        fun bindItem(itemViewModel: MovieItemViewModel)
        {
            binding.viewModel = itemViewModel
            setObservers(binding)

            binding.root.setOnClickListener {
                val position = adapterPosition
                clickListener.itemOnClick(position)
            }
            binding.buttonBook.setOnClickListener {
                clickListener.bookOnClick()
            }

        }

        private fun setObservers(binding:FragmentMovieRecyclerViewItemBinding)
        {

            binding.viewModel?.poster?.observe(lifecycleOwner){
                binding.moviePoster.setImageDrawable(BitmapDrawable(resources, it))
            }
            binding.viewModel?.title?.observe(lifecycleOwner){
                binding.movieName.setText(it)
            }
            binding.viewModel?.heroName?.observe(lifecycleOwner){
                binding.castNames.setText(it)
            }
            binding.viewModel?.heroineName?.observe(lifecycleOwner){
                val oldText = binding.castNames.text
                val text = "$oldText | $it"
                binding.castNames.setText(text)
            }
            binding.viewModel?.villainName?.observe(lifecycleOwner){
                val oldText = binding.castNames.text
                val text = "$oldText | $it"
                binding.castNames.setText(text)
            }

            binding.viewModel?.language?.observe(lifecycleOwner){
                binding.movieLanguage.setText(it)
            }


        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val inflater = LayoutInflater.from(parent.context)
        binding  = FragmentMovieRecyclerViewItemBinding.inflate(inflater,parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return movieList?.size?:0
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val viewModel = movieList?.get(position)
        if (viewModel != null) {
            holder.bindItem(viewModel)
        }
    }

    fun dataChanged(movieList:List<MovieItemViewModel>)
    {
        this.movieList = movieList
        notifyDataSetChanged()
    }

}