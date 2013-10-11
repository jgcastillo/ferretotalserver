package com.spontecorp.ferreasesor.controller.encuesta;

import com.spontecorp.ferreasesor.controller.reporte.JasperBeanEncuestas;
import com.spontecorp.ferreasesor.controller.reporte.JasperManagement;
import com.spontecorp.ferreasesor.controller.util.JsfUtil;
import com.spontecorp.ferreasesor.entity.Encuesta;
import com.spontecorp.ferreasesor.entity.Numericas;
import com.spontecorp.ferreasesor.entity.Pregunta;
import com.spontecorp.ferreasesor.entity.RespuestaConf;
import com.spontecorp.ferreasesor.entity.RespuestaObtenida;
import com.spontecorp.ferreasesor.jpa.EncuestaFacade;
import com.spontecorp.ferreasesor.jpa.PreguntaFacade;
import com.spontecorp.ferreasesor.jpa.RespuestaConfFacade;
import com.spontecorp.ferreasesor.jpa.RespuestaObtenidaFacade;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import net.sf.jasperreports.engine.JRException;
import org.primefaces.model.chart.CartesianChartModel;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.chart.PieChartModel;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Casper
 */
@ManagedBean(name = "preguntaBean")
@SessionScoped
public class PreguntaBeanController implements Serializable {

    private Pregunta current;
    private Pregunta selectedQuestion;
    private Pregunta selectedQuestionTextual;
    private Pregunta selectedQuestionExport;
    private RespuestaConf respuesta;
    private String preguntaTexto;
    private String promptPreguntaTextual;
    private String respuestaTexto;
    private String respuestaNumeric;
    private String preguntaSeleccion;
    private String preguntaSeleccionItem;
    private List<String> preguntaSeleccionValores;
    private List<Pregunta> preguntaList = null;
    private List<Pregunta> preguntas = null;
    private String respuestaList[] = new String[25];
    private List<RespuestaConf> opcionsList = null;
    private Integer respuestaSeleccion;
    private Integer respuestaRating;
    private int tipoPregunta;
    private Encuesta encuesta;
    @EJB
    private PreguntaFacade preguntaFacade;
    @EJB
    private RespuestaConfFacade respuestaFacade;
    @EJB
    private EncuestaFacade encuestaFacade;
    @EJB
    private RespuestaObtenidaFacade respObtenidaFacade;
    private DataModel<Pregunta> preguntaItems;
    private Map<Pregunta, List<RespuestaConf>> respuestas;
    private static final int ENCUESTA_ACTIVA = 1;
    //Tipos de Preguntas
    private static final int TEXTUAL = 1;
    private static final int NUMERICO = 2;
    private static final int SELECCION = 3;
    private static final int CALIFICACION = 4;
    private boolean message1 = false;
    private boolean message2 = false;
    private boolean message3 = false;
    //Datos para los Gráficos y Reportes
    protected boolean showChart = false;
    private CartesianChartModel categoryModel;
    private PieChartModel categoryModelPie;
    private String nombreReporte;
    private String nombreRango = "Opción";
    private String nombreDominio = "Cantidad";

    public PreguntaBeanController() {
    }

    private PreguntaFacade getPreguntaFacade() {
        return preguntaFacade;
    }

    private RespuestaConfFacade getRespuestaFacade() {
        return respuestaFacade;
    }

    public EncuestaFacade getEncuestaFacade() {
        return encuestaFacade;
    }

    public RespuestaObtenidaFacade getRespObtenidaFacade() {
        return respObtenidaFacade;
    }

    public String getRespuestaTexto() {
        return respuestaTexto;
    }

    public String[] getRespuestaList() {
        return respuestaList;
    }

    public void setRespuestaTexto(String respuestaTexto) {
        this.respuestaTexto = respuestaTexto;
    }

    public String getPreguntaTexto() {
        return preguntaTexto;
    }

    public void setPreguntaTexto(String preguntaTexto) {
        this.preguntaTexto = preguntaTexto;
    }

    public int getTipoPregunta() {
        return tipoPregunta;
    }

    public void setTipoPregunta(int tipoPregunta) {
        this.tipoPregunta = tipoPregunta;
    }

    public String getPromptPreguntaTextual() {
        return promptPreguntaTextual;
    }

    public void setPromptPreguntaTextual(String promptPreguntaTextual) {
        this.promptPreguntaTextual = promptPreguntaTextual;
    }

    public String getRespuestaNumeric() {
        return respuestaNumeric;
    }

