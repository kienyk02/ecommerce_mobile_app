package com.example.ecomapp.ui.view.notification

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ecomapp.data.Notification
import com.example.ecomapp.databinding.FragmentNotificationBinding
import com.example.ecomapp.ui.adapter.NotificationAdapter

class NotificationFragment : Fragment() {

    private lateinit var binding: FragmentNotificationBinding
    private val controller by lazy {
        findNavController()
    }

    private lateinit var notifications: MutableList<Notification>
    private lateinit var displayRecycleView: RecyclerView
    private lateinit var displayAdapter: NotificationAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentNotificationBinding.inflate(inflater, container, false)

        initFakeData()

        checkNoti()

        binding.apply {

            btnBack.setOnClickListener {
                controller.popBackStack()
            }

            btnClear.setOnClickListener {
                notifications.clear()
                displayAdapter.setData(notifications)
                checkNoti()
            }

            displayRecycleView = listNotification
            displayRecycleView.layoutManager =
                LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
            displayAdapter = NotificationAdapter(notifications, this)
            displayRecycleView.adapter = displayAdapter

        }

        return binding.root
    }

    private fun initFakeData() {
        notifications = mutableListOf(
            Notification("", "Sách mới ra mắt tháng 3!!", "10 minutes ago"),
            Notification("", "Đơn hàng của bạn đã được giao", "1 day ago"),
            Notification("", "Sách mới ra mắt tháng 3!!", "10 minutes ago"),
            Notification("", "Sách mới ra mắt tháng 3!!", "10 minutes ago"),
            Notification("", "Sách mới ra mắt tháng 3!!", "10 minutes ago"),
            Notification("", "Sách mới ra mắt tháng 3!!", "10 minutes ago"),
            Notification("", "Sách mới ra mắt tháng 3!!", "10 minutes ago"),
            Notification("", "Sách mới ra mắt tháng 3!!", "10 minutes ago"),
            Notification("", "Sách mới ra mắt tháng 3!!", "10 minutes ago"),
            Notification("", "Sách mới ra mắt tháng 3!!", "10 minutes ago"),
            Notification("", "Sách mới ra mắt tháng 3!!", "10 minutes ago"),
            Notification("", "Sách mới ra mắt tháng 3!!", "10 minutes ago"),
            Notification("", "Sách mới ra mắt tháng 3!!", "10 minutes ago"),
            Notification("", "Sách mới ra mắt tháng 3!!", "10 minutes ago")
        )
    }

    private fun checkNoti(){
        binding.apply {
            if(notifications.isEmpty()){
                txtNoNoti.visibility = View.VISIBLE
            } else {
                txtNoNoti.visibility = View.INVISIBLE
            }
        }
    }

}