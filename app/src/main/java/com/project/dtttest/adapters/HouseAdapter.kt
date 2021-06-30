package com.project.dtttest.adapters

import android.location.Location
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders
import com.project.dtttest.databinding.ItemHouseBinding
import com.project.dtttest.model.HouseResponse
import com.project.dtttest.ui.fragments.OverviewFragment
import java.text.DecimalFormat
import java.util.*
import kotlin.collections.ArrayList

class HouseAdapter(private val overviewFragment: OverviewFragment) :
    RecyclerView.Adapter<HouseAdapter.HouseViewHolder>(), Filterable {

    private var housesList = emptyList<HouseResponse>()
    private var visibleHousesList = emptyList<HouseResponse>()

    private val TAG = "houses"

    inner class HouseViewHolder(val binding: ItemHouseBinding) :
        RecyclerView.ViewHolder(binding.root)

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

    private fun getItem(position: Int): HouseResponse {
        return visibleHousesList[position]
    }

    // Card click listener
    private var onItemClickListener: ((HouseResponse) -> Unit)? = null

    private var userCoordinates = overviewFragment.userCoordinates
    override fun onBindViewHolder(holder: HouseAdapter.HouseViewHolder, position: Int) {
        val house = getItem(position)
        holder.binding.apply {
            // Format price number
            val formattedPrice = DecimalFormat("#,###").format(house.price)

            tvPrice.text = "$" + formattedPrice
            tvZipcode.text = house.zip.filter { !it.isWhitespace() }
            tvCity.text = house.city
            tvBedrooms.text = house.bedrooms.toString()
            tvBathrooms.text = house.bathrooms.toString()
            tvSize.text = house.size.toString()

            // Calculate distance between house & user location
            if (userCoordinates.isNotEmpty()) {
                tvDistance.text = calculateDistance(
                    userCoordinates[userCoordinates.lastIndex - 1],
                    userCoordinates[userCoordinates.lastIndex],
                    house.latitude.toDouble(),
                    house.longitude.toDouble()
                ).toString() + " km"

            } else {
                tvDistance.text = "Need Permissions"
            }
            // Image binding
            val url: String =
                "https://intern.docker-dev.d-tt.nl" + house.image
            val glideUrl = GlideUrl(
                url,
                LazyHeaders.Builder()
                    .addHeader("Access-Key", "98bww4ezuzfePCYFxJEWyszbUXc7dxRx")
                    .build()
            )
            Glide.with(root).load(glideUrl).into(ivHouse)
            // Card click listener
            root.setOnClickListener {
                onItemClickListener?.let { it(house) }
            }
        }
    }

    /**
     * Sets data for adapter sorted in ascending order by price & initializes visibleHousesList for
     * use in search functionality
     */
    fun setData(newList: List<HouseResponse>) {
        housesList = ArrayList<HouseResponse>(newList).sortedBy { it.price }
        visibleHousesList = housesList
        notifyDataSetChanged()
    }

    /**
     * Sets the click listener for the detailed view to display
     */
    fun setOnItemClickListener(listener: (HouseResponse) -> Unit) {
        onItemClickListener = listener
    }

    /**
     * Calculates distance between user location and house coordinates
     */
    private fun calculateDistance(
        latOwnLocation: Double,
        lonOwnLocation: Double,
        latHouseLocation: Double,
        lonHouseLocation: Double
    ): Float {
        val distance = FloatArray(2)
        Location.distanceBetween(
            latOwnLocation, lonOwnLocation,
            latHouseLocation, lonHouseLocation, distance
        )

        return DecimalFormat("#.#").format(distance[0] / 1000).toFloat()
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence): FilterResults {
                val filteredList: List<HouseResponse>

                if (constraint.isNullOrEmpty()) {
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
                if (results?.values != null && (results.values as List<HouseResponse>).isNotEmpty()) {
                    visibleHousesList =
                        (results.values ?: emptyList<HouseResponse>()) as List<HouseResponse>
                    notifyDataSetChanged()
                    this@HouseAdapter.overviewFragment.binding.rlNoData.visibility = View.GONE
                    this@HouseAdapter.overviewFragment.binding.rvHouses.visibility = View.VISIBLE

                } else {
                    this@HouseAdapter.overviewFragment.binding.rlNoData.visibility = View.VISIBLE
                    this@HouseAdapter.overviewFragment.binding.rvHouses.visibility = View.GONE
                }
            }
        }
    }
}
