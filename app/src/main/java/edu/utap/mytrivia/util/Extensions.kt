package edu.utap.mytrivia.util

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.Typeface
import android.os.Build
import android.text.*
import android.text.style.BackgroundColorSpan
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.util.Patterns
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.text.clearSpans
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import edu.utap.mytrivia.R
import kotlin.math.floor

fun Int.getDurationString(): String {
    val minutes = floor((this.toDouble() / 60.0)).toInt()
    val secondsRemaining = this - minutes * 60
    return String.format("%02d:%02d", minutes, secondsRemaining)
}

fun String.getDurationString(): String = toInt().getDurationString()

fun Fragment.showSnackBar(
    message: String,
    length: Int = Snackbar.LENGTH_LONG,
    actionString: String = "",
    listener: View.OnClickListener? = null
) {
    val snackBar = view?.let { Snackbar.make(it, message, length) }
    if (actionString.isNotEmpty()) {
        snackBar?.setAction(actionString, listener)
    }
    snackBar?.show()
}

fun TextInputLayout.validate(message: String, validator: (String) -> Boolean) {
    editText?.doAfterTextChanged {
        it?.let {
            error = if (!validator(it.toString())) {
                message
            } else {
                null
            }
        }
    }
}

fun String.isValidEmail(): Boolean = Patterns.EMAIL_ADDRESS.matcher(this).matches()

fun String.isValidPassword(): Boolean = length > 5

fun Fragment.hideKeyboard() {
    val imm =
        activity?.let { it.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager }
    activity?.currentFocus?.let {
        imm?.hideSoftInputFromWindow(it.windowToken, 0)
    }
}

fun Context.showShortToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun String.fromHtmlToString() =
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        Html.fromHtml(this, Html.FROM_HTML_MODE_LEGACY)
    } else {
        @Suppress("DEPRECATION")
        Html.fromHtml(this)
    }.toString()

fun Context.showDialog(
    title: String? = null,
    message: String? = null,
    positiveText: String = getString(R.string.action_ok),
    negativeText: String = getString(R.string.action_cancel),
    negativeAction: (dialog: DialogInterface, id: Int) -> Unit = { dialog, _ -> dialog.dismiss() },
    positiveAction: (dialog: DialogInterface, id: Int) -> Unit
) {
    val builder =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            MaterialAlertDialogBuilder(
                this,
                android.R.style.ThemeOverlay_Material_Dialog_Alert
            )
        } else {
            MaterialAlertDialogBuilder(
                this
            )
        }.apply {
            setTitle(title)
            setMessage(message)
            setPositiveButton(positiveText) { dialog, id ->
                positiveAction(dialog, id)
            }
            setNegativeButton(negativeText) { dialog, id ->
                negativeAction(dialog, id)
            }
        }

    builder.create().show()
}

fun SpannableString.searchSpan(subtext: String): Boolean {
    clearSpans()
    if (subtext.isEmpty()) return true
    val i = indexOf(subtext, ignoreCase = true)
    if (i == -1) return false
    setSpan(
        StyleSpan(Typeface.BOLD), i, i + subtext.length,
        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
    )
    setSpan(
        BackgroundColorSpan(Color.CYAN), i, i + subtext.length,
        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
    )
    return true
}

fun Context.minMaxInputFilter(
    minValue: Int,
    maxValue: Int
) =
    InputFilter { charSequence: CharSequence, _: Int, _: Int, spanned: Spanned, i2: Int, i3: Int ->
        try {
            var input = spanned.substring(0, i2) + spanned.substring(
                i3,
                spanned.toString().length
            )
            input = input.substring(0, i2) + charSequence + input.substring(
                i2,
                input.length
            )

            val status = input.toInt() in minValue..maxValue
            if (!status) {
                showShortToast("Number not in range $minValue - $maxValue")
            } else {
                return@InputFilter null
            }
        } catch (e: NumberFormatException) {
            e.printStackTrace()
        }
        ""
    }


fun Lifecycle.onEventObserver(
    resumeFunction: () -> Unit,
    pauseFunction: () -> Unit
) {
    val lifecycleObserver = object : LifecycleObserver {
        @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
        private fun resumeFunction() {
            resumeFunction()
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
        private fun pauseFunction() {
            pauseFunction()
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
        private fun removeObserver() {
            removeObserver(this)
        }
    }

    addObserver(lifecycleObserver)
}