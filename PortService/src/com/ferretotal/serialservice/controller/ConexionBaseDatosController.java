package com.ferretotal.serialservice.controller;


import com.ferretotal.serialservice.utilidades.JpaUtilities;
import com.ferretotal.serialservice.controller.extensions.FeriadoJpaControllerExt;
import com.ferretotal.serialservice.controller.extensions.TurnoJpaControllerExt;
import com.ferretotal.serialservice.entity.Feriado;
import com.ferretotal.serialservice.entity.Tienda;
import com.ferretotal.serialservice.entity.Turno;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author jgcastillo
 */
public class ConexionBaseDatosController {

    private static Logger logger = LoggerFactory.getLogger(ConexionBaseDatosController.class);
    private TiendaJpaController controllerTienda = new TiendaJpaController(JpaUtilities.getEntityManagerFactory());
//    private TiempoJpaControllerExt controllerTiempo = new TiempoJpaControllerExt(JpaUtilities.getEntityManagerFactory());
    private TurnoJpaControllerExt controllerTurno = new TurnoJpaControllerExt(JpaUtilities.getEntityManagerFactory());
//    private UsuarioJpaController controllerUsuario = new UsuarioJpaController(JpaUtilities.getEntityManagerFactory());
//    private PerfilJpaController controllerPerfil = new PerfilJpaController(JpaUtilities.getEntityManagerFactory());
//    private AsesorJpaControllerExt controllerAsesor = new AsesorJpaControllerExt(JpaUtilities.getEntityManagerFactory());
//    private BotonJpaControllerExt controllerBoton = new BotonJpaControllerExt(JpaUtilities.getEntityManagerFactory());
//    private DistribucionJpaControllerExt controllerDist = new DistribucionJpaControllerExt(JpaUtilities.getEntityManagerFactory());
    private FeriadoJpaControllerExt controllerFeriado = new FeriadoJpaControllerExt(JpaUtilities.getEntityManagerFactory());

//    public List<Perfil> getPerfiles() {
//        return controllerPerfil.findPerfilEntities();
//    }
//
//    public void savePerfil(Perfil perfil) {
//        controllerPerfil.create(perfil);
//    }
//
//    public Perfil getAdminPerfil() {
//        return controllerPerfil.findPerfil(GeneralUtilities.ADMINISTRADOR);
//    }
//
//    public List<Usuario> getUsuarios() {
//        return controllerUsuario.findUsuarioEntities();
//    }
//
//    public void saveUsuario(Usuario usuario) {
//        controllerUsuario.create(usuario);
//    }
//
    public Tienda getTienda() {
        controllerTienda = new TiendaJpaController(JpaUtilities.getEntityManagerFactory());
        Tienda tienda = controllerTienda.findTienda(1);
//        List<Tienda> tiendas = controllerTienda.findTiendaEntities();
//        Tienda tienda = null;
//
//        if (tiendas.isEmpty()) {
//            return tienda;
//        } else {
//            for (Tienda store : tiendas) {
//                tienda = store;
//                break;
//            }
//        }
        return tienda;
    }

//    public void saveTienda(Tienda tienda) {
//        controllerTienda.create(tienda);
//    }
//
//    public void updateTienda(Tienda tienda) {
//
//        try {
//            controllerTienda.edit(tienda);
//        } catch (IllegalOrphanException | NonexistentEntityException ex) {
//            logger.error("Trata de editar un entidad no existente " + ex.getMessage());
//        } catch (Exception ex) {
//            logger.error("Error al acceder la Base de datoa " + ex.getMessage());
//        }
//    }
//
    public List<Turno> getTurnos() {
        System.out.println("2.- Llego a getTurnos");
        return controllerTurno.findTurnoEntities();
    }

//    public Turno findTurno(String nombre) {
//        return controllerTurno.findTurno(nombre);
//    }
//
//    public Turno findTurno(int turnoId) {
//        return controllerTurno.findTurno(turnoId);
//    }
//
//    public boolean saveTurno(Turno turno) {
//        boolean result = false;
//        try {
//            controllerTurno.create(turno);
//            result = true;
//        } catch (Exception e) {
//            logger.error("Error guardando turno: " + e.getMessage());
//        }
//        return result;
//    }
//
//    public boolean updateTurno(Turno turno) {
//        boolean result = false;
//        try {
//            controllerTurno.edit(turno);
//            result = true;
//        } catch (IllegalOrphanException | NonexistentEntityException ex) {
//            logger.error("Trata de editar un entidad no existente " + ex.getMessage());
//        } catch (Exception ex) {
//            logger.error("Error al acceder la Base de datos " + ex.getMessage());
//        }
//        return result;
//    }
//
//    public boolean deleteTurno(Turno turno) {
//        boolean result = false;
//        try {
//            controllerTurno.destroy(turno.getId());
//            result = true;
//        } catch (Exception e) {
//            logger.error("error eliminando un turno: " + e.getMessage());
//        }
//        return result;
//    }
//
//    public List<Tiempo> getTiempo() {
//        List<Tiempo> tiempos = null;
//        try {
//            tiempos = controllerTiempo.findTiempoEntities();
//        } catch (Exception e) {
//        }
//        return tiempos;
//    }
//
//    public Tiempo findTiempo(Turno turno) {
//        Tiempo tiempo = null;
//        try {
//            tiempo = controllerTiempo.findTiempos(turno);
//        } catch (Exception e) {
//        }
//        return tiempo;
//    }
//
//    public boolean saveTiempo(Tiempo tiempo) {
//        boolean result = false;
//        try {
//            controllerTiempo.create(tiempo);
//            result = true;
//        } catch (Exception e) {
//        }
//        return result;
//    }
//
//    public boolean updateTiempo(Tiempo tiempo) {
//        boolean result = false;
//        try {
//            controllerTiempo.edit(tiempo);
//            result = true;
//        } catch (IllegalOrphanException | NonexistentEntityException ex) {
//            logger.error("Trata de editar una configuración de tiempos no existente " + ex.getMessage());
//        } catch (Exception ex) {
//            logger.error("Error al acceder la Base de datos " + ex.getMessage());
//        }
//        return result;
//    }
//
//    public List<Asesor> getAsesores() {
//        return controllerAsesor.findAsesorEntities();
//    }
//
//    public List<Asesor> findAsesorEnabled() {
//        return controllerAsesor.findAsesorEnabled();
//    }
//
//    public Asesor findAsesor(String nombre, String apellido) {
//        return controllerAsesor.findAsesor(nombre, apellido);
//    }
//
//    public Asesor findAsesor(int asesorId) {
//        return controllerAsesor.findAsesor(asesorId);
//    }
//
//    public boolean saveAsesor(Asesor asesor) {
//        boolean result = false;
//        try {
//            controllerAsesor.create(asesor);
//            result = true;
//        } catch (Exception e) {
//            logger.error("Error guardando asesor: " + e.getMessage());
//        }
//        return result;
//    }
//
//    // ver como actualizar el asesor sin modificar la lista de distribucion
//    public boolean updateAsesor(Asesor asesor) {
//        boolean result = false;
//        try {
//            List<Distribucion> lista = controllerDist.findDistribucionEntities(asesor);
//
//            controllerAsesor.edit(asesor);
//            result = true;
//        } catch (Exception e) {
//        }
//        return result;
//    }
//
//    public boolean deleteAsesor(Asesor asesor) {
//        boolean result = false;
//        try {
//            controllerAsesor.destroy(asesor.getId());
//            result = true;
//        } catch (NonexistentEntityException e) {
//            logger.error("error eliminando asesor: " + e.getMessage());
//        }
//        return result;
//    }
//
//    public List<Boton> getDispositivos() {
//        return controllerBoton.findBotonEntities();
//    }
//
//    public Boton findBoton(String ubicacion) {
//        return controllerBoton.findBoton(ubicacion);
//    }
//
//    public Boton findBoton(int botonId) {
//        return controllerBoton.findBoton(botonId);
//    }
//
//    public Boton findBotonByDireccion(String direccion) {
//        return controllerBoton.findBotonByDireccion(direccion);
//    }
//
//    public boolean botonExiste(String direccion) {
//        if (controllerBoton.botonExiste(direccion)) {
//            return true;
//        } else {
//            return false;
//        }
//    }
//
//    public List<String> findBotonUbicacion() {
//        return controllerBoton.findBotonUbicacion();
//    }
//
//    public boolean saveBoton(Boton boton) {
//        boolean result = false;
//        try {
//            controllerBoton.create(boton);
//            result = true;
//        } catch (Exception e) {
//            logger.error("Error creando el dispositivo: " + e.getMessage());
//        }
//        return result;
//    }
//
//    public boolean updateBoton(Boton boton) {
//        boolean result = false;
//        try {
//            controllerBoton.edit(boton);
//            result = true;
//        } catch (Exception e) {
//            logger.error("Error al actualizar el dispositivo: " + e.getMessage());
//        }
//        return result;
//    }
//
//    public boolean deleteBoton(Boton boton) {
//        boolean result = false;
//        try {
//            controllerBoton.destroy(boton.getId());
//            result = true;
//        } catch (NonexistentEntityException e) {
//            logger.error("error eliminando asesor: " + e.getMessage());
//        }
//        return result;
//    }
//
//    public List<Boton> getBotones() {
//        return controllerBoton.findBotonEntities();
//    }
//
//    public boolean saveDistribucion(Distribucion dist) {
//        boolean result = false;
//        try {
//            controllerDist.create(dist);
//            result = true;
//        } catch (Exception e) {
//            logger.error("Error creando la distribución: " + e.getMessage());
//        }
//        return result;
//    }
//
//    public List<Distribucion> getDistribucionEntities() {
//        return controllerDist.findDistribucionWAsesorEnabled();
//    }
//
//    public List<Distribucion> getDistribucionEntities(Asesor asesor) {
//        return controllerDist.findDistribucionEntities(asesor);
//    }
//
//    public List<Boton> getBotonesXTurno(Asesor asesor, Turno turno) {
//        return controllerDist.findBotonesXTurno(asesor, turno);
//    }
//
    public List<Feriado> findFeriadoEntities() {
        return controllerFeriado.findFeriadoEntities();
    }
//
//    public boolean isFeriadoExistente(int dia, int mes, String motivo) {
//        boolean result = false;
//        Feriado feriado1 = controllerFeriado.findFeriado(dia, mes);
//        if (feriado1 != null) {
//            System.out.println("Feriado1 " + feriado1.getDescripcion() + " " + feriado1.getDia() + feriado1.getMes());
//        }
//        Feriado feriado2 = controllerFeriado.findFeriado(motivo);
//        if (feriado2 != null) {
//            System.out.println("Feriado2 " + feriado2.getDescripcion() + " " + feriado2.getDia() + feriado2.getMes());
//        }
//
//        if (feriado1 != null || feriado2 != null) {
//            result = true;
//        }
//        return result;
//    }

