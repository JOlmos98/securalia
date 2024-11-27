import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.stream.Stream;

public class DefinicionCopiaDAO {
	
	//Variables:

    private static File firstPath = new File(".\\src\\main\\resources\\securalia.db");
    private static File secondPath = new File("securalia.db");
	private static String url;
	private static String usuario = "root";
	private static String password = "";
	
	//Constructor:
	
	public DefinicionCopiaDAO() {}
	
	static {
	    if (firstPath.exists()) {
	        url = "jdbc:sqlite:.\\src\\main\\resources\\securalia.db?journal_mode=WAL";
	        System.out.println("Usando base de datos en: " + firstPath.getAbsolutePath());
	    } else if (secondPath.exists()){
            url = "jdbc:sqlite:" + secondPath.getAbsolutePath() + "?journal_mode=WAL";
            System.out.println("Usando base de datos en: " + secondPath.getAbsolutePath());
        } else {
	        url = "jdbc:sqlite:" + secondPath.getAbsolutePath() + "?journal_mode=WAL";
	        System.out.println("Base de datos no encontrada. Creando nueva en: " + secondPath.getAbsolutePath());
	        crearBaseDeDatos();
	        /*
	         * Este bloque static (se ejecuta con cada instancia de DefinicionCopiaDAO) valida si la base de
	         * datos se encuentra en el directorio del proyecto, es decir, mientras lo desarrollo, si no,
	         * valida que se encuentre en el mismo directorio que nuestro hipotético .jar, si no, la crea.
	         */
	    }
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////////
	// ---------------------------------------- Métodos PROGRAMA 1: ----------------------------------------
	////////////////////////////////////////////////////////////////////////////////////////////////////////

	private static void crearBaseDeDatos() { //Este método se ejecuta según el bloque static de arriba.
	    String crearTablaSQL =
	        "CREATE TABLE IF NOT EXISTS DefinicionesCopias ("+
	            "\nId INTEGER PRIMARY KEY,"+
	            "\nNombre TEXT NOT NULL,"+
	            "\nDirectorioOrigen TEXT NOT NULL,"+
	            "\nDirectorioDestino TEXT NOT NULL,"+
	            "\nIntervaloDias INTEGER NOT NULL,"+
	            "\nFechaUltimaCopia TEXT"+
	            "\n);";
	    try (Connection con = DriverManager.getConnection(url)) {
	        try (PreparedStatement ps = con.prepareStatement(crearTablaSQL)) {
	            ps.execute();
	            System.out.println("Tabla 'DefinicionesCopias' creada exitosamente.");
	        }
	    } catch (SQLException ex) {
	        System.err.println("Error al crear la base de datos o la tabla.");
	        ex.printStackTrace();
	    }
	}
	
	public static boolean crear(DefinicionCopia dc) {
		try (Connection con = DriverManager.getConnection(url,usuario,password)){
			try {
				String sql = "INSERT INTO DefinicionesCopias (Id, Nombre, DirectorioOrigen, DirectorioDestino, IntervaloDias, FechaUltimaCopia) VALUES (?,?,?,?,?,?)";
				PreparedStatement ps = con.prepareStatement(sql);
				ps.setInt(1, dc.getId());
				ps.setString(2, dc.getNombre());
				ps.setString(3, dc.getDirectorioOrigen());
				ps.setString(4, dc.getDirectorioDestino());
				ps.setInt(5, dc.getIntervaloDias());
				ps.setString(6, dc.getFechaUltimaCopia().toString());
				System.out.println("Creando copia con ID: "+dc.getId());
				ps.executeUpdate();
			} 
			
			finally {con.close();}
		} 
		catch (SQLException ex) 
		{
			System.err.println("Error en el proceso de alta");
			ex.printStackTrace();
			return false;
		}
		System.out.println("Copia creada.");
		return true;
	}
	
	public static int getIdMax() { //Obtenemos el Id máximo para que el método crear() se ubique y cree a partir de ese Id estableciendo el contadorId, línea comentada en el método crear() del Main1.
	    int maxId=-1;
	    String sql="SELECT MAX(Id) AS maxId FROM DefinicionesCopias";
	    
	    try (Connection con = DriverManager.getConnection(url, usuario, password)) {	        
	        try (PreparedStatement ps = con.prepareStatement(sql);
	             ResultSet rs = ps.executeQuery()) { 
	            if (rs.next()) { 
	                maxId = rs.getInt("maxId"); 
	            } else {
	                System.out.println("No se encontraron registros en la tabla.");
	            }
	        }
	    } catch (SQLException ex) {
	        System.err.println("Error en la consulta de ID.");
	        ex.printStackTrace();
	    }
	    return maxId;
	}

	public static boolean listar(int id) {
	    String sql = "SELECT * FROM DefinicionesCopias WHERE Id=?";
	    
	    try (Connection con = DriverManager.getConnection(url, usuario, password)) {
	        try (PreparedStatement ps = con.prepareStatement(sql)) {
	            ps.setInt(1, id);
	            try (ResultSet rs = ps.executeQuery()) {
	                if (rs.next()) { 
	                    System.out.println("ID: " + rs.getInt("Id"));
	                    System.out.println("Nombre: " + rs.getString("Nombre"));
	                    System.out.println("Dir. Origen: "+rs.getString("DirectorioOrigen"));
	                    System.out.println("Dir. Destino: "+rs.getString("DirectorioDestino"));
	                    System.out.println("Intervalo: " + rs.getString("IntervaloDias"));
	                    System.out.println("Fecha: " + rs.getString("FechaUltimaCopia"));
	                } else {
	                    System.err.println("No se encontró ningún registro con el ID proporcionado.");
	                }
	            }
	        }
	    } catch (SQLException ex) {
	        System.err.println("Error al realizar la consulta.");
	        ex.printStackTrace();
	        return false;
	    }
	    return true;
	}

	
	public static boolean listarTodas() {
	    String sql = "SELECT * FROM DefinicionesCopias";
	    
	    try (Connection con = DriverManager.getConnection(url, usuario, password)) {
	        try (PreparedStatement ps = con.prepareStatement(sql)) {
	            try (ResultSet rs = ps.executeQuery()) {
	                if (rs.next()) { 
                		System.out.println("ID: " + rs.getInt("Id")+" || "+"Nombre: " + rs.getString("Nombre")+" || "+"Dir. Origen: "+rs.getString("DirectorioOrigen")+" || "+"Dir. Destino: "+rs.getString("DirectorioDestino")+" || "+"Intervalo: " + rs.getString("IntervaloDias")+" || "+"Fecha: " + rs.getString("FechaUltimaCopia"));
	                	while (rs.next()) {
	                		System.out.println("ID: " + rs.getInt("Id")+" || "+"Nombre: " + rs.getString("Nombre")+" || "+"Dir. Origen: "+rs.getString("DirectorioOrigen")+" || "+"Dir. Destino: "+rs.getString("DirectorioDestino")+" || "+"Intervalo: " + rs.getString("IntervaloDias")+" || "+"Fecha: " + rs.getString("FechaUltimaCopia"));
	                	}
	                } else {
	                    System.err.println("No se encontró ningún registro con el ID proporcionado.");
	                }
	            }
	        }
	    } catch (SQLException ex) {
	        System.err.println("Error al realizar la consulta.");
	        ex.printStackTrace();
	        return false;
	    }
	    return true;
	}
	
	public static boolean eliminar(int id) { //Aquí hago una primera consulta para mostrar el registro que se va a eliminar antes de eliminarlo.
	    String sql1 = "SELECT * FROM DefinicionesCopias WHERE Id=?"; 
	    String sql2 = "DELETE FROM DefinicionesCopias WHERE Id=?";
	    try (Connection con = DriverManager.getConnection(url, usuario, password)) {
	        try (PreparedStatement ps = con.prepareStatement(sql1)) {
	            ps.setInt(1, id);     
	            try (ResultSet rs = ps.executeQuery()) {
	                if (rs.next()) { 
	                	System.err.println("DEFINICION DE COPIA ELIMINADA: "
	                    +"\nID: " + rs.getInt("Id")
	                    +"\nNombre: " + rs.getString("Nombre")
	                    +"\nDir. Origen: "+rs.getString("DirectorioOrigen")
	                    +"\nDir. Destino: "+rs.getString("DirectorioDestino")
	                    +"\nIntervalo: " + rs.getString("IntervaloDias")
	                    +"\nFecha: " + rs.getString("FechaUltimaCopia"));
	                } else {
	                    System.err.println("No se encontró ningún registro con el ID proporcionado.");
	                }
	            }
	        }
	        try (PreparedStatement ps = con.prepareStatement(sql2)) {
	            ps.setInt(1, id);
	            int filasAfectadas = ps.executeUpdate();
	            if (filasAfectadas > 0) {
	                System.out.println("Registro con ID " + id + " eliminado correctamente.");
	                return true;
	            } else {
	                System.err.println("No se encontró ningún registro con el ID proporcionado.");
	            }
	        }
	    } catch (SQLException ex) {
	        System.err.println("Error al realizar operación.");
	        ex.printStackTrace();
	        return false;
	    }
	    return true;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////////
	// ---------------------------------------- Métodos PROGRAMA 2: ----------------------------------------
	////////////////////////////////////////////////////////////////////////////////////////////////////////

	public static boolean leerFechas() { //Este método usa copiar() y actualizarFechaUltimaCopia()
		
		String sql = "SELECT * FROM DefinicionesCopias";
		boolean copiaDisponible=false;
		
	    try (Connection con = DriverManager.getConnection(url, usuario, password)) {
	        try (PreparedStatement ps = con.prepareStatement(sql)) {
	            try (ResultSet rs = ps.executeQuery()) {
                		while (rs.next()) {
	                		if (ChronoUnit.DAYS.between(LocalDate.parse(rs.getString("FechaUltimaCopia")), LocalDate.now())>=rs.getInt("IntervaloDias")) {
	                			System.out.println("Intervalo de días cumplido en el registro: "+rs.getInt("Id"));
	                			copiaDisponible=true;
	                			Path dirOrigen=Paths.get(rs.getString("DirectorioOrigen"));
	                			Path dirDestino=Paths.get(rs.getString("DirectorioDestino"));
	                			copiar(dirOrigen, dirDestino);
	                			actualizarFechaUltimaCopia(rs.getInt("Id"));
	                		};
	                	}
	            }
	        }		if (copiaDisponible) System.out.println("Copias realizadas."); else System.out.println("No hay copias a realizar.");
	    } catch (SQLException ex) {
	        System.err.println("Error al realizar la consulta.");
	        ex.printStackTrace();
	        return false;
	    }
	    return true;
	}
	
	public static boolean copiar(Path dirOrigen, Path dirDestino) {
		if (dirOrigen==null||dirDestino==null) {
			System.out.println("Faltan directorios por especificar."); 
			return false;
		}
		
		String fechaActual=LocalDate.now().format(DateTimeFormatter.ofPattern("yyMMdd"));
		Path dirOri=dirOrigen;
		int ultimoSlash=dirOrigen.toString().lastIndexOf("\\");
		String dirOriNombre=dirOrigen.toString().substring(ultimoSlash);
		Path dirDes=Paths.get(dirDestino.toString()+"\\"+dirOriNombre+"-"+fechaActual);
		
		try {if (!Files.exists(dirDes)) Files.createDirectories(dirDes);
		} catch(Exception e){
			System.out.println("Error al crear el directorio de destino.");
			e.printStackTrace();
			return false;
		  }
		
		try (Stream<Path> paths = Files.walk(dirOri)){
			
			////////////////////////////// ↓ COPIAR ↓ //////////////////////////////
			paths.forEach(path -> {
                   try {
                        //Calculamos la ruta relativa dentro de pathOldDir
                        Path relativePath = dirOri.relativize(path);
                        //Obtenemos la ruta completa en el nuevo directorio
                        Path targetPath = dirDes.resolve(relativePath);

                        //Si es un directorio lo que lee el Stream, lo creamos
                        if (Files.isDirectory(path)) {
                            if (!Files.exists(targetPath)) { //Evidentemente si no existe el directorio de destino, también se crea
                                Files.createDirectories(targetPath);
                            }
                        }
                        //Si es un archivo regular lo que lee el Stream, lo copiamos
                        else if (Files.isRegularFile(path)) {
                        	if (!Files.exists(targetPath)) {
                                Files.createDirectories(targetPath.getParent()); //Esta línea se asegura de que el directorio padre existe
                                Files.copy(path, targetPath);
                        	} else System.out.println("El archivo "+path.toString()+" ya existe, no se copia.");
                        }
                    } catch (IOException e) {
                        System.out.println("Error copiando el archivo: " + path);
                        e.printStackTrace();
                    }
                });
			////////////////////////////// ↑ COPIAR ↑ //////////////////////////////

			} catch (Exception e) {
				System.err.println("Error al copiar el directorio.");
				e.printStackTrace();
				return false;
			}
		System.out.println("Copia realizada en: "+dirDes.toString());
		return true;
	}
	
	public static void actualizarFechaUltimaCopia(int id) { //Este método sencillamente, después de realizar una copia, establece la fechaUltimaCopia al día actual
		String sql="UPDATE DefinicionesCopias SET FechaUltimaCopia=? WHERE Id=?";
		
	    try (Connection con=DriverManager.getConnection(url, usuario, password)) {
	        try (PreparedStatement ps = con.prepareStatement(sql)) {
	        	ps.setString(1, LocalDate.now().toString());
	        	ps.setInt(2, id);
	            ps.executeUpdate();
	            System.out.println("Fecha de ultima copia actualizada del registro: "+id);
	        }
	    } catch (SQLException ex) {
	        System.err.println("Error al realizar la consulta.");
	        ex.printStackTrace();
	    }
	}
}
