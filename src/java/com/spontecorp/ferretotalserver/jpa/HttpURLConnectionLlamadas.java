/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spontecorp.ferretotalserver.jpa;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.spontecorp.ferretotalserver.entity.Llamada;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;

/**
 *
 * @author sponte03
 */
@Stateless
public class HttpURLConnectionLlamadas implements Serializable {

    private Boolean statusConnection = false;
    private String messageConnection;

    public HttpURLConnectionLlamadas() {
    }

    /**
     *
     * @param hostname
     * @return
     * @throws Exception
     */
    public List<Llamada> getLlamadasWS(String hostname) {
        List<Llamada> lista = null;
        try {
            // Use the java.net.* APIs to access the Duke’s Age RESTful web service
            HttpURLConnection connection = null;
            BufferedReader rd = null;
            StringBuilder sb = null;
            String line = null;
            URL serverAddress = null;

            serverAddress = new URL(hostname);
            connection = (HttpURLConnection) serverAddress.openConnection();

            System.out.println("URL: "+hostname);
            
            if (connection != null) {
                try {
                    connection.setRequestMethod("GET");
                    connection.setRequestProperty("charset", "UTF-8");
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

                        rd = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
                        sb = new StringBuilder();

                        while ((line = rd.readLine()) != null) {
                            sb.append(line);
                        }

                        Gson gson = new Gson();
                        lista = gson.fromJson(sb.toString(), new TypeToken<List<Llamada>>() {
                        }.getType());

                    }

                    connection.disconnect();

                } catch (IOException | JsonSyntaxException e) {
                    Logger.getLogger(HttpURLConnectionLlamadas.class.getName()).log(Level.SEVERE, null, e);
                }

            }

        } catch (MalformedURLException ex) {
            Logger.getLogger(HttpURLConnectionLlamadas.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(HttpURLConnectionLlamadas.class.getName()).log(Level.SEVERE, null, ex);
        }
        return lista;
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
