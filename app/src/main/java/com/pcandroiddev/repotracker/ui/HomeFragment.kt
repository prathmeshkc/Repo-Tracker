package com.pcandroiddev.repotracker.ui

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.pcandroiddev.repotracker.MainActivity
import com.pcandroiddev.repotracker.R
import com.pcandroiddev.repotracker.adapter.RepoAdapter
import com.pcandroiddev.repotracker.databinding.FragmentHomeBinding
import com.pcandroiddev.repotracker.util.Resource

class HomeFragment : Fragment(R.layout.fragment_home) {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    lateinit var viewModel: HomeVM
    lateinit var repoAdapter: RepoAdapter
    lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = (activity as MainActivity).homeVM
        setupRecyclerView()
        navController = (activity as MainActivity).navController
        viewModel.getAllRepo()
        binding.btnAddNow.setOnClickListener {
            navController.navigate(R.id.action_homeFragment_to_addRepoFragment)
        }

        viewModel.repos.observe(viewLifecycleOwner) { response ->

            when (response) {
                is Resource.Loading -> {
                    showProgressBar()
                }
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { repositoryResponse ->
                        repoAdapter.differ.submitList(repositoryResponse.toList())
                        repoAdapter.setOnItemClickListener { itemRepoBinding, repoListItem ->
                            itemRepoBinding.btnShare.setOnClickListener {
                                val repoName = repoListItem.repoName
                                val repoDesc = repoListItem.repoDesc
                                val htmlUrl = repoListItem.html_url

                                val intent = Intent()
                                intent.apply {
                                    action = Intent.ACTION_SEND
                                    putExtra(Intent.EXTRA_TEXT, "Repo Name: $repoName\n\nRepo Description: $repoDesc\n\nRepo URL: $htmlUrl")
                                    type = "text/plain"
                                }
                                startActivity(Intent.createChooser(intent,"Share via"))

                            }
                        }

                    }
                }
                is Resource.Error -> Snackbar.make(
                    view,
                    "${response.message}",
                    Snackbar.LENGTH_LONG
                ).show()
            }

        }

        viewModel.listState.observe(viewLifecycleOwner) { listState ->

            when (listState) {
                is Resource.Success -> {
                    binding.btnAddNow.visibility = View.GONE
                    binding.tvEmptyListTitle.visibility = View.GONE
                }
                is Resource.Error -> {
                    binding.btnAddNow.visibility = View.VISIBLE
                    binding.tvEmptyListTitle.visibility = View.VISIBLE
                    repoAdapter.differ.submitList(emptyList())
//                    RepoAdapter().differ.submitList(emptyList())
//                    Snackbar.make(view, "${listState.message}", Snackbar.LENGTH_LONG).show()
                }
            }

        }



    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_home, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when (item.itemId) {
            R.id.addNewRepo -> {
                navController.navigate(R.id.action_homeFragment_to_addRepoFragment)
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }

    }

    private fun setupRecyclerView() {
        repoAdapter = RepoAdapter()
        binding.rvRepo.apply {
            adapter = repoAdapter
            layoutManager = LinearLayoutManager(activity)
        }

    }

    private fun hideProgressBar() {
        binding.progressBar.visibility = View.GONE
    }

    private fun showProgressBar() {
        binding.progressBar.visibility = View.VISIBLE
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}