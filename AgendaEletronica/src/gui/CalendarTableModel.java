package gui;


import java.lang.reflect.Constructor;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.YearMonth;

import javax.swing.event.TableModelEvent;
import javax.swing.table.AbstractTableModel;

import backend.CalendarDate;

public class CalendarTableModel extends AbstractTableModel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private CalendarDate[][] datas;
	private Class<? extends CalendarDate> classe;
	private char[] colunas;
	private int mes;
	private int ano;
	
	public CalendarTableModel(Class<? extends CalendarDate> classe, int mes, int ano) {
		this.classe = classe;
		colunas = new char[]{'D', 'S', 'T', 'Q', 'Q', 'S', 'S'};
		createMonth(ano, mes);
	}
	
	private void createMonth(int ano, int mes) {
		
		if (mes < 1 || mes > 12) {
			throw new DateTimeException("Invalid month");
		}
		this.mes = mes;
		this.ano = ano;
		
		LocalDate primeiroDia = LocalDate.of(ano, mes, 01);
		YearMonth mesAtual = YearMonth.of(ano, mes);
		
		int ultimoDia = mesAtual.atEndOfMonth().getDayOfMonth();
		int diaSemana = primeiroDia.getDayOfWeek().getValue();
		int numRow = 5;
		
		if (diaSemana == 7) {
			diaSemana = 0;
		}
		
		if ((diaSemana + ultimoDia) % 7 == 0) {
			numRow =(diaSemana + ultimoDia) / 7;
		} else {
			numRow =(diaSemana + ultimoDia) / 7 + 1;
		}
		
		
		int numCol = 7;
		datas = new CalendarDate[numRow][numCol];
		int dia = 1;
		
		boolean inicio = false;
		boolean fim = false;
		
		for (int i = 0; i < numRow; i++) {
			
			if (fim) break;
			for (int j = 0; j < numCol; j++) {
				
				if (!inicio) {
					j = diaSemana;
					inicio = true;
				}
				
				if (dia == ultimoDia + 1) {
					fim = true;
					break;
				}
				Object data;
				try {
					Constructor<? extends CalendarDate> cons = classe.getDeclaredConstructor(LocalDate.class);
					data = cons.newInstance(LocalDate.of(ano, mes, dia));
				} catch (Exception e) {
					e.printStackTrace();
					return;
				}
				
				datas[i][j] = (CalendarDate) data;
				dia++;
			}
		}
	}
	
	private void verifyRowIndex(int rowIndex) {
		int length = datas.length;
		if (rowIndex >= length || rowIndex < 0) {
			throw new IndexOutOfBoundsException("Row index " + rowIndex + " out of bounds for length " + datas.length);
		}
	}
	
	private void verifyColumnIndex(int columnIndex) {
		if (columnIndex > 6 || columnIndex < 0) {
			throw new IndexOutOfBoundsException("Column index " + columnIndex + " out of bounds for length 7");
		}
	}
	
	private void verifyIndex(int rowIndex, int columnIndex) {
		verifyRowIndex(rowIndex);
		verifyColumnIndex(columnIndex);
	}
	
	private void verifyDay(int dia) {
		YearMonth mes = YearMonth.of(this.ano, this.mes);
		if (dia < 1 || dia > mes.atEndOfMonth().getDayOfMonth()) {
			throw new DateTimeException("Wrong day");
		}
	}
	
    public void fireTableCellUpdated(int dia) {
    	int values[] = getPosition(dia);
        fireTableChanged(new TableModelEvent(this, values[0], values[0], values[1]));
    }
	
	@Override
	public int getRowCount() {
		return datas.length;
	}

	@Override
	public int getColumnCount() {
		return 7;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		verifyIndex(rowIndex, columnIndex);
		return datas[rowIndex][columnIndex];
	}
	
	public Object getValueAt(int dia) {
		verifyDay(dia);
		int[] values = getPosition(dia);
		return datas[values[0]][values[1]];
	}
	
	@Override
	public String getColumnName(int columnIndex) {
		verifyColumnIndex(columnIndex);
		return "" + colunas[columnIndex];
	}
	
	@Override
	public Class<?> getColumnClass(int columnIndex) {
		verifyColumnIndex(columnIndex);
		return Object.class;
	}
	
	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		verifyIndex(rowIndex, columnIndex);
		return false;
	}
	
	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		verifyIndex(rowIndex, columnIndex);
		datas[rowIndex][columnIndex] = (CalendarDate)aValue;
		fireTableCellUpdated(rowIndex, columnIndex);
	}
	
	public void setValueAt(Object aValue, int dia) {
		verifyDay(dia);
		int[] values = getPosition(dia);
		datas[values[0]][values[1]] = (CalendarDate)aValue;
		fireTableCellUpdated(values[0], values[1]);
	}
	
	public int[] getPosition(int dia) {
		int[] values = new int[2];
		LocalDate diaLocalDate;
		diaLocalDate = LocalDate.of(ano, mes, 1);
		int diaSemana = diaLocalDate.getDayOfWeek().getValue();
		if (diaSemana == 7) {
			diaSemana = 0;
		}
		int pos = diaSemana + dia;
		int posI = pos / 7;
		int posJ = pos % 7;
		if(posJ == 0) {
			posI--;
			posJ = 7;
		}
		posJ--;
		values[0] = posI;
		values[1] = posJ;
		return values;
	}
	
	public int getMes() {
		return mes;
	}
	
	public int getAno() {
		return ano;
	}

}
