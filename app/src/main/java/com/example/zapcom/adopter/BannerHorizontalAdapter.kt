package com.example.zapcom.adopter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.zapcom.R
import com.example.zapcom.data.model.Item

class BannerHorizontalAdapter(private val items: List<Item>) : RecyclerView.Adapter<BannerHorizontalAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageView: ImageView = itemView.findViewById(R.id.iv_banner)
        private val textView: TextView = itemView.findViewById(R.id.title)
        fun bindItem(bannerItem: Item) {

            Glide.with(itemView.context)
                .load(bannerItem.image)
                .placeholder(R.drawable.ic_launcher_background)
                .into(imageView)

            textView.text = bannerItem.title
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.horizontal_row_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItem(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }
}