    public Feriado findFeriado(int dia, int mes) {
        return controllerFeriado.findFeriado(dia, mes);
    }

    public Feriado findFeriado(String motivo) {
        return controllerFeriado.findFeriado(motivo);
    }

//    public boolean saveFeriado(Feriado feriado) {
//        boolean result = false;
//        try {
//            controllerFeriado.create(feriado);
//            result = true;
//        } catch (Exception e) {
//            logger.error("Error creando el feriado: " + e.getMessage());
//        }
//        return result;
//    }
//
//    public boolean updateFeriado(Feriado feriado) {
//        boolean result = false;
//        try {
//            controllerFeriado.edit(feriado);
//            result = true;
//        } catch (Exception e) {
//            logger.error("Error actualizando el feriado: " + e.getMessage());
//        }
//        return result;
//    }
//
//    public boolean deleteFeriado(Feriado feriado) {
//        boolean result = false;
//        try {
//            controllerFeriado.destroy(feriado.getId());
//            result = true;
//        } catch (Exception e) {
//            logger.error("Error eliminando el feriado: " + e.getMessage());
//        }
//        return result;
//    }
//
//    public boolean updateDistribucion(Distribucion dist) {
//        boolean result = false;
//        try {
//            controllerDist.edit(dist);
//            result = true;
//        } catch (Exception e) {
//            logger.error("Error actualizando la distribución: " + e.getMessage());
//        }
//        return result;
//    }
//
//    public Distribucion findDistribucion(Asesor asesor, Turno turno, Boton boton) {
//        return controllerDist.findDistribucion(asesor, turno, boton);
//    }
//
//    public List<Distribucion> findDistribucion(Asesor asesor, int status) {
//        return controllerDist.findDistribucion(asesor, status);
//    }
//
//    public List<Distribucion> findDistribucion(Asesor asesor, Turno turno, int status) {
//        return controllerDist.findDistribucion(asesor, turno, status);
//    }

//    public boolean saveAsesorFeriado(Asesor asesor) {
//        boolean result = false;
//        Guardia guardia = new Guardia();
//        try {
//            guardia = controllerGuardia.findGuardia(1);
//            guardia.setAsesorId(asesor);
//            controllerGuardia.edit(guardia);
//            result = true;
//        } catch (Exception e) {
//            guardia = new Guardia();
//            guardia.setAsesorId(asesor);
//            result = true;
//            try {
//                controllerGuardia.create(guardia);
//            } catch (Exception ex) {
//                logger.error("Excepcion " + ex.getMessage());
//            }
//        }
//        return result;
//    }
//    

//    public Asesor findAsesorFeriado() {
//        Asesor asesor = null;
//        try {
//            List<Distribucion> distList = controllerDist.findDistribucionFeriada();
//            for(Distribucion dist : distList){
//                asesor = controllerAsesor.findAsesor(dist.getAsesorId());
//                break;
//            }
//        } finally {
//            return asesor;
//        }
//    }
//    
//    public boolean changeAsesorFeriado(Asesor asesor, int status){
//        int oldStatus;
//        boolean result = false;
//        if(status == GeneralUtilities.HABILITADO){
//            oldStatus = GeneralUtilities.INHABILITADO;
//        } else {
//            oldStatus = GeneralUtilities.HABILITADO;
//        }
//        try {
//            List<Distribucion> listDist = controllerDist.findDistribucionFeriada(asesor, oldStatus);
//            for (Distribucion dist : listDist) {
//                dist.setStatus(status);
//                controllerDist.edit(dist);
//            }
//            result = true;
//        } catch (Exception e) {
//            logger.error("Error actaulizando el asesor para feriado");
//        }
//        return result;
//    }
}