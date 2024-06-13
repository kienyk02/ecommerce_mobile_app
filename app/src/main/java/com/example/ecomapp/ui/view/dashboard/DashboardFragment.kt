package com.example.ecomapp.ui.view.dashboard

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.ecomapp.R
import com.example.ecomapp.databinding.FragmentDashboardBinding
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.utils.ColorTemplate

class DashboardFragment : Fragment() {

    private lateinit var binding: FragmentDashboardBinding
    private val controller by lazy {
        findNavController()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDashboardBinding.inflate(inflater, container, false)

        binding.apply {
            ivBackArrow.setOnClickListener {
                findNavController().popBackStack()
            }
        }

        val barChart = binding.bcSales

        val salesTable = arrayListOf<BarEntry>()

        salesTable.add(BarEntry(0f, 40f))
        salesTable.add(BarEntry(1f, 10f))
        salesTable.add(BarEntry(2f, 40f))
        salesTable.add(BarEntry(3f, 50f))
        salesTable.add(BarEntry(4f, 60f))
        salesTable.add(BarEntry(5f, 60f))
        salesTable.add(BarEntry(6f, 90f))
        salesTable.add(BarEntry(7f, 70f))
        salesTable.add(BarEntry(8f, 40f))
        salesTable.add(BarEntry(9f, 50f))

        val barDataSet = BarDataSet(salesTable, "Sales")

        barDataSet.setColors(*ColorTemplate.COLORFUL_COLORS)

        val data = BarData(barDataSet)
        barChart.data = data

        barChart.setFitBars(true)
        barChart.invalidate()

        val pieChart = binding.bcViewsNumber

        val viewsTable = arrayListOf<PieEntry>()

        viewsTable.add(PieEntry(80f, "12 rules for life"))
        viewsTable.add(PieEntry(30f, "Atomic Habits"))
        viewsTable.add(PieEntry(40f, "Ego is the enemy"))
        viewsTable.add(PieEntry(50f, "The Book of Five Rings"))
        viewsTable.add(PieEntry(60f, "No game no life"))

        val pieDataSet = PieDataSet(viewsTable, "Views")
        pieDataSet.setColors(*ColorTemplate.COLORFUL_COLORS)

        val data2 = PieData(pieDataSet)
        pieChart.data = data2

        pieChart.invalidate()

        return binding.root
    }


}