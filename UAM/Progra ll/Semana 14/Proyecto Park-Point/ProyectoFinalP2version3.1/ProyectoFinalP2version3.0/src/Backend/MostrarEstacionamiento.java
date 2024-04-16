
package Backend;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;


public class MostrarEstacionamiento extends JFrame {
    
    private JTextArea area;
    
    private JButton btMostrar;
    
    
    
    public void MostrarEstacionamiento() {
        
        
  
        
        setLayout(new BorderLayout());
        
        add(new PanelArea(), BorderLayout.CENTER);
        
        add(new PanelBoton(), BorderLayout.SOUTH);
        

setTitle("Mostrar Parqueo");

setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

pack();

setLocationRelativeTo(null);

setVisible(true);

    }
      
    private class PanelArea extends JPanel {
        
        public PanelArea() {
            
            area = new JTextArea(40, 40);
            
            add(area);
            setBackground(new java.awt.Color(37, 152, 134));
            setBorder(BorderFactory.createCompoundBorder(
                    
                    BorderFactory.createEmptyBorder(25,30,25,30),
                    
                    BorderFactory.createTitledBorder("Lista de Plazas ")));
            
        }
        
    }
    private class PanelBoton extends JPanel {
        
        public PanelBoton() {
            
            btMostrar = new JButton("Mostrar");
            
            btMostrar.addActionListener(new ActionListener() {
                
                @Override
                
                public void actionPerformed(ActionEvent e) {
                    
                    area.setText(Backend.Parking.parking + "\n\n"); //Limpiamos area texto
                                      
                }
                
            });
            
            
            
            add(btMostrar);
            
        }
        
    }

}
    

