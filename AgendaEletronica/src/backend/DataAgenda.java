package backend;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;

public class DataAgenda extends CalendarDate implements Serializable {
	
	/**
	 * @author Mateus Albano Santos
	 */
	private static final long serialVersionUID = 1L;
	private ArrayList<Evento> eventos;
	private Agenda agenda;
	
	public DataAgenda(LocalDate date) {
		super(date);
		eventos = new ArrayList<Evento>();
	}

	public ArrayList<Evento> getEventos() {
		return eventos;
	}

	public void setEventos(ArrayList<Evento> eventos) {
		this.eventos = eventos;
	}
	
	public void addEvento(Evento evento) {
		if(agenda != null) {
			agenda.addEvento(evento);
		}
		eventos.add(evento);
		setImportant(true);
	}
	
	public boolean removeEvento(Evento evento) {
		if(agenda != null) {
			agenda.removeEvento(evento);
		}
		if (eventos.size() == 1) {
			setImportant(false);
			agenda.removeDataAgenda(this);
		}
		return eventos.remove(evento);
	}
	
	@Override
	public String toString() {
		int q = eventos.size();
		
		if (q > 3) {
			return date.getDayOfMonth() + "**";
		}
		if(q > 0) {
			return date.getDayOfMonth() + "*";
		}
		return "" + date.getDayOfMonth();
	}
	
	protected void setAgenda(Agenda agenda) {
		this.agenda = agenda;
	}
	
}
