/* Desenvolvido por Daniel Floriano 
 * Classe que cont�m o m�todo de teste de estrutura e l� o arquivo de entrada
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
 * Classe para verifica��o estrutural de arquivos CNAB240
 */
public class ArquivoCore {

	// Declara��o de variaveis

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
	 * M�todo que cria um arquivo de sa�da e n�o imprime a uma linha quando seu valor � null
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

		// Verificar se todas as linhas tem 240 posi��es
		for (int i = 0; i < arqEntrada.size(); i++)
			if (arqEntrada.get(i).length() != 240) {
				verificacaoPorLinha
						.add("Linha " + String.format("%06d", i + 1) + ": Formato inv�lido - A linha n�o cont�m 240 posi��es, cont�m "
								+ (arqEntrada.get(i).length()) + ".");
				contErros += 1;
			}

		// Verificar se existe Header de Arquivo e Trailer de Arquivo
		if (!arqEntrada.get(0).substring(7, 8).equals("0")) {
			verificacaoPorLinha.add("Linha 01: N�o existe Header de Arquivo na primeira linha. ");
			contErros += 1;
		}

		if (!arqEntrada.get(arqEntrada.size() - 1).substring(7, 8).equals("9")) {
			verificacaoPorLinha
					.add("Linha " + String.format("%06d",arqEntrada.size()) + ": N�o existe Trailer de Arquivo na �ltima linha. ");
			contErros += 1;
		}

		// Verifica��o Header de Lote e Trailer de Lote
		for (int i = 1; i < arqEntrada.size() - 1; i++) {
			if (arqEntrada.get(i).substring(7, 8).equals("1")) {
				contHL += 1;
				if (Arrays.asList("01", "02", "03", "04", "05", "10", "20", "41", "43")
						.contains(arqEntrada.get(i).substring(11, 13))) {
					HLA = i;
					if (!arqEntrada.get(i + 1).substring(7, 8).equals("3")
							|| !arqEntrada.get(i + 1).substring(13, 14).equals("A")) {
						verificacaoPorLinha.add("Linha " + String.format("%06d",i + 1)
								+ ": Header de Lote A/B/C n�o seguido de Registro Detalhe Segmento A.");
						contErros += 1;
					}
				}
				if (Arrays.asList("30", "31").contains(arqEntrada.get(i).substring(11, 13))) {
					HLJ = i;
					if (!arqEntrada.get(i + 1).substring(7, 8).equals("3")
							|| !arqEntrada.get(i + 1).substring(13, 14).equals("J")) {
						verificacaoPorLinha.add(
								"Linha " + String.format("%06d",i + 1) + ": Header de Lote J n�o seguido de Registro Detalhe Segmento J.");
						contErros += 1;
					}
				}
				if (Arrays.asList("11", "16", "17", "18", "21", "22", "23", "24")
						.contains(arqEntrada.get(i).substring(11, 13))) {
					HLON = i;
					if (!arqEntrada.get(i + 1).substring(7, 8).equals("3")
							|| !Arrays.asList("O", "N").contains(arqEntrada.get(i - 1).substring(13, 14))) {
						verificacaoPorLinha.add("Linha " + String.format("%06d",i + 1)
								+ ": Header de Lote O/N n�o seguido de Registro Detalhe Segmento O ou Segmento N.");
						contErros += 1;
					}
				}

				if (!arqEntrada.get(i - 1).substring(7, 8).equals("5")
						&& !arqEntrada.get(i - 1).substring(7, 8).equals("0")) {
					verificacaoPorLinha.add("Linha " + String.format("%06d",i + 1)
							+ ": Header de Lote n�o precedido de Header de Arquivo ou Trailer de Lote.");
					contErros += 1;
				}
			}
			if (arqEntrada.get(i).substring(7, 8).equals("5")) {
				contTL += 1;
				if (!arqEntrada.get(i + 1).substring(7, 8).equals("9")
						&& !arqEntrada.get(i + 1).substring(7, 8).equals("1")) {
					verificacaoPorLinha.add("Linha " + String.format("%06d",i + 1)
							+ ": Trailer de Lote n�o seguido de Trailer de Arquivo ou Header de Lote.");
					contErros += 1;
				}
				if (!arqEntrada.get(i - 1).substring(7, 8).equals("3")) {
					verificacaoPorLinha
							.add("Linha " + String.format("%06d",i + 1) + ": Trailer de Lote n�o precedido de Registro Detalhe.");
					contErros += 1;
				}
			}
		}
		if (contHL == 0) {
			verificacaoPorSegmento.add("N�o existe Header de Lote no Arquivo.");
			contErros += 1;
		}
		if (contTL == 0) {
			verificacaoPorSegmento.add("N�o existe Trailer de Lote no Arquivo.");
			contErros += 1;
		}

