package gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import backend.Agenda;
import backend.DataAgenda;
import backend.Evento;
import javax.swing.SpinnerNumberModel;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import java.awt.Font;
import javax.swing.border.SoftBevelBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.border.BevelBorder;
import java.awt.Cursor;
import javax.swing.ScrollPaneConstants;
import javax.swing.JTextArea;
import java.awt.Dimension;
import javax.swing.UIManager;
import javax.swing.border.MatteBorder;
import java.awt.Color;

public class CalendarioPanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTable table;
	private CalendarTableModel calendarioModel;
	private JButton btnSalvar;
	private JButton btnCancelar;
	private JButton btnAdicionar;
	private JButton btnExcluir;
	private JButton btnVoltarAno;
	private JButton btnVoltarMes;
	private JButton btnAvancarMes;
	private JButton btnAvancarAno;
	private JComboBox<String> cbMes;
	private JComboBox<Integer> cbAno;
	private Agenda agenda;
	private JTextField tfNome;
	private JSpinner spinnerHora;
	private JSpinner spinnerMinuto;
	private JSpinner spinnerTempo;
	private JRadioButton rdbtnBaixa;
	private JRadioButton rdbtnMedia;
	private JRadioButton rdbtnAlta;
	private JCheckBox chckbxFinalizado;
	private JList<Evento> list;
	private JLabel lblNome;
	private JLabel lblHorario;
	private JLabel lblPrioridade;
	private JLabel lblDuracao;
	private ButtonGroup buttonGroup;
	private JLabel lblDoisPontos;
	private JButton btnHoje;
	private JLabel lblData;
	private JLabel lblDescricao;
	private JScrollPane scrollPane_1;
	private JTextArea taDescricao;
	
	private void mudaAvancarVoltar() {
		int indexMes = cbMes.getSelectedIndex();
		int indexAno = cbAno.getSelectedIndex();
		int anoLastIndex = cbAno.getModel().getSize() - 1;
		if (indexAno == anoLastIndex) {
			btnAvancarAno.setEnabled(false);
			btnVoltarMes.setEnabled(true);
			btnVoltarAno.setEnabled(true);
			if(indexMes == 11) {
				btnAvancarMes.setEnabled(false);
			} else {
				btnAvancarMes.setEnabled(true);
			}
		} else {
			btnAvancarAno.setEnabled(true);
			btnAvancarMes.setEnabled(true);
			if (indexAno == 0) {
				btnVoltarAno.setEnabled(false);
				if (indexMes == 0) {
					btnVoltarMes.setEnabled(false);
				} else {
					btnVoltarMes.setEnabled(true);
				}
			} else {
				btnVoltarAno.setEnabled(true);
				btnVoltarMes.setEnabled(true);
			}
		}

	}
	
	private void setDay(LocalDate data) {
		int dia = data.getDayOfMonth();
		int mes = data.getMonthValue();
		int ano = data.getYear();
		cbMes.setSelectedIndex(mes - 1);
		cbAno.setSelectedItem(ano);
		int[] values = calendarioModel.getPosition(dia);
		table.setRowSelectionInterval(values[0], values[0]);
		table.setColumnSelectionInterval(values[1], values[1]);
		table.requestFocus();
	}
	
	private void atualizaCalendario(int mes, int ano) {
		CalendarTableModel calendarioModel = new CalendarTableModel(DataAgenda.class, mes, ano);
		table.setModel(calendarioModel);
		int[] values = calendarioModel.getPosition(1);
		table.setRowSelectionInterval(values[0], values[0]);
		table.setColumnSelectionInterval(values[1], values[1]);
		this.calendarioModel = calendarioModel;
		carregaDatas();
		atualizaLista(getTableSelectedValue());
		table.requestFocus();
	}
	
	private void atualizaCalendario() {
		LocalDate hoje = LocalDate.now();
		CalendarTableModel calendarioModel = new CalendarTableModel(DataAgenda.class, hoje.getMonthValue(), hoje.getYear());
		table.setModel(calendarioModel);
		int[] values = calendarioModel.getPosition(hoje.getDayOfMonth());
		table.setRowSelectionInterval(values[0], values[0]);
		table.setColumnSelectionInterval(values[1], values[1]);
		this.calendarioModel = calendarioModel;
		carregaDatas();
		atualizaLista(getTableSelectedValue());
		table.requestFocus();
	}
	
	private void adicionaEvento(DataAgenda dataAgenda) {
		LocalDate data;
		Evento evento;
		data = dataAgenda.getDate();
		int dia = dataAgenda.getDate().getDayOfMonth();
		evento = new Evento(data);
		agenda.addData(dataAgenda);
		dataAgenda.addEvento(evento);
		calendarioModel.fireTableCellUpdated(dia);
		
		atualizaLista(dataAgenda);
	}
	
	private void selecionaData(DataAgenda dataAgenda) {
		if (dataAgenda == null) {
			lblData.setText("");
			return;
		} 
		LocalDate data = dataAgenda.getDate();
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/uuuu");
		String dataString = data.format(dtf) + " - " + data.getDayOfWeek().getDisplayName(TextStyle.FULL, getLocale());
		lblData.setText(dataString);
	}
	
	private void removeEventos(DataAgenda dataAgenda) {
		ArrayList<Evento> eventos = (ArrayList<Evento>) list.getSelectedValuesList();
		int q = eventos.size();
		DefaultListModel<Evento> listModel = (DefaultListModel<Evento>) list.getModel();
		int opcao;
		if (q == 1) {
			opcao = JOptionPane.showConfirmDialog(this, "Deseja remover o evento \"" + eventos.get(0).getNome() + "\"?");
		} else {
			opcao = JOptionPane.showConfirmDialog(this, "Deseja remover os " + q + " eventos selecionados?");
		}
		
		if(opcao == 0) {
			for (Evento evento : eventos) {
				listModel.removeElement(evento);
				dataAgenda.removeEvento(evento);
				int dia = dataAgenda.getDate().getDayOfMonth();
				calendarioModel.fireTableCellUpdated(dia);
			}
		}
	}
	
	private void carregaDatas() {
		int mes = calendarioModel.getMes();
		int ano = calendarioModel.getAno();
		ArrayList<DataAgenda> datas = agenda.getDatas(mes, ano);
		
		for (DataAgenda dataAgenda : datas) {
			calendarioModel.setValueAt(dataAgenda, dataAgenda.getDate().getDayOfMonth());
		}
	}
	
	private void atualizaLista(DataAgenda dataAgenda) {
		DefaultListModel<Evento> listModel = new DefaultListModel<Evento>();
		if (dataAgenda != null) {
			listModel.addAll(dataAgenda.getEventos());
		}
		list.setModel(listModel);
	}
	
	private int getPrioridade() {
		if (rdbtnBaixa.isSelected()) {
			return Evento.PRIORIDADE_BAIXA;
		} if (rdbtnMedia.isSelected()) {
			return Evento.PRIORIDADE_MEDIA;
		}
		
		return Evento.PRIORIDADE_ALTA;
	}
	
	private void setPrioridade(int prioridade) {
		if (prioridade == Evento.PRIORIDADE_BAIXA) {
			rdbtnBaixa.setSelected(true);
		} else if (prioridade == Evento.PRIORIDADE_MEDIA) {
			rdbtnMedia.setSelected(true);
		} else if (prioridade == Evento.PRIORIDADE_ALTA) {
			rdbtnAlta.setSelected(true);
		} else {
			buttonGroup.clearSelection();
		}
	}
	
	private void carregaEvento(Evento evento) {
		if(evento == null) {
			mudaSalvarCancelar(false);
			limpaCampos();
			habilitaCamposPreenchimento(false);
			return;
		}
		habilitaCamposPreenchimento(true);
		LocalTime hora = evento.getHorario();
		tfNome.setText(evento.getNome());
		spinnerHora.setValue(hora.getHour());
		spinnerMinuto.setValue(hora.getMinute());
		spinnerTempo.setValue(evento.getMinutos());
		setPrioridade(evento.getPrioridade());
		chckbxFinalizado.setSelected(evento.isFinalizado());
		taDescricao.setText(evento.getDescricao());
		mudaSalvarCancelar(true);
	}
	
	private void salvaEvento(Evento evento) {
		String nome = tfNome.getText();
		LocalTime horario = LocalTime.of((Integer)spinnerHora.getValue(), (Integer)spinnerMinuto.getValue());
		int tempo = (Integer) spinnerTempo.getValue();
		int prioridade = getPrioridade();
		boolean finalizado = chckbxFinalizado.isSelected();
		String descricao = taDescricao.getText();
		try {
			Evento eventoAux = (Evento) evento.clone();
			eventoAux.setNome(nome);
			eventoAux.setHorario(horario);
			eventoAux.setMinutos(tempo);
			eventoAux.setPrioridade(prioridade);
			eventoAux.setFinalizado(finalizado);
			eventoAux.setDescricao(descricao);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, e.getMessage(), "ERRO", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		evento.setNome(nome);
		evento.setHorario(horario);
		evento.setMinutos(tempo);
		evento.setPrioridade(prioridade);
		evento.setFinalizado(finalizado);
		evento.setDescricao(descricao);
		limpaSelecao();
		JOptionPane.showMessageDialog(this, "Evento salvo com sucesso!", "Mensagem", JOptionPane.INFORMATION_MESSAGE);
	}
	
	private void limpaCampos() {
		tfNome.setText("");
		spinnerHora.setValue(0);
		spinnerMinuto.setValue(0);
		setPrioridade(-1);
		chckbxFinalizado.setSelected(false);
		taDescricao.setText("");
	}
	
	private void limpaSelecao() {
		list.clearSelection();
		limpaCampos();
		mudaSalvarCancelar(false);
	}
	
	private void habilitaCamposPreenchimento(boolean mod) {
		lblNome.setEnabled(mod);
		tfNome.setEnabled(mod);
		lblHorario.setEnabled(mod);
		spinnerHora.setEnabled(mod);
		lblDoisPontos.setEnabled(mod);
		spinnerMinuto.setEnabled(mod);
		lblDuracao.setEnabled(mod);
		spinnerTempo.setEnabled(mod);
		lblPrioridade.setEnabled(mod);
		rdbtnBaixa.setEnabled(mod);
		rdbtnMedia.setEnabled(mod);
		rdbtnAlta.setEnabled(mod);
		chckbxFinalizado.setEnabled(mod);
		lblDescricao.setEnabled(mod);
		taDescricao.setEnabled(mod);
	}
	
	private DataAgenda getTableSelectedValue() {
		int column = table.getSelectedColumn();
		int row = table.getSelectedRow();
		if (column == -1 || row == -1) return null;
		return (DataAgenda) table.getValueAt(row, column);
	}
	
	private void mudaSalvarCancelar(boolean mod) {
		btnSalvar.setEnabled(mod);
		btnCancelar.setEnabled(mod);
		btnAdicionar.setEnabled(!mod);
		btnExcluir.setEnabled(mod);
	}
	
	/**
	 * Create the panel.
	 */
	public CalendarioPanel(Agenda agenda, LocalDate hoje) {
		setBorder(new MatteBorder(5, 5, 5, 5, (Color) null));
		int anoAtual = hoje.getYear();
		int mesAtual = hoje.getMonthValue();
		addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent evt) {
            	int height = getHeight();
            	int width = getWidth();

            	int fontSize = 5 + ((height + width) / 80);
        		table.setRowHeight(height / 15);
        		table.setFont(new Font(table.getFont().getFontName(), Font.PLAIN, fontSize));
            }
		});
		this.agenda = agenda;
        setBounds(0, 0, 800, 600);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{450, 450, 0};
		gridBagLayout.rowHeights = new int[]{0, 0};
		gridBagLayout.columnWeights = new double[]{1.0, 1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{1.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		
		JPanel panel = new JPanel();
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.insets = new Insets(0, 0, 0, 5);
		gbc_panel.fill = GridBagConstraints.BOTH;
		gbc_panel.gridx = 0;
		gbc_panel.gridy = 0;
		add(panel, gbc_panel);
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[]{0, 0};
		gbl_panel.rowHeights = new int[]{0, 0, 0, 0, 0, 0};
		gbl_panel.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_panel.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
		panel.setLayout(gbl_panel);
		
		JPanel panel_2 = new JPanel();
		GridBagConstraints gbc_panel_2 = new GridBagConstraints();
		gbc_panel_2.insets = new Insets(0, 0, 5, 0);
		gbc_panel_2.fill = GridBagConstraints.BOTH;
		gbc_panel_2.gridx = 0;
		gbc_panel_2.gridy = 0;
		panel.add(panel_2, gbc_panel_2);
		GridBagLayout gbl_panel_2 = new GridBagLayout();
		gbl_panel_2.columnWidths = new int[]{0, 0, 0, 0, 0, 0, 0};
		gbl_panel_2.rowHeights = new int[]{0, 0};
		gbl_panel_2.columnWeights = new double[]{1.0, 1.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		gbl_panel_2.rowWeights = new double[]{0.0, Double.MIN_VALUE};
		panel_2.setLayout(gbl_panel_2);
		
		cbMes = new JComboBox<String>();
		cbMes.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		cbMes.setModel(new DefaultComboBoxModel<String>(new String[] {"Janeiro", "Fevereiro", "Março", "Abril", "Maio", "Junho", "Julho", "Agosto", "Setembro", "Outubro", "Novembro", "Dezembro"}));
		GridBagConstraints gbc_cbMes = new GridBagConstraints();
		gbc_cbMes.insets = new Insets(0, 0, 0, 5);
		gbc_cbMes.fill = GridBagConstraints.HORIZONTAL;
		gbc_cbMes.gridx = 0;
		gbc_cbMes.gridy = 0;
		panel_2.add(cbMes, gbc_cbMes);
		
		cbAno = new JComboBox<Integer>();
		cbAno.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		GridBagConstraints gbc_cbAno = new GridBagConstraints();
		gbc_cbAno.insets = new Insets(0, 0, 0, 5);
		gbc_cbAno.fill = GridBagConstraints.HORIZONTAL;
		gbc_cbAno.gridx = 1;
		gbc_cbAno.gridy = 0;
		panel_2.add(cbAno, gbc_cbAno);
		
		DefaultComboBoxModel<Integer> modelAno = new DefaultComboBoxModel<Integer>();
		int firstYear = hoje.getYear() - 5;
		int lastYear = hoje.getYear() + 5;

		for (int i = firstYear; i <= lastYear; i++) {
			modelAno.addElement(i);
		}
		cbAno.setModel(modelAno);
		cbAno.setSelectedIndex(anoAtual - firstYear);
		
		btnVoltarMes = new JButton("<");
		btnVoltarMes.setToolTipText("Retroceder um mês");
		btnVoltarMes.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		GridBagConstraints gbc_btnVoltarMes = new GridBagConstraints();
		gbc_btnVoltarMes.insets = new Insets(0, 0, 0, 5);
		gbc_btnVoltarMes.gridx = 3;
		gbc_btnVoltarMes.gridy = 0;
		panel_2.add(btnVoltarMes, gbc_btnVoltarMes);
		
		btnVoltarAno = new JButton("<<");
		btnVoltarAno.setToolTipText("Retroceder um ano");
		btnVoltarAno.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnVoltarAno.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int index = cbAno.getSelectedIndex();
				if (index > 0) {
					cbAno.setSelectedIndex(index - 1);
				}
			}
		});
		GridBagConstraints gbc_btnVoltarAno = new GridBagConstraints();
		gbc_btnVoltarAno.insets = new Insets(0, 0, 0, 5);
		gbc_btnVoltarAno.gridx = 2;
		gbc_btnVoltarAno.gridy = 0;
		panel_2.add(btnVoltarAno, gbc_btnVoltarAno);
		
		btnAvancarMes = new JButton(">");
		btnAvancarMes.setToolTipText("Avançar um mês");
		btnAvancarMes.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		GridBagConstraints gbc_btnAvancarMes = new GridBagConstraints();
		gbc_btnAvancarMes.insets = new Insets(0, 0, 0, 5);
		gbc_btnAvancarMes.gridx = 4;
		gbc_btnAvancarMes.gridy = 0;
		panel_2.add(btnAvancarMes, gbc_btnAvancarMes);
		
		JPanel panel_3 = new JPanel();
		panel_3.setBorder(new SoftBevelBorder(BevelBorder.RAISED, null, null, null, null));
		GridBagConstraints gbc_panel_3 = new GridBagConstraints();
		gbc_panel_3.insets = new Insets(0, 0, 5, 0);
		gbc_panel_3.fill = GridBagConstraints.BOTH;
		gbc_panel_3.gridx = 0;
		gbc_panel_3.gridy = 1;
		panel.add(panel_3, gbc_panel_3);
		GridBagLayout gbl_panel_3 = new GridBagLayout();
		gbl_panel_3.columnWidths = new int[]{0, 0, 0, 0, 0, 0, 0, 0};
		gbl_panel_3.rowHeights = new int[]{0, 0};
		gbl_panel_3.columnWeights = new double[]{1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, Double.MIN_VALUE};
		gbl_panel_3.rowWeights = new double[]{0.0, Double.MIN_VALUE};
		panel_3.setLayout(gbl_panel_3);
		
		JLabel lblDomingo = new JLabel("D");
		GridBagConstraints gbc_lblDomingo = new GridBagConstraints();
		gbc_lblDomingo.insets = new Insets(0, 0, 0, 5);
		gbc_lblDomingo.gridx = 0;
		gbc_lblDomingo.gridy = 0;
		panel_3.add(lblDomingo, gbc_lblDomingo);
		
		JLabel lblSegunda = new JLabel("S");
		GridBagConstraints gbc_lblSegunda = new GridBagConstraints();
		gbc_lblSegunda.insets = new Insets(0, 0, 0, 5);
		gbc_lblSegunda.gridx = 1;
		gbc_lblSegunda.gridy = 0;
		panel_3.add(lblSegunda, gbc_lblSegunda);
		
		JLabel lblTerca = new JLabel("T");
		GridBagConstraints gbc_lblTerca = new GridBagConstraints();
		gbc_lblTerca.insets = new Insets(0, 0, 0, 5);
		gbc_lblTerca.gridx = 2;
		gbc_lblTerca.gridy = 0;
		panel_3.add(lblTerca, gbc_lblTerca);
		
		JLabel lblQuarta = new JLabel("Q");
		GridBagConstraints gbc_lblQuarta = new GridBagConstraints();
		gbc_lblQuarta.insets = new Insets(0, 0, 0, 5);
		gbc_lblQuarta.gridx = 3;
		gbc_lblQuarta.gridy = 0;
		panel_3.add(lblQuarta, gbc_lblQuarta);
		
		JLabel lblQuinta = new JLabel("Q");
		GridBagConstraints gbc_lblQuinta = new GridBagConstraints();
		gbc_lblQuinta.insets = new Insets(0, 0, 0, 5);
		gbc_lblQuinta.gridx = 4;
		gbc_lblQuinta.gridy = 0;
		panel_3.add(lblQuinta, gbc_lblQuinta);
		
		JLabel lblSexta = new JLabel("S");
		GridBagConstraints gbc_lblSexta = new GridBagConstraints();
		gbc_lblSexta.insets = new Insets(0, 0, 0, 5);
		gbc_lblSexta.gridx = 5;
		gbc_lblSexta.gridy = 0;
		panel_3.add(lblSexta, gbc_lblSexta);
		
		JLabel lblSabado = new JLabel("S");
		GridBagConstraints gbc_lblSabado = new GridBagConstraints();
		gbc_lblSabado.gridx = 6;
		gbc_lblSabado.gridy = 0;
		panel_3.add(lblSabado, gbc_lblSabado);
		
		JPanel panel_4 = new JPanel();
		GridBagConstraints gbc_panel_4 = new GridBagConstraints();
		gbc_panel_4.insets = new Insets(0, 0, 5, 0);
		gbc_panel_4.fill = GridBagConstraints.BOTH;
		gbc_panel_4.gridx = 0;
		gbc_panel_4.gridy = 2;
		panel.add(panel_4, gbc_panel_4);
		GridBagLayout gbl_panel_4 = new GridBagLayout();
		gbl_panel_4.columnWidths = new int[]{0, 0};
		gbl_panel_4.rowHeights = new int[]{0, 0};
		gbl_panel_4.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_panel_4.rowWeights = new double[]{0.0, Double.MIN_VALUE};
		panel_4.setLayout(gbl_panel_4);
		
		table = new JTable();
		table.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		table.setDefaultRenderer(Object.class, new CalendarCellRenderer());
		table.setColumnSelectionAllowed(true);
		table.setFont(new Font("Consolas", Font.PLAIN, 22));
		table.setRowHeight(48);
		GridBagConstraints gbc_table = new GridBagConstraints();
		gbc_table.fill = GridBagConstraints.HORIZONTAL;
		gbc_table.anchor = GridBagConstraints.NORTH;
		gbc_table.gridx = 0;
		gbc_table.gridy = 0;
		panel_4.add(table, gbc_table);
		
		int anoAtualIndex = anoAtual - firstYear;
		
		cbMes.setSelectedIndex(mesAtual - 1);
		cbAno.setSelectedIndex(anoAtualIndex);
		
		btnAvancarAno = new JButton(">>");
		btnAvancarAno.setToolTipText("Avançar um ano");
		btnAvancarAno.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		GridBagConstraints gbc_btnAvancarAno = new GridBagConstraints();
		gbc_btnAvancarAno.gridx = 5;
		gbc_btnAvancarAno.gridy = 0;
		panel_2.add(btnAvancarAno, gbc_btnAvancarAno);
		
		btnAvancarMes.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int index01 = cbMes.getSelectedIndex();
				if (index01 == 11) {
					cbMes.setSelectedIndex(0);
					int anos = cbAno.getModel().getSize();
					int index02 = cbAno.getSelectedIndex();
					if (index02 < anos - 1) {
						cbAno.setSelectedIndex(index02 + 1);
					}
				} else {
					cbMes.setSelectedIndex(index01 + 1);
				}
				mudaAvancarVoltar();
			}
		});
		
		btnVoltarMes.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int index01 = cbMes.getSelectedIndex();
				if (index01 == 0) {
					int index02 = cbAno.getSelectedIndex();
					if (index02 > 0) {
						cbMes.setSelectedIndex(11);
						cbAno.setSelectedIndex(index02 - 1);
					}
				} else {
					cbMes.setSelectedIndex(index01 - 1);
				}
				mudaAvancarVoltar();
			}
		});
		
		btnAvancarAno.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int index = cbAno.getSelectedIndex();
				if (index < cbAno.getModel().getSize() - 1) {
					cbAno.setSelectedIndex(index + 1);
				}
			}
		});
		cbAno.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				atualizaCalendario(cbMes.getSelectedIndex() + 1, (int) cbAno.getSelectedItem());
				mudaAvancarVoltar();
			}
		});
		
		cbMes.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				atualizaCalendario(cbMes.getSelectedIndex() + 1, (int) cbAno.getSelectedItem());
				mudaAvancarVoltar();
			}
		});
		
		btnHoje = new JButton("Hoje");
		btnHoje.setToolTipText("Selecionar dia atual");
		btnHoje.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnHoje.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setDay(hoje);
			}
		});
		GridBagConstraints gbc_btnHoje = new GridBagConstraints();
		gbc_btnHoje.anchor = GridBagConstraints.EAST;
		gbc_btnHoje.insets = new Insets(0, 0, 5, 0);
		gbc_btnHoje.gridx = 0;
		gbc_btnHoje.gridy = 3;
		panel.add(btnHoje, gbc_btnHoje);
		
		JPanel panel_5 = new JPanel();
		GridBagConstraints gbc_panel_5 = new GridBagConstraints();
		gbc_panel_5.fill = GridBagConstraints.BOTH;
		gbc_panel_5.gridx = 0;
		gbc_panel_5.gridy = 4;
		panel.add(panel_5, gbc_panel_5);
		GridBagLayout gbl_panel_5 = new GridBagLayout();
		gbl_panel_5.columnWidths = new int[]{0, 0};
		gbl_panel_5.rowHeights = new int[]{0, 0, 0, 0};
		gbl_panel_5.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_panel_5.rowWeights = new double[]{0.0, 1.0, 0.0, Double.MIN_VALUE};
		panel_5.setLayout(gbl_panel_5);
		
		lblData = new JLabel("dd/mm/aaaa - dia semana");
		GridBagConstraints gbc_lblData = new GridBagConstraints();
		gbc_lblData.insets = new Insets(0, 0, 5, 0);
		gbc_lblData.gridx = 0;
		gbc_lblData.gridy = 0;
		panel_5.add(lblData, gbc_lblData);
		
		JScrollPane scrollPane = new JScrollPane();
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.insets = new Insets(0, 0, 5, 0);
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridx = 0;
		gbc_scrollPane.gridy = 1;
		panel_5.add(scrollPane, gbc_scrollPane);
		
		list = new JList<Evento>();
		list.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		list.addListSelectionListener(new ListSelectionListener() {
			
			@Override
			public void valueChanged(ListSelectionEvent e) {
				carregaEvento(list.getSelectedValue());
			}
		});
		scrollPane.setViewportView(list);
		
		JPanel panel_10 = new JPanel();
		GridBagConstraints gbc_panel_10 = new GridBagConstraints();
		gbc_panel_10.fill = GridBagConstraints.BOTH;
		gbc_panel_10.gridx = 0;
		gbc_panel_10.gridy = 2;
		panel_5.add(panel_10, gbc_panel_10);
		GridBagLayout gbl_panel_10 = new GridBagLayout();
		gbl_panel_10.columnWidths = new int[]{0, 0, 0};
		gbl_panel_10.rowHeights = new int[]{0, 0};
		gbl_panel_10.columnWeights = new double[]{1.0, 0.0, Double.MIN_VALUE};
		gbl_panel_10.rowWeights = new double[]{0.0, Double.MIN_VALUE};
		panel_10.setLayout(gbl_panel_10);
		
		btnAdicionar = new JButton("Adicionar");
		btnAdicionar.setToolTipText("Adicionar um novo evento básico");
		btnAdicionar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnAdicionar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				DataAgenda dataAgenda = getTableSelectedValue();
				adicionaEvento(dataAgenda);
			}
		});
		
		table.getColumnModel().getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			
			@Override
			public void valueChanged(ListSelectionEvent e) {

				if (!e.getValueIsAdjusting()) {
					DataAgenda dataAgenda = getTableSelectedValue();
					if (dataAgenda == null) {
						btnAdicionar.setEnabled(false);
					} else {
						btnAdicionar.setEnabled(true);
						atualizaLista(dataAgenda);
					}
					selecionaData(dataAgenda);
				}
			}
		});
		
		table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			
			@Override
			public void valueChanged(ListSelectionEvent e) {

				if (!e.getValueIsAdjusting()) {
					DataAgenda dataAgenda = getTableSelectedValue();
					if (dataAgenda == null) {
						btnAdicionar.setEnabled(false);
					} else {
						btnAdicionar.setEnabled(true);
					}
					atualizaLista(dataAgenda);
					selecionaData(dataAgenda);
				}
			}
		});
		GridBagConstraints gbc_btnAdicionar = new GridBagConstraints();
		gbc_btnAdicionar.anchor = GridBagConstraints.EAST;
		gbc_btnAdicionar.insets = new Insets(0, 0, 0, 5);
		gbc_btnAdicionar.gridx = 0;
		gbc_btnAdicionar.gridy = 0;
		panel_10.add(btnAdicionar, gbc_btnAdicionar);
		
		btnExcluir = new JButton("Excluir");
		btnExcluir.setToolTipText("Excluir o evento selecionado");
		btnExcluir.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnExcluir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				removeEventos((DataAgenda)getTableSelectedValue());
			}
		});
		GridBagConstraints gbc_btnExcluir_1 = new GridBagConstraints();
		gbc_btnExcluir_1.gridx = 1;
		gbc_btnExcluir_1.gridy = 0;
		panel_10.add(btnExcluir, gbc_btnExcluir_1);
		
		JPanel panel_1 = new JPanel();
		GridBagConstraints gbc_panel_1 = new GridBagConstraints();
		gbc_panel_1.fill = GridBagConstraints.BOTH;
		gbc_panel_1.gridx = 1;
		gbc_panel_1.gridy = 0;
		add(panel_1, gbc_panel_1);
		GridBagLayout gbl_panel_1 = new GridBagLayout();
		gbl_panel_1.columnWidths = new int[]{0, 0};
		gbl_panel_1.rowHeights = new int[]{0, 0, 0};
		gbl_panel_1.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_panel_1.rowWeights = new double[]{1.0, 1.0, Double.MIN_VALUE};
		panel_1.setLayout(gbl_panel_1);
		
		JPanel panel_6 = new JPanel();
		GridBagConstraints gbc_panel_6 = new GridBagConstraints();
		gbc_panel_6.insets = new Insets(0, 0, 5, 0);
		gbc_panel_6.fill = GridBagConstraints.BOTH;
		gbc_panel_6.gridx = 0;
		gbc_panel_6.gridy = 0;
		panel_1.add(panel_6, gbc_panel_6);
		GridBagLayout gbl_panel_6 = new GridBagLayout();
		gbl_panel_6.columnWidths = new int[]{0, 0, 0};
		gbl_panel_6.rowHeights = new int[]{0, 0, 0, 0, 0};
		gbl_panel_6.columnWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		gbl_panel_6.rowWeights = new double[]{0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
		panel_6.setLayout(gbl_panel_6);
		
		lblNome = new JLabel("Nome:");
		GridBagConstraints gbc_lblNome = new GridBagConstraints();
		gbc_lblNome.insets = new Insets(0, 0, 5, 5);
		gbc_lblNome.gridx = 0;
		gbc_lblNome.gridy = 0;
		panel_6.add(lblNome, gbc_lblNome);
		
		tfNome = new JTextField();
		GridBagConstraints gbc_tfNome = new GridBagConstraints();
		gbc_tfNome.insets = new Insets(0, 0, 5, 0);
		gbc_tfNome.fill = GridBagConstraints.HORIZONTAL;
		gbc_tfNome.gridx = 1;
		gbc_tfNome.gridy = 0;
		panel_6.add(tfNome, gbc_tfNome);
		tfNome.setColumns(10);
		
		lblHorario = new JLabel("Horário:");
		GridBagConstraints gbc_lblHorario = new GridBagConstraints();
		gbc_lblHorario.insets = new Insets(0, 0, 5, 5);
		gbc_lblHorario.gridx = 0;
		gbc_lblHorario.gridy = 1;
		panel_6.add(lblHorario, gbc_lblHorario);
		
		JPanel panel_8 = new JPanel();
		GridBagConstraints gbc_panel_8 = new GridBagConstraints();
		gbc_panel_8.insets = new Insets(0, 0, 5, 0);
		gbc_panel_8.fill = GridBagConstraints.BOTH;
		gbc_panel_8.gridx = 1;
		gbc_panel_8.gridy = 1;
		panel_6.add(panel_8, gbc_panel_8);
		GridBagLayout gbl_panel_8 = new GridBagLayout();
		gbl_panel_8.columnWidths = new int[]{0, 0, 0, 0, 0, 0};
		gbl_panel_8.rowHeights = new int[]{0, 0};
		gbl_panel_8.columnWeights = new double[]{1.0, 0.0, 1.0, 0.0, 1.0, Double.MIN_VALUE};
		gbl_panel_8.rowWeights = new double[]{0.0, Double.MIN_VALUE};
		panel_8.setLayout(gbl_panel_8);
		
		spinnerHora = new JSpinner();
		spinnerHora.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		spinnerHora.setModel(new SpinnerNumberModel(0, 0, 23, 1));
		GridBagConstraints gbc_spinnerHora = new GridBagConstraints();
		gbc_spinnerHora.fill = GridBagConstraints.HORIZONTAL;
		gbc_spinnerHora.insets = new Insets(0, 0, 0, 5);
		gbc_spinnerHora.gridx = 0;
		gbc_spinnerHora.gridy = 0;
		panel_8.add(spinnerHora, gbc_spinnerHora);
		
		lblDoisPontos = new JLabel(":");
		GridBagConstraints gbc_lblDoisPontos = new GridBagConstraints();
		gbc_lblDoisPontos.insets = new Insets(0, 0, 0, 5);
		gbc_lblDoisPontos.gridx = 1;
		gbc_lblDoisPontos.gridy = 0;
		panel_8.add(lblDoisPontos, gbc_lblDoisPontos);
		
		spinnerMinuto = new JSpinner();
		spinnerMinuto.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		spinnerMinuto.setModel(new SpinnerNumberModel(0, 0, 59, 1));
		GridBagConstraints gbc_spinnerMinuto = new GridBagConstraints();
		gbc_spinnerMinuto.fill = GridBagConstraints.HORIZONTAL;
		gbc_spinnerMinuto.insets = new Insets(0, 0, 0, 5);
		gbc_spinnerMinuto.gridx = 2;
		gbc_spinnerMinuto.gridy = 0;
		panel_8.add(spinnerMinuto, gbc_spinnerMinuto);
		
		lblDuracao = new JLabel("Duração(min):");
		GridBagConstraints gbc_lblDuracao = new GridBagConstraints();
		gbc_lblDuracao.insets = new Insets(0, 0, 0, 5);
		gbc_lblDuracao.gridx = 3;
		gbc_lblDuracao.gridy = 0;
		panel_8.add(lblDuracao, gbc_lblDuracao);
		
		spinnerTempo = new JSpinner();
		spinnerTempo.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		spinnerTempo.setModel(new SpinnerNumberModel(0, 0, 1440, 5));
		GridBagConstraints gbc_spinnerTempo = new GridBagConstraints();
		gbc_spinnerTempo.fill = GridBagConstraints.HORIZONTAL;
		gbc_spinnerTempo.gridx = 4;
		gbc_spinnerTempo.gridy = 0;
		panel_8.add(spinnerTempo, gbc_spinnerTempo);
		
		lblPrioridade = new JLabel("Prioridade:");
		GridBagConstraints gbc_lblPrioridade = new GridBagConstraints();
		gbc_lblPrioridade.insets = new Insets(0, 0, 5, 5);
		gbc_lblPrioridade.gridx = 0;
		gbc_lblPrioridade.gridy = 2;
		panel_6.add(lblPrioridade, gbc_lblPrioridade);
		
		JPanel panel_9 = new JPanel();
		GridBagConstraints gbc_panel_9 = new GridBagConstraints();
		gbc_panel_9.insets = new Insets(0, 0, 5, 0);
		gbc_panel_9.fill = GridBagConstraints.BOTH;
		gbc_panel_9.gridx = 1;
		gbc_panel_9.gridy = 2;
		panel_6.add(panel_9, gbc_panel_9);
		GridBagLayout gbl_panel_9 = new GridBagLayout();
		gbl_panel_9.columnWidths = new int[]{0, 0, 0, 0, 0};
		gbl_panel_9.rowHeights = new int[]{0, 0};
		gbl_panel_9.columnWeights = new double[]{0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
		gbl_panel_9.rowWeights = new double[]{0.0, Double.MIN_VALUE};
		panel_9.setLayout(gbl_panel_9);
		
		rdbtnBaixa = new JRadioButton("Baixa");
		rdbtnBaixa.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		GridBagConstraints gbc_rdbtnBaixa = new GridBagConstraints();
		gbc_rdbtnBaixa.anchor = GridBagConstraints.EAST;
		gbc_rdbtnBaixa.insets = new Insets(0, 0, 0, 5);
		gbc_rdbtnBaixa.gridx = 0;
		gbc_rdbtnBaixa.gridy = 0;
		panel_9.add(rdbtnBaixa, gbc_rdbtnBaixa);
		
		rdbtnMedia = new JRadioButton("Média");
		rdbtnMedia.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		GridBagConstraints gbc_rdbtnMedia = new GridBagConstraints();
		gbc_rdbtnMedia.insets = new Insets(0, 0, 0, 5);
		gbc_rdbtnMedia.gridx = 1;
		gbc_rdbtnMedia.gridy = 0;
		panel_9.add(rdbtnMedia, gbc_rdbtnMedia);
		
		rdbtnAlta = new JRadioButton("Alta");
		rdbtnAlta.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		GridBagConstraints gbc_rdbtnAlta = new GridBagConstraints();
		gbc_rdbtnAlta.insets = new Insets(0, 0, 0, 5);
		gbc_rdbtnAlta.gridx = 2;
		gbc_rdbtnAlta.gridy = 0;
		panel_9.add(rdbtnAlta, gbc_rdbtnAlta);
		
		buttonGroup = new ButtonGroup();
		buttonGroup.add(rdbtnBaixa);
		buttonGroup.add(rdbtnMedia);
		buttonGroup.add(rdbtnAlta);

		chckbxFinalizado = new JCheckBox("Finalizado");
		chckbxFinalizado.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		GridBagConstraints gbc_chckbxFinalizado = new GridBagConstraints();
		gbc_chckbxFinalizado.anchor = GridBagConstraints.EAST;
		gbc_chckbxFinalizado.gridx = 3;
		gbc_chckbxFinalizado.gridy = 0;
		panel_9.add(chckbxFinalizado, gbc_chckbxFinalizado);
		
		lblDescricao = new JLabel("Descrição:");
		GridBagConstraints gbc_lblDescricao = new GridBagConstraints();
		gbc_lblDescricao.anchor = GridBagConstraints.NORTH;
		gbc_lblDescricao.insets = new Insets(0, 0, 0, 5);
		gbc_lblDescricao.gridx = 0;
		gbc_lblDescricao.gridy = 3;
		panel_6.add(lblDescricao, gbc_lblDescricao);
		
		scrollPane_1 = new JScrollPane();
		scrollPane_1.setPreferredSize(new Dimension(0, 0));
		scrollPane_1.setBorder(null);
		scrollPane_1.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		GridBagConstraints gbc_scrollPane_1 = new GridBagConstraints();
		gbc_scrollPane_1.fill = GridBagConstraints.BOTH;
		gbc_scrollPane_1.gridx = 1;
		gbc_scrollPane_1.gridy = 3;
		panel_6.add(scrollPane_1, gbc_scrollPane_1);
		
		taDescricao = new JTextArea();
		taDescricao.setBorder(UIManager.getBorder("TextField.border"));
		taDescricao.setMargin(new Insets(0, 0, 0, 0));
		taDescricao.setLineWrap(true);
		scrollPane_1.setViewportView(taDescricao);
		
		JPanel panel_7 = new JPanel();
		GridBagConstraints gbc_panel_7 = new GridBagConstraints();
		gbc_panel_7.fill = GridBagConstraints.BOTH;
		gbc_panel_7.gridx = 0;
		gbc_panel_7.gridy = 1;
		panel_1.add(panel_7, gbc_panel_7);
		GridBagLayout gbl_panel_7 = new GridBagLayout();
		gbl_panel_7.columnWidths = new int[]{0, 0, 0};
		gbl_panel_7.rowHeights = new int[]{0, 0};
		gbl_panel_7.columnWeights = new double[]{1.0, 0.0, Double.MIN_VALUE};
		gbl_panel_7.rowWeights = new double[]{0.0, Double.MIN_VALUE};
		panel_7.setLayout(gbl_panel_7);
		
		btnSalvar = new JButton("Salvar");
		btnSalvar.setToolTipText("Salvar alterações do evento");
		btnSalvar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnSalvar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				salvaEvento(list.getSelectedValue());
			}
		});
		GridBagConstraints gbc_btnSalvar = new GridBagConstraints();
		gbc_btnSalvar.anchor = GridBagConstraints.EAST;
		gbc_btnSalvar.insets = new Insets(0, 0, 0, 5);
		gbc_btnSalvar.gridx = 0;
		gbc_btnSalvar.gridy = 0;
		panel_7.add(btnSalvar, gbc_btnSalvar);
		
		btnCancelar = new JButton("Cancelar");
		btnCancelar.setToolTipText("Descartar alterações do evento");
		btnCancelar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnCancelar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				limpaSelecao();
			}
		});
		GridBagConstraints gbc_btnCancelar = new GridBagConstraints();
		gbc_btnCancelar.gridx = 1;
		gbc_btnCancelar.gridy = 0;
		panel_7.add(btnCancelar, gbc_btnCancelar);
		mudaSalvarCancelar(false);
		habilitaCamposPreenchimento(false);
		atualizaCalendario();
	}
}
