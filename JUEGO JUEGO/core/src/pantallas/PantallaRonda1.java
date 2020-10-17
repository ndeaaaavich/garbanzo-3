package pantallas;

import com.badlogic.gdx.Gdx;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import utiles.Render;
import utiles.Utiles;

import cuerpos.Cuerpo;
import personajes.SpriteInfo;
import mapas.Mapa;
import personajes.Guardia;
import personajes.Jugador;
import personajes.Ladron;
import personajes.NPC;

public class PantallaRonda1 implements Screen {

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

	private NPC[] npcs = new NPC[9];
	//private boolean cerca = false;

	private Sprite sprRobo;
	private float posX = 0, posY = 0;
	
	private Vector2 posicion = new Vector2(0,0);
	private Vector2 puntoLlegada;
	private Vector2 puntoSalida;
	private Interpolation interpol = Interpolation.circle;
	
	private float tiempo, duracion = 2.5f;
	
	@Override
	public void show() {

		int WScreen = Gdx.graphics.getWidth();
		int HScreen = Gdx.graphics.getHeight();

		//camara
		camera = new OrthographicCamera();
		camera.setToOrtho(false, (WScreen * Utiles.PPM) / 2, (HScreen * Utiles.PPM) / 2);
		//mundo
		mundo = new World(new Vector2(0, 0), true);
		
		viewport = new FitViewport((WScreen * Utiles.PPM) / 2, (HScreen * Utiles.PPM) / 2, camera);
		stage = new Stage(viewport);
		//TiledMap
		tileMap = new TmxMapLoader().load("mapas/escenario.tmx");
		b2dr = new Box2DDebugRenderer();
		tmr = new OrthogonalTiledMapRenderer(tileMap, 1 * Utiles.PPM);
		mapa = new Mapa(tileMap, mundo);
		//b2dr.setDrawBodies(false);
		//sprites
		jugadorLadron = new Ladron(new Cuerpo(mundo, 16, 15, BodyType.DynamicBody, 150, 200), "personajes/s1.png");
		stage.addActor(jugadorLadron);
		
		jugadorGuardia = new Guardia(new Cuerpo(mundo, 16, 15, BodyType.DynamicBody, 200, 160), "personajes/badlogic.jpg");
		stage.addActor(jugadorGuardia);
		
		sprRobo = new Sprite( new Texture("personajes/badlogic.jpg") );
		//NPC
		crearNPCs(npcs.length/mapa.getVectorZonas().length-2 , npcs.length/mapa.getVectorZonas().length+2);
		//eventos

		stage.setKeyboardFocus(jugadorLadron);
		stage.setKeyboardFocus(jugadorGuardia);
		
		mundo.setContactListener(new ContactListener() {
			@Override
			public void beginContact(Contact contact) {
				Object o1 = contact.getFixtureA().getBody().getUserData();
				Object o2 = contact.getFixtureB().getBody().getUserData();
				//HAY QUE HACER TODAS LAS COMPROBACIONES 2 VECES UNA CON o1 Y OTRA CON o2
				try {
					if(o2 instanceof Cuerpo){//contactos zonas 
						if(o1 instanceof Jugador){//comprueba si el objeto que choca es el ladron
							
							if( ((Jugador) o1).getSala() != -1 ) {
								((Jugador) o1).salaAnterior = ((Jugador) o1).getSala();
							}
							
							((Jugador) o1).setSala( ((Cuerpo) o2).getZona() );//cambia la sala del ladron a la sala
																		     //en la que está
							
							((Jugador) o1).cambiarSala = true;
							
						}
						if(o1 instanceof NPC){//comprueba si el objeto que choca es el NPC
							((NPC) o1).setSala( ((Cuerpo) o2).getZona() );//cambia la sala del NPC a la sala
							   											//en la que está
							if( ((Cuerpo) o2).isRobado() ) {//si en la sala ya se realizó un robo el atributo
															//robado del cuerpo que representa a la sala será true

								((NPC) o1).setRobado(true); //no se podrá robar en esta sala asi que se pone el 
															//atributo robado del NPC en true
							}
						}
					}
					
					if(o1 instanceof Cuerpo){//contactos zonas 
						if(o2 instanceof Ladron){//comprueba si el objeto que choca es el ladron
							
							if( ((Jugador) o2).getSala() != -1 ) {
								((Jugador) o2).salaAnterior = ((Jugador) o2).getSala();
							}
							
							((Jugador) o2).setSala( ((Cuerpo) o1).getZona() );//cambia la sala del ladron a la sala
																		     //en la que está
							
							((Jugador) o2).cambiarSala = true;
							
						}
						if(o2 instanceof NPC){//comprueba si el objeto que choca es el NPC
							
							((NPC) o2).setSala( ((Cuerpo) o1).getZona() );//cambia la sala del NPC a la sala
							   											//en la que está
							if( ((Cuerpo) o1).isRobado() ) {//si en la sala ya se realizó un robo el atributo
															//robado del cuerpo que representa a la sala será true

								((NPC) o2).setRobado(true); //no se podrá robar en esta sala asi que se pone el 
															//atributo robado del NPC en true
							}
						}
					}

					
					if(o2 == null || o2 instanceof NPC) {
						if(o1 instanceof NPC){
							((NPC) o1).setCambioDirec(true);
						}
					}
					if(o1 == null || o1 instanceof NPC) {
						if(o2 instanceof NPC){
							((NPC) o2).setCambioDirec(true);
						}
					}
				}catch(Exception e) {}
			}
			@Override
			public void endContact(Contact contact) {
			}
			@Override
			public void preSolve(Contact contact, Manifold oldManifold) {
			}
			@Override
			public void postSolve(Contact contact, ContactImpulse impulse) {
			}
		});
	}