		// Verifica��o de Segmento A
		for (int i = 2; i <= arqEntrada.size() - 3; i++) {
			if (arqEntrada.get(i).substring(7, 8).equals("3") && arqEntrada.get(i).substring(13, 14).equals("A")) {
				contA += 1;
				if (!(arqEntrada.get(i + 1).substring(7, 8).equals("3")
						&& arqEntrada.get(i + 1).substring(13, 14).equals("B"))) {
					verificacaoPorLinha.add("Linha " + String.format("%06d",i + 1)
							+ ": Registro Detalhe Segmento A n�o seguido de Registro Detalhe Segmento B.");
					contErros += 1;
				}
				if (!(arqEntrada.get(i - 1).substring(7, 8).equals("3")
						&& Arrays.asList("B", "C").contains(arqEntrada.get(i - 1).substring(13, 14)))
						&& !(arqEntrada.get(i - 1).substring(7, 8).equals("1")
								&& Arrays.asList("01", "02", "03", "04", "05", "10", "20", "41", "43")
										.contains(arqEntrada.get(i - 1).substring(11, 13)))) {
					verificacaoPorLinha.add("Linha " + String.format("%06d", i + 1)
							+ ": Registro Detalhe Segmento A n�o precedido de Registro Detalhe Segmento B, Segmento C ou Header de Lote A/B/C.");
					contErros += 1;
				}
			}

			// Verifica��o de Segmento B
			if (arqEntrada.get(i).substring(7, 8).equals("3") && arqEntrada.get(i).substring(13, 14).equals("B")) {
				contB += 1;
				if (!(arqEntrada.get(i + 1).substring(7, 8).equals("3")
						&& Arrays.asList("A", "C").contains(arqEntrada.get(i + 1).substring(13, 14)))
						&& !arqEntrada.get(i + 1).substring(7, 8).equals("5")) {
					verificacaoPorLinha.add("Linha " + String.format("%06d", i + 1)
							+ ": Registro Detalhe Segmento B n�o seguido de Registro Detalhe Segmento A, Segmento C ou Trailer de Lote.");
					contErros += 1;
				}
				if (!(arqEntrada.get(i - 1).substring(7, 8).equals("3")
						&& arqEntrada.get(i - 1).substring(13, 14).equals("A"))) {
					verificacaoPorLinha.add("Linha " + String.format("%06d", i + 1)
							+ ": Registro Detalhe Segmento B n�o precedido de Registro Detalhe Segmento A.");
					contErros += 1;
				}
			}

			// Verifica��o de Segmento C
			if (arqEntrada.get(i).substring(7, 8).equals("3") && arqEntrada.get(i).substring(13, 14).equals("C")) {
				contC += 1;
				if (!(arqEntrada.get(i + 1).substring(7, 8).equals("3")
						&& arqEntrada.get(i + 1).substring(13, 14).equals("A"))
						&& !arqEntrada.get(i + 1).substring(7, 8).equals("5")) {
					verificacaoPorLinha.add("Linha " + String.format("%06d", i + 1)
							+ ": Registro Detalhe Segmento C n�o seguido de Registro Detalhe Segmento A ou Trailer de Lote.");
					contErros += 1;
				}
				if (!(arqEntrada.get(i - 1).substring(7, 8).equals("3")
						&& arqEntrada.get(i - 1).substring(13, 14).equals("B"))) {
					verificacaoPorLinha.add("Linha " + String.format("%06d", i + 1)
							+ ": Registro Detalhe Segmento C n�o precedido de Registro Detalhe Segmento B.");
					contErros += 1;
				}
			}

			// Verifica��o de Segmento J
			if (arqEntrada.get(i).substring(7, 8).equals("3") && arqEntrada.get(i).substring(13, 14).equals("J")) {
				contJ += 1;
				if (!(arqEntrada.get(i + 1).substring(7, 8).equals("3")
						&& arqEntrada.get(i + 1).substring(13, 14).equals("J"))
						&& !arqEntrada.get(i + 1).substring(7, 8).equals("5")) {
					verificacaoPorLinha.add("Linha " + String.format("%06d", i + 1)
							+ ": Registro Detalhe Segmento J n�o seguido de Registro Detalhe Segmento J ou Trailer de Lote.");
					contErros += 1;
				}
				if (!(arqEntrada.get(i - 1).substring(7, 8).equals("3")
						&& arqEntrada.get(i - 1).substring(13, 14).equals("J"))
						&& (!(arqEntrada.get(i - 1).substring(7, 8).equals("1")
								&& Arrays.asList("30", "31").contains(arqEntrada.get(i - 1).substring(11, 13))))) {
					verificacaoPorLinha.add("Linha " + String.format("%06d", i + 1)
							+ ": Registro Detalhe Segmento J n�o precedido de Registro Detalhe Segmento J ou Header de Lote J.");
					contErros += 1;
				}
			}

			// Verifica��o de Segmento J52
			if (arqEntrada.get(i).substring(7, 8).equals("3") && arqEntrada.get(i).substring(13, 14).equals("J")) {
				if (Integer.parseInt(arqEntrada.get(i).substring(152, 167)) > 25000000)
					if (!arqEntrada.get(i + 1).substring(17, 19).equals("52")) {
						verificacaoPorLinha.add("Linha " + String.format("%06d", i + 1)
								+ ": Para t�tulos de valor superior a R$ 250.000,00 � obrigat�ria a utiliza��o de Segmento J52 na linha seguinte.");
						contErros += 1;
					}
				if (arqEntrada.get(i + 1).substring(17, 19).equals("52")) {
					if (!(arqEntrada.get(i + 1).substring(7, 8).equals("3")
							&& arqEntrada.get(i + 1).substring(13, 14).equals("J"))
							&& !arqEntrada.get(i + 1).substring(7, 8).equals("5")) {
						verificacaoPorLinha.add("Linha " + String.format("%06d", i + 1)
								+ ": Registro Detalhe Segmento J52 n�o seguido de Registro Detalhe Segmento J ou Trailer de Lote.");
						contErros += 1;
					}
					if (!(arqEntrada.get(i - 1).substring(7, 8).equals("3")
							&& arqEntrada.get(i - 1).substring(13, 14).equals("J"))) {
						verificacaoPorLinha.add("Linha " + String.format("%06d", i + 1)
								+ ": Registro Detalhe Segmento J52 n�o precedido de Registro Detalhe Segmento J.");
						contErros += 1;
					}
				}
			}

			// Verifica��o de Segmento O
			if (arqEntrada.get(i).substring(7, 8).equals("3") && arqEntrada.get(i).substring(13, 14).equals("O")) {
				contON += 1;
				if (!(arqEntrada.get(i + 1).substring(7, 8).equals("3")
						&& Arrays.asList("O", "N").contains(arqEntrada.get(i + 1).substring(13, 14)))
						&& !arqEntrada.get(i + 1).substring(7, 8).equals("5")) {
					verificacaoPorLinha.add("Linha " + String.format("%06d", i + 1)
							+ ": Registro Detalhe Segmento O n�o seguido de Registro Detalhe Segmento O, Segmento N ou Trailer de Lote.");
					contErros += 1;
				}
				if (!(arqEntrada.get(i - 1).substring(7, 8).equals("3")
						&& Arrays.asList("O", "N").contains(arqEntrada.get(i - 1).substring(13, 14)))
						&& !(arqEntrada.get(i - 1).substring(7, 8).equals("1")
								&& Arrays.asList("16", "17", "18", "19", "21", "22", "23", "24", "25", "26", "27")
										.contains(arqEntrada.get(i - 1).substring(11, 13)))) {
					verificacaoPorLinha.add("Linha " + String.format("%06d", i + 1)
							+ ": Registro Detalhe Segmento O n�o precedido de Registro Detalhe Segmento O, Segmento N ou Header de Lote O/N.");
					contErros += 1;
				}
			}

			// Verifica��o de Segmento N
			if (arqEntrada.get(i).substring(7, 8).equals("3") && arqEntrada.get(i).substring(13, 14).equals("N")) {
				contON += 1;
				if (!(arqEntrada.get(i + 1).substring(7, 8).equals("3")
						&& Arrays.asList("O", "N").contains(arqEntrada.get(i + 1).substring(13, 14)))
						&& !arqEntrada.get(i + 1).substring(7, 8).equals("5")) {
					verificacaoPorLinha.add("Linha " + String.format("%06d", i + 1)
							+ ": Registro Detalhe Segmento N n�o seguido de Registro Detalhe Segmento O, Segmento N ou Trailer de Lote.");
					contErros += 1;
				}
				if (!(arqEntrada.get(i - 1).substring(7, 8).equals("3")
						&& Arrays.asList("O", "N").contains(arqEntrada.get(i - 1).substring(13, 14)))
						&& !(arqEntrada.get(i - 1).substring(7, 8).equals("1")
								&& Arrays.asList("16", "17", "18", "19", "21", "22", "23", "24", "25", "26", "27")
										.contains(arqEntrada.get(i - 1).substring(11, 13)))) {
					verificacaoPorLinha.add("Linha " + String.format("%06d", i + 1)
							+ ": Registro Detalhe Segmento N n�o precedido de Registro Detalhe Segmento O, Segmento N ou Header de Lote O/N.");
					contErros += 1;
				}
				if (!Arrays.asList("16", "17", "18", "19", "21", "22", "23", "24", "25", "26", "27")
						.contains(arqEntrada.get(i).substring(132, 134))) {
					verificacaoPorLinha.add("Linha " + String.format("%06d", i + 1)
							+ ": Registro Detalhe Segmento N n�o possui complemento N1, N2, N3, N4, N5, N6, N7 ou N8.");
					contErros += 1;
				}
			}
		}

