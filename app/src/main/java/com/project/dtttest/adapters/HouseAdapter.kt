package com.project.dtttest.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.project.dtttest.R
import com.project.dtttest.databinding.ItemHouseBinding
import com.project.dtttest.model.HouseResponse
import com.project.dtttest.ui.fragments.HousesOverviewFragment
import com.project.dtttest.utils.formatDistance
import com.project.dtttest.utils.formatPrice
import com.project.dtttest.utils.loadHouseImage
import java.util.*
import kotlin.collections.ArrayList

class HouseAdapter(private val overviewFragment: HousesOverviewFragment) :
    RecyclerView.Adapter<HouseAdapter.HouseViewHolder>(), Filterable {

    private var housesList = emptyList<HouseResponse>()
    private var housesListInitialized = false
    private var visibleHousesList = emptyList<HouseResponse>()

    // Card click listener
    private var onItemClickListener: ((HouseResponse) -> Unit)? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HouseViewHolder {
        return HouseViewHolder(
            ItemHouseBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return visibleHousesList.size
    }

    /**
     * Gets each individual item from the visibleHousesList
     *
     * @param position the position in the recyclerview
     * @return         the house on that position
     */
    private fun getItem(position: Int): HouseResponse {
        return visibleHousesList[position]
    }

    override fun onBindViewHolder(holder: HouseViewHolder, position: Int) {
        val house = getItem(position)
        holder.bind(house)

        holder.binding.apply {

            if (house.distance != null) {

                // If distance is not null, bind view in correct format
                tvDistance.text = formatDistance(house.distance!!)

                // Keep default textSize
                tvDistance.textSize = 10.0F

            } else {

                // Without permissions bind view with text & decrease textSize to better fit the screen
                tvDistance.text = holder.itemView.context.getString(R.string.no_permissions)
                tvDistance.textSize = 8.0F
            }

            // Card click listener
            root.setOnClickListener {
                onItemClickListener?.let { it(house) }
            }
        }
    }

    class HouseViewHolder(val binding: ItemHouseBinding) :
        RecyclerView.ViewHolder(binding.root) {

        /**
         * Binds views with corresponding field
         *
         * @param house each house that we receive from the overviewFragment
         */
        fun bind(house: HouseResponse) {
            binding.tvPrice.text = formatPrice(house.price)

            // Don't filter for whitespace
            binding.tvZipcode.text = house.zip.filter { !it.isWhitespace() }
            binding.tvCity.text = house.city
            binding.tvBedrooms.text = house.bedrooms.toString()
            binding.tvBathrooms.text = house.bathrooms.toString()
            binding.tvSize.text = house.size.toString()

            // Load house image
            Glide.with(binding.root).load(loadHouseImage(house.image)).into(binding.ivHouse)
        }
    }

    /**
     * Sets data for adapter sorted in ascending order by price & initializes visibleHousesList for use in search functionality
     *
     * @param newList   the list of houses that we receive from the viewModel observer in housesOverviewFragment
     */
    fun setData(newList: List<HouseResponse>) {
        housesList = ArrayList<HouseResponse>(newList).sortedBy { it.price }
        visibleHousesList = housesList
        housesListInitialized = true
        notifyDataSetChanged()
    }

    //TODO check Lackner's video
    /**
     * Sets up the onClickListener for the detailed view to display the house that's been clicked
     * @param listener  a listener that takes a HouseResponse as a parameter and returns nothing
     */
    fun setOnItemClickListener(listener: (HouseResponse) -> Unit) {
        onItemClickListener = listener
    }

    /**
     * Search through the list of houses via zipcode or city
     */
    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence): FilterResults {
                if (!housesListInitialized) {

                    return FilterResults()
                }

                val filteredList: List<HouseResponse>
                if (constraint.isBlank()) {
                    filteredList = housesList

                } else {
                    val filterPattern = constraint.toString().lowercase(Locale.getDefault()).trim()
                    filteredList = housesList.filter { house ->
                        house.zip.lowercase(Locale.getDefault())
                            .contains(filterPattern) || house.city.lowercase(Locale.getDefault())
                            .contains(filterPattern)
                    }
                }

                val result = FilterResults()
                result.values = filteredList

                return result
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                if (results?.values != null) {
                    if ((results.values as List<HouseResponse>).isNotEmpty()) {
                        visibleHousesList =
                            (results.values ?: emptyList<HouseResponse>()) as List<HouseResponse>
                        notifyDataSetChanged()

                        // When constraint matches results, show recyclerview with data
                        this@HouseAdapter.overviewFragment.binding.llNoData.visibility = View.GONE
                        this@HouseAdapter.overviewFragment.binding.rvHouses.visibility =
                            View.VISIBLE

                    } else {

                        // When no results are found, hide RecyclerView with data & show LinearLayout
                        // with no results image and textViews
                        this@HouseAdapter.overviewFragment.binding.llNoData.visibility =
                            View.VISIBLE
                        this@HouseAdapter.overviewFragment.binding.rvHouses.visibility = View.GONE
                    }
                }
            }
        }
    }
}