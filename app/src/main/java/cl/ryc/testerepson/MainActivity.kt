package cl.ryc.testerepson

import android.hardware.usb.UsbDevice
import android.hardware.usb.UsbManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import cl.ryc.testerepson.databinding.ActivityMainBinding
import com.dantsu.escposprinter.EscPosPrinter
import com.dantsu.escposprinter.connection.usb.UsbPrintersConnections
import com.dantsu.escposprinter.connection.tcp.TcpConnection
import com.dantsu.escposprinter.exceptions.EscPosBarcodeException
import com.dantsu.escposprinter.exceptions.EscPosConnectionException
import com.dantsu.escposprinter.exceptions.EscPosEncodingException
import com.dantsu.escposprinter.exceptions.EscPosParserException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {


    private val scope = CoroutineScope(Dispatchers.Default)

    private var binding: ActivityMainBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding!!.root)


        binding?.btnPrint?.setOnClickListener { _: View? ->
            val tcpConnection = TcpConnection("192.168.0.60", 9300, 15)

            scope.launch {
                try {
                    val printer = EscPosPrinter(tcpConnection, 203, 80f, 32)
                    printer.printFormattedText("[C]<u><font size='big'>ORDER N°045</font></u>")
                } catch (e: EscPosConnectionException) {
                    Log.e("PRINTER", "FAILED TO INSTANTIATE ESC/POST PRINTER")
                } catch (e: EscPosEncodingException) {
                    Log.e("PRINTER", "ENCODING EXCEPTION")
                } catch (e: EscPosBarcodeException) {
                    Log.e("PRINTER", "ENCODING EXCEPTION")
                } catch (e: EscPosParserException) {
                    Log.e("PRINTER", "ENCODING EXCEPTION")
                }
            }
        }
    }
}