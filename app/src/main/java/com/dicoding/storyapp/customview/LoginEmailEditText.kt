package com.dicoding.storyapp.customview

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText

class LoginEmailEditText : AppCompatEditText{
    constructor(context: Context) : super(context) {
    }
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {

    }
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {

    }

    override fun onTextChanged(
        text: CharSequence?,
        start: Int,
        lengthBefore: Int,
        lengthAfter: Int
    ) {
        if (!text.toString().contains("@")) {
            setError("The email must contain '@'", null)
        } else {
            error = null
        }
    }
}