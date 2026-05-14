package com.example.nallanudi

import android.os.Bundle
import android.speech.tts.TextToSpeech
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.util.Locale

class MainActivity : ComponentActivity() {

    private lateinit var textToSpeech: TextToSpeech

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        textToSpeech = TextToSpeech(this) {
            textToSpeech.language = Locale.US
        }

        setContent {
            NallaNudiApp(textToSpeech)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        textToSpeech.stop()
        textToSpeech.shutdown()
    }
}

@Composable
fun NallaNudiApp(textToSpeech: TextToSpeech) {

    var currentScreen by remember {
        mutableStateOf("home")
    }

    var savedWords by remember {
        mutableStateOf(listOf<TechnicalWord>())
    }

    var isDarkMode by remember {
        mutableStateOf(false)
    }

    MaterialTheme(

        colorScheme =
            if (isDarkMode)
                darkColorScheme()
            else
                lightColorScheme()

    ) {

        Scaffold(

            bottomBar = {

                NavigationBar {

                    NavigationBarItem(
                        selected = currentScreen == "home",
                        onClick = {
                            currentScreen = "home"
                        },
                        icon = {
                            Icon(Icons.Default.Home, "")
                        },
                        label = {
                            Text("Home")
                        }
                    )

                    NavigationBarItem(
                        selected = currentScreen == "dictionary",
                        onClick = {
                            currentScreen = "dictionary"
                        },
                        icon = {
                            Icon(Icons.Default.Search, "")
                        },
                        label = {
                            Text("Dictionary")
                        }
                    )

                    NavigationBarItem(
                        selected = currentScreen == "quiz",
                        onClick = {
                            currentScreen = "quiz"
                        },
                        icon = {
                            Icon(Icons.Default.Star, "")
                        },
                        label = {
                            Text("Quiz")
                        }
                    )

                    NavigationBarItem(
                        selected = currentScreen == "saved",
                        onClick = {
                            currentScreen = "saved"
                        },
                        icon = {
                            Icon(Icons.Default.Star, "")
                        },
                        label = {
                            Text("Saved")
                        }
                    )

                    NavigationBarItem(
                        selected = currentScreen == "profile",
                        onClick = {
                            currentScreen = "profile"
                        },
                        icon = {
                            Icon(Icons.Default.Person, "")
                        },
                        label = {
                            Text("Profile")
                        }
                    )
                }
            }

        ) { padding ->

            Box(
                modifier = Modifier.padding(padding)
            ) {

                when (currentScreen) {

                    "home" -> {
                        HomeScreen()
                    }

                    "dictionary" -> {

                        DictionaryScreen(
                            textToSpeech = textToSpeech,
                            savedWords = savedWords,

                            onSaveWord = { word ->

                                if (!savedWords.contains(word)) {
                                    savedWords = savedWords + word
                                }
                            }
                        )
                    }

                    "quiz" -> {
                        QuizScreen()
                    }

                    "saved" -> {
                        SavedScreen(savedWords)
                    }

                    "profile" -> {

                        ProfileScreen(
                            isDarkMode = isDarkMode,

                            onToggleDarkMode = {
                                isDarkMode = !isDarkMode
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun HomeScreen() {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),

        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(modifier = Modifier.height(40.dp))

        Text(
            text = "📘",
            fontSize = 80.sp
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Nalla-Nudi",
            fontSize = 34.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF7B61FF)
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Learn Technical English Easily",
            fontSize = 18.sp
        )

        Spacer(modifier = Modifier.height(40.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),

            colors = CardDefaults.cardColors(
                containerColor = Color(0xFF7B61FF)
            )
        ) {

            Column(
                modifier = Modifier.padding(24.dp)
            ) {

                Text(
                    text = "Word of the Day",
                    color = Color.White
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "Artificial Intelligence",
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Machines performing human-like intelligence.",
                    color = Color.White
                )
            }
        }
    }
}

@Composable
fun DictionaryScreen(
    textToSpeech: TextToSpeech,
    savedWords: List<TechnicalWord>,
    onSaveWord: (TechnicalWord) -> Unit
) {

    var searchText by remember {
        mutableStateOf("")
    }

    val filteredWords = technicalWords.filter {

        it.english.contains(
            searchText,
            ignoreCase = true
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {

        Text(
            text = "Dictionary",
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(
            value = searchText,

            onValueChange = {
                searchText = it
            },

            placeholder = {
                Text("Search technical word...")
            },

            leadingIcon = {
                Icon(Icons.Default.Search, "")
            },

            modifier = Modifier.fillMaxWidth(),

            shape = RoundedCornerShape(16.dp)
        )

        Spacer(modifier = Modifier.height(20.dp))

        filteredWords.forEach { word ->

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),

                shape = RoundedCornerShape(16.dp)
            ) {

                Column(
                    modifier = Modifier.padding(16.dp)
                ) {

                    Text(
                        text = word.english,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = word.kannada,
                        color = Color(0xFF7B61FF),
                        fontSize = 18.sp
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(word.explanation)

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement =
                            Arrangement.SpaceBetween
                    ) {

                        Button(
                            onClick = {

                                textToSpeech.speak(
                                    word.english,
                                    TextToSpeech.QUEUE_FLUSH,
                                    null,
                                    null
                                )
                            }
                        ) {

                            Text("🔊 Pronounce")
                        }

                        Button(
                            onClick = {
                                onSaveWord(word)
                            }
                        ) {

                            if (savedWords.contains(word)) {
                                Text("✅ Saved")
                            } else {
                                Text("⭐ Save")
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(100.dp))
    }
}

@Composable
fun QuizScreen() {

    val quizQuestions = listOf(

        QuizQuestion(
            "What is AI?",
            listOf(
                "Plant process",
                "Machine intelligence",
                "Math formula",
                "Virus"
            ),
            "Machine intelligence"
        ),

        QuizQuestion(
            "What is Database?",
            listOf(
                "Network cable",
                "Math theorem",
                "Collection of data",
                "Chemical reaction"
            ),
            "Collection of data"
        ),

        QuizQuestion(
            "What is Gravity?",
            listOf(
                "Programming language",
                "Force pulling objects",
                "Cloud storage",
                "Business strategy"
            ),
            "Force pulling objects"
        ),

        QuizQuestion(
            "What is Algorithm?",
            listOf(
                "Internet cable",
                "Physics law",
                "Step-by-step solution",
                "Plant food process"
            ),
            "Step-by-step solution"
        ),

        QuizQuestion(
            "What is Cybersecurity?",
            listOf(
                "Cooking method",
                "Protection from cyber attacks",
                "Plant growth",
                "Music system"
            ),
            "Protection from cyber attacks"
        ),

        QuizQuestion(
            "What is Robotics?",
            listOf(
                "Study of robots",
                "Math formula",
                "Business plan",
                "Medical test"
            ),
            "Study of robots"
        )
    )

    var currentQuestionIndex by remember {
        mutableIntStateOf(0)
    }

    var score by remember {
        mutableIntStateOf(0)
    }

    var selectedAnswer by remember {
        mutableStateOf("")
    }

    var answered by remember {
        mutableStateOf(false)
    }

    val currentQuestion =
        quizQuestions[currentQuestionIndex]

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),

        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "🧠 Quiz Mode",
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "Score: $score",
            fontSize = 20.sp,
            color = Color(0xFF7B61FF)
        )

        Spacer(modifier = Modifier.height(30.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp)
        ) {

            Column(
                modifier = Modifier.padding(20.dp)
            ) {

                Text(
                    text = currentQuestion.question,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(20.dp))

                currentQuestion.options.forEach { option ->

                    val buttonColor = when {

                        answered &&
                                option == currentQuestion.correctAnswer ->
                            Color(0xFF4CAF50)

                        answered &&
                                option == selectedAnswer &&
                                option != currentQuestion.correctAnswer ->
                            Color(0xFFF44336)

                        else ->
                            Color(0xFF7B61FF)
                    }

                    Button(

                        onClick = {

                            if (!answered) {

                                answered = true
                                selectedAnswer = option

                                if (
                                    option ==
                                    currentQuestion.correctAnswer
                                ) {
                                    score++
                                }
                            }
                        },

                        enabled = !answered,

                        colors = ButtonDefaults.buttonColors(
                            containerColor = buttonColor
                        ),

                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp)
                    ) {

                        Text(
                            text = option,
                            color = Color.White
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Button(

                    onClick = {

                        if (
                            currentQuestionIndex <
                            quizQuestions.size - 1
                        ) {

                            currentQuestionIndex++
                            answered = false
                            selectedAnswer = ""

                        } else {

                            currentQuestionIndex = 0
                            score = 0
                            answered = false
                            selectedAnswer = ""
                        }
                    },

                    modifier = Modifier.fillMaxWidth()
                ) {

                    if (
                        currentQuestionIndex ==
                        quizQuestions.size - 1
                    ) {

                        Text("🔄 Restart Quiz")

                    } else {

                        Text("➡ Next Question")
                    }
                }
            }
        }
    }
}

@Composable
fun SavedScreen(savedWords: List<TechnicalWord>) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {

        Text(
            text = "Saved Words",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(20.dp))

        if (savedWords.isEmpty()) {

            Text("No saved words yet.")

        } else {

            savedWords.forEach { word ->

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),

                    shape = RoundedCornerShape(16.dp)
                ) {

                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {

                        Text(
                            text = word.english,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = word.kannada,
                            color = Color(0xFF7B61FF),
                            fontSize = 18.sp
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(word.explanation)
                    }
                }
            }
        }
    }
}

@Composable
fun ProfileScreen(
    isDarkMode: Boolean,
    onToggleDarkMode: () -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),

        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(modifier = Modifier.height(40.dp))

        Text(
            text = "👨‍🎓",
            fontSize = 80.sp
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Student Profile",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(30.dp))

        Button(
            onClick = {
                onToggleDarkMode()
            }
        ) {

            if (isDarkMode) {
                Text("☀ Light Mode")
            } else {
                Text("🌙 Dark Mode")
            }
        }
    }
}

val technicalWords = listOf(

    TechnicalWord(
        "Artificial Intelligence",
        "ಕೃತಕ ಬುದ್ಧಿಮತ್ತೆ",
        "Machines performing human-like intelligence."
    ),

    TechnicalWord(
        "Machine Learning",
        "ಯಂತ್ರ ಕಲಿಕೆ",
        "Computers learning from data."
    ),

    TechnicalWord(
        "Database",
        "ಡೇಟಾಬೇಸ್",
        "Collection of organized data."
    ),

    TechnicalWord(
        "Algorithm",
        "ಅಲ್ಗೋರಿದಮ್",
        "Step-by-step solution process."
    ),

    TechnicalWord(
        "Cybersecurity",
        "ಸೈಬರ್ ಭದ್ರತೆ",
        "Protection of systems from attacks."
    ),

    TechnicalWord(
        "Cloud Computing",
        "ಮೋಡ ಗಣಕಯಂತ್ರ",
        "Internet-based computing services."
    ),

    TechnicalWord(
        "Blockchain",
        "ಬ್ಲಾಕ್‌ಚೈನ್",
        "Secure digital transaction technology."
    ),

    TechnicalWord(
        "Robotics",
        "ರೋಬೋಟಿಕ್ಸ್",
        "Study of robots and automation."
    ),

    TechnicalWord(
        "Quantum Physics",
        "ಕ್ವಾಂಟಂ ಭೌತಶಾಸ್ತ್ರ",
        "Study of atomic particles."
    ),

    TechnicalWord(
        "DNA",
        "ಡಿಎನ್‌ಎ",
        "Carrier of genetic information."
    ),

    TechnicalWord(
        "Photosynthesis",
        "ಪ್ರಕಾಶ ಸಂಶ್ಲೇಷಣೆ",
        "Plants making food using sunlight."
    ),

    TechnicalWord(
        "Economics",
        "ಅರ್ಥಶಾಸ್ತ್ರ",
        "Study of money and business."
    ),

    TechnicalWord(
        "Gravity",
        "ಗುರುತ್ವಾಕರ್ಷಣೆ",
        "Force pulling objects toward earth."
    ),

    TechnicalWord(
        "Electricity",
        "ವಿದ್ಯುತ್",
        "Energy from electric charge."
    ),

    TechnicalWord(
        "Software",
        "ಸಾಫ್ಟ್‌ವೇರ್",
        "Programs used in computers."
    )
)

data class TechnicalWord(
    val english: String,
    val kannada: String,
    val explanation: String
)

data class QuizQuestion(
    val question: String,
    val options: List<String>,
    val correctAnswer: String
)