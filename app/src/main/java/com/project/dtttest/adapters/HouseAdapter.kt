package com.project.dtttest.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders
import com.project.dtttest.R
import com.project.dtttest.databinding.ItemHouseBinding
import com.project.dtttest.model.HouseResponse

class HouseAdapter() :
    RecyclerView.Adapter<HouseAdapter.HouseViewHolder>() {

    private var housesLists = emptyList<HouseResponse>()

    // Click listener to HouseDetailFragment
    private var onitemClickListener: ((HouseResponse) -> Unit)? = null

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
        // val layoutInflater = LayoutInflater.from(parent.context)
        // return HouseViewHolder(layoutInflater.inflate(R.layout.item_house, parent, false))
    }

    override fun getItemCount(): Int = housesLists.size

    override fun onBindViewHolder(holder: HouseViewHolder, position: Int) {

        holder.itemView.apply {
            holder.binding.tvPrice.text = "$" + housesLists[position].price.toString()
            holder.binding.tvZipcode.text = housesLists[position].zip
            holder.binding.tvCity.text = housesLists[position].city
            holder.binding.tvBedrooms.text = housesLists[position].bedrooms.toString()
            holder.binding.tvBathrooms.text = housesLists[position].bathrooms.toString()
            holder.binding.tvSize.text = housesLists[position].size.toString()
            // Image binding
            val url: String =
                "https://intern.docker-dev.d-tt.nl" + housesLists[position].image
            val glideUrl = GlideUrl(
                url,
                LazyHeaders.Builder()
                    .addHeader("Access-Key", "98bww4ezuzfePCYFxJEWyszbUXc7dxRx")
                    .build()
            )
            Glide.with(this).load(glideUrl).into(holder.binding.ivHouse)

            holder.binding.cvHouseItem.setOnClickListener {
                findNavController().navigate(R.id.action_overviewFragment_to_houseDetailFragment)
            }
        }
    }

    fun setData(newList: List<HouseResponse>) {
        housesLists = newList
        notifyDataSetChanged()
    }

    /**
     * Sets click listener that sends you to the house details screen
     */
    fun setOnClickListener(listener: (HouseResponse) -> Unit) {
        onitemClickListener = listener
    }
}
