package com.spontecorp.ferreasesor.webservice;

import com.spontecorp.ferreasesor.entity.Ubicacion;
import com.spontecorp.ferreasesor.jpa.UbicacionFacade;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.ws.rs.core.Response;

public class UbicacionServiceManager {

    private Response response;

    public UbicacionServiceManager() {
    }

    public Response guardarUbicaciones(Ubicacion ubc) {
        try {
            InitialContext cont = new InitialContext();
            UbicacionFacade ubicacionFacade = (UbicacionFacade) cont.lookup("java:module/UbicacionFacade");
            ubicacionFacade.create(ubc);
            response = Response.status(200).entity("Las ubicaciones fueron creadas con éxito").build();

        } catch (NamingException ex) {
            Logger.getLogger(UbicacionServiceManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return response;
    }

    public Response editarUbicacion(Ubicacion ubc) {
        try {
            InitialContext cont = new InitialContext();
            UbicacionFacade ubicacionFacade = (UbicacionFacade) cont.lookup("java:module/UbicacionFacade");
            ubicacionFacade.edit(ubc);
            response = Response.status(200).entity("La ubicación fue editada con éxito").build();

        } catch (NamingException ex) {
            Logger.getLogger(UbicacionServiceManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return response;
    }
}
