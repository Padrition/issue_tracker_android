package cz.mendelu.projek.ui.elements

import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageAnalysis.Analyzer
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import java.util.concurrent.Executors

@Composable
fun CameraComposeView(
    paddingValues: PaddingValues,
    analyzer: Analyzer?
){
    val context = LocalContext.current
    val cameraProvider = remember {
        ProcessCameraProvider.getInstance(context)
    }
    val cameraExecutor = remember {
        Executors.newSingleThreadExecutor()
    }

    val mainThreadExecutor = ContextCompat.getMainExecutor(context)


    val lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current

    var preview by remember {
        mutableStateOf<Preview?>(null)
    }




    Box(modifier = Modifier
        .fillMaxSize()
        .padding(paddingValues)) {

        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = { context ->
                var previewView = PreviewView(context)

                preview = Preview.Builder().build().also {
                    it.setSurfaceProvider(previewView.surfaceProvider)
                }

                cameraProvider.addListener(
                    {
                        val cameraSelector = CameraSelector.Builder()
                            .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                            .build()

                        val cameraProviderInstance = cameraProvider.get()

                        val analyzerBuilder = ImageAnalysis.Builder()
                            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                            .build()
                        analyzer?.let {
                            analyzerBuilder.setAnalyzer(cameraExecutor, analyzer)
                        }

                        try{
                            cameraProviderInstance.unbindAll()
                            cameraProviderInstance.bindToLifecycle(
                                lifecycleOwner = lifecycleOwner,
                                cameraSelector = cameraSelector,
                                analyzerBuilder,
                                preview
                            )
                        }catch (exc: Exception) {
                            exc.printStackTrace()
                        }

                    },mainThreadExecutor
                )

                previewView
            }
        )

    }


}