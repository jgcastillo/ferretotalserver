package com.spontecorp.ferreasesor.utilities;

import com.spontecorp.ferreasesor.security.Cifrador;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
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
    private static String PERSITENCE_UNIT = "FerreAsesorWebPU";
    
    public static int HABILITADO = 1;
    public static int INHABILITADO = 0;
    public static final Integer ID_TIENDA = 1;
    public static final int FERIADO = 1;
    public static final int NORMAL = 0;

    public JpaUtilities(){
    }
    
    public static EntityManagerFactory getEntityManagerFactory(){
        return Persistence.createEntityManagerFactory(PERSITENCE_UNIT);
//        String[] props = null;
//        try {
//            props = readPreFile();    
//        } catch (Exception e) {
//        }
//        return setProperties(props[0], props[1], props[2], props[3]);
    }
    
    private static EntityManagerFactory setProperties(String url, String psw, String driver, String user){
        Map props = new HashMap();
        props.put("javax.persistence.jdbc.url", url);
        props.put("javax.persistence.jdbc.password", psw);
        props.put("javax.persistence.jdbc.driver", driver);
        props.put("javax.persistence.jdbc.user", user);
        EntityManagerFactory emf = Persistence.createEntityManagerFactory(PERSITENCE_UNIT, props);
        return emf;
    }
    
    private static String[] readPreFile() throws IOException{
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
            if(input != null){
                input.close();
            }
        }
        return props;
    }
  
    
}
