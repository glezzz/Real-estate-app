package com.project.dtttest.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders
import com.project.dtttest.databinding.ItemHouseBinding
import com.project.dtttest.model.HouseResponse

class HouseAdapter(/*private val houses: List<HouseResponse>*/) :
    RecyclerView.Adapter<HouseAdapter.HouseViewHolder>() {

    private var housesLists = emptyList<HouseResponse>()

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

    override fun onBindViewHolder(holder: HouseViewHolder, position: Int) {
        holder.itemView.apply {
            holder.binding.tvPrice.text = housesLists[position].price.toString()
            holder.binding.tvBedrooms.text = housesLists[position].bedrooms.toString()
            holder.binding.tvZipcode.text = housesLists[position].zip
            holder.binding.tvCity.text = housesLists[position].city

            val url: String =
                "https://intern.docker-dev.d-tt.nl" + housesLists[position].image
            val glideUrl = GlideUrl(
                url,
                LazyHeaders.Builder()
                    .addHeader("Access-Key", "98bww4ezuzfePCYFxJEWyszbUXc7dxRx")
                    .build()
            )

            Glide.with(this).load(glideUrl).into(holder.binding.ivHouse)
        }

        // holder.binding.tvPrice.text = housesLists[position].price.toString()
        // holder.binding.tvBedrooms.text = housesLists[position].bedrooms.toString()
        // holder.binding.tvZipcode.text = housesLists[position].zip
        // holder.binding.tvCity.text = housesLists[position].city


        // holder.bind(houses[position])
    }

    override fun getItemCount(): Int = housesLists.size

    // class HouseViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    //
    //     private val binding = ItemHouseBinding.bind(view)
    //
    //     fun bind(house: HouseResponse) {
    //         binding.tvPrice.text = house.price.toString()
    //         binding.tvBedrooms.text = house.bedrooms.toString()
    //         binding.tvZipcode.text = house.zip
    //         binding.tvCity.text = house.city
    //         Glide.with(itemView).load(house.image).into(binding.ivHouse)
    //     }
    // }

    fun setData(newList: List<HouseResponse>) {
        housesLists = newList
        notifyDataSetChanged()
    }
}
