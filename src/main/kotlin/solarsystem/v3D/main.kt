package solarsystem.v3D

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.backends.lwjgl.LwjglApplication
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration
import com.badlogic.gdx.graphics.*
import com.badlogic.gdx.graphics.g3d.Environment
import com.badlogic.gdx.graphics.g3d.Material
import com.badlogic.gdx.graphics.g3d.ModelBatch
import com.badlogic.gdx.graphics.g3d.ModelInstance
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder
import com.badlogic.gdx.math.Vector3
import ktx.app.KtxApplicationAdapter


fun main() {
    val config = LwjglApplicationConfiguration().apply {
        width = WIDTH
        height = HEIGHT
        foregroundFPS = FPS
    }
    LwjglApplication(SolarSystem3DApplication(), config)
}

class SolarSystem3DApplication: KtxApplicationAdapter {
    private lateinit var camera: PerspectiveCamera
    private lateinit var cameraController: CameraInputController;
    private lateinit var modelBuilder: ModelBuilder
    private lateinit var modelBatch: ModelBatch
    private lateinit var instances: List<ModelInstance>
    private lateinit var celestialObjects: List<CelestialObject>
    private lateinit var environment: Environment
    private val origin = Vector3(0f, 0f,0f)
    private var dayNo = 0

    override fun create() {
        camera = PerspectiveCamera(67f, Gdx.graphics.width.toFloat(), Gdx.graphics.height.toFloat())
        camera.position.set(25f, 25f, 25f);
        camera.lookAt(0f,0f,0f);
        camera.near = 1f;
        camera.far = 300f;
        camera.update();

        cameraController = CameraInputController(camera)
        Gdx.input.setInputProcessor(cameraController);

        environment = Environment()
        environment.set(ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f))
        environment.add(DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f))

        modelBuilder = ModelBuilder()

        modelBatch = ModelBatch()

        var sun = CelestialObject(Color.YELLOW,7.5f, origin, Vector3.Z)
        var earth = CelestialObject(Color.BLUE, 2f, Vector3(15f,0f, 0f), Vector3.Z)

        celestialObjects = listOf(sun,earth)

    }

    override fun render() {
        //cameraController.update()

        Gdx.gl.glViewport(0, 0, Gdx.graphics.width, Gdx.graphics.height)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT or GL20.GL_DEPTH_BUFFER_BIT)

        modelBatch.begin(camera);
        celestialObjects.forEach {
            var model = it.modelInstance(modelBuilder)

            model.transform.rotate(it.axis,dayNo.toFloat()).translate(it.initialPosition)

            modelBatch.render(model,environment)
        }
        println(dayNo)
        modelBatch.end();

        update()

        camera.update()
    }

    private fun update() {
        dayNo += 1
        dayNo %= 365
    }

    private fun draw() {

    }
}

fun CelestialObject.modelInstance(modelBuilder:ModelBuilder) : ModelInstance {
    return ModelInstance(modelBuilder.createSphere(radius,radius,radius,50,50,
        Material(ColorAttribute.createDiffuse(color)),
        (VertexAttributes.Usage.Position or VertexAttributes.Usage.Normal).toLong()))
}

data class CelestialObject(val color: Color, val radius: Float, val initialPosition: Vector3, val axis: Vector3)

sealed class CelestialObject2{
    data class Fixed(val color: Color, val radius: Float, val position: Vector3): CelestialObject2()
    data class Rotating(val color: Color, val radius: Float, val rotatesAround: CelestialObject2, val distance: Vector3) : CelestialObject2()
}