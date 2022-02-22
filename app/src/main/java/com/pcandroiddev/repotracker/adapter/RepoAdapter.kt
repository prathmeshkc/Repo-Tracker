package com.pcandroiddev.repotracker.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.pcandroiddev.repotracker.databinding.ItemRepoBinding
import com.pcandroiddev.repotracker.models.RepoListItem

class RepoAdapter(
//    private val onItemClickListener: (ItemRepoBinding, RepoListItem) -> Unit = { _: ItemRepoBinding, _: RepoListItem -> }
) : RecyclerView.Adapter<RepoAdapter.RepoViewHolder>() {


    private val differCallBack = object : DiffUtil.ItemCallback<RepoListItem>(){
        override fun areItemsTheSame(oldItem: RepoListItem, newItem: RepoListItem): Boolean {
            return oldItem.repoName == newItem.repoName
        }

        override fun areContentsTheSame(oldItem: RepoListItem, newItem: RepoListItem): Boolean {
            return oldItem == newItem
        }

    }

    val differ = AsyncListDiffer(this, differCallBack)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepoViewHolder {
        val binding = ItemRepoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RepoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RepoViewHolder, position: Int) {
        val repoItem = differ.currentList[position]
        holder.bind(repoItem)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    private var onItemClickListener : ((ItemRepoBinding, RepoListItem) -> Unit)? = null

    fun setOnItemClickListener(listener: (ItemRepoBinding, RepoListItem) -> Unit){
        onItemClickListener = listener
    }

    inner class RepoViewHolder(private val binding: ItemRepoBinding) :
        RecyclerView.ViewHolder(binding.root) {



        fun bind(repo: RepoListItem) {
            binding.apply {
                tvRepoName.text = repo.repoName
                tvRepoDesc.text = repo.repoDesc
            }

            onItemClickListener?.let { it(binding,repo) }
        }


    }
}
