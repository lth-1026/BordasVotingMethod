package de.fra_uas.fb2.mobiledevices.bordasvotingmethod

import java.io.Serializable

data class Option(val name: String, val index: Int) {
    var progress: Int = 0
}