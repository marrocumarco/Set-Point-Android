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
    fun setSelectedNumberOfSets_validNumberOfSets_success() {
        settings.setSelectedNumberOfSets(3)
        assertEquals(3, settings.getSelectedNumberOfSets())
    }

    @Test
    fun setSelectedNumberOfSets_invalidNumberOfSets_throwsException() {
        assertThrows<IllegalArgumentException> { settings.setSelectedNumberOfSets(2) }
    }

    @Test
    fun getSelectedNumberOfSets_defaultValue() {
        assertEquals(SettingsImpl.defaultNumberOfSets, settings.getSelectedNumberOfSets())
    }

    @Test
    fun setTiebreakEnabled_true() {
        settings.setTiebreakEnabled(true)
        assertTrue(settings.getTiebreakEnabled())
    }

    @Test
    fun setTiebreakEnabled_false() {
        settings.setTiebreakEnabled(false)
        assertFalse(settings.getTiebreakEnabled())
    }
}