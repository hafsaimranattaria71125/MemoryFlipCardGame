package com.example.memoryflipgame

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout

/**
 * A FrameLayout that forces itself to be square (width == height).
 * Used for card items in the RecyclerView grid so every card is perfectly square.
 */
class SquareFrameLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        // Make height equal to width so cards are always square
        super.onMeasure(widthMeasureSpec, widthMeasureSpec)
    }
}
