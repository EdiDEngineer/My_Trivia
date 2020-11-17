package edu.utap.mytrivia.ui.main.fragment.adapter

import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import androidx.databinding.BindingAdapter

@BindingAdapter("done")
inline fun EditText.done(crossinline function: () -> Unit) {
    setOnEditorActionListener { _, actionId, event ->
        if (event?.keyCode == KeyEvent.KEYCODE_ENTER) {
            if (event.action == KeyEvent.ACTION_UP) {
                function()
            }
        } else if (actionId == EditorInfo.IME_ACTION_DONE) {
            function()
        }
        false
    }
}