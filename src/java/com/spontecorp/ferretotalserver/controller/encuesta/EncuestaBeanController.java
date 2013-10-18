package com.spontecorp.ferretotalserver.controller.encuesta;

import com.spontecorp.ferretotalserver.controller.util.JsfUtil;
import com.spontecorp.ferretotalserver.entity.Encuesta;
import com.spontecorp.ferretotalserver.entity.Pregunta;
import com.spontecorp.ferretotalserver.entity.RespuestaConf;
import com.spontecorp.ferretotalserver.entity.Tienda;
import com.spontecorp.ferretotalserver.jpa.EncuestaFacade;
import com.spontecorp.ferretotalserver.jpa.HttpURLConnectionEncuestas;
import com.spontecorp.ferretotalserver.jpa.PreguntaFacade;
import com.spontecorp.ferretotalserver.jpa.RespuestaConfFacade;
import com.spontecorp.ferretotalserver.jpa.TiendaFacade;
import com.spontecorp.ferretotalserver.utilities.JpaUtilities;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author jgcastillo
 */
@ManagedBean(name = "encuestaBeanController")
@SessionScoped
public class EncuestaBeanController extends EncuestaAbstract implements Serializable {

    @EJB
    private EncuestaFacade ejbFacade;
    private Encuesta current;
    private transient DataModel items = null;
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

    public String prepareSend() {
        getCurrent();
        return "sendSurvey?faces-redirect=true";
    }

    public String prepareShowAnalysisDetails() {
        getCurrent();
        return "showAnalysisDetails?faces-redirect=true";
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
        selectedTiendas = null;
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
        try {
            current.setGlobal(ACTIVO);
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

    /**
     * Enviar Encuesta a una Tienda a través del WS
     *
     * @return
     */
    public String sendSurvey() {

        //Verifico las Tiendas Seleccionadas
        getSelectedAllTiendas();
        getSelectedTiendas();

        //Obtengo la Lista de Tiendas seleccionadas Final
        List<Tienda> listTiendaFinal = obtenerListTiendaSeleccionadas();

        if (listTiendaFinal.size() > 0) {
            //Acceder al WS para enviar la Encuesta a las Tienda(s) Seleccionada(s)
            enviarEncuestaTiendaSeleccionadas(listTiendaFinal, current);
        } else {
            JsfUtil.addErrorMessage("Seleccione la Tienda a la que desea enviar la Encuesta.");
        }
        return prepareList();
        
    }

    /**
     * Obtener Resultados de Encuestas a través del WS
     *
     * @return
     */
    public String showAnalysisSurvey() {
        try {
            System.out.println("Tiendas seleccionadas: " + selectedTiendas.size());

            InitialContext context = new InitialContext();
            TiendaFacade tiendaFacade = (TiendaFacade) context.lookup("java:module/TiendaFacade");
            HttpURLConnectionEncuestas httpURLConnection = (HttpURLConnectionEncuestas) context.lookup("java:module/HttpURLConnectionEncuestas");

            //Obtengo las Tiendas Seleccionadas
            getSelectedTiendas();

            if (selectedTiendas.size() > 0) {
                for (int i = 0; i < selectedTiendas.size(); i++) {
                    String url = null;
                    String hostname;
                    //Busco la Tienda seleccionada
                    int idTiendaSelected = Integer.parseInt(selectedTiendas.get(i));
                    tienda = tiendaFacade.find(idTiendaSelected);

                    if (tienda != null) {
                        //Inicializo el Status de Conexión en False
                        //Con esto se verifica la Conexión a la Tienda
                        httpURLConnection.setStatusConnection(false);
                        //Seteo el id de la Sucursal (Tienda)
                        tiendaId = tienda.getSucursal();
                        hostname = tienda.getUrl();
                        if (!hostname.equals("") && hostname != null) {
                            //Construyo el URL
                            url = hostname + "/" + JpaUtilities.COMMON_PATH + "/" + JpaUtilities.OBTENER_RESULTADOS_ENCUESTA + "/" + current.getId();

                            //Llamo al método del WS para enviar la Encuesta
                            //a la Tienda seleccionada
                            httpURLConnection.getResultsSurveyWS(url, current, tienda);

                        } else {
                            JsfUtil.addErrorMessage("En Configuración de Tiendas verifique el URL de la Tienda: " + tienda.getNombre());
                        }
                    }
                    if (!httpURLConnection.getStatusConnection()) {
                        JsfUtil.addErrorMessage("Problemas al Conectarse con la Tienda: " + tienda.getNombre());
                    } else {
                        JsfUtil.addSuccessMessage("Encuesta enviada con éxito. Conexión exitosa con Tienda: " + tienda.getNombre() + " !");
                    }
                }

            }

            //JsfUtil.addSuccessMessage("Encuesta enviada con éxito");
            return prepareSend();
        } catch (Exception e) {
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
     * Lista de Preguntas (con lista de Respuestas por cada Pregunta) de la
     * Encuesta Seleccionada
     *
     * @return
     */
    public List<Pregunta> getPreguntaList() throws NamingException {
        preguntaList = null;
        opcionsList = null;

        InitialContext context = new InitialContext();
        PreguntaFacade preguntaFacade = (PreguntaFacade) context.lookup("java:module/PreguntaFacade");
        RespuestaConfFacade respuestaConfFacade = (RespuestaConfFacade) context.lookup("java:module/RespuestaConfFacade");

        if (current != null && preguntaList == null) {
            preguntaList = preguntaFacade.findAll(current);
            for (Pregunta pregunta : preguntaList) {
                opcionsList = respuestaConfFacade.findRespuestaConf(pregunta);
                pregunta.setRespuestaConfList(opcionsList);
            }
        }

        return preguntaList;

    }
}
