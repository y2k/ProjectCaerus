package com.projectcaerus

import org.mockito.Mockito
import org.mockito.stubbing.OngoingStubbing
import java.lang.reflect.Field
import java.lang.reflect.Modifier

//
// Created by y2k on 6/4/2016.
//

fun <T> when_(methodCall: T): OngoingStubbing<T> {
    return Mockito.`when`(methodCall)
}

fun setFinalField(instance: Any, field: Field, newValue: Any) {
    field.isAccessible = true

    // remove final modifier from field
    val modifiersField = Field::class.java.getDeclaredField("modifiers")
    modifiersField.isAccessible = true
    modifiersField.setInt(field, field.modifiers and Modifier.FINAL.inv())

    field.set(instance, newValue)
}