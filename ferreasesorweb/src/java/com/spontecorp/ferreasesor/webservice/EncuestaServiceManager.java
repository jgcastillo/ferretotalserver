
package com.spontecorp.ferreasesor.webservice;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.spontecorp.ferreasesor.entity.Encuesta;
import com.spontecorp.ferreasesor.entity.Pregunta;
import com.spontecorp.ferreasesor.entity.RespuestaConf;
import com.spontecorp.ferreasesor.entity.RespuestaObtenida;
import com.spontecorp.ferreasesor.entity.Tienda;
import com.spontecorp.ferreasesor.jpa.EncuestaAuxFacade;
import com.spontecorp.ferreasesor.jpa.EncuestaFacade;
import com.spontecorp.ferreasesor.jpa.RespuestaObtenidaFacade;
import com.spontecorp.ferreasesor.jpa.TiendaFacade;
import com.spontecorp.ferreasesor.utilities.WebServicesUtilities;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.ws.rs.core.Response;


public class EncuestaServiceManager implements Serializable {

    private Response rsp;
    private ResultadoEncuesta restultadoEncuesta;
    private Encuesta encuesta;
    private Tienda tienda;


    public Response eliminarEncuestaGlobal(int idGlobal) {
        try {
            InitialContext cont = new InitialContext();
            EncuestaAuxFacade encuestaFacadeAux = (EncuestaAuxFacade) cont.lookup("java:module/EncuestaAuxFacade");
            EncuestaFacade encuestaFacade = (EncuestaFacade) cont.lookup("java:module/EncuestaFacade");
            Encuesta encuesta = (Encuesta) encuestaFacadeAux.findEncuestasByIdGlobal(idGlobal);
            Gson gson = new GsonBuilder().setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().create();
            encuestaFacade.remove(encuesta);
            rsp = Response.status(200).entity("La encuesta fue eliminada " + encuesta.getTiendaId().getNombre()).build();

        } catch (NamingException ex) {
            Logger.getLogger(EncuestaServiceManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return rsp;
    }

    /**
     * Método para crear una encuesta global recibida en un "POST request" Se
     * guarda la misma con estatus inactivo en la base de datos
     *
     * @param enc
     * @return rsp
     */
    public Response crearEncuestaGlobal(Encuesta enc) {

        try {
            InitialContext cont = new InitialContext();
            EncuestaFacade encuestaFacade = (EncuestaFacade) cont.lookup("java:module/EncuestaFacade");
            TiendaFacade tiendafacade = (TiendaFacade) cont.lookup("java:module/TiendaFacade");
            EncuestaAuxFacade encuestaFacadeAux = (EncuestaAuxFacade) cont.lookup("java:module/EncuestaAuxFacade");
            Encuesta encuesta = (Encuesta) encuestaFacadeAux.findEncuestasByIdGlobal(enc.getId());
            //Busco la encuesta por si idglobal, si viene null la creo
            if (encuesta == null) {
                encuesta = enc;
                encuesta.setFechaInicio(convertirFecha(encuesta.getFechaInicioString()));
                encuesta.setFechaFin(convertirFecha(encuesta.getFechaFinString()));
                encuesta.setGlobalId(encuesta.getId());
                //tienda = tiendafacade.find(WebServicesUtilities.ID_TIENDA);
                Tienda tienda = tiendafacade.find(WebServicesUtilities.ID_TIENDA);
                encuesta.setTiendaId(tienda);
                encuesta.getPreguntaList();
                //Se hace este doble ciclo apra volver a setear los ids de las listas de clases
                //cuando no lo hacía me daba un error de que estaban en null
                for (Pregunta pregunta : encuesta.getPreguntaList()) {
                    pregunta.setEncuestaId(encuesta);
                    List<RespuestaConf> listaRespConf = pregunta.getRespuestaConfList();
                    for (RespuestaConf respuestaConf : listaRespConf) {
                        respuestaConf.setPreguntaId(pregunta);
                    }
                }
                //con la persistencia se guarda la informacion en todas las tablas por la anotacion de cascada en las relaciones "one to many"
                encuestaFacade.create(encuesta);
                //Este mensaje de response puede ser leido en el lado cliente a través del inputstream con un streambuilder.
                rsp = Response.status(200).entity("La encuesta fue creada exitosamente en la tienda " + encuesta.getTiendaId().getNombre()).build();
            } else {
                rsp = Response.status(200).entity("La encuesta ya fue enviada anteriormente a la tienda " + encuesta.getTiendaId().getNombre()).build();
            }
        } catch (NamingException ex) {
            Logger.getLogger(EncuestaServiceManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return rsp;
    }

    public Date convertirFecha(String fecha) {
        Date date = null;
        try {
            SimpleDateFormat formateador = new SimpleDateFormat("dd-MM-yyyy");
            date = formateador.parse(fecha);

        } catch (ParseException ex) {
            Logger.getLogger(EncuestaServiceManager.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return date;
    }
/**
 * Método para retornar el resultado de una encuesta dado el igGlobal
 * @param idGlobal
 * @return String json de la encueta con las preguntas y sus resultados.
 */
    public String enviarResultadoEncuesta(int idGlobal) {
        String json = "";
        try {
            Encuesta encuesta = null;
            InitialContext cont = new InitialContext();
            EncuestaAuxFacade encuestaFacadeAux = (EncuestaAuxFacade) cont.lookup("java:module/EncuestaAuxFacade");
            if (encuesta == null) {
                encuesta = (Encuesta) encuestaFacadeAux.findEncuestasByIdGlobal(idGlobal);
                Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().setPrettyPrinting().create();
                json = gson.toJson(encuesta);
            }
        } catch (NamingException ex) {
            Logger.getLogger(EncuestaServiceManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return json;
    }
    /**
     * Método auxiliar para obtener los resultados de la encuesta con la clase auxiliar "ResultadoEncuesta"
     * @param idGlobal
     * @return 
     */
    public String enviarResultadoEncuesta2(int idGlobal) {
        String jsonResultado = null;
        try {
            ResultadoEncuesta restultadoEncuesta = new ResultadoEncuesta();
            InitialContext cont = new InitialContext();
            EncuestaAuxFacade encuestaFacadeAux = (EncuestaAuxFacade) cont.lookup("java:module/EncuestaAuxFacade");
            Encuesta encuesta = (Encuesta) encuestaFacadeAux.findEncuestasByIdGlobal(idGlobal);
            List<Pregunta> listaPreguntas = encuesta.getPreguntaList();

            restultadoEncuesta.setIdGlobal(idGlobal);
            restultadoEncuesta.setNombreEncuesta(encuesta.getNombre());

            for (Pregunta pregunta : listaPreguntas) {
                if (pregunta.getTipo() == 1) {
                    Tipo1 t1 = new Tipo1();
                    List<String> respuestasT1 = new ArrayList<>();
                    t1.setPregunta(pregunta.getPregunta());
                    for (RespuestaObtenida respObt : pregunta.getRespuestaObtenidaList()) {
                        respuestasT1.add(respObt.getRespuesta());
                    }
                    t1.setRespuestas(respuestasT1);
                    restultadoEncuesta.getPreguntasT1().add(t1);

                } else if (pregunta.getTipo() == 2) {
                    Tipo2 t2 = new Tipo2();
                    List<Integer> respuestasT2 = new ArrayList<>();
                    t2.setPregunta(pregunta.getPregunta());
                    for (RespuestaObtenida respObt : pregunta.getRespuestaObtenidaList()) {
                        respuestasT2.add(Integer.parseInt(respObt.getRespuesta()));
                    }
                    t2.setRespuestas(respuestasT2);
                    restultadoEncuesta.getPreguntasT2().add(t2);

                } else if (pregunta.getTipo() == 3) {
                    Tipo3 t3 = new Tipo3();
                    t3.setPregunta(pregunta.getPregunta());

                    for (RespuestaConf respConf : pregunta.getRespuestaConfList()) {
                        Opcion opc = new Opcion();
                        opc.setOpcion(respConf.getOpcion());
                        opc.setCantidad(respConf.getRespuestaObtenidaList().size());
                        t3.getOpciones().add(opc);
                    }
                    restultadoEncuesta.getPreguntasT3().add(t3);
                } else if (pregunta.getTipo() == 4) {
                    Tipo4 t4 = new Tipo4();
                    List<Integer> respuestasT4 = new ArrayList<>();
                    t4.setPregunta(pregunta.getPregunta());
                    for (RespuestaObtenida respObt : pregunta.getRespuestaObtenidaList()) {
                        respuestasT4.add(Integer.parseInt(respObt.getRespuesta()));
                    }
                    t4.setRespuestas(respuestasT4);
                    restultadoEncuesta.getPreguntasT4().add(t4);
                }
            }
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            jsonResultado = gson.toJson(restultadoEncuesta);

        } catch (NamingException ex) {
            Logger.getLogger(EncuestaServiceManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return jsonResultado;
    }

    /**
     * Método para retornar el resultado de una encuesta dado el igGlobal
     * SE setea la lista de Respuestas por Pregunta
     * @param idGlobal
     * @return 
     */
    public String enviarResultadoEncuesta3(int idGlobal) {
        String jsonRespuestas = null;
        try {          
            InitialContext cont = new InitialContext();
            EncuestaAuxFacade encuestaFacadeAux = (EncuestaAuxFacade) cont.lookup("java:module/EncuestaAuxFacade");
            encuesta = (Encuesta) encuestaFacadeAux.findEncuestasByIdGlobal(idGlobal);
            RespuestaObtenidaFacade respObtenidaFacade = (RespuestaObtenidaFacade) cont.lookup("java:module/RespuestaObtenidaFacade");
            List<RespuestaObtenida> respuestas = respObtenidaFacade.findRespuestaObtenida(encuesta);
            
            for(RespuestaObtenida resp : respuestas){
                resp.setPregunta(resp.getPreguntaId().getPregunta());
                resp.setTipoPregunta(resp.getPreguntaId().getTipo());
                resp.setConf(resp.getRespuestaConfId().getOpcion());
            }

            Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().setPrettyPrinting().create();
            jsonRespuestas = gson.toJson(respuestas, new TypeToken<List<RespuestaObtenida>>() {}.getType());

        } catch (NamingException ex) {
            Logger.getLogger(EncuestaServiceManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return jsonRespuestas;
    }
}