    public void setRespuestaNumeric(String respuestaNumeric) {
        this.respuestaNumeric = respuestaNumeric;
    }

    public String getPreguntaSeleccionItem() {
        return preguntaSeleccionItem;
    }

    public void setPreguntaSeleccionItem(String preguntaSeleccionItem) {
        this.preguntaSeleccionItem = preguntaSeleccionItem;
    }

    public List<String> getPreguntaSeleccionValores() {
        return preguntaSeleccionValores;
    }

    public void setPreguntaSeleccionValores(List<String> preguntaSeleccionValores) {
        this.preguntaSeleccionValores = preguntaSeleccionValores;
    }

    public String getPreguntaSeleccion() {
        return preguntaSeleccion;
    }

    public void setPreguntaSeleccion(String preguntaSeleccion) {
        this.preguntaSeleccion = preguntaSeleccion;
    }

    public Integer getRespuestaSeleccion() {
        return respuestaSeleccion;
    }

    public void setRespuestaSeleccion(Integer respuestaSeleccion) {
        this.respuestaSeleccion = respuestaSeleccion;
    }

    public Integer getRespuestaRating() {
        return respuestaRating;
    }

    public void setRespuestaRating(Integer respuestaRating) {
        this.respuestaRating = respuestaRating;
    }

    public Encuesta getEncuestaActiva() {
        encuesta = getEncuestaFacade().findAll(ENCUESTA_ACTIVA);
        return encuesta;
    }

    public Encuesta getEncuesta() {
        return encuesta;
    }

    public void setEncuesta(Encuesta encuesta) {
        this.encuesta = encuesta;
    }

    public boolean isMessage1() {
        return message1;
    }

    public boolean isMessage2() {
        return message2;
    }

    public boolean isMessage3() {
        return message3;
    }

    public boolean isShowChart() {
        return showChart;
    }

    public CartesianChartModel getCategoryModel() {
        return categoryModel;
    }

    public PieChartModel getCategoryModelPie() {
        return categoryModelPie;
    }

    public Pregunta getSelectedQuestion() {
        return selectedQuestion;
    }

    public void setSelectedQuestion(Pregunta selectedQuestion) {
        this.selectedQuestion = selectedQuestion;
    }

    public Pregunta getSelectedQuestionTextual() {
        return selectedQuestionTextual;
    }

    public void setSelectedQuestionTextual(Pregunta selectedQuestionTextual) {
        this.selectedQuestionTextual = selectedQuestionTextual;
    }

    public Pregunta getSelectedQuestionExport() {
        return selectedQuestionExport;
    }

    public void setSelectedQuestionExport(Pregunta selectedQuestionExport) {
        this.selectedQuestionExport = selectedQuestionExport;
    }

    public String getNombreReporte() {
        return nombreReporte;
    }

    public void setNombreReporte(String nombreReporte) {
        this.nombreReporte = nombreReporte;
    }

