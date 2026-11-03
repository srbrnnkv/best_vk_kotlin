package ru.bmstu.squares

import android.os.Bundle
import android.view.ViewGroup
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updateLayoutParams
import androidx.core.view.updatePadding
import androidx.recyclerview.widget.GridLayoutManager
import ru.bmstu.squares.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: SquaresAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Количество квадратов переживает поворот экрана через Bundle — без персистентного хранилища
        val squaresCount = savedInstanceState?.getInt(KEY_SQUARES_COUNT) ?: 0
        adapter = SquaresAdapter(squaresCount)

        // Число колонок берётся из ресурсов: values/ — 3 (портрет), values-land/ — 4 (ландшафт)
        val columns = resources.getInteger(R.integer.grid_columns)
        binding.squaresList.layoutManager = GridLayoutManager(this, columns)
        binding.squaresList.adapter = adapter

        binding.addButton.setOnClickListener {
            adapter.addSquare()
            binding.squaresList.smoothScrollToPosition(adapter.itemCount - 1)
        }

        applyWindowInsets()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(KEY_SQUARES_COUNT, adapter.itemCount)
    }

    /**
     * Приложение рисуется edge-to-edge, поэтому отступы считаем с учётом системных панелей и выреза.
     * Снизу у списка оставляем место под кнопку, чтобы при скролле до конца она не перекрывала квадраты.
     */
    private fun applyWindowInsets() {
        val listPadding = resources.getDimensionPixelSize(R.dimen.list_padding)
        val fabSize = resources.getDimensionPixelSize(R.dimen.fab_size)
        val fabMargin = resources.getDimensionPixelSize(R.dimen.fab_margin)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { _, windowInsets ->
            val insets = windowInsets.getInsets(
                WindowInsetsCompat.Type.systemBars() or WindowInsetsCompat.Type.displayCutout(),
            )
            binding.squaresList.updatePadding(
                left = listPadding + insets.left,
                top = listPadding + insets.top,
                right = listPadding + insets.right,
                // кнопка + отступ над ней и под ней + навигационная панель
                bottom = fabSize + 2 * fabMargin + insets.bottom,
            )
            binding.addButton.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                leftMargin = fabMargin + insets.left
                rightMargin = fabMargin + insets.right
                bottomMargin = fabMargin + insets.bottom
            }
            WindowInsetsCompat.CONSUMED
        }
    }

    private companion object {
        const val KEY_SQUARES_COUNT = "squares_count"
    }
}
