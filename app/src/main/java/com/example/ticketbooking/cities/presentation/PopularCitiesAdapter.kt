package com.example.ticketbooking.cities.presentation

import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView
import com.example.ticketbooking.databinding.PopularCitiesItemViewBinding

class PopularCitiesAdapter(
    private val popularCities: MutableList<PopularCitiesItemViewModel>,
    private val lifeCycleOwner: LifecycleOwner
) : RecyclerView.Adapter<PopularCitiesAdapter.ViewHolder>() {


    private val nonFilteredList = mutableListOf<PopularCitiesItemViewModel>().apply { addAll(popularCities) }
    private var previouslyClickedItemViewModel: PopularCitiesItemViewModel? = null

    inner class ViewHolder( val binding: PopularCitiesItemViewBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(viewModel: PopularCitiesItemViewModel) {

            binding.viewModel = viewModel

            viewModel.name.observe(lifeCycleOwner) {
                binding.cityName.text = it
            }

            viewModel.image.observe(lifeCycleOwner) {
                binding.cityImage.setImageDrawable(it)
            }

            viewModel.isSelectedMarkVisible.observe(lifeCycleOwner) {
                binding.checkMark.visibility = if (it) View.VISIBLE else View.GONE
            }

            binding.root.setOnClickListener {
                previouslyClickedItemViewModel?.let {
                    it.isSelectedMarkVisible.value = false
                }
                previouslyClickedItemViewModel = popularCities[adapterPosition].apply {
                    isSelectedMarkVisible.value = true
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = PopularCitiesItemViewBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return popularCities.size
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(popularCities[position])
    }


    override fun onViewRecycled(holder: ViewHolder) {
        holder.apply {
            binding.viewModel?.name?.removeObservers(lifeCycleOwner)
            binding.viewModel?.image?.removeObservers(lifeCycleOwner)
        }
        super.onViewRecycled(holder)
    }


    fun updateCities(cities: MutableList<PopularCitiesItemViewModel>)
    {
        popularCities.clear()
        popularCities.addAll(cities)
        nonFilteredList.addAll(cities)
        notifyDataSetChanged()
    }


    fun searchQuery(query: String) {

        val trimmedQuery = query.trim()
        if (trimmedQuery.isNotEmpty())
        {
            val filteredList = mutableListOf<PopularCitiesItemViewModel>()
            nonFilteredList.forEach {
                it.name.value?.let { name ->
                    if (name.lowercase().contains(trimmedQuery.lowercase()))
                        filteredList.add(it)
                }
            }
            val oldSize = popularCities.size
            popularCities.clear()
            popularCities.addAll(filteredList)
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
        else
        {
            val oldSize = popularCities.size
            popularCities.clear()
            popularCities.addAll(nonFilteredList)
            notifyItemRangeChanged(0, popularCities.size)
        }
    }

}


class PopularCitiesItemViewModel : ViewModel() {
    val name by lazy { MutableLiveData<String>() }
    val image by lazy { MutableLiveData<Drawable>() }
    val isSelectedMarkVisible by lazy { MutableLiveData<Boolean>(false) }
}
