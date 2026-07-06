package com.aenempeh.habittracker.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.aenempeh.habittracker.R
import com.aenempeh.habittracker.databinding.FragmentHabitListBinding
import com.aenempeh.habittracker.model.Habit
import com.aenempeh.habittracker.viewmodel.HabitViewModel

class HabitListFragment : Fragment() {

    private lateinit var binding: FragmentHabitListBinding
    private lateinit var viewModel: HabitViewModel
    private lateinit var adapter: HabitListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentHabitListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(requireActivity())[HabitViewModel::class.java]

        adapter = HabitListAdapter(
            arrayListOf(),
            onIncrement = { id -> viewModel.incrementProgress(id) },
            onDecrement = { id -> viewModel.decrementProgress(id) }
        )

        binding.recViewHabits.layoutManager = LinearLayoutManager(context)
        binding.recViewHabits.adapter = adapter
        binding.fabAddHabit.setOnClickListener {
            val action = HabitListFragmentDirections
                .actionHabitListFragmentToHabitCreateFragment()
            Navigation.findNavController(it).navigate(action)
        }

        binding.refreshLayout.setOnRefreshListener {
            viewModel.loadHabits()
            binding.refreshLayout.isRefreshing = false
        }

        viewModel.loadHabits()
        observeViewModel()


    }

    override fun onResume() {
        super.onResume()
        viewModel.loadHabits()  // reload setiap kali fragment aktif kembali
    }

    private fun observeViewModel() {
        viewModel.habitsLD.observe(viewLifecycleOwner, Observer { habits ->
            adapter.updateList(habits as ArrayList<Habit>)
            binding.txtEmpty.visibility =
                if (habits.isEmpty()) View.VISIBLE else View.GONE
            binding.recViewHabits.visibility =
                if (habits.isEmpty()) View.GONE else View.VISIBLE
        })
    }
}