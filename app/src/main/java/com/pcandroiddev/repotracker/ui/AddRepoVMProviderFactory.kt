package com.pcandroiddev.repotracker.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.pcandroiddev.repotracker.repository.RepoRepository

class AddRepoVMProviderFactory(
    private val repository: RepoRepository
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AddRepoVM(repository) as T
    }
}