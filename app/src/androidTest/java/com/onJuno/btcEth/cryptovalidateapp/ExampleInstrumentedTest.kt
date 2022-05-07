package com.onJuno.btcEth.cryptovalidateapp

import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intending
import androidx.test.espresso.intent.matcher.IntentMatchers.toPackage
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.After

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {

    @Before
    fun initializeIntent() = Intents.init()

    private val intent = Intent(ApplicationProvider.getApplicationContext(),MainActivity::class.java)

    @get:Rule
    val scannerActivityIntentTestRule = ActivityScenarioRule<MainActivity>(intent)

    @Test
    fun scannerActivityResult(){
        val address = "5665646488444776"
        val resultIntent = Intent().apply {
            putExtra("data",address)
        }
        val result = Instrumentation.ActivityResult(Activity.RESULT_OK,resultIntent)
        intending(toPackage("com.android.camera")).respondWith(result)

        onView(withId(R.id.scan_eth_btn)).perform(click())
        onView(withId(R.id.qr_code_data)).check(matches(withText("Scanned ETH Address: $address")))
    }

    @After
    fun releaseIntent() = Intents.release()
}