/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.gov.rn.emater.IA;

import java.util.Iterator;
import java.util.Vector;

 import javax.swing.table.AbstractTableModel;

import org.neuroph.core.learning.SupervisedTrainingElement;
import org.neuroph.core.learning.TrainingElement;
import org.neuroph.core.learning.TrainingSet;

/**
 * @author Zoran Sevarac <sevarac@gmail.com>
 */
public class TrainingSetTableModel extends AbstractTableModel {
	private static final long serialVersionUID = 1L;

	public int HIDDEN_INDEX;
	
     protected Vector<String> columnIdentifiers;
     protected Vector<Vector>dataVector;	

	public TrainingSetTableModel() {
		this.columnIdentifiers = new Vector<String>();
		this.dataVector = new Vector<Vector>();
	}
	
	
	public TrainingSetTableModel(TrainingSet trainingSet) {
		this.columnIdentifiers = new Vector<String>();
		this.dataVector = new Vector<Vector>();
				
		TrainingElement trainingElement = trainingSet.elementAt(0);
		
		HIDDEN_INDEX = trainingElement.getInput().size();

		Vector<Double> inputVector = trainingElement.getInput();
		for (int i = 0; i < inputVector.size(); i++) {
			this.columnIdentifiers.add("Input " + (i + 1));
		}

		if (trainingElement instanceof SupervisedTrainingElement) {
			Vector<Double> outputVector = ((SupervisedTrainingElement) trainingElement)
					.getDesiredOutput();
			for (int i = 0; i < outputVector.size(); i++) {
				this.columnIdentifiers.add("Output " + (i + 1));
			}
			
			HIDDEN_INDEX += outputVector.size();
		}
		
		
		this.columnIdentifiers.add(""); // hidden col

		// columns = inputs + outputs
		Iterator<TrainingElement> iterator = trainingSet.iterator();
		while (iterator.hasNext()) {
			trainingElement = iterator.next();
			inputVector = trainingElement.getInput();

			Vector rowVector = new Vector();
			rowVector.addAll(inputVector);

			if (trainingElement instanceof SupervisedTrainingElement) {
				Vector<Double> outputVector = new Vector<Double>();
				outputVector = ((SupervisedTrainingElement) trainingElement)
						.getDesiredOutput();
				rowVector.addAll(outputVector);
			}
			
			rowVector.add(new String());

			this.dataVector.add(rowVector);
		}
	}

	public TrainingSetTableModel(int inputs, int outputs) {
		this.columnIdentifiers = new Vector<String>();
		this.dataVector = new Vector<Vector>();
		
		HIDDEN_INDEX = inputs + outputs;
		
		for (int i = 0; i < inputs; i++) {
			this.columnIdentifiers.add("Input " + (i + 1));
		}

		for (int j = 0; j < outputs; j++) {
			this.columnIdentifiers.add("Output " + (j + 1));
		}
		this.columnIdentifiers.add(""); // hidden col
		
	}
	
	@Override
	public String getColumnName(int column) {
		return this.columnIdentifiers.elementAt(column).toString();
	}	
	
	@Override
	public boolean isCellEditable(int row, int column) {
		if (column == HIDDEN_INDEX) return false;
		else return true;
	}
	
	@Override
	public Class getColumnClass(int column) {
		return Object.class;
	}	

        @Override
	public Object getValueAt(int row, int column) {
		return this.dataVector.get(row).get(column);
	}	
	
	@Override
	public void setValueAt(Object value, int row, int column) {
		Vector rowVector = dataVector.get(row);	
		rowVector.set(column, value);
		fireTableCellUpdated(row, column);
	}	

        @Override
	public int getRowCount() {
		return dataVector.size();
	}

        @Override
	public int getColumnCount() {
		return columnIdentifiers.size();
	}	
	

	public void addEmptyRow() {
		int columns = columnIdentifiers.size();
		Vector<String> newRow = new Vector<String>();
		for (int c = 0; c < columns; c++) {
			newRow.add(new String());
		}		
		
		dataVector.add(newRow);
		fireTableRowsInserted(
			dataVector.size() - 1,
			dataVector.size() - 1);
	}
	
	public boolean hasEmptyRow() {
		if (dataVector.size() == 0) return false;
		Vector rowVector = dataVector.get(dataVector.size() - 1);
	 
		Iterator i=rowVector.iterator();
		while(i.hasNext()) {
			if (!i.next().toString().trim().equals("")) return false;
		}
	
		return true;
	}	
	
	public Vector getDataVector() {
		return this.dataVector;
	}
		
	public void removeLastEmptyRow() {
		dataVector.removeElementAt(dataVector.size() - 1);
		fireTableRowsInserted(
			dataVector.size() - 1,
			dataVector.size() - 1);		
	}
	
	public void removeRow(int row) {
		if (row < 0 || row > getRowCount()){ //may need to be >=
			return;
		}
           
		dataVector.remove(row);
		fireTableRowsDeleted(row, row);
	}		    

}