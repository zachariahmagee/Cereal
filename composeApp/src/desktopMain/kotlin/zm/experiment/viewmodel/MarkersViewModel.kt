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

    val markers get() = plot.markers
    val selectedMarkerID get() = plot.selectedMarkerID;
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

    fun selectMarker(id: Int) {
        plot.selectMarker(selectedMarkerID)
    }

    fun addMarker() {
        plot.createMarker()
    }

    fun deleteMarker() {
        plot.removeMarker()
    }

    fun togglePeakSearch() {
        peakSearch = !peakSearch
    }

    fun _setPeakSearch(peaks: Boolean) {
        peakSearch = peaks
    }

    fun moveLeft() {
        plot.moveMarkerBackward(selectedMarkerID)
    }

    fun moveRight() {
        plot.moveMarkerForward(selectedMarkerID)
    }
}