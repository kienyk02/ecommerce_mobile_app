package com.example.ecomapp.ui.view.setting

import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toFile
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.ecomapp.auth.Resource
import com.example.ecomapp.databinding.FragmentProfileBinding
import com.example.ecomapp.ui.viewmodel.UserViewModel
import com.github.dhaval2404.imagepicker.ImagePicker
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


class ProfileFragment() : Fragment() {

    private var uri: Uri? = null
    private var avatarImage: File? = null
    private var prevName: String? = null
    private var prevGender: Int? = null
    private var prevPhoneNum: String? = null
    private var prevAvatar: Bitmap? = null
    private lateinit var binding: FragmentProfileBinding
    private val controller by lazy {
        findNavController()
    }
    private val viewModel: UserViewModel by lazy {
        ViewModelProvider(this, UserViewModel.UserViewModelFactory(requireActivity().application))
            .get(UserViewModel::class.java)
    }

    private val startForProfileImageResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            val resultCode = result.resultCode
            val data = result.data

            if (resultCode == Activity.RESULT_OK) {
                //Image Uri will not be null for RESULT_OK
                val fileUri = data?.data!!
                uri = fileUri
                Glide.with(requireContext()).load(fileUri).apply(RequestOptions.circleCropTransform()).into(binding.userAvt)

            } else if (resultCode == ImagePicker.RESULT_ERROR) {
                Toast.makeText(requireContext(), ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "Task Cancelled", Toast.LENGTH_SHORT).show()
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(inflater, container, false)

        getUserInfo()

        binding.apply {

            btnBack.setOnClickListener {
                controller.popBackStack()
            }

            btnAvt.isClickable = false

            val itemSpinner = arrayOf("Nam", "Nữ")
            val spinnerAdapter = ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_dropdown_item, itemSpinner)
            spinnerGender.adapter = spinnerAdapter
            spinnerGender.isEnabled = false
            spinnerGender.isClickable = false
            btnSelectGender.visibility = View.INVISIBLE

            btnEdit.setOnClickListener{
                btnEdit.visibility = View.INVISIBLE
                txtChangeAvt.visibility = View.VISIBLE
                btnAvt.isClickable = true
                txtEditName.isEnabled = true
                spinnerGender.isEnabled = true
                spinnerGender.isClickable = true
                btnSelectGender.visibility = View.VISIBLE
                txtEditPhone.isEnabled = true
                btnSave.visibility = View.VISIBLE
                btnCancel.visibility = View.VISIBLE
                btnBack.visibility = View.INVISIBLE
            }

            btnAvt.setOnClickListener {
                ImagePicker.with(this@ProfileFragment)
                    .cropSquare()
                    .compress(512)
                    .maxResultSize(512, 512)
                    .createIntent { intent ->
                        startForProfileImageResult.launch(intent)
                    }
            }

            btnSave.setOnClickListener {
                btnEdit.visibility = View.VISIBLE
                txtChangeAvt.visibility = View.INVISIBLE
                btnAvt.isClickable = false
                txtEditName.isEnabled = false
                spinnerGender.isEnabled = false
                spinnerGender.isClickable = false
                btnSelectGender.visibility = View.INVISIBLE
                txtEditPhone.isEnabled = false
                btnSave.visibility = View.INVISIBLE
                btnCancel.visibility = View.INVISIBLE
                btnBack.visibility = View.VISIBLE

                updateUserInfo()
            }

            btnCancel.setOnClickListener {
                btnEdit.visibility = View.VISIBLE
                txtChangeAvt.visibility = View.INVISIBLE
                btnAvt.isClickable = false
                txtEditName.isEnabled = false
                spinnerGender.isEnabled = false
                spinnerGender.isClickable = false
                btnSelectGender.visibility = View.INVISIBLE
                txtEditPhone.isEnabled = false
                btnSave.visibility = View.INVISIBLE
                btnCancel.visibility = View.INVISIBLE
                btnBack.visibility = View.VISIBLE

                userAvt.setImageBitmap(prevAvatar)
                txtEditName.setText(prevName)
                spinnerGender.setSelection(prevGender!!)
                txtEditPhone.setText(prevPhoneNum)
            }

        }
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    private fun getUserInfo(){
        viewModel.user.observe(viewLifecycleOwner, Observer {
            when(it){
                is Resource.Success -> {
                    val imageBytes = Base64.decode(it.value.avatarImage, Base64.DEFAULT)
                    avatarImage = byteArrayToFile(imageBytes)
                    val decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
                    binding.userAvt.setImageBitmap(decodedImage)
                    prevAvatar = decodedImage

                    binding.userName.text = it.value.name
                    binding.txtEditName.setText(it.value.name)
                    prevName = it.value.name

                    if(it.value.gender == "Nam") {
                        binding.spinnerGender.setSelection(0)
                        prevGender = 0
                    } else {
                        binding.spinnerGender.setSelection(1)
                        prevGender = 1
                    }

                    if(it.value.phoneNumber == null){
                        binding.txtEditPhone.setText("Chưa có số điện thoại")
                        prevPhoneNum = "Chưa có số điện thoại"
                    } else {
                        binding.txtEditPhone.setText(it.value.phoneNumber)
                        prevPhoneNum = it.value.phoneNumber
                    }
                }
                is Resource.Failure -> {
                    Toast.makeText(requireContext(), "Không tìm thấy thông tin người dùng!", Toast.LENGTH_LONG).show()
                }
            }
        })
        viewModel.getUserInfo()
    }

    private fun updateUserInfo() {

        viewModel.responseUpdateUser.observe(viewLifecycleOwner, Observer {
            Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
        })

        val name = binding.txtEditName.text.toString().trim()
        val gender = binding.spinnerGender.selectedItem.toString().trim()
        val phoneNumber = binding.txtEditPhone.text.toString().trim()

        val reqBodyName: RequestBody = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), name)
        val reqBodyGender: RequestBody = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), gender)
        val reqBodyPhoneNumber: RequestBody = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), phoneNumber)

        if(uri != null) {
            avatarImage = uri?.toFile()!!
        }
        val reqBodyAvatarImage: RequestBody = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), avatarImage!!)
        val multipartBodyAvatar: MultipartBody.Part = MultipartBody.Part.createFormData("avatarImage", avatarImage!!.name, reqBodyAvatarImage)

        viewModel.updateUserInfo(reqBodyName, reqBodyGender, reqBodyPhoneNumber, multipartBodyAvatar)
    }

    private fun byteArrayToFile(byteArray: ByteArray): File {
        try {
            val tempFile = File.createTempFile("temp_image", null)
            val fileOutputStream = FileOutputStream(tempFile)
            fileOutputStream.write(byteArray)
            fileOutputStream.close()
            return tempFile
        } catch (e: IOException) {
            throw e
        }
    }

}