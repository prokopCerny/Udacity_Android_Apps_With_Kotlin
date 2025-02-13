package com.example.android.guesstheword.screens.game

import android.os.CountDownTimer
import android.text.format.DateUtils
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import timber.log.Timber

private val CORRECT_BUZZ_PATTERN = longArrayOf(100, 100, 100, 100, 100, 100)
private val PANIC_BUZZ_PATTERN = longArrayOf(0, 200)
private val GAME_OVER_BUZZ_PATTERN = longArrayOf(0, 2000)
private val NO_BUZZ_PATTERN = longArrayOf(0)

class GameViewModel : ViewModel() {
    enum class BuzzType(val pattern: LongArray) {
        CORRECT(CORRECT_BUZZ_PATTERN),
        GAME_OVER(GAME_OVER_BUZZ_PATTERN),
        COUNTDOWN_PANIC(PANIC_BUZZ_PATTERN),
        NO_BUZZ(NO_BUZZ_PATTERN)
    }

    companion object {
        // These represent different important times
        // This is when the game is over
        const val DONE = 0L
        // This is the number of milliseconds in a second
        const val ONE_SECOND = 1000L
        // This is the total time of the game
        const val COUNTDOWN_TIME = 60000L

        const val COUNTDOWN_PANIC_SECONDS = 10L
    }

    // Timer data
    private val timer: CountDownTimer
    private val _currentTime = MutableLiveData<Long>(0L)
    val currentTime : LiveData<Long> get() = _currentTime

    val currentTimeString = Transformations.map(currentTime) { time -> DateUtils.formatElapsedTime(time) }

    // Game Finished state
    private val _eventGameFinished = MutableLiveData<Boolean>(false)
    val eventGameFinished: LiveData<Boolean> get() = _eventGameFinished

    // The current word
    private val _word = MutableLiveData<String>("")
    val word : LiveData<String> get() = _word

    // The current score
    private val _score = MutableLiveData<Int>(0)
    val score : LiveData<Int> get() = _score

    // Buzzing
    private val _eventBuzz = MutableLiveData<BuzzType>(BuzzType.NO_BUZZ)
    val eventBuzz: LiveData<BuzzType>
        get() = _eventBuzz

    // The list of words - the front of the list is the next word to guess
    private lateinit var wordList: MutableList<String>

    init {
        Timber.i("GameViewModel created!")
        resetList()
        nextWord()
        timer = object : CountDownTimer(COUNTDOWN_TIME, ONE_SECOND) {
            override fun onFinish() {
                _currentTime.value = DONE
                _eventGameFinished.value = true
                _eventBuzz.value = BuzzType.GAME_OVER
            }

            override fun onTick(millisecondsToEnd: Long) {
                _currentTime.value = (millisecondsToEnd / ONE_SECOND).also { remainingSeconds ->
                    if (remainingSeconds < COUNTDOWN_PANIC_SECONDS) {
                        _eventBuzz.value = BuzzType.COUNTDOWN_PANIC
                    }
                }
            }
        }
        timer.start()
    }

    override fun onCleared() {
        super.onCleared()
        Timber.i("GameViewModel destroyed.")
        timer.cancel()
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
            resetList()
        }
        _word.value = wordList.removeAt(0)
    }

    fun onSkip() {
        _score.value = _score.value?.minus(1)
        nextWord()
    }

    fun onCorrect() {
        _score.value = _score.value?.plus(1)
        _eventBuzz.value = BuzzType.CORRECT
        nextWord()
    }

    fun onGameFinishComplete() {
        _eventGameFinished.value = false
    }

    fun onBuzzComplete() {
        _eventBuzz.value = BuzzType.NO_BUZZ
    }
}