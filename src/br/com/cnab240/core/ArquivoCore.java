/* Desenvolvido por Daniel Floriano 
 * Classe que contém o método de teste de estrutura e lê o arquivo de entrada
 */

package br.com.cnab240.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;

/**
 * @author Daniel Floriano
 * Classe para verificação estrutural de arquivos CNAB240
 */
public class ArquivoCore {

	// Declaração de variaveis

	private ArrayList<String> arqEntrada = new ArrayList<String>();
	private String caminhoEntrada;
	private String caminhoSaida;
	private String nomeArquivoSaida;
	private long contErros;

	public ArquivoCore(String caminhoEntrada, String caminhoSaida, String nomeArquivoSaida, long contErros) {
		this.caminhoEntrada = caminhoEntrada;
		this.caminhoSaida = caminhoSaida;
		this.nomeArquivoSaida = nomeArquivoSaida;
		this.contErros = contErros;
	}

	/**
	 * @param caminhoEntrada
	 * @return
	 */
	public ArrayList<String> leArquivoEntrada() {

		String linha = "";

		try {
			FileReader arq = new FileReader(caminhoEntrada);
			BufferedReader ler = new BufferedReader(arq);

			while (linha != null) {
				linha = ler.readLine();
				if (linha != null)
					arqEntrada.add(linha);
				else
					break;
			}
			arq.close();
		} catch (IOException e) {
			System.err.printf("Erro na abertura do arquivo: %s.\n", e.getMessage());
		}
		return arqEntrada;
	}

