package juego.garbanzo.lol;
import com.badlogic.gdx.Game;

import pantallas.PantallaRonda1;
import pantallas.PantallaRonda2;

public class Principal extends Game {
	PantallaRonda1 ronda1 = new PantallaRonda1();
	PantallaRonda2 ronda2 = new PantallaRonda2();
	
	@Override
	public void create () {
		this.setScreen(ronda1);
	}

	@Override
	public void render () {
		super.render();
	}
	
	@Override
	public void dispose () {
	}
}
