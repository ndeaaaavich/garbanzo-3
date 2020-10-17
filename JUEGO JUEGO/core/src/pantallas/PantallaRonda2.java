package pantallas;

import com.badlogic.gdx.Gdx;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import utiles.Render;
import utiles.Utiles;

import cuerpos.Cuerpo;
import mapas.Mapa;
import personajes.Guardia;
import personajes.Ladron;

public class PantallaRonda2 implements Screen {
	
	private Ladron jugadorLadron;
	private Guardia jugadorGuardia;

	private OrthographicCamera camera;

	private World mundo;
	private Box2DDebugRenderer b2dr;

	private TiledMap tileMap;
	private OrthogonalTiledMapRenderer tmr;

	private Mapa mapa;

	private FitViewport viewport;
	private Stage stage;
	
	@Override
	public void show() {
		int WScreen = Gdx.graphics.getWidth();
		int HScreen = Gdx.graphics.getHeight();

		// camara
		camera = new OrthographicCamera();
		camera.setToOrtho(false, (WScreen * Utiles.PPM) / 2, (HScreen * Utiles.PPM) / 2);
		// mundo
		mundo = new World(new Vector2(0, 0), true);

		viewport = new FitViewport((WScreen * Utiles.PPM) / 2, (HScreen * Utiles.PPM) / 2, camera);
		stage = new Stage(viewport);
		// TiledMap
		tileMap = new TmxMapLoader().load("mapas/ronda2.tmx");
		b2dr = new Box2DDebugRenderer();
		tmr = new OrthogonalTiledMapRenderer(tileMap, 1 * Utiles.PPM);
		mapa = new Mapa(tileMap, mundo);
		// b2dr.setDrawBodies(false);
		// sprites
		jugadorLadron = new Ladron(new Cuerpo(mundo, 16, 15, BodyType.DynamicBody, 30, 90), "personajes/s1.png");
		stage.addActor(jugadorLadron);
		
		jugadorGuardia = new Guardia(new Cuerpo(mundo, 16, 15, BodyType.DynamicBody, 200, 200), "personajes/badlogic.jpg");
		stage.addActor(jugadorGuardia);
		
	}

	@Override
	public void render(float delta) {
		Render.limpiarPantalla();
		
		tmr.setView(camera);
		tmr.render();
		b2dr.render(mundo, camera.combined);
		
		stage.act();
		stage.draw();
		
		Render.batch.setProjectionMatrix(camera.combined);
	}
	
	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void hide() {
	}

	@Override
	public void dispose() {
		Render.batch.dispose();
		mundo.dispose();
	}

}
