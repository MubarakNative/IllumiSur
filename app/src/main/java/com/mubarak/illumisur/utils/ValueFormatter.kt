package com.mubarak.illumisur.utils

import java.text.DecimalFormat

object ValueFormatter {
    fun formatValue(value:Float):String{
        val footCandle = (value / 10.764)

        val format = DecimalFormat("##.#")
        return format.format(footCandle)
    }
}