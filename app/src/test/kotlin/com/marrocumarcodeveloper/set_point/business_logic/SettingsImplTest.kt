package com.marrocumarcodeveloper.set_point.business_logic

import android.content.SharedPreferences
import org.junit.jupiter.api.Assertions.assertArrayEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito.*
import kotlin.test.DefaultAsserter.assertEquals

class SettingsImplTest {

    private lateinit var sharedPref: SharedPreferences
    private lateinit var settings: SettingsImpl

    @BeforeEach
    fun setUp() {
        sharedPref = mock(SharedPreferences::class.java)
        settings = SettingsImpl(sharedPref)
    }

    @Test
    fun setSelectedNumberOfSets() {
        val editor = mock(SharedPreferences.Editor::class.java)
        `when`(sharedPref.edit()).thenReturn(editor)
        `when`(editor.putInt(anyString(), anyInt())).thenReturn(editor)

        settings.setSelectedNumberOfSets(5)

        verify(editor).putInt(SettingsImpl.NUMBER_OF_SETS, 5)
        verify(editor).apply()
    }

    @Test
    fun setSelectedNumberOfSets_invalidNumberOfSets_failure() {
        assertThrows<IllegalArgumentException> { settings.setSelectedNumberOfSets(0) }
    }

    @Test
    fun getSelectedNumberOfSets() {
        `when`(sharedPref.getInt(SettingsImpl.NUMBER_OF_SETS, SettingsImpl.defaultNumberOfSets))
            .thenReturn(3)

        val result = settings.getSelectedNumberOfSets()

        assertEquals(expected = 3, actual = result, message = null)
    }

    @Test
    fun getNumberOfSets() {
        val result = settings.getNumberOfSets()

        assertArrayEquals(intArrayOf(1, 3, 5), result)
    }

    @Test
    fun setTiebreakEnabled() {
        val editor = mock(SharedPreferences.Editor::class.java)
        `when`(sharedPref.edit()).thenReturn(editor)
        `when`(editor.putBoolean(anyString(), anyBoolean())).thenReturn(editor)

        settings.setTiebreakEnabled(true)

        verify(editor).putBoolean(SettingsImpl.TIEBREAK_ENABLED, true)
        verify(editor).apply()
    }

    @Test
    fun getTiebreakEnabled() {
        `when`(sharedPref.getBoolean(SettingsImpl.TIEBREAK_ENABLED, true))
            .thenReturn(true)

        val result = settings.getTiebreakEnabled()

        assert(result)
    }
}