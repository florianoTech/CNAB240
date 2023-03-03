package br.com.cnab240.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.apache.commons.lang3.StringUtils;

import br.com.cnab240.util.SizeSpecification;
import br.com.cnab240.util.TypeSpecification;
import br.com.cnab240.core.ArquivoCore;
import br.com.cnab240.core.ConteudoCore;
import br.com.cnab240.core.ConvenenteCore;
import br.com.cnab240.dao.ConvenenteDAO;
import br.com.cnab240.entity.Convenente;

public class CNAB240GUI extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// Variáveis
	EntityManagerFactory emf = Persistence.createEntityManagerFactory("Convenente");
	EntityManager em = emf.createEntityManager();
	private ConvenenteCore convenenteCore = new ConvenenteCore();
	private ConvenenteDAO dao = new ConvenenteDAO(em);
	private ArrayList<String> label;
	private String caminhoEntrada;
	private String nomeArqEntrada;

	// Painéis
	private JPanel jPanel = new JPanel();
	private JPanel arquivoPanel = new JPanel();
	private JPanel clientePanel = new JPanel();
	private JPanel infoBancoPanel = new JPanel();
	private JPanel enderecoPanel = new JPanel();

	// Componentes
	private JLabel convenente = new JLabel("Cliente:");
	private JLabel nome = new JLabel("Nome:");
	private JLabel tpInsc = new JLabel("Tipo de inscrição:");
	private JLabel nrInsc = new JLabel("Número de Inscrição:");
	private JLabel banco = new JLabel("Banco:");
	private JLabel ag = new JLabel("Agência:");
	private JLabel conta = new JLabel("Conta:");
	private JLabel convenio = new JLabel("Convênio:");
	private JLabel logradouro = new JLabel("Logradouro:");
	private JLabel numero = new JLabel("Número:");
	private JLabel complemento = new JLabel("Complemento:");
	private JLabel cidade = new JLabel("Cidade:");
	private JLabel estado = new JLabel("Estado:");
	private JLabel cep = new JLabel("CEP:");

	private JComboBox<String> convenenteComboBox = new JComboBox<String>();
	private JTextField nomeText = new SizeSpecification(30);
	private short tpInscText = 0;
	private JRadioButton cpfRadio = new JRadioButton("CPF", false);
	private JRadioButton cnpjRadio = new JRadioButton("CNPJ", false);
	private ButtonGroup buttonGroup = new ButtonGroup();
	private JTextField arqEntradaText = new JTextField();
	private JTextField nrInscText = new TypeSpecification(14);
	private JTextField bancoText = new TypeSpecification(3);
	private JTextField agText = new TypeSpecification(5);
	private JTextField agDvText = new SizeSpecification(1);
	private JTextField contaText = new TypeSpecification(12);
	private JTextField contaDvText = new SizeSpecification(1);
	private JTextField convenioText = new SizeSpecification(20);
	private JTextField logradouroText = new SizeSpecification(30);
	private JTextField numeroText = new TypeSpecification(5);
	private JTextField complementoText = new SizeSpecification(15);
	private JTextField cidadeText = new SizeSpecification(20);
	private JTextField estadoText = new SizeSpecification(2);
	private JTextField cepText = new TypeSpecification(5);
	private JTextField cepCompText = new SizeSpecification(3);

	private JButton selecionaArqEntrada = new JButton("...");
	private JButton salvarConvenente = new JButton("Salvar convenente");
	private JButton alterarConvenente = new JButton("Alterar convenente");
	private JButton removerConvenente = new JButton("Remover convenente");
	private JButton validarArquivo = new JButton("Validar arquivo");
	private JButton fechar = new JButton("Fechar");

	public CNAB240GUI() {

		// Parâmetros dos painéis
		this.setContentPane(jPanel);
		jPanel.setLayout(null);
		this.setBounds(150, 150, 985, 435);
		this.setTitle("CNAB240");

		arquivoPanel.setLayout(null);
		arquivoPanel.setBounds(10, 20, 948, 55);
		arquivoPanel.setBorder(BorderFactory.createTitledBorder("Arquivo para validação"));

		clientePanel.setLayout(null);
		clientePanel.setBounds(10, 85, 948, 90);
		clientePanel.setBorder(BorderFactory.createTitledBorder("Dados do cliente"));

		infoBancoPanel.setLayout(null);
		infoBancoPanel.setBounds(10, 185, 948, 55);
		infoBancoPanel.setBorder(BorderFactory.createTitledBorder("Dados bancários"));

		enderecoPanel.setLayout(null);
		enderecoPanel.setBounds(10, 250, 948, 90);
		enderecoPanel.setBorder(BorderFactory.createTitledBorder("Endereço"));

		// Adicionando os JRadioButton ao ButtonGroup
		buttonGroup.add(cpfRadio);
		buttonGroup.add(cnpjRadio);

		// Definindo os tamanhos e posições dos componentes
		convenente.setBounds(20, 25, 100, 20);
		nome.setBounds(516, 25, 100, 20);
		tpInsc.setBounds(20, 60, 100, 20);
		cpfRadio.setBounds(135, 60, 50, 20);
		cnpjRadio.setBounds(195, 60, 70, 20);
		nrInsc.setBounds(516, 60, 140, 20);
		banco.setBounds(20, 25, 100, 20);
		ag.setBounds(183, 25, 70, 20);
		conta.setBounds(343, 25, 70, 20);
		convenio.setBounds(516, 25, 100, 20);
		logradouro.setBounds(20, 25, 100, 20);
		numero.setBounds(391, 25, 50, 20);
		complemento.setBounds(516, 25, 100, 20);
		cidade.setBounds(20, 60, 50, 20);
		estado.setBounds(391, 60, 50, 20);
		cep.setBounds(516, 60, 50, 20);

		convenenteComboBox.setBounds(100, 25, 314, 20);
		configureComboBox(0);

		arqEntradaText.setBounds(10, 23, 900, 20);
		nomeText.setBounds(650, 25, 278, 20);
		nrInscText.setBounds(650, 60, 105, 20);
		bancoText.setBounds(100, 25, 30, 20);
		agText.setBounds(238, 25, 40, 20);
		agDvText.setBounds(279, 25, 15, 20);
		contaText.setBounds(388, 25, 90, 20);
		contaDvText.setBounds(479, 25, 15, 20);
		convenioText.setBounds(650, 25, 190, 20);
		logradouroText.setBounds(100, 25, 278, 20);
		numeroText.setBounds(451, 25, 43, 20);
		complementoText.setBounds(650, 25, 145, 20);
		cidadeText.setBounds(100, 60, 186, 20);
		estadoText.setBounds(451, 60, 22, 20);
		cepText.setBounds(650, 60, 43, 20);
		cepCompText.setBounds(694, 60, 33, 20);

		selecionaArqEntrada.setBounds(912, 23, 20, 19);
		salvarConvenente.setBounds(105, 355, 150, 20);
		alterarConvenente.setBounds(105, 355, 150, 20);
		removerConvenente.setBounds(303, 355, 154, 20);
		validarArquivo.setBounds(505, 355, 150, 20);
		fechar.setBounds(705, 355, 150, 20);

		// Definindo as ações dos botões e iniciando configurações
		RadioButtonHandler handler = new RadioButtonHandler();
		cpfRadio.addItemListener(handler);
		cnpjRadio.addItemListener(handler);
		desabilitaCampos();

		selecionaArqEntrada.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// Definindo variáveis para arquivos de entrada e instanciando classes
				String extension = "txt";

				JFileChooser jFileChooser = new JFileChooser();

				// Criação de filtro com opção para todos os formatos, para permitir '.rem' e
				// assemelhados
				FileNameExtensionFilter filter = new FileNameExtensionFilter("Arquivos texto (*.txt)", extension);
				jFileChooser.setFileFilter(filter);

				jFileChooser.setDialogTitle("Escolha o nome e o diretório do arquivo de entrada");
				jFileChooser.showSaveDialog(CNAB240GUI.this);

				try {
					nomeArqEntrada = jFileChooser.getSelectedFile().getName();
					caminhoEntrada = jFileChooser.getSelectedFile().getPath().replaceAll(nomeArqEntrada, "");
				} catch (NullPointerException e1) {
					return;
				}

				arqEntradaText.setText(caminhoEntrada + nomeArqEntrada);
				habilitaCampos();

			}
		});

		convenenteComboBox.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent itemEvent) {

				int index = convenenteComboBox.getSelectedIndex();
				try {
					List<Convenente> lista = dao.listarConvenentes();

					if (index != 0) {
						salvarConvenente.setVisible(false);
						alterarConvenente.setVisible(true);
						removerConvenente.setEnabled(true);
						validarArquivo.setEnabled(true);
						cpfRadio.setEnabled(false);
						cnpjRadio.setEnabled(false);
						nrInscText.setEnabled(false);
						Convenente convenente = lista.get(index - 1);
						if (convenente.getTpInsc() == 1) {
							buttonGroup.setSelected(cpfRadio.getModel(), true);
							buttonGroup.setSelected(cnpjRadio.getModel(), false);
						} else if (convenente.getTpInsc() == 2) {
							buttonGroup.setSelected(cnpjRadio.getModel(), true);
							buttonGroup.setSelected(cpfRadio.getModel(), false);
						}
						nomeText.setText(convenente.getNome());
						nrInscText.setText(String.valueOf(convenente.getNrInsc()));
						bancoText.setText(String.valueOf(convenente.getBanco()));
						agText.setText(String.valueOf(convenente.getAg()));
						agDvText.setText(convenente.getAgDv());
						contaText.setText(String.valueOf(convenente.getConta()));
						contaDvText.setText(convenente.getContaDv());
						convenioText.setText(convenente.getConvenio());
						logradouroText.setText(convenente.getLogradouro());
						numeroText.setText(String.valueOf(convenente.getNumero()));
						complementoText.setText(convenente.getComplemento());
						cidadeText.setText(convenente.getCidade());
						estadoText.setText(convenente.getEstado());
						cepText.setText(String.valueOf(convenente.getCep()));
						cepCompText.setText(convenente.getCepComp());
					} else
						limparFormulario();

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		salvarConvenente.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {

				dao = new ConvenenteDAO(em);
				
					try {
						if(dao.verificaExistencia(tpInscText, Long.parseLong(nrInscText.getText()))) {
							JOptionPane.showMessageDialog(salvarConvenente, "Convenente já existe na base de dados!", "Aviso",
									JOptionPane.WARNING_MESSAGE);
							return;
						}
						
					} catch (NumberFormatException e1) {
						JOptionPane.showMessageDialog(salvarConvenente, "Número de inscrição inválido!", "Erro",
								JOptionPane.ERROR_MESSAGE);
					} catch (Exception e1) {
						JOptionPane.showMessageDialog(salvarConvenente, "Erro ao incluir convenente", "Erro",
								JOptionPane.ERROR_MESSAGE);
					}
		
				try {
					Convenente convenente = setConvenente();
					StringBuilder validaDadosEntrada = new StringBuilder();
					int cont = 0;
					
					//Impedindo gravação de dados caso Ag, Conta, CPF ou CNPJ estejam inválidos
					if (!convenenteCore.verificaAGCONTA(convenente.getAg() + convenente.getAgDv())) {
						validaDadosEntrada.append("Agência inválida!\n");
						cont++;
					}
					if (!convenenteCore.verificaAGCONTA(convenente.getConta() + convenente.getContaDv())) {
						validaDadosEntrada.append("Conta inválida!\n");
						cont++;
					}
					if (!convenenteCore.verificaCPF(String.valueOf(convenente.getNrInsc())) 
							&& convenente.getTpInsc() == 1) {
						validaDadosEntrada.append("CPF inválido!\n");
						cont++;
					}
					if (!convenenteCore.verificaCNPJ(String.valueOf(convenente.getNrInsc()))
							&& convenente.getTpInsc() == 2) {
						validaDadosEntrada.append("CNPJ inválido!");
						cont++;
					}
					
					if (cont > 0)
						JOptionPane.showMessageDialog(salvarConvenente, validaDadosEntrada,
								"Erro", JOptionPane.ERROR_MESSAGE);

					else {
						if (tpInscText != 0) {
								dao.incluir(convenente);
	
							// Adiciona o convenente incluído no ComboBox
	
							int indice = label.size();
							configureComboBox(indice + 1);
							convenenteComboBox.setSelectedIndex(indice);
	
							JOptionPane.showMessageDialog(salvarConvenente, "Registro incluído com sucesso!",
									"Inclusão de Convenente", JOptionPane.INFORMATION_MESSAGE);
	
						} else {
							JOptionPane.showMessageDialog(salvarConvenente, "Selecione um Tipo de inscrição!", "Erro",
									JOptionPane.ERROR_MESSAGE);
						}
					}
				} catch (Exception e) {
					JOptionPane.showMessageDialog(salvarConvenente, "Erro ao incluir convenente", "Erro",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		});

		alterarConvenente.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {

				dao = new ConvenenteDAO(em);

				try {
					Convenente convenente = setConvenente();
					StringBuilder validaDadosEntrada = new StringBuilder();
					int cont = 0;
					
					//Impedindo gravação de dados caso Ag, Conta, CPF ou CNPJ estejam inválidos
					if (!convenenteCore.verificaAGCONTA(convenente.getAg() + convenente.getAgDv())) {
						validaDadosEntrada.append("Agência inválida!\n");
						cont++;
					}
					if (!convenenteCore.verificaAGCONTA(convenente.getConta() + convenente.getContaDv())) {
						validaDadosEntrada.append("Conta inválida!\n");
						cont++;
					}
					if (!convenenteCore.verificaCPF(String.valueOf(convenente.getNrInsc())) 
							&& convenente.getTpInsc() == 1) {
						validaDadosEntrada.append("CPF inválido!\n");
						cont++;
					}
					if (!convenenteCore.verificaCNPJ(String.valueOf(convenente.getNrInsc()))
							&& convenente.getTpInsc() == 2) {
						validaDadosEntrada.append("CNPJ inválido!");
						cont++;
					}
					
					if (cont > 0)
						JOptionPane.showMessageDialog(alterarConvenente, validaDadosEntrada,
								"Erro", JOptionPane.ERROR_MESSAGE);
					else {
						dao.alterar(convenente);
						// Atualiza o convenente alterado no ComboBox
						int indice = convenenteComboBox.getSelectedIndex();
						configureComboBox(indice);
						convenenteComboBox.setSelectedIndex(indice);

						JOptionPane.showMessageDialog(alterarConvenente, "Registro alterado com sucesso!",
								"Alteração de Convenente", JOptionPane.INFORMATION_MESSAGE);
					}
					
				} catch (Exception e) {
					JOptionPane.showMessageDialog(alterarConvenente, "Erro ao alterar convenente", "Erro",
							JOptionPane.ERROR_MESSAGE);
				}

			}
		});

		removerConvenente.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				dao = new ConvenenteDAO(em);
				try {
					Convenente convenente = setConvenente();
					dao.remover(convenente);

					// Atualiza o convenente alterado no ComboBox
					int indice = convenenteComboBox.getSelectedIndex();
					configureComboBox(indice);
					convenenteComboBox.setSelectedIndex(0);
					limparFormulario();

					JOptionPane.showMessageDialog(removerConvenente, "Registro removido com sucesso!",
							"Remoção de Convenente", JOptionPane.INFORMATION_MESSAGE);

				} catch (Exception e) {
					JOptionPane.showMessageDialog(removerConvenente, "Erro ao remover convenente", "Erro",
							JOptionPane.ERROR_MESSAGE);
				}

			}
		});

		validarArquivo.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				// Definindo variáveis para arquivos de saída e instanciando classes
				String pathEntrada = caminhoEntrada + nomeArqEntrada;
				String nomeArquivoSaida = null;
				String caminhoSaida = null;
				String extension = "txt";
				int opcao = 1;

				JFileChooser jFileChooser = new JFileChooser();

				// Filtro que impede que o arquivo de entrada seja sobrescrito pelo arquivo de saída
				FileFilter filter = new FileFilter() {
					public boolean accept(File file) {
						if (file.isDirectory())
							return true;
						else if (file.getName().endsWith("." + extension) && !file.getName().equals(nomeArqEntrada))
							return true;
						else
							return false;
					}

					public String getDescription() {
						return "Arquivos texto (*.txt)";
					}
				};

				jFileChooser.setAcceptAllFileFilterUsed(false);
				jFileChooser.setFileFilter(filter);
				jFileChooser.setDialogTitle("Escolha o nome e o diretório do arquivo de saída");

				// loop para garantir que um arquivo de saída não será substituído se o usuário não permitir
				while (opcao == 1) {
					try {
						if (jFileChooser.showSaveDialog(CNAB240GUI.this) == JFileChooser.APPROVE_OPTION) {

							nomeArquivoSaida = (jFileChooser.getSelectedFile().getName() + "." + extension)
									.replaceAll(extension + "." + extension, extension);
							caminhoSaida = jFileChooser.getSelectedFile().getPath()
									.replaceAll(nomeArquivoSaida.replaceAll("." + extension, ""), "")
									.replaceAll("." + extension, "");
							// Perguntar se o usuário deseja substituir o arquivo caso o mesmo já exista
							File arqSaida = new File(jFileChooser.getSelectedFile().getPath());
							if (arqSaida.exists())
								opcao = JOptionPane.showConfirmDialog(validarArquivo,
										"O arquivo selecionado já existe. Deseja substituí-lo?", "Atenção",
										JOptionPane.YES_NO_OPTION);
							else {
								opcao = 0;
							}
						} else {
							opcao = 0;
							return;
						}
					} catch (NullPointerException e1) {
						return;
					}
				}

				long contErros = 0;

				ArquivoCore arquivo = new ArquivoCore(pathEntrada, caminhoSaida, nomeArquivoSaida, contErros);
				ArrayList<String> L = arquivo.leArquivoEntrada();
				ConvenenteCore convenenteCore = new ConvenenteCore(L);

				// Incluindo os dados de entrada nos atributos da classe Convenente
				Convenente convenente = setConvenente();
				ConteudoCore conteudo = new ConteudoCore(L, contErros, convenente, convenenteCore);

				// Criando arquivo de saída
				PrintWriter gravarArq = arquivo.criaArquivoSaida();

				try {
					// Início da verificação de estrutura
					gravarArq.println(StringUtils.repeat("*", 46) + " Verificação de estrutura "
							+ StringUtils.repeat("*", 46) + "\n");

					// Verificação de estrutura
					ArrayList<String> verificacaoEstrutural = arquivo.verificaEstrutura();
					for (int i = 0; i < verificacaoEstrutural.size(); i++)
						gravarArq.println(verificacaoEstrutural.get(i));

					// pulando uma linha para início de verificação de conteúdo
					gravarArq.println("\n" + StringUtils.repeat("*", 48) + " Verificação de conteúdo "
							+ StringUtils.repeat("*", 47));

				} catch (IOException e1) {
					JOptionPane.showMessageDialog(validarArquivo, "Erro ao verificar estrutura", "Erro",
							JOptionPane.ERROR_MESSAGE);
					return;
				} catch (Exception e2) {
					JOptionPane.showMessageDialog(validarArquivo, "Erro ao verificar estrutura: " + e2, "Erro",
							JOptionPane.ERROR_MESSAGE);
					return;
				}

				try {
					// Verificação de conteúdo
					ArrayList<String> verificacaoConteudo = conteudo.cnab240();
					for (int i = 0; i < verificacaoConteudo.size(); i++)
						if (verificacaoConteudo.get(i) != null)
							gravarArq.println(verificacaoConteudo.get(i));
				} catch (Exception e3) {
					JOptionPane.showMessageDialog(validarArquivo, "Erro ao verificar conteúdo: " + e, "Erro",
							JOptionPane.ERROR_MESSAGE);
					e3.printStackTrace();
					return;
				}

				// Gravando resultados no arquivo de saída
				gravarArq.println("\n" + StringUtils.repeat("*", 119));
				gravarArq.println("\n---- Totalizadores ----");
				gravarArq.println("\nErros de estrutura: " + arquivo.getContErros() + "\nErros de conteúdo : "
						+ conteudo.getContErros());
				gravarArq.close();
				JOptionPane.showMessageDialog(validarArquivo, "Arquivo verificado com sucesso!",
						"Inclusão de Convenente", JOptionPane.INFORMATION_MESSAGE);
			}
		});

		fechar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				System.exit(0);
			}
		});

		// Adicionando os componentes aos painéis
		jPanel.add(arquivoPanel);
		jPanel.add(clientePanel);
		jPanel.add(infoBancoPanel);
		jPanel.add(enderecoPanel);
		jPanel.add(salvarConvenente);
		jPanel.add(alterarConvenente);
		jPanel.add(removerConvenente);
		jPanel.add(validarArquivo);
		jPanel.add(fechar);

		arquivoPanel.add(arqEntradaText);
		arquivoPanel.add(selecionaArqEntrada);

		clientePanel.add(convenente);
		clientePanel.add(nome);
		clientePanel.add(tpInsc);
		clientePanel.add(cpfRadio);
		clientePanel.add(cnpjRadio);
		clientePanel.add(nrInsc);

		infoBancoPanel.add(banco);
		infoBancoPanel.add(ag);
		infoBancoPanel.add(conta);
		infoBancoPanel.add(convenio);

		enderecoPanel.add(logradouro);
		enderecoPanel.add(numero);
		enderecoPanel.add(complemento);
		enderecoPanel.add(cidade);
		enderecoPanel.add(estado);
		enderecoPanel.add(cep);
		enderecoPanel.add(cepCompText);

		clientePanel.add(convenenteComboBox);
		clientePanel.add(nomeText);
		clientePanel.add(cpfRadio);
		clientePanel.add(cnpjRadio);
		clientePanel.add(nrInscText);

		infoBancoPanel.add(bancoText);
		infoBancoPanel.add(agText);
		infoBancoPanel.add(agDvText);
		infoBancoPanel.add(contaText);
		infoBancoPanel.add(contaDvText);
		infoBancoPanel.add(convenioText);

		enderecoPanel.add(logradouroText);
		enderecoPanel.add(numeroText);
		enderecoPanel.add(complementoText);
		enderecoPanel.add(cidadeText);
		enderecoPanel.add(estadoText);
		enderecoPanel.add(cepText);

	}

	// Métodos auxiliares

	private Convenente setConvenente() {
		Convenente convenente = new Convenente();
		convenente.setTpInsc(tpInscText);
		convenente.setNrInsc(Long.parseLong(nrInscText.getText()));
		convenente.setNome(nomeText.getText());
		convenente.setBanco(Short.parseShort(bancoText.getText()));
		convenente.setAg(Integer.parseInt(agText.getText()));
		convenente.setAgDv(agDvText.getText());
		convenente.setConta(Long.parseLong(contaText.getText()));
		convenente.setContaDv(contaDvText.getText());
		convenente.setConvenio(convenioText.getText());
		convenente.setLogradouro(logradouroText.getText());
		convenente.setNumero(Integer.parseInt(numeroText.getText()));
		convenente.setComplemento(complementoText.getText());
		convenente.setCidade(cidadeText.getText());
		convenente.setEstado(estadoText.getText());
		convenente.setCep(Integer.parseInt(cepText.getText()));
		convenente.setCepComp(cepCompText.getText());
		return convenente;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void configureComboBox(int index) {
		try {
			label = dao.listarLabel();
		} catch (Exception e) {
			e.printStackTrace();
		}
		label.add(0, "Selecione...");
		Object[] items = label.toArray();
		DefaultComboBoxModel model = new DefaultComboBoxModel(items);
		convenenteComboBox.setModel(model);
	}

	/**
	 * Método que limpa os campos de entrada do formulário e retorna os botões ao
	 * estado inicial
	 */
	private void limparFormulario() {
		buttonGroup.clearSelection();
		nrInscText.setText("");
		nomeText.setText("");
		bancoText.setText("");
		agText.setText("");
		agDvText.setText("");
		contaText.setText("");
		contaDvText.setText("");
		convenioText.setText("");
		logradouroText.setText("");
		numeroText.setText("");
		complementoText.setText("");
		cidadeText.setText("");
		estadoText.setText("");
		cepText.setText("");
		cepCompText.setText("");

		salvarConvenente.setVisible(true);
		alterarConvenente.setVisible(false);
		removerConvenente.setEnabled(false);
		validarArquivo.setEnabled(false);
		cpfRadio.setEnabled(true);
		cnpjRadio.setEnabled(true);
		nrInscText.setEnabled(true);
	}

	private void desabilitaCampos() {

		convenenteComboBox.setEnabled(false);

		arqEntradaText.setEnabled(false);
		nomeText.setEnabled(false);
		nrInscText.setEnabled(false);
		bancoText.setEnabled(false);
		agText.setEnabled(false);
		agDvText.setEnabled(false);
		contaText.setEnabled(false);
		contaDvText.setEnabled(false);
		convenioText.setEnabled(false);
		logradouroText.setEnabled(false);
		numeroText.setEnabled(false);
		complementoText.setEnabled(false);
		cidadeText.setEnabled(false);
		estadoText.setEnabled(false);
		cepText.setEnabled(false);
		cepCompText.setEnabled(false);

		salvarConvenente.setEnabled(false);
		removerConvenente.setEnabled(false);
		validarArquivo.setEnabled(false);
	}

	public void habilitaCampos() {

		convenenteComboBox.setEnabled(true);

		arqEntradaText.setEnabled(true);
		nomeText.setEnabled(true);
		nrInscText.setEnabled(true);
		bancoText.setEnabled(true);
		agText.setEnabled(true);
		agDvText.setEnabled(true);
		contaText.setEnabled(true);
		contaDvText.setEnabled(true);
		convenioText.setEnabled(true);
		logradouroText.setEnabled(true);
		numeroText.setEnabled(true);
		complementoText.setEnabled(true);
		cidadeText.setEnabled(true);
		estadoText.setEnabled(true);
		cepText.setEnabled(true);
		cepCompText.setEnabled(true);

		salvarConvenente.setEnabled(true);
	}

	private class RadioButtonHandler implements ItemListener {
		@Override
		public void itemStateChanged(ItemEvent itemEvent) {
			if (cpfRadio.isSelected())
				tpInscText = 1;
			if (cnpjRadio.isSelected())
				tpInscText = 2;
		}
	}
}
