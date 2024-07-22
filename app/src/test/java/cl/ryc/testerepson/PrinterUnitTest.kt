package cl.ryc.testerepson

import android.util.Log
import com.dantsu.escposprinter.EscPosPrinter
import com.dantsu.escposprinter.connection.tcp.TcpConnection
import org.junit.Test
import com.dantsu.escposprinter.exceptions.EscPosBarcodeException
import com.dantsu.escposprinter.exceptions.EscPosConnectionException
import com.dantsu.escposprinter.exceptions.EscPosEncodingException
import com.dantsu.escposprinter.exceptions.EscPosParserException
import kotlinx.coroutines.*

class PrinterUnitTest {

    @Test
    fun test_print() = runBlocking {
        // 创建 10 个协程，每个协程调用 print 函数
        val deferreds = List(5) {
            async {
                printJob()
                delay(1000L ) // 模拟耗时任务
            }
        }
        val results = deferreds.awaitAll() // 等待所有协程完成并收集结果
        results.forEach { println(it) }
        println("All tasks are finished")
    }

    suspend fun printJob(){
        val tcpConnection = TcpConnection("192.168.0.5", 9100, 100)
        var printer: EscPosPrinter? = null
        try {
            printer = EscPosPrinter(tcpConnection, 203, 80f, 32)
            printer.printFormattedTextAndCut("[C]<u><font size='big'>ORDER N°045</font></u>")
            // 等待10s
            delay(10000L)
        } catch (e: EscPosConnectionException) {
            Log.e("PRINTER", "FAILED TO INSTANTIATE ESC/POST PRINTER")
            throw e
        } catch (e: EscPosEncodingException) {
            Log.e("PRINTER", "ENCODING EXCEPTION")
            throw e

        } catch (e: EscPosBarcodeException) {
            Log.e("PRINTER", "ENCODING EXCEPTION")
            throw e

        } catch (e: EscPosParserException) {
            Log.e("PRINTER", "ENCODING EXCEPTION")
            throw e

        } finally {
            printer?.disconnectPrinter()
        }
    }
}