
package Main;

import Backend.Parking;
import InterfazGrafica.PantallaLogin;
import javax.swing.JOptionPane;

/**
 *
 * @authores: Esteban Mora Matamoros
 *            Marco Esteban Rivera Barboza
 *            Sebastian Jesus Ulloa Díaz
 *            Luis Diego Díaz Torelli
 *            Rogelio Leiton Hernandez 
 */


public class ProyectoFinalP2 {

    
    public static void main(String[] args) {
      
        PantallaLogin pant=new PantallaLogin();
        pant.setVisible(true);
        pant.setLocationRelativeTo(null);
        //cargar los datos de los vehículos almacenados previamente
        try{
            Parking.cargarDatosVehiculo();
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
        
    }
    
}
