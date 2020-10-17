package personajes;

import com.badlogic.gdx.Gdx;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import utiles.Utiles;

import cuerpos.Cuerpo;
import eventos.InterfaceRobable;

public class NPC extends Entidad implements InterfaceRobable{
	private int movimiento; // 1 arriba 2 abajo 3 izquierda 4 derecha 5-40 nada
	private float tiempoMov;
	private boolean finRecorrido, detectado, derecha, CambioDirec, robado = false;
	private float tiempo, tiempoDetectado;
	private Sprite sprExclamation = new Sprite( new Texture("personajes/exclamacion-removebg-preview.png") );

	int[] apariencia = new int[3]; 
	//0 pelo 
	//1 torso
	
	public NPC(Cuerpo cuerpo, String sprite, int[] apariencia) {
		super(cuerpo, sprite);
		
		this.apariencia = apariencia;

        Utiles.addListener(this);
		cuerpo.setUserData(this);
	}
	// --------------------------------------------------------------------------------------------------------------------------------------
	// -------------------------------------------------------------ACCIONES-----------------------------------------------------------------
	// --------------------------------------------------------------------------------------------------------------------------------------
	public void detectarRobo() {
		setCambioDirec(true);
		setRobado(true);
		detectado = true;
	}
	@Override
	public void robado(int sala) {
		if(getSala() == sala) {
			setRobado(true);
		}
	}
	// --------------------------------------------------------------------------------------------------------------------------------------
	// -------------------------------------------------------------SCENE 2D-----------------------------------------------------------------
	// --------------------------------------------------------------------------------------------------------------------------------------
	public void draw(Batch batch, float parentAlpha) {
		Color color = getColor();
		batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);
		animacion.getSprite().draw(batch);
		
		sprExclamation.setSize(16 * Utiles.PPM, 16 * Utiles.PPM);
		sprExclamation.setPosition(this.cuerpo.getPosition().x, this.cuerpo.getPosition().y + (cuerpo.getAlto() / 2));
		if(detectado){
			sprExclamation.draw(batch);
		}
	}
	public void act(float delta) {
		this.animacion.setPosicion(this.cuerpo.getPosition().x - (cuerpo.getAncho() / 2) ,
								   this.cuerpo.getPosition().y - (cuerpo.getAlto() / 2));
		this.animacion.setTexReg(animacion.getTexReg(0, delta));

		tiempo += Gdx.graphics.getRawDeltaTime();

		if(detectado) {
			tiempoDetectado += Gdx.graphics.getRawDeltaTime();
			if(tiempoDetectado > 5){
				tiempoDetectado = 0;
				detectado = false;
				setRobado(false);
			}
		}
		
		duracion += delta;
		
		if(tiempo < tiempoMov) {
			finRecorrido = false;
			
			if(CambioDirec) {
				int ultimoMov = movimiento;
				
				do {
					movimiento = Utiles.r.nextInt(4)+1; 
					movimiento = (movimiento == ultimoMov)?Utiles.r.nextInt(10)+1:movimiento;
				}while(movimiento == ultimoMov);
				CambioDirec = false;
			
			}else {
				finRecorrido = true;
			}
		}else if(tiempo > tiempoMov && duracion < tiempoMov * 2){
			finRecorrido = true;
		}else {
			tiempoMov = Utiles.r.nextFloat() + 0.69f;
			//movimiento = Utiles.r.nextInt(10)+1;
			movimiento = Utiles.r.nextInt(50)+1; 
			//movimiento = Utiles.r.nextInt(50)+5; 
			tiempo = 0;
		}
		
		if(finRecorrido) {
			this.cuerpo.getBodyReferencia().setLinearVelocity(( (fuerzaX * -1) * Utiles.PPM) * 100, ( (fuerzaY * -1) * Utiles.PPM) * 100);	
		}else {
			this.cuerpo.getBodyReferencia().setLinearVelocity(( (fuerzaX) * Utiles.PPM) * 100, ( (fuerzaY) * Utiles.PPM) * 100);	
		}
		animacion.setTexReg(animacionMovimiento());
	}
	// --------------------------------------------------------------------------------------------------------------------------------------
	// -------------------------------------------------------------ANIMACION----------------------------------------------------------------
	// --------------------------------------------------------------------------------------------------------------------------------------
	@Override
	protected TextureRegion animacionMovimiento() {
		switch (movimiento) {
		case 1:
			if(!CambioDirec) {
				fuerzaX = 0;
				fuerzaY = 1;
			}
			setDerecha((ultimoIndice==0)?true:false);
			return animacion.getTexReg(ultimoIndice, duracion);
		case 2:
			if(!CambioDirec) {
				fuerzaX = 0;
				fuerzaY = -1;
			}
			setDerecha((ultimoIndice==0)?true:false);
			return animacion.getTexReg(ultimoIndice, duracion);
		case 3:
			if(!CambioDirec) {
				fuerzaY = 0;
				fuerzaX = -1;
			}
			if(finRecorrido) {
				setDerecha(true);
				ultimoIndice = 0;
				return animacion.getTexReg(0, duracion); 
			}else {
				setDerecha(false);
				ultimoIndice = 1;
				return animacion.getTexReg(1, duracion);		
			}
		case 4:
			if(!CambioDirec) {
				fuerzaY = 0;
				fuerzaX = 1;
			}
			if(finRecorrido) {
				setDerecha(false);
				ultimoIndice = 1;
				return animacion.getTexReg(1, duracion);
			}else {
				setDerecha(true);
				ultimoIndice = 0;
				return animacion.getTexReg(0, duracion);		
			}
		default:
			fuerzaX = 0;
			fuerzaY = 0;
			return animacion.getTexReg(2, duracion);
		}
	}
	// --------------------------------------------------------------------------------------------------------------------------------------
	// -------------------------------------------------------------GETTERS------------------------------------------------------------------
	// --------------------------------------------------------------------------------------------------------------------------------------
	public boolean isRobado() {
		return robado;
	}
	public boolean isCambioDirec() {
		return CambioDirec;
	}
	// --------------------------------------------------------------------------------------------------------------------------------------
	// -------------------------------------------------------------SETTERS------------------------------------------------------------------
	// --------------------------------------------------------------------------------------------------------------------------------------
	public void setRobado(boolean robado) {
		this.robado = robado;
	}
	public void setPosicion(float x, float y) {
    	this.animacion.setPosicion(x,y);
    	this.cuerpo.setPosition(x,y);
	}
	public void setCambioDirec(boolean parar) {
		this.CambioDirec = parar;
		tiempo = 0;
	}
}
