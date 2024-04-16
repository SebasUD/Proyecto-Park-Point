
package Backend;


public class ParkingException extends Exception{
    public String mensaje;
    public String matricula;
    
    public ParkingException(String mensaje, String matricula) {
        this.mensaje = mensaje;
        this.matricula = matricula;
    }
    
    public String getMensaje() {
        return mensaje;
    }
    
    public String getMatricula() {
        return matricula;
    }   
}
