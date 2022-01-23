package com.cloudlevi.nearlabs.extensions

fun String.subStringSafe(start: Int, end: Int): String {
    return if (this.length > start && end <= this.length) substring(start, end)
    else if (this.length > start) substring(start, this.length)
    else ""
}