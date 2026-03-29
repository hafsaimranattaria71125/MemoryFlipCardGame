package com.example.memoryflipgame

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class GameActivity : AppCompatActivity() {

    private lateinit var gameEngine: GameEngine
    private lateinit var cardAdapter: CardAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var movesText: TextView
    private lateinit var pairsText: TextView
    private lateinit var resetButton: Button
    private lateinit var winBanner: View
    private lateinit var winMovesText: TextView
    private lateinit var playAgainButton: Button

    private var gridSize: Int = 4
    private var isAnimating = false
    private val handler = Handler(Looper.getMainLooper())

    private var firstFlippedPosition: Int = -1
    private var secondFlippedPosition: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        gridSize = intent.getIntExtra("GRID_SIZE", 4)

        recyclerView = findViewById(R.id.cardRecyclerView)
        movesText = findViewById(R.id.movesText)
        pairsText = findViewById(R.id.pairsText)
        resetButton = findViewById(R.id.resetButton)
        winBanner = findViewById(R.id.winBanner)
        winMovesText = findViewById(R.id.winMovesText)
        playAgainButton = findViewById(R.id.playAgainButton)

        val gridLabel = findViewById<TextView>(R.id.gridLabel)
        gridLabel.text = "${gridSize}x${gridSize} Grid"

        recyclerView.itemAnimator = null

        startNewGame()

        resetButton.setOnClickListener { startNewGame() }
        playAgainButton.setOnClickListener {
            winBanner.visibility = View.GONE
            startNewGame()
        }
    }

    private fun startNewGame() {
        handler.removeCallbacksAndMessages(null)
        isAnimating = false
        firstFlippedPosition = -1
        secondFlippedPosition = -1
        winBanner.visibility = View.GONE

        gameEngine = GameEngine(gridSize)

        cardAdapter = CardAdapter(this, gameEngine.cards) { position ->
            onCardClicked(position)
        }

        recyclerView.layoutManager = GridLayoutManager(this, gridSize)
        recyclerView.adapter = cardAdapter

        updateStats()
    }

    private fun onCardClicked(position: Int) {
        if (isAnimating) return
        val card = gameEngine.cards[position]
        if (card.isFaceUp || card.isMatched) return

        when (gameEngine.flipCard(position)) {
            GameEngine.FlipResult.FLIPPED -> {
                firstFlippedPosition = position
                cardAdapter.notifyItemChanged(position)
            }

            GameEngine.FlipResult.MATCH -> {
                secondFlippedPosition = position
                cardAdapter.notifyItemChanged(position)
                isAnimating = true
                handler.postDelayed({
                    cardAdapter.notifyItemChanged(firstFlippedPosition)
                    cardAdapter.notifyItemChanged(secondFlippedPosition)
                    firstFlippedPosition = -1
                    secondFlippedPosition = -1
                    isAnimating = false
                    updateStats()
                }, 400)
            }

            GameEngine.FlipResult.NO_MATCH -> {
                secondFlippedPosition = position
                cardAdapter.notifyItemChanged(position)
                isAnimating = true
                handler.postDelayed({
                    gameEngine.flipDownUnmatched()
                    cardAdapter.notifyItemChanged(firstFlippedPosition)
                    cardAdapter.notifyItemChanged(secondFlippedPosition)
                    firstFlippedPosition = -1
                    secondFlippedPosition = -1
                    isAnimating = false
                    updateStats()
                }, 900)
            }

            GameEngine.FlipResult.GAME_COMPLETE -> {
                secondFlippedPosition = position
                cardAdapter.notifyItemChanged(position)
                isAnimating = true
                handler.postDelayed({
                    cardAdapter.notifyItemChanged(firstFlippedPosition)
                    cardAdapter.notifyItemChanged(secondFlippedPosition)
                    updateStats()
                    showWinBanner()
                    isAnimating = false
                }, 500)
            }

            GameEngine.FlipResult.INVALID -> {}
        }
    }

    private fun updateStats() {
        movesText.text = "Moves: ${gameEngine.moves}"
        pairsText.text = "Pairs: ${gameEngine.matchedPairs}/${gameEngine.totalPairs}"
    }

    private fun showWinBanner() {
        winMovesText.text = "Completed in ${gameEngine.moves} moves! 🎉"
        winBanner.visibility = View.VISIBLE
        winBanner.alpha = 0f
        winBanner.animate().alpha(1f).setDuration(400).start()
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacksAndMessages(null)
    }
}