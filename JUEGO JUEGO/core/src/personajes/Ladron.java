package personajes;

import com.badlogic.gdx.Gdx;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import utiles.Utiles;

import cuerpos.Cuerpo;
import eventos.InterfaceRobable;

public class Ladron extends Jugador{
	
	private boolean robando;
	private int roboTiempo = 0;
	private int billeteras = 0;

	private int numAzar;
	
	public Ladron(Cuerpo cuerpo, String sprite) {
		super(cuerpo, sprite);

		Inputlistener = new InputListener() {
			public boolean keyDown (InputEvent event, int keycode) {
				if (keycode == Keys.DPAD_RIGHT) fuerzaX = 1;//derecha
				if (keycode == Keys.DPAD_LEFT) fuerzaX = -1;//izquierda
				if (keycode == Keys.DPAD_DOWN) fuerzaY = -1;//abajo
				if (keycode == Keys.DPAD_UP) fuerzaY = 1;//arriba
				if (keycode == Keys.E) robando = true;
				return true;
			}
			public boolean keyUp (InputEvent event, int keycode) {
				if (keycode == Keys.DPAD_RIGHT) fuerzaX = 0;//derecha
				if (keycode == Keys.DPAD_LEFT) fuerzaX = 0;//izquierda
				if (keycode == Keys.DPAD_DOWN) fuerzaY = 0;//abajo
				if (keycode == Keys.DPAD_UP) fuerzaY = 0;//arriba
				if (keycode == Keys.E) robando = false; roboTiempo = 0;
				return false;
			}
		};
        addListener(Inputlistener);
        cuerpo.setUserData(this);
	}
	//--------------------------------------------------------------------------------------------------------------------------------------
	//-------------------------------------------------------------ACCIONES-----------------------------------------------------------------
	//--------------------------------------------------------------------------------------------------------------------------------------
	public int robar() {
		//0 robo exitoso, 1 nada, 2 detectado
		int randomF = Utiles.r.nextInt(100)+1;
		
		if(roboTiempo == 0) {numAzar = randomF;}
		if(roboTiempo!=0 && numAzar == randomF) {return 2;}
		
		System.out.println(roboTiempo);
		if(roboTiempo++ == 100) {
			
			for(int i = 0; i<Utiles.getListeners().size();i++) {
				((InterfaceRobable) Utiles.getListeners().get(i)).robado(this.getSala());
			}
			
			billeteras ++;
			System.out.println("robo con exito");
			return 0;
		}else {
			return 1;
		}
	}
	//--------------------------------------------------------------------------------------------------------------------------------------
	//-------------------------------------------------------------SCENE 2D-----------------------------------------------------------------
	//--------------------------------------------------------------------------------------------------------------------------------------
	public void draw(Batch batch, float parentAlpha){
		Color color = getColor();
	    batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);
	    this.animacion.getSprite().draw(batch);
	}
	public void act(float delta){
		this.animacion.setPosicion(cuerpo.getPosition().x - (cuerpo.getAncho()/2) ,cuerpo.getPosition().y - (cuerpo.getAlto()/2));
		this.animacion.setTexReg(animacionMovimiento());
		this.cuerpo.getBodyReferencia().setLinearVelocity((fuerzaX * Utiles.PPM) * 100, (fuerzaY * Utiles.PPM) * 100);
		
		duracion += Gdx.graphics.getRawDeltaTime();
	}
	//--------------------------------------------------------------------------------------------------------------------------------------
	//-------------------------------------------------------------ANIMACION-----------------------------------------------------------------
	//--------------------------------------------------------------------------------------------------------------------------------------
	protected TextureRegion animacionMovimiento() {
		if(fuerzaX>0) {// moverse a la derecha
			setDerecha(true);
			ultimoIndice = 0;
			return animacion.getTexReg(0, duracion);
		}
		if(fuerzaX<0) {// moverse a la izquierda
			setDerecha(false);
			ultimoIndice = 1;
			return animacion.getTexReg(1, duracion);
		}
		if(fuerzaY!=0) {
			return animacion.getTexReg(ultimoIndice, duracion);
		}
		return animacion.getTexReg((ultimoIndice==1)?3:2, duracion);
	}
	//--------------------------------------------------------------------------------------------------------------------------------------
	//-------------------------------------------------------------GETTERS------------------------------------------------------------------
	//--------------------------------------------------------------------------------------------------------------------------------------
	public boolean isRobando() {
		return robando;
	}	
}
