package mapas;

import com.badlogic.gdx.maps.MapObject;

import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import utiles.Utiles;
import cuerpos.Cuerpo;

//import juego.Cuerpo;

public class Mapa{

	private World mundo;
	private TiledMap tileMap;

	private PolygonShape forma = new PolygonShape();
	
	private Cuerpo[] zonas = new Cuerpo[3];
	
	private int i; 
	public Mapa(TiledMap tileMap, World mundo) {
		this.tileMap = tileMap;
		this.mundo = mundo;
		cargarObjetosMapa();
	}
	
	private void cargarObjetosMapa() {
		for (int j = 0; j < tileMap.getLayers().getCount(); j++) {
			for(MapObject objeto : tileMap.getLayers().get(j).getObjects().getByType(RectangleMapObject.class)) {
				
				Rectangle rec = ((RectangleMapObject)objeto).getRectangle(); 
				
				//forma = getRectangleShape((RectangleMapObject) objeto);

				if(tileMap.getLayers().get(j) == tileMap.getLayers().get("zona")){
					//en este if se crean las zonas, osea lo que diferencia una sala de otra
					zonas[i] = new Cuerpo(mundo, (rec.getWidth()) , (rec.getHeight()) , 
									      BodyType.StaticBody , 
									      rec.getX()+rec.getWidth()/2 , rec.getY()+rec.getHeight()/2);
					zonas[i].setZona(i);
					i++;
				}else {
					//en el else se hacen el respto de cuerpos que del tiledMap
					new Cuerpo(mundo, rec.getWidth() , rec.getHeight() , 
						       BodyType.StaticBody , 
						       (rec.getX()+rec.getWidth()/ 2) , (rec.getY()+rec.getHeight()/ 2) );
				}
			}
		/*
			for(MapObject objeto : tileMap.getLayers().get(j).getObjects().getByType(PolygonMapObject.class)) {
				//Polygon polygon = getPolygon((PolygonMapObject)objeto);
				
				//PolygonShape polygon = new PolygonShape();
				Polygon polygon = ( (PolygonMapObject) objeto).getPolygon();  
				
				float[] vertices = polygon.getTransformedVertices();
			    float[] worldVertices = new float[vertices.length];
			    
				System.out.println("a");
				
				Body bodyReferencia = null;
				BodyDef def = new BodyDef();;
				Fixture fixture;
				
				def.type = BodyType.StaticBody;
				System.out.println();
				def.position.set(150* Utiles.PPM, 200* Utiles.PPM);
				
				def.fixedRotation = true;
				bodyReferencia = mundo.createBody(def);
				
		        for (int i = 0; i < vertices.length; ++i) {
		            System.out.println(vertices[i]);
		            worldVertices[i] = vertices[i] * Utiles.PPM;
		        }
		        polygon.setVertices(worldVertices);
		        
				fixture = bodyReferencia.createFixture(forma, 1f);
				
				forma.dispose();
			}*/
		}
	}
	
	private PolygonShape getPolygon(PolygonMapObject polygonObject) {
       PolygonShape polygon = new PolygonShape();
       float[] vertices = polygonObject.getPolygon().getTransformedVertices();
       float[] worldVertices = new float[vertices.length];

        for (int i = 0; i < vertices.length; ++i) {
            System.out.println(vertices[i]);
            worldVertices[i] = vertices[i] * Utiles.PPM;
        }
        polygon.set(worldVertices);
		return polygon;
    }
	private PolygonShape getCustom(PolygonMapObject customObject) {
		Polygon polyline = customObject.getPolygon();
		PolygonShape polygon = new PolygonShape();
		polygon.set(polyline.getVertices());
		
		return polygon;
	}
   private PolygonShape getRectangleShape(RectangleMapObject rectangleObject) {
       Rectangle rectangle = rectangleObject.getRectangle();
       PolygonShape polygon = new PolygonShape();
       Vector2 size = new Vector2((rectangle.x + rectangle.width * Utiles.PPM),
                                  (rectangle.y + rectangle.height * Utiles.PPM));
       polygon.setAsBox(rectangle.width * Utiles.PPM,
                        rectangle.height * Utiles.PPM,
                        size,
                        0.0f);
       return polygon;
   }    
	public World getMundo() {
		return mundo;
	}
	public Cuerpo[] getVectorZonas() {
		return zonas;
	}
}
