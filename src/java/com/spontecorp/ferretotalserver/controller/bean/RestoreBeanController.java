/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spontecorp.ferretotalserver.controller.bean;

import com.spontecorp.ferretotalserver.controller.util.JsfUtil;
import com.spontecorp.ferretotalserver.entity.ConfigurationDb;
import com.spontecorp.ferretotalserver.jpa.ConfigurationDbFacade;
import com.spontecorp.ferretotalserver.utilities.JpaUtilities;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author sponte03
 */
@ManagedBean(name = "restoreBeanController")
@SessionScoped
public class RestoreBeanController implements Serializable {

    private UploadedFile uploadedFile;
    private StreamedContent downloadFile;
    //Datos de la BD 
    private String dbName = null;
    private String dbUserName = null;
    private String dbPassword = null;
    private String mysqlexe = null;
    //Ruta para crear el archivo .sql
    private String srcFile = null;
    private String restoreFile = null;

    public RestoreBeanController() {
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
                dbName = configDB.getDbName();
                dbUserName = configDB.getDbuserName();
                dbPassword = configDB.getDbPassword();
                // Set path. Set location of mysqlexe        
                mysqlexe = "\"" + configDB.getPathMysql() + "\"";
                isConfig = true;
            } else {
                isConfig = false;
            }
        } catch (NamingException ex) {
            java.util.logging.Logger.getLogger(JpaUtilities.class.getName()).log(Level.SEVERE, null, ex);
        }
        return isConfig;
    }

    /**
     * Restore de la DB. Método para Restaurar la Base de Datos
     *
     * @param evt
     * @return
     */
    public boolean restoreDB(ActionEvent evt) throws IOException {

        boolean result = false;
        //Tipo de archivo permitido (.sql)
        String contentType = "application/octet-stream";

        if (uploadedFile != null) {

            //Obtengo la configuración de la Base de Datos
            configurarDB();

            if (uploadedFile.getContentType().equalsIgnoreCase(contentType)) {
                restoreFile = uploadedFile.getFileName();

                //Ruta + Nombre del Archivo a Restaurar
                srcFile = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/resources/temp/" + restoreFile);

                //Se copia el archivo a la Carpeta temporal
                result = JpaUtilities.saveFile(uploadedFile, srcFile);

                if (result) {
                    //Se arma el comando a ejecutar
                    String[] executeCmd = new String[]{mysqlexe, "--user=" + dbUserName, "--password="
                        + dbPassword, dbName, "-e", "source " + srcFile};
                    Process runtimeProcess;

                    try {
                        runtimeProcess = Runtime.getRuntime().exec(executeCmd);
                        int processComplete = runtimeProcess.waitFor();

                        if (processComplete == 0) {
                            JsfUtil.addSuccessMessage("La Base de Datos fue restaurada con éxito!");
                            result = true;
                            //Elimino el archivo de la Carpeta Temporal
                            JpaUtilities.deleteFile(srcFile);
                        } else {
                            JsfUtil.addErrorMessage("No se pudo restaurar la Base de Datos.");
                            result = false;
                        }
                    } catch (IOException | InterruptedException ex) {
                        System.out.println("Error al restaurar la Base de Datos: " + ex.getMessage());
                    }

                } else {
                    JsfUtil.addErrorMessage("No se pudo restaurar la Base de Datos.");
                }
            } else {
                JsfUtil.addErrorMessage("Selecciones un archivo .sql");
            }
        } else {
            JsfUtil.addErrorMessage("Selecciones un archivo .sql");
        }
        uploadedFile = null;
        restoreFile = null;
        return result;
    }

    public UploadedFile getUploadedFile() {
        return uploadedFile;
    }

    public void setUploadedFile(UploadedFile uploadedFile) {
        this.uploadedFile = uploadedFile;
    }

    public StreamedContent getDownloadFile() {
        return downloadFile;
    }

    public void setDownloadFile(StreamedContent downloadFile) {
        this.downloadFile = downloadFile;
    }
}
