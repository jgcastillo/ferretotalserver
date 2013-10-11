package com.spontecorp.ferreasesor.jpa.ext;

import com.spontecorp.ferreasesor.controller.reporte.ReporteHelper;
import com.spontecorp.ferreasesor.entity.Boton;
import com.spontecorp.ferreasesor.entity.Llamada;
import com.spontecorp.ferreasesor.jpa.LlamadaFacade;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
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
@Stateless
public class LlamadaFacadeExt extends LlamadaFacade {
    //@PersistenceContext(unitName = "FerreAsesorWebPU")

    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("FerreAsesorWebPU");
    private EntityManager em = emf.createEntityManager();
    private static final Logger logger = LoggerFactory.getLogger(LlamadaFacadeExt.class);

    /**
     * Listado de las ultimas 75 Llamadas cerradas accion = 0
     *
     * @return
     */
    public List<Llamada> findLastCalls() {
        String query = "SELECT ll from Llamada ll where ll.accion = '0' ORDER BY ll.id DESC";
        Query q = getEntityManager().createQuery(query);
        q.setMaxResults(75);
        return q.getResultList();
    }

    public List<Llamada> findLlamadasTiempo(Date fechaInicio, Date fechaFin) {
        EntityManager em = getEntityManager();
        List<Llamada> result = null;
        try {
            String query = "SELECT ll "
                    + "FROM Llamada ll , Distribucion d, Tiempo t WHERE ll.distribucionId.id = d.id AND ll.tiempoId.id = t.id "
                    + "AND ll.fechaClose = :fechaInicio AND ll.fechaClose = :fechaFin AND ll.accion = '0'"
                    + "ORDER BY ll.id";

            Query q = em.createQuery(query);
            q.setParameter("fechaInicio", fechaInicio);
            q.setParameter("fechaFin", fechaFin);
            result = q.getResultList();
        } catch (Exception e) {
            System.out.println("El error es: " + e);
        }
        return result;
    }

    /**
     * Selecciono la última Llamada abierta
     *
     * @param boton
     * @return
     */
    public Llamada findLlamadaAbierta(Boton boton) {
        EntityManager em = getEntityManager();
        Llamada llamada = null;
        try {
            String q = "SELECT ll From Llamada ll INNER JOIN ll.distribucionId d "
                    + "WHERE (ll.accion = :accion OR ll.accion = :accion1) AND d.botonId = :boton"
                    + " ORDER BY ll.id DESC";
            Query query = em.createQuery(q);
            query.setParameter("accion", 2);
            query.setParameter("accion1", 1);
            query.setParameter("boton", boton.getId());

            llamada = (Llamada) query.getResultList().get(0);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return llamada;
    }

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
     * Devuelve una lista de llamadas cuya fecha de inicio sea igual o mayor
     * y su hora de inicio sea superios a los parámetros recibidos
     * @param fechaInicio
     * @param horaInicio
     * @return 
     */
    public List<Llamada> findLlamadasUpdateList(Date fechaInicio, Date horaInicio) {
        List<Llamada> result = null;
        String query = "SELECT ll "
                + "FROM Llamada ll , Distribucion d, Tiempo t WHERE ll.distribucionId.id = d.id AND ll.tiempoId.id = t.id "
                + "AND ((ll.fechaOpen >= :fechaInicio AND ll.horaOpen > :horaInicio) OR (ll.fechaOpen > :fechaInicio)) AND ll.accion = '0' "
                + "ORDER BY ll.id";
        try {
            Query q = em.createQuery(query);
            q.setParameter("fechaInicio", fechaInicio);
            q.setParameter("horaInicio", horaInicio);
            result = q.getResultList();
        } catch (Exception e) {
            logger.error("Error generando los datos: " + e);
        }
        return result;
    }

    /**
     * Devuelve una lista de llamadas, excluyendo las llamadas que no han sido
     * cerradas
     *
     * @return La lista de llamadas
     */
    public List<Llamada> findLlamadasList() {
        List<Llamada> result = null;
        String query = "SELECT ll "
                + "FROM Llamada ll , Distribucion d, Tiempo t WHERE ll.distribucionId.id = d.id AND ll.tiempoId.id = t.id "
                + "AND ll.accion = '0'"
                + "ORDER BY ll.id";
        try {
            Query q = em.createQuery(query);
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
