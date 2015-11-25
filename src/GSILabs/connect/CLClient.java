/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
 *
 * @author Alex
 */
public class CLClient {
    
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
            objectTag = "CLGateway";
        }
        
        try {
            // Paso 4 -  Conectarse al registro remoto
            Registry registry = LocateRegistry.getRegistry(remoteMachine);
            // Paso 5 - Unir el objeto remoto como si fuera un objeto local
            ClientGateway cGateway = (ClientGateway) registry.lookup(objectTag);
            // Paso 6 - Usar el objeto
            Client cli = cGateway.getClient(11111111);
            Event e = cGateway.getEvent("Concierto uno");            
            System.out.println("Mostramos el cliente con id 11111111:\n" + cli.toString());
            System.out.println("Obtengo mediante el método getEvent el evento con nombre: 'Concierto uno'\n" + e.toString());            
        } catch (RemoteException | NotBoundException ex) {
            System.out.println("Exception in connection : "+ex.getMessage());
        }
        
    }
    
}