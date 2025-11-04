package com.example.geoquizjetpack

import androidx.lifecycle.ViewModel

private const val TAG="QuizVieModel"
class QuizViewModel: ViewModel() {
    init {
        android.util.Log.d(TAG,"ViewModel instance created")
    }
}