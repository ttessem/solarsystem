package solarsystem.v2D

import com.badlogic.gdx.backends.lwjgl.LwjglApplication
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Vector2
import ktx.app.KtxApplicationAdapter
import ktx.app.clearScreen
import ktx.graphics.use


fun main() {
    val config = LwjglApplicationConfiguration().apply {
        width = WIDTH
        height = HEIGHT
        foregroundFPS = FPS
    }
    LwjglApplication(SolarSystem3DApplication(), config)
}


class SolarSystem3DApplication : KtxApplicationAdapter {

    private lateinit var shapeRenderer: ShapeRenderer
    private val position = Vector2(500f, 500f)

    override fun create() {
        shapeRenderer = ShapeRenderer()
    }

    override fun render() {
        update()
        draw()
    }

    private fun update() {
        position.x += 2
    }

    private fun draw() {
        clearScreen(0f, 0f, 0f, 1f)
        shapeRenderer.use(ShapeRenderer.ShapeType.Filled) {
            it.color = Color.YELLOW
            it.circle(WIDTH/2f, HEIGHT/2f, 100f, 25)
        }

        shapeRenderer.use(ShapeRenderer.ShapeType.Filled) {
            it.color = Color.BLUE
            it.circle(position.x, position.y, 25f, 25)
        }
    }

}
