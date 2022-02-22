package com.pcandroiddev.repotracker.ui

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.pcandroiddev.repotracker.repository.RepoRepository

class HomeVMProviderFactory(
    val application: Application,
    private val repository: RepoRepository
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return HomeVM(application,repository) as T
    }
}