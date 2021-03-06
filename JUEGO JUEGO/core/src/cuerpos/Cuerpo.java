package cuerpos;

import com.badlogic.gdx.math.Vector2;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import utiles.Utiles;

public class Cuerpo{
	
	private float ancho, alto;
	private Body bodyReferencia = null;
	private Fixture fixture;
	private int numeroZona= -1; //si numeroZona = -1 el cuerpo no es una zona
								//si numeroZona != -1 el cuerpo es una zona
	private boolean robado;
	
	private BodyDef def;
	
	public Cuerpo(World mundo,
				  float ancho, float alto, BodyType bodyType,
				  float positionX, float positionY) {
		this.ancho = ancho * Utiles.PPM;
		this.alto = alto * Utiles.PPM;	
		
		def = new BodyDef();
		def.type = bodyType;
		
		def.position.set((positionX * Utiles.PPM),(positionY * Utiles.PPM));
		def.fixedRotation = true;
		bodyReferencia = mundo.createBody(def);
		
		PolygonShape forma = new PolygonShape();
		forma.setAsBox((ancho * Utiles.PPM)/2, (alto * Utiles.PPM)/2);
		
		fixture = bodyReferencia.createFixture(forma, 1f);
		
		forma.dispose();
	}
	//--------------------------------------------------------------------------------------------------------------------------------------
	//-------------------------------------------------------------SETTERS------------------------------------------------------------------
	//--------------------------------------------------------------------------------------------------------------------------------------
	public void setZona(int numeroZona){
		this.numeroZona = numeroZona;			
		fixture.setSensor(true);
		setUserData(this);
	}
	public void setUserData(Object ojeto) {
		bodyReferencia.setUserData(ojeto);
	}
	public void setRobado(boolean robado) {
		this.robado = robado;
	}
	
	public void setPosition(float x, float y) {
		bodyReferencia.setTransform(x, y, 0);
	}
	//--------------------------------------------------------------------------------------------------------------------------------------
	//-------------------------------------------------------------GETTERS------------------------------------------------------------------
	//--------------------------------------------------------------------------------------------------------------------------------------
	public Vector2 getPosition() {
		return bodyReferencia.getPosition();
	}
	public float getAncho() {
		return ancho;
	}
	public float getAlto() {
		return alto;
	}
	public Body getBodyReferencia() {
		return bodyReferencia;
	}
	public int getZona(){
		return numeroZona;
	}
		public boolean isRobado() {
		return robado;
	}
}
