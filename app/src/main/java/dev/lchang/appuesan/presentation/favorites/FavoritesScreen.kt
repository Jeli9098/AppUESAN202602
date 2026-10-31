package dev.lchang.appuesan.presentation.favorites

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import dev.lchang.appuesan.data.local.AppDatabase
import kotlinx.coroutines.launch

@Composable
fun FavoritesScreen() {
    val context = LocalContext.current
    val dao = remember(context) {
        AppDatabase.getDatabase(context).favoriteCountryDao()
    }

    val favorites by remember(dao) {
        dao.getAllFavorites()
    }.collectAsState(initial = emptyList())

    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp)
    ) {
        Text(
            text = "Mis paises favoritos",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text("Guardados: ${favorites.size}")

        Spacer(modifier = Modifier.height(12.dp))

        if (favorites.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("Todavia no tienes paises favoritos")
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(favorites, key = { it.name }) { country ->
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            AsyncImage(
                                model = country.imageUrl,
                                contentDescription = country.name,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.size(56.dp)
                            )

                            Spacer(modifier = Modifier.width(12.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Text(country.name)
                                Text("Ranking FIFA: ${country.ranking}")
                            }

                            TextButton(
                                onClick = {
                                    scope.launch {
                                        dao.delete(country)
                                    }
                                }
                            ) {
                                Text("Quitar")
                            }
                        }
                    }
                }
            }
        }
    }
}