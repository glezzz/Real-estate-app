import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.project.dtttest.R
import com.project.dtttest.databinding.ItemHouseBinding
import com.project.dtttest.model.HouseResponse

class HouseAdapter(val houses: List<HouseResponse>) : RecyclerView.Adapter<HouseAdapter.HouseViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HouseViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return HouseViewHolder(layoutInflater.inflate(R.layout.item_house, parent, false))
    }

    override fun onBindViewHolder(holder: HouseViewHolder, position: Int) {
        holder.bind(houses[position])
    }

    override fun getItemCount(): Int = houses.size

    class HouseViewHolder(view: View): RecyclerView.ViewHolder(view) {

        private val binding = ItemHouseBinding.bind(view)

        fun bind(house: HouseResponse) {
            binding.tvPrice.text = house.price.toString()
            binding.tvBedrooms.text = house.bedrooms.toString()
            binding.tvZipcode.text = house.zip
            binding.tvCity.text = house.city
            Glide.with(itemView).load(house.image).into(binding.ivHouse)
        }
    }
}
