package com.spontecorp.ferreasesor.webservice;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.spontecorp.ferreasesor.entity.Encuesta;
import com.spontecorp.ferreasesor.entity.Ubicacion;
import java.util.Date;
import java.util.List;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.PathParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.enterprise.context.RequestScoped;
import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.core.Response;

@Path("llamadaService")
@RequestScoped
public class LlamadaService {

    @Context
    private UriInfo context;
    
    public LlamadaService() {
    }/**
     * Método para guardar una ubicación en la BD de la tienda
     * @param ubicacion
     * @return 
     */

    @POST
    @Path("guardarubicaciones")
    @Consumes("application/json")
    public Response guardarUbicacion(Ubicacion ubicacion) {
        UbicacionServiceManager ubicacionServiceManager = new UbicacionServiceManager();
        Response resp = ubicacionServiceManager.guardarUbicaciones(ubicacion);
        return resp;
    }
/**
 * Metodo para editar una ubicación en las tiendas.
 * @param ubicacion
 * @return 
 */
    @PUT
    @Path("editarubicacion")
    @Consumes("application/json")
    public Response editarUbicacion(Ubicacion ubicacion) {
        UbicacionServiceManager ubicacionServiceManager = new UbicacionServiceManager();
        Response resp = ubicacionServiceManager.editarUbicacion(ubicacion);
        return resp;
    }
/**
 * Método que permite borrar una encuesta en las tiendas dado su id_global
 * @param globalId 
 */
    @DELETE
    @Path("eliminarencuesta/{globalId}")
    @Consumes("application/json")
    public void eliminarEncuestaGlobal(@PathParam(value = "globalId") int globalId) {
        EncuestaServiceManager encuestaServiceManager = new EncuestaServiceManager();
        Response rsp = encuestaServiceManager.eliminarEncuestaGlobal(globalId);
        
    }

    /**
     * Método que permite colocar una nueva encuesta en las tiendas, retorna un
     * response con un mensaje dependiendo si se pudo crear la encuesta o no.
     */
    @POST
    @Path("enviarencuesta")
    @Consumes("application/json")
    public Response crearEncuestaGlobal(Encuesta encuesta) {
        EncuestaServiceManager encuestaServiceManager = new EncuestaServiceManager();
        Response rsp = encuestaServiceManager.crearEncuestaGlobal(encuesta);
        return rsp;
    }

    /**
     * Método que retorna el resultados de una encuesta dado el IdGlobal
     *
     * @param globalId
     * @return
     */
    @GET
    @Path("obtenerresultadosencuesta/{globalId}")
    @Produces("application/json")
    public String obtenerResultadosEncuesta(@PathParam(value = "globalId") int globalId) {

        EncuestaServiceManager encuestaServiceManager = new EncuestaServiceManager();
        String json = encuestaServiceManager.enviarResultadoEncuesta(globalId);
        return json;
    }

    @GET
    @Path("obtenerresultadosencuesta2/{globalId}")
    @Produces("application/json")
    public String obtenerResultadosEncuesta2(@PathParam(value = "globalId") int globalId) {

        EncuestaServiceManager encuestaServiceManager = new EncuestaServiceManager();
        String json = encuestaServiceManager.enviarResultadoEncuesta2(globalId);
        return json;
    }
    
    @GET
    @Path("obtenerresultadosencuesta3/{globalId}")
    @Produces("application/json")
    public String getResultadosEncuesta3(@PathParam(value = "globalId") int globalId) {


        EncuestaServiceManager encuestaServiceManager = new EncuestaServiceManager();
        String json = encuestaServiceManager.enviarResultadoEncuesta3(globalId);
        return json;
    }

