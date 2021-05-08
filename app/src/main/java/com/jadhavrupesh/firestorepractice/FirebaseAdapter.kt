package com.jadhavrupesh.firestorepractice

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jadhavrupesh.firestorepractice.FirebaseAdapter.FirebaseViewHolder
import com.jadhavrupesh.firestorepractice.databinding.SingleItemLayoutBinding
import java.util.*

class FirebaseAdapter: RecyclerView.Adapter<FirebaseViewHolder>() {

    var items: MutableList<UserModel> = mutableListOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    class FirebaseViewHolder(val binding:SingleItemLayoutBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FirebaseViewHolder {
       return FirebaseViewHolder(SingleItemLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: FirebaseViewHolder, position: Int) {
        val userDetails=items.get(position)
        holder.binding.tvName.text =userDetails.name
        holder.binding.tvSname.text =userDetails.sname
        if (userDetails.online){
            holder.binding.tvOnline.text ="Online"
            holder.binding.tvOnline.setTextColor(Color.parseColor("#56c596"))
        }else{
            holder.binding.tvOnline.text ="Offline"
            holder.binding.tvOnline.setTextColor(Color.parseColor("#801336"))
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

}