package com.example.datingapp.ui

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.datingapp.R
import com.example.datingapp.activity.EditProfileActivity
import com.example.datingapp.auth.loginActivity
import com.example.datingapp.databinding.FragmentProfileBinding
import com.example.datingapp.model.UserModel
import com.example.datingapp.util.Config
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase


class ProfileFragment : Fragment() {

    private lateinit var binding : FragmentProfileBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(layoutInflater)

        Config.showdialog(requireContext())





        FirebaseDatabase.getInstance().getReference("users").child(FirebaseAuth.getInstance().currentUser!!.phoneNumber!!).get()
            .addOnSuccessListener {
                if (it.exists()){
                    val data = it.getValue(UserModel::class.java)

                    binding.UserNameProfile.setText(data!!.name.toString())
                    binding.UserCityProfile.setText(data.city.toString())
                    binding.UserEmailProfile.setText(data.email.toString())
                    binding.UserPhonemumberprofile.setText(data.number.toString())

                    Glide.with(requireContext()).load(data.image).into(binding.UserImageProfile)
                    Config.hideDialog()


                }
            }

        binding.Logoutbtn.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(requireContext(), loginActivity::class.java))
            requireActivity().finish()
        }

        binding.Editproflebtn.setOnClickListener {
            startActivity(Intent(requireContext(), EditProfileActivity::class.java))

        }

        return binding.root
    }
}

