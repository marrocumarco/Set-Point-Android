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
        assertEquals("Game, set, match P1", matchUseCase.winnerDescription)
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

    @Test
    fun shouldShowResetMatchAlert_true() {
        `when`(match.shouldRestartMatch).thenReturn(true)
        assertTrue(matchUseCase.shouldShowResetMatchAlert())
    }

    @Test
    fun shouldShowResetMatchAlert_false() {
        `when`(match.shouldRestartMatch).thenReturn(false)
        assertFalse(matchUseCase.shouldShowResetMatchAlert())
    }

    @Test
    fun showConfirmSettingsAlert_true() {
        `when`(match.shouldRestartMatch).thenReturn(true)
        assertTrue(matchUseCase.showConfirmSettingsAlert)
    }

    @Test
    fun showConfirmSettingsAlert_false() {
        `when`(match.shouldRestartMatch).thenReturn(false)
        assertFalse(matchUseCase.showConfirmSettingsAlert)
    }

    @Test
    fun player1FinalScoreDescription_correctFormat() {
        val endedSets = arrayListOf(EndedSet(6, 4), EndedSet(7, 5))
        `when`(match.endedSets).thenReturn(endedSets)
        assertEquals("6 7", matchUseCase.player1FinalScoreDescription)
    }

    @Test
    fun player2FinalScoreDescription_correctFormat() {
        val endedSets = arrayListOf(EndedSet(6, 4), EndedSet(7, 5))
        `when`(match.endedSets).thenReturn(endedSets)
        assertEquals("4 5", matchUseCase.player2FinalScoreDescription)
    }
}