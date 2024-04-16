package Backend;


import java.io.*;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import javax.swing.JOptionPane;
import java.util.Random;

    
    public class Parking implements Serializable {
    private final ArrayList<ParkingEntry> entradas;
    private final String nombre;
    public int totalPagar;
    public int numFactura;
    public static Parking parking = new Parking("PARK-POINT\n\n\nINFORMACIÓN", 10);
    
    
    public Parking Parking(Parking parking){
        Parking.parking = parking;
        return this;
    }
    
    public Parking(String nombre, int plazas) {
        this.nombre = nombre;
        entradas = new ArrayList<>(Collections.nCopies(plazas, null));
        
    }

    public String getNombre() {
        return nombre;
    }
//Método para las entradas en el arraylist
    public void entrada(String matricula, int plaza) throws ParkingException {
        if (plaza-1 > entradas.size() || plaza < 0) {
            throw new ParkingException("Plaza inexistente", matricula);
               
        }

        if (matricula == null || matricula.length() != 6) {
            throw new ParkingException("Matrícula incorrecta", matricula);
        }

        if (entradas.get(plaza-1) != null) {
            throw new ParkingException("Plaza ocupada", matricula);
        }

        if (entradas.stream().anyMatch(entry -> entry != null && entry.getMatricula().equals(matricula))) {
            throw new ParkingException("Matrícula repetida", matricula);
        }

        entradas.set(plaza-1, new ParkingEntry(matricula, LocalTime.now())); // Registra la hora de entrada

        // Serializar objeto y guardar en archivo
        guardarDatos();
    }
//Método para liberar una plaza del parqueo y genera la factura
        public int salida(String matricula) throws ParkingException {
            ParkingEntry entry = null;
            for (ParkingEntry e : entradas) {
                if (e != null && e.getMatricula().equals(matricula)) {
                    entry = e;
                    break;
                }
            }

            if (entry == null) {
                throw new ParkingException("Matrícula no existente", matricula);
            }

            int plaza = entradas.indexOf(entry);
            entradas.set(plaza, null);

            // Calcula la permanencia y muestra el tiempo de permanencia
            LocalTime horaSalida = LocalTime.now();
            LocalTime horaEntrada = entry.getHoraEntrada();
            //calcular permancencia en horas y minutos
            long permanenciaHoras = horaEntrada.until(horaSalida, java.time.temporal.ChronoUnit.HOURS);
            long permanenciaMinutos = horaEntrada.until(horaSalida, java.time.temporal.ChronoUnit.MINUTES);
            //cobra el uso de parqueo en base a los minutos
            cobro(permanenciaMinutos);
            
            //hace el cálculo de minutos restantes después de 1 hora para mostrar en factura por horas y minutos
            int tempHora = (int)permanenciaHoras;
            if(tempHora>=0){
                for (int i=0;i<tempHora;i++){
                    permanenciaMinutos = permanenciaMinutos-60;
                }
            }
            
            // Muestra el tiempo de permanencia con formato horas:minutos
            String horaFormateadaEntrada = String.format("%02d:%02d", horaEntrada.getHour(), horaEntrada.getMinute());
            String horaFormateadaSalida = String.format("%02d:%02d", horaSalida.getHour(), horaSalida.getMinute());
            generaFactura();
            JOptionPane.showMessageDialog(null,
                     "      *****************************\n"
                    +"      *           Park Point           *\n" 
                    +"      *       Cédula jurídica:      *\n"
                    +"      *         3-101-001254         *\n" 
                    +"      *   Factura # "+ numFactura +"    *\n"
                    +"      *****************************\n"
                    +"\nTiempo de permanencia del vehículo"
                    +"\nPlaca #: " + matricula 
                    +"\nTiempo estacionado: " + permanenciaHoras + " hora(s) y " + permanenciaMinutos + " minutos\n"
                    +"\nHora de entrada: "+horaFormateadaEntrada
                    +"\nHora de Salida: "+horaFormateadaSalida
                    +"\nTotal a pagar: ¢" + totalPagar, "Factura Electrónica",JOptionPane.INFORMATION_MESSAGE);
            
            guardarDatos(); // Serializar objeto y guardar en archivo.

            return plaza;
        }

    /*
      Nuevo metodo para implementar la serializacion  
    */
    private void guardarDatos() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("datos_vehiculos.txt"))) {
            out.writeObject(this);
            JOptionPane.showMessageDialog(null,"Datos del parqueo guardados correctamente");
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null,"Error al guardar los datos del vehículo: " + ex.getMessage());
        }
    }
    /*Metodo que sirve para hacer el cobro*/
    public int cobro(long permanencia){
        int costoHora=900;
        int costoMediaHora = 450;
        int mediaHora = 30;

        if(permanencia <= 60){//la primer hora debe cobrarse completa
            totalPagar = costoHora;
        }else{
            totalPagar = costoHora;
        
        //se realizan los cálculos para cobrar medias horas adicionales a partir de la primer hora completa
        int minAdicionales = (int) permanencia - 60;//le resta los minutos adicionales a la primer hora
        int mediaHoraAdicional = (int)Math.ceil((double)minAdicionales/mediaHora);
        totalPagar += mediaHoraAdicional * costoMediaHora;//suma la primer hora más el adicional de medias horas según corresponda
        }
        return totalPagar;
    }//fin del metodo cobro

    public int getPlazasTotales() {
        return entradas.size();
    }

    public int getPlazasOcupadas() {
        return (int) entradas.stream().filter(entry -> entry != null).count();
    }

    public int getPlazasLibres() {
        return getPlazasTotales() - getPlazasOcupadas();
    }

    /*
    Se modifico este metodo para que se muestre la hora registrada solo con la hora y los minutos
    */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("            ").append(nombre).append("\n");
        sb.append("\n");
        for (int i = 0; i < entradas.size(); i++) {
            sb.append("Plaza ").append(i+1).append("---->Placa # ");
            if (entradas.get(i) == null) {
                sb.append("(Disponible)\n");
            } else {
                ParkingEntry entry = entradas.get(i);
                LocalTime horaEntrada = entry.getHoraEntrada();
                String horaFormateada = String.format("%02d:%02d", horaEntrada.getHour(), horaEntrada.getMinute()); // Formatear hora sin segundos
                LocalTime horaSalida = LocalTime.now();
                //sacar horas y minutos para mostrarlos
                long permanenciaHoras = horaEntrada.until(horaSalida, java.time.temporal.ChronoUnit.HOURS);
                long permanenciaMinutos = horaEntrada.until(horaSalida, java.time.temporal.ChronoUnit.MINUTES);
                //hace el cálculo de minutos restantes después de 1 hora para mostrar en factura por horas y minutos
                int tempHora = (int)permanenciaHoras;
                if(tempHora>=0){
                    for (int j=0;j<tempHora;j++){
                        permanenciaMinutos = permanenciaMinutos-60;
                    }
                }
                
                sb.append(entry.getMatricula()).append(" | Hora entrada: ").append(horaFormateada).append("\nPermanencia: ").append(permanenciaHoras).append(" hora(s) y ").append(permanenciaMinutos).append(" minutos\n\n");
            }
        }
        sb.append("\n");
        return sb.toString();
    }
    
    // Método para generar número de factura aleatoria
    public int generaFactura() {
        Random ranfactura = new Random(); //esto saca los 8 numeros de forma aleatoria
        numFactura = 10000000 + ranfactura.nextInt(90000000); // genera el numero aleatorio de 8 cifras por eso los numeros tan grandes
        return numFactura;
    }
    /*Método para la entrada del auto al parqueo*/
    public static void entradaCoche() {
        boolean correcto = false;
        try {
            String m = JOptionPane.showInputDialog("Introduzca matrícula: ");
            int p = Integer.parseInt(JOptionPane.showInputDialog("Introduzca la plaza: "));
            parking.entrada(m, p);
            correcto = true;
        } catch (ParkingException ex) {
            JOptionPane.showMessageDialog(null, "ERROR: " + ex.getMensaje());
            JOptionPane.showMessageDialog(null, "No se realizó la entrada del coche con matrícula "
                    + ex.getMatricula() + " en el parking");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "Formato de número incorrecto");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "ERROR DESCONOCIDO. ");
        } finally {
            if (!correcto) {
                JOptionPane.showMessageDialog(null, "Se produjo un error.");
            }
        }
    }
