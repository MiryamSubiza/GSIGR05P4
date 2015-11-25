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
            Registry registry = LocateRegistry.getRegistry(remoteMachine);
            // Paso 5 - Unir el objeto remoto como si fuera un objeto local
            EventGateway eGateway = (EventGateway) registry.lookup(objectTag);
            // Paso 6 - Usar el objeto
            System.out.println("Miro el concierto uno");
            System.out.println(eGateway.getConcert("Concierto uno").toString());
            System.out.println("Miro el festival uno");
            System.out.println(eGateway.getFestival("Festival uno").toString());
        } catch (RemoteException | NotBoundException ex) {
            System.out.println("Exception in connection : "+ex.getMessage());
        }
        
    }
    
}
