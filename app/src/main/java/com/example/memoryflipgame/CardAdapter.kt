package com.example.memoryflipgame

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView

class CardAdapter(
    private val context: Context,
    private val cards: List<Card>,
    private val onCardClick: (Int) -> Unit
) : RecyclerView.Adapter<CardAdapter.CardViewHolder>() {

    inner class CardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cardContainer: CardView = itemView.findViewById(R.id.cardContainer)
        val cardFront: View = itemView.findViewById(R.id.cardFront)
        val cardBack: View = itemView.findViewById(R.id.cardBack)
        val emojiText: TextView = itemView.findViewById(R.id.emojiText)

        init {
            cardContainer.setOnClickListener {
                val pos = bindingAdapterPosition
                if (pos != RecyclerView.NO_ID.toInt()) {
                    onCardClick(pos)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_card, parent, false)
        return CardViewHolder(view)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        val card = cards[position]
        holder.emojiText.text = card.emoji

        if (card.isFaceUp || card.isMatched) {
            holder.cardFront.visibility = View.VISIBLE
            holder.cardBack.visibility = View.GONE
            holder.emojiText.visibility = View.VISIBLE
            if (card.isMatched) {
                holder.cardContainer.alpha = 0.75f
                holder.cardContainer.setCardBackgroundColor(context.getColor(R.color.matched_card))
            } else {
                holder.cardContainer.alpha = 1f
                holder.cardContainer.setCardBackgroundColor(context.getColor(R.color.card_front))
            }
        } else {
            holder.cardFront.visibility = View.GONE
            holder.cardBack.visibility = View.VISIBLE
            holder.emojiText.visibility = View.GONE
            holder.cardContainer.alpha = 1f
            holder.cardContainer.setCardBackgroundColor(context.getColor(R.color.card_back))
        }
    }

    override fun getItemCount(): Int = cards.size
}