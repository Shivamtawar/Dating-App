package com.example.datingapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.view.menu.MenuView.ItemView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.example.datingapp.R
import com.example.datingapp.model.UserModel
import com.example.datingapp.model.messageModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class messageAdapter(val context : Context, val list: List<messageModel>)
    :RecyclerView.Adapter<messageAdapter.MessageViewHolder>() {

    val MSG_TYPE_RIGHT = 0
    val MSG_TYPE_LEFT = 1


    inner class MessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val text = itemView.findViewById<TextView>(R.id.messagetext)
        val img = itemView.findViewById<ImageView>(R.id.senderimg)


    }

    override fun getItemViewType(position: Int): Int {
        return if (list[position].senderID == FirebaseAuth.getInstance().currentUser!!.phoneNumber) {
            MSG_TYPE_RIGHT
        } else {
            MSG_TYPE_LEFT
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {

        return if (viewType == MSG_TYPE_RIGHT) {
            MessageViewHolder(
                LayoutInflater.from(context).inflate(R.layout.user_sendermsg, parent, false)
            )
        } else {
            MessageViewHolder(
                LayoutInflater.from(context).inflate(R.layout.user_receivermsg, parent, false)
            )
        }
    }

    override fun getItemCount(): Int {
        return list.size

    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {


        holder.text.text = list[position].message

        FirebaseDatabase.getInstance().getReference("users")
            .child(list[position].senderID!!).addListenerForSingleValueEvent(
                object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            val data = snapshot.getValue(UserModel::class.java)

                            Glide.with(context).load(data!!.image).into(holder.img)


                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Toast.makeText(context, error.message, Toast.LENGTH_SHORT).show()

                    }

                })



    }
}