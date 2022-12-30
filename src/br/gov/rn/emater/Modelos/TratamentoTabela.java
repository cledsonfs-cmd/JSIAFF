/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.gov.rn.emater.Modelos;

import br.gov.rn.emater.Classes.Tratamento;
import br.gov.rn.emater.Controler.TratamentoController;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author cledsonfs
 */
public class TratamentoTabela extends AbstractTableModel {

    private ArrayList<Tratamento> lista;
    private TratamentoController controller;
    private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyy hh:mm");
    private boolean previa = false;

    public TratamentoTabela() {
        controller = new TratamentoController();
        try {
            lista = (ArrayList<Tratamento>) controller.getTodos();
        } catch (Exception ex) {
            Logger.getLogger(TratamentoTabela.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public TratamentoTabela(ArrayList<Tratamento> lista, boolean previa) {
        this.previa = previa;
        this.lista = lista;
    }

    public int getRowCount() {
        return lista.size();
    }

    public int getColumnCount() {
        if (previa) {
            return 2;
        } else {
            return 4;
        }
    }

    @Override
    public String getColumnName(int columnIndex) {
        if (previa) {
            if (columnIndex == 0) {
                return "Cód.";
            } else if (columnIndex == 1) {
                return "Descrição";
            }
        } else {
            if (columnIndex == 0) {
                return "Cód.";
            } else if (columnIndex == 1) {
                return "Descrição";
            } else if (columnIndex == 2) {
                return "Cad. por";
            } else if (columnIndex == 3) {
                return "Data/Hora";
            }
        }
        return null;
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        if (columnIndex == 0) {
            return lista.get(rowIndex).getIdtratamento();
        } else if (columnIndex == 1) {
            return lista.get(rowIndex).getDescricao().trim();
        } else if (columnIndex == 2) {
            return lista.get(rowIndex).getUsuario().getLogin().trim();
        } else if (columnIndex == 3) {
            return sdf.format(lista.get(rowIndex).getDatacadastro());
        }
        return null;
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        if (columnIndex == 0) {
            return Integer.class;
        } else if (columnIndex == 1) {
            return String.class;
        } else if (columnIndex == 2) {
            return String.class;
        } else if (columnIndex == 3) {
            return String.class;
        }
        return null;
    }
}
