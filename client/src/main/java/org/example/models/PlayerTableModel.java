package org.example.models;

import org.javatuples.Pair;
import org.jetbrains.annotations.Nls;

import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import java.util.Vector;

public class PlayerTableModel  implements TableModel {
    private Object[][] players;
    private String[] columnNames = {"Id", "Player Name", "Score"};

    public PlayerTableModel(Vector<Pair<String, Integer>> players) {
        this.players = new Object[players.size()][3];
        for (int i = 0; i < players.size(); i++) {
            this.players[i][0] = i + 1;
            this.players[i][1] = players.get(i).getValue0();
            this.players[i][2] = players.get(i).getValue1();
        }
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
        return players[rowIndex][columnIndex];
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        players[rowIndex][columnIndex] = aValue;
    }

    @Override
    public void addTableModelListener(TableModelListener l) {
        // When clicking on the column points, the table will be sorted by points, update the rank too


    }

    @Override
    public void removeTableModelListener(TableModelListener l) {

    }
}
