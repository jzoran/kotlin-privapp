/**
 * Licensed under the LICENSE
 * Copyright (C) 2018, Sony Mobile Communications Inc.
 */
package com.android.test.kotlin;

import android.content.Context;
import android.content.Intent;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

class Test {

    private Context context;

    Test(Context ctx) {
        context = ctx;
    }

    public String go() {
        LocalBroadcastManager.getInstance(context).
            sendBroadcast(new Intent("com.android.test.kotlin.ACTION_JAVA"));

        return "Java is a go!";
    }
}
