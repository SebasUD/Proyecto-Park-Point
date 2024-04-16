
package Backend;

import ClaseDao.UsuarioDao;
import ClasesLogin.Usuario;

public class UsuarioLogic {
    private static final UsuarioDao usuariodao=new UsuarioDao();
    
    //Método que autentifica al usuario y la contraseña
    public static boolean autentificar(String usuario, String contrasenia){
       if(obtener(usuario)!=null){
           Usuario usuarioConsultar=obtener(usuario);
           if(usuarioConsultar.getUsuario().equals(usuario)&&usuarioConsultar.getContrasenia().equals(contrasenia)){
           return true;
           }else{
           return false;
           }
       }else{
       return false;
       }
    }
    //Métodos que llaman a loa métodos en la clase UsuarioDao
    public static boolean insertar(Usuario usuario){
         return usuariodao.insertar(usuario);
    }
    public static boolean modificar(Usuario usuario){
         return usuariodao.modificar(usuario);
    }
    public static boolean eliminar(String usuario){
         return usuariodao.eliminar(usuario);
    }
    
    public static Usuario obtener(String usuario){
          return usuariodao.obtener(usuario);
    }
    
}
