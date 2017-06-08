package com.softwarehut.kotlinmystery

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.Html
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    val mysteryService by lazy {
        MysteryService(this)
    }

    /**
     * Why this method is so slow? I wonder if someone could optimize it...
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val decryptedMessage = mysteryService.getSecretMessage()

        question.text = Html.fromHtml(decryptedMessage)

    }
}
