
package ClaseDao;

import ClasesLogin.Usuario;

import java.util.ArrayList;
import java.util.List;



public class UsuarioDao {
    private final  List<Usuario> usuarios;

    public UsuarioDao() {
        usuarios=new ArrayList<>();
    }
    //Método que busca el usuario
    public  int buscar(String usuario){
     int n=-1;
     for(int i=0;i<usuarios.size();i++){
         if (usuarios.get(i).getUsuario().equals(usuario)){
         n=i;
         break;
         }
     }
        return n;
    }
    //Método que inserta el usuario
    public  boolean insertar(Usuario usuario){
          if(buscar(usuario.getUsuario())==-1){
          usuarios.add(usuario);
          return true;
          }else{
          return false;
          }
      }
    //Método que modifica el usuario
    public  boolean modificar(Usuario usuario){
          if(buscar(usuario.getUsuario())!=-1){
          Usuario usuarioaux=obtener(usuario.getUsuario());
          
          usuarioaux.setContrasenia(usuario.getContrasenia());
          usuarioaux.setNombre(usuario.getNombre());
          usuarioaux.setApellido(usuario.getApellido());
          usuarioaux.setCorreo(usuario.getCorreo());
          return true;
          }else{
          return false;
          }
    }
    //Método que elimina el usuario
    public  boolean eliminar(String usuario){
    if(buscar(usuario)!=-1){
          usuarios.remove(buscar(usuario));
          return true;
          }else{
          return false;
          }
    }
    //Método para obtener el usuario
    public  Usuario obtener(String usuario){
         if(buscar(usuario)!=-1){
         return usuarios.get(buscar(usuario));
         }else{
         return null;
         }
        
    }
}
