package net.nomia.pos.ui.auth.internal

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import net.nomia.common.data.model.Employee
import net.nomia.main.domain.EmployeeRepository
import net.nomia.main.domain.PrincipalRepository
import net.nomia.pos.domain.auth.internal.Code
import net.nomia.pos.domain.LogoutUseCase
import javax.inject.Inject

@HiltViewModel
class InternalAuthViewModel @Inject constructor(
    private val employeeRepository: EmployeeRepository,
    private val principalRepository: PrincipalRepository,
    private val logoutUseCase: LogoutUseCase,
) : ViewModel() {
    private val _pinState = MutableStateFlow(Code.Pin())
    val pinState = _pinState.asStateFlow()

    init {
        _pinState
            .filter { it.input.length == PinSize }
            .mapLatest { it.input }
            .mapLatest(Employee::Pin)
            .flatMapLatest(employeeRepository::findByPin)
            .onEach(::processSearchEmployee)
            .launchIn(viewModelScope)
    }

    private suspend fun processSearchEmployee(employee: Employee?) {
        if (employee != null) {
            principalRepository.login(employee)
        } else {
            _pinState.update { Code.Pin(isCorrect = false) }
            // delay is needed to demonstrate an incorrect input and after it comes back to origin
            delay(300L)
            _pinState.update { Code.Pin(input = "") }
        }
    }

    fun onDigit(digit: String) = onPinStateChange(
        predicate = { it.length < PinSize },
        onPinUpdate = { it + digit }
    )

    fun onErase() = onPinStateChange(
        predicate = { it.isNotEmpty() },
        onPinUpdate = { it.dropLast(1) }
    )

    private fun onPinStateChange(
        predicate: suspend (input: String) -> Boolean,
        onPinUpdate: suspend (input: String) -> String
    ) = pinState.take(1)
        .mapLatest { it.input }
        .filter { predicate(it) }
        .mapLatest { input -> _pinState.update { it.copy(input = onPinUpdate(input)) } }
        .launchIn(viewModelScope)


    fun onLogout() = viewModelScope.launch {
        logoutUseCase.invoke()
    }

    companion object {
        const val PinSize = 4
    }
}
