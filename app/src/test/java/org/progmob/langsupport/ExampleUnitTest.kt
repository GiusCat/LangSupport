package org.progmob.langsupport

import org.junit.Test
import org.junit.Assert.*
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner
import org.progmob.langsupport.util.Converters
import org.progmob.langsupport.util.LanguageManager
import java.util.Locale


@RunWith(MockitoJUnitRunner::class)
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun listStringConvertersTest() {
        val c = Converters()
        val l1: List<String>? = null
        val l2 = listOf<String>()
        val l3 = listOf("val1")
        val l4 = listOf("val1", "val2", "val3")

        assertEquals(null, c.listToString(l1))
        assertEquals("", c.listToString(l2))
        assertEquals("val1", c.listToString(l3))
        assertEquals("val1,val2,val3", c.listToString(l4))
    }

    @Test
    fun stringListConvertersTest() {
        val c = Converters()
        val s1: String? = null
        val s2 = ""
        val s3 = "val1"
        val s4 = "val1,val2,val3"

        assertEquals(null, c.fromString(s1))
        assertTrue(c.fromString(s2) != null && c.fromString(s2)!![0].isEmpty())
        assertEquals(listOf("val1"), c.fromString(s3))
        assertEquals(listOf("val1","val2","val3"), c.fromString(s4))
    }

    @Mock
    private lateinit var mockLocale: Locale
    private val fakeLang = "de"

    @Test
    fun languageManagerTest() {
        val lm = LanguageManager
        mockLocale = mock(Locale::class.java)
        `when`(mockLocale.language).thenReturn(fakeLang)

        assertTrue(lm.getLanguages().contains(mockLocale.language))
        assertEquals(R.mipmap.ic_german_flag_round, lm.flagOf(mockLocale.language))
        assertEquals("GER", lm.nameOf(mockLocale.language))
    }
}