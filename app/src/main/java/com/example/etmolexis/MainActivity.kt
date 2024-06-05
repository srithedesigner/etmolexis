package com.example.etmolexis

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.etmolexis.ui.theme.EtmoLexisTheme
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.GoogleGenerativeAIException

class MainActivity : ComponentActivity() {




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            EtmoLexisTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color(0xFFFFFFFF)
                ) {
                    explorePage()
                }
            }
        }
    }
}




@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun explorePage() {
    val context = LocalContext.current
    var text by remember { mutableStateOf(TextFieldValue("")) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(Color(0xFFFFFFFF)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top


    ) {
        Spacer(modifier = Modifier.height(40.dp))
        Image(
            painter = painterResource(id = R.drawable.img),
            contentDescription = null,
            contentScale = ContentScale.Crop,

            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)

        )
        Spacer(modifier = Modifier.height(20.dp))
        TextField(
            value = text,
            onValueChange = { text = it },
            label = { Text("Search the word!", style = TextStyle(fontWeight = FontWeight.Bold)) },
            textStyle = TextStyle(fontWeight = FontWeight.Bold, fontSize = 18.sp),
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .background(Color.White, RoundedCornerShape(4.dp))
                .border(width = 2.dp, color = Color.Black),

            colors = TextFieldDefaults.textFieldColors(
                disabledTextColor = Color.Black,
                containerColor = Color.White,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                cursorColor = Color.Black,
                focusedTextColor = Color.Black
            )
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = { context.startActivity(Intent(context, Etymology::class.java).apply {
                putExtra("word", text.text)
            }) },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .background(Color.Black, RoundedCornerShape(4.dp)),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
        ) {
            Text("Explore the Origins", fontSize = 18.sp, color = Color.White, fontWeight = FontWeight.Bold)
        }
    }

}