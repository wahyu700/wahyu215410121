package com.example.wahyu215410121

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.*
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import androidx.compose.ui.tooling.preview.Preview as Preview1

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FilmRecommendationApp()
        }
    }
}

// Data Model
data class Film(val title: String, val genre: String, val rating: Double, val imageRes: Int)

// ViewModel
class FilmViewModel : ViewModel() {
    private val _filmList = MutableStateFlow(
        listOf(
            Film("Film A", "Action", 4.5, R.drawable.wahyu1),
            Film("Film B", "Drama", 4.2, R.drawable.wahyu2),
            Film("Film C", "Comedy", 4.0, R.drawable.wahyu3)
        )
    )
    val filmList: StateFlow<List<Film>> = _filmList

    fun searchFilm(query: String) {
        _filmList.value = _filmList.value.filter {
            it.title.contains(query, ignoreCase = true)
        }
    }
}

// Main Composable Function
@JvmOverloads
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilmRecommendationApp(viewModel: FilmViewModel = viewModel) {
    val filmList by viewModel.filmList.collectAsState()
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Rekomendasi Film Terkini") })
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                SearchBar { query -> viewModel.searchFilm(query) }
                FilmList(filmList)
            }
        }
    )
}

// Search Bar Component
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar(onSearch: (String) -> Unit) {
    var text by remember { mutableStateOf("") }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        TextField(
            value = text,
            onValueChange = { text = it },
            label = { Text("Cari Film") },
            modifier = Modifier.weight(1f)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Button(onClick = { onSearch(text) }) {
            Text("Cari")
        }
    }
}

// Film List Component
@Composable
fun FilmList(filmList: List<Film>) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(filmList) { film ->
            FilmCard(film)
        }
    }
}

// Film Card Component
@Composable
fun FilmCard(film: Film) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        elevation = 4.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(film.imageRes),
                contentDescription = film.title,
                modifier = Modifier.size(100.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(text = film.title, style = MaterialTheme.typography.headlineLarge, fontWeight = FontWeight.Bold)
                Text(text = "Genre: ${film.genre}", style = MaterialTheme.typography.bodyLarge)
                Text(text = "Rating: ${film.rating}", style = MaterialTheme.typography.bodyLarge)
            }
        }
    }
}

// Preview Function
@Preview1(showBackground = true)
@Composable
fun DefaultPreview() {
    FilmRecommendationApp()
}
