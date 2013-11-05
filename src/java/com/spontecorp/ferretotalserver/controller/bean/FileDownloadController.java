/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spontecorp.ferretotalserver.controller.bean;


import com.spontecorp.ferretotalserver.controller.util.JsfUtil;
import com.spontecorp.ferretotalserver.utilities.JpaUtilities;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

/**
 *
 * @author sponte03
 */
@ManagedBean(name = "fileDownloadController")
@SessionScoped
public final class FileDownloadController implements Serializable{

    private StreamedContent downloadFile;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
    //Datos de la BD 
    private String dbName = JpaUtilities.DB_NAME;
    private String dbUserName = JpaUtilities.DB_USER;
    private String dbPassword = JpaUtilities.DB_PASSWORD;
    private String mysqldump = JpaUtilities.mysqldump;
    private String savePath;
    //Nombre del archivo .sql a crear
    private String nameFile;

    public FileDownloadController() {
        backUpDB();
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

        boolean result = false;
        nameFile = "ferretotalServerDB_" + sdf.format((new Date())) + ".sql";
        savePath = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/resources/temp/" + nameFile);

        //Se arma el comando a ejecutar
        String executeCmd = mysqldump + " -u " + dbUserName + " -p" + dbPassword
                + " --add-drop-database -B " + dbName + " -r " + savePath;

        Process runtimeProcess;
        try {
            runtimeProcess = Runtime.getRuntime().exec(executeCmd);
            int processComplete = runtimeProcess.waitFor();

            if (processComplete == 0) {
                //System.out.println("Backup created successfully");
                JsfUtil.addSuccessMessage("Respaldo de la Base de Datos creado con éxito! "
                        + "Ruta del archivo creado: " + savePath);
                result = true;

                InputStream stream = ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getResourceAsStream("/resources/temp/" + nameFile);
                downloadFile = new DefaultStreamedContent(stream, "application/octet-stream", nameFile);

                //Elimino el archivo de la Carpeta Temporal
                JpaUtilities.deleteFile(savePath);
            } else {
                //System.out.println("Could not create the backup");
                JsfUtil.addErrorMessage("No se pudo crear el Respaldo de la Base de Datos.");
                result = false;
            }
        } catch (IOException | InterruptedException ex) {
            System.out.println("Error al crear el Respaldo de la Base de Datos: " + ex.getMessage());
        }
    }

}
