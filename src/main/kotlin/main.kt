import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.opencv.core.CvType
import org.opencv.core.Mat
import org.opencv.core.MatOfByte
import org.opencv.imgcodecs.Imgcodecs
import origami.Camera
import origami.Filter
import origami.Origami
import origami.filters.NoOPFilter
import origami.filters.cartoon.Manga
import java.awt.datatransfer.DataFlavor
import java.awt.dnd.DnDConstants
import java.awt.dnd.DropTarget
import java.awt.dnd.DropTargetDropEvent
import java.io.File
import java.util.*

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Origami Webcam with JetCompose",
        state = rememberWindowState(width = 300.dp, height = 300.dp)
    ) {
        Origami.init()


        val filter = remember { mutableStateOf<Filter>(NoOPFilter()) }
        val target = object : DropTarget() {
            @Synchronized
            override fun drop(evt: DropTargetDropEvent) {
                evt.acceptDrop(DnDConstants.ACTION_REFERENCE)
                val droppedFiles = evt.transferable.getTransferData(DataFlavor.javaFileListFlavor) as List<*>
                droppedFiles.first()?.let {
                    filter.value = Origami.StringToFilter((it as File).absolutePath)
                }
            }
        }
        this.window.dropTarget = target

        MaterialTheme {
            myVideoComponent(filter)
        }
    }
}

fun getCamera(last: MutableState<Mat>): Camera {
    val camera = Camera()
    camera.device("{:device 0}")
//    camera.setFn(CameraFn { _, buffer -> last.value = buffer; buffer })
    camera.prepareStream()
    return camera
}

@Composable
fun myVideoComponent(filter: MutableState<Filter>) {
    val last = remember { mutableStateOf(Mat(800, 800, CvType.CV_16UC3)) }
    val camera = remember { getCamera(last) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        while (true) {
            delay(15)
            scope.launch {
                // TODO: this should be one function in Origami.Camera
                val mat = Mat()
                camera.VC().read(mat)
                last.value = filter.value.apply(mat)
            }
        }
    }

    Column(modifier = Modifier.background(Color.Black)) {
        Image(
            bitmap = asImageAsset(last.value),
            modifier = Modifier.fillMaxSize().clickable { saveLastFrameAsImage(last) },
            contentDescription = "Icon",
        )
    }
}

private fun saveLastFrameAsImage(last: MutableState<Mat>) {
    Imgcodecs.imwrite("hello_" + Date() + ".png", last.value)
}

// TODO: this should be one function in Origami
fun asImageAsset(image: Mat): ImageBitmap {
    val bytes = MatOfByte()
    Imgcodecs.imencode(".jpg", image, bytes)
    val byteArray = ByteArray((image.total() * image.channels()).toInt())
    bytes.get(0, 0, byteArray)
    return org.jetbrains.skia.Image.makeFromEncoded(byteArray).toComposeImageBitmap()
}

