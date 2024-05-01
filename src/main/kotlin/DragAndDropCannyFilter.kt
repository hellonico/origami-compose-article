//import androidx.compose.foundation.Image
//import androidx.compose.foundation.background
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.aspectRatio
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.material.Button
//import androidx.compose.material.MaterialTheme
//import androidx.compose.material.Slider
//import androidx.compose.material.Text
//import androidx.compose.runtime.*
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.graphics.ImageBitmap
//import androidx.compose.ui.graphics.toComposeImageBitmap
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.window.Window
//import androidx.compose.ui.window.application
//import androidx.compose.ui.window.rememberWindowState
//import kotlinx.coroutines.delay
//import kotlinx.coroutines.launch
//import org.opencv.core.CvType
//import org.opencv.core.Mat
//import org.opencv.core.MatOfByte
//import org.opencv.imgcodecs.Imgcodecs
//import origami.Camera
//import origami.CameraFn
//import origami.Origami
//import origami.filters.Canny
//import java.awt.datatransfer.DataFlavor
//import java.awt.dnd.DnDConstants
//import java.awt.dnd.DropTarget
//import java.awt.dnd.DropTargetDropEvent
//import java.io.File
//import kotlin.time.Duration.Companion.seconds
//
//fun asImageAsset(image: Mat): ImageBitmap {
//    val bytes = MatOfByte()
//    Imgcodecs.imencode(".jpg", image, bytes)
//    val byteArray = ByteArray((image.total() * image.channels()).toInt())
//    bytes.get(0, 0, byteArray)
//    return org.jetbrains.skia.Image.makeFromEncoded(byteArray).toComposeImageBitmap()
//}
//
//fun main() = application {
//    Window(
//        onCloseRequest = ::exitApplication,
//        title = "Compose for Desktop",
//        state = rememberWindowState(width = 300.dp, height = 300.dp)
//    ) {
//
//        Origami.init()
//
//        val name = remember { mutableStateOf("") }
//        val target = object : DropTarget() {
//            @Synchronized
//            override fun drop(evt: DropTargetDropEvent) {
//                evt.acceptDrop(DnDConstants.ACTION_REFERENCE)
//                val droppedFiles = evt.transferable.getTransferData(DataFlavor.javaFileListFlavor) as List<*>
//                droppedFiles.first()?.let {
//                    name.value = (it as File).absolutePath
//                }
//            }
//        }
//        this.window.dropTarget = target
//
//        MaterialTheme {
////            MyCustomOrigamiComponent(name)
//            MyVideoComponent()
////            TimerScreen()
//        }
//    }
//}
//
//fun getCamera(last: MutableState<Mat>): Camera {
//    val camera = Camera();
//    camera.device("{:device 0}")
//    //camera.setFn(CameraFn { _, buffer -> last.value = buffer; buffer })
//    camera.prepareStream()
//    return camera
//}
//
//@Composable
//fun MyVideoComponent() {
//    val last = remember { mutableStateOf(Mat(800, 800, CvType.CV_16UC3)) }
//    val camera = remember { getCamera(last) }
//    val scope = rememberCoroutineScope()
//    val filter = Canny()
//
//    LaunchedEffect(Unit) {
//        while(true) {
//            delay(5)
//            scope.launch {
//                val mat = Mat()
//                camera.VC().read(mat)
//                last.value = filter.apply(mat)
//            }
//        }
//    }
//
//    Column() {
//        Image(
//            bitmap = asImageAsset(last.value),
//            modifier = Modifier.fillMaxSize().background(Color.Black),
//            contentDescription = "Icon",
//        )
//    }
//
//}
//
//@Composable
//fun MyCustomOrigamiComponent(name: MutableState<String>) {
//
//    if (name.value == "") {
//        Text("Drop a file . . .")
//    } else {
//
//        val value = remember { mutableStateOf(10.0F) }
//        val value2 = remember { mutableStateOf(10.0F) }
//        val filter = Canny()
//        filter.threshold1 = value.value.toInt()
//        filter.threshold2 = value2.value.toInt()
//
//        Column {
//            Text(value.value.toString())
//            Slider(steps = 100, valueRange = 1f..250f, value = value.value, onValueChange = {
//                value.value = it
//            })
//            Text(value2.value.toString())
//            Slider(steps = 100, valueRange = 1f..250f, value = value2.value, onValueChange = {
//                value2.value = it
//            })
//
//            Image(
//                bitmap = asImageAsset(filter.apply(Imgcodecs.imread(name.value))),
//                contentDescription = "Icon",
//                modifier = Modifier.fillMaxSize()
//            )
//        }
//    }
//}