	/**
	 * @param diretorioSaida
	 * @param nomeArquivoSaida
	 * @return
	 * 
	 * Método que cria um arquivo de saída e não imprime a uma linha quando seu valor é null
	 */
	public PrintWriter criaArquivoSaida() {

		FileWriter resultado = null;

		if (caminhoSaida.endsWith("/") || caminhoSaida.endsWith("\\"))
			try {
				resultado = new FileWriter(caminhoSaida + nomeArquivoSaida);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		else
			try {
				resultado = new FileWriter(caminhoSaida + "\\" + nomeArquivoSaida);
			} catch (IOException e) {
				e.printStackTrace();
			}

		PrintWriter gravarArq = new PrintWriter(resultado);

		return gravarArq;
	}

	/**
	 * @param caminhoEntrada
	 * @param caminhoSaida
	 * @param nomeArquivoSaida
	 * @return
	 * @throws IOException
	 */
	public ArrayList<String> verificaEstrutura() throws IOException {

		int contHL = 0, contTL = 0, contA = 0, contB = 0, contC = 0, contJ = 0, contON = 0, HLA = 0, HLJ = 0,
				HLON = 0;
		ArrayList<String> verificacaoPorLinha = new ArrayList<String>();
		ArrayList<String> verificacaoPorSegmento = new ArrayList<String>();
		ArrayList<String> resultadoVerificacaoEstrutural = new ArrayList<String>();
		leArquivoEntrada();

		// Verificar se todas as linhas tem 240 posições
		for (int i = 0; i < arqEntrada.size(); i++)
			if (arqEntrada.get(i).length() != 240) {
				verificacaoPorLinha
						.add("Linha " + String.format("%06d", i + 1) + ": Formato inválido - A linha não contém 240 posições, contém "
								+ (arqEntrada.get(i).length()) + ".");
				contErros += 1;
			}

		// Verificar se existe Header de Arquivo e Trailer de Arquivo
		if (!arqEntrada.get(0).substring(7, 8).equals("0")) {
			verificacaoPorLinha.add("Linha 01: Não existe Header de Arquivo na primeira linha. ");
			contErros += 1;
		}

		if (!arqEntrada.get(arqEntrada.size() - 1).substring(7, 8).equals("9")) {
			verificacaoPorLinha
					.add("Linha " + String.format("%06d",arqEntrada.size()) + ": Não existe Trailer de Arquivo na última linha. ");
			contErros += 1;
		}

		// Verificação Header de Lote e Trailer de Lote
		for (int i = 1; i < arqEntrada.size() - 1; i++) {
			if (arqEntrada.get(i).substring(7, 8).equals("1")) {
				contHL += 1;
				if (Arrays.asList("01", "02", "03", "04", "05", "10", "20", "41", "43")
						.contains(arqEntrada.get(i).substring(11, 13))) {
					HLA = i;
					if (!arqEntrada.get(i + 1).substring(7, 8).equals("3")
							|| !arqEntrada.get(i + 1).substring(13, 14).equals("A")) {
						verificacaoPorLinha.add("Linha " + String.format("%06d",i + 1)
								+ ": Header de Lote A/B/C não seguido de Registro Detalhe Segmento A.");
						contErros += 1;
					}
				}
				if (Arrays.asList("30", "31").contains(arqEntrada.get(i).substring(11, 13))) {
					HLJ = i;
					if (!arqEntrada.get(i + 1).substring(7, 8).equals("3")
							|| !arqEntrada.get(i + 1).substring(13, 14).equals("J")) {
						verificacaoPorLinha.add(
								"Linha " + String.format("%06d",i + 1) + ": Header de Lote J não seguido de Registro Detalhe Segmento J.");
						contErros += 1;
					}
				}
				if (Arrays.asList("11", "16", "17", "18", "21", "22", "23", "24")
						.contains(arqEntrada.get(i).substring(11, 13))) {
					HLON = i;
					if (!arqEntrada.get(i + 1).substring(7, 8).equals("3")
							|| !Arrays.asList("O", "N").contains(arqEntrada.get(i - 1).substring(13, 14))) {
						verificacaoPorLinha.add("Linha " + String.format("%06d",i + 1)
								+ ": Header de Lote O/N não seguido de Registro Detalhe Segmento O ou Segmento N.");
						contErros += 1;
					}
				}

				if (!arqEntrada.get(i - 1).substring(7, 8).equals("5")
						&& !arqEntrada.get(i - 1).substring(7, 8).equals("0")) {
					verificacaoPorLinha.add("Linha " + String.format("%06d",i + 1)
							+ ": Header de Lote não precedido de Header de Arquivo ou Trailer de Lote.");
					contErros += 1;
				}
			}
			if (arqEntrada.get(i).substring(7, 8).equals("5")) {
				contTL += 1;
				if (!arqEntrada.get(i + 1).substring(7, 8).equals("9")
						&& !arqEntrada.get(i + 1).substring(7, 8).equals("1")) {
					verificacaoPorLinha.add("Linha " + String.format("%06d",i + 1)
							+ ": Trailer de Lote não seguido de Trailer de Arquivo ou Header de Lote.");
					contErros += 1;
				}
				if (!arqEntrada.get(i - 1).substring(7, 8).equals("3")) {
					verificacaoPorLinha
							.add("Linha " + String.format("%06d",i + 1) + ": Trailer de Lote não precedido de Registro Detalhe.");
					contErros += 1;
				}
			}
		}
		if (contHL == 0) {
			verificacaoPorSegmento.add("Não existe Header de Lote no Arquivo.");
			contErros += 1;
		}
		if (contTL == 0) {
			verificacaoPorSegmento.add("Não existe Trailer de Lote no Arquivo.");
			contErros += 1;
		}

		// Verificação de Segmento A
		for (int i = 2; i <= arqEntrada.size() - 3; i++) {
			if (arqEntrada.get(i).substring(7, 8).equals("3") && arqEntrada.get(i).substring(13, 14).equals("A")) {
				contA += 1;
				if (!(arqEntrada.get(i + 1).substring(7, 8).equals("3")
						&& arqEntrada.get(i + 1).substring(13, 14).equals("B"))) {
					verificacaoPorLinha.add("Linha " + String.format("%06d",i + 1)
							+ ": Registro Detalhe Segmento A não seguido de Registro Detalhe Segmento B.");
					contErros += 1;
				}
				if (!(arqEntrada.get(i - 1).substring(7, 8).equals("3")
						&& Arrays.asList("B", "C").contains(arqEntrada.get(i - 1).substring(13, 14)))
						&& !(arqEntrada.get(i - 1).substring(7, 8).equals("1")
								&& Arrays.asList("01", "02", "03", "04", "05", "10", "20", "41", "43")
										.contains(arqEntrada.get(i - 1).substring(11, 13)))) {
					verificacaoPorLinha.add("Linha " + String.format("%06d", i + 1)
							+ ": Registro Detalhe Segmento A não precedido de Registro Detalhe Segmento B, Segmento C ou Header de Lote A/B/C.");
					contErros += 1;
				}
			}

			// Verificação de Segmento B
			if (arqEntrada.get(i).substring(7, 8).equals("3") && arqEntrada.get(i).substring(13, 14).equals("B")) {
				contB += 1;
				if (!(arqEntrada.get(i + 1).substring(7, 8).equals("3")
						&& Arrays.asList("A", "C").contains(arqEntrada.get(i + 1).substring(13, 14)))
						&& !arqEntrada.get(i + 1).substring(7, 8).equals("5")) {
					verificacaoPorLinha.add("Linha " + String.format("%06d", i + 1)
							+ ": Registro Detalhe Segmento B não seguido de Registro Detalhe Segmento A, Segmento C ou Trailer de Lote.");
					contErros += 1;
				}
				if (!(arqEntrada.get(i - 1).substring(7, 8).equals("3")
						&& arqEntrada.get(i - 1).substring(13, 14).equals("A"))) {
					verificacaoPorLinha.add("Linha " + String.format("%06d", i + 1)
							+ ": Registro Detalhe Segmento B não precedido de Registro Detalhe Segmento A.");
					contErros += 1;
				}
			}

			// Verificação de Segmento C
			if (arqEntrada.get(i).substring(7, 8).equals("3") && arqEntrada.get(i).substring(13, 14).equals("C")) {
				contC += 1;
				if (!(arqEntrada.get(i + 1).substring(7, 8).equals("3")
						&& arqEntrada.get(i + 1).substring(13, 14).equals("A"))
						&& !arqEntrada.get(i + 1).substring(7, 8).equals("5")) {
					verificacaoPorLinha.add("Linha " + String.format("%06d", i + 1)
							+ ": Registro Detalhe Segmento C não seguido de Registro Detalhe Segmento A ou Trailer de Lote.");
					contErros += 1;
				}
				if (!(arqEntrada.get(i - 1).substring(7, 8).equals("3")
						&& arqEntrada.get(i - 1).substring(13, 14).equals("B"))) {
					verificacaoPorLinha.add("Linha " + String.format("%06d", i + 1)
							+ ": Registro Detalhe Segmento C não precedido de Registro Detalhe Segmento B.");
					contErros += 1;
				}
			}

			// Verificação de Segmento J
			if (arqEntrada.get(i).substring(7, 8).equals("3") && arqEntrada.get(i).substring(13, 14).equals("J")) {
				contJ += 1;
				if (!(arqEntrada.get(i + 1).substring(7, 8).equals("3")
						&& arqEntrada.get(i + 1).substring(13, 14).equals("J"))
						&& !arqEntrada.get(i + 1).substring(7, 8).equals("5")) {
					verificacaoPorLinha.add("Linha " + String.format("%06d", i + 1)
							+ ": Registro Detalhe Segmento J não seguido de Registro Detalhe Segmento J ou Trailer de Lote.");
					contErros += 1;
				}
				if (!(arqEntrada.get(i - 1).substring(7, 8).equals("3")
						&& arqEntrada.get(i - 1).substring(13, 14).equals("J"))
						&& (!(arqEntrada.get(i - 1).substring(7, 8).equals("1")
								&& Arrays.asList("30", "31").contains(arqEntrada.get(i - 1).substring(11, 13))))) {
					verificacaoPorLinha.add("Linha " + String.format("%06d", i + 1)
							+ ": Registro Detalhe Segmento J não precedido de Registro Detalhe Segmento J ou Header de Lote J.");
					contErros += 1;
				}
			}

			// Verificação de Segmento J52
			if (arqEntrada.get(i).substring(7, 8).equals("3") && arqEntrada.get(i).substring(13, 14).equals("J")) {
				if (Integer.parseInt(arqEntrada.get(i).substring(152, 167)) > 25000000)
					if (!arqEntrada.get(i + 1).substring(17, 19).equals("52")) {
						verificacaoPorLinha.add("Linha " + String.format("%06d", i + 1)
								+ ": Para títulos de valor superior a R$ 250.000,00 é obrigatória a utilização de Segmento J52 na linha seguinte.");
						contErros += 1;
					}
				if (arqEntrada.get(i + 1).substring(17, 19).equals("52")) {
					if (!(arqEntrada.get(i + 1).substring(7, 8).equals("3")
							&& arqEntrada.get(i + 1).substring(13, 14).equals("J"))
							&& !arqEntrada.get(i + 1).substring(7, 8).equals("5")) {
						verificacaoPorLinha.add("Linha " + String.format("%06d", i + 1)
								+ ": Registro Detalhe Segmento J52 não seguido de Registro Detalhe Segmento J ou Trailer de Lote.");
						contErros += 1;
					}
					if (!(arqEntrada.get(i - 1).substring(7, 8).equals("3")
							&& arqEntrada.get(i - 1).substring(13, 14).equals("J"))) {
						verificacaoPorLinha.add("Linha " + String.format("%06d", i + 1)
								+ ": Registro Detalhe Segmento J52 não precedido de Registro Detalhe Segmento J.");
						contErros += 1;
					}
				}
			}

			// Verificação de Segmento O
			if (arqEntrada.get(i).substring(7, 8).equals("3") && arqEntrada.get(i).substring(13, 14).equals("O")) {
				contON += 1;
				if (!(arqEntrada.get(i + 1).substring(7, 8).equals("3")
						&& Arrays.asList("O", "N").contains(arqEntrada.get(i + 1).substring(13, 14)))
						&& !arqEntrada.get(i + 1).substring(7, 8).equals("5")) {
					verificacaoPorLinha.add("Linha " + String.format("%06d", i + 1)
							+ ": Registro Detalhe Segmento O não seguido de Registro Detalhe Segmento O, Segmento N ou Trailer de Lote.");
					contErros += 1;
				}
				if (!(arqEntrada.get(i - 1).substring(7, 8).equals("3")
						&& Arrays.asList("O", "N").contains(arqEntrada.get(i - 1).substring(13, 14)))
						&& !(arqEntrada.get(i - 1).substring(7, 8).equals("1")
								&& Arrays.asList("16", "17", "18", "19", "21", "22", "23", "24", "25", "26", "27")
										.contains(arqEntrada.get(i - 1).substring(11, 13)))) {
					verificacaoPorLinha.add("Linha " + String.format("%06d", i + 1)
							+ ": Registro Detalhe Segmento O não precedido de Registro Detalhe Segmento O, Segmento N ou Header de Lote O/N.");
					contErros += 1;
				}
			}

			// Verificação de Segmento N
			if (arqEntrada.get(i).substring(7, 8).equals("3") && arqEntrada.get(i).substring(13, 14).equals("N")) {
				contON += 1;
				if (!(arqEntrada.get(i + 1).substring(7, 8).equals("3")
						&& Arrays.asList("O", "N").contains(arqEntrada.get(i + 1).substring(13, 14)))
						&& !arqEntrada.get(i + 1).substring(7, 8).equals("5")) {
					verificacaoPorLinha.add("Linha " + String.format("%06d", i + 1)
							+ ": Registro Detalhe Segmento N não seguido de Registro Detalhe Segmento O, Segmento N ou Trailer de Lote.");
					contErros += 1;
				}
				if (!(arqEntrada.get(i - 1).substring(7, 8).equals("3")
						&& Arrays.asList("O", "N").contains(arqEntrada.get(i - 1).substring(13, 14)))
						&& !(arqEntrada.get(i - 1).substring(7, 8).equals("1")
								&& Arrays.asList("16", "17", "18", "19", "21", "22", "23", "24", "25", "26", "27")
										.contains(arqEntrada.get(i - 1).substring(11, 13)))) {
					verificacaoPorLinha.add("Linha " + String.format("%06d", i + 1)
							+ ": Registro Detalhe Segmento N não precedido de Registro Detalhe Segmento O, Segmento N ou Header de Lote O/N.");
					contErros += 1;
				}
				if (!Arrays.asList("16", "17", "18", "19", "21", "22", "23", "24", "25", "26", "27")
						.contains(arqEntrada.get(i).substring(132, 134))) {
					verificacaoPorLinha.add("Linha " + String.format("%06d", i + 1)
							+ ": Registro Detalhe Segmento N não possui complemento N1, N2, N3, N4, N5, N6, N7 ou N8.");
					contErros += 1;
				}
			}
		}

		// Verificação de existência de Segmento Detalhe e Header de Lote
		if (HLA > 0 && contA == 0) {
			verificacaoPorSegmento
					.add("O arquivo contém Registro Header do tipo A/B/C mas não contém Registro Detalhe Segmento A.");
			contErros += 1;
		}
		if (HLA == 0 && contA > 0) {
			verificacaoPorSegmento
					.add("O arquivo contém Registro Detalhe Segmento A mas não contém Registro Header do tipo A/B/C.");
			contErros += 1;
		}
		if (HLA > 0 && contB == 0) {
			verificacaoPorSegmento
					.add("O arquivo contém Registro Header do tipo A/B/C mas não contém Registro Detalhe Segmento B.");
			contErros += 1;
		}
		if (HLA == 0 && contB > 0) {
			verificacaoPorSegmento
					.add("O arquivo contém Registro Detalhe Segmento B mas não contém Registro Header do tipo A/B/C.");
			contErros += 1;
		}
		if (HLA == 0 && contC > 0) {
			verificacaoPorSegmento
					.add("O arquivo contém Registro Detalhe Segmento C mas não contém Registro Header do tipo A/B/C.");
			contErros += 1;
		}
		if (HLJ > 0 && contJ == 0) {
			verificacaoPorSegmento
					.add("O arquivo contém Registro Header do tipo J mas não contém Registro Detalhe Segmento J.");
			contErros += 1;
		}
		if (HLJ == 0 && contJ > 0) {
			verificacaoPorSegmento
					.add("O arquivo contém Registro Detalhe Segmento J mas não contém Registro Header do tipo J.");
			contErros += 1;
		}
		if (HLON > 0 && contON == 0) {
			verificacaoPorSegmento.add(
					"O arquivo contém Registro Header do tipo O/N mas não contém Registro Detalhe Segmento O ou Segmento N.");
			contErros += 1;
		}
		if (HLJ == 0 && contON > 0) {
			verificacaoPorSegmento.add(
					"O arquivo contém Registro Detalhe Segmento O ou Segmento N mas não contém Registro Header do tipo O/N.");
			contErros += 1;
		}

		// Gravação no arquivo de saída dos erros encontrados
		for (int i = 0; i < verificacaoPorSegmento.size(); i++)
			if (!verificacaoPorSegmento.get(i).equals(null))
				resultadoVerificacaoEstrutural.add(verificacaoPorSegmento.get(i));

		for (int i = 0; i < verificacaoPorLinha.size(); i++)
			if (!verificacaoPorLinha.get(i).equals(null))
				resultadoVerificacaoEstrutural.add(verificacaoPorLinha.get(i));

		// Verificação final de erros
		if (contErros == 0)
			resultadoVerificacaoEstrutural.add("O arquivo não contém erros de estrutura.");

		// Retorno do método
		return resultadoVerificacaoEstrutural;
	}

	public long getContErros() {
		return contErros;
	}

	public String getCaminhoEntrada() {
		return caminhoEntrada;
	}

	public String getCaminhoSaida() {
		return caminhoSaida;
	}

	public String getNomeArquivoSaida() {
		return nomeArquivoSaida;
	}
}