package com.socialite.solite_pos.view.main.menu.purchase

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.socialite.solite_pos.adapters.recycleView.supplier.SelectSupplierAdapter
import com.socialite.solite_pos.data.source.local.entity.room.master.Supplier
import com.socialite.solite_pos.databinding.FragmentSelectSupplierBinding
import com.socialite.solite_pos.utils.tools.BottomSheetView
import com.socialite.solite_pos.view.viewModel.MainViewModel
import com.socialite.solite_pos.view.viewModel.MainViewModel.Companion.getMainViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.Locale

class SelectSupplierFragment(private val callback: (Supplier?) -> Unit) :
    BottomSheetDialogFragment() {

    private lateinit var _binding: FragmentSelectSupplierBinding
    private lateinit var adapter: SelectSupplierAdapter
    private lateinit var viewModel: MainViewModel

    private val suppliers: ArrayList<Supplier> = ArrayList()
    private var supplier: Supplier? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSelectSupplierBinding.inflate(inflater, container, false)
        return _binding.root
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val bottomSheetDialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        return BottomSheetView.setBottom(bottomSheetDialog)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (activity != null) {

            viewModel = getMainViewModel(activity!!)

            adapter = SelectSupplierAdapter {
                supplier = it
                dialog?.dismiss()
            }
            _binding.rvSelectSupplier.layoutManager = LinearLayoutManager(activity)
            _binding.rvSelectSupplier.adapter = adapter

            getSuppliers()

            _binding.edtSsSearch.addTextChangedListener {
                object : TextWatcher {
                    override fun beforeTextChanged(
                        s: CharSequence?,
                        start: Int,
                        count: Int,
                        after: Int
                    ) {
                    }

                    override fun afterTextChanged(s: Editable?) {}

                    override fun onTextChanged(
                        s: CharSequence?,
                        start: Int,
                        before: Int,
                        count: Int
                    ) {
                        setFilter(s.toString())
                    }

                }
            }
        }
    }

    private fun getSuppliers() {
        lifecycleScope.launch {
            viewModel.suppliers
                .collect {
                    if (it.isNotEmpty()) {
                        if (suppliers.isNotEmpty()) {
                            suppliers.clear()
                        }
                        suppliers.addAll(it)
                    }
                    setFilter(null)
                }
        }
    }

    private fun setFilter(s: String?) {
        if (!s.isNullOrEmpty()) {
            val array: ArrayList<Supplier> = ArrayList()
            for (user in suppliers) {
                if (user.name.toLowerCase(Locale.getDefault())
                        .contains(s.toLowerCase(Locale.getDefault()))
                ) {
                    array.add(user)
                }
            }
            adapter.items = array
        } else {
            adapter.items = suppliers
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        callback.invoke(supplier)
    }
}
