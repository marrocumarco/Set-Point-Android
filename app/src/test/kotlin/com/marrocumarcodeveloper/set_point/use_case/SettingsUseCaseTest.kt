package com.marrocumarcodeveloper.set_point.use_case

import com.marrocumarcodeveloper.set_point.business_logic.Settings
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito.*

class SettingsUseCaseTest {

    private lateinit var settings: Settings
    private lateinit var dataAccess: DataAccess
    private lateinit var settingsUseCase: SettingsUseCase
    private lateinit var localizationRepository: LocalizationRepository

    @BeforeEach
    fun setUp() {
        settings = mock(Settings::class.java)
        dataAccess = mock(DataAccess::class.java)
        localizationRepository = mock(LocalizationRepository::class.java)
        settingsUseCase = SettingsUseCase(settings, dataAccess, localizationRepository)
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

        verify(settings).setSelectedNumberOfSets(numberOfSets, true)
    }

    @Test
    fun getSelectedNumberOfSets() {
        val expectedNumberOfSets = 3
        `when`(settings.getSelectedNumberOfSets()).thenReturn(expectedNumberOfSets)

        val result = settingsUseCase.getSelectedNumberOfSets()

        assertEquals(expectedNumberOfSets, result)
    }

    @Test
    fun setTiebreakEnabled() {
        val enabled = true

        settingsUseCase.setTiebreakEnabled(enabled)

        verify(settings).setTiebreakEnabled(enabled, true)
    }

    @Test
    fun getTiebreakEnabled() {
        val expectedEnabled = true
        `when`(settings.getTiebreakEnabled()).thenReturn(expectedEnabled)

        val result = settingsUseCase.getTiebreakEnabled()

        assertEquals(expectedEnabled, result)
    }

    @Test
    fun confirmSettings_savesCurrentSettings() {
        `when`(settings.getSelectedNumberOfSets()).thenReturn(3)
        `when`(settings.getTiebreakEnabled()).thenReturn(true)

        settingsUseCase.confirmSettings()

        verify(dataAccess).setSelectedNumberOfSets(3)
        verify(dataAccess).setTiebreakEnabled(true)
    }

    @Test
    fun resetToLastSavedSettings_resetsToSavedValues() {
        `when`(dataAccess.getSelectedNumberOfSets(settings.getDefaultNumberOfSets())).thenReturn(5)
        `when`(dataAccess.getTiebreakEnabled(settings.getDefaultTiebreakEnabled())).thenReturn(false)

        settingsUseCase.resetToLastSavedSettings()

        verify(settings).setSelectedNumberOfSets(5, false)
        verify(settings, times(2)).setTiebreakEnabled(false, false)
        verify(settings).resetSettingsStatus()
    }

    @Test
    fun showConfirmSettingsAlert_settingsChanged_returnsTrue() {
        `when`(settings.getSettingsChanged()).thenReturn(true)

        val result = settingsUseCase.showConfirmSettingsAlert()

        assertTrue(result)
    }

    @Test
    fun showConfirmSettingsAlert_settingsNotChanged_returnsFalse() {
        `when`(settings.getSettingsChanged()).thenReturn(false)

        val result = settingsUseCase.showConfirmSettingsAlert()

        assertFalse(result)
    }
}