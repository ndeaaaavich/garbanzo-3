package personajes;

public enum SpriteInfo {

	UNO("personajes/s1.png",new int[]{1,1,1}),
	DOS("personajes/s2.png",new int[]{1,3,2}),
	TRES("personajes/s1.png",new int[]{1,2,1}),
	CUATRO("personajes/s2.png",new int[]{2,2,2}),
	CINCO("personajes/s1.png",new int[]{2,2,3}),
	SEIS("personajes/s2.png",new int[]{2,1,3}),
	SIETE("personajes/s1.png",new int[]{3,3,3}),
	OCHO("personajes/s2.png",new int[]{3,1,2}),
	NUEVE("personajes/s1.png",new int[]{3,3,1});
	
	private String filename;
	private int[] apariencia = new int[3];
	
	SpriteInfo(String filename, int[] apariencia) {
		this.filename = filename;
		this.apariencia = apariencia;
	}
	
	public int[] getApariencia() {
		return apariencia;
	}
	
	public String getFilename() {
		return filename;
	}
	
}
