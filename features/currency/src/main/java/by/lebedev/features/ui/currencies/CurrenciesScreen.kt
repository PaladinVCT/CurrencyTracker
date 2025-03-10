package by.lebedev.features.ui.currencies

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
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavHostController
import by.lebedev.core.R
import by.lebedev.core.data.UiState
import by.lebedev.core.data.model.BaseCurrency
import by.lebedev.core.data.model.Currencies
import by.lebedev.core.data.model.FilterOption
import by.lebedev.core.data.model.Rate
import by.lebedev.core.ui.theme.bgHeader
import by.lebedev.core.ui.theme.primary
import by.lebedev.core.ui.theme.yellow_favourite
import by.lebedev.core.util.Dimens
import by.lebedev.features.ui.navigation.model.CurrenciesScreenRoutes
import java.util.Locale


@Composable
fun CurrenciesScreen(
    viewModel: CurrenciesViewModel = hiltViewModel(),
    navController: NavHostController
) {
    val uiStateCurrencies by viewModel.uiStateCurrencies.collectAsState()
    val appliedFilter by viewModel.appliedFilter.collectAsState()
    val baseCurrency by viewModel.baseCurrency.collectAsState()

    val lifecycleOwner = LocalLifecycleOwner.current
    LaunchedEffect(Unit) {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.getAppliedFiltersCall()
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
    ) {
        Column {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.currencies), fontWeight = FontWeight.Bold
                    )
                },
                colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = bgHeader),
                modifier = Modifier.shadow(elevation = Dimens.dp0)
            )

            BaseCurrencyHeader(currencies = BaseCurrency.values().map { it.name },
                selectedCurrency = baseCurrency.name,
                onCurrencySelected = {
                    viewModel.setBaseCurrency(BaseCurrency.valueOf(it))
                },
                onFilterClick = {
                    navController.navigate(route = CurrenciesScreenRoutes.Filter.route)
                })

            when (uiStateCurrencies) {
                is UiState.Loading -> {
                    viewModel.getCurrenciesApiCall(baseCurrency)
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
                            text = (uiStateCurrencies as UiState.Error).errorMessage,
                            color = primary
                        )
                    }
                }

                is UiState.Success -> {
                    val rates =
                        (uiStateCurrencies as UiState.Success<Currencies>).data.rates

                    LazyColumn(
                        contentPadding = PaddingValues(Dimens.dp16),
                        verticalArrangement = Arrangement.spacedBy(Dimens.dp16)
                    ) {
                        item(rates.size) {

                            val filteredRates = when (appliedFilter?.filter) {
                                FilterOption.A_Z -> rates.sortedBy { it.secondaryCurrency }
                                FilterOption.Z_A -> rates.sortedByDescending { it.secondaryCurrency }
                                FilterOption.QUOTE_ASC -> rates.sortedBy { it.price }
                                FilterOption.QUOTE_DESC -> rates.sortedByDescending { it.price }

                                null -> rates.sortedBy { it.secondaryCurrency }
                            }

                            filteredRates.forEach { rate ->
                                CurrencyListItem(
                                    rate = rate,
                                    onFavoriteClick = {
                                        viewModel.onFavouriteClick(it)
                                    },
                                    isFavourite = viewModel.isInFavourites(rate)
                                )
                                Spacer(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(8.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CurrencyListItem(
    rate: Rate = Rate(BaseCurrency.USD.name, 3.12),
    onFavoriteClick: ((Rate) -> Unit)? = null,
    isFavourite: Boolean = false
) {

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(Dimens.dp48)
            .clip(RoundedCornerShape(Dimens.dp12)),
        color = primary.copy(alpha = 0.1f)
    ) {
        Row(
            modifier = Modifier.padding(Dimens.dp14),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = rate.secondaryCurrency, fontSize = Dimens.sp14, fontWeight = FontWeight.Medium)

            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = String.format(Locale.US, "%.6f", rate.price),
                fontSize = Dimens.sp16,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.size(Dimens.dp8))
            IconButton(
                modifier = Modifier.width(Dimens.dp24),
                onClick = { onFavoriteClick?.let { it(rate) } }) {
                Icon(
                    painter = painterResource(id = if (isFavourite) R.drawable.ic_star_on else R.drawable.ic_star_off),
                    tint = if (isFavourite) yellow_favourite else primary.copy(alpha = 0.5f),
                    contentDescription = "Star",
                )
            }
        }
    }
}

@Composable
fun BaseCurrencyHeader(
    currencies: List<String>,
    selectedCurrency: String,
    onCurrencySelected: (String) -> Unit,
    onFilterClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = Dimens.dp16, vertical = Dimens.dp8),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(Dimens.dp8)
    ) {
        Box(modifier = Modifier.weight(1f)) {
            CustomDropdown(
                currencies = currencies,
                selectedCurrency = selectedCurrency,
                onCurrencySelected = onCurrencySelected
            )
        }
        Spacer(modifier = Modifier.height(Dimens.dp8))
        Box() {
            FilterButton(onClick = onFilterClick)
        }
    }
}

@Composable
fun CustomDropdown(
    currencies: List<String>,
    selectedCurrency: String,
    onCurrencySelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Surface(
        shape = RoundedCornerShape(Dimens.dp8),
        modifier = Modifier
            .wrapContentWidth()
            .border(
                width = 1.dp, color = primary.copy(alpha = 0.4f), shape = RoundedCornerShape(Dimens.dp8)
            ),
        color = White,

        ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,

            ) {
            Row(modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = !expanded }
                .padding(Dimens.dp12),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween) {
                Text(
                    text = selectedCurrency, color = Color.Black, fontSize = 14.sp
                )
                Icon(
                    painter = painterResource(if (expanded) R.drawable.ic_arrow_up else R.drawable.ic_arrow_down),
                    contentDescription = "Dropdown",
                    tint = primary,
                    modifier = Modifier.size(Dimens.dp24)
                )
            }

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier
                    .width(300.dp)
                    .background(White)
            ) {
                currencies.forEach { currency ->
                    DropdownMenuItem(onClick = {
                        onCurrencySelected(currency)
                        expanded = false
                    }, modifier = Modifier.width(300.dp), text = {
                        Text(
                            text = currency,
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(Dimens.dp8)
                        )
                    })
                }
            }
        }
    }
}

@Composable
fun FilterButton(onClick: () -> Unit) {
    IconButton(
        onClick = onClick,
        modifier = Modifier
            .size(Dimens.dp40)
            .padding(end = Dimens.dp8)
            .clip(RoundedCornerShape(Dimens.dp8))
            .border(
                width = Dimens.dp1, color = primary.copy(alpha = 0.4f), shape = RoundedCornerShape(Dimens.dp8)
            ),
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_filter),
            tint = primary,
            contentDescription = stringResource(id = R.string.filter)
        )
    }
}
