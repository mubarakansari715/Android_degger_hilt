package com.mubarak.android_degger_hilt.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mubarak.android_degger_hilt.databinding.CustomViewBinding
import com.mubarak.android_degger_hilt.home.model.HomeDataClass

class HomeAdapter(
    private val list: List<HomeDataClass>,
    private val clickOnItem: (HomeDataClass?) -> (Unit)
) :
    RecyclerView.Adapter<HomeAdapter.HomeAdapterViewHolder>() {
    class HomeAdapterViewHolder(private val binding: CustomViewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: HomeDataClass) {
            binding.apply {
                model = data
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeAdapterViewHolder {
        return HomeAdapterViewHolder(
            CustomViewBinding.inflate(
                LayoutInflater.from(
                    parent.context
                ),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: HomeAdapterViewHolder, position: Int) {
        holder.bind(list[position])

        holder.itemView.setOnClickListener {
            clickOnItem.invoke(list[position])
        }
    }

    override fun getItemCount(): Int = list.size

}