/*Método para la salida del auto del parqueo*/
    public static void salidaCoche() {
        boolean correcto = false;
        try {
            String m = JOptionPane.showInputDialog("Introduzca la matrícula: ");
            int p = parking.salida(m);
            JOptionPane.showMessageDialog(null, "El coche " + m + " salió de la plaza " + (p+1) + "\n\n");
            JOptionPane.showMessageDialog(null, "Plazas totales: " + parking.getPlazasTotales() + "\n");
            JOptionPane.showMessageDialog(null, "Plazas ocupadas: " + parking.getPlazasOcupadas() + "\n");
            JOptionPane.showMessageDialog(null, "Plazas libres: " + parking.getPlazasLibres() + "\n\n");
            correcto = true;
        } catch (ParkingException ex) {
            JOptionPane.showMessageDialog(null, "ERROR: " + ex.getMensaje());
            JOptionPane.showMessageDialog(null, "No se realizó la salida del coche con matrícula "
                    + ex.getMatricula() + " del parking");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "ERROR DESCONOCIDO.");
        } finally {
            if (!correcto) {
                JOptionPane.showMessageDialog(null, "Se produjo un error");
            }
        }
    }
    
    /*
    Se hizo este metodo para usar el objeto parking y poder mostrar los datos 
    guardados
    */
    public static void guardarDatosVehiculo() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("datos_vehiculos.txt"))) {
            out.writeObject(parking);
            JOptionPane.showMessageDialog(null, "Datos del vehículo guardados correctamente");
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Error al guardar los datos del vehículo: " + ex.getMessage());
        }
    }
    /*Método para cargar las matriculas al archivo txt*/
    public static void cargarDatosVehiculo() throws ClassNotFoundException {
       
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream("datos_vehiculos.txt"))) {
            parking =(Parking)in.readObject();
            
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Error al leer los datos del vehículo: " + ex.getMessage());
        }
    }
    
    
    }
/*Clase que funciona para serializar*/
class ParkingEntry implements Serializable {
    private final String matricula;
    private final LocalTime horaEntrada;

    public ParkingEntry(String matricula, LocalTime horaEntrada) {
        this.matricula = matricula;
        this.horaEntrada = horaEntrada;
    }

    public String getMatricula() {
        return matricula;
    }

    public LocalTime getHoraEntrada() {
        return horaEntrada;
    }
}

    
    


