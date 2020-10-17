package utiles;

import java.util.ArrayList;
import java.util.EventListener;
import java.util.Random;
import java.util.Scanner;



public class Utiles {
	
	public static Scanner s = new Scanner(System.in);
	public static Random r = new Random();
	public static float PPM = 0.01f;
	
	private static ArrayList<EventListener> listeners = new ArrayList<EventListener>();
	
	public static void addListener(EventListener listener) {
		listeners.add(listener);
	}
	public static ArrayList<EventListener> getListeners() {
		return listeners;
	}
}
