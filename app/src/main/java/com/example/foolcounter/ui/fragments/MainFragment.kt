package com.example.foolcounter.ui.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import androidx.fragment.app.Fragment
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foolcounter.App
import com.example.foolcounter.R
import com.example.foolcounter.databinding.AcceptDialogBinding
import com.example.foolcounter.databinding.AddPlayerDialogBinding
import com.example.foolcounter.databinding.FragmentMainBinding
import com.example.foolcounter.model.Player
import com.example.foolcounter.ui.PlayerAdapter
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.divider.MaterialDividerItemDecoration
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainFragment : Fragment(R.layout.fragment_main) {

    private val viewModel: MainFragmentViewModel by viewModels {
        MainFragmentViewModelFactory((activity?.application as App).db.playersDao())
    }
    private val playerAdapter by lazy {
        PlayerAdapter(::onButtonsClicked)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentMainBinding.bind(view)

        setupRV(binding)

        viewModel.visible.observe(viewLifecycleOwner) {
            binding.empty.isVisible = it
        }

        binding.toolBar.setOnMenuItemClickListener {
            toolBarItemClicked(it)
        }
    }

    private fun setupRV(binding: FragmentMainBinding) {
        val divider = MaterialDividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL)
        divider.setDividerColorResource(requireContext(), R.color.blue_light)
        divider.isLastItemDecorated = false

        lifecycleScope.launch {
            viewModel.getAllPlayers().collect { entities ->
                val list = entities.map { it.toPlayer() }
                playerAdapter.submitList(list.sortedByDescending { it.count })
                delay(100)
                viewModel.setVisible(list.isEmpty())
            }
        }

        binding.recyclerView.adapter = playerAdapter
        binding.recyclerView.addItemDecoration(divider)
    }

    private fun onButtonsClicked(player: Player, addPoint: Boolean) {
        val num = player.count + if (addPoint) +1 else -1
        val msg =
            getString((if (addPoint) R.string.add_point else R.string.remove_point), player.name)

        val binding = AcceptDialogBinding.inflate(LayoutInflater.from(requireContext()))
        val builder = MaterialAlertDialogBuilder(requireContext())
            .setView(binding.root)
            .create()

        with(binding) {
            message.text = msg

            acceptBtn.setOnClickListener {
                lifecycleScope.launch {
                    viewModel.update(
                        Player(
                            id = player.id,
                            name = player.name,
                            count = if (num > 0) num else 0
                        )
                    )
                }
                builder.dismiss()
            }

            cancelBtn.setOnClickListener { builder.dismiss() }
        }

        builder.show()
    }

    private fun deletePlayersDialog(ctx: Context, msg: String) {
        val binding = AcceptDialogBinding.inflate(LayoutInflater.from(ctx))
        val builder = MaterialAlertDialogBuilder(ctx)
            .setView(binding.root)
            .create()

        with(binding) {
            message.text = msg

            acceptBtn.setOnClickListener {
                lifecycleScope.launch {
                    viewModel.deleteAll()
                }
                builder.dismiss()
            }

            binding.cancelBtn.setOnClickListener { builder.dismiss() }
        }

        builder.show()
    }

    private fun addPlayerDialog(ctx: Context) {
        val binding = AddPlayerDialogBinding.inflate(LayoutInflater.from(ctx))
        val builder = MaterialAlertDialogBuilder(ctx)
            .setView(binding.root)
            .create()

        binding.acceptBtn.setOnClickListener {
            val playerName = binding.nameText.text.toString()

            lifecycleScope.launch {
                viewModel.addPlayer(
                    Player(name = playerName.ifBlank { getString(R.string.no_name) })
                )
            }

            builder.dismiss()
        }

        binding.cancelBtn.setOnClickListener {
            builder.dismiss()
        }

        builder.show()
    }

    private fun toolBarItemClicked(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.clear_all -> {
                deletePlayersDialog(requireContext(), getString(R.string.warning))
                true
            }
            R.id.add_player -> {
                addPlayerDialog(requireContext())
                true
            }
            else -> false
        }
    }
}