	@Override
	public void render(float delta) {
		Render.limpiarPantalla();

		update(delta);
		
		tmr.setView(camera);
		tmr.render();
		b2dr.render(mundo, camera.combined);
		
		stage.act();
		stage.draw();
		
		roboNPC();
		arrestar();
		adelantarCuerpos();
		
		Render.batch.setProjectionMatrix(camera.combined);
		Gdx.input.setInputProcessor(stage);
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
		stage.dispose();
		mundo.dispose();
	}
	//--------------------------------------------------------------------------------------------------------------------------------------
	//-------------------------------------------------------------CAMARA-------------------------------------------------------------------
	//--------------------------------------------------------------------------------------------------------------------------------------
	private void update(float delta) {
		mundo.step(1 / 60f, 6, 2);
		
        if(jugadorLadron.cambiarSala){  // necesito sumar tiempo mientras se hace la transición
        	tiempo += delta;

        	camaraUpdate();

        	if (tiempo>duracion) { 
            	jugadorLadron.cambiarSala = false;
                tiempo = 0;
            }
		}
		camera.update();
	}
	private void camaraUpdate() {
		puntoLlegada = mapa.getVectorZonas()[jugadorLadron.getSala()].getPosition();
		puntoSalida = mapa.getVectorZonas()[jugadorLadron.getSalaAnterior()].getPosition();
			
		posicion.set(puntoLlegada);
		posicion.sub(puntoSalida);
		posicion.scl(interpol.apply(tiempo/duracion));
		posicion.add(puntoSalida);

		camera.position.set(posicion,0);
			
		camera.update();
	}
	private void adelantarCuerpos() {
		for (int i = 0; i < npcs.length; i++) {
			if(jugadorGuardia.getPosition().dst(npcs[i].getPosition()) < 30 * Utiles.PPM) {
				if(jugadorGuardia.getPosition().y > npcs[i].getPosition().y ) {
					jugadorGuardia.toBack();
				}else {	
					jugadorGuardia.toFront();
				}
			}
			if(jugadorLadron.getPosition().dst(npcs[i].getPosition()) < 30 * Utiles.PPM) {
				if(jugadorLadron.getPosition().y > npcs[i].getPosition().y ) {
					jugadorLadron.toBack();
				}else {	
					jugadorLadron.toFront();
				}
			}
		}
		if(jugadorLadron.getPosition().dst(jugadorGuardia.getPosition()) < 30 * Utiles.PPM) {
			if(jugadorLadron.getPosition().y > jugadorGuardia.getPosition().y ) {
				jugadorLadron.toBack();
			}else {	
				jugadorLadron.toFront();
			}
		}
	}
	//--------------------------------------------------------------------------------------------------------------------------------------
	//-------------------------------------------------------------NPC COSAS----------------------------------------------------------------
	//--------------------------------------------------------------------------------------------------------------------------------------
	private void crearNPCs(int minporsala, int maxporsala) {
		int i;
		int porsala[] = new int[mapa.getVectorZonas().length];

		while(porsala[0]+porsala[1]+porsala[2] != npcs.length){
			for (int j = 0; j < porsala.length; j++) {
				porsala[j] = Utiles.r.nextInt(maxporsala-minporsala)+minporsala;
			}
		}

		for(i=0;i<npcs.length;i++){
			npcs[i] = new NPC(new Cuerpo(mundo,15,15,BodyType.DynamicBody,0,0),
							  SpriteInfo.values()[i].getFilename(),
							  SpriteInfo.values()[i].getApariencia());
			stage.addActor(npcs[i]);
			
			int salaRandom = Utiles.r.nextInt(mapa.getVectorZonas().length);
			
			do{
				if(porsala[salaRandom]>0){            //se fija si hay "cupo" 
					npcs[i].setSala(salaRandom);	  //en la salaRandom elegida
					porsala[salaRandom]--;            //y setea la sala y resta un cupo
				}else{
					salaRandom = Utiles.r.nextInt(mapa.getVectorZonas().length);
					npcs[i].setSala(-1);
				} 
			}while(npcs[i].getSala()==-1);
			
			int margen = 60;

			float minimoX = mapa.getVectorZonas()[npcs[i].getSala()].getPosition().x - mapa.getVectorZonas()[npcs[i].getSala()].getAncho()/2;
			float minimoY = mapa.getVectorZonas()[npcs[i].getSala()].getPosition().y - mapa.getVectorZonas()[npcs[i].getSala()].getAlto()/2;
			
			float maximoX = ((mapa.getVectorZonas()[npcs[i].getSala()].getAncho()) - (margen * Utiles.PPM)) + minimoX;
			float maximoY = ((mapa.getVectorZonas()[npcs[i].getSala()].getAlto()) - (margen * Utiles.PPM)) + minimoY;
			
			float posX = (Utiles.r.nextFloat() * (maximoX - minimoX) + minimoX) + (margen/2)*Utiles.PPM;
			float posY = (Utiles.r.nextFloat() * (maximoY - minimoY) + minimoY) + (margen/2)*Utiles.PPM;
			
			npcs[i].setPosicion(posX,posY);
		}
	}
	//--------------------------------------------------------------------------------------------------------------------------------------
	//-------------------------------------------------------------JUGADORES COSAS----------------------------------------------------------
	//--------------------------------------------------------------------------------------------------------------------------------------
	
