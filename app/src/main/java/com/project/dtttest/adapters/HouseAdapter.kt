package com.project.dtttest.adapters

import android.location.Location
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders
import com.project.dtttest.databinding.ItemHouseBinding
import com.project.dtttest.model.HouseResponse
import com.project.dtttest.ui.fragments.OverviewFragment
import java.text.DecimalFormat

class HouseAdapter(overviewFragment: OverviewFragment) :
    RecyclerView.Adapter<HouseAdapter.HouseViewHolder>()/*, Filterable*/ {

    private var housesList = emptyList<HouseResponse>()

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

    override fun getItemCount(): Int = housesList.size

    // Card click listener
    private var onItemClickListener: ((HouseResponse) -> Unit)? = null

    private var userCoordinates = overviewFragment.userCoordinates
    override fun onBindViewHolder(holder: HouseViewHolder, position: Int) {
        val house = housesList[position]
        holder.itemView.apply {

            val formattedPrice = DecimalFormat("#,###").format(
                housesList[position].price
            )
            holder.binding.tvPrice.text = "$" + formattedPrice
            holder.binding.tvZipcode.text = housesList[position].zip
            holder.binding.tvCity.text = housesList[position].city
            holder.binding.tvBedrooms.text = housesList[position].bedrooms.toString()
            holder.binding.tvBathrooms.text = housesList[position].bathrooms.toString()
            holder.binding.tvSize.text = housesList[position].size.toString()
            // Calculate distance between house & user location
            if (userCoordinates.isNotEmpty()) {
                holder.binding.tvDistance.text = calculateDistance(
                    userCoordinates[0],
                    userCoordinates[1],
                    housesList[position].latitude.toDouble(),
                    housesList[position].longitude.toDouble()
                ).toString() + " km"

            } else {
                holder.binding.tvDistance.text = "Need Permissions"
            }
            // Image binding
            val url: String =
                "https://intern.docker-dev.d-tt.nl" + housesList[position].image
            val glideUrl = GlideUrl(
                url,
                LazyHeaders.Builder()
                    .addHeader("Access-Key", "98bww4ezuzfePCYFxJEWyszbUXc7dxRx")
                    .build()
            )
            Glide.with(this).load(glideUrl).into(holder.binding.ivHouse)
            // Card click listener
            setOnClickListener {
                onItemClickListener?.let { it(house) }
            }
        }
    }

    fun setData(newList: List<HouseResponse>) {
        housesList = newList
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
}
