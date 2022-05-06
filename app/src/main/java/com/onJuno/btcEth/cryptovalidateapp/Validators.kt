package com.onJuno.btcEth.cryptovalidateapp

object Validators {
    private val btcExceptions = listOf('0','O','l','I')
    private val btcAddressPattern = Regex("^1(?=.*[a-zA-Z])(?=.*[0-9])[A-Za-z0-9]+$")
    private val ethAddressPattern = Regex("^0x(?=.*[a-fA-F])(?=.*[0-9])[A-Fa-f0-9]+$")

    fun validateBTCAddress(address:String): Boolean{
        if (address[0] != '1'){
            return false
        }
        else{
            if ((address.length < 25)||(address.length > 34)) return false
            else{
                if (!btcAddressPattern.matches(address)) return false
                for(char in address){
                    for (exceptionChar in btcExceptions){
                        if (char == exceptionChar) return false
                    }
                }
                return true
            }
        }
    }

    fun validateETHAddress(address:String):Boolean{
        if (ethAddressPattern.matches(address)) return true
        return false
    }
}