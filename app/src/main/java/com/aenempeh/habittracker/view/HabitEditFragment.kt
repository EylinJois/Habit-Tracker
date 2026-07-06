package com.aenempeh.habittracker.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.aenempeh.habittracker.R
import com.aenempeh.habittracker.databinding.FragmentHabitEditBinding
import com.aenempeh.habittracker.model.Habit
import com.aenempeh.habittracker.viewmodel.HabitViewModel

class HabitEditFragment : Fragment() {

    private lateinit var binding: FragmentHabitEditBinding
    private lateinit var viewModel: HabitViewModel

    private lateinit var selectedHabit: Habit

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_habit_create,
            container,
            false
        )

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this)[HabitViewModel::class.java]

        val args = HabitEditFragmentArgs.fromBundle(requireArguments())

        selectedHabit = Habit(
            id = args.id,
            name = args.name,
            description = args.desc,
            goal = args.goal,
            unit = args.unit,
            icon = args.icon,
            currentCount = args.count
        )

        binding.habit = selectedHabit
        binding.lifecycleOwner = viewLifecycleOwner

        val iconArray = resources.getStringArray(R.array.habit_icons)
        val iconPosition = iconArray.indexOf(selectedHabit.icon)

        if (iconPosition >= 0) {
            binding.pickIcon.setSelection(iconPosition)
        }

        binding.btnSubmit.setOnClickListener {

            selectedHabit.goal =
                binding.txtGoal.text.toString().toIntOrNull() ?: 0

            selectedHabit.icon =
                binding.pickIcon.selectedItem.toString()

            viewModel.updateHabit(selectedHabit)

            Navigation.findNavController(it).popBackStack()
        }

        binding.topbarCreate.setNavigationOnClickListener {
            Navigation.findNavController(it).popBackStack()
        }
    }
}