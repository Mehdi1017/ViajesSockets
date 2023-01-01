package servidor;

import java.io.IOException;
import java.net.SocketException;

import gestor.GestorViajes;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.util.Iterator;
import java.util.regex.*;

import comun.MyStreamSocket;

/**
 * Clase ejecutada por cada hebra encargada de servir a un cliente del servicio de viajes.
 * El metodo run contiene la logica para gestionar una sesion con un cliente.
 */

class HiloServidorViajes implements Runnable {


	private MyStreamSocket myDataSocket;
	private GestorViajes gestor;

	/**
	 * Construye el objeto a ejecutar por la hebra para servir a un cliente
	 * @param	myDataSocket	socket stream para comunicarse con el cliente
	 * @param	unGestor		gestor de viajes
	 */
	HiloServidorViajes(MyStreamSocket myDataSocket, GestorViajes unGestor) {
		this.myDataSocket = myDataSocket;
		this.gestor = unGestor;
	}

	/**
	 * Gestiona una sesion con un cliente	
	 */
	public void run( ) {
		String operacion = "0";
		boolean done = false;
	    // ...
		try {
			while (!done) {
				// Recibe una petición del cliente
				String mensaje = myDataSocket.receiveMessage();
				// Extrae la operación y sus parámetros
				JSONParser parser = new JSONParser();
				JSONObject peticion= (JSONObject) parser.parse(mensaje);
				operacion = peticion.get("operacion").toString();
				switch (operacion) {
					case "0" -> {
						gestor.guardaDatos();
						myDataSocket.close();
						done = true;
					}
					case "1" -> { // Consulta los viajes con un origen dado
						String origen = (String) peticion.get("origen");
						JSONArray consulta = gestor.consultaViajes(origen);
						myDataSocket.sendMessage(consulta.toJSONString());
						break;
					}
					case "2" -> { // Reserva una plaza en un viaje
						String codviaje = (String) peticion.get("codviaje");
						String codcli = (String) peticion.get("codcli");
						JSONObject reserva = gestor.reservaViaje(codviaje, codcli);
						myDataSocket.sendMessage(reserva.toJSONString());
						break;
					}
					case "3" -> { // Anular una reserva
						String codviaje = (String) peticion.get("codviaje");
						String codcli = (String) peticion.get("codcli");
						JSONObject reserva = gestor.anulaReserva(codviaje, codcli);
						myDataSocket.sendMessage(reserva.toJSONString());
						break;
					}
					case "4" -> { // Oferta un viaje
						String codviaje = (String) peticion.get("codviaje");
						String codcli = (String) peticion.get("codcli");
						String origen = (String) peticion.get("origen");
						String destino = (String) peticion.get("destino");
						String fecha = (String) peticion.get("fecha");
						long precio = (Long) peticion.get("precio");
						long numplazas = (Long) peticion.get("numplazas");
						JSONObject viaje = gestor.ofertaViaje(codcli, origen, destino, fecha, precio, numplazas);
						myDataSocket.sendMessage(viaje.toJSONString());
						break;
					}
					case "5" -> { // Borra un viaje
						String codviaje = (String) peticion.get("codviaje");
						String codcli = (String) peticion.get("codcli");
						JSONObject borrado = gestor.borraViaje(codviaje, codcli);
						myDataSocket.sendMessage(borrado.toJSONString());
						break;
					}
				} // fin switch
			} // fin while   
		} // fin try
		catch (SocketException ex) {
			System.out.println("Capturada SocketException");
		}
		catch (IOException ex) {
			System.out.println("Capturada IOException");
		}
		catch (Exception ex) {
			System.out.println("Exception caught in thread: " + ex);
		} // fin catch
	} //fin run

} //fin class 