    /**
     * Listado de Preguntas para Analizar las Encuestas
     *
     * @return
     */
    public List<Pregunta> getPreguntas() {
        preguntas = null;
        opcionsList = null;
        categoryModel = null;
        categoryModelPie = null;

        if (preguntas == null) {
            preguntas = new ArrayList(getPreguntaFacade().findAll(encuesta));
            for (Pregunta pregunta : preguntas) {
                List<RespuestaObtenida> respList = null;
                List<RespuestaConf> options = null;
                List<Numericas> listaNumericas = new ArrayList<>();
                List<Integer> listRespObtenidas = new ArrayList<>();

                if (respList == null) {
                    respList = getRespObtenidaFacade().findRespuestaObtenidaList(pregunta);
                }

                //Si la Pregunta es de Tipo Numérica
                if (pregunta.getTipo() == 2) {
                    Map<Integer, Integer> mapNumeric = new HashMap<>();

                    for (int i = 0; i < respList.size(); i++) {
                        mapNumeric.put(Integer.valueOf(respList.get(i).getRespuesta()), 0);
                    }

                    if (respList.isEmpty()) {
                        Numericas numericas = new Numericas();
                        numericas.setNombre("0");
                        numericas.setCantidad(0);
                        listaNumericas.add(numericas);
                    }

                    int i = 0;
                    int cant = 0;
                    for (Map.Entry<Integer, Integer> mapa : mapNumeric.entrySet()) {
                        cant = getRespObtenidaFacade().findCantidadRespuestaObtenida(pregunta, (String) mapa.getKey().toString());
                        Numericas numericas = new Numericas();
                        numericas.setNombre((String) mapa.getKey().toString());
                        numericas.setCantidad(cant);
                        listaNumericas.add(numericas);
                        i++;
                    }
                }

                //Si la Pregunta es de Tipo Selección
                if (pregunta.getTipo() == 3) {
                    if (options == null) {
                        options = getRespuestaFacade().findRespuestaConf(pregunta);
                        pregunta.setRespuestaConfList(options);
                    }
                    for (RespuestaConf option : options) {
                        int count = 0;
                        Numericas numericas = new Numericas();
                        numericas.setNombre(option.getOpcion());
                        for (RespuestaObtenida resp : respList) {
                            if (option.getId() == resp.getRespuestaConfId().getId()) {
                                option.setTotalOptions(++count);
                            }
                            numericas.setCantidad(count);
                        }
                        listaNumericas.add(numericas);
                    }
                }

                //Si la Pregunta es de Tipo Calificación
                if (pregunta.getTipo() == 4) {
                    Map<Integer, Integer> mapCalific = new HashMap<>();
                    int[] counts = new int[10];
                    for (int i = 1; i < 11; i++) {
                        mapCalific.put(i, 0);
                        counts[i - 1] = 0;
                    }

                    int j = 1;
                    int x = 0;
                    for (RespuestaObtenida resp : respList) {
                        switch (resp.getRespuesta()) {
                            case "1":
                                counts[0] = mapCalific.get(1);
                                mapCalific.put(1, ++counts[0]);
                                break;
                            case "2":
                                counts[1] = mapCalific.get(2);
                                mapCalific.put(2, ++counts[1]);
                                break;
                            case "3":
                                counts[2] = mapCalific.get(3);
                                mapCalific.put(3, ++counts[2]);
                                break;
                            case "4":
                                counts[3] = mapCalific.get(4);
                                mapCalific.put(4, ++counts[3]);
                                break;
                            case "5":
                                counts[4] = mapCalific.get(5);
                                mapCalific.put(5, ++counts[4]);
                                break;
                            case "6":
                                counts[5] = mapCalific.get(6);
                                mapCalific.put(6, ++counts[5]);
                                break;
                            case "7":
                                counts[6] = mapCalific.get(7);
                                mapCalific.put(7, ++counts[6]);
                                break;
                            case "8":
                                counts[7] = mapCalific.get(8);
                                mapCalific.put(8, ++counts[7]);
                                break;
                            case "9":
                                counts[8] = mapCalific.get(9);
                                mapCalific.put(9, ++counts[8]);
                                break;
                            case "10":
                                counts[9] = mapCalific.get(10);
                                mapCalific.put(10, ++counts[9]);
                                break;
                        }
                    }

                    int i = 0;
                    for (Map.Entry<Integer, Integer> mapa : mapCalific.entrySet()) {
                        listRespObtenidas.add(mapa.getValue());
                        Numericas numericas = new Numericas();
                        numericas.setNombre((String) mapa.getKey().toString());
                        numericas.setCantidad(mapa.getValue());
                        listaNumericas.add(numericas);
                        i++;
                    }
                }
                pregunta.setTotalRespuestas(respList.size());
                pregunta.setRespuestaObtenidaList(respList);
                pregunta.setListRespObtenidas(listRespObtenidas);
                pregunta.setListaNumericas(listaNumericas);
                categoryModel = createCategoryModel(listaNumericas);
                categoryModelPie = createCategoryModelPie(listaNumericas);
                pregunta.setCategoryModel(categoryModel);
                pregunta.setCategoryModelPie(categoryModelPie);
            }
        }
        return preguntas;
    }

    /**
     * Listado de Preguntas para llenar la Tabla
     *
     * @return
     */
    public DataModel getPreguntaItems() {
        //recreateModel();
        preguntaItems = null;
        opcionsList = null;
        //categoryModel = null;

        if (preguntaItems == null) {
            preguntaItems = new ListDataModel(getPreguntaFacade().findAll(encuesta));
        }
        return preguntaItems;
    }

    /**
     * Metodo para Generar el Grafico de Barras en PrimeFaces
     */
    public CartesianChartModel createCategoryModel(List<Numericas> listaNumericas) {
        categoryModel = new CartesianChartModel();

        ChartSeries cant = new ChartSeries("Opción");

        for (Numericas data : listaNumericas) {
            cant.set(data.getNombre(), data.getCantidad());
        }
        categoryModel.addSeries(cant);

        return categoryModel;

    }

