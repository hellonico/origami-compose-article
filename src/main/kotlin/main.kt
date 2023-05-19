
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Slider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import org.opencv.core.Mat
import org.opencv.core.MatOfByte
import org.opencv.imgcodecs.Imgcodecs
import origami.Origami
import origami.filters.Canny
import java.awt.datatransfer.DataFlavor
import java.awt.dnd.DnDConstants
import java.awt.dnd.DropTarget
import java.awt.dnd.DropTargetDropEvent
import java.io.File

fun asImageAsset(image: Mat): ImageBitmap {
    val bytes = MatOfByte()
    Imgcodecs.imencode(".jpg", image, bytes)
    val byteArray = ByteArray((image.total() * image.channels()).toInt())
    bytes.get(0, 0, byteArray)
    return org.jetbrains.skia.Image.makeFromEncoded(byteArray).toComposeImageBitmap()
}

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Compose for Desktop",
        state = rememberWindowState(width = 300.dp, height = 300.dp)
    ) {

        Origami.init()

        val name = remember { mutableStateOf("") }
        val target = object : DropTarget() {
            @Synchronized
            override fun drop(evt: DropTargetDropEvent) {
                evt.acceptDrop(DnDConstants.ACTION_REFERENCE)
                val droppedFiles = evt.transferable.getTransferData(DataFlavor.javaFileListFlavor) as List<*>
                droppedFiles.first()?.let {
                    name.value = (it as File).absolutePath
                }
            }
        }
        this.window.dropTarget = target

        MaterialTheme {
            MyCustomOrigamiComponent(name)
        }
    }
}


@Composable
fun MyCustomOrigamiComponent(name: MutableState<String>) {

    if (name.value == "") {
        Text("Drop a file . . .")
    } else {

        val value = remember { mutableStateOf(10.0F) }
        val value2 = remember { mutableStateOf(10.0F) }
        val filter = Canny()
        filter.threshold1 = value.value.toInt()
        filter.threshold2 = value2.value.toInt()

        Column {
            Text(value.value.toString())
            Slider(steps = 100, valueRange = 1f..250f, value = value.value, onValueChange = {
                value.value = it
            })
            Text(value2.value.toString())
            Slider(steps = 100, valueRange = 1f..250f, value = value2.value, onValueChange = {
                value2.value = it
            })

            Image(
                bitmap = asImageAsset(filter.apply(Imgcodecs.imread(name.value))),
                contentDescription = "Icon",
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}