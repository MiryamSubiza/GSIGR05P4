/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GSILabs.BSystem;

import GSILabs.BModel.Artist;
import GSILabs.BModel.Client;
import GSILabs.BModel.Collective;
import GSILabs.BModel.Concert;
import GSILabs.BModel.Event;
import GSILabs.BModel.Exhibition;
import GSILabs.BModel.FechaCompleta;
import GSILabs.BModel.Festival;
import GSILabs.BModel.Location;
import GSILabs.BModel.Sales;
import GSILabs.BModel.Ticket;
import GSILabs.connect.*;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 * @author Alex
 */
public class PublicBussinessSystem extends BussinessSystem implements EventGateway, ClientGateway{        
    
    public PublicBussinessSystem(){
        super();
    }
    
    @Override
    public Client getClient(Integer id) throws RemoteException {
        
        return super.retrieveClient(id);
        
    }

    @Override
    public Sales addFreeSale(Client c, Ticket t) throws RemoteException {
        
        Calendar actualDate = Calendar.getInstance();
        actualDate.setTime(new Date());
        if (tickets.containsValue(t) && clients.containsValue(c) && !t.isSold()) {
            
            // Si el cliente y el ticket existen y además el ticket no está vendido
            // creamos la Sale
            t.setSold(true);
            Sales s = new Sales(t, c, 0, c.getFirstCreditCard(), new FechaCompleta(actualDate.get(Calendar.DAY_OF_MONTH), 
                actualDate.get(Calendar.MONTH)+1, actualDate.get(Calendar.YEAR), 
                actualDate.get(Calendar.HOUR), actualDate.get(Calendar.MINUTE)));
            sales.add(s);
            return s;
            
        }
        else
            // Si el cliente o el ticket no existen en el sistema devolvemos
            // nulo
            return null;
        
    }

    @Override
    public Boolean removeSale(Sales s) throws RemoteException {
        
        // Como no se nos indica nada, voy a considerar que si se anula una Sale
        // el ticket vuelve a esta de nuevo a la venta de modo que no elimino el
        // ticket asociado.
        
        // Pongo el ticket asociado de nuevo a la venta
        Ticket t = s.getTicket();
        t.setSold(true);
        // Si se elimina correctamente devolvera true en caso
        // contrario devolvera false
        return sales.remove(s);
        
    }

    @Override
    public Event[] getEvents(String name) throws RemoteException {
        
        //Creamos un ArrayList porque un array no es dinámico
        ArrayList <Event> al = new ArrayList();
        Iterator i = concerts.values().iterator();
        Iterator j = festivals.values().iterator();
        Iterator z = exhibitions.values().iterator();

        // Recorro todos los eventos mirando uno a uno si los nombres de los mismos
        // tiene parcial o totalmente el nombre que me pasan como argumento y los guardo
        // en el array de eventos
        while (i.hasNext()) {
            Concert concertAux = (Concert)i.next();
            if (concertAux.getName().contains(name)) al.add(concertAux);
        }
        while (j.hasNext()){
            Festival festivalAux = (Festival)j.next();
            if (festivalAux.getName().contains(name)) al.add(festivalAux);
            
        }
        while (z.hasNext()) {
            Exhibition exhibitionAux = (Exhibition)z.next();
            if (exhibitionAux.getName().contains(name)) al.add(exhibitionAux);
        }
        return (Event[]) al.toArray(new Event[al.size()]);
        
    }

    @Override
    public Concert getConcert(String name) throws RemoteException {
        
        if(concerts.containsKey(name)){
            // Si el concierto que buscamos existe lo devolvemos            
            return concerts.get(name);
        }
        else{
            // Si no existe devolvemos null
            return null;
        }
        
    }

    @Override
    public Festival getFestival(String name) throws RemoteException {
        
        if(festivals.containsKey(name)){
            // Si el festival que buscamos existe lo devolvemos            
            return festivals.get(name);
        }
        else{
            // Si no existe devolvemos null
            return null;
        }
        
    }

    @Override
    public Boolean removeConcert(Concert c) throws RemoteException {
        
        // Uso el metodo ya implementado en bussinessSystem para eliminar el
        // concierto
        return deleteConcert(c);
        
    }

    @Override
    public Boolean removeFestival(Festival f) throws RemoteException {
        
        // Uso el metodo ya implementado en bussinessSystem para eliminar el
        // festival
        return deleteFestival(f);
        
    }

    @Override
    public Festival addConcertToFestival(String festivalName, Concert c) throws RemoteException {
                
        if(festivals.containsKey(festivalName)){
            // Si el festival existe intento añadir el concierto y devuelvo
            // el festival actualizado
            this.addConcertToFestival(festivals.get(festivalName), c);
            return festivals.get(festivalName);
        } 
        else {
            // En caso contrario devuelvo nulo
            return null;
        }
        
    }

    @Override
    public Boolean updateEvent(Event ev) throws RemoteException {
        
        
        
    }

    @Override
    public Boolean removeEvent(Event ev) throws RemoteException {
        
        if(ev instanceof Concert){
            // Lo elimino con el método de eliminar conciertos
            // de la propia clase
            return this.deleteConcert((Concert)ev);
        }
        else if(ev instanceof Festival){
            // Lo elimino con el método de eliminar festivales
            // de la propia clase
            return this.deleteFestival((Festival)ev);
        }
        else if(ev instanceof Exhibition){
            // Lo elimino con el método de eliminar exhibiciones
            // de la propia clase
            return this.deleteExhibition((Exhibition)ev);
        }
        else{
            return false;
        }
        
    }
}

