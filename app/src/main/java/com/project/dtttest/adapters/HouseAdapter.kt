package com.project.dtttest.adapters

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders
import com.project.dtttest.R
import com.project.dtttest.databinding.ItemHouseBinding
import com.project.dtttest.model.HouseResponse
import java.text.DecimalFormat

class HouseAdapter :
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

    private var onItemClickListener: ((HouseResponse) -> Unit)? = null

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
}
