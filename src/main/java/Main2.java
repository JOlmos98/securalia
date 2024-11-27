import java.nio.file.Path;
import java.nio.file.Paths;

public class Main2 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		DefinicionCopiaDAO.leerFechas();
		crearTarea();

	}
	
	public static void crearTarea() {
        try { 
        	String rutaJAR = Securalia2.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        	Path path = Paths.get(rutaJAR);
            // Ruta del archivo .bat que ejecutará tu programa .jar       )
        	String tarea = "schtasks /create /tn \"Securalia2\" /tr \"java -jar \\\""+path.toAbsolutePath().toString()+"\\\"\" /sc onlogon";

            // Ejecutamos el comando para crear la tarea programada
            Process process = Runtime.getRuntime().exec(tarea);
            process.waitFor();

            System.out.println("Tarea programada creada correctamente.");
        } catch (Exception e) {
            System.out.println("Error al crear la tarea programada.");
            e.printStackTrace();
        }
    }
}
