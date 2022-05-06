package com.onJuno.btcEth.cryptovalidateapp

import org.junit.Test

import org.junit.Assert.*

class ValidatorsUnitTest {

    @Test
    fun `Validate BTC Address`(){
        val validAddress = "14qViLJfdGaP4EeHnDyJbEGQysnCpwk3gd"
        val invalidAddress1 = "14qViLJfdGaP"
        val invalidAddress2 = "14qViLJfdGaP4EeHnDyJbEGQysnCpwk3gdfbsjbdfbdjbfjdsbjfbdsjfbjf"
        val invalidAddress3 = "14qViLJfdGaP4EeHnDyJbEGQysnCpwkgd0"
        val invalidAddress4 = "4qViLJfdGaP4EeHnDyJbEGQysnCpwk3gd"
        val invalidAddress5 = "14qViLJfdGaP4EeHnDyJbEGQysnCpwk3Od"
        val invalidAddress6 = "14qViLJfdGaP4EeHnDyJbEGQysnCpwk3gl"
        val invalidAddress7 = "14qViLJfdGaP4EeHnDyJbEGQysnCpwk3g*"
        val invalidAddress8 = "14qViLJfdGaP4IeHnDyJbEGQysnCpwk3gd"
        assertEquals(Validators.validateBTCAddress(validAddress),true)
        assertEquals(Validators.validateBTCAddress(invalidAddress1),false)
        assertEquals(Validators.validateBTCAddress(invalidAddress2),false)
        assertEquals(Validators.validateBTCAddress(invalidAddress3),false)
        assertEquals(Validators.validateBTCAddress(invalidAddress4),false)
        assertEquals(Validators.validateBTCAddress(invalidAddress5),false)
        assertEquals(Validators.validateBTCAddress(invalidAddress6),false)
        assertEquals(Validators.validateBTCAddress(invalidAddress7),false)
        assertEquals(Validators.validateBTCAddress(invalidAddress8),false)
    }

    @Test
    fun `Validate ETH Address`(){
        val validAddress = "0xDBC05B1ECB4FDAEF943819C0B04E9EF6DF4BABD6"
        val invalidAddress1 = "xDBC05B1ECB4FDAEF943819C0B04E9EF6DF4BABD6"
        val invalidAddress2 = "0DBC05B1ECB4FDAEF943819C0B04E9EF6DF4BABD6"
        val invalidAddress3 = "DBC05B1ECB4FDAEF943819C0B04E9EF6DF4BABD6"
        val invalidAddress4 = "0xDBC05B1ECB4FDAEF943819C0B04E9EF6DF4BABD6g"
        val invalidAddress5 = "0xDBC05B1ECB4FDAEF943819C0B04E9EF6DF4BABD6lm"
        val invalidAddress6 = "0xDBC05B1ECB4FDAEF943819C0B04E9EF6DF4BABD6z"
        val invalidAddress7 = "0xDBC05B1ECB4FDAEF943819C0B04E9EF6DF4BABD6-*"
        val invalidAddress8 = "0xDBC05B1ECB4FDAEF943819C0B04E9EF6DF4BABD6xx"
        assertEquals(Validators.validateETHAddress(validAddress),true)
        assertEquals(Validators.validateETHAddress(invalidAddress1),false)
        assertEquals(Validators.validateETHAddress(invalidAddress2),false)
        assertEquals(Validators.validateETHAddress(invalidAddress3),false)
        assertEquals(Validators.validateETHAddress(invalidAddress4),false)
        assertEquals(Validators.validateETHAddress(invalidAddress5),false)
        assertEquals(Validators.validateETHAddress(invalidAddress6),false)
        assertEquals(Validators.validateETHAddress(invalidAddress7),false)
        assertEquals(Validators.validateETHAddress(invalidAddress8),false)
    }
}