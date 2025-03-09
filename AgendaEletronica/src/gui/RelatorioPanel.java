package gui;

import javax.swing.JPanel;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.ArrayList;
import javax.swing.JTextField;
import backend.Agenda;
import backend.Evento;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JComboBox;
import javax.swing.JCheckBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import java.awt.Cursor;
import javax.swing.border.MatteBorder;
import java.awt.Color;

public class RelatorioPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JScrollPane scrollPane;
	private JTextArea taOutput;
	private Agenda agenda;
	private JTextField tfPesquisa;
	private JButton btnPesquisa;
	private JLabel lblInicio;
	private JComboBox<Integer> cbDia01;
	private JComboBox<String> cbMes01;
	private JComboBox<Integer> cbAno01;
	private JLabel lblFim;
	private JComboBox<Integer> cbDia02;
	private JComboBox<String> cbMes02;
	private JComboBox<Integer> cbAno02;
	private JCheckBox chckbxQualquerNome;
	private JCheckBox chckbxQualquerData;
	private JButton btnCopiar;
	
	private LocalDate getDataStart() {
		int ano = (int) cbAno01.getSelectedItem();
		int mes = cbMes01.getSelectedIndex() + 1;
		int dia = cbDia01.getSelectedIndex() + 1;
		
		return LocalDate.of(ano, mes, dia);
	}
	
	private LocalDate getDataEnd() {
		int ano = (int) cbAno02.getSelectedItem();
		int mes = cbMes02.getSelectedIndex() + 1;
		int dia = cbDia02.getSelectedIndex() + 1;
		
		return LocalDate.of(ano, mes, dia);
	}
	
	private String getDataString(Evento evento) {
		LocalDate data = evento.getData();
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/uuuu");
		return data.format(dtf);
	}
	
	private String getPrioridadeString(Evento evento) {
		int prioridade = evento.getPrioridade();
		if (prioridade == Evento.PRIORIDADE_BAIXA) {
			return "Baixa";
		}
		if (prioridade == Evento.PRIORIDADE_MEDIA) {
			return "Média";
		}
		return "Alta";
	}
	
	private String getBooleanString(Evento evento) {
		if (evento.isFinalizado()) {
			return "Sim";
		}
		return "Não";
	}
	
	private void pesquisa() {
		ArrayList<Evento> eventos;
		String output = "";
		LocalDate inicio = null;
		LocalDate fim = null;
		String nome = null;
		if (!chckbxQualquerData.isSelected()) {
			inicio = getDataStart();
			fim = getDataEnd();
		}
		if (!chckbxQualquerNome.isSelected()) {
			nome = tfPesquisa.getText();
		}
		eventos = agenda.pesquisaEventos(inicio, fim, nome);
		
		int i = 0;
		int size = eventos.size();
		
		for (Evento evento : eventos) {
			output += getDataString(evento) + '(' + evento.getData().getDayOfWeek()
					  .getDisplayName(TextStyle.SHORT, getLocale()).replace(".", "") + ')' + " - " + 
					  evento.getHorario() + "\nNome: " + evento.getNome() + "\nDuração: " +
					  evento.getMinutos() + " minutos" + "\nPrioridade: " +
					  getPrioridadeString(evento) +"\nFinalizado: " + getBooleanString(evento);
			i++;
			
			if (i < size) {
				output+= "\n\n";
			}
		}
		if (size == 0) {
			output = "Não foi encontrado nenhum evento com essas especificações.";
		}
		
		taOutput.setText(output);
	}
	
	private void copiaDados(String dados) {
		StringSelection selection = new StringSelection(dados);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(selection, null);
		JOptionPane.showMessageDialog(this, "Copiado para a área de transferência.");
	}
	
	/**
	 * Create the panel.
	 */
	public RelatorioPanel(Agenda agenda, LocalDate hoje) {
		setBorder(new MatteBorder(5, 5, 5, 5, (Color) null));
		this.agenda = agenda;
		int diaAtual = hoje.getDayOfMonth();
		int mesAtual = hoje.getMonthValue();
		int anoAtual = hoje.getYear();
		
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0};
		gridBagLayout.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{1.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		
		JPanel panel = new JPanel();
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.fill = GridBagConstraints.BOTH;
		gbc_panel.gridx = 0;
		gbc_panel.gridy = 0;
		add(panel, gbc_panel);
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[]{0, 0};
		gbl_panel.rowHeights = new int[]{0, 0, 0};
		gbl_panel.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_panel.rowWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		panel.setLayout(gbl_panel);
		
		JPanel panel_2 = new JPanel();
		GridBagConstraints gbc_panel_2 = new GridBagConstraints();
		gbc_panel_2.insets = new Insets(0, 0, 5, 0);
		gbc_panel_2.fill = GridBagConstraints.BOTH;
		gbc_panel_2.gridx = 0;
		gbc_panel_2.gridy = 0;
		panel.add(panel_2, gbc_panel_2);
		GridBagLayout gbl_panel_2 = new GridBagLayout();
		gbl_panel_2.columnWidths = new int[]{0, 0, 0};
		gbl_panel_2.rowHeights = new int[]{0, 0, 0, 0};
		gbl_panel_2.columnWeights = new double[]{1.0, 0.0, Double.MIN_VALUE};
		gbl_panel_2.rowWeights = new double[]{0.0, 1.0, 0.0, Double.MIN_VALUE};
		panel_2.setLayout(gbl_panel_2);
		
		JPanel panel_4 = new JPanel();
		GridBagConstraints gbc_panel_4 = new GridBagConstraints();
		gbc_panel_4.insets = new Insets(0, 0, 5, 5);
		gbc_panel_4.fill = GridBagConstraints.BOTH;
		gbc_panel_4.gridx = 0;
		gbc_panel_4.gridy = 0;
		panel_2.add(panel_4, gbc_panel_4);
		GridBagLayout gbl_panel_4 = new GridBagLayout();
		gbl_panel_4.columnWidths = new int[]{0, 0};
		gbl_panel_4.rowHeights = new int[]{0, 0};
		gbl_panel_4.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_panel_4.rowWeights = new double[]{0.0, Double.MIN_VALUE};
		panel_4.setLayout(gbl_panel_4);
		
		tfPesquisa = new JTextField();
		tfPesquisa.setToolTipText("Digitar aqui o nome do(s) evento(s)");
		tfPesquisa.setColumns(10);
		GridBagConstraints gbc_tfPesquisa = new GridBagConstraints();
		gbc_tfPesquisa.fill = GridBagConstraints.HORIZONTAL;
		gbc_tfPesquisa.gridx = 0;
		gbc_tfPesquisa.gridy = 0;
		panel_4.add(tfPesquisa, gbc_tfPesquisa);
		
		chckbxQualquerNome = new JCheckBox("Qualquer nome");
		chckbxQualquerNome.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		GridBagConstraints gbc_chckbxQualquerNome = new GridBagConstraints();
		gbc_chckbxQualquerNome.anchor = GridBagConstraints.WEST;
		gbc_chckbxQualquerNome.insets = new Insets(0, 0, 5, 0);
		gbc_chckbxQualquerNome.gridx = 1;
		gbc_chckbxQualquerNome.gridy = 0;
		panel_2.add(chckbxQualquerNome, gbc_chckbxQualquerNome);
		
		chckbxQualquerNome.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if (chckbxQualquerNome.isSelected()) {
					tfPesquisa.setEnabled(false);
				} else {
					tfPesquisa.setEnabled(true);
				}
			}
		});
		
		JPanel panel_5 = new JPanel();
		GridBagConstraints gbc_panel_5 = new GridBagConstraints();
		gbc_panel_5.insets = new Insets(0, 0, 5, 5);
		gbc_panel_5.fill = GridBagConstraints.BOTH;
		gbc_panel_5.gridx = 0;
		gbc_panel_5.gridy = 1;
		panel_2.add(panel_5, gbc_panel_5);
		GridBagLayout gbl_panel_5 = new GridBagLayout();
		gbl_panel_5.columnWidths = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		gbl_panel_5.rowHeights = new int[]{0, 0};
		gbl_panel_5.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
		gbl_panel_5.rowWeights = new double[]{0.0, Double.MIN_VALUE};
		panel_5.setLayout(gbl_panel_5);
		
		lblInicio = new JLabel("De:");
		GridBagConstraints gbc_lblInicio = new GridBagConstraints();
		gbc_lblInicio.anchor = GridBagConstraints.EAST;
		gbc_lblInicio.insets = new Insets(0, 0, 0, 5);
		gbc_lblInicio.gridx = 0;
		gbc_lblInicio.gridy = 0;
		panel_5.add(lblInicio, gbc_lblInicio);
		
		cbDia01 = new JComboBox<Integer>();
		cbDia01.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		GridBagConstraints gbc_cbDia01 = new GridBagConstraints();
		gbc_cbDia01.fill = GridBagConstraints.HORIZONTAL;
		gbc_cbDia01.insets = new Insets(0, 0, 0, 5);
		gbc_cbDia01.gridx = 1;
		gbc_cbDia01.gridy = 0;
		panel_5.add(cbDia01, gbc_cbDia01);
		
		cbMes01 = new JComboBox<String>();
		cbMes01.setModel(new DefaultComboBoxModel<String>(new String[] {"Janeiro", "Fevereiro", "Março", "Abril", "Maio", "Junho", "Julho", "Agosto", "Setembro", "Outubro", "Novembro", "Dezembro"}));
		cbMes01.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		GridBagConstraints gbc_cbMes01 = new GridBagConstraints();
		gbc_cbMes01.fill = GridBagConstraints.HORIZONTAL;
		gbc_cbMes01.insets = new Insets(0, 0, 0, 5);
		gbc_cbMes01.gridx = 2;
		gbc_cbMes01.gridy = 0;
		panel_5.add(cbMes01, gbc_cbMes01);
		
		cbAno01 = new JComboBox<Integer>();
		cbAno01.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		GridBagConstraints gbc_cbAno01 = new GridBagConstraints();
		gbc_cbAno01.fill = GridBagConstraints.HORIZONTAL;
		gbc_cbAno01.insets = new Insets(0, 0, 0, 5);
		gbc_cbAno01.gridx = 3;
		gbc_cbAno01.gridy = 0;
		panel_5.add(cbAno01, gbc_cbAno01);
		
		lblFim = new JLabel("Até:");
		GridBagConstraints gbc_lblFim = new GridBagConstraints();
		gbc_lblFim.anchor = GridBagConstraints.EAST;
		gbc_lblFim.insets = new Insets(0, 0, 0, 5);
		gbc_lblFim.gridx = 4;
		gbc_lblFim.gridy = 0;
		panel_5.add(lblFim, gbc_lblFim);
		
		cbDia02 = new JComboBox<Integer>();
		cbDia02.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		GridBagConstraints gbc_cbDia02 = new GridBagConstraints();
		gbc_cbDia02.fill = GridBagConstraints.HORIZONTAL;
		gbc_cbDia02.insets = new Insets(0, 0, 0, 5);
		gbc_cbDia02.gridx = 5;
		gbc_cbDia02.gridy = 0;
		panel_5.add(cbDia02, gbc_cbDia02);
		
		cbMes02 = new JComboBox<String>();
		cbMes02.setModel(new DefaultComboBoxModel<String>(new String[] {"Janeiro", "Fevereiro", "Março", "Abril", "Maio", "Junho", "Julho", "Agosto", "Setembro", "Outubro", "Novembro", "Dezembro"}));
		cbMes02.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		GridBagConstraints gbc_cbMes02 = new GridBagConstraints();
		gbc_cbMes02.fill = GridBagConstraints.HORIZONTAL;
		gbc_cbMes02.insets = new Insets(0, 0, 0, 5);
		gbc_cbMes02.gridx = 6;
		gbc_cbMes02.gridy = 0;
		panel_5.add(cbMes02, gbc_cbMes02);
		
		cbAno02 = new JComboBox<Integer>();
		cbAno02.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		GridBagConstraints gbc_cbAno02 = new GridBagConstraints();
		gbc_cbAno02.fill = GridBagConstraints.HORIZONTAL;
		gbc_cbAno02.insets = new Insets(0, 0, 0, 5);
		gbc_cbAno02.gridx = 7;
		gbc_cbAno02.gridy = 0;
		panel_5.add(cbAno02, gbc_cbAno02);
		
		
		DefaultComboBoxModel<Integer> modelDia01 = new DefaultComboBoxModel<Integer>();
		DefaultComboBoxModel<Integer> modelDia02 = new DefaultComboBoxModel<Integer>();
		
		for (int i = 1; i <= 31; i++) {
			modelDia01.addElement(i);
			modelDia02.addElement(i);
		}
		
		cbDia01.setModel(modelDia01);
		cbDia02.setModel(modelDia02);
		
		DefaultComboBoxModel<Integer> modelAno01 = new DefaultComboBoxModel<Integer>();
		DefaultComboBoxModel<Integer> modelAno02 = new DefaultComboBoxModel<Integer>();
		
		for (int i = anoAtual - 5; i <= anoAtual + 5; i++) {
			modelAno01.addElement(i);
			modelAno02.addElement(i);
		}
		
		cbAno01.setModel(modelAno01);
		cbAno02.setModel(modelAno02);
		
		int diaIndex = diaAtual - 1;
		int mesIndex = mesAtual - 1;
		
		cbDia01.setSelectedIndex(diaIndex);
		cbDia02.setSelectedIndex(diaIndex);
		cbMes01.setSelectedIndex(mesIndex);
		cbMes02.setSelectedIndex(mesIndex);
		cbAno01.setSelectedItem(anoAtual);
		cbAno02.setSelectedItem(anoAtual);
		
		
		chckbxQualquerData = new JCheckBox("Qualquer data");
		chckbxQualquerData.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		GridBagConstraints gbc_chckbxQualquerData = new GridBagConstraints();
		gbc_chckbxQualquerData.insets = new Insets(0, 0, 5, 0);
		gbc_chckbxQualquerData.anchor = GridBagConstraints.NORTHWEST;
		gbc_chckbxQualquerData.gridx = 1;
		gbc_chckbxQualquerData.gridy = 1;
		panel_2.add(chckbxQualquerData, gbc_chckbxQualquerData);
		
		btnPesquisa = new JButton("Gerar relatório");
		btnPesquisa.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				pesquisa();
			}
		});
		btnPesquisa.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		GridBagConstraints gbc_btnPesquisa = new GridBagConstraints();
		gbc_btnPesquisa.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnPesquisa.gridx = 1;
		gbc_btnPesquisa.gridy = 2;
		panel_2.add(btnPesquisa, gbc_btnPesquisa);
		
		chckbxQualquerData.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if (chckbxQualquerData.isSelected()) {
					lblInicio.setEnabled(false);
					cbDia01.setEnabled(false);
					cbMes01.setEnabled(false);
					cbAno01.setEnabled(false);
					lblFim.setEnabled(false);
					cbDia02.setEnabled(false);
					cbMes02.setEnabled(false);
					cbAno02.setEnabled(false);
				} else {
					lblInicio.setEnabled(true);
					cbDia01.setEnabled(true);
					cbMes01.setEnabled(true);
					cbAno01.setEnabled(true);
					lblFim.setEnabled(true);
					cbDia02.setEnabled(true);
					cbMes02.setEnabled(true);
					cbAno02.setEnabled(true);
				}
			}
		});
		
		JPanel panel_3 = new JPanel();
		GridBagConstraints gbc_panel_3 = new GridBagConstraints();
		gbc_panel_3.fill = GridBagConstraints.BOTH;
		gbc_panel_3.gridx = 0;
		gbc_panel_3.gridy = 1;
		panel.add(panel_3, gbc_panel_3);
		GridBagLayout gbl_panel_3 = new GridBagLayout();
		gbl_panel_3.columnWidths = new int[]{0, 0};
		gbl_panel_3.rowHeights = new int[]{0, 0, 0};
		gbl_panel_3.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_panel_3.rowWeights = new double[]{1.0, 0.0, Double.MIN_VALUE};
		panel_3.setLayout(gbl_panel_3);
		
		scrollPane = new JScrollPane();
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.insets = new Insets(0, 0, 5, 0);
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridx = 0;
		gbc_scrollPane.gridy = 0;
		panel_3.add(scrollPane, gbc_scrollPane);
		
		taOutput = new JTextArea();
		taOutput.setEditable(false);
		scrollPane.setViewportView(taOutput);
		
		JPanel panel_1 = new JPanel();
		GridBagConstraints gbc_panel_1 = new GridBagConstraints();
		gbc_panel_1.fill = GridBagConstraints.BOTH;
		gbc_panel_1.gridx = 0;
		gbc_panel_1.gridy = 1;
		panel_3.add(panel_1, gbc_panel_1);
		GridBagLayout gbl_panel_1 = new GridBagLayout();
		gbl_panel_1.columnWidths = new int[]{0, 0};
		gbl_panel_1.rowHeights = new int[]{0, 0};
		gbl_panel_1.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_panel_1.rowWeights = new double[]{0.0, Double.MIN_VALUE};
		panel_1.setLayout(gbl_panel_1);
		
		btnCopiar = new JButton("Copiar");
		btnCopiar.setToolTipText("Copiar o relatório gerado para a área de transferência");
		btnCopiar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				copiaDados(taOutput.getText());
			}
		});
		btnCopiar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		GridBagConstraints gbc_btnCopiar = new GridBagConstraints();
		gbc_btnCopiar.anchor = GridBagConstraints.EAST;
		gbc_btnCopiar.gridx = 0;
		gbc_btnCopiar.gridy = 0;
		panel_1.add(btnCopiar, gbc_btnCopiar);
		
	}
}
