
package org.example.models;

import org.jetbrains.annotations.Nls;

import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

public class RegistrationTable implements TableModel {
    private Registration players[];
    private String[] columnNames = {"ID", "Player Name"};

    public RegistrationTable(Registration players[]) {
        this.players = players;
    }

    @Override
    public int getRowCount() {
        return players.length;
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Nls
    @Override
    public String getColumnName(int columnIndex) {
        return columnNames[columnIndex];
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        if (getRowCount() == 0) {
            return Object.class;
        }
        return getValueAt(0, columnIndex).getClass();
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Registration player = players[rowIndex];
        switch (columnIndex) {
            case 0:
                return player.id;
            case 1:
                return player.username;
            default:
                return null;
        }
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        switch (columnIndex) {
            case 0:
                players[rowIndex].id = (int) aValue;
                break;
            case 1:
                players[rowIndex].username = (String) aValue;
                break;
        }
    }

    @Override
    public void addTableModelListener(TableModelListener l) {
        // When clicking on the column points, the table will be sorted by points, update the rank too


    }

    @Override
    public void removeTableModelListener(TableModelListener l) {

    }
}
