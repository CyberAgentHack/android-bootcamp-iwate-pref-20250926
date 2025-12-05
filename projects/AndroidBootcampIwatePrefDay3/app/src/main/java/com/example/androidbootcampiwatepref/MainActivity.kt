package com.example.androidbootcampiwatepref

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.androidbootcampiwatepref.data.datastore.AppDataStore
import com.example.androidbootcampiwatepref.domain.domainobject.News
import com.example.androidbootcampiwatepref.viewmodel.NewsViewModel
import com.example.androidbootcampiwatepref.ui.theme.AndroidBootcampIwatePrefTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AndroidBootcampIwatePrefTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val context = LocalContext.current
                    val newsViewModel = viewModel {
                        NewsViewModel(
                        dataStore = AppDataStore(context = context),
                        )
                    }
                    val uiState by newsViewModel.uiState.collectAsStateWithLifecycle()
                    Column {
                        NewsList(
                            newsList = uiState.newsList,
                            modifier = Modifier.weight(1f)
                        )
                        Button(
                            onClick = { newsViewModel.toggleNewsListOrder() },
                            modifier = Modifier.fillMaxWidth().padding(16.dp),
                        ) {
                            Text(text = "並び替えの切り替え")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun NewsList(
    newsList: List<News>,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        items(
            items = newsList,
            key = { news -> news.id },
        ) { news ->
            News(
                news = news,
                modifier = Modifier.animateItem(),
            )
        }
    }
}

@Composable
fun News(
    news: News,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
    ) {
        Column(
            modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp),
        ) {
            Text(
                text = news.title,
            )
            Text(
                text = news.body,
            )
        }
    }
}
