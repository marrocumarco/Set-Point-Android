import com.marrocumarcodeveloper.set_point.business_logic.MatchImpl
import com.marrocumarcodeveloper.set_point.business_logic.Settings
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*

class MatchImplTest {

    private lateinit var match: MatchImpl
    private lateinit var settings: Settings

    @BeforeEach
    fun setUp() {
        settings = mock(Settings::class.java)
        `when`(settings.getTiebreakEnabled()).thenReturn(true)
        `when`(settings.getSelectedNumberOfSets()).thenReturn(3)
        match = MatchImpl(settings)
    }

    @Test
    fun resetMatch_shouldResetTheMatch() {
        runBlocking {
            match.resetMatch()
            assertEquals(0, match.player1NumberOfSets)
            assertEquals(0, match.player2NumberOfSets)
            assertEquals(0, match.endedSets.size)
            assertTrue(match.showCurrentSetScore)
            assertFalse(match.showEndedMatchAlert)
            assertFalse(match.matchEnded)
            assertTrue(match.player1Serves)
        }
    }

    @Test
    fun pointWonByPlayerOne_shouldIncrementPlayer1Points() {
        runBlocking {
            match.pointWonByPlayerOne()
            assertEquals("15", match.player1PointsDescription)
        }
    }

    @Test
    fun pointWonByPlayerTwo_shouldIncrementPlayer2Points() {
        runBlocking {
            match.pointWonByPlayerTwo()
            assertEquals("15", match.player2PointsDescription)
        }
    }

    @Test
    fun pointWonByPlayerOne_shouldIncrementPlayer1Points_tiebreakMode() {
        runBlocking {
            goToTiebreak()
            match.pointWonByPlayerOne()
            assertEquals("1", match.player1PointsDescription)
        }
    }

    @Test
    fun undo_shouldDecrementPoints() {
        runBlocking {
            match.pointWonByPlayerOne()
            match.pointWonByPlayerOne()
            match.undo()
            assertEquals("15", match.player1PointsDescription)
        }
    }

    @Test
    fun undo_shouldThrowExceptionIfNoPreviousState() {
        assertThrows(IllegalStateException::class.java) {
            runBlocking {
                match.undo()
            }
        }
    }

    @Test
    fun canUndo_shouldReturnTrueIfThereArePointsToUndo() {
        runBlocking {
            match.pointWonByPlayerOne()
            assertTrue(match.canUndo)
        }
    }

    @Test
    fun canUndo_shouldReturnFalseIfThereAreNoPointsToUndo() {
        runBlocking {
            assertFalse(match.canUndo)
        }
    }

    @Test
    fun player2GameScoreDescription_shouldReturnThePlayer2Points() {
        runBlocking {
            match.pointWonByPlayerTwo()
            assertEquals("15", match.player2PointsDescription)
        }
    }

    @Test
    fun matchWin_shouldSetMatchEnded() {
        runBlocking {
            repeat(24 * 2) { match.pointWonByPlayerOne() }
            assertTrue(match.matchEnded)
            assertTrue(match.showEndedMatchAlert)
        }
    }

    @Test
    fun tiebreakWin_shouldEndSet() {
        runBlocking {
            goToTiebreak()
            repeat(7) { match.pointWonByPlayerOne() }
            assertEquals(1, match.player1NumberOfSets)
        }
    }

    private suspend fun goToTiebreak() {
        repeat(4 * 5) { match.pointWonByPlayerOne() }
        repeat(4 * 5) { match.pointWonByPlayerTwo() }
        repeat(4) { match.pointWonByPlayerOne() }
        repeat(4) { match.pointWonByPlayerTwo() }
    }

    @Test
    fun deuce_shouldResetScoreToForty() {
        runBlocking {
            repeat(3) { match.pointWonByPlayerOne() }
            repeat(3) { match.pointWonByPlayerTwo() }
            match.pointWonByPlayerOne()
            match.pointWonByPlayerTwo()
            assertEquals("40", match.player1PointsDescription)
            assertEquals("40", match.player2PointsDescription)
        }
    }

    @Test
    fun advantage_shouldSetAdvantageScore() {
        runBlocking {
            repeat(3) { match.pointWonByPlayerOne() }
            repeat(3) { match.pointWonByPlayerTwo() }
            match.pointWonByPlayerOne()
            assertEquals("A", match.player1PointsDescription)
        }
    }

    @Test
    fun gameWin_shouldResetGameScore() {
        runBlocking {
            repeat(4) { match.pointWonByPlayerOne() }
            assertEquals("0", match.player1PointsDescription)
            assertEquals("0", match.player2PointsDescription)
        }
    }

    @Test
    fun player1NumberOfGames_shouldReturnCorrectValue() {
        runBlocking {
            repeat(4) { match.pointWonByPlayerOne() }
            assertEquals(1, match.player1NumberOfGames)
        }
    }

    @Test
    fun player2NumberOfGames_shouldReturnCorrectValue() {
        runBlocking {
            repeat(4) { match.pointWonByPlayerTwo() }
            assertEquals(1, match.player2NumberOfGames)
        }
    }

    @Test
    fun player1NumberOfSets_shouldReturnCorrectValue() {
        runBlocking {
            repeat(24) { match.pointWonByPlayerOne() }
            assertEquals(1, match.player1NumberOfSets)
        }
    }

    @Test
    fun player2NumberOfSets_shouldReturnCorrectValue() {
        runBlocking {
            repeat(24) { match.pointWonByPlayerTwo() }
            assertEquals(1, match.player2NumberOfSets)
        }
    }

    @Test
    fun winnerDescription_shouldReturnCorrectValue() {
        runBlocking {
            repeat(24 * 2) { match.pointWonByPlayerOne() }
            assertEquals("P1", match.winnerDescription)
        }
    }

    @Test
    fun matchEnded_shouldReturnCorrectValue() {
        runBlocking {
            repeat(24 * 2) { match.pointWonByPlayerOne() }
            assertTrue(match.matchEnded)
        }
    }

    @Test
    fun showEndedMatchAlert_shouldReturnCorrectValue() {
        runBlocking {
            repeat(24 * 2) { match.pointWonByPlayerOne() }
            assertTrue(match.showEndedMatchAlert)
        }
    }

    @Test
    fun showCurrentSetScore_shouldReturnCorrectValue() {
        runBlocking {
            assertTrue(match.showCurrentSetScore)
        }
    }

    @Test
    fun endedSets_shouldReturnCorrectValue() {
        runBlocking {
            repeat(24) { match.pointWonByPlayerOne() }
            assertEquals(1, match.endedSets.size)
        }
    }

    @Test
    fun player1Serves_shouldReturnCorrectValue() {
        runBlocking {
            assertTrue(match.player1Serves)
        }
    }
}