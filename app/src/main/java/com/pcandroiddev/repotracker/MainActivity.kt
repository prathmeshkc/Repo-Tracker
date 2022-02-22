package com.pcandroiddev.repotracker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupActionBarWithNavController
import com.pcandroiddev.repotracker.databinding.ActivityMainBinding
import com.pcandroiddev.repotracker.db.RepoDatabase
import com.pcandroiddev.repotracker.repository.RepoRepository
import com.pcandroiddev.repotracker.ui.AddRepoVM
import com.pcandroiddev.repotracker.ui.AddRepoVMProviderFactory
import com.pcandroiddev.repotracker.ui.HomeVM
import com.pcandroiddev.repotracker.ui.HomeVMProviderFactory

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    lateinit var homeVM: HomeVM
    lateinit var addRepoVM: AddRepoVM
    lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        setupActionBarWithNavController(navController)

        val repoRepository = RepoRepository(RepoDatabase(this))
        val homeVMProviderFactory = HomeVMProviderFactory(application, repoRepository)
        val addRepoVMProviderFactory = AddRepoVMProviderFactory(repoRepository)
        homeVM = ViewModelProvider(this,homeVMProviderFactory).get(HomeVM::class.java)
        addRepoVM = ViewModelProvider(this, addRepoVMProviderFactory).get(AddRepoVM::class.java)


    }


    override fun onSupportNavigateUp(): Boolean {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        return navHostFragment.navController.navigateUp() || super.onSupportNavigateUp()
    }
}