		// Verifica��o de exist�ncia de Segmento Detalhe e Header de Lote
		if (HLA > 0 && contA == 0) {
			verificacaoPorSegmento
					.add("O arquivo cont�m Registro Header do tipo A/B/C mas n�o cont�m Registro Detalhe Segmento A.");
			contErros += 1;
		}
		if (HLA == 0 && contA > 0) {
			verificacaoPorSegmento
					.add("O arquivo cont�m Registro Detalhe Segmento A mas n�o cont�m Registro Header do tipo A/B/C.");
			contErros += 1;
		}
		if (HLA > 0 && contB == 0) {
			verificacaoPorSegmento
					.add("O arquivo cont�m Registro Header do tipo A/B/C mas n�o cont�m Registro Detalhe Segmento B.");
			contErros += 1;
		}
		if (HLA == 0 && contB > 0) {
			verificacaoPorSegmento
					.add("O arquivo cont�m Registro Detalhe Segmento B mas n�o cont�m Registro Header do tipo A/B/C.");
			contErros += 1;
		}
		if (HLA == 0 && contC > 0) {
			verificacaoPorSegmento
					.add("O arquivo cont�m Registro Detalhe Segmento C mas n�o cont�m Registro Header do tipo A/B/C.");
			contErros += 1;
		}
		if (HLJ > 0 && contJ == 0) {
			verificacaoPorSegmento
					.add("O arquivo cont�m Registro Header do tipo J mas n�o cont�m Registro Detalhe Segmento J.");
			contErros += 1;
		}
		if (HLJ == 0 && contJ > 0) {
			verificacaoPorSegmento
					.add("O arquivo cont�m Registro Detalhe Segmento J mas n�o cont�m Registro Header do tipo J.");
			contErros += 1;
		}
		if (HLON > 0 && contON == 0) {
			verificacaoPorSegmento.add(
					"O arquivo cont�m Registro Header do tipo O/N mas n�o cont�m Registro Detalhe Segmento O ou Segmento N.");
			contErros += 1;
		}
		if (HLJ == 0 && contON > 0) {
			verificacaoPorSegmento.add(
					"O arquivo cont�m Registro Detalhe Segmento O ou Segmento N mas n�o cont�m Registro Header do tipo O/N.");
			contErros += 1;
		}

		// Grava��o no arquivo de sa�da dos erros encontrados
		for (int i = 0; i < verificacaoPorSegmento.size(); i++)
			if (!verificacaoPorSegmento.get(i).equals(null))
				resultadoVerificacaoEstrutural.add(verificacaoPorSegmento.get(i));

		for (int i = 0; i < verificacaoPorLinha.size(); i++)
			if (!verificacaoPorLinha.get(i).equals(null))
				resultadoVerificacaoEstrutural.add(verificacaoPorLinha.get(i));

		// Verifica��o final de erros
		if (contErros == 0)
			resultadoVerificacaoEstrutural.add("O arquivo n�o cont�m erros de estrutura.");

		// Retorno do m�todo
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