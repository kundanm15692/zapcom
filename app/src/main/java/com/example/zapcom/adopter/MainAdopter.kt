package com.example.zapcom.adopter

import android.content.Context
import android.graphics.Rect
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.zapcom.R
import com.example.zapcom.data.model.Item
import com.example.zapcom.data.model.ItemList

class MainAdopter(private val itemList: List<ItemList>, private val context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    companion object {
        const val sectionTypeBanner = 1
        const val sectionTypeHorizontalFreeScroll = 2
        const val sectionTypeSplitBanner = 3
    }

    override fun getItemViewType(position: Int): Int {
        val item = itemList[position]

        return when (item.sectionType) {
            "banner" -> sectionTypeBanner
            "horizontalFreeScroll" -> sectionTypeHorizontalFreeScroll
            "splitBanner" -> sectionTypeSplitBanner
            else -> throw IllegalArgumentException("Unknown section type")
        }
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {


        return when (viewType) {
            sectionTypeBanner -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.banner_rv, parent, false)
                BannerHolder(view)
            }
            sectionTypeHorizontalFreeScroll -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.horizontalfreescroll_rv, parent, false)
                HorizontalFreeScroll(view)
            }
            sectionTypeSplitBanner -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.splitbanner_rv, parent, false)
                SplitBanner(view,context)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }

    }



    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        when (holder.itemViewType) {
            sectionTypeBanner -> {

                val viewHolder = holder as BannerHolder
                viewHolder.bindData(itemList[position].items)

            }
            sectionTypeHorizontalFreeScroll -> {

                val viewHolder = holder as HorizontalFreeScroll
                viewHolder.bindData(itemList[position].items)

            }

            sectionTypeSplitBanner -> {

                val viewHolder = holder as SplitBanner
                viewHolder.bindData(itemList[position].items)
            }
        }

    }

    override fun getItemCount(): Int {
        return itemList.size
    }



    class BannerHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val recyclerView: RecyclerView = itemView.findViewById(R.id.recyclerView1)
        fun bindData(bannerItem: List<Item>) {

            val singleBannerAdapter = BannerAdapter(bannerItem)
            recyclerView.layoutManager =LinearLayoutManager(itemView.context, RecyclerView.HORIZONTAL, false)
            recyclerView.adapter = singleBannerAdapter

        }
    }

    class HorizontalFreeScroll(itemView: View) : RecyclerView.ViewHolder(itemView) {


        private val recyclerView: RecyclerView = itemView.findViewById(R.id.recyclerView1)
        fun bindData(bannerItem: List<Item>) {

            val bannerHorizontalAdapter = BannerHorizontalAdapter(bannerItem)
            recyclerView.layoutManager =LinearLayoutManager(itemView.context, RecyclerView.HORIZONTAL, false)
            recyclerView.adapter = bannerHorizontalAdapter


        }
    }

    class SplitBanner(itemView: View, val context: Context) : RecyclerView.ViewHolder(itemView) {
        private val recyclerView: RecyclerView = itemView.findViewById(R.id.recyclerView1)
        fun bindData(bannerItem: List<Item>) {

            val singleBannerAdapter = SplitBannerAdapter(bannerItem)
            val displayMetrics = context.resources.displayMetrics
            val screenWidth = displayMetrics.widthPixels
            val itemWidth = (screenWidth / 2)
            recyclerView.apply {

                layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                adapter = singleBannerAdapter
                addItemDecoration(object : RecyclerView.ItemDecoration() {
                    override fun getItemOffsets(
                        outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State
                    ) {
                        val params = view.layoutParams
                        params.width = itemWidth
                        view.layoutParams = params
                    }
                })
            }

        }
    }
}