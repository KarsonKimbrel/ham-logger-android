package com.kimbrelk.android.hamlogger.data.model

import java.util.*

data class Band(
    val name: String,
    val lowerFreq: Double,
    val upperFreq: Double,
) {

    fun isWithin(frequency: Double?) : Boolean {
        return frequency != null && lowerFreq <= frequency && upperFreq >= frequency
    }

    class Bands : ArrayList<Band>() {

        fun fromName(name: String?) : Band? {
            if (name != null) {
                for (band in this) {
                    if (name == band.name) {
                        return band
                    }
                }
            }
            return null
        }

        fun fromFrequency(frequency: Double?) : Band? {
            if (frequency != null) {
                for (band in this) {
                    if (band.isWithin(frequency)) {
                        return band
                    }
                }
            }
            return null
        }

        init {
            add(Band("2190m", 0.1357, 0.1378))
            add(Band("630m", 0.472, 0.479))
            add(Band("560m", 0.501, 0.504))
            add(Band("160m", 1.8, 2.0))
            add(Band("80m", 3.5, 4.0))
            add(Band("60m", 5.06, 5.45))
            add(Band("40m", 7.0, 7.3))
            add(Band("30m", 10.1, 10.15))
            add(Band("20m", 14.0, 14.35))
            add(Band("17m", 18.068, 18.168))
            add(Band("15m", 21.0, 21.45))
            add(Band("12m", 24.890, 24.99))
            add(Band("10m", 28.0, 29.7))
            add(Band("8m", 40.0, 45.0))
            add(Band("6m", 50.0, 54.0))
            add(Band("5m", 54.0001, 69.9999))
            add(Band("4m", 70.0, 71.0))
            add(Band("2m", 144.0, 148.0))
            add(Band("1.25m", 222.0, 225.0))
            add(Band("70cm", 420.0, 450.0))
            add(Band("33cm", 902.0, 928.0))
            add(Band("23cm", 1240.0, 1300.0))
            add(Band("13cm", 2300.0, 2450.0))
            add(Band("9cm", 3300.0, 3500.0))
            add(Band("6cm", 5650.0, 5925.0))
            add(Band("3cm", 10000.0, 10500.0))
            add(Band("1.25cm", 2400.0, 24250.0))
            add(Band("6mm", 47000.0, 47200.0))
            add(Band("4mm", 75700.0, 81000.0))
            add(Band("2.5mm", 119980.0, 120020.0))
            add(Band("2mm", 142000.0, 149000.0))
            add(Band("1mm", 241000.0, 250000.0))
        }
    }
}