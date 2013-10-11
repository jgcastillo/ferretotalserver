package com.spontecorp.ferreasesor.controller.encuesta;


import com.spontecorp.ferreasesor.controller.util.JsfUtil;
import com.spontecorp.ferreasesor.entity.Encuesta;
import com.spontecorp.ferreasesor.entity.Pregunta;
import com.spontecorp.ferreasesor.entity.RespuestaConf;
import com.spontecorp.ferreasesor.entity.Tienda;
import com.spontecorp.ferreasesor.jpa.EncuestaFacade;
import com.spontecorp.ferreasesor.jpa.PreguntaFacade;
import com.spontecorp.ferreasesor.jpa.RespuestaConfFacade;
import com.spontecorp.ferreasesor.jpa.TiendaFacade;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import net.sf.jasperreports.engine.JRException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author jgcastillo
 */
@ManagedBean(name = "encuestaBeanController")
@SessionScoped
public class EncuestaBeanController implements Serializable {

    private Encuesta current;
    private transient DataModel items = null;
    @EJB
    private EncuestaFacade ejbFacade;
    @EJB
    private TiendaFacade tiendaFacade;
    @EJB
    private PreguntaFacade preguntaFacade;
    @EJB
    private RespuestaConfFacade respuestaFacade;
    private static final int ACTIVO = 1;
    private static final int INACTIVO = 0;
    @ManagedProperty(value = "#{preguntaBean}")
    private PreguntaBeanController preguntaBean;
    private String nombreReporte;
    private List<Pregunta> preguntaList = null;
    private List<RespuestaConf> opcionsList = null;
    private Logger logger = LoggerFactory.getLogger(EncuestaBeanController.class);

    public EncuestaBeanController() {
    }

    /**
     * Usados para soportar la annotacion
     *
     * @ManagedProperty
     * @return
     */
    public PreguntaBeanController getPreguntaBean() {
        return preguntaBean;
    }
    
    private PreguntaFacade getPreguntaFacade() {
        return preguntaFacade;
    }

    private RespuestaConfFacade getRespuestaFacade() {
        return respuestaFacade;
    }

    public void setPreguntaBean(PreguntaBeanController preguntaBean) {
        this.preguntaBean = preguntaBean;
    }

    public Encuesta getSelected() {
        if (current == null) {
            current = new Encuesta();
        }
        return current;
    }

    private EncuestaFacade getFacade() {
        return ejbFacade;
    }

    private TiendaFacade getTiendaFacade() {
        return tiendaFacade;
    }

    public DataModel getItems() {
        if (items == null) {
            items = new ListDataModel(getFacade().findAll());
        }
        return items;
    }

    public String prepareList() {
        recreateModel();
        return "List";
    }

    public String prepareCreate() {
        current = new Encuesta();
        return "createSurvey?faces-redirect=true";
    }

    private void getCurrent() {
        current = (Encuesta) getItems().getRowData();
        logger.info("la encuesta seleccionada es: " + current.getNombre());
        preguntaBean.setEncuesta(current);
    }

    public String prepareCancel() {
        current = null;
        recreateModel();
        return "encuestaMain?faces-redirect=true";
    }

    public String prepareEdit() {
        getCurrent();
        return "editSurvey?faces-redirect=true";
    }

    public String prepareAddQuestions() {
        getCurrent();
        return "createQuestions?faces-redirect=true";
    }
    
    public String prepareSurveyAnalysis() {
        getCurrent();
        return "surveyAnalysisDetails?faces-redirect=true";
    }

    public String prepareDelete() {
        getCurrent();
        if (!current.getPreguntaList().isEmpty()) {
            JsfUtil.addErrorMessage("La encuesta tiene preguntas cargadas, no puede ser eliminada");
        } else {
            getFacade().remove(current);
        }
        return prepareCancel();
    }

    private void recreateModel() {
        items = null;
    }

    public String prepareActivate() {
        getCurrent();
        if (current.getStatus() == ACTIVO) {
            current.setStatus(INACTIVO);
        } else {
            current.setStatus(ACTIVO);
        }
        update();
        return prepareCancel();
    }

    public String create() {
        Tienda tienda = getTiendaFacade().find(1);
        try {
            current.setTiendaId(tienda);
            current.setStatus(INACTIVO);
            getFacade().create(current);
            JsfUtil.addSuccessMessage("Encuesta creada con éxito");
            return prepareCancel();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, "Error a nivel de Base de Datos");
            return null;
        }
    }

    public String update() {
        try {
            getFacade().edit(current);
            JsfUtil.addSuccessMessage("Encuesta actualizada con éxito");
            return prepareCancel();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, "Error a nivel de Base de Datos");
            return null;
        }
    }

    public String getNombreReporte() {
        return nombreReporte;
    }

    public void setNombreReporte(String nombreReporte) {
        this.nombreReporte = nombreReporte;
    }
    
    /**
     * Lista de Preguntas (con lista de Respuestas por cada Pregunta) de la Encuesta Seleccionada
     * @return 
     */
    public List<Pregunta> getPreguntaList() {
        preguntaList = null;
        opcionsList = null;

        if (current != null && preguntaList == null) {
            preguntaList = getPreguntaFacade().findAll(current);
            for (Pregunta pregunta : preguntaList) {
                opcionsList = getRespuestaFacade().findRespuestaConf(pregunta);
                pregunta.setRespuestaConfList(opcionsList);
            }
        }

        return preguntaList;

    }
    
    /**
     * Preparar Exportar Reporte de Encuestas PDF
     * @param actionEvent
     * @throws JRException
     * @throws IOException 
     */
    public void exportarReportePDF(ActionEvent actionEvent) throws JRException, IOException {
        String extension = "PDF";
        String jasperFileAddress = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/resources/reports/encuesta/reporteEncuestaPdf.jasper");
   //     exportarReporte(extension, jasperFileAddress);

    }
    
    
}
