/**
 * Licensed under the LICENSE
 * Copyright (C) 2018, Sony Mobile Communications Inc.
 */
package com.android.test.kotlin

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.os.SystemProperties
import android.util.Log
import android.widget.TextView

import androidx.appcompat.app.AppCompatActivity
import androidx.localbroadcastmanager.content.LocalBroadcastManager

import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneFactory;

import java.io.PrintWriter
import java.io.StringWriter

import kotlin.sequences.sequence
import kotlin.sequences.SequenceScope

class ActivityMain : AppCompatActivity() {

    override
    fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.main)
        (findViewById(R.id.text) as TextView).setText(R.string.base)

        // 1. verify access to hidden API in framework.jar
        val sp = SystemProperties.getInt("status.battery.level", 0)
        Log.d("ActivityMain", "Battery Level is: $sp")

        // 2. verify access to support library
        val intentFilter = IntentFilter("com.android.test.kotlin.ACTION_JAVA")
        LocalBroadcastManager.getInstance(this).registerReceiver(
                object : BroadcastReceiver() {
                    override fun onReceive(ctx: Context?, intent: Intent?) {
                        if (ctx == null || intent == null) {
                            Log.d("ActivityMain", "null context and/or intent received")
                            return
                        }

                        val action = intent.action
                        if (action == "com.android.test.kotlin.ACTION_JAVA") {
                            Log.d("ActivityMain", "ACTION_JAVA intent received")
                        }
                    }
                },
                intentFilter)

        // 3. java interop
        val test = Test(this)
        Log.d("ActivityMain", "${test.go()}")

        // 4. verify access to hidden API in a library (e.g. telephony-common)
        try {
            val phone = PhoneFactory.getDefaultPhone()
            phone?.invokeOemRilRequestRaw(null, null)
            phone.dumpToLog()
        } catch (e: Exception) {
            Log.d("ActivityMain", e.message)
        }

        // 5. coroutines
        lazySeq.forEach { Log.d("ActivityMain", "$it") }
    }
}

fun Phone.dumpToLog() {
    dump(null, object : PrintWriter(StringWriter(0)) {
            override
            fun println(s: String) {
                Log.d("ActivityMain", s)
            }

            override
            fun flush() {}
        }, null);
}

suspend fun SequenceScope<Int>.yieldIfOdd(x: Int) {
        if (x % 2 != 0) yield(x)
}

val lazySeq =  sequence<Int> {
    for (i in 1..10) yieldIfOdd(i)
}
