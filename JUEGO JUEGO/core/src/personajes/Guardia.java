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

public class Guardia extends Jugador {

	public boolean arrestando;
	
	public Guardia(Cuerpo cuerpo, String sprite) {
		super(cuerpo, sprite);
		
		Inputlistener = new InputListener() {
			public boolean keyDown (InputEvent event, int keycode) {
				if (keycode == Keys.D) fuerzaX = 1;//derecha
				if (keycode == Keys.A) fuerzaX = -1;//izquierda
				if (keycode == Keys.S) fuerzaY = -1;//abajo
				if (keycode == Keys.W) fuerzaY = 1;//arriba
				if (keycode == Keys.E) arrestando = true;
				return true;
			}
			public boolean keyUp (InputEvent event, int keycode) {
				if (keycode == Keys.D) fuerzaX = 0;//derecha
				if (keycode == Keys.A) fuerzaX = 0;//izquierda
				if (keycode == Keys.S) fuerzaY = 0;//abajo
				if (keycode == Keys.W) fuerzaY = 0;//arriba
				return false;
			}
		};
        addListener(Inputlistener);
	}
	
	//--------------------------------------------------------------------------------------------------------------------------------------
	//-------------------------------------------------------------SCENE 2D-----------------------------------------------------------------
	//--------------------------------------------------------------------------------------------------------------------------------------	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		Color color = getColor();
	    batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);
	    this.animacion.getSprite().draw(batch);
	}

	@Override
	public void act(float delta) {
		this.animacion.setPosicion(cuerpo.getPosition().x - (cuerpo.getAncho()/2) ,cuerpo.getPosition().y - (cuerpo.getAlto()/2));
		this.animacion.setTexReg(animacionMovimiento());
		this.cuerpo.getBodyReferencia().setLinearVelocity((fuerzaX * Utiles.PPM) * 100, (fuerzaY * Utiles.PPM) * 100);
		
		duracion += Gdx.graphics.getRawDeltaTime();
	}
	//--------------------------------------------------------------------------------------------------------------------------------------
	//-------------------------------------------------------------ANIMACION-----------------------------------------------------------------
	//--------------------------------------------------------------------------------------------------------------------------------------	
	@Override
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
	public boolean isArrestando() {
		return arrestando;
	}
}
