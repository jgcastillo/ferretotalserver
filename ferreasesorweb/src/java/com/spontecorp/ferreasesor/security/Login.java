package com.spontecorp.ferreasesor.security;


import com.spontecorp.ferreasesor.entity.Usuario;
import com.spontecorp.ferreasesor.service.UsuarioService;
import com.spontecorp.ferreasesor.utilities.JpaUtilities;
import javax.persistence.NoResultException;

/**
 *
 * @author jgcastillo
 */
public class Login {
    private static Usuario usuario;
    private static int permiso;
    
    public static Usuario authenticate(String username, char[] password){
        String pswEncripted = SecurePassword.encript(password);
        UsuarioService controller = new UsuarioService();
        try {
            usuario = controller.findUsuario(username);
            permiso = usuario.getPerfilId().getPermiso();
            
            if(!pswEncripted.equals(usuario.getPsw())){
                return null;
            } else {
                return usuario;
            }
        } catch (NoResultException e) {
            return null;
        }
    }
    
    public static int getPermiso(){
        return permiso;
    }
}
