package com.projectcaerus

import org.mockito.Mockito
import org.mockito.stubbing.OngoingStubbing

//
// Created by y2k on 6/4/2016.
//

fun <T> when_(methodCall: T): OngoingStubbing<T> {
    return Mockito.`when`(methodCall)
}