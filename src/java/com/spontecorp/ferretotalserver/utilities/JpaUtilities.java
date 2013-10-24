package com.spontecorp.ferretotalserver.utilities;

import com.spontecorp.ferretotalserver.security.Cifrador;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.Permission;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author jgcastillo
 */
public class JpaUtilities {

    private static final Logger logger = LoggerFactory.getLogger(JpaUtilities.class);
    private static String PERSITENCE_UNIT = "FerreAsesorServerPU";
    public static int HABILITADO = 1;
    public static int INHABILITADO = 0;
    public static final Integer ID_TIENDA = 1;
    public static final int FERIADO = 1;
    public static final int NORMAL = 0;
    //Ruta del WS
    public static final String COMMON_PATH = "ferreasesorweb/webresources/llamadaService";
    //PAra Seleccionar el Metodo del WS dependiendo de la solicitud
    public static final String TOTAL_LLAMADA_TIENDA = "totaltienda";
    public static final String UPDATE_TIENDA_X_FECHA = "tiendaporfecha";
    public static final String ENVIAR_ENCUESTA = "enviarencuesta";
    public static final String OBTENER_RESULTADOS_ENCUESTA = "obtenerresultadosencuesta3";
    public static final String UPDATE_LLAMADA_FECHA = "updatellamadasporfecha";
    public static final String UPDATE_UBICACION_TIENDA = "guardarubicaciones";
    //Encuesta Tipo de pregunta
    public static final int PREGUNTA_TEXTUAL = 1;
    public static final int PREGUNTA_NUMERICA = 2;
    public static final int PREGUNTA_SELECCION = 3;
    public static final int PREGUNTA_CALIFICACION = 4;
    // Tipo de reporte a mostrar
    public static final int REPORTE_POR_FECHA = 1;
    public static final int REPORTE_POR_DISPOSITIVO = 2;
    public static final int REPORTE_POR_FERREASESOR = 3;
    public static final int REPORTE_POR_CALIDAD = 4;
    //Calidad de la Llamada
    public static final String ATENCION_BUENA = "Buena";
    public static final String ATENCION_REGULAR = "Regular";
    public static final String ATENCION_MALA = "Mala";
    public static final String CIERRE_AUTOMATICO = "Automatica";

    public JpaUtilities() {
    }

    public static EntityManagerFactory getEntityManagerFactory() {
        return Persistence.createEntityManagerFactory(PERSITENCE_UNIT);
//        String[] props = null;
//        try {
//            props = readPreFile();    
//        } catch (Exception e) {
//        }
//        return setProperties(props[0], props[1], props[2], props[3]);
    }

    private static EntityManagerFactory setProperties(String url, String psw, String driver, String user) {
        Map props = new HashMap();
        props.put("javax.persistence.jdbc.url", url);
        props.put("javax.persistence.jdbc.password", psw);
        props.put("javax.persistence.jdbc.driver", driver);
        props.put("javax.persistence.jdbc.user", user);
        EntityManagerFactory emf = Persistence.createEntityManagerFactory(PERSITENCE_UNIT, props);
        return emf;
    }

    private static String[] readPreFile() throws IOException {
        Cifrador cipher = new Cifrador();
        String[] props = new String[5];
        BufferedReader input = null;
        try {
            int i = 0;
            String line;
            String decode;
            String temp = null;
            input = new BufferedReader(new FileReader("pre.spt"));

            while ((line = input.readLine()) != null) {
                props[i++] = line;
            }
//            while((line = input.readLine()) != null){
//                System.out.println("read: " + line);
//                if(line.endsWith("=")){
//                    if(temp != null){
//                        decode = cipher.decrypt(temp + line);
//                        temp = null;
//                    } else {
//                        decode = cipher.decrypt(line);
//                    }
//                    System.out.println(decode);
//                    props[i++] = decode;
//                } else {
//                    temp = line;
//                }
//            }
        } catch (IOException e) {
            logger.error("Error leyendo archivo de configuración" + e);
        } finally {
            if (input != null) {
                input.close();
            }
        }
        return props;
    }

    /**
     * Método para Verificar la Conexión con el WS
     *
     * @param connection
     * @throws Exception
     */
    public static void testHttpURLConnection(HttpURLConnection connection) throws Exception {
        boolean connAllowUserInteraction = connection.getAllowUserInteraction();
        String connContentType = connection.getContentType();
        String connContentEncoding = connection.getContentEncoding();
        String connRequestMethod = connection.getRequestMethod();
        boolean connDoInput = connection.getDoInput();
        boolean connDoOutput = connection.getDoOutput();
        Permission connPermission = connection.getPermission();
        URL connURL = connection.getURL();
        Map<String, List<String>> connHeaderFields = connection.getHeaderFields();

        System.out.println("connAllowUserInteraction: " + connAllowUserInteraction);
        System.out.println("connContentType: " + connContentType);
        System.out.println("connContentEncoding: " + connContentEncoding);
        System.out.println("connRequestMethod: " + connRequestMethod);
        System.out.println("connDoInput: " + connDoInput);
        System.out.println("connDoOutput: " + connDoOutput);
        System.out.println("connPermission: " + connPermission);
        System.out.println("connURL: " + connURL);

        if (connHeaderFields != null) {
            Set<Map.Entry<String, List<String>>> connHeaderFieldsEntries = connHeaderFields.entrySet();
            for (Map.Entry<String, List<String>> entry : connHeaderFieldsEntries) {
                System.out.println("connHeaderField: " + entry);
            }
        }
    }
}
