package com.example.hockeynamibiaorg.data.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hockeynamibiaorg.data.models.Event
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class EventViewModelNew : ViewModel() {
    private val db = FirebaseFirestore.getInstance()
    private val _events = MutableStateFlow<List<Event>>(emptyList())
    val events: StateFlow<List<Event>> = _events

    init {
        fetchEvents()
    }

    private fun fetchEvents() {
        viewModelScope.launch {
            try {
                val result = db.collection("events").get().await()
                val eventList = mutableListOf<Event>()
                for (document in result) {
                    val event = document.toObject(Event::class.java)
                    eventList.add(event)
                }
                _events.value = eventList
            } catch (e: Exception) {
                Log.e("EventViewModelNew", "Exception fetching events: ", e)
            }
        }
    }

    fun addEvent(event: Event) {
        viewModelScope.launch {
            try {
                val docRef = db.collection("events").document()
                val eventWithId = event.copy(id = docRef.id)
                docRef.set(eventWithId).await()
                fetchEvents()
            } catch (e: Exception) {
                Log.e("EventViewModelNew", "Exception adding event: ", e)
            }
        }
    }

    fun updateEvent(event: Event) {
        viewModelScope.launch {
            try {
                if (event.id.isNotEmpty()) {
                    db.collection("events").document(event.id).set(event).await()
                    fetchEvents()
                }
            } catch (e: Exception) {
                Log.e("EventViewModelNew", "Exception updating event: ", e)
            }
        }
    }

    fun deleteEvent(eventId: String) {
        viewModelScope.launch {
            try {
                if (eventId.isNotEmpty()) {
                    db.collection("events").document(eventId).delete().await()
                    fetchEvents()
                }
            } catch (e: Exception) {
                Log.e("EventViewModelNew", "Exception deleting event: ", e)
            }
        }
    }
}
