package adapter;

import javax.swing.*;
import domain.Driver;
import domain.Ride;

import java.awt.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DriverTable extends JFrame {
    private Driver driver;
    private JTable table;

    public DriverTable(Driver driver) {
        super(driver.getUsername() + "'s rides");
        this.driver = driver;
        
        // Configuración de la ventana
        this.setBounds(100, 100, 700, 200);
        
        // Crear el adaptador y la tabla
        DriverAdapter adapter = new DriverAdapter(driver);
        table = new JTable(adapter);
        
        // Configurar la tabla dentro de un JScrollPane
        table.setPreferredScrollableViewportSize(new Dimension(500, 70));
        JScrollPane scrollPane = new JScrollPane(table);
        getContentPane().add(scrollPane, BorderLayout.CENTER);
    }   
    
    public static void main(String[] args) {
        // Ejemplo de prueba - reemplaza con el código para obtener un objeto Driver real
        Driver gidari = new Driver("user","pass");
        
        // Utilizar SimpleDateFormat para convertir las cadenas a Date
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date data1 = dateFormat.parse("2024-12-13");
            Date data2 = dateFormat.parse("2024-12-15");
            gidari.addRide("Donostia", "Bilbo", data1, 2, 30);
            gidari.addRide("Madril", "Donostia", data2, 1, 10);
        } catch (ParseException e) {
            e.printStackTrace();  // Manejo de error si las fechas son incorrectas
        }

        DriverTable driverTable = new DriverTable(gidari);
        driverTable.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        driverTable.setVisible(true);
    }
}
