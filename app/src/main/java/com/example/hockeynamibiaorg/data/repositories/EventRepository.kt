package com.example.hockeynamibiaorg.data.repositories

import com.example.hockeynamibiaorg.data.models.Event
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await

class EventRepository {

    private val firestore = FirebaseFirestore.getInstance()
    private val eventsCollection = firestore.collection("events")

    fun getEventsFlow(): Flow<List<Event>> = callbackFlow {
        val listenerRegistration: ListenerRegistration = eventsCollection.addSnapshotListener { snapshot, error ->
            if (error != null) {
                close(error)
                return@addSnapshotListener
            }
            val events = snapshot?.documents?.mapNotNull { doc ->
                doc.toObject(Event::class.java)?.copy(id = doc.id)
            } ?: emptyList()
            trySend(events)
        }
        awaitClose { listenerRegistration.remove() }
    }

    suspend fun addEvent(event: Event) {
        eventsCollection.add(event).await()
    }

    suspend fun updateEvent(event: Event) {
        event.id.takeIf { it.isNotEmpty() }?.let {
            eventsCollection.document(it).set(event).await()
        }
    }

    suspend fun deleteEvent(eventId: String) {
        eventsCollection.document(eventId).delete().await()
    }
}
