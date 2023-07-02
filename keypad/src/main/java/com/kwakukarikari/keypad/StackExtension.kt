package com.kwakukarikari.keypad

import java.lang.StringBuilder

/**
 * Created by Kwaku Karikari on 15/05/2023.
 */


fun <T> MutableList<T>.push(item: T) = this.add(this.count(), item)
fun <T> MutableList<T>.pop(): T? = if (this.isNotEmpty()) this.removeAt(this.count() - 1) else null

fun MutableList<String>.join(): String {
    val sb = StringBuilder()
    for (i in this){
        sb.append(i)
    }
    return sb.toString()
}