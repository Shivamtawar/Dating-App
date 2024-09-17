package com.example.datingapp.adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.datingapp.activity.ChatActivity
import com.example.datingapp.databinding.ChatlayoutBinding
import com.example.datingapp.model.UserModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

    class chatAdapter(val context: Context, val list: ArrayList<String> , val chatKey: List<String>) : RecyclerView.Adapter<chatAdapter.MessageUserViewHolder>() {
    inner class MessageUserViewHolder(val binding: ChatlayoutBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageUserViewHolder {
        return MessageUserViewHolder(ChatlayoutBinding.inflate(LayoutInflater.from(context), parent,false ))
    }

    override fun getItemCount(): Int {
        return list.size
    }

        override fun onBindViewHolder(holder: MessageUserViewHolder, position: Int) {
            val userId = list[position]

            FirebaseDatabase.getInstance().getReference("users")
                .child(userId).addListenerForSingleValueEvent(
                    object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            if (snapshot.exists()) {
                                val data = snapshot.getValue(UserModel::class.java)
                                if (data != null) {
                                    Glide.with(context).load(data.image)
                                        .into(holder.binding.userImagechat)
                                    holder.binding.Usernamechat.text = data.name
                                }
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {

                        }
                    })

            holder.itemView.setOnClickListener{
                val inte = Intent(context, ChatActivity::class.java)
                inte.putExtra("chat_id", chatKey[position])
                inte.putExtra("userID", list[position])
                context.startActivity(inte)

            }
        }
    }










