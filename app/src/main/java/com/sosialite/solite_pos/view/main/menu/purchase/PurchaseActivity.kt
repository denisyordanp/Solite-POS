package com.sosialite.solite_pos.view.main.menu.purchase

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.sosialite.solite_pos.databinding.ActivityPurchaseBinding

class PurchaseActivity : AppCompatActivity() {

	private lateinit var _binding: ActivityPurchaseBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
		_binding = ActivityPurchaseBinding.inflate(layoutInflater)
        setContentView(_binding.root)
    }
}