    /**
     * Método para obtener las llamadas de una tienda por un rango de
     * fechas
     *
     * @param fechaI Fecha inicial del rango
     * @param fechaF Fecha final del rango
     * @param tiendaId id de la tienda
     * @param content "application/json" obligatoriamente
     * @return Arreglo Json con las llamadas
     */    
    @GET
    @Path("tiendaporfecha/{fechaInicio}/{fechaFin}/{tiendaId}")
    @Produces("application/json")
    public String obtenerLlamadaPorFecha(
            @PathParam(value = "fechaInicio") String fechaI,
            @PathParam(value = "fechaFin") String fechaF,
            @PathParam(value = "tiendaId") int tiendaId) {

        LlamadaServiceManager llamadaServerManager = new LlamadaServiceManager();
        //Se transforman los string de las fechas a fechas
        Date fechaInicio = llamadaServerManager.convertirFecha(fechaI);
        Date fechaFin = llamadaServerManager.convertirFecha(fechaF);
        //Se construye el gson de forma tal que ignore los campos que NO tengan la anotacion @Expose
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().setPrettyPrinting().create();
        //llenamos la lista con el método de la clase llamadaServerManager
        List<LlamadaServer> listaLlamadaServer = llamadaServerManager.llenarListaLlamadasServer(fechaInicio, fechaFin);
        //Se convierte la lista a tipo json
        String arrLlamaJson = gson.toJson(listaLlamadaServer, new TypeToken<List<LlamadaServer>>() {
        }.getType());
        //se retorna el arreglo de objetos json
        return arrLlamaJson;

    }
    
    /**
     * Método para obtener las llamadas de una Tienda 
     *
     * @param tiendaId id de la tienda
     * @param content "application/json" obligatoriamente
     * @return Arreglo Json con las llamadas
     */    
    @GET
    @Path("totaltienda/{tiendaId}")
    @Produces("application/json")
    public String obtenerLlamadasTienda(@PathParam(value = "tiendaId") int tiendaId) {

        LlamadaServiceManager llamadaServerManager = new LlamadaServiceManager();
        //Se construye el gson de forma tal que ignore los campos que NO tengan la anotacion @Expose
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().setPrettyPrinting().create();
        //llenamos la lista con el método de la clase llamadaServerManager
        List<LlamadaServer> listaLlamadaServer = llamadaServerManager.llenarListaLlamadasServer();
        //Se convierte la lista a tipo json
        String arrLlamaJson = gson.toJson(listaLlamadaServer, new TypeToken<List<LlamadaServer>>() {
        }.getType());
        //se retorna el arreglo de objetos json
        return arrLlamaJson;
    }
    
    /**
     * Método para obtener listado de Llamadas cuya fecha de Inicio sea igual o mayor
     * y su Hora de Inicio sea mayor a los parámetros recibidos
     * @param fechaI
     * @param horaI
     * @param tiendaId
     * @return 
     */
    @GET
    @Path("updatellamadasporfecha/{fechaInicio}/{horaInicio}/{tiendaId}")
    @Produces("application/json")
    public String updateLlamadaPorFecha(
            @PathParam(value = "fechaInicio") String fechaI,
            @PathParam(value = "horaInicio") String horaI,
            @PathParam(value = "tiendaId") int tiendaId) {

        LlamadaServiceManager llamadaServerManager = new LlamadaServiceManager();
        //Se transforman los string de la fecha a fecha
        Date fechaInicio = llamadaServerManager.convertirFecha(fechaI);
        //Se transforman los string de la hora a hora
        Date horaInicio = llamadaServerManager.convertirHora(horaI);
        //Se construye el gson de forma tal que ignore los campos que NO tengan la anotacion @Expose
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().setPrettyPrinting().create();
        //llenamos la lista con el método de la clase llamadaServerManager
        List<LlamadaServer> listaLlamadaServer = llamadaServerManager.llenarListaUpdateLlamadasServer(fechaInicio, horaInicio);
        //Se convierte la lista a tipo json
        String arrLlamaJson = gson.toJson(listaLlamadaServer, new TypeToken<List<LlamadaServer>>() {
        }.getType());
        //se retorna el arreglo de objetos json
        return arrLlamaJson;

    }
}
