package com.example.ecomapp.ui.view.product

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.SeekBar
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ecomapp.R
import com.example.ecomapp.data.Category
import com.example.ecomapp.data.Product
import com.example.ecomapp.databinding.FragmentDisplayProductBinding
import com.example.ecomapp.databinding.ReuseBottombarBinding
import com.example.ecomapp.ui.adapter.display.CategoryRecyclerViewAdapter
import com.example.ecomapp.ui.adapter.display.DisplayProductRecyclerViewAdapter
import com.example.ecomapp.ui.viewmodel.CategoryViewModel
import com.example.ecomapp.ui.viewmodel.ProductsViewModel
import kotlin.properties.Delegates

class DisplayProductFragment : Fragment() {

    //region Create variables
    private lateinit var binding: FragmentDisplayProductBinding
    private lateinit var barBinding: ReuseBottombarBinding

    private lateinit var listCategory: List<Category>

    private lateinit var displayRecyclerView: RecyclerView
    private lateinit var displayAdapter: DisplayProductRecyclerViewAdapter

    private lateinit var seekBar: SeekBar
    private lateinit var products: List<Product>
    private lateinit var keyword : String

    private var allCategoryIds = ArrayList<Int>()

    private var currentPrice : Int = 0
    private var type by Delegates.notNull<String>()
    //endregion

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentDisplayProductBinding.inflate(inflater, container, false)

        barBinding = ReuseBottombarBinding.inflate(inflater, container, false)

        type = (arguments?.getSerializable("type") as? String)!!

        if (type == "3") {
            keyword = (arguments?.getSerializable("keyword") as? String)!!
        }
        // 0 -> all, 1 -> popular, 2 -> suggest, 3 -> search

        fetchDisplayProducts()
        fetchAllCategories()

        binding.apply {

            root.addView(barBinding.root)

            when (type) {
                "0" -> tvTopTitle.text = "Tất cả sách"
                "1" -> tvTopTitle.text = "Sách nổi tiếng"
                else -> tvTopTitle.text = "Sách được gợi ý"
            }

            //region Click Listener
            ivBackArrow.setOnClickListener {
                findNavController().popBackStack()
            }

            btnFilter.setOnClickListener {
                createDialogFilter()
            }

            btnSortPriceLTH.setOnClickListener {
                val sortedList = products.sortedBy { it.price }
                addDisplayProducts(sortedList)
            }

            btnSortPriceHTL.setOnClickListener {
                val sortedList = products.sortedByDescending { it.price }
                addDisplayProducts(sortedList)
            }

            ivSearch.setOnClickListener {
                it.findNavController()
                    .navigate(R.id.action_displayProductFragment_to_searchFragment)
            }
            //endregion

            //region Navigate to other pages
            barBinding.apply {

                llWishlist.setOnClickListener {
                    it.findNavController()
                        .navigate(R.id.action_displayProductFragment_to_favoriteFragment)
                }

                llNoti.setOnClickListener {
                    it.findNavController()
                        .navigate(R.id.action_displayProductFragment_to_notificationFragment)
                }

                llHome.setOnClickListener {
                    it.findNavController()
                        .navigate(R.id.action_displayProductFragment_to_homeFragment)
                }

            }
            //endregion

        }

        return binding.root

    }

    //region Fetch data here
    private fun fetchDisplayProducts() {
        val productsViewModel = ViewModelProvider(this)[ProductsViewModel::class.java]
        if (type != "3") {
            productsViewModel.fetchProducts(type.toInt())
        }
        else {
            productsViewModel.searchProducts(keyword)
        }
        productsViewModel.displayProducts.observe(viewLifecycleOwner) {
            if (it != null) {
                addDisplayProducts(it)
                products = it
            }
        }
    }

    private fun fetchDisplayProductsApplyFilter(type : Int, listCategoryId : List<Int>, listCount : Int, price : Int) {
        val productsViewModel = ViewModelProvider(this)[ProductsViewModel::class.java]
        if (type == 0) {
            productsViewModel.fetchProductsFilterByCategoriesAndPrice(listCategoryId, listCount, price)
        }
        else if (type == 1) {
            productsViewModel.fetchProductsFilterByCategoriesAndPriceAndType(type, listCategoryId, listCount, price)
        }
        productsViewModel.displayProducts.observe(viewLifecycleOwner) {
            if (it != null) {
                addDisplayProducts(it)
            }
        }
    }

    private fun fetchAllCategories() {
        val categoriesViewModel = ViewModelProvider(this)[CategoryViewModel::class.java]
        categoriesViewModel.fetchCategories()
        categoriesViewModel.categories.observe(viewLifecycleOwner) {
            if (it != null) {
                listCategory = it
                for (i in listCategory) {
                    allCategoryIds.add(i.id)
                }
            }
        }
    }
    //endregion

    private fun addDisplayProducts(products: List<Product>) {

        binding.apply {

            displayRecyclerView = rvDisplayBook
            displayRecyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
            displayAdapter = DisplayProductRecyclerViewAdapter(products)
            displayRecyclerView.adapter = displayAdapter

            displayAdapter.onItemClick = {
                val bundle = bundleOf("product" to it)
                findNavController().navigate(
                    R.id.action_displayProductFragment_to_detailProductFragment,
                    bundle
                )
            }
        }
    }

    private fun createDialogFilter() {

        val dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.popup_filter)

        val categoryRecyclerView = dialog.findViewById<RecyclerView>(R.id.rvCategoryPopup)
        categoryRecyclerView.layoutManager = GridLayoutManager(requireContext(), 3)

        val categoryAdapter = CategoryRecyclerViewAdapter(listCategory)
        categoryRecyclerView.adapter = categoryAdapter

        seekBar = dialog.findViewById(R.id.skPrice)

        val startPrice = dialog.findViewById<TextView>(R.id.tvStartPrice)
        val endPrice = dialog.findViewById<TextView>(R.id.tvEndPrice)

        initializeSeekbar(startPrice, endPrice)

        dialog.findViewById<Button>(R.id.btnApply).setOnClickListener {
            if (categoryAdapter.listCategoryId.isEmpty()) {
                fetchDisplayProductsApplyFilter(type.toInt(), allCategoryIds, 1, currentPrice)
            }
            else {
                fetchDisplayProductsApplyFilter(type.toInt(), categoryAdapter.listCategoryId, categoryAdapter.listCategoryId.size, currentPrice)
            }
        }

        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val layoutParams = dialog.window?.attributes
        layoutParams?.gravity = Gravity.BOTTOM
        dialog.window?.attributes = layoutParams

        dialog.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )

        dialog.show()
    }

    private fun initializeSeekbar(startPrice : TextView, endPrice : TextView) {

        seekBar.max = 100
        currentPrice = 2000000

        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {

            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {

                currentPrice = calculatePrice(progress)

                startPrice.text = formatPrice(currentPrice)
                endPrice.text = formatPrice(2000000)

            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }

        })
    }

    private fun calculatePrice(progress: Int): Int {
        return progress * 20000
    }

    private fun formatPrice(price: Int): String {
        return price.toString().reversed().chunked(3).joinToString(".").reversed() + " đ"
    }

}