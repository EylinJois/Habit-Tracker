package com.aenempeh.habittracker.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.aenempeh.habittracker.R
import com.aenempeh.habittracker.databinding.ActivityMainBinding
import com.aenempeh.habittracker.databinding.FragmentHabitCreateBinding
import com.aenempeh.habittracker.viewmodel.HabitViewModel

class HabitCreateFragment : Fragment() {
    private lateinit var binding: FragmentHabitCreateBinding
    private lateinit var viewModel: HabitViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHabitCreateBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this)[HabitViewModel::class.java]

        binding.btnSubmit.setOnClickListener {
            val name = binding.txtName.text.toString()
            val desc = binding.txtDescription.text.toString()
            val goal = binding.txtGoal.text.toString().toIntOrNull() ?: 0
            val unit = binding.txtUnit.text.toString()
            val icon = binding.pickIcon.selectedItem.toString()

            viewModel.createHabit(name, desc, goal, unit, icon)
        }

        // Tombol back di toolbar
        binding.topbarCreate.setNavigationOnClickListener {
            Navigation.findNavController(it).popBackStack()
        }
    }
}