package com.example.hockeynamibiaorg.data.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hockeynamibiaorg.data.models.Event
import com.example.hockeynamibiaorg.data.repositories.EventRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CoachViewModel : ViewModel() {

    private val eventRepository = EventRepository()

    private val _events = MutableStateFlow<List<Event>>(emptyList())
    val events: StateFlow<List<Event>> = _events.asStateFlow()

    init {
        fetchEvents()
    }

    private fun fetchEvents() {
        viewModelScope.launch {
            eventRepository.getEventsFlow().collect { eventList ->
                _events.value = eventList
            }
        }
    }

    fun addEvent(event: Event) {
        viewModelScope.launch {
            eventRepository.addEvent(event)
        }
    }

    fun updateEvent(event: Event) {
        viewModelScope.launch {
            eventRepository.updateEvent(event)
        }
    }

    fun deleteEvent(eventId: String) {
        viewModelScope.launch {
            eventRepository.deleteEvent(eventId)
        }
    }
}
