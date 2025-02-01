package com.marrocumarcodeveloper.set_point.use_case

import com.marrocumarcodeveloper.set_point.business_logic.Settings
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*

class SettingsUseCaseTest {

    private lateinit var settings: Settings
    private lateinit var dataAccess: DataAccess
    private lateinit var settingsUseCase: SettingsUseCase

    @BeforeEach
    fun setUp() {
        settings = mock(Settings::class.java)
        dataAccess = mock(DataAccess::class.java)
        settingsUseCase = SettingsUseCase(settings, dataAccess)
    }

    @Test
    fun getSelectableNumberOfSets() {
        val expectedSets = intArrayOf(3, 5)
        `when`(settings.getSelectableNumberOfSets()).thenReturn(expectedSets)

        val result = settingsUseCase.getSelectableNumberOfSets()

        assertArrayEquals(expectedSets, result)
        verify(settings).getSelectableNumberOfSets()
    }

    @Test
    fun setSelectedNumberOfSets() {
        val numberOfSets = 3

        settingsUseCase.setSelectedNumberOfSets(numberOfSets)

        verify(settings).setSelectedNumberOfSets(numberOfSets)
        verify(dataAccess).setSelectedNumberOfSets(numberOfSets)
    }

    @Test
    fun getSelectedNumberOfSets() {
        val expectedNumberOfSets = 3
        `when`(dataAccess.getSelectedNumberOfSets(settings.getDefaultNumberOfSets())).thenReturn(expectedNumberOfSets)

        val result = settingsUseCase.getSelectedNumberOfSets()

        assertEquals(expectedNumberOfSets, result)
    }

    @Test
    fun setTiebreakEnabled() {
        val enabled = true

        settingsUseCase.setTiebreakEnabled(enabled)

        verify(settings).setTiebreakEnabled(enabled)
        verify(dataAccess).setTiebreakEnabled(enabled)
    }

    @Test
    fun getTiebreakEnabled() {
        val expectedEnabled = true
        `when`(dataAccess.getTiebreakEnabled(settings.getDefaultTiebreakEnabled())).thenReturn(expectedEnabled)

        val result = settingsUseCase.getTiebreakEnabled()

        assertEquals(expectedEnabled, result)
    }
}