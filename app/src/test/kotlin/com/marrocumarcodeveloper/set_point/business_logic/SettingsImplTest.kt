package com.marrocumarcodeveloper.set_point.business_logic

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class SettingsImplTest {

    private lateinit var settings: SettingsImpl

    @BeforeEach
    fun setUp() {
        settings = SettingsImpl()
    }

    @Test
    fun setSelectedNumberOfSets_validNumberOfSets_fromUser_success() {
        settings.setSelectedNumberOfSets(3, true)
        assertEquals(3, settings.getSelectedNumberOfSets())
        assertTrue(settings.getSettingsChanged())
    }

    @Test
    fun setSelectedNumberOfSets_validNumberOfSets_notFromUser_success() {
        settings.setSelectedNumberOfSets(3, false)
        assertEquals(3, settings.getSelectedNumberOfSets())
        assertFalse(settings.getSettingsChanged())
    }

    @Test
    fun setSelectedNumberOfSets_invalidNumberOfSets_throwsException() {
        assertThrows<IllegalArgumentException> { settings.setSelectedNumberOfSets(2, true) }
    }

    @Test
    fun getSelectedNumberOfSets_defaultValue() {
        assertEquals(SettingsImpl.defaultNumberOfSets, settings.getSelectedNumberOfSets())
    }

    @Test
    fun setTiebreakEnabled_notFromUser_true() {
        settings.setTiebreakEnabled(true, false)
        assertTrue(settings.getTiebreakEnabled())
        assertFalse(settings.getSettingsChanged())
    }

    @Test
    fun setTiebreakEnabled_fromUser_true() {
        settings.setTiebreakEnabled(true, true)
        assertTrue(settings.getTiebreakEnabled())
        assertTrue(settings.getSettingsChanged())
    }

    @Test
    fun setTiebreakEnabled_false() {
        settings.setTiebreakEnabled(false, true)
        assertFalse(settings.getTiebreakEnabled())
    }

    @Test
    fun getSettingsChanged_selectedNumberOfSets_fromUser_true() {
        settings.setSelectedNumberOfSets(3, true)

        val result = settings.getSettingsChanged()

        assertTrue(result)
        assertTrue(settings.getSettingsChanged())
    }

    @Test
    fun getSettingsChanged_selectedNumberOfSets_notFromUser_true() {
        settings.setSelectedNumberOfSets(3, false)

        val result = settings.getSettingsChanged()

        assertFalse(result)
    }

    @Test
    fun getSettingsChanged_tiebreakEnabled_fromUser_true() {
        settings.setTiebreakEnabled(true, true)

        val result = settings.getSettingsChanged()

        assertTrue(result)
    }

    @Test
    fun getSettingsChanged_tiebreakEnabled_fromUser_false() {
        val tiebreakEnabled = settings.getTiebreakEnabled()
        settings.setTiebreakEnabled(tiebreakEnabled, true)

        val result = settings.getSettingsChanged()

        assertFalse(result)
    }

    @Test
    fun getSettingsChanged_tiebreakEnabled_notFromUser_false() {
        val tiebreakEnabled = settings.getTiebreakEnabled()
        settings.setTiebreakEnabled(tiebreakEnabled, true)

        val result = settings.getSettingsChanged()

        assertFalse(result)
    }

    @Test
    fun getSettingsChanged_selectedNumberOfSets_fromUser_false() {
        val selectedNumberOfSets = settings.getSelectedNumberOfSets()
        settings.setSelectedNumberOfSets(selectedNumberOfSets, true)

        val result = settings.getSettingsChanged()

        assertFalse(result)
    }

    @Test
    fun getSettingsChanged_selectedNumberOfSets_notFromUser_false() {
        val selectedNumberOfSets = settings.getSelectedNumberOfSets()
        settings.setSelectedNumberOfSets(selectedNumberOfSets, true)

        val result = settings.getSettingsChanged()

        assertFalse(result)
    }

    @Test
    fun resetSettingsStatus_fromUser_success() {
        val tiebreakEnabled = settings.getTiebreakEnabled()
        settings.setTiebreakEnabled(!tiebreakEnabled, true)

        settings.resetSettingsStatus()

        val result = settings.getSettingsChanged()

        assertFalse(result)
    }

    @Test
    fun resetSettingsStatus_notFromUser_success() {
        val tiebreakEnabled = settings.getTiebreakEnabled()
        settings.setTiebreakEnabled(!tiebreakEnabled, false)

        settings.resetSettingsStatus()

        val result = settings.getSettingsChanged()

        assertFalse(result)
    }

    @Test
    fun setSelectedNumberOfSets_invalidNumberOfSets_notFromUser_throwsException() {

        assertThrows<IllegalArgumentException> { settings.setSelectedNumberOfSets(23, false) }
    }

    @Test
    fun setTiebreakEnabled_notFromUser_false() {
        settings.setTiebreakEnabled(false, false)
        assertFalse(settings.getTiebreakEnabled())
        assertFalse(settings.getSettingsChanged())
    }

    @Test
    fun resetSettingsStatus_afterChange_success() {
        settings.setSelectedNumberOfSets(3, true)
        settings.resetSettingsStatus()
        assertFalse(settings.getSettingsChanged())
    }

    @Test
    fun resetSettingsStatus_noChange_success() {
        settings.resetSettingsStatus()
        assertFalse(settings.getSettingsChanged())
    }

    @Test
    fun getSelectableNumberOfSets_containsValidNumbers() {
        val selectableNumbers = settings.getSelectableNumberOfSets()
        assertArrayEquals(intArrayOf(1, 3, 5), selectableNumbers)
    }

    @Test
    fun getSelectableNumberOfSets_notEmpty() {
        val selectableNumbers = settings.getSelectableNumberOfSets()
        assertTrue(selectableNumbers.isNotEmpty())
    }

    @Test
    fun getSelectableNumberOfSets_containsDefaultNumberOfSets() {
        val selectableNumbers = settings.getSelectableNumberOfSets()
        assertTrue(selectableNumbers.contains(SettingsImpl.defaultNumberOfSets))
    }

    @Test
    fun getDefaultNumberOfSets_returnsDefaultValue() {
        assertEquals(SettingsImpl.defaultNumberOfSets, settings.getDefaultNumberOfSets())
    }

    @Test
    fun getDefaultNumberOfSets_isPositive() {
        assertTrue(settings.getDefaultNumberOfSets() > 0)
    }

    @Test
    fun getDefaultTiebreakEnabled_returnsDefaultValue() {
        assertEquals(SettingsImpl.defaultTiebreakEnabled, settings.getDefaultTiebreakEnabled())
    }
}