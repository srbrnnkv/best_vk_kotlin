package ru.bmstu.squares

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import ru.bmstu.squares.databinding.ItemSquareBinding

/**
 * Адаптер списка квадратов. Хранит только количество элементов:
 * число в квадрате — это индекс элемента (с 0), цвет зависит от его чётности.
 */
class SquaresAdapter(initialCount: Int) : RecyclerView.Adapter<SquaresAdapter.SquareViewHolder>() {

    private var count = initialCount

    fun addSquare() {
        count++
        notifyItemInserted(count - 1)
    }

    override fun getItemCount(): Int = count

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SquareViewHolder {
        val binding = ItemSquareBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SquareViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SquareViewHolder, position: Int) {
        holder.bind(position)
    }

    class SquareViewHolder(
        private val binding: ItemSquareBinding,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(index: Int) {
            val square = binding.root
            val context = square.context
            square.text = context.getString(R.string.square_number, index)

            // Чётные — красные, нечётные — синие
            val colorRes = if (index % 2 == 0) R.color.square_even else R.color.square_odd
            square.backgroundTintList = ContextCompat.getColorStateList(context, colorRes)
        }
    }
}
