package com.mallen.photobackup

import android.text.TextUtils
import android.util.Patterns

// String extensions
fun String.isInt(): Boolean = TextUtils.isDigitsOnly(this)
fun String.isEmail(): Boolean = Patterns.EMAIL_ADDRESS.matcher(this).matches()
fun String.isIpv4(): Boolean = Patterns.IP_ADDRESS.matcher(this).matches()
fun String.isUrl(): Boolean = Patterns.WEB_URL.matcher(this).matches()

// Int extensions
fun Int.secondsInMs(): Long = (this * 1000).toLong()
fun Int.minutesInMs(): Long = secondsInMs() * 60
fun Int.hoursInMs(): Long = minutesInMs() * 60
fun Int.daysInMs(): Long = hoursInMs() * 24