package com.example.myapplication

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.io.Serializable

data class Player(
    var username: String,
    var name: String,
    var ageGroup: String,
    var teamName: String = ""
) : Serializable

data class Team(val name: String, val ageGroup: String, val coachName: String)

class PlayerViewModel {

    private val _players = MutableStateFlow<MutableList<Player>>(mutableListOf())
    val players = _players.asStateFlow()

    private val _teams = MutableStateFlow<List<Team>>(
        listOf(
            Team("Team A", "U10", "Coach John"),
            Team("Team B", "U10", "Coach John"),
            Team("Team C", "U12", "Coach Jane"),
            Team("Team D", "U12", "Coach Jane"),
            Team("Team E", "U14", "Coach Mike")
        )
    )
    val teams = _teams.asStateFlow()

    private val _currentCoach =
        MutableStateFlow<String?>("Coach John")
    val currentCoach = _currentCoach.asStateFlow()

    fun setCurrentCoach(coachName: String) {
        _currentCoach.value = coachName
    }

    fun addPlayer(player: Player) {
        val currentPlayers = _players.value
        if (currentPlayers.none { it.username == player.username }) {
            _players.value = (currentPlayers + player).toMutableList()
        }
    }

    fun removePlayer(username: String, ageGroup: String) {
        val currentPlayers = _players.value
        _players.value =
            currentPlayers.filter { it.username != username || it.ageGroup != ageGroup }
                .toMutableList()
    }

    fun updatePlayer(username: String, ageGroup: String, newTeamName: String) {
        val currentPlayers = _players.value
        val updatedPlayers = currentPlayers.map {
            if (it.username == username && it.ageGroup == ageGroup) {
                it.copy(teamName = newTeamName)
            } else {
                it
            }
        }.toMutableList()
        _players.value = updatedPlayers
    }

    fun getTeamsForCurrentCoach(): List<Team> {
        val coach = _currentCoach.value ?: return emptyList()
        return _teams.value.filter { it.coachName == coach }
    }

    fun getPlayersForCoachAndAgeGroup(coachName: String, ageGroup: String): List<Player> {
        val coachTeams = _teams.value.filter { it.coachName == coachName && it.ageGroup == ageGroup }
        val allPlayers = _players.value
        return allPlayers.filter { player ->
            coachTeams.any { team ->
                team.name == player.teamName && team.ageGroup == ageGroup
            }
        }
    }

    fun getPlayersByAgeGroup(ageGroup: String): List<Player> {
        return _players.value.filter { it.ageGroup == ageGroup }
    }
}

