package com.example.ticketbooking.cities.presentation

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView
import com.example.ticketbooking.databinding.CitiesItemViewBinding
import androidx.lifecycle.LifecycleOwner

class CitiesAdapter(private val cities:MutableList<CitiesItemViewModel>, private val lifecycle:LifecycleOwner): RecyclerView.Adapter<CitiesAdapter.ViewHolder>(){

    private val nonFilteredList = mutableListOf<CitiesItemViewModel>().apply {  addAll(cities) }
    private var previouslyClickedItemViewModel:CitiesItemViewModel? = null

    inner class ViewHolder( val binding: CitiesItemViewBinding):RecyclerView.ViewHolder(binding.root)
    {
        
        fun bind(viewModel:CitiesItemViewModel)
        {
            binding.viewModel = viewModel

            viewModel.name.observe(lifecycle){
                binding.cityNameTextView.text = it
            }

            viewModel.isSelectedMarkVisible.observe(lifecycle){
                binding.checkMark.visibility = if(it) View.VISIBLE else View.GONE
            }


            binding.root.setOnClickListener {

                if(binding.viewModel != previouslyClickedItemViewModel)
                {
                    previouslyClickedItemViewModel?.let { viewModel ->
                        viewModel.isSelectedMarkVisible.value = false
                    }
                    previouslyClickedItemViewModel = binding.viewModel!!.apply {
                        isSelectedMarkVisible.value = true
                    }
                }
            }
        }

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
    {
        val inflater = LayoutInflater.from(parent.context)
        val binding = CitiesItemViewBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return cities.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(cities[position])
    }

    override fun onViewRecycled(holder: ViewHolder) {
        holder.apply{
            binding.viewModel?.name?.removeObservers(lifecycle)
            binding.viewModel?.isSelectedMarkVisible?.removeObservers(lifecycle)
        }
        super.onViewRecycled(holder)
    }

    fun updateCities(cities:MutableList<CitiesItemViewModel>){

        this.cities.clear()
        this.cities.addAll(cities)
        this.nonFilteredList.addAll(cities)
        notifyDataSetChanged()
    }

    fun searchQuery(query: String) {
        val trimmedQuery = query.trim()
        if (trimmedQuery.isNotEmpty() && cities.size!=0)
        {
            val filteredList = mutableListOf<CitiesItemViewModel>()
            nonFilteredList.forEach {
                it.name.value?.let{ name ->
                    if(name.lowercase().contains(trimmedQuery.lowercase()))
                        filteredList.add(it)
                }
            }
            val oldSize = cities.size
            cities.clear()
            cities.addAll(filteredList)
            if(oldSize >= filteredList.size)
            {
                notifyItemRangeRemoved(0,oldSize-filteredList.size)
                notifyItemRangeChanged(0,filteredList.size)
            }
            else
            {
                notifyItemRangeChanged(0,oldSize)
                notifyItemRangeInserted(oldSize-1,filteredList.size-oldSize)

            }

        }
        else {
            val oldSize = cities.size
            cities.clear()
            cities.addAll(nonFilteredList)
            notifyItemRangeChanged(0,cities.size)
        }
    }
}

class CitiesItemViewModel: ViewModel()
{
    val name by lazy{ MutableLiveData<String>() }
    val isSelectedMarkVisible by lazy{ MutableLiveData<Boolean>( false )}
}
