package com.pcandroiddev.repotracker.ui

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import androidx.lifecycle.*
import com.pcandroiddev.repotracker.RepoApplication
import com.pcandroiddev.repotracker.models.RepoEntity.Repo
import com.pcandroiddev.repotracker.models.RepoListItem
import com.pcandroiddev.repotracker.models.RepositoryResponse.RepositoryResponse
import com.pcandroiddev.repotracker.repository.RepoRepository
import com.pcandroiddev.repotracker.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException
import java.lang.Exception

class HomeVM(
    application: Application,
    private val repository: RepoRepository
) : AndroidViewModel(application) {

    private val _repos: MutableLiveData<Resource<List<RepoListItem>>> = MutableLiveData()
    val repos: LiveData<Resource<List<RepoListItem>>> = _repos

    private val _listState: MutableLiveData<Resource<String>> = MutableLiveData()
    val listState: LiveData<Resource<String>> = _listState

    /*init {
        getAllRepo()
    }
*/
     fun getAllRepo() = viewModelScope.launch(Dispatchers.IO) {
        Log.d("HomeVM", "getAllRepo")
        val listOfRepos = repository.getAllRepos()
        if (listOfRepos.isEmpty()) {
            _listState.postValue(Resource.Error("No repo added yet!"))
        } else {
            _repos.postValue(Resource.Loading())
            _listState.postValue(Resource.Success("Repo available"))
            val repoListItemList = ArrayList<RepoListItem>()
            for (repo in listOfRepos) {
                val item = safeAPICall(ownerName = repo.ownerName, repoName = repo.repoName)
                if(!(item.repoName == "Error" || item.repoDesc =="Error" || item.html_url == "Error")){
                    repoListItemList.add(safeAPICall(ownerName = repo.ownerName, repoName = repo.repoName))
                }
//                safeAPICall(ownerName = repo.ownerName, repoName = repo.repoName)
            }
            _repos.postValue(Resource.Success(repoListItemList))
        }
    }


    private suspend fun safeAPICall(ownerName: String, repoName: String): RepoListItem {
        try {
            if(hasInternetConnection()){
                val response = repository.getRepository(ownerName = ownerName, repoName = repoName)
                return handleAPICall(response)
            }else {
                _repos.postValue(Resource.Error("No Internet Connection!"))
            }
        }catch (t: Throwable){
            when(t){
                is IOException -> _repos.postValue(Resource.Error("Network Failure!"))
                else -> _repos.postValue(Resource.Error("Something Went Wrong..."))
            }
        }

        return RepoListItem(repoName = "null", repoDesc = "null", html_url = "null")
    }

    private fun handleAPICall(response: Response<RepositoryResponse>): RepoListItem {
        if(response.isSuccessful){
            response.body()?.let { repositoryResponse ->
                return RepoListItem(repoName = repositoryResponse.name, repoDesc = repositoryResponse.description, html_url = repositoryResponse.html_url)
            }
        }
        return RepoListItem(repoName = "Error", repoDesc = "Error", html_url = "Error")
    }



    /*private suspend fun safeAPICall(ownerName: String, repoName: String) {

        _repos.postValue(Resource.Loading())

        try {
            if (hasInternetConnection()) {
                val response = repository.getRepository(ownerName = ownerName, repoName = repoName)

                _repos.postValue(handleAPICall(response))
            } else {
                _repos.postValue(Resource.Error("No Internet Connection!"))
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> _repos.postValue(Resource.Error("Network Failure!"))
                else -> _repos.postValue(Resource.Error("Something Went Wrong..."))
            }
        }
    }

    private fun handleAPICall(response: Response<RepositoryResponse>): Resource<RepositoryResponse> {
        if (response.isSuccessful) {
            response.body()?.let { repositoryResponse ->
                return Resource.Success(repositoryResponse)
            }
        }
        return Resource.Error(response.message())
    }
*/



    private fun hasInternetConnection(): Boolean {
        val connectivityManager =
            getApplication<RepoApplication>().getSystemService(
                Context.CONNECTIVITY_SERVICE
            ) as ConnectivityManager


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val activeNetwork = connectivityManager.activeNetwork ?: return false
            val capabilities =
                connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false

            return when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> return true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> return true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> return true
                else -> false
            }

        } else {
            connectivityManager.activeNetworkInfo?.run {
                return when (type) {
                    ConnectivityManager.TYPE_WIFI -> true
                    ConnectivityManager.TYPE_MOBILE -> true
                    ConnectivityManager.TYPE_ETHERNET -> true
                    else -> false
                }
            }
        }

        return false

    }


}