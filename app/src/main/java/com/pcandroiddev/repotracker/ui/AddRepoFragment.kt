package com.pcandroiddev.repotracker.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.pcandroiddev.repotracker.MainActivity
import com.pcandroiddev.repotracker.R
import com.pcandroiddev.repotracker.databinding.FragmentAddRepoBinding
import com.pcandroiddev.repotracker.models.RepoEntity.Repo


class AddRepoFragment : Fragment(R.layout.fragment_add_repo) {

    private var _binding: FragmentAddRepoBinding? = null
    private val binding get() = _binding!!

    lateinit var viewModel: AddRepoVM
    lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddRepoBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = (activity as MainActivity).addRepoVM
        navController = (activity as MainActivity).navController

        binding.tiedOwnerName.doOnTextChanged { text, start, before, count ->
            if (text!!.isEmpty()) {
                binding.tilOwnerName.error = "Please enter Owner/Organization name!"
            } else if (text.isNotEmpty()) {
                binding.tilOwnerName.error = null
            }
        }

        binding.tiedRepoName.doOnTextChanged { text, start, before, count ->
            if (text!!.isEmpty()) {
                binding.tilRepoName.error = "Please enter Repository name!"
            } else if (text.isNotEmpty()) {
                binding.tilRepoName.error = null
            }
        }

        binding.btnAddRepo.setOnClickListener {
            if (!(binding.tiedOwnerName.text!!.isEmpty() && binding.tiedRepoName.text!!.isEmpty())) {
                val ownerName = binding.tiedOwnerName.text.toString()
                val repoName = binding.tiedRepoName.text.toString()
                val newRepo = Repo(
                    ownerName = ownerName,
                    repoName = repoName
                )
                viewModel.insertRepo(newRepo)
                navController.navigate(R.id.action_addRepoFragment_to_homeFragment)

            } else {
                if (binding.tiedOwnerName.text!!.isEmpty()) {
                    binding.tilOwnerName.error = "Please enter Owner/Organization name!"
                } else if (binding.tiedRepoName.text!!.isEmpty()) {
                    binding.tilRepoName.error = "Please enter Repository name!"
                }
            }
        }


    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}