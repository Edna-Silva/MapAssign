package com.example.hockeynamibiaorg.data.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hockeynamibiaorg.data.models.Event
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class EventViewModel : ViewModel() {
    private val firestore = FirebaseFirestore.getInstance()

    private val _events = MutableStateFlow<List<Event>>(emptyList())
    val events: StateFlow<List<Event>> = _events

    private var newEventsListener: ListenerRegistration? = null

    init {
        fetchEvents()
    }

    private fun fetchEvents() {
        // Use snapshot listener to keep events updated in real-time
        firestore.collection("events")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.e("EventViewModelNew", "Error fetching events: ", error)
                    return@addSnapshotListener
                }
                if (snapshot != null) {
                    val eventList = snapshot.documents.mapNotNull { doc ->
                        doc.toObject(Event::class.java)?.copy(id = doc.id)
                    }
                    _events.value = eventList
                }
            }
    }

    fun listenForNewEvents(onNewEvent: (Event) -> Unit) {
        newEventsListener?.remove()
        newEventsListener = firestore.collection("events")
            .orderBy("date")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.e("EventViewModelNew", "Error listening for new events: ", error)
                    return@addSnapshotListener
                }
                if (snapshot != null && !snapshot.isEmpty) {
                    val newEventDocs = snapshot.documentChanges.filter { it.type.name == "ADDED" }
                    for (change in newEventDocs) {
                        val newEvent = change.document.toObject(Event::class.java).copy(id = change.document.id)
                        onNewEvent(newEvent)
                    }
                }
            }
    }

    fun addEvent(event: Event) {
        viewModelScope.launch {
            try {
                val docRef = firestore.collection("events").document()
                val eventWithId = event.copy(id = docRef.id)
                docRef.set(eventWithId).await()
                // Optionally refresh events list
                // fetchEvents() // Not needed if snapshot listener is active
            } catch (e: Exception) {
                Log.e("EventViewModelNew", "Exception adding event: ", e)
            }
        }
    }

    fun updateEvent(event: Event) {
        viewModelScope.launch {
            try {
                if (event.id.isNotEmpty()) {
                    firestore.collection("events").document(event.id).set(event).await()
                    // Optionally refresh events list
                    // fetchEvents()
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
                    firestore.collection("events").document(eventId).delete().await()
                    // Optionally refresh events list
                    // fetchEvents()
                }
            } catch (e: Exception) {
                Log.e("EventViewModelNew", "Exception deleting event: ", e)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        newEventsListener?.remove()
    }
}
