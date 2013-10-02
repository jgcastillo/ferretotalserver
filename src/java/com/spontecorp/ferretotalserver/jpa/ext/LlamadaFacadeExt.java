package com.spontecorp.ferretotalserver.jpa.ext;

import com.spontecorp.ferretotalserver.controller.reporte.ReporteHelper;
import com.spontecorp.ferretotalserver.entity.Llamada;
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

    public List<Object[]> findLlamadas(Date fechaInicio, Date fechaFin) {
        String q = "SELECT ll.fechaClose, count(ll) FROM Llamada ll "
                + "WHERE ll.accion = '0' AND ll.fechaClose >= :fechaInicio AND ll.fechaClose <= :fechaFin "
                + "GROUP BY ll.fechaClose";
        Query query = em.createQuery(q);
        query.setParameter("fechaInicio", fechaInicio);
        query.setParameter("fechaFin", fechaFin);
        return query.getResultList();
    }

    /**
     * Devuelve una lista de llamadas entre dos fechas, dependiendo del query
     * que reciba
     *
     * @param tipo de reporte
     * @param fechaInicio
     * @param fechaFin
     * @return
     */
    public List<Object[]> findLlamadas(int tipo, Date fechaInicio, Date fechaFin) {
        String query = "";
        switch (tipo) {
            case ReporteHelper.LLAMADAS_TOTALES:
                query = "SELECT ll.fechaClose, count(ll) FROM Llamada ll "
                        + "WHERE ll.accion = '0' AND ll.fechaClose >= :fechaInicio AND ll.fechaClose <= :fechaFin "
                        + "GROUP BY ll.fechaClose";
                break;
            case ReporteHelper.LLAMADAS_DISPOSITIVO:
                query = "SELECT b, COUNT(ll) "
                        + "FROM Llamada ll, Distribucion d, Boton b "
                        + "WHERE ll.distribucionId.id = d.id AND d.botonId = b.id "
                        + "AND ll.accion = '0' AND ll.fechaClose >= :fechaInicio AND ll.fechaClose <= :fechaFin "
                        + "GROUP BY b.id";
                break;
            case ReporteHelper.LLAMADAS_ASESOR:
                query = "SELECT a, COUNT(ll) "
                        + "FROM Llamada ll, Distribucion d, Asesor a "
                        + "WHERE ll.distribucionId.id = d.id AND d.asesorId = a.id "
                        + "AND ll.accion = '0' AND ll.fechaClose >= :fechaInicio AND ll.fechaClose <= :fechaFin "
                        + "GROUP BY a.id";
                break;
            case ReporteHelper.LLAMADAS_TURNO:
                query = "SELECT t, COUNT(ll) "
                        + "FROM Llamada ll, Distribucion d, Turno t "
                        + "WHERE ll.distribucionId.id = d.id AND d.turnoId = t.id "
                        + "AND ll.accion = '0' AND ll.fechaClose >= :fechaInicio AND ll.fechaClose <= :fechaFin "
                        + "GROUP BY d.turnoId";
                break;
            case ReporteHelper.TIEMPOS_X_DISPOSITIVO:
                query = "SELECT ll, b "
                        + "FROM Llamada ll , Distribucion d, Boton b "
                        + "WHERE ll.distribucionId.id = d.id AND d.botonId = b.id "
                        + "AND ll.fechaClose >= :fechaInicio AND ll.fechaClose <= :fechaFin "
                        + "ORDER BY b.id";
                break;
            case ReporteHelper.TIEMPOS_X_FERREASESOR:
                query = "SELECT ll, a "
                        + "FROM Llamada ll, Distribucion d, Asesor a "
                        + "WHERE ll.distribucionId.id = d.id AND d.asesorId = a.id "
                        + "AND ll.fechaClose >= :fechaInicio AND ll.fechaClose <= :fechaFin "
                        + "ORDER BY a.id";
                break;
            case ReporteHelper.TIEMPOS_X_TURNO:
                query = "SELECT ll, t "
                        + "FROM Llamada ll, Distribucion d, Turno t "
                        + "WHERE ll.distribucionId.id = d.id AND d.turnoId = t.id "
                        + "AND ll.fechaClose >= :fechaInicio AND ll.fechaClose <= :fechaFin "
                        + "ORDER BY t.id";
                break;
            case ReporteHelper.CALIDAD_X_DISPOSITIVO:
                query = "SELECT ll, b, t "
                        + "FROM Llamada ll, Distribucion d, Boton b, Tiempo t "
                        + "WHERE ll.distribucionId.id = d.id AND t.turnoId.id = d.turnoId AND d.botonId = b.id "
                        + "AND ll.tiempoId.id = t.id AND ll.fechaClose >= :fechaInicio AND ll.fechaClose <= :fechaFin "
                        + "ORDER BY b.id";
                break;
            case ReporteHelper.CALIDAD_X_FERREASESOR:
                query = "SELECT ll, a, t "
                        + "FROM Llamada ll, Distribucion d, Asesor a, Tiempo t "
                        + "WHERE ll.distribucionId.id = d.id AND t.turnoId.id = d.turnoId AND ll.tiempoId.id = t.id "
                        + "AND d.asesorId = a.id AND ll.fechaClose >= :fechaInicio AND ll.fechaClose <= :fechaFin "
                        + "ORDER BY a.id";
                break;
            case ReporteHelper.CALIDAD_X_TURNO:
                query = "SELECT ll, tu , t "
                        + "FROM Llamada ll , Distribucion d, Turno tu, Tiempo t "
                        + "WHERE ll.distribucionId.id = d.id AND t.turnoId.id = d.turnoId AND d.turnoId = tu.id "
                        + "AND ll.tiempoId.id = t.id AND ll.fechaClose >= :fechaInicio AND ll.fechaClose <= :fechaFin "
                        + "ORDER BY tu.id";
                break;
        }

        List<Object[]> result = null;
        try {
            Query q = em.createQuery(query);
            q.setParameter("fechaInicio", fechaInicio);
            q.setParameter("fechaFin", fechaFin);
            result = q.getResultList();
        } catch (Exception e) {
            logger.error("Error generando los datos: " + e);
        }
        return result;
    }

    /**
     * Devuelve una lista de llamadas entre dos fechas
     *
     * @param fechaInicio
     * @param fechaFin
     * @return
     */
    public List<Llamada> findLlamadasList(Date fechaInicio, Date fechaFin) {
        List<Llamada> result = null;
        String query = "SELECT ll "
                + "FROM Llamada ll , Distribucion d, Tiempo t WHERE ll.distribucionId.id = d.id AND ll.tiempoId.id = t.id "
                + "AND ll.fechaClose >= :fechaInicio AND ll.fechaClose <= :fechaFin AND ll.accion = '0'"
                + "ORDER BY ll.id";
        try {
            Query q = em.createQuery(query);
            q.setParameter("fechaInicio", fechaInicio);
            q.setParameter("fechaFin", fechaFin);
            result = q.getResultList();
        } catch (Exception e) {
            logger.error("Error generando los datos: " + e);
        }
        return result;


    }

    /**
     * Cuenta las llamdas entre dos fechas
     *
     * @param fechaInicial
     * @param fechaFin
     * @return la cantidad de llamadas
     */
    public Long getLlamadaCount(Date fechaInicio, Date fechaFin) {
        String query = "SELECT COUNT(ll) FROM Llamada ll "
                + "WHERE ll.accion = '0' AND ll.fechaClose >= :fechaInicio AND ll.fechaClose <= :fechaFin ";
        Query q = em.createQuery(query);
        q.setParameter("fechaInicio", fechaInicio);
        q.setParameter("fechaFin", fechaFin);
        return (Long) q.getSingleResult();
    }

    /**
     * Cuenta la cantidad de llamdas cerradas entre dos fechas
     *
     * @param fechaInicial
     * @param fechaFin
     * @return
     */
    public Long getDiasEntreFechasCount(Date fechaInicial, Date fechaFin) {
        String query = "SELECT COUNT(DISTINCT ll.fechaClose) FROM Llamada ll "
                + "WHERE ll.accion = '0' AND ll.fechaClose >= :fechaInicio AND ll.fechaClose <= :fechaFin ";
        Query q = em.createQuery(query);
        q.setParameter("fechaInicio", fechaInicial);
        q.setParameter("fechaFin", fechaFin);
        return (Long) q.getSingleResult();
    }
}
