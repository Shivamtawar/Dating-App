package com.example.datingapp.activity

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.datingapp.adapter.messageAdapter
import com.example.datingapp.databinding.ActivityChatBinding
import com.example.datingapp.model.messageModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ChatActivity : AppCompatActivity() {

    private lateinit var binding : ActivityChatBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityChatBinding.inflate(layoutInflater)

        setContentView(binding.root)

//        getdata(intent.getStringExtra("chat_id"))

        verifyChatID()

        binding.buttonachatact.setOnClickListener {
            if(binding.Messagechatact.text!!.isEmpty()){
                Toast.makeText(this, "Please Enter Your message", Toast.LENGTH_SHORT).show()
            }
            else{
                storeData(binding.Messagechatact.text.toString())

            }

        }

    }


    private var senderID : String? = null
    private var ChatID : String? = null
    private fun verifyChatID() {
        val receiverID = intent.getStringExtra("userID")
         senderID = FirebaseAuth.getInstance().currentUser?.phoneNumber

        if (receiverID.isNullOrEmpty() || senderID.isNullOrEmpty()) {
            Toast.makeText(this, "User ID is missing", Toast.LENGTH_SHORT).show()
            return
        }

         ChatID = senderID + receiverID
        val reverseChatID = receiverID + senderID


        val reference = FirebaseDatabase.getInstance().getReference("chats")

        reference.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.hasChild(ChatID!!)){
                   getdata(ChatID!!)
                }else if (snapshot.hasChild(reverseChatID)) {
                    ChatID = reverseChatID
                    getdata(ChatID!!)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@ChatActivity, "chin tapak dhum dum", Toast.LENGTH_SHORT).show()
            }

        })

    }

    private fun getdata(chatId: String?) {

        if (chatId == null) {
            Toast.makeText(this, "Chat ID is missing", Toast.LENGTH_SHORT).show()
            return
        }


        FirebaseDatabase.getInstance().getReference("chats")
            .child(chatId).addValueEventListener(object :ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {

                    val list = arrayListOf<messageModel>()

                    for (show in snapshot.children){
                        list.add(show.getValue(messageModel::class.java)!!)
                    }

                    binding.recyclerView2.adapter = messageAdapter(this@ChatActivity, list)
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@ChatActivity, error.message, Toast.LENGTH_SHORT).show()
                }
            })
        


    }



    private fun storeData(msg: String) {
        val receiverID = intent.getStringExtra("userID")

        val currentDate: String = SimpleDateFormat("dd-MM-yyyy" , Locale.getDefault()).format(Date())
        val currentTime: String = SimpleDateFormat("HH:mm a" , Locale.getDefault()).format(Date())


        val map = hashMapOf<String, String>()
        map["message"] = msg
        map ["senderID"] = senderID!!
        map["receiverID"] = receiverID!!
        map ["CurrentTime"] = currentTime
        map ["CurrentDate"] = currentDate

        val reference = FirebaseDatabase.getInstance().getReference("chats").child(ChatID!!)

        reference.child(reference.push().key ?: "").setValue(map).addOnCompleteListener{
            if(it.isSuccessful){
                binding.Messagechatact.text = null
                Toast.makeText(this , "Message Sended", Toast.LENGTH_SHORT).show()
            }
            else{
                Toast.makeText(this, "Something Went wrong", Toast.LENGTH_SHORT).show()
            }
        }


    }
}