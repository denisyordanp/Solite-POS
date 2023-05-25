package com.socialite.solite_pos.view.settings

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.material.ExperimentalMaterialApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.socialite.solite_pos.data.source.local.room.AppDatabase
import com.socialite.solite_pos.utils.config.DateUtils
import com.socialite.solite_pos.view.OpeningActivity
import com.socialite.solite_pos.view.SoliteActivity
import com.socialite.solite_pos.view.order_customer.OrderCustomerActivity
import com.socialite.solite_pos.view.orders.OrdersActivity
import com.socialite.solite_pos.view.store.StoreActivity
import com.socialite.solite_pos.view.ui.GeneralMenus
import com.socialite.solite_pos.view.ui.theme.SolitePOSTheme
import com.socialite.solite_pos.view.viewModel.MainViewModel
import com.socialite.solite_pos.view.viewModel.OrderViewModel
import java.io.File
import java.io.FileInputStream

class SettingsActivity : SoliteActivity() {

    private lateinit var orderViewModel: OrderViewModel
    private lateinit var mainViewModel: MainViewModel

    @ExperimentalMaterialApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        orderViewModel = OrderViewModel.getOrderViewModel(this)
        mainViewModel = MainViewModel.getMainViewModel(this)

        val date = DateUtils.currentDate

        setContent {

            SolitePOSTheme {
                SettingsMainMenu(
                    orderViewModel = orderViewModel,
                    mainViewModel = mainViewModel,
                    currentDate = date,
                    onGeneralMenuClicked = {
                        when (it) {
                            GeneralMenus.NEW_ORDER -> goToOrderCustomerActivity()
                            GeneralMenus.ORDERS -> goToOrdersActivity()
                            GeneralMenus.STORE -> goToStoreActivity()
                            else -> {
                                // Do nothing
                            }
                        }
                    },
                    onDarkModeChange = {
                        val delegate = if (it) {
                            AppCompatDelegate.MODE_NIGHT_YES
                        } else {
                            AppCompatDelegate.MODE_NIGHT_NO
                        }

                        AppCompatDelegate.setDefaultNightMode(delegate)
                        reLaunchSettingActivity()
                    },
                    onDeveloperClicked = {
                        exportDatabase()
                    },
                    onLogout = {
                        mainViewModel.logout()
                        goToOpening()
                    }
                )
            }
        }
    }

    private fun goToOpening() {
        val intent = Intent(this, OpeningActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
    }

    private fun goToOrderCustomerActivity() {
        val intent = Intent(this, OrderCustomerActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
    }

    private fun goToOrdersActivity() {
        val intent = Intent(this, OrdersActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
    }

    private fun goToStoreActivity() {
        val intent = Intent(this, StoreActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
    }

    private fun reLaunchSettingActivity() {
        startActivity(intent)
        finish()
    }

    fun openDirectory() {
        val writePermission = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        val readPermission = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
        if (
            writePermission != PackageManager.PERMISSION_GRANTED ||
                    readPermission != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ),
                0
            )
        } else {
            exportDatabase()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 0) {
            exportDatabase()
        }
    }

    fun exportDatabase() {
        try {
            val dbName = AppDatabase.DB_NAME
            val dbWalName = "$dbName-wal"
            val dbShmName = "$dbName-shm"
            val currentDBPath = "/data/data/${packageName}/databases/$dbName"
            val currentDB = File(currentDBPath)
            val currentDBWal = File("$currentDBPath-wal")
            val currentDBShm = File("$currentDBPath-shm")
            val firebaseStorage = Firebase.storage("gs://pos-sosialita.appspot.com")
            val storageRef = firebaseStorage.reference

            if (currentDB.exists()) {

                val needUploadDb = FileInputStream(currentDB)
                val needUploadDbWal = FileInputStream(currentDBWal)
                val needUploadDbShm = FileInputStream(currentDBShm)

                storageRef.child(dbName).putStream(needUploadDb)
                    .addOnFailureListener {
                        Log.d("TESTING", "storage failure : $it")
                    }.addOnSuccessListener {
                        Log.d("TESTING", "succeed")
                    }.addOnCompleteListener {
                        Log.d("TESTING", "complete")
                        needUploadDb.close()
                    }
                storageRef.child(dbWalName).putStream(needUploadDbWal)
                    .addOnFailureListener {
                        Log.d("TESTING", "storage failure : $it")
                    }.addOnSuccessListener {
                        Log.d("TESTING", "succeed")
                    }.addOnCompleteListener {
                        Log.d("TESTING", "complete")
                        needUploadDbWal.close()
                    }
                storageRef.child(dbShmName).putStream(needUploadDbShm)
                    .addOnFailureListener {
                        Log.d("TESTING", "storage failure : $it")
                    }.addOnSuccessListener {
                        Log.d("TESTING", "succeed")
                    }.addOnCompleteListener {
                        Log.d("TESTING", "complete")
                        needUploadDbShm.close()
                    }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
