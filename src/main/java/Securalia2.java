import java.nio.file.Path;
import java.nio.file.Paths;

public class Securalia2 { //Este es el antes llamado Main2

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		DefinicionCopiaDAO.leerFechas();
		crearTarea();
	}
	
	public static void crearTarea() {
	    try {
	        //En principio, esto obtiene la ruta sea donde sea que esté nuestro Securalia2.jar
	        String rutaJAR = Securalia2.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
	        
	        //En principio, esto elimina la innecesaria "/" de el principio del la ruta, si la tiene.
	        if (rutaJAR.startsWith("/")) {
	            rutaJAR = rutaJAR.substring(1);
	        }
	        
	        Path path = Paths.get(rutaJAR);
	        
	        //El comando que parece no generar bien la tarea.
	        String tarea = "schtasks /create /tn \"Securalia2\" /tr \"java -jar \\\""+path.toAbsolutePath().toString()+"\\\"\" /sc onlogon";

	        //Ejecutamos el comando y se supone que todo va bien e imprime por consola el "Tarea programada creada correctamente.", pero al buscarla en CMD no aparece.
	        Process process = Runtime.getRuntime().exec(tarea);
	        process.waitFor();

	        System.out.println("Tarea programada creada correctamente.");
	    } catch (Exception e) {
	        System.out.println("Error al crear la tarea programada.");
	        e.printStackTrace();
	    }
	}

}