	private void roboNPC() {
		int i = 0, resultadoRobo;
		sprRobo.setSize(16 * Utiles.PPM, 16 * Utiles.PPM);
		do {
			if (jugadorLadron.getPosition().dst(npcs[i].getPosition()) < 30 * Utiles.PPM 
			 && !npcs[i].isRobado()
			 && jugadorLadron.getPosition().y - (jugadorLadron.getAlto() / 2) < npcs[i].getPosition().y + (npcs[i].getCuerpo().getAlto() / 2)
			 && jugadorLadron.getPosition().y + (jugadorLadron.getAlto() / 2) > npcs[i].getPosition().y - (npcs[i].getCuerpo().getAlto() / 2)
			 && (jugadorLadron.isDerecha() && npcs[i].isDerecha() && jugadorLadron.getPosition().x < npcs[i].getPosition().x
		     || !jugadorLadron.isDerecha() && !npcs[i].isDerecha() && jugadorLadron.getPosition().x > npcs[i].getPosition().x) ) {

				if (jugadorLadron.getPosition().x > npcs[i].getPosition().x + (npcs[i].getCuerpo().getAncho() / 2)) {
					posX = npcs[i].getPosition().x + (npcs[i].getCuerpo().getAncho() / 2);
				} else if (jugadorLadron.getPosition().x < npcs[i].getPosition().x - (npcs[i].getCuerpo().getAncho())) {
					posX = npcs[i].getPosition().x - (npcs[i].getCuerpo().getAncho()) - (npcs[i].getCuerpo().getAncho() / 2);
				}
				sprRobo.setPosition(posX, npcs[i].getPosition().y - (npcs[i].getCuerpo().getAlto() / 2));
				
				Render.batch.begin();
				sprRobo.draw(Render.batch);
				Render.batch.end();

				if (jugadorLadron.isRobando()) {// compruebo que se este presionando la letra e			
					resultadoRobo = jugadorLadron.robar();
					if(resultadoRobo == 2) {
						npcs[i].detectarRobo();
					}
				}
			}
			i++;
		} while (i < npcs.length);
	}
	private void arrestar() {
		int i = 0;

		if (jugadorGuardia.getPosition().dst(jugadorLadron.getPosition()) < 30 * Utiles.PPM 
		 && 
		 ( (jugadorGuardia.getPosition().y + (jugadorGuardia.getAlto() / 2) < (jugadorLadron.getPosition().y + (jugadorLadron.getAlto() / 2))+ 3 * Utiles.PPM 
		 && jugadorGuardia.getPosition().y - (jugadorGuardia.getAlto() / 2) > (jugadorLadron.getPosition().y - (jugadorLadron.getAlto() / 2))- 3 * Utiles.PPM)
		 ||
		    (jugadorGuardia.getPosition().x + (jugadorGuardia.getAncho() / 2) < (jugadorLadron.getPosition().x + (jugadorLadron.getAncho() / 2))+ 3 * Utiles.PPM 
		 && jugadorGuardia.getPosition().x - (jugadorGuardia.getAncho() / 2) > (jugadorLadron.getPosition().x - (jugadorLadron.getAncho() / 2))- 3 * Utiles.PPM)
		 ) 
		    ){
			System.out.println("a");
			/*if( jugadorGuardia.getPosition().y < jugadorLadron.getPosition().y - (jugadorLadron.getCuerpo().getAlto()) ) {
				posY = jugadorLadron.getPosition().y - (jugadorLadron.getAlto()) - (jugadorLadron.getAlto() / 2);
				posX = jugadorLadron.getPosition().x - (jugadorLadron.getCuerpo().getAncho() / 2);
			}
			if( jugadorGuardia.getPosition().y > jugadorLadron.getPosition().y + (jugadorLadron.getCuerpo().getAlto()) ) {
				posY = jugadorLadron.getPosition().y + (jugadorLadron.getCuerpo().getAlto() / 2);
				posX = jugadorLadron.getPosition().x - (jugadorLadron.getCuerpo().getAncho() / 2);
			}*/
		/*	if( jugadorGuardia.getPosition().x > jugadorLadron.getPosition().x + (jugadorLadron.getCuerpo().getAncho()) ) {
				posY = jugadorLadron.getPosition().y - (jugadorLadron.getAlto() / 2);
				posX = jugadorLadron.getPosition().x + (jugadorLadron.getAncho() / 2);
			}
			if( jugadorGuardia.getPosition().x < jugadorLadron.getPosition().x + (jugadorLadron.getCuerpo().getAncho()) ) {
				posY = jugadorLadron.getPosition().y - (jugadorLadron.getAlto() / 2);
				posX = jugadorLadron.getPosition().x - jugadorLadron.getAncho() - (jugadorLadron.getAncho() / 2);
			}*/
			if (jugadorGuardia.getPosition().y + (jugadorGuardia.getAlto() / 2) < (jugadorLadron.getPosition().y + (jugadorLadron.getAlto() / 2))+ 3 * Utiles.PPM 
			 || jugadorGuardia.getPosition().y - (jugadorGuardia.getAlto() / 2) > (jugadorLadron.getPosition().y - (jugadorLadron.getAlto() / 2))- 3 * Utiles.PPM) {
						if( jugadorGuardia.getPosition().x > jugadorLadron.getPosition().x + (jugadorLadron.getCuerpo().getAncho()) ) {
							posX = jugadorLadron.getPosition().x + (jugadorLadron.getAncho() / 2);
						}else {
							posX = jugadorLadron.getPosition().x - jugadorLadron.getAncho() - (jugadorLadron.getAncho() / 2);
						}
						posY = jugadorLadron.getPosition().y - (jugadorLadron.getAlto() / 2);
					}
			if (jugadorGuardia.getPosition().x + (jugadorGuardia.getAncho() / 2) < (jugadorLadron.getPosition().x + (jugadorLadron.getAncho() / 2))+ 3 * Utiles.PPM 
			 || jugadorGuardia.getPosition().x - (jugadorGuardia.getAncho() / 2) > (jugadorLadron.getPosition().x - (jugadorLadron.getAncho() / 2))- 3 * Utiles.PPM) {
				if( jugadorGuardia.getPosition().y < jugadorLadron.getPosition().y - (jugadorLadron.getCuerpo().getAlto()) ) {
					posY = jugadorLadron.getPosition().y - (jugadorLadron.getAlto()) - (jugadorLadron.getAlto() / 2);
				}else {
					posY = jugadorLadron.getPosition().y + (jugadorLadron.getCuerpo().getAlto() / 2);
				}
				posX = jugadorLadron.getPosition().x - (jugadorLadron.getCuerpo().getAncho() / 2);
			}
			
			
			
			sprRobo.setPosition(posX, posY);

			Render.batch.begin();
			sprRobo.draw(Render.batch);
			Render.batch.end();
		}
		do {
			if (jugadorGuardia.getPosition().dst(npcs[i].getPosition()) < 30 * Utiles.PPM 
			 && jugadorGuardia.getPosition().y + (jugadorGuardia.getAlto() / 2) < (npcs[i].getPosition().y + (npcs[i].getAlto() / 2))+ 3 * Utiles.PPM 
			 && jugadorGuardia.getPosition().y - (jugadorGuardia.getAlto() / 2) > (npcs[i].getPosition().y - (npcs[i].getAlto() / 2))- 3 * Utiles.PPM
			 && jugadorGuardia.getPosition().x + (jugadorGuardia.getAncho() / 2) < (npcs[i].getPosition().x + (npcs[i].getAncho() / 2))+ 3 * Utiles.PPM 
			 && jugadorGuardia.getPosition().x - (jugadorGuardia.getAncho() / 2) > (npcs[i].getPosition().x - (npcs[i].getAncho() / 2))- 3 * Utiles.PPM){
				
				if (jugadorGuardia.getPosition().x > npcs[i].getPosition().x + (npcs[i].getCuerpo().getAncho() / 2)) {
					posX = npcs[i].getPosition().x + (npcs[i].getCuerpo().getAncho() / 2);
				} else if (jugadorGuardia.getPosition().x < npcs[i].getPosition().x - (npcs[i].getCuerpo().getAncho())) {
					posX = npcs[i].getPosition().x - (npcs[i].getCuerpo().getAncho()) - (npcs[i].getCuerpo().getAncho() / 2);
				}
				sprRobo.setPosition(posX, jugadorGuardia.getPosition().y - (jugadorGuardia.getCuerpo().getAlto() / 2));
				
				Render.batch.begin();
				sprRobo.draw(Render.batch);
				Render.batch.end();
			}
			i++;
		}while (i < npcs.length);

		if(jugadorGuardia.isArrestando()) {
			/*if(){
				
			}*/
		}
	}
}