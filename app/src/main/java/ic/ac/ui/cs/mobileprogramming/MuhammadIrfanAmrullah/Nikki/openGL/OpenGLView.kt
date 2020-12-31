package ic.ac.ui.cs.mobileprogramming.MuhammadIrfanAmrullah.Nikki.openGL

import android.content.Context
import android.opengl.GLSurfaceView

class OpenGLView(context: Context) : GLSurfaceView(context) {
    private val renderer: OpenGLRenderer

    init {

        // Create an OpenGL ES 2.0 context
        setEGLContextClientVersion(2)

        renderer = OpenGLRenderer()

        // Set the Renderer for drawing on the GLSurfaceView
        preserveEGLContextOnPause = true
        setRenderer(renderer)
    }

}