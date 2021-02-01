package com.example.android.guesstheword.screens.game

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import timber.log.Timber

class GameViewModel : ViewModel() {

    // Game Finished state
    private val _eventGameFinished = MutableLiveData<Boolean>(false)
    val eventGameFinished: LiveData<Boolean> get() = _eventGameFinished

    // The current word
    private val _word = MutableLiveData<String>("")
    val word : LiveData<String> get() = _word

    // The current score
    private val _score = MutableLiveData<Int>(0)
    val score : LiveData<Int> get() = _score

    // The list of words - the front of the list is the next word to guess
    private lateinit var wordList: MutableList<String>

    init {
        Timber.i("GameViewModel created!")
        resetList()
        nextWord()
    }

    override fun onCleared() {
        super.onCleared()
        Timber.i("GameViewModel destroyed.")
    }

    /**
     * Resets the list of words and randomizes the order
     */
    private fun resetList() {
        wordList = mutableListOf(
                "queen",
                "hospital",
                "basketball",
                "cat",
                "change",
                "snail",
                "soup",
                "calendar",
                "sad",
                "desk",
                "guitar",
                "home",
                "railway",
                "zebra",
                "jelly",
                "car",
                "crow",
                "trade",
                "bag",
                "roll",
                "bubble"
        )
        wordList.shuffle()
    }

    /**
     * Moves to the next word in the list
     */
    private fun nextWord() {
        //Select and remove a word from the list
        if (wordList.isEmpty()) {
            _eventGameFinished.value = true
        } else {
            _word.value = wordList.removeAt(0)
        }
    }

    fun onSkip() {
        _score.value = _score.value?.minus(1)
        nextWord()
    }

    fun onCorrect() {
        _score.value = _score.value?.plus(1)
        nextWord()
    }

    fun onGameFinishComplete() {
        _eventGameFinished.value = false
    }
}