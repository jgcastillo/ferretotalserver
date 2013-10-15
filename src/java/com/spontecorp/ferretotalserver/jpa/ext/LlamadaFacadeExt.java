package com.spontecorp.ferretotalserver.jpa.ext;

import com.spontecorp.ferretotalserver.controller.reporte.ReporteHelper;
import com.spontecorp.ferretotalserver.entity.Llamada;
import com.spontecorp.ferretotalserver.entity.Tienda;
import com.spontecorp.ferretotalserver.jpa.LlamadaFacade;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author jgcastillo
 */
public class LlamadaFacadeExt extends LlamadaFacade {
    //@PersistenceContext(unitName = "FerreAsesorWebPU")

    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("FerreAsesorServerPU");
    private EntityManager em = emf.createEntityManager();
    private static final Logger logger = LoggerFactory.getLogger(LlamadaFacadeExt.class);

    /**
     * Devuelve una lista de llamadas entre dos fechas, dependiendo del query
     * que reciba
     * @param tipo
     * @param tienda
     * @param fechaInicio
     * @param fechaFin
     * @return 
     */
    public List<Object[]> findLlamadas(int tipo, Tienda tienda, Date fechaInicio, Date fechaFin) {
        String query = "";
        switch (tipo) {

            case ReporteHelper.LLAMADAS_TOTALES_TIENDA:
                query = "SELECT ll.fechaClose, count(ll), ll.tiendaId FROM Llamada ll "
                        + "WHERE ll.fechaClose >= :fechaInicio AND ll.fechaClose <= :fechaFin "
                        + "AND ll.tiendaId = :tienda GROUP BY ll.fechaClose ORDER BY ll.fechaClose DESC";
                break;

            case ReporteHelper.LLAMADAS_DISPOSITIVO_TIENDA:
                query = "SELECT ll.dispositivo, COUNT(ll), ll.tiendaId FROM Llamada ll "
                        + "WHERE ll.tiendaId = :tienda AND ll.fechaClose >= :fechaInicio AND ll.fechaClose <= :fechaFin "
                        + "GROUP BY ll.dispositivo";
                break;

            case ReporteHelper.LLAMADAS_ASESOR_TIENDA:
                query = "SELECT ll.asesor, COUNT(ll), ll.tiendaId FROM Llamada ll "
                        + "WHERE ll.tiendaId = :tienda AND ll.fechaClose >= :fechaInicio AND ll.fechaClose <= :fechaFin "
                        + "GROUP BY ll.asesor";
                break;
            case ReporteHelper.CALIDAD_TOTAL_TIENDA:
                query = "SELECT ll.calidad, COUNT(ll), ll.tiendaId FROM Llamada ll "
                        + "WHERE ll.tiendaId = :tienda AND ll.fechaClose >= :fechaInicio AND ll.fechaClose <= :fechaFin "
                        + "GROUP BY ll.calidad";
                break;

        }

        List<Object[]> result = null;
        try {
            Query q = em.createQuery(query);
            q.setParameter("fechaInicio", fechaInicio);
            q.setParameter("fechaFin", fechaFin);
            q.setParameter("tienda", tienda);
            result = q.getResultList();
        } catch (Exception e) {
            logger.error("Error generando los datos: " + e);
        }
        return result;
    }

    /**
     * Devuelve una lista de llamadas entre dos fechas 
     * Para la Tienda seleccionada
     * @param tienda
     * @param fechaInicio
     * @param fechaFin
     * @return 
     */
    public List<Llamada> findLlamadasList(Tienda tienda, Date fechaInicio, Date fechaFin) {
        List<Llamada> result = null;
        String query = "SELECT ll FROM Llamada ll WHERE ll.tiendaId = :tienda AND "
                + "ll.fechaClose >= :fechaInicio AND ll.fechaClose <= :fechaFin ORDER BY ll.id";
        try {
            Query q = em.createQuery(query);
            q.setParameter("fechaInicio", fechaInicio);
            q.setParameter("fechaFin", fechaFin);
            q.setParameter("tienda", tienda);
            result = q.getResultList();
        } catch (Exception e) {
            logger.error("Error generando los datos: " + e);
        }
        return result;
    }

}
