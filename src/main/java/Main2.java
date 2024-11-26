import java.time.LocalDate;

public class Main2 {
	/*
	 * Hay que hacer que cumpruebe primero si hay una base de datos 
	 * disponible (securalia.db) en el propio directorio donde se encuentra
	 * el .jar, junto con SecuraliaP1.jar. Vale, esto ya lo hace la propia instancia de DAO.
	 * 
	 * Métodos que hacer:
	 * 
	 *  --- copiar(Path dirOrigen, Path dirDestino) (este método se ejecuta si se detecta que ha pasado el intervalo de días entre el actual
	 * y la fecha establecida como fechaUltimaCopia)
	 * 
	 *  --- actualizarFechaUltimaCopia(int id) (este metodo se usará en copiar() para establecer ese campo en el dia actual)
	 *  
	 *  --- leerFechas() -> boolean (Calcula la diferencia de dias de las fechas de los registros en la DB con el dia
	 *  actual, si ha pasado el intervalo de dias del respectivo registro, ejecuta copiar().
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//leerFechas();
		DefinicionCopiaDAO.actualizarFechaUltimaCopia(4);
	}

}
