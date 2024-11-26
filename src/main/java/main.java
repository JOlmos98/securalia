import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

public class main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try (Scanner sc=new Scanner(System.in)){
		System.out.println("========== Securalia iniciado ==========");
		
		menuPrincipal(sc);
		}
	}

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public static void menuPrincipal(Scanner sc) {
		System.out.println("\n__________ MENU PRINCIPAL __________");
		System.out.println("\nEscriba uno de los siguientes comandos:\n"
				+ "\ncrear 		- Crear una nueva definición de copia."
				+ "\nlistar id 	- Lista una definición de copia según su ID."
				+ "\nlistar todas 	- Listar todas las definiciones existentes."
				+ "\neliminar 	- Eliminar una definición de copia según su ID."
				+ "\nsalir 		- Salir del programa.");
		String input;
		input=sc.nextLine();
		while (!input.equalsIgnoreCase("crear")&&!input.equalsIgnoreCase("listar id")&&
				!input.equalsIgnoreCase("listar todas")&&!input.equalsIgnoreCase("eliminar")
				&&!input.equalsIgnoreCase("salir")&&!input.equalsIgnoreCase("exit")) {
			System.err.println("\nComando NO reconocido.");
			System.out.println("Escriba uno de los siguientes comandos:\n"
					+ "\ncrear 		- Crear una nueva definición de copia."
					+ "\nlistar id 	- Lista una definición de copia según su ID."
					+ "\nlistar todas 	- Listar todas las definiciones existentes."
					+ "\neliminar 	- Eliminar una definición de copia según su ID."
					+ "\nsalir 		- Salir del programa.");
			input=sc.nextLine();
		}
		if (input.equalsIgnoreCase("crear")) crear(sc);
		else if (input.equalsIgnoreCase("listar id")) listarId(sc);
		else if (input.equalsIgnoreCase("listar todas")) listarTodas(sc);
		else if (input.equalsIgnoreCase("eliminar")) eliminar(sc);
		else if (input.equalsIgnoreCase("salir")||input.equalsIgnoreCase("exit")) salir();
	}
	
	public static void crear(Scanner sc) {
		
		String nombre;
		String directorioOrigen;
		String directorioDestino;
		int intervaloDias;
		
		System.out.println("\n__________ Crear __________");
		System.out.print("\nInserta nombre: ");
		nombre=sc.nextLine();
		
		System.out.println("\nInserta el directorio de Origen: ");
		directorioOrigen=sc.nextLine();
		Path path1 = Paths.get(directorioOrigen);
		while(!Files.exists(path1)||!Files.isDirectory(path1)){
			System.err.print("\nDirectorio de origen NO válido.");
			System.out.println("\nInserta el directorio de origen: ");
			directorioOrigen=sc.nextLine();
			path1 = Paths.get(directorioOrigen);
		}
		
		System.out.println("\nInserta el directorio de destino: ");
		directorioDestino=sc.nextLine();
		Path path2 = Paths.get(directorioDestino);
		while(!Files.exists(path2)||!Files.isDirectory(path2)){
			System.err.print("\nDirectorio de destino NO válido.");
			System.out.println("\nInserta el directorio de destino: ");
			directorioDestino=sc.nextLine();
			path2 = Paths.get(directorioDestino);
		}
		
		boolean numero1a365=false;
		String input;
		do {
			System.out.println("\nInserta intervalo de días entre copias: ");
			input=sc.nextLine();
			for (int i=0;i<366;i++) if(input.equals(String.valueOf(i))) numero1a365=true;
			if (numero1a365==false) System.err.println("Inserta un valor entre 1 y 365.");
		}while(numero1a365==false);
		intervaloDias=Integer.parseInt(input);
		DefinicionCopia.setContadortId(DefinicionCopiaDAO.getIdMax()); //Esto hace que el programa, cada vez que se inicie, no empiece el id desde 0, ya que no dejaría meter registros si ya hay.
		DefinicionCopia dc=new DefinicionCopia(nombre, directorioOrigen, directorioDestino, intervaloDias);
		try {
			DefinicionCopiaDAO.crear(dc);
			menuPrincipal(sc);
		} catch (Exception e) {
			System.out.println("Error al crear definición de copia.");
			e.printStackTrace();
			menuPrincipal(sc);
		}
	}
	
	public static void listarId(Scanner sc) {
		
		String id;
		int idNum;
		
		System.out.println("\n__________ Listar por Id __________\nmenu - para volver al menu principal.\nsalir - para salir del programa.");
		System.out.print("\nInserta un Id: ");
		id=sc.nextLine();
		boolean isNumber=false;
		for (int i=0;i<999;i++) if(id.equals(String.valueOf(i))) isNumber=true; //Valida si se ha introducido un número.
		while (!id.equalsIgnoreCase("menu")&&!id.equalsIgnoreCase("salir")&&!id.equalsIgnoreCase("exit")&&!isNumber) {
			System.err.println("Inserta un id.");
			id=sc.nextLine();
			for (int i=0;i<999;i++) if(id.equals(String.valueOf(i))) isNumber=true; //Valida si se ha introducido un número.
		}
		
		if (id.equalsIgnoreCase("menu")) {
			menuPrincipal(sc);
		} else if (id.equalsIgnoreCase("salir")||id.equalsIgnoreCase("exit")) {
			salir();
		} else if (isNumber) {
			idNum=Integer.parseInt(id);
			DefinicionCopiaDAO.listar(idNum);
			menuPrincipal(sc);
		} //else System.err.println("Inserta un id.");
		
	}
	
	public static void listarTodas(Scanner sc) {
		DefinicionCopiaDAO.listarTodas();
		menuPrincipal(sc);
	}
	
	public static void eliminar(Scanner sc) {
		
		String id;
		int idNum;
		
		System.out.println("\n__________ Eliminar __________\nmenu - para volver al menu principal.\nsalir - para salir del programa.");
		System.out.print("\nInserta un Id: ");
		id=sc.nextLine();
		boolean isNumber=false;
		for (int i=0;i<999;i++) if(id.equals(String.valueOf(i))) isNumber=true; //Valida si se ha introducido un número.
		while (!id.equalsIgnoreCase("menu")&&!id.equalsIgnoreCase("salir")&&!id.equalsIgnoreCase("exit")&&!isNumber) {
			System.err.println("Inserta un id.");
			id=sc.nextLine();
			for (int i=0;i<999;i++) if(id.equals(String.valueOf(i))) isNumber=true; //Valida si se ha introducido un número.
		}
		
		if (id.equalsIgnoreCase("menu")) {
			menuPrincipal(sc);
		} else if (id.equalsIgnoreCase("salir")||id.equalsIgnoreCase("exit")) {
			salir();
		} else if (isNumber) {
			idNum=Integer.parseInt(id);
			DefinicionCopiaDAO.eliminar(idNum);
			menuPrincipal(sc);
		} //else System.err.println("Inserta un id.");
		
	}
	
	public static void salir() {
		System.out.println("Programa finalizado.");
		System.exit(0);
	}
	
}


