package com.example.geoquizjetpack

import android.content.res.Configuration
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MyScreen()

        }
    }
}



@Composable
fun MyScreen(){
    HandleOrientationChanges()
}

@Composable
fun HandleOrientationChanges(){
    //Settings for landscape or portrait orientation
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    //Asks for orientation
    QuizLogic(isLandscape = isLandscape)
}

/**
 * @param isLandscape Indicates layout type horizontal (true) vertical (false).
 */
@Composable
fun QuizLogic(isLandscape: Boolean) {
    val context = LocalContext.current

    val mQuestionBank = listOf(
        Question(R.string.question_australia, true),
        Question(R.string.question_asia, true),
        Question(R.string.question_africa, false),
        Question(R.string.question_americas, true),
        Question(R.string.question_mideast, false),
        Question(R.string.question_oceans, true)
    )

    var currentIndex by rememberSaveable { mutableIntStateOf(0) }

    val currentQuestion = mQuestionBank[currentIndex]

    val onAnswerClicked: (Boolean) -> Unit = { userAnswer ->
        val isCorrect = userAnswer == currentQuestion.answer
        val msg = if (isCorrect) context.getString(R.string.toast_correct) else context.getString(R.string.toast_incorrect)
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }

    val onNextClicked: () -> Unit = {
        currentIndex = (currentIndex + 1) % mQuestionBank.size
    }

    if (isLandscape) {
        LandscapeLayout(currentQuestion, onAnswerClicked, onNextClicked)
    } else {
        PortraitLayout(currentQuestion, onAnswerClicked, onNextClicked)
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PortraitLayout(
    currentQuestion: Question,
    onAnswerClicked: (Boolean) -> Unit,
    onNextClicked: () -> Unit
) {

    Scaffold(
        topBar = { TopAppBar(title = { Text("GeoQuiz") }) }
    ) { padding ->
        Box(
            Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            Column(
                Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {

                Text(
                    stringResource(currentQuestion.textResId),
                    fontSize = 18.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(bottom = 24.dp)
                )

                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    FilledTonalButton(onClick = { onAnswerClicked(true) }) {
                        Text(stringResource(R.string.btnTrue))
                    }
                    FilledTonalButton(onClick = { onAnswerClicked(false) }) {
                        Text(stringResource(R.string.btnFalse))
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))


                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    FilledTonalButton(onClick = onNextClicked) {
                        Text(text = stringResource(R.string.btnNext))
                    }
                }
            }
        }
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LandscapeLayout(
    currentQuestion: Question,
    onAnswerClicked: (Boolean) -> Unit,
    onNextClicked: () -> Unit
) {
    Scaffold(
        topBar = { TopAppBar(title = { Text("GeoQuiz") }) }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Row(
                modifier = Modifier
                    .align(Alignment.Center)
                    .fillMaxWidth(0.9f),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Column(
                    modifier = Modifier.weight(2f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        stringResource(currentQuestion.textResId),
                        fontSize = 16.sp,
                        textAlign = TextAlign.Center
                    )
                }


                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    FilledTonalButton(onClick = { onAnswerClicked(true) }) {
                        Text(stringResource(R.string.btnTrue))
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    FilledTonalButton(onClick = { onAnswerClicked(false) }) {
                        Text(stringResource(R.string.btnFalse))
                    }
                }
            }


            FilledTonalButton(
                onClick = onNextClicked,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(bottom = 8.dp, end = 8.dp) // Padding ajustado
            ) {
                Text(text = stringResource(R.string.btnNext))
            }
        }
    }
}