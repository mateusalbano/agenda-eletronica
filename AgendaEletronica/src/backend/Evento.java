package backend;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class Evento implements Cloneable, Serializable, Comparable<Evento> {
	/**
	 * @author Mateus Albano Santos
	 */
	private static final long serialVersionUID = 1L;
	private String nome;
	private int prioridade;
	private int minutos;
	private boolean finalizado;
	private LocalDateTime dataHorario;
	private String descricao;

	public static final int NOME_MAX_LENGTH = 50;
	public static final int DESC_MAX_LENGTH = 150;
	public static final int PRIORIDADE_BAIXA = 0;
	public static final int PRIORIDADE_MEDIA = 1;
	public static final int PRIORIDADE_ALTA = 2;

	public Evento(String nome, int prioridade, int minutos, boolean finalizado, LocalDateTime dataHorario) {

		this.dataHorario = dataHorario;
	}
	
	public Evento(LocalDate data) {
		nome = "Novo evento";
		minutos = 5;
		dataHorario = LocalDateTime.of(data, LocalTime.now().withSecond(0).withNano(0));
		descricao = "Sem descrição";
	}
	
	public String getNome() {
		return nome;
	}
	
	public void setNome(String nome) {
		if (nome.length() == 0) {
			throw new IllegalArgumentException("Nome deve ter pelo menos 1 caracater.");
		}

		if (nome.length() > NOME_MAX_LENGTH) {
			throw new IllegalArgumentException("Nome deve ter no máximo " + NOME_MAX_LENGTH + " carecteres.");
		}
		this.nome = nome;
	}
	
	public void setPrioridade(int prioridade) {
		if (prioridade < 0 || prioridade > 2) {
			throw new IllegalArgumentException("Prioridade deve estar entre 0 - 2.");
		}
		
		this.prioridade = prioridade;
	}
	
	public void setMinutos(int minutos) {
		if (minutos <= 0) {
			throw new IllegalArgumentException("Número de minutos deve ser positivo.");
		}
		
		this.minutos = minutos;
	}

	public LocalDateTime getDataHorario() {
		return dataHorario;
	}
	
	public LocalDate getData() {
		return dataHorario.toLocalDate();
	}
	
	public LocalTime getHorario() {
		return dataHorario.toLocalTime();
	}

	public boolean isFinalizado() {
		return finalizado;
	}

	public void setFinalizado(boolean finalizado) {
		this.finalizado = finalizado;
	}

	public int getPrioridade() {
		return prioridade;
	}

	public int getMinutos() {
		return minutos;
	}

	public void setDataHorario(LocalDateTime dataHorario) {
		this.dataHorario = dataHorario;
	}
	
	public void setData(LocalDate data) {
		this.dataHorario = LocalDateTime.of(data, getHorario());
	}
	
	public void setHorario(LocalTime horario) {
		this.dataHorario = LocalDateTime.of(getData(), horario);
	}
	
	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		
		if (descricao.length() > DESC_MAX_LENGTH) {
			throw new IllegalArgumentException("Descrição deve ter no máximo " + DESC_MAX_LENGTH + " caracteres.");
		}
		this.descricao = descricao;
	}
	
	@Override
	public String toString() {
		return nome;
	}
	
	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

	@Override
	public int compareTo(Evento o) {
		return o.getData().compareTo(this.getData());
	}
}
