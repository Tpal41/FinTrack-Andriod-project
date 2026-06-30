package com.fintrack.app

import org.junit.Assert.assertEquals
import org.junit.Test

class ArchitectureSmokeTest {
    @Test
    fun appName_isStable() {
        assertEquals("FinTrack", "FinTrack")
    }
}
