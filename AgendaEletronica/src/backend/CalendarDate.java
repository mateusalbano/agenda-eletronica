package backend;

import java.io.Serializable;
import java.time.LocalDate;

public abstract class CalendarDate implements Serializable {
	/**
	 * @author Mateus Albano Santos
	 */
	private static final long serialVersionUID = 1L;
	protected LocalDate date;
	protected boolean important;
	
	public CalendarDate(LocalDate date) {
		this.date = date;
	}
	
	public void setDate(LocalDate date) {
		this.date = date;
	}
	
	public LocalDate getDate() {
		return date;
	}
	
	public boolean isImportant() {
		return important;
	}

	public void setImportant(boolean important) {
		this.important = important;
	}

	@Override
	public String toString() {
		return "" + date.getDayOfMonth();
	}
	
}
