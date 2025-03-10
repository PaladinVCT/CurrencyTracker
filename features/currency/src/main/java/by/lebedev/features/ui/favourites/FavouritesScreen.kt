package by.lebedev.features.ui.favourites

import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import by.lebedev.core.R
import by.lebedev.core.data.UiState
import by.lebedev.core.data.datasource.local.db.entity.CurrencyPairEntity
import by.lebedev.core.ui.theme.bgHeader
import by.lebedev.core.ui.theme.primary
import by.lebedev.core.ui.theme.yellow_favourite
import by.lebedev.core.util.Dimens
import java.util.Locale


@Composable
fun FavouritesScreen(
    viewModel: FavouritesViewModel = hiltViewModel()
) {

    val uiStateFavourites by viewModel.uiStateFavourites.collectAsState()

    Surface(
        modifier = Modifier.fillMaxSize(),
    ) {
        Column {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.favourites), fontWeight = FontWeight.Bold
                    )
                },
                colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = bgHeader),
                modifier = Modifier.shadow(elevation = Dimens.dp0)
            )

            when (uiStateFavourites) {
                is UiState.Loading -> {
                    viewModel.observeCurrencyPairsDbCall()
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularProgressIndicator(
                            color = primary
                        )
                    }
                }

                is UiState.Error -> {

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(Dimens.dp16),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = (uiStateFavourites as UiState.Error).errorMessage,
                            color = primary
                        )
                    }
                }

                is UiState.Success -> {
                    val pairs =
                        (uiStateFavourites as UiState.Success<List<CurrencyPairEntity>>).data

                    LazyColumn(
                        contentPadding = PaddingValues(Dimens.dp16),
                        verticalArrangement = Arrangement.spacedBy(Dimens.dp16)
                    ) {
                        item(pairs.size) {

                            pairs.forEach { pair ->
                                PairListItem(
                                    pair = pair,
                                    onFavoriteClick = {
                                        viewModel.onDeleteFavouriteClick(it)
                                    }
                                )
                                Spacer(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(Dimens.dp8)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PairListItem(
    pair: CurrencyPairEntity,
    onFavoriteClick: ((CurrencyPairEntity) -> Unit)
) {

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .clip(RoundedCornerShape(Dimens.dp12)),
        color = primary.copy(alpha = 0.1f)
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "${pair.baseCurrency}/ ${pair.secondaryCurrency}",
                fontSize = Dimens.sp14,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = String.format(Locale.US, "%.6f", pair.price),
                fontSize = Dimens.sp16,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.size(Dimens.dp8))
            IconButton(
                modifier = Modifier.width(Dimens.dp24),
                onClick = { onFavoriteClick(pair) }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_star_on),
                    tint = yellow_favourite,
                    contentDescription = "Star",
                )
            }
        }
    }
}