/*String url = "jdbc:sqlite:.\\src\\main\\resources\\securalia.db?journal_mode=WAL";
String usuario = "root";
String password = "";

DefinicionCopia dc1=new DefinicionCopia("copiaPrueba111", "C:\\Users\\soler\\OneDrive\\Desktop\\App1", "C:\\Users\\soler\\OneDrive\\Desktop\\copiaSeguridad1", 57);
DefinicionCopia dc2=new DefinicionCopia("copiaPrueba222", "C:\\Users\\soler\\OneDrive\\Desktop\\App1", "C:\\Users\\soler\\OneDrive\\Desktop\\copiaSeguridad1", 23);
DefinicionCopia dc3=new DefinicionCopia("copiaPrueba333", "C:\\Users\\soler\\OneDrive\\Desktop\\App1", "C:\\Users\\soler\\OneDrive\\Desktop\\copiaSeguridad1", 234);


try (Connection con = DriverManager.getConnection(url,usuario,password)){
	 
	System.out.println("1. Conexión establecida.");
	try {
		String sql = "INSERT INTO DefinicionesCopias (Id, Nombre, DirectorioOrigen, DirectorioDestino, IntervaloDias) VALUES (?,?,?,?,?)";
		System.out.println("2. Ejecutando sentencia SQL: "+sql);
		PreparedStatement ps = con.prepareStatement(sql);
		ps.setInt(1, dc1.getId());
		ps.setString(2, dc1.getNombre());
		ps.setString(3, dc1.getDirectorioOrigen());
		ps.setString(4, dc1.getDirectorioDestino());
		ps.setInt(5, dc1.getIntervaloDias());
		System.out.println("Creando copia con ID: "+dc1.getId());
		ps.executeUpdate();
		System.out.println("3. Sentencia ejecutada.");
	} 
	
	finally {con.close();}
} 
catch (SQLException ex) 
{
	System.err.println("Error en el proceso de alta");
	ex.printStackTrace();
} System.out.println("Copia creada.");*/
