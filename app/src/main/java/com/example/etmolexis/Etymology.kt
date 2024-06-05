package com.example.etmolexis

import android.graphics.fonts.FontStyle
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.etmolexis.ui.theme.EtmoLexisTheme
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.GoogleGenerativeAIException
import kotlinx.serialization.json.Json
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.Response
import okhttp3.ResponseBody
import okio.IOException
import java.lang.Exception

val apiKey = `YOUR_GEMINI_API_KEY`

var word = ""
var prompt = ""

val generativeModel = GenerativeModel(
    modelName = "gemini-1.5-flash",
    apiKey = apiKey
)
class Etymology : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        word = intent.getStringExtra("word").toString()
        prompt = """
            You are an expert linguist, Unravel the fascinating origins of the word "{{$word}}"!
            Craft a JSON output that details its etymology, including its part of speech, and its various sources, following this structure:
            {
            "word": "{{word}}",
            "wordType": "{{word_type}}",
            "origins": [
            {
            "originLanguage": "{{origin_language_1}}",
            "originDescription": "{{origin_description_1}}",
            "relatedWords": [
            {{related_word_1}}
            ]
            },
            {
            "originLanguage": "{{origin_language_2}}",
            "originDescription": "{{origin_description_2}}",
            "relatedWords": [
            {{related_words_2}}
            ]
            }
            There can be many origins, the origins should be linguistically accurate.
            Engage the reader! Make the descriptions captivating and informative, highlighting the nuances of the word's history.
            Use quotes for non-English words. This helps readers understand the word's roots and pronunciation.
            Focus on etymological related words. List words that share the same origin, even if they have evolved differently over time. and related words should just contains word, nothing else
            Strictly return a JSON, start with { and end with }, no markup
        """
        super.onCreate(savedInstanceState)
        setContent {
            EtmoLexisTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    FetchAndDisplayData()
                }
            }
        }
    }
}

@Composable
fun FetchAndDisplayData() {
    var data by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        try {
            val response = generativeModel.generateContent(prompt)
            data = response.text
            isLoading = false
        } catch (e: Exception) {
            println("SRI $e")
        } finally {
            println("SRI finished!")
        }
    }

    if (isLoading) {
        Loading()
    } else {
        data?.let {
            WordInfo(data!!)
        }
        
//        Text(text = data.toString())
    }
}

@Composable
fun Loading() {
    // A simple loading UI
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}



@Composable
fun WordInfo(jsonOutput: String) {
    val wordData = parseWordData(jsonOutput)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Display Word
        Text(
            text = wordData.word,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFFFAA41A), // Orange color
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Display Type
        Text(
            text = "(${wordData.wordType})",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier.padding(bottom = 16.dp)
        )



        // Display Origins
        LazyColumn(
            modifier = Modifier.fillMaxWidth()
        ) {
            items(wordData.origins.size) { index ->
                OriginInfo(origin = wordData.origins[index])
                Spacer(modifier = Modifier.height(40.dp))
            }


        }
    }
}

@Composable
fun OriginInfo(origin: Origin) {
    Column(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth()
    ) {
        // Origin Number


        // Origin Language
        Text(
            text = "Language: ${origin.originLanguage}",
            fontSize = 16.sp,
            fontStyle = androidx.compose.ui.text.font.FontStyle(FontStyle.FONT_SLANT_ITALIC),
            color = Color.Gray,
            modifier = Modifier.padding(bottom = 4.dp)
        )

        DescriptionWithQuotesItalic(origin.originDescription)

        // Related Words
        Column(
            modifier = Modifier.padding(top = 5.dp, bottom = 3.dp)
        ) {
            Text(
                text = "Related Words: ",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color.DarkGray,
                modifier = Modifier.padding(end = 8.dp)
            )
            Text(
                text = origin.relatedWords.joinToString(separator = ", "),
                fontSize = 14.sp,
                color = Color.Gray,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }
    }
}

@Composable
fun DescriptionWithQuotesItalic(description: String) {
    val annotatedString = buildAnnotatedString {
        var inQuotes = false
        description.split(" ").forEach { word ->
            val trimmedWord = word.trim(',', '.', '!', '?')
            if (trimmedWord.startsWith("'") && trimmedWord.endsWith("'")) {
                inQuotes = true
                withStyle(style = SpanStyle(fontStyle = androidx.compose.ui.text.font.FontStyle.Italic)) {
                    append("$trimmedWord ")
                }
            } else if (trimmedWord.endsWith("'")) {
                withStyle(style = SpanStyle(fontStyle = androidx.compose.ui.text.font.FontStyle.Italic)) {
                    append("$trimmedWord ")
                }
                inQuotes = false
            } else {
                if (inQuotes) {
                    withStyle(style = SpanStyle(fontStyle = androidx.compose.ui.text.font.FontStyle.Italic)) {
                        append("$trimmedWord ")
                    }
                } else {
                    append("$word ")
                }
            }
        }
    }

    Text(
        text = annotatedString,
        fontSize = 16.sp,
        color = Color.Black,
        modifier = Modifier.padding(bottom = 8.dp)
    )
}



fun parseWordData(jsonString: String): WordData {
    val json = Json { ignoreUnknownKeys = true }

    println("SRI $jsonString")
    return json.decodeFromString<WordData>(jsonString)
}