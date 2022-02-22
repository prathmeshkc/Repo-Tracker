package com.pcandroiddev.repotracker.db

import androidx.room.*
import com.pcandroiddev.repotracker.models.RepoEntity.Repo
import kotlinx.coroutines.flow.Flow

@Dao
interface RepoDao {

    @Query("SELECT * FROM repo_table ORDER BY id ASC")
    fun getAllRepos(): List<Repo>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRepo(repo: Repo)

}