import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DefinicionCopiaDAO {
	
	//Variables:
	
	private static String url = "jdbc:sqlite:.\\src\\main\\resources\\securalia.db?journal_mode=WAL";
	private static String usuario = "root";
	private static String password = "";
	
	//Constructor:
	
	public DefinicionCopiaDAO() {}
	
	//Métodos:
	
	public static boolean crear(DefinicionCopia dc) {
		try (Connection con = DriverManager.getConnection(url,usuario,password)){
			 
			System.out.println("1. Conexión establecida.");
			try {
				String sql = "INSERT INTO DefinicionesCopias (Id, Nombre, DirectorioOrigen, DirectorioDestino, IntervaloDias) VALUES (?,?,?,?,?)";
				System.out.println("2. Ejecutando sentencia SQL: "+sql);
				PreparedStatement ps = con.prepareStatement(sql);
				ps.setInt(1, dc.getId());
				ps.setString(2, dc.getNombre());
				ps.setString(3, dc.getDirectorioOrigen());
				ps.setString(4, dc.getDirectorioDestino());
				ps.setInt(5, dc.getIntervaloDias());
				System.out.println("Creando copia con ID: "+dc.getId());
				ps.executeUpdate();
				System.out.println("3. Sentencia ejecutada.");
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
}
