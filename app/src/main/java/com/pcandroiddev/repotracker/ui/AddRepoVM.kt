package com.pcandroiddev.repotracker.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pcandroiddev.repotracker.models.RepoEntity.Repo
import com.pcandroiddev.repotracker.repository.RepoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AddRepoVM(
    private val repository: RepoRepository
) : ViewModel() {


    fun insertRepo(repo: Repo) {
        viewModelScope.launch(Dispatchers.IO) {
            Log.d("AddRepoVM", "insertRepo")
            repository.insertRepo(repo)
        }
    }


}