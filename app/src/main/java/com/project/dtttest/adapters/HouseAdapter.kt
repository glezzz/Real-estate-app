package com.project.dtttest.adapters

import android.content.Context
import android.location.Location
import android.provider.Contacts
import android.util.Log
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

class HouseAdapter(private val overviewFragment: OverviewFragment, private val context: Context/*, list: ArrayList<HouseResponse>*/) :
    RecyclerView.Adapter<HouseAdapter.HouseViewHolder>(), Filterable {

    // private var searchList = ArrayList<HouseResponse>()
    // housesList = mainList
    private var housesList = emptyList<HouseResponse>()
    private var visibleHousesList = emptyList<HouseResponse>()

    // init {
    //     searchList = housesList
    // }

    // var zipAndCityList = ArrayList<String>()

    private val TAG = "houses"

    inner class HouseViewHolder(val binding: ItemHouseBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HouseViewHolder {
        // Log.d(TAG, "this is onCreateViewHolder")
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

     fun getItem(position: Int): HouseResponse {
            return visibleHousesList[position]
    }

    // Card click listener
    private var onItemClickListener: ((HouseResponse) -> Unit)? = null

    private var userCoordinates = overviewFragment.userCoordinates
    override fun onBindViewHolder(holder: HouseAdapter.HouseViewHolder, position: Int) {
        Log.d(TAG, "this is onBindViewHolder")
        val house = getItem(position)

        holder.binding.apply {
            // Format price number
            val formattedPrice = DecimalFormat("#,###").format(house.price)

            // TODO: refactor
            tvPrice.text = "$" + formattedPrice
            tvZipcode.text = house.zip
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
            // TODO: refactor
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

       // Log.d(TAG, "searchList: $searchList")
        
    }

    fun setData(newList: List<HouseResponse>) {
        housesList =  ArrayList<HouseResponse>(newList).sortedBy { it.price }
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
                    val filterPattern = constraint.toString().lowercase(Locale.ROOT).trim()
                    filteredList = housesList.filter { house ->
                        house.zip.lowercase(Locale.ROOT).contains(filterPattern) || house.city.lowercase(Locale.ROOT).contains(filterPattern)
                    }
                }

                val result = FilterResults()
                result.values = filteredList

                return result
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                if(results != null && results.values != null && (results.values as List<HouseResponse>).isNotEmpty()) {
                    visibleHousesList =
                        (results?.values ?: emptyList<HouseResponse>()) as List<HouseResponse>
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

    // override fun getFilter(): Filter {
    //     return object : Filter() {
    //         override fun performFiltering(constraint: CharSequence?): FilterResults {
    //             val charSearch = constraint.toString()
    //             zipAndCityFilterList = if (charSearch.isEmpty()) {
    //                 zipAndCityList
    //             } else {
    //                 val resultList = ArrayList<String>()
    //                 for (row in zipAndCityList) {
    //                     if (row.lowercase(Locale.ROOT)
    //                             .contains(charSearch.lowercase(Locale.ROOT))
    //                     ) {
    //                         resultList.add(row)
    //                     }
    //                 }
    //                 resultList
    //             }
    //             val filterResults = FilterResults()
    //             filterResults.values = zipAndCityFilterList
    //             return filterResults
    //         }
    //
    //         @Suppress("UNCHECKED_CAST")
    //         override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
    //             zipAndCityFilterList = results?.values as ArrayList<String>
    //             notifyDataSetChanged()
    //         }
    //     }
    // }
}
