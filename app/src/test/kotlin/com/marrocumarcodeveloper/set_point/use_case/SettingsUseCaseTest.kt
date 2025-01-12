package com.marrocumarcodeveloper.set_point.use_case

import com.marrocumarcodeveloper.set_point.business_logic.Settings
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*

class SettingsUseCaseTest {

    private lateinit var settings: Settings
    private lateinit var settingsUseCase: SettingsUseCase

    @BeforeEach
    fun setUp() {
        settings = mock(Settings::class.java)
        settingsUseCase = SettingsUseCase(settings)
    }

    @Test
    fun getNumberOfSets() {
        val expectedSets = intArrayOf(3, 5)
        `when`(settings.getNumberOfSets()).thenReturn(expectedSets)

        val result = settingsUseCase.getNumberOfSets()

        assertArrayEquals(expectedSets, result)
        verify(settings).getNumberOfSets()
    }

    @Test
    fun setSelectedNumberOfSets() {
        val numberOfSets = 3

        settingsUseCase.setSelectedNumberOfSets(numberOfSets)

        verify(settings).setSelectedNumberOfSets(numberOfSets)
    }

    @Test
    fun getSelectedNumberOfSets() {
        val expectedNumberOfSets = 3
        `when`(settings.getSelectedNumberOfSets()).thenReturn(expectedNumberOfSets)

        val result = settingsUseCase.getSelectedNumberOfSets()

        assertEquals(expectedNumberOfSets, result)
        verify(settings).getSelectedNumberOfSets()
    }

    @Test
    fun setTiebreakEnabled() {
        val enabled = true

        settingsUseCase.setTiebreakEnabled(enabled)

        verify(settings).setTiebreakEnabled(enabled)
    }

    @Test
    fun getTiebreakEnabled() {
        val expectedEnabled = true
        `when`(settings.getTiebreakEnabled()).thenReturn(expectedEnabled)

        val result = settingsUseCase.getTiebreakEnabled()

        assertEquals(expectedEnabled, result)
        verify(settings).getTiebreakEnabled()
    }
}