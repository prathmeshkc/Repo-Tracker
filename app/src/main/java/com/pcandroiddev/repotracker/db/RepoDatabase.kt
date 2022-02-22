package com.pcandroiddev.repotracker.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room.databaseBuilder
import androidx.room.RoomDatabase
import com.pcandroiddev.repotracker.models.RepoEntity.Repo


@Database(entities = [Repo::class], version = 1, exportSchema = false)
abstract class RepoDatabase : RoomDatabase() {

    abstract fun getRepoDao(): RepoDao


    companion object {
        @Volatile
        private var instance: RepoDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: createDatabase(context).also {
                instance = it
            }
        }

        private fun createDatabase(context: Context) = databaseBuilder(
            context,
            RepoDatabase::class.java,
            "repo_database"
        ).build()
    }


}