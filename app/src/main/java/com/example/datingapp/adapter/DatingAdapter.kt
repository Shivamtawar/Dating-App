package com.example.datingapp.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.datingapp.activity.ChatActivity
import com.example.datingapp.databinding.ItemUserlayoutBinding
import com.example.datingapp.model.UserModel

class DatingAdapter(val context : Context, val list : ArrayList<UserModel>) : RecyclerView.Adapter<DatingAdapter.DatingViewHolder>()  {
    
    inner class DatingViewHolder(val binding : ItemUserlayoutBinding)
        :RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DatingViewHolder {

        return DatingViewHolder(ItemUserlayoutBinding.inflate(LayoutInflater.from(context),parent,false))

    }


    override fun onBindViewHolder(holder: DatingViewHolder, position: Int) {

        holder.binding.bigtext.text = list[position].name
        holder.binding.smalltext.text = list[position].email


        Glide.with(context).load(list[position].image).into(holder.binding.userImageaCard)
        holder.binding.chatCard.setOnClickListener{
            val userID = list[position].number


            if (userID.isNullOrEmpty()) {
                Toast.makeText(context, "User ID is missing", Toast.LENGTH_SHORT).show()
            } else {
                val intent = Intent(context, ChatActivity::class.java).apply {
                    putExtra("userID", userID)
                }
                context.startActivity(intent)
            }
        }

        holder.binding.videocallCard.setOnClickListener{
            Toast.makeText(context, "videocall", Toast.LENGTH_SHORT).show()
        }

        holder.binding.heartCard.setOnClickListener{
            Toast.makeText(context, "like", Toast.LENGTH_SHORT).show()
        }


    }

    override fun getItemCount(): Int {
        return list.size
    }

}