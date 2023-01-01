package cliente;

import java.io.IOException;
import java.util.Iterator;
import java.util.Scanner;

import gestor.GestorViajes;
import gestor.Viaje;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;


public class ClienteViajes {


	// Sustituye esta clase por tu versión de la clase SubastasLocal de la práctica 1

	// Modifícala para que instancie un objeto de la clase AuxiliarClienteViajes

	// Modifica todas las llamadas al objeto de la clase GestorViajes
	// por llamadas al objeto de la clase AuxiliarClienteViajes.
	// Los métodos a llamar tendrán la misma signatura.
    public static int menu(Scanner teclado) {
        int opcion;
        System.out.println("\n\n");
        System.out.println("=====================================================");
        System.out.println("============            MENU        =================");
        System.out.println("=====================================================");
        System.out.println("0. Salir");
        System.out.println("1. Consultar viajes con un origen dado");
        System.out.println("2. Reservar un viaje");
        System.out.println("3. Anular una reserva");
        System.out.println("4. Ofertar un viaje");
        System.out.println("5. Borrar un viaje");
        do {
            System.out.print("\nElige una opcion (0..5): ");
            opcion = teclado.nextInt();
        } while ( (opcion<0) || (opcion>5) );
        teclado.nextLine(); // Elimina retorno de carro del buffer de entrada
        return opcion;
    }


    /**
     * Programa principal. Muestra el menú repetidamente y atiende las peticiones del cliente.
     *
     * @param args	no se usan argumentos de entrada al programa principal
     */
    public static void main(String[] args)  {
        try {
            AuxiliarClienteViajes auxiliar = new AuxiliarClienteViajes("localhost", "12345");
            Scanner teclado = new Scanner(System.in);


            System.out.print("Introduce tu codigo de cliente: ");
            String codcli = teclado.nextLine();

            int opcion;
            do {
                opcion = menu(teclado);
                switch (opcion) {
                    case 0: // Guardar los datos en el fichero y salir del programa
                        auxiliar.cierraSesion();
                        opcion = 0;
                        break;

                    case 1: { // Consultar viajes con un origen dado
                        System.out.print("Introduce origen: ");
                        String origen = teclado.nextLine();
                        JSONArray viajes = auxiliar.consultaViajes(origen);
                        if (viajes.isEmpty()) {
                            System.out.print("No se han encontrado viajes de este origen: ");
                            break;
                        }
                        Iterator<JSONObject> iterator = viajes.iterator();
                        while (iterator.hasNext()) {
                            System.out.println(iterator.next().toJSONString());
                        }
                        break;
                    }

                    case 2: { // Reservar un viaje
                        System.out.print("Introduce codigo de viaje: ");
                        String codviaje = teclado.nextLine();
                        JSONObject reserva = auxiliar.reservaViaje(codviaje, codcli);
                        if (reserva.isEmpty()) {
                            System.out.print("No se ha podido hacer la reserva");
                            break;
                        }
                        System.out.print("Viaje reservado con exito");
                        break;
                    }

                    case 3: { // Anular una reserva

                        System.out.println("Por favor estimado cliente...\n");
                        System.out.println("Asegurese de haber consultado correctamente el viaje que desea anular antes de introducir el código de viaje...\n");

                        System.out.print("Introduzca código de viaje: ");
                        String codviaje = teclado.nextLine();
                        JSONObject reserva = auxiliar.anulaReserva(codviaje, codcli);
                        if (reserva.isEmpty()) {
                            System.out.print("No se ha podido anular la reserva");
                            break;
                        }
                        System.out.print("Reserva anulada con exito");
                        break;
                    }

                    case 4: { // Ofertar un viaje
                        System.out.print("Introduzca origen: ");
                        String origen = teclado.next();
                        System.out.print("Introduzca destino: ");
                        String destino = teclado.next();
                        System.out.print("Introduzca fecha del viaje: ");
                        String fecha = teclado.next();
                        System.out.print("Introduzca precio del viaje: ");
                        long precio = teclado.nextLong();
                        System.out.print("Introduzca numero de plazas: ");
                        long numplazas = teclado.nextLong();
                        JSONObject viaje = auxiliar.ofertaViaje(codcli, origen, destino, fecha, precio, numplazas);
                        if (viaje.isEmpty()){
                            System.out.print("La fecha del viaje no es valida");
                            break;
                        }
                        System.out.print("Estos son los datos del viaje ofertado:" + viaje);
                        break;
                    }

                    case 5: { // Borrar un viaje ofertado

                        System.out.print("Introduzca el codigo del viaje ofertado a borrar: ");
                        String codviaje = teclado.next();
                        JSONObject borrado = auxiliar.borraViaje(codviaje, codcli);
                        if (borrado.isEmpty()) {
                            System.out.print("No se ha podido borrar el viaje");
                            break;
                        }
                        System.out.print("Viaje borrado con exito");
                        break;
                    }

                } // fin switch

            } while (opcion != 0);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }

    }
} // fin class
