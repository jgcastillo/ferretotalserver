/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spontecorp.ferretotalserver.jpa;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.spontecorp.ferretotalserver.entity.Ubicacion;
import com.spontecorp.ferretotalserver.utilities.JpaUtilities;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
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
public class HttpURLConnectionUbicacion implements Serializable{

    private Boolean statusConnection = false;
    
    public HttpURLConnectionUbicacion() {
    }
    
    public String sendUbicacionesWS(String hostname, String jsn) throws Exception {
        String message = "";
        try {
            URL serverAddress;
            serverAddress = new URL(hostname);
            HttpURLConnection connection = (HttpURLConnection) serverAddress.openConnection();

            setStatusConnection(false);
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");

            connection.setRequestProperty("charset", "UTF-8");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Accept", "application/json");
            byte[] bytes = jsn.getBytes("UTF-8");
            connection.setRequestProperty("Content-Length", String.valueOf(bytes.length));

            connection.getOutputStream().write(bytes);
            connection.getOutputStream().flush();
            connection.connect();
            
            int response = connection.getResponseCode();

            //Verificamos la Conexión
            //JpaUtilities.testHttpURLConnection(connection); 
            
            //Si la Conexión fue exitosa leo el Response
            //lo seteo en la variable message para imprimirla en el Jsf
            //y seteo el Status de Conexión a true. (Para validar mensaje a mostrar al Usuario).
            if (response == 200) {
                InputStreamReader in = new InputStreamReader((InputStream) connection.getContent(), "UTF-8");
                BufferedReader buff = new BufferedReader(in);
                StringBuilder text = new StringBuilder();
                String line;
                
                while ((line = buff.readLine()) != null) {
                        text.append(line);
                }
                buff.close();
                
                message = text.toString();
                setStatusConnection(true);
            }
            //Close Connection
            connection.disconnect();

        } catch (MalformedURLException ex) {
            Logger.getLogger(HttpURLConnectionUbicacion.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ProtocolException ex) {
            Logger.getLogger(HttpURLConnectionUbicacion.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(HttpURLConnectionUbicacion.class.getName()).log(Level.SEVERE, null, ex);
        }

        return message;
    }
    
    /**
     * Genera el Objeto Json de la Lista de Ubicaciones
     *
     * @return
     */
    public String getJsonUbicacionList() throws NamingException {
        String json = null;

        InitialContext context = new InitialContext();
        UbicacionFacade ubicacionFacade = (UbicacionFacade) context.lookup("java:module/UbicacionFacade");
        RespuestaConfFacade respuestaConfFacade = (RespuestaConfFacade) context.lookup("java:module/RespuestaConfFacade");

        //Obtengo el Listado de Ubcaciones con Status Activo
        List<Ubicacion> ubicacionList = ubicacionFacade.findUbicacionList(JpaUtilities.HABILITADO);

        //Se construye el gson de forma tal que ignore los campos que NO tengan la anotacion @Expose
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().setPrettyPrinting().create();
        String arrUbicacionJson = gson.toJson(ubicacionList, new TypeToken<List<Ubicacion>>() {
        }.getType());

        return arrUbicacionJson;
    }

    public Boolean getStatusConnection() {
        return statusConnection;
    }

    public void setStatusConnection(Boolean statusConnection) {
        this.statusConnection = statusConnection;
    }
    
    
}