    /**
     * Metodo para Generar el Grafico de Tortas en PrimeFaces
     */
    public PieChartModel createCategoryModelPie(List<Numericas> listaNumericas) {
        categoryModelPie = new PieChartModel();

        for (Numericas data : listaNumericas) {
            categoryModelPie.set(data.getNombre(), data.getCantidad());
        }

        return categoryModelPie;

    }

    /**
     * Listado de Preguntas
     *
     * @return
     */
    public List<Pregunta> getPreguntaList() {
        encuesta = getEncuestaActiva();
        preguntaList = null;
        opcionsList = null;

        if (encuesta != null && preguntaList == null && opcionsList == null) {
            preguntaSeleccionValores = new ArrayList();
            preguntaList = getPreguntaFacade().findAll(encuesta);
            for (Pregunta pregunta : preguntaList) {
                opcionsList = getRespuestaFacade().findRespuestaConf(pregunta);
                pregunta.setRespuestaConfList(opcionsList);
            }
        }

        return preguntaList;

    }

    /**
     * Metodo para Guardar Respuesta por Preguntas
     *
     * @param actionEvent
     * @return
     */
    public String guardaRespuesta() {

        for (int i = 0; i < preguntaList.size(); i++) {
            respuestaTexto = respuestaList[i].toString();

            RespuestaObtenida respObtenida = new RespuestaObtenida();

            if (!respuestaTexto.equals("")) {
                if (preguntaList.get(i).getTipo() == SELECCION) {
                    respuesta = getRespuestaFacade().find(Integer.valueOf(respuestaTexto));
                    respObtenida.setRespuesta(null);
                } else {
                    respuesta = preguntaList.get(i).getRespuestaConfList().get(0);
                    respObtenida.setRespuesta(respuestaTexto);
                }

                respObtenida.setRespuestaConfId(respuesta);
                respObtenida.setEncuestaId(encuesta);
                respObtenida.setPreguntaId(preguntaList.get(i));
                respObtenidaFacade.create(respObtenida);
            }
        }
        for (int i = 0; i < preguntaList.size(); i++) {
            respuestaList[i] = "";
        }

        setRespuestaTexto(null);
        return showMessage();
        //JsfUtil.addSuccessMessage("La Encuesta fue enviada con éxito!");
        //return "message?faces-redirect=true";
    }

    public String showMessage() {
        //System.out.println("Entro a ShowMessage()");
        return "message";
    }

    /**
     * Configurar Preguntas
     *
     * @return
     */
    public String configuraPregunta() {
        //promptPreguntaTextual = "";
        //setPromptPreguntaTextual(promptPreguntaTextual);
        //System.out.println("Pregunta en configuraPregunta: "+preguntaTexto);
        preguntaSeleccionValores = new ArrayList();
        String next;

        if (tipoPregunta == 1 || tipoPregunta == 2) {
            next = "showQuestion";
        } else {
            next = "configQuestion";
        }
        resetMessage();
        return next;
    }

    /**
     *
     * @return
     */
    public String retornaCreate() {
        resetMessage();
        recreateModel();
        return "createQuestions";
    }

    /**
     *
     * @return
     */
    public String muestraPregunta() {
        return "showQuestion";
    }

    /**
     * Guardar Preguntas
     *
     * @return
     */
    public String guardaPregunta() {
        preguntaFacade = getPreguntaFacade();
        respuestaFacade = getRespuestaFacade();
        current = new Pregunta();
        current.setEncuestaId(encuesta);
        current.setPregunta(preguntaTexto);
        current.setTipo(tipoPregunta);

        if (encuesta != null) {
            preguntaFacade.create(current);
            switch (tipoPregunta) {
                case 1:
                case 2:
                case 4: {
                    respuesta = new RespuestaConf();
                    respuesta.setPreguntaId(current);
                    respuesta.setPrompt(promptPreguntaTextual);
                    respuestaFacade.create(respuesta);
                    break;
                }
                case 3: {
                    for (String opcion : preguntaSeleccionValores) {
                        respuesta = new RespuestaConf();
                        respuesta.setPreguntaId(current);
                        respuesta.setOpcion(opcion);
                        respuesta.setPrompt(promptPreguntaTextual);
                        respuestaFacade.create(respuesta);
                    }
                    break;
                }
            }
            JsfUtil.addSuccessMessage("Pregunta agregada con éxito");
        }
        recreateModel();
        resetMessage();
        message3 = true;
        return "createQuestions";
    }

