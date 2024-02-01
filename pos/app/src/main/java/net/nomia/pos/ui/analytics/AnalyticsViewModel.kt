package net.nomia.pos.ui.analytics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import net.nomia.pos.domain.analytics.AnalyticsRepository
import javax.inject.Inject

@HiltViewModel
internal class AnalyticsViewModel @Inject constructor(
    repository: AnalyticsRepository
): ViewModel() {

    val state = MutableStateFlow(AnalyticsState(true))

    init {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                state.emit(AnalyticsState(data = repository.getAnalytics()))
            } catch (e: Exception) {
                state.emit(AnalyticsState(error = e.message))
            }
        }
    }

}