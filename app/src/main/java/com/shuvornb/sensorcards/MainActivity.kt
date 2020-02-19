package com.shuvornb.sensorcards

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.speech.RecognizerIntent
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.content_main.*
import java.io.IOException
import java.util.*

/*
Course: COP5659(Mobile Programming)
Course Instructor: Professor Gary Tyson
Student1: Md Shamim Seraj(ms19bt)
Student2: Trina Dutta(td19e)
*/

class MainActivity : AppCompatActivity() {
    
    val REQ_CODE_SPEECH_INPUT = 100
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //setContentView(R.layout.content_main)
        setContentView(R.layout.content_main)

        // set initial image to the imageView
        val assetsBitmap1: Bitmap? = getBitmapFromAssets("joker_plain.jpg")
        imgViewCard.setImageBitmap(assetsBitmap1)

        // setting onClickListener on the "tap on mic to speak" button
        btnSpeak.setOnClickListener {
            promptSpeechInput()
        }
    }


    // displaying google speech input dialog
    private fun promptSpeechInput() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        intent.putExtra(
            RecognizerIntent.EXTRA_LANGUAGE_MODEL,
            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
        )
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
        intent.putExtra(
            RecognizerIntent.EXTRA_PROMPT,
            getString(R.string.speech_prompt)
        )
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT)
        } catch (a: ActivityNotFoundException) {
            Toast.makeText(
                applicationContext,
                getString(R.string.speech_not_supported),
                Toast.LENGTH_SHORT
            ).show()
        }

    }


    // receiving text data from speech
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            REQ_CODE_SPEECH_INPUT -> {
                if (resultCode == Activity.RESULT_OK && null != data) {

                    val result = data
                        .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)

                    val text = result!![0]
                    txtSpeechInput.text = text

                    val imageIndex = convertToImageNumber(text)
                    if(imageIndex.isNotEmpty()){
                        val assetsBitmap1: Bitmap? = getBitmapFromAssets("$imageIndex.png")
                        imgViewCard.setImageBitmap(assetsBitmap1)
                    }
                    else {
                        val assetsBitmap1: Bitmap? = getBitmapFromAssets("joker.jpg")
                        imgViewCard.setImageBitmap(assetsBitmap1)
                    }

                }
            }
        }
    }

    // mapping text data to corresponding card number
    private fun convertToImageNumber(text: String?) : String {

        var imageIndex = ""
        val textInLowerCase = text?.toLowerCase()
        if(textInLowerCase?.split(" ")?.size == 2) {
            val suit = textInLowerCase?.split(" ")?.get(0)
            val number = textInLowerCase?.split(" ")?.get(1)

            Log.i("CARD", suit)
            Log.i("CARD", number)

            when (suit) {
                "clubs" -> imageIndex += "C"
                "club" -> imageIndex += "C"
                "hearts" -> imageIndex += "H"
                "heart" -> imageIndex += "H"
                "diamonds" -> imageIndex += "D"
                "diamond" -> imageIndex += "D"
                "spades" -> imageIndex += "S"
                "spade" -> imageIndex += "S"
            }

            when (number) {
                "a" -> imageIndex += "1"
                "ace" -> imageIndex += "1"
                "two" -> imageIndex += "2"
                "to" -> imageIndex += "2"
                "2" -> imageIndex += "2"
                "three" -> imageIndex += "3"
                "3" -> imageIndex += "3"
                "four" -> imageIndex += "4"
                "for" -> imageIndex += "4"
                "4" -> imageIndex += "4"
                "five" -> imageIndex += "5"
                "V" -> imageIndex += "5"
                "5" -> imageIndex += "5"
                "six" -> imageIndex += "6"
                "6" -> imageIndex += "6"
                "seven" -> imageIndex += "7"
                "7" -> imageIndex += "7"
                "eight" -> imageIndex += "8"
                "8" -> imageIndex += "8"
                "nine" -> imageIndex += "9"
                "9" -> imageIndex += "9"
                "ten" -> imageIndex += "10"
                "10" -> imageIndex += "10"
                "jack" -> imageIndex += "11"
                "queen" -> imageIndex += "12"
                "king" -> imageIndex += "13"
            }

            Log.i("CARD", imageIndex)
        }

        return if(imageIndex.length > 1) imageIndex else ""
    }

    // Custom method to get assets folder image as bitmap
    private fun getBitmapFromAssets(fileName: String): Bitmap? {
        return try {
            BitmapFactory.decodeStream(assets.open(fileName))
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }
}
