/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spontecorp.ferretotalserver.controller.bean;

import com.spontecorp.ferretotalserver.controller.util.JsfUtil;
import com.spontecorp.ferretotalserver.entity.ConfigurationDb;
import com.spontecorp.ferretotalserver.jpa.ConfigurationDbFacade;
import com.spontecorp.ferretotalserver.utilities.JpaUtilities;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletContext;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

/**
 *
 * @author sponte03
 */
@ManagedBean(name = "fileDownloadController")
@SessionScoped
public final class FileDownloadController implements Serializable {

    private StreamedContent downloadFile;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
    //Datos de la BD 
    private String dbName = null;
    private String dbUserName = null;
    private String dbPassword = null;
    private String mysqldump = null;
    private String filepath = null;
    private String host = null;
    private String port = null;
    //Nombre del archivo .sql a crear
    private String filename = null;

    public FileDownloadController() {
        boolean findConfig = configurarDB();

        if (findConfig) {
            backUpDB();
        } else {
            JsfUtil.addErrorMessage("No se ha Configurado la Base de Datos.");
        }
    }

    /**
     * Obtengo la configuración de la Base de Datos
     *
     * @return
     */
    public boolean configurarDB() {
        List<ConfigurationDb> configuracionDB = new ArrayList<>();
        ConfigurationDb configDB = new ConfigurationDb();
        boolean isConfig = false;
        try {
            InitialContext context = new InitialContext();
            ConfigurationDbFacade configuracionDBFacade = (ConfigurationDbFacade) context.lookup("java:module/ConfigurationDbFacade");
            configuracionDB = configuracionDBFacade.findAll();
            if (configuracionDB.size() > 0) {
                configDB = configuracionDB.get(0);
                host = "localhost";
                port = "3306";
                dbName = configDB.getDbName();
                dbUserName = configDB.getDbuserName();
                dbPassword = configDB.getDbPassword();
                // Set path. Set location of mysqldump        
                mysqldump = "\"" + configDB.getPathMysqldump() + "\"";
                filename = "ferreasesorServerDB_" + sdf.format((new Date())) + ".sql";
                filepath = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/resources/temp/" + filename);
                isConfig = true;
            } else {
                isConfig = false;
            }
        } catch (NamingException ex) {
            java.util.logging.Logger.getLogger(JpaUtilities.class.getName()).log(Level.SEVERE, null, ex);
        }
        return isConfig;
    }

    public StreamedContent getDownloadFile() {
        return downloadFile;
    }

    public void setDownloadFile(StreamedContent downloadFile) {
        this.downloadFile = downloadFile;
    }

    /**
     * BackUp de la DB. Método para Respaldar la Base de Datos
     */
    public void backUpDB() {

        //Se arma el comando a ejecutar para probar que funciona 
        //la creación del BackUp vía Cónsola (cmd)
        //String executeCmd = mysqldump + " -u " + dbUserName + " -p" + dbPassword
        //        + " --add-drop-database -B " + dbName + " -r " + filepath;
        //System.out.println("executeCmd: " + executeCmd);

        //Llamo al método que crea el archivo con el Back Up
        boolean success = BackupDatabase();

        if (success) {
            //System.out.println("Backup created successfully");
            JsfUtil.addSuccessMessage("Respaldo de la Base de Datos creado con éxito! "
                    + "Ruta del archivo creado: " + filepath);

            InputStream stream = ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getResourceAsStream("/resources/temp/" + filename);
            downloadFile = new DefaultStreamedContent(stream, "application/octet-stream", filename);

            //Elimino el archivo de la Carpeta Temporal
            JpaUtilities.deleteFile(filepath);
        } else {
            //System.out.println("Could not create the backup");
            JsfUtil.addErrorMessage("No se pudo crear el Respaldo de la Base de Datos.");
        }
    }

    /**
     * Método para crear el archivo con el Back Up
     *
     * @return
     */
    public boolean BackupDatabase() {

        boolean success = false;
        try {
            // Get SQL DUMP data
            String dump = getServerDumpData(dbUserName, dbPassword, dbName);
            if (!dump.isEmpty()) {
                byte[] data = dump.getBytes("UTF-8");
                // Set backup folder
                String rootpath = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/resources/temp/");
                // See if backup folder exists
                File file = new File(rootpath);
                if (!file.isDirectory()) {
                    // Create backup folder when missing. Write access is needed.
                    file.mkdir();
                }
                // Write SQL DUMP file
                File filedst = new File(filepath);
                FileOutputStream dest = new FileOutputStream(filedst);
                dest.write(data);
                dest.close();
                success = true;
            }

        } catch (Exception ex) {
            System.out.println("Error al crear el Respaldo de la Base de Datos BackupDatabase(): " + ex.getMessage());
        }
        return success;
    }

    /**
     * Get SQL DUMP data
     *
     * @param host
     * @param port
     * @param user
     * @param password
     * @param db
     * @return
     */
    public String getServerDumpData(String user, String password, String db) {
        StringBuilder dumpdata = new StringBuilder();
        int STREAM_BUFFER = 512000;
        try {
            if (host != null && user != null && password != null && db != null) {

                String[] command = new String[]{mysqldump,
                    // "--host=" + host,
                    //  "--port=" + port,
                    "--user=" + user,
                    "--password=" + password,
                    "--databases",
                    "--add-drop-database",
                    "--complete-insert",
                    "--extended-insert",
                    db};

                // Run mysqldump
                ProcessBuilder pb = new ProcessBuilder(command);
                Process process = pb.start();

                InputStream in = process.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));

                int count;
                char[] cbuf = new char[STREAM_BUFFER];

                // Read datastream
                while ((count = br.read(cbuf, 0, STREAM_BUFFER)) != -1) {
                    dumpdata.append(cbuf, 0, count);
                }
                // Close
                br.close();
                in.close();
            }

        } catch (Exception ex) {
            // Handle exception as you wish
            System.out.println("Error al crear el Respaldo de la Base de Datos getServerDumpData(): " + ex.getMessage());
            return "";
        }
        return dumpdata.toString();
    }
}
