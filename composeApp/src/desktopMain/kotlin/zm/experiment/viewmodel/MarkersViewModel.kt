package zm.experiment.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import zm.experiment.model.event.AppEvent
import zm.experiment.model.event.EventBus
import zm.experiment.model.type.SidePanelType

class MarkersViewModel(private val plot: PlotViewModel) : ViewModel() {

    var peakSearch: Boolean by mutableStateOf(false)


    init {
        viewModelScope.launch {
            EventBus.events.collect { event ->
                when (event) {
                    is AppEvent.PanelChanged -> {
                        if (event.panel == SidePanelType.MARKERS) {

                        }
                    }
                    is AppEvent.CommandSent -> {}
                    is AppEvent.PortConnected -> {}
                    is AppEvent.PortDisconnected -> {}
                }
            }
        }
    }
}