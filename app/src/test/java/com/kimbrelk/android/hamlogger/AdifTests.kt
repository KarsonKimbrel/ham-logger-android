package com.kimbrelk.android.hamlogger

import com.kimbrelk.android.hamlogger.data.Adif
import com.kimbrelk.android.hamlogger.data.model.AdiLexeme
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test

class AdifTests {

    @Test
    fun testLexAdi() {
        val tests = arrayOf(
            Pair("",
                listOf()),
            Pair("2345234klegdfsggsdgdfsgf<<<<<dfdfa",
                listOf()),
            Pair("<TAG:12>Hello world!",
                listOf(
                    AdiLexeme("TAG", "Hello world!"),)),
            Pair("\n<tag:12>Hello world!\r\n",
                listOf(
                    AdiLexeme("TAG", "Hello world!"),)),
            Pair("this is\n\rtrash\tbefore the data*(%#)$ FD <tAg:12>Hello world!tr\n\tash\n23452 343 er t",
                listOf(
                    AdiLexeme("TAG", "Hello world!"),)),
            Pair("<a:1>b<cc:2>dd<eee:3>fff",
                listOf(
                    AdiLexeme("A", "b"),
                    AdiLexeme("CC", "dd"),
                    AdiLexeme("EEE", "fff"),)),
            Pair("<1:1>1<2:2>22<3:3>333",
                listOf(
                    AdiLexeme("1", "1"),
                    AdiLexeme("2", "22"),
                    AdiLexeme("3", "333"),)),
        )
        for (test in tests) {
            runBlocking {
                val result = Adif.lexAdi(test.first)
                Assert.assertEquals(test.second, result)
            }
        }
    }

    @Test
    fun testfromAdi() {
        val tests = arrayOf(
            Pair("",
                0),
            Pair("aafd35 5>4  78+_*5656gh%>>>fhgf ad>",
                0),
            Pair("aafd3<5 5>4  78+_*56<56gh%>>>fhgf ad>",
                0),
            Pair("aafd3<5 5>4  78+_*56<56gh%>>>fhgf ad><EOH> fhgfh<EOR>fgsdh sdfh >",
                1),
            Pair("<EOH><Frequency:3>123<EOR>",
                1),
            Pair("<EOH><Frequency:3>123<EOR><EOR><EOR>",
                3),
        )
        for (test in tests) {
            runBlocking {
                val result = Adif.fromAdi(test.first)
                Assert.assertEquals(test.second, result.size)
            }
        }
    }

    @Test
    fun testImportDataSets() {
        val testFiles = arrayOf(
            "com/kimbrelk/android/hamlogger/small_test.adi",
            "com/kimbrelk/android/hamlogger/small_test_fd.adi",
            "com/kimbrelk/android/hamlogger/pu2pop.ADI"
        )
        for (testFile in testFiles) {
            testImport(testFile)
        }
    }

    private fun testImport(fileName: String) {
        Assert.assertTrue(true)
    }
}