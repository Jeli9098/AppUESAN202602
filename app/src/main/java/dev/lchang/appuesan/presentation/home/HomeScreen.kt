package dev.lchang.appuesan.presentation.home

import androidx.compose.foundation.Image
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
import coil.compose.rememberAsyncImagePainter
import dev.lchang.appuesan.data.local.AppDatabase
import dev.lchang.appuesan.data.local.FavoriteCountryEntity
import dev.lchang.appuesan.data.model.CountryModel
import kotlinx.coroutines.launch

val mockCountries = listOf(
    CountryModel("Perú", 50, "https://flagcdn.com/w320/pe.png"),
    CountryModel("Brasil", 11, "https://flagcdn.com/w320/br.png"),
    CountryModel("España", 1, "https://flagcdn.com/w320/es.png"),
    CountryModel("Portugal", 7, "https://flagcdn.com/w320/pt.png"),
    CountryModel("Argentina", 4, "https://flagcdn.com/w320/ar.png"),
    CountryModel("Japón", 30, "https://flagcdn.com/w320/jp.png"),
    CountryModel("Francia", 5, "https://flagcdn.com/w320/fr.png")
)

@Composable
fun HomeScreen() {

    val context = LocalContext.current
    val dao = remember(context) {
        AppDatabase.getDatabase(context).favoriteCountryDao()
    }
    val favoritos by remember(dao) {
        dao.getAllFavorites()
    }.collectAsState(initial = emptyList())

    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Ranking FIFA 2026",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text("Favoritos guardados: ${favoritos.size}")

        Spacer(modifier = Modifier.height(12.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(mockCountries, key = { it.name }) { country ->

                val guardado = favoritos.any {
                    it.name == country.name
                }

                Card(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Image(
                            painter = rememberAsyncImagePainter(country.imageUrl),
                            contentDescription = country.name,
                            modifier = Modifier.size(64.dp),
                            contentScale = ContentScale.Crop
                        )

                        Spacer(modifier = Modifier.width(12.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(country.name)
                            Text("Ranking FIFA: ${country.ranking}")
                        }

                        IconButton(
                            onClick = {
                                scope.launch {
                                    val entity = FavoriteCountryEntity(
                                        name = country.name,
                                        ranking = country.ranking,
                                        imageUrl = country.imageUrl
                                    )

                                    if (guardado) {
                                        dao.delete(entity)
                                    } else {
                                        dao.insert(entity)
                                    }
                                }
                            }
                        ) {
                            Text(
                                text = if (guardado) "♥" else "♡",
                                style = MaterialTheme.typography.headlineLarge,
                                color = if (guardado)
                                    MaterialTheme.colorScheme.error
                                else
                                    MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            }
        }
    }
}