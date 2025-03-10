package by.lebedev.core.domain.usecase.currency

import by.lebedev.core.data.model.BaseCurrency
import by.lebedev.core.data.model.Currencies
import by.lebedev.core.data.model.Rate
import by.lebedev.core.domain.repository.currency.CurrencyRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
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
class GetCurrenciesUseCaseTest {

    @Mock
    private lateinit var currencyRepository: CurrencyRepository
    private lateinit var getCurrenciesUseCase: GetCurrenciesUseCase

    @Before
    fun setUp() {
        getCurrenciesUseCase = GetCurrenciesUseCase(currencyRepository)
    }

    @Test
    fun `execute should return flow of currencies response`() = runTest {
        // given
        val currenciesResponse = Currencies("USD", listOf(Rate("EUR",0.947)))
        whenever(currencyRepository.getCurrencies(BaseCurrency.USD)).thenReturn(flowOf(currenciesResponse))

        // when
        val result = getCurrenciesUseCase.execute(BaseCurrency.USD).first()

        // then
        assertEquals(currenciesResponse, result)
    }

    @Test
    fun `execute should return error flow when an exception occurs`() = runTest {
        // Given
        val exception = Exception("An error occurred")
        val flow: Flow<Currencies> = flow { throw exception }
        given(currencyRepository.getCurrencies(BaseCurrency.USD)).willReturn(flow)

        // When
        val result = runCatching { getCurrenciesUseCase.execute(BaseCurrency.USD).first() }

        // Then
        assertTrue(result.isFailure)
        assertEquals(exception, result.exceptionOrNull())
    }
}