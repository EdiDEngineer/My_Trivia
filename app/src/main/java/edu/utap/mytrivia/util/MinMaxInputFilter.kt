package edu.utap.mytrivia.util

import android.content.Context
import android.text.InputFilter
import android.text.Spanned


class MinMaxInputFilter(
    private val minValue: Int,
    private val maxValue: Int,
    private val context: Context?
) :
    InputFilter {

    override fun filter(
        source: CharSequence,
        start: Int,
        end: Int,
        dest: Spanned,
        dstart: Int,
        dend: Int
    ): CharSequence? {
        try {
            var input = dest.substring(0, dstart) + dest.substring(
                dend,
                dest.toString().length
            )
            input = input.substring(0, dstart) + source + input.substring(
                dstart,
                input.length
            )

            val status = input.toInt() in minValue..maxValue
            if (!status) {
                context?.showShortToast("Value not in range $minValue - $maxValue")
            } else {
                return null
            }

        } catch (e: NumberFormatException) {
            e.printStackTrace()
        }
        return ""
    }

}