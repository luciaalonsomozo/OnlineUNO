package logic.gameobjects;

import java.io.Serializable;

/**
 * 
 * Clase que representa los colores posibles de las cartas: azul, rojo, verde y amarillo.
 * Cada color presenta su código ANSI.
 *
 */
public enum ColorUNO implements Serializable{
	
	BLUE("\033[0;94m"), RED("\u001B[31m"), GREEN("\u001B[32m"), YELLOW("\u001B[33m");
	
	private String code;
	
	private ColorUNO(String code) {
		this.code = code;
	}
	
	public static ColorUNO getColor(String color) {
		return ColorUNO.valueOf(color.toUpperCase());
	}
	
	public String code() {
		return code;
	}
	
}
