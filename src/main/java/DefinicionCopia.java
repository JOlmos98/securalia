import java.time.LocalDate;

public class DefinicionCopia {
	
	//Variables:
	
	private static int contadortId=0;
	private final int id;
	private String nombre;
	private String directorioOrigen;
	private String directorioDestino;
	private int IntervaloDias;
	private LocalDate fechaUltimaCopia;
	
	//Constructor:
	
	public DefinicionCopia(String nombre, String directorioOrigen, String directorioDestino, int IntervaloDias) {
	    this.id=++contadortId;
	    this.nombre = nombre; 
	    this.directorioOrigen = directorioOrigen;
	    this.directorioDestino = directorioDestino;
	    this.IntervaloDias = IntervaloDias;
	    this.fechaUltimaCopia = LocalDate.now();
	}

	//Métodos:
	
	@Override
	public String toString() {
		return "Definicion Copia con ID: "+id+"\nNombre: "+nombre+"\nDir. Origen: "+directorioOrigen
				+ "\nDir. Destino: "+directorioDestino+"\nIntervalo de días: "+IntervaloDias
				+"\nFecha de última copia: "+fechaUltimaCopia;
	}
	
	public static int getContadortId() {return contadortId;}
	public static void setContadortId(int contadortId) {DefinicionCopia.contadortId = contadortId;}
	public int getId() {return id;}
	public String getNombre() {return nombre;}
	public void setNombre(String nombre) {this.nombre = nombre;}
	public String getDirectorioOrigen() {return directorioOrigen;}
	public void setDirectorioOrigen(String directorioOrigen) {this.directorioOrigen = directorioOrigen;}
	public String getDirectorioDestino() {return directorioDestino;}
	public void setDirectorioDestino(String directorioDestino) {this.directorioDestino = directorioDestino;}
	public int getIntervaloDias() {return IntervaloDias;}
	public void setIntervaloDias(int intervaloDias) {IntervaloDias = intervaloDias;}
	public LocalDate getFechaUltimaCopia() {return fechaUltimaCopia;}
	public void setFechaUltimaCopia(LocalDate fechaUltimaCopia) {this.fechaUltimaCopia = fechaUltimaCopia;}
	
}
