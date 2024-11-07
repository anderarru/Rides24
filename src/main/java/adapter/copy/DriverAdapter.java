package adapter.copy;

import javax.swing.table.AbstractTableModel;

import domain.Driver;
import domain.Ride;

import java.util.List;

public class DriverAdapter extends AbstractTableModel {
    private Driver driver;
    private List<Ride> rides; // Suponiendo que Driver tiene una lista de viajes (Ride)

    public DriverAdapter(Driver driver) {
        this.driver = driver;
        this.rides = driver.getCreatedRides(); // Método que devuelve la lista de viajes de Driver
    }

    @Override
    public int getRowCount() {
        return rides.size(); // Número de viajes
    }

    @Override
    public int getColumnCount() {
        return 3; // Número de columnas que deseas mostrar, por ejemplo: Origen, Destino, Fecha
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Ride ride = rides.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return ride.getFrom();
            case 1:
                return ride.getTo();
            case 2:
                return ride.getDate();
            default:
                return null;
        }
    }

    @Override
    public String getColumnName(int column) {
        switch (column) {
            case 0:
                return "Origin";
            case 1:
                return "Destination";
            case 2:
                return "Date";
            default:
                return "";
        }
    }
}
