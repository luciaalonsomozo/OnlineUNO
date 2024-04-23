package storage;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import logic.Game;

/**
 * Clase encargada de la comunicación entre el sistema de archivos y el modelo con 
 * el fin de abstraer la forma en que se guarda y carga la partida.
 *
 */
public class Storage {
	
	/**
	 * Localización de la carpeta donde se encuentran los archivos de guardado.
	 */
	private static final String MASTER_SAVE_FOLDER = "saves/";
	/**
	 * Localización del archivo maestro de guardado, donde se tendrá una lista con
	 * las partidas guardadas.
	 */
	private static final String MASTER_SAVE_FILE = "saves/save.json";
	
	/**
	 * Referencia del juego que se va a guardar/cargar.
	 */
	private Game game;

	/**
	 * Se inicializa la referencia al juego.
	 * 
	 * @param game Juego que se va a guardar/cargar.
	 */
	public Storage(Game game) {
		this.game = game;
	}	
	
	/**
	 * Método que permite sobreescribir una partida guardada.
	 * 
	 * @param fileName Localización del archivo de guardado.
	 * @param id Identificador de la partida guardada.
	 * @throws IOException Fallo en la escritura/lectura.
	 */
	private void override(String fileName, String id) throws IOException {
		BufferedWriter p = new BufferedWriter(new FileWriter(fileName));
		JSONObject gameJSON = game.report();
		gameJSON.put("id", id);
		p.write(gameJSON.toString());
		p.close();
	}
	
	/**
	 * Método para crear una nueva partida guardada.
	 * 
	 * @param id Identificador de la partida guardada.
	 * @param index Índice en la lista de partidas guardadas de aquella que se va a cargar.
	 * @return Archivo de guardado.
	 * @throws IOException Fallo en lectura/escritura.
	 */
	private JSONObject createNewSave(String id, int index) throws IOException {
		String fileName = "saves/save" + index + ".json";
		override(fileName, id);
		JSONObject save = new JSONObject();
		save.put("id", id);
		save.put("filename", fileName);
		return save;
	}
	
	/**
	 * Método para guardar una partida, ya sea sobreescribir o crearla de 0. Si no 
	 * había ninguna partida guardada crea el archivo maestro.
	 * 
	 * @param id Identificador de la partida guardada.
	 * @throws IOException Fallo en lectura/escritura.
	 */
	public void save(String id) throws IOException {
		BufferedReader p;
		try {
			p = new BufferedReader(new FileReader(MASTER_SAVE_FILE));
			String fileName = null; 
			JSONObject saveFilesFolder = new JSONObject(new JSONTokener(p)); 
			JSONArray saveFiles = saveFilesFolder.getJSONArray("savefiles");
			for (int i = 0; i < saveFiles.length(); i++) {
				if (saveFiles.getJSONObject(i).getString("id").equals(id)) {
					fileName = saveFiles.getJSONObject(i).getString("filename");
					break;
				}
			}
			if (fileName != null) {
				override(fileName, id);
				p.close();
			}
			else {
				JSONObject newSave = createNewSave(id, saveFiles.length());
				saveFiles.put(newSave);
				JSONObject newSaveFilesFolder = new JSONObject();
				newSaveFilesFolder.put("savefiles", saveFiles);
				p.close();
				BufferedWriter wr = new BufferedWriter(new FileWriter(MASTER_SAVE_FILE));
				wr.write(newSaveFilesFolder.toString());
				wr.close();
			}
		}
		catch(IOException e) {
			new File(MASTER_SAVE_FOLDER).mkdirs();
			BufferedWriter newFile = new BufferedWriter(new FileWriter(MASTER_SAVE_FILE));
			JSONObject newSaveFilesFolder = new JSONObject();
			JSONArray newSaveFiles = new JSONArray();
			newSaveFilesFolder.put("savefiles", newSaveFiles);
			newFile.write(newSaveFilesFolder.toString());
			newFile.close();
			save(id);
		}
	}

	/**
	 * Carga las partidas disponibles para permitir que el modelo mantenga una lista de ellas.
	 * 
	 * @return Lista de los archivos de guardados.
	 * @throws IOException Fallo en lectura/escritura.
	 */
	public List<JSONObject> loadAvailables() throws IOException {
		List<JSONObject> res = new ArrayList<JSONObject>();
		BufferedReader p = new BufferedReader(new FileReader(MASTER_SAVE_FILE));
		JSONObject data = new JSONObject(new JSONTokener(p));
		JSONArray savesArray = data.getJSONArray("savefiles");
		
		JSONObject currentSave;
		for (int i = 0; i < savesArray.length(); i++) {
			currentSave = load(i);
			res.add(currentSave);
		}
		
		return res;
	}
	
	/**
	 * Carga la partida guardada en la lista con el índice. 
	 * 
	 * @param index Índice de la partida guardada en la lista de archivos de guardado.
	 * @return Archivo de guardado.
	 * @throws IOException Fallo en lectura/escritura.
	 */
	public JSONObject load(int index) throws IOException {
		BufferedReader p1 = new BufferedReader(new FileReader(MASTER_SAVE_FILE));
		JSONObject auxMaster = new JSONObject(new JSONTokener(p1));
		JSONArray savesArray = auxMaster.getJSONArray("savefiles");
		JSONObject auxSave = savesArray.getJSONObject(index);
		String fileName = auxSave.getString("filename");	
		p1.close();
		
		BufferedReader p2 = new BufferedReader(new FileReader(fileName));
		JSONObject result = new JSONObject(new JSONTokener(p2));
		p2.close();
		return result;
	}
	
}
