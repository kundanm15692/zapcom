package com.example.zapcom.ui

import android.app.AlertDialog
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.zapcom.adopter.MainAdopter
import com.example.zapcom.data.model.ItemList
import com.example.zapcom.data.repository.MainActivityRepository
import com.example.zapcom.databinding.ActivityMainBinding
import com.example.zapcom.utils.UiState
import com.example.zapcom.viewmodel.MainViewModel
import com.example.zapcom.viewmodel.MainViewModelFactory
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var mainViewModel: MainViewModel
    private lateinit var adapter: MainAdopter

    @Inject
    lateinit var mainActivityRepository : MainActivityRepository

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        val factory = MainViewModelFactory(mainActivityRepository)
        mainViewModel = ViewModelProvider(this, factory)[MainViewModel::class.java]
        checkInternetAndShowPopup();
        setupObserver();
    }

    private fun setupObserver() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                binding.progressBar.visibility = View.GONE
                mainViewModel.uiState.collect {

                    when (it) {

                        is UiState.Loading -> {
                            binding.progressBar.visibility = View.VISIBLE
                        }

                        is UiState.Success -> {
                            binding.progressBar.visibility = View.GONE
                            setupRecyclerView(it.data)
                        }

                        is UiState.Error -> {
                           Toast.makeText(applicationContext,it.message,Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }

    private fun setupRecyclerView(data: List<ItemList>) {

        adapter = MainAdopter(data, this)
        binding.mainRv.layoutManager = LinearLayoutManager(applicationContext)
        binding.mainRv.adapter = adapter

    }



    private fun checkInternetAndShowPopup() {
        if (!isInternetAvailable(this)) {
            showNoInternetDialog()
        } else {
            mainViewModel.getItemList();
        }
    }

    private fun isInternetAvailable(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val network = connectivityManager.activeNetwork ?: return false
            val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false
            return when {
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                else -> false
            }
        } else {
            val networkInfo = connectivityManager.activeNetworkInfo ?: return false
            return networkInfo.isConnected
        }
    }

    private fun showNoInternetDialog() {

        val builder = AlertDialog.Builder(this)
        builder.setTitle("No Internet")
        builder.setMessage("Please check your network and try again.")
        builder.setCancelable(false)

        builder.setPositiveButton("Retry") { dialog, _ ->
            dialog.dismiss()
            checkInternetAndShowPopup()
        }

        builder.setNegativeButton("Close") { dialog, _ ->
            dialog.dismiss()
            finish()
        }

        val dialog: AlertDialog = builder.create()
        dialog.show()
    }
}