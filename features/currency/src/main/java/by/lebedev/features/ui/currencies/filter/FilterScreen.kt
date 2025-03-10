package by.lebedev.features.ui.currencies.filter

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.RadioButton
import androidx.compose.material.RadioButtonDefaults
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import by.lebedev.core.R
import by.lebedev.core.data.UiState
import by.lebedev.core.data.model.FilterOption
import by.lebedev.core.ui.theme.bgHeader
import by.lebedev.core.ui.theme.primary
import by.lebedev.core.util.Dimens


@Composable
fun FilterScreen(
    onBackClick: () -> Unit,
    viewModel: FilterViewModel = hiltViewModel(),
) {
    var selectedOption by remember { mutableStateOf(FilterOption.A_Z) }
    val uiStateFilter by viewModel.uiStateFilter.collectAsState()

    when (uiStateFilter) {
        is UiState.Loading -> {
            viewModel.getAppliedFiltersCall()
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
            Toast.makeText(
                LocalContext.current,
                (uiStateFilter as UiState.Error).errorMessage,
                Toast.LENGTH_SHORT
            ).show()
        }

        is UiState.Success -> {

            selectedOption = (uiStateFilter as UiState.Success<FilterOption>).data

            Scaffold(
                topBar = {
                    TopAppBar(
                        title = {
                            Text(
                                text = stringResource(R.string.filters),
                                fontWeight = FontWeight.Bold
                            )
                        },
                        navigationIcon = {
                            IconButton(onClick = onBackClick) {
                                Icon(
                                    imageVector = Icons.Filled.ArrowBack,
                                    contentDescription = "Back",
                                    tint = primary
                                )
                            }
                        },
                        backgroundColor = bgHeader,
                        contentColor = Color.Black,
                        elevation = Dimens.dp0
                    )
                },
                bottomBar = {
                    Button(
                        onClick = {
                            viewModel.saveAppliedFiltersCall(selectedOption)
                            onBackClick()
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(Dimens.dp16),
                        colors = ButtonDefaults.buttonColors(backgroundColor = primary),
                        shape = RoundedCornerShape(Dimens.dp100)
                    ) {
                        Text("Apply", color = Color.White)
                    }
                }
            ) { padding ->
                Column(
                    modifier = Modifier
                        .padding(padding)
                        .padding(Dimens.dp16)
                ) {
                    Text(
                        text = stringResource(R.string.sort_by),
                        fontWeight = FontWeight.Bold,
                        fontSize = Dimens.sp14,
                        color = Color.Gray
                    )
                    Spacer(modifier = Modifier.height(Dimens.dp8))

                    val sortOptions = FilterOption.values()
                    sortOptions.forEach { option ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { selectedOption = option }
                                .padding(vertical = Dimens.dp8),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = option.strValue, fontSize = Dimens.sp16)
                            RadioButton(
                                selected = (selectedOption == option),
                                onClick = { selectedOption = option },
                                colors = RadioButtonDefaults.colors(selectedColor = Color(0xFF003DA5)) // Dark blue color
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FilterScreenPreview() {
    FilterScreen(
        onBackClick = {
            println("Back clicked")
        }
    )
}