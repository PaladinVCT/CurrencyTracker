package by.lebedev.core.data.repository.currency

import by.lebedev.core.data.datasource.remote.ApiService
import by.lebedev.core.data.model.BaseCurrency
import by.lebedev.core.data.model.CurrenciesResponse
import by.lebedev.core.data.model.mapper.CurrenciesResponseMapper
import by.lebedev.core.util.UtilTests
import com.google.gson.Gson
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.given
import org.mockito.kotlin.whenever


@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class CurrencyRepositoryImplTest {

    @Mock
    private lateinit var apiService: ApiService
    private lateinit var currenciesResponseMapper: CurrenciesResponseMapper
    private lateinit var currencyRepositoryImpl: CurrencyRepositoryImpl

    @Before
    fun setUp() {
        currencyRepositoryImpl = CurrencyRepositoryImpl(apiService, currenciesResponseMapper)
    }

    @Test
    fun `getCurrenciesApiCall should return the correct data`() = runTest {
        // Given
        val expectedResponse = Gson().fromJson(UtilTests.dummyCurrenciesResponse, CurrenciesResponse::class.java)
        whenever(apiService.getCurrencies(BaseCurrency.USD.name)).thenReturn(expectedResponse)

        // When
        val actualResponse = currencyRepositoryImpl.getCurrencies(BaseCurrency.USD).first()

        // Then
        assertEquals(expectedResponse, actualResponse)
    }

    @Test
    fun `getCurrenciesApiCall should return error flow when an exception occurs`() = runTest {
        // Given
        val expectedException = RuntimeException("An error occurred")
        given(apiService.getCurrencies(BaseCurrency.USD.name)).willThrow(expectedException)

        // When
        val actualException = runCatching { currencyRepositoryImpl.getCurrencies(BaseCurrency.USD).first() }

        // Then
        assertTrue(actualException.isFailure)
        assertEquals(expectedException, actualException.exceptionOrNull())
    }
}