    /**
     * Añadir Opciones a las Preguntas del Tipo Seleccion
     *
     * @param event
     */
    public void addItemToSeleccion(ActionEvent event) {
        if (preguntaSeleccionValores.add(preguntaSeleccionItem)) {
            preguntaSeleccionItem = null;
            JsfUtil.addSuccessMessage("Opción agregada con éxito");
        } else {
            JsfUtil.addErrorMessage("No se pudo agregar Opción");
        }
    }

    /**
     * Remover Opciones a las Preguntas del Tipo Seleccion
     */
    public void removeItemFromSeleccion() {
        if (preguntaSeleccionValores.remove(preguntaSeleccionItem)) {
            preguntaSeleccionItem = null;
            JsfUtil.addSuccessMessage("Opción eliminada con éxito");
        } else {
            JsfUtil.addErrorMessage("No se pudo eliminar la Opción");
        }
    }

    /**
     * Opciones de las Preguntas del Tipo Seleccion
     *
     * @return
     */
    public Map<String, Integer> getOpcionesSeleccion() {
        Map<String, Integer> opciones = new LinkedHashMap();
        //List<SelectItem> opciones = new ArrayList<SelectItem>();
        int i = 0;
        for (String opcion : preguntaSeleccionValores) {
            opciones.put(opcion, i);
            i++;
        }
        return opciones;
    }

    /**
     *
     * @return
     */
    public String prepareCancel() {
        //setEncuesta(null);
        current = null;
        recreateModel();
        return "createQuestions";
    }

    /**
     *
     * @return
     */
    public String prepareCreate() {
        recreateModel();
        return "createQuestions";
    }

    /**
     *
     * @return
     */
    public String prepareDelete() {
        preguntaSeleccionValores = new ArrayList();
        current = (Pregunta) getPreguntaItems().getRowData();
        tipoPregunta = current.getTipo();
        preguntaTexto = current.getPregunta();
        for (RespuestaConf opcionConf : current.getRespuestaConfList()) {
            promptPreguntaTextual = opcionConf.getPrompt();
            preguntaSeleccionValores.add(opcionConf.getOpcion());
        }
        resetMessage();
        message3 = false;
        return "deleteQuestions";
    }

    /**
     * Eliminar Preguntas
     *
     * @return
     */
    public String deletePregunta() {
        respuestaFacade = getRespuestaFacade();
        preguntaFacade = getPreguntaFacade();

        int totalRespuestas = 0;
        totalRespuestas = respObtenidaFacade.findRespuestaObtenida(current);

        if (totalRespuestas == 0) {
            opcionsList = respuestaFacade.findRespuestaConf(current);
            current.setRespuestaConfList(opcionsList);
            for (RespuestaConf opcionConf : current.getRespuestaConfList()) {
                respuestaFacade.remove(opcionConf);
            }
            //current.setRespuestaConfList(null);
            preguntaFacade.remove(current);
            //tipoPregunta = 0;
            //preguntaTexto = null;
            //promptPreguntaTextual = null;
            //preguntaSeleccionValores.clear();
            message2 = false;
            message1 = true;
            JsfUtil.addSuccessMessage("Pregunta eliminada de la encuesta");
        } else if (totalRespuestas > 0) {
            JsfUtil.addErrorMessage("La pregunta no puede ser eliminada, ya que tiene respuestas asociadas");
            message1 = false;
            message2 = true;
        }
        recreateModel();
        return prepareCreate();
    }

    private void resetMessage() {
        message1 = false;
        message2 = false;
    }

    /**
     * Limpiar Variables para mostrar Listados actualizados
     */
    private void recreateModel() {
        preguntaTexto = null;
        tipoPregunta = 0;
        preguntaItems = null;
        promptPreguntaTextual = null;
        message3 = false;
    }

    /**
     * Exportar Reportes a PDF (Gráfico Bar)
     *
     * @throws JRException
     * @throws IOException
     */
    public void exportarReportePDFBar() throws JRException, IOException {
        String extension = "PDF";
        String jasperFileAddress = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/resources/reports/encuesta/reportepdf.jasper");
        exportarReporte(extension, jasperFileAddress);

    }

    /**
     * Exportar Reportes a PDF (Gráfico Pie)
     *
     * @throws JRException
     * @throws IOException
     */
    public void exportarReportePDFPie() throws JRException, IOException {

        String extension = "PDF";
        String jasperFileAddress = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/resources/reports/encuesta/reportepdfpie.jasper");
        exportarReporte(extension, jasperFileAddress);

    }
    
    /**
     * Exportar Reportes a PDF (Sólo para Preguntas de tipo Textual)
     * @throws JRException
     * @throws IOException 
     */
    public void exportarReportePDFTextual() throws JRException, IOException {
        String extension = "PDF";
        String jasperFileAddress = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/resources/reports/encuesta/reportepdfTextual.jasper");
        exportarReporteTextual(extension, jasperFileAddress);

    }
    
     /**
     * Exportar Reportes a PDF (Sólo para Preguntas de tipo Textual)
     * @throws JRException
     * @throws IOException 
     */
    public void exportarReporteXLSTextual() throws JRException, IOException {
        String extension = "XLS";
        String jasperFileAddress = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/resources/reports/encuesta/reportexlsTextual.jasper");
        exportarReporteTextual(extension, jasperFileAddress);

    }

    /**
     * Exportar Reportes a Excel
     *
     * @throws JRException
     * @throws IOException
     */
    public void exportarReporteXLS() throws JRException, IOException {
        String extension = "XLS";
        String jasperFileAddress = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/resources/reports/encuesta/reportexls.jasper");
        exportarReporte(extension, jasperFileAddress);

    }

    /**
     * Método para exportar Reportes de Encuestas Para Preguntas de Tipo
     * Númerico, Selección y Calificación
     *
     * @param extension
     * @param jasperFileAddress
     * @throws JRException
     * @throws IOException
     */
    public void exportarReporte(String extension, String jasperFileAddress) throws JRException, IOException {

        JasperManagement jm = new JasperManagement();
        List<Numericas> myList;
        String logoAddress = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/resources/reports/ferretotallogo.jpg");

        try {

            //Revisa los casos y llena la lista de jasperbean
            myList = selectedQuestion.getListaNumericas();
            nombreReporte = selectedQuestion.getEncuestaId().getNombre();
            String pregunta = selectedQuestion.getPregunta();
            int totalRespuestas = selectedQuestion.getTotalRespuestas();

            Map parametros = new HashMap();
            parametros.put("pregunta", pregunta);
            parametros.put("nombrereporte", nombreReporte);
            parametros.put("totalRespuestas", totalRespuestas);
            parametros.put("nombre", nombreRango);
            parametros.put("cantidad", nombreDominio);
            parametros.put("logo", logoAddress);

            nombreReporte = nombreReporte + " - " + pregunta;
            jm.FillReportEncuesta(parametros, myList, extension, jasperFileAddress, nombreReporte);

        } catch (JRException e) {
            Logger.getLogger(PreguntaBeanController.class.getName()).log(Level.SEVERE, null, e);
        }

    }

    /**
     * Método para exportar Reportes de Encuestas Sólo para Preguntas de tipo
     * Textual
     *
     * @param extension
     * @param jasperFileAddress
     * @throws JRException
     * @throws IOException
     */
    public void exportarReporteTextual(String extension, String jasperFileAddress) throws JRException, IOException {

        JasperManagement jm = new JasperManagement();
        List<JasperBeanEncuestas> myList;
        String logoAddress = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/resources/reports/ferretotallogo.jpg");

        try {

            //Revisa los casos y llena la lista de jasperbean

            List<RespuestaObtenida> respList = selectedQuestionTextual.getRespuestaObtenidaList();

            myList = jm.FillListEncuesta(respList);

            nombreReporte = selectedQuestionTextual.getEncuestaId().getNombre();
            nombreRango = "Respuestas Obtenidas";
            String pregunta = selectedQuestionTextual.getPregunta();
            int totalRespuestas = selectedQuestionTextual.getTotalRespuestas();

            Map parametros = new HashMap();
            parametros.put("pregunta", pregunta);
            parametros.put("nombrereporte", nombreReporte);
            parametros.put("nombre", nombreRango);
            parametros.put("totalRespuestas", totalRespuestas);
            parametros.put("logo", logoAddress);

            nombreReporte = nombreReporte + " - " + pregunta;
            jm.FillReportTextualEncuesta(parametros, myList, extension, jasperFileAddress, nombreReporte);

        } catch (JRException e) {
            Logger.getLogger(PreguntaBeanController.class.getName()).log(Level.SEVERE, null, e);
        }

    }
}
