/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spontecorp.ferretotalserver.jpa;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.spontecorp.ferretotalserver.entity.Encuesta;
import com.spontecorp.ferretotalserver.entity.Pregunta;
import com.spontecorp.ferretotalserver.entity.RespuestaConf;
import com.spontecorp.ferretotalserver.entity.RespuestaObtenida;
import com.spontecorp.ferretotalserver.entity.Tienda;
import com.spontecorp.ferretotalserver.utilities.JpaUtilities;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 *
 * @author sponte03
 */
@Stateless
public class HttpURLConnectionEncuestas {

    private Boolean statusConnection = false;
    private String messageConnection;

    public HttpURLConnectionEncuestas() {
    }

    /**
     * Enviar Encuestas a una Tienda a través del WS
     *
     * @param hostname
     * @param encuesta
     * @throws Exception
     */
    public String sendSurveyWS(String hostname, Encuesta encuesta) throws Exception {
        String message = "";
        try {
            URL serverAddress;
            serverAddress = new URL(hostname);
            HttpURLConnection connection = (HttpURLConnection) serverAddress.openConnection();

            //Se genera el Objeto Json de la Encuesta
            String jsn = getJsonEncuesta(encuesta);
            setStatusConnection(false);

            //System.out.println("2.- URL: " + hostname);
            //System.out.println("3.- Json Encuesta: " + jsn);

            connection.setDoOutput(true);
            connection.setRequestMethod("POST");

            connection.setRequestProperty("charset", "UTF-8");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestProperty("Content-Length", Integer.toString(jsn.length()));

            jsn = new String(jsn.getBytes(), "UTF-8");
            //jsn = new String(jsn.getBytes());

            connection.getOutputStream().write(jsn.getBytes());
            connection.getOutputStream().flush();
            connection.connect();
            int response = connection.getResponseCode();

            //Verificamos la Conexión
            //JpaUtilities.testHttpURLConnection(connection); 
            
            //Si la Conexión fue exitosa leo el Response
            //lo seteo en la variable message para imprimirla en el Jsf
            //y seteo el Status de Conexión a true. (Para validar mensaje a mostrar al Usuario).
            if (response == 200) {
                InputStreamReader in = new InputStreamReader((InputStream) connection.getContent());
                BufferedReader buff = new BufferedReader(in);
                StringBuilder text = new StringBuilder();
                String line;
                
                while ((line = buff.readLine()) != null) {
                        text.append(line);
                }
                
                message = text.toString();
                setStatusConnection(true);
            }
            //connection
            connection.disconnect();

        } catch (MalformedURLException ex) {
            Logger.getLogger(HttpURLConnectionEncuestas.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ProtocolException ex) {
            Logger.getLogger(HttpURLConnectionEncuestas.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(HttpURLConnectionEncuestas.class.getName()).log(Level.SEVERE, null, ex);
        }

        return message;
    }

    /**
     * Obtener Resultados de una Encuesta de una Tienda a través del WS
     *
     * @param hostname
     * @param encuesta
     * @param tienda
     * @throws NamingException
     */
    public void getResultsSurveyWS(String hostname, Encuesta encuesta, Tienda tienda) throws NamingException {
        List<RespuestaObtenida> lista = null;
        try {
            // Use the java.net.* APIs to access the Duke’s Age RESTful web service
            HttpURLConnection connection = null;
            BufferedReader rd = null;
            StringBuilder sb = null;
            String line = null;
            URL serverAddress = null;

            serverAddress = new URL(hostname);
            connection = (HttpURLConnection) serverAddress.openConnection();

            if (connection != null) {
                connection.setRequestMethod("GET");
                // connection.setRequestProperty("charset", "UTF-8");
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setRequestProperty("Accept", "application/json");
                connection.setDoInput(true);
                connection.setReadTimeout(10000);
                connection.connect();

                //Verificamos la Conexión
                //JpaUtilities.testHttpURLConnection(connection);

                if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    setStatusConnection(false);
                } else {
                    setStatusConnection(true);

                    rd = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    sb = new StringBuilder();

                    while ((line = rd.readLine()) != null) {
                        sb.append(line);
                    }

                    Gson gson = new Gson();
                    lista = gson.fromJson(sb.toString(), new TypeToken<List<RespuestaObtenida>>() {
                    }.getType());

                    guardarRespuestas(lista, encuesta, tienda);
                }
                connection.disconnect();
            }

        } catch (MalformedURLException ex) {
            Logger.getLogger(HttpURLConnectionLlamadas.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(HttpURLConnectionLlamadas.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     * Genera el Objeto Json de la Encuesta
     *
     * @return
     */
    public String getJsonEncuesta(Encuesta encuesta) throws NamingException {
        String json = null;

        InitialContext context = new InitialContext();
        PreguntaFacade preguntaFacade = (PreguntaFacade) context.lookup("java:module/PreguntaFacade");
        RespuestaConfFacade respuestaConfFacade = (RespuestaConfFacade) context.lookup("java:module/RespuestaConfFacade");

        List<Pregunta> preguntaList = preguntaFacade.findAll(encuesta);
        for (Pregunta pregunta : preguntaList) {
            List<RespuestaConf> opcionsList = respuestaConfFacade.findRespuestaConf(pregunta);
            pregunta.setRespuestaConfList(opcionsList);
        }

        encuesta.setPreguntaList(preguntaList);

        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().setPrettyPrinting().create();
        json = gson.toJson(encuesta);
        return json;
    }

    /**
     * Se procesa la Encuesta obtenida a través del WS para guardar las
     * Respuestas Obtenidas en la BD
     *
     * @param encuestaJson
     * @param encuesta
     * @param tienda
     */
    public void guardarRespuestas(List<RespuestaObtenida> respuestaJson, Encuesta encuesta, Tienda tienda) throws NamingException {

        InitialContext context = new InitialContext();
        PreguntaFacade preguntaFacade = (PreguntaFacade) context.lookup("java:module/PreguntaFacade");
        RespuestaConfFacade respuestaConfFacade = (RespuestaConfFacade) context.lookup("java:module/RespuestaConfFacade");
        RespuestaObtenidaFacade respuestaObtFacade = (RespuestaObtenidaFacade) context.lookup("java:module/RespuestaObtenidaFacade");

        //Recorro la lista de Respuestas Obtenidas
        for (RespuestaObtenida respuesta : respuestaJson) {
            //Seteo el id de la Encuesta
            respuesta.setEncuestaId(encuesta);
            //Seteo el id de la Tienda
            respuesta.setTiendaId(tienda);
            //Busco la Pregunta para obtener el id Local (BD Server)
            Pregunta preguntaLocal = preguntaFacade.findPregunta(respuesta.getPregunta(), respuesta.getTipoPregunta(), encuesta);
            //Seteo el id de la Pregunta Local
            respuesta.setPreguntaId(preguntaLocal);
            RespuestaConf respConfLocal;
            //Se setea el id Local de Respuesta Conf dependiendo del Tipo de Pregunta
            if (respuesta.getTipoPregunta() == JpaUtilities.PREGUNTA_SELECCION) {
                respuesta.setRespuesta(respuesta.getConf());
                respConfLocal = respuestaConfFacade.find(respuesta.getPreguntaId(), respuesta.getConf());
            } else {
                respConfLocal = respuestaConfFacade.find(respuesta.getPreguntaId());
            }
            respuesta.setRespuestaConfId(respConfLocal);
            //Se inserta la Respuesta en la DB
            respuestaObtFacade.create(respuesta);
        }

    }

    public Boolean getStatusConnection() {
        return statusConnection;
    }

    public void setStatusConnection(Boolean statusConnection) {
        this.statusConnection = statusConnection;
    }

    public String getMessageConnection() {
        return messageConnection;
    }

    public void setMessageConnection(String messageConnection) {
        this.messageConnection = messageConnection;
    }
}
