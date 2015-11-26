package GSILabs.connect;

import GSILabs.BModel.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * Clase ejecutable capaz de conectarse a un servidor.
 * @author subiza.79082
 * @author izu.78236
 * @version 25/11/2015
 */
public class EVClient {
    
    public static void main(String[] args) {
        
        // Paso 1- Leer por teclado la dirección remota de la máquina        
        System.out.print("Por favor introduzca la dirección de la máquina: ");
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String remoteMachine;
        try {
            remoteMachine = br.readLine();
        } catch (IOException ioe) {
            System.out.println("Exception when reading : "+ioe.getMessage());
            remoteMachine="localhost";
        }
        // Paso 2 - Leer por teclado la puerto remota de la máquina  
        System.out.print("Por favor introduzca el puerto de la máquina: ");
        int remotePort;
        try {
            remotePort = Integer.parseInt(br.readLine());
        } catch (IOException ioe) {
            System.out.println("Exception when reading : "+ioe.getMessage());
            remotePort = 1099;
        }
        // Paso 3 - Leer por teclado la etiqueta del objeto remoto a contactar  
        System.out.print("Por favor introduzca la etiqueta del objeto remoto: ");
        String objectTag;
        try {
            objectTag = br.readLine();
        } catch (IOException ioe) {
            System.out.println("Exception when reading : "+ioe.getMessage());
            objectTag = "EVGateway";
        }
        
        try {
            // Paso 4 -  Conectarse al registro remoto
            Registry registry = LocateRegistry.getRegistry(remoteMachine, remotePort);
            // Paso 5 - Unir el objeto remoto como si fuera un objeto local
            EventGateway eGateway = (EventGateway) registry.lookup(objectTag);
            // Paso 6 - Usar el objeto
            Concert c1 = eGateway.getConcert("Concierto uno");
            System.out.println("Obtengo mediante el método getConcert el concierto con nombre: 'Concierto uno'\n" + c1);
            
            Festival f = eGateway.getFestival("Festival uno");
            System.out.println("Obtengo mediante el método getFestival el festival con nombre: 'Festival uno'\n" + f);
            
            System.out.println("Elimino mediante el método removeConcert el concierto 'Concierto uno'\n" + eGateway.removeConcert(c1));
            
            System.out.println("Añado un concierto a un festival en el que ya se encuentra:\n" + eGateway.addConcertToFestival("Festival uno", eGateway.getConcert("Concierto dos")));
                    
            System.out.println("Añado un concierto a un festival en el que no se encuentra:\n" + eGateway.addConcertToFestival("Festival uno", eGateway.getConcert("Concierto tres")));
            
            Concert c2 = new Concert("Concierto uno", c1.getPerformer(), new FechaCompleta("01/02/2020", "22:00"),
                        new FechaCompleta("01/02/2020", "22:00"), new FechaCompleta("01/02/2020", "21:00"),
                        new FechaCompleta("01/02/2020", "23:45"), c1.getLocation());
            System.out.println("Actualizo la fecha de 'Concierto uno' de 2016 a 2020\n" + eGateway.updateEvent(c2));
            
            System.out.println("Elimino mediante el método removeEvent el concierto 'Concierto tres'\n" + eGateway.removeEvent(eGateway.getConcert("Concierto tres")));
            
        } catch (RemoteException | NotBoundException ex) {
            System.out.println("Exception in connection : " + ex.getMessage());
        }
        
    }
    
}
