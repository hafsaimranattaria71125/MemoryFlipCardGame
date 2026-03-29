package com.example.memoryflipgame

import android.content.Intent
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val titleText = findViewById<TextView>(R.id.titleText)
        val btn4x4 = findViewById<Button>(R.id.btn4x4)
        val btn6x6 = findViewById<Button>(R.id.btn6x6)
        val emojiRow = findViewById<TextView>(R.id.emojiRow)

        val bounceAnim = AnimationUtils.loadAnimation(this, R.anim.bounce)
        emojiRow.startAnimation(bounceAnim)

        btn4x4.setOnClickListener {
            startGame(4)
        }

        btn6x6.setOnClickListener {
            startGame(6)
        }
    }

    private fun startGame(gridSize: Int) {
        val intent = Intent(this, GameActivity::class.java)
        intent.putExtra("GRID_SIZE", gridSize)
        startActivity(intent)
    }
}
