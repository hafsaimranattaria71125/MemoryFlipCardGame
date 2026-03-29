package com.example.memoryflipgame

class GameEngine(private val gridSize: Int) {

    val cards: MutableList<Card> = mutableListOf()
    var moves: Int = 0
        private set
    var matchedPairs: Int = 0
        private set
    val totalPairs: Int get() = (gridSize * gridSize) / 2

    private val emojis4x4 = listOf("🐶","🐱","🐭","🐹","🐰","🦊","🐻","🐼")
    private val emojis6x6 = listOf(
        "🐶","🐱","🐭","🐹","🐰","🦊","🐻","🐼",
        "🐨","🐯","🦁","🐮","🐷","🐸","🐵","🙈",
        "🦄","🐙"
    )

    init {
        generateCards()
    }

    private fun generateCards() {
        cards.clear()
        moves = 0
        matchedPairs = 0

        val emojiPool = if (gridSize == 4) emojis4x4 else emojis6x6
        val needed = (gridSize * gridSize) / 2
        val selected = emojiPool.shuffled().take(needed)

        val cardList = mutableListOf<Card>()
        selected.forEachIndexed { index, emoji ->
            cardList.add(Card(index * 2, emoji))
            cardList.add(Card(index * 2 + 1, emoji))
        }
        cardList.shuffle()
        cards.addAll(cardList)
    }

    fun flipCard(position: Int): FlipResult {
        val card = cards[position]
        if (card.isFaceUp || card.isMatched) return FlipResult.INVALID

        card.isFaceUp = true
        val faceUpUnmatched = cards.filter { it.isFaceUp && !it.isMatched }

        return if (faceUpUnmatched.size == 2) {
            moves++
            val (first, second) = faceUpUnmatched
            if (first.emoji == second.emoji) {
                first.isMatched = true
                second.isMatched = true
                matchedPairs++
                if (matchedPairs == totalPairs) FlipResult.GAME_COMPLETE
                else FlipResult.MATCH
            } else {
                FlipResult.NO_MATCH
            }
        } else {
            FlipResult.FLIPPED
        }
    }

    fun flipDownUnmatched() {
        cards.filter { it.isFaceUp && !it.isMatched }.forEach { it.isFaceUp = false }
    }

    fun reset() {
        generateCards()
    }

    enum class FlipResult {
        FLIPPED, MATCH, NO_MATCH, GAME_COMPLETE, INVALID
    }
}
