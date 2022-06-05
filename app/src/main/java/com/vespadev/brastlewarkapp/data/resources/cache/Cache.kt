package com.vespadev.brastlewarkapp.data.resources.cache

interface Cache<T> {
    var value: T
    fun isEmpty(): Boolean
}