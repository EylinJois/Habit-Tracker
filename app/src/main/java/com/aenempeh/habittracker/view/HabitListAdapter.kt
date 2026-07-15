package com.aenempeh.habittracker.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.aenempeh.habittracker.R
import com.aenempeh.habittracker.databinding.HabitListItemBinding
import com.aenempeh.habittracker.model.Habit

class HabitListAdapter(
    val habitList: ArrayList<Habit>,
    val onIncrement: (Habit) -> Unit, // tombol +
    val onDecrement: (Habit) -> Unit // tombol -
) : RecyclerView.Adapter<HabitListAdapter.HabitViewHolder>() {
    class HabitViewHolder(val binding: HabitListItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HabitViewHolder {
        val binding = HabitListItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return HabitViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HabitViewHolder, position: Int) {
        val habit = habitList[position]
        holder.binding.apply{

            txtHabitName.text =  habit.name
            txtDescription.text = habit.description
            progressBar.progress = habit.progressPercent()
            txtProgress.text = "${habit.currentCount} / ${habit.goal} ${habit.unit}"

            if (habit.isCompleted()) {
                txtStatus.text = "Completed"
                txtStatus.setTextColor(
                    ContextCompat.getColor(root.context, R.color.status_completed))
            } else {
                txtStatus.text = "In Progress"
                txtStatus.setTextColor(
                    ContextCompat.getColor(root.context, R.color.status_in_progress))
            }

            val iconRes = when (habit.icon) {
                "book"       -> R.drawable.book
                "meditation" -> R.drawable.meditation
                "water"      -> R.drawable.water
                "health"     -> R.drawable.health
                else         -> R.drawable.water
            }

            imgIcon.setImageResource(iconRes)
            btnIncrement.isEnabled = !habit.isCompleted() // button naik baru nyala klo misal habitnya blm complete
            btnDecrement.isEnabled = habit.currentCount > 0 // button turun baru nyala klo misal habitnya ada isinya
            btnIncrement.setOnClickListener {
                onIncrement(habit) // ini panggil fungsi onIncrement di fragmentnya klo klik
            }
            btnDecrement.setOnClickListener {
                onDecrement(habit) // ini panggil fungsi onDecrement di fragmentnya klo klik
            }

            txtHabitName.setOnClickListener {
                val action =
                    HabitListFragmentDirections
                        .actionHabitEditFragment(
                            habit.id,
                            habit.name,
                            habit.description,
                            habit.goal,
                            habit.unit,
                            habit.icon,
                            habit.currentCount
                        )
                Navigation.findNavController(root).navigate(action)
            }
        }
    }
    override fun getItemCount() = habitList.size

    fun updateList(newList: ArrayList<Habit>) {
        habitList.clear()
        habitList.addAll(newList)
        notifyDataSetChanged()
}
}

