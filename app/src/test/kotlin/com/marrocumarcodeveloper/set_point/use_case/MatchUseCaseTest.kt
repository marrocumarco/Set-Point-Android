package com.marrocumarcodeveloper.set_point.use_case

import com.marrocumarcodeveloper.set_point.business_logic.EndedSet
import com.marrocumarcodeveloper.set_point.business_logic.Match
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*

class MatchUseCaseTest {

    private lateinit var match: Match
    private lateinit var matchUseCase: MatchUseCase

    @BeforeEach
    fun setUp() {
        match = mock(Match::class.java)
        matchUseCase = MatchUseCase(match)
    }

    @Test
    fun getCanUndo() {
        `when`(match.canUndo).thenReturn(true)
        assertTrue(matchUseCase.canUndo)
    }

    @Test
    fun getPlayer1PointsDescription() {
        `when`(match.player1PointsDescription).thenReturn("15")
        assertEquals("15", matchUseCase.player1PointsDescription)
    }

    @Test
    fun getPlayer2PointsDescription() {
        `when`(match.player2PointsDescription).thenReturn("15")
        assertEquals("15", matchUseCase.player2PointsDescription)
    }

    @Test
    fun getPlayer1NumberOfSets() {
        `when`(match.player1NumberOfSets).thenReturn(1)
        assertEquals(1, matchUseCase.player1NumberOfSets)
    }

    @Test
    fun getPlayer2NumberOfSets() {
        `when`(match.player2NumberOfSets).thenReturn(1)
        assertEquals(1, matchUseCase.player2NumberOfSets)
    }

    @Test
    fun getPlayer1NumberOfGames() {
        `when`(match.player1NumberOfGames).thenReturn(1)
        assertEquals(1, matchUseCase.player1NumberOfGames)
    }

    @Test
    fun getPlayer2NumberOfGames() {
        `when`(match.player2NumberOfGames).thenReturn(1)
        assertEquals(1, matchUseCase.player2NumberOfGames)
    }

    @Test
    fun getWinnerDescription() {
        `when`(match.winnerDescription).thenReturn("P1")
        assertEquals("P1", matchUseCase.winnerDescription)
    }

    @Test
    fun getMatchEnded() {
        `when`(match.matchEnded).thenReturn(true)
        assertTrue(matchUseCase.matchEnded)
    }

    @Test
    fun getEndedSets() {
        val endedSets = arrayListOf(EndedSet(1, 6))
        `when`(match.endedSets).thenReturn(endedSets)
        assertEquals(endedSets, matchUseCase.endedSets)
    }

    @Test
    fun getPlayer1Serves() {
        `when`(match.player1Serves).thenReturn(true)
        assertTrue(matchUseCase.player1Serves)
    }

    @Test
    fun pointWonByPlayerOne() {
        runBlocking {
            matchUseCase.pointWonByPlayerOne()
            verify(match).pointWonByPlayerOne()
        }
    }

    @Test
    fun pointWonByPlayerTwo() {
        runBlocking {
            matchUseCase.pointWonByPlayerTwo()
            verify(match).pointWonByPlayerTwo()
        }
    }

    @Test
    fun resetMatch() {
        runBlocking {
            matchUseCase.resetMatch()
            verify(match).resetMatch()
        }
    }

    @Test
    fun undo() {
        runBlocking {
            matchUseCase.undo()
            verify(match).undo()
        }
    }
}