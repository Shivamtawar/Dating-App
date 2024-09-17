package com.example.datingapp.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.datingapp.adapter.chatAdapter
import com.example.datingapp.databinding.FragmentChatBinding
import com.example.datingapp.util.Config
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager

class ChatFragment : Fragment() {

    private var _binding: FragmentChatBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentChatBinding.inflate(inflater, container, false)

        getdata()

        return binding.root
    }

    private fun getdata() {
        Config.showdialog(requireContext())

        val currentId = FirebaseAuth.getInstance().currentUser?.phoneNumber

        if (currentId == null) {
            Toast.makeText(requireContext(), "User not logged in", Toast.LENGTH_SHORT).show()
            Config.hideDialog()
            return
        }

        Log.d("ChatFragment", "Current User ID: $currentId")

        FirebaseDatabase.getInstance().getReference("chats")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {

                    val list = arrayListOf<String>()
                    val chatkey = arrayListOf<String>()

                    for (data in snapshot.children) {
                        val key = data.key ?: continue
                        if (key.contains(currentId)) {
                            val userId = key.replace(currentId, "")
                            list.add(userId)
                            chatkey.add(key)


                        }
                    }

                    binding.recyclerview.layoutManager = LinearLayoutManager(requireContext())
                        binding.recyclerview.adapter =
                            chatAdapter(requireContext(), list, chatkey)


                    Config.hideDialog()
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(requireContext(), error.message, Toast.LENGTH_SHORT).show()
                    Log.e("ChatFragment", "DatabaseError: ${error.message}")
                    Config.hideDialog()
                }
            })
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }




}
