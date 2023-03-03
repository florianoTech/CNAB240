package br.com.cnab240.core;

import java.util.ArrayList;
import java.util.Arrays;

import org.apache.commons.lang3.StringUtils;

import br.com.cnab240.entity.Convenente;

/**
 * @author Daniel Floriano
 * 
 *         Classe que contém os métodos utilizados no teste de conteúdo de
 *         arquivos
 */
/**
 * @author Andrezza
 *
 */
public class ConteudoCore {
	private ArrayList<String> L;
	ArrayList<String> resultadoVerificacaoConteudo = new ArrayList<String>();
	private long contErros;
	private Convenente convenente;
	private ConvenenteCore convenenteCore = new ConvenenteCore(L);

	/**
	 * @param l
	 * @param contErros
	 * @param banco
	 * @param convenente
	 */
	public ConteudoCore(ArrayList<String> l, Long contErros, Convenente convenente, ConvenenteCore convenenteCore) {
		L = l;
		this.contErros = contErros;
		this.convenente = convenente;
		this.convenenteCore = convenenteCore;
	}

	/**
	 * Método que compara uma String informada (J) com parte de uma linha de um
	 * arquivo, parte esta delimitada pelos índices a e b
	 *
	 * @param k
	 * @param a
	 * @param b
	 * @param J
	 * @return
	 */
	public void compara(int k, int a, int b, String J) {
		String erro;

		if (a < b && !J.contains(L.get(k).substring(a - 1, b))) {
			erro = String.format("Posições %03d a %03d: Valor esperado - %s; Valor encontrado - %s", 
					a, 
					b, 
					J.replaceAll("  ", ""),
					L.get(k).substring(a - 1, b)).replaceAll("  ", "");
			contErros += 1;
		} else if (a == b && !J.contains(L.get(k).substring(a - 1, b))) {
			erro = String.format("Posição  %03d      : Valor esperado - %s; Valor encontrado - %s", a, J,
					L.get(k).substring(a - 1, b));
			contErros += 1;
		} else
			erro = null;
		
		resultadoVerificacaoConteudo.add(erro);
	}

	public void compara(int k, int a, int b, String[] J) {
		String erro;
		if (!Arrays.asList(J).contains(L.get(k).substring(a - 1, b))) {
			erro = String.format("Posições %03d a %03d: Valor esperado - %s; Valor encontrado - %s", a, b, Arrays.asList(J),
					L.get(k).substring(a - 1, b));
			contErros += 1;
		} else {
			erro = null;
		}
		resultadoVerificacaoConteudo.add(erro);
	}

	/**
	 * Método que verifica se uma String composta de parte de uma linha delimitada
	 * pelos índices a e b é numérica ou alfanumérica
	 * 
	 * @param k
	 * @param a
	 * @param b
	 * @param tipo
	 * @return
	 */
	public void verificaTipo(int k, int a, int b, String tipo) {
		String erro = null;

		if (tipo == "digit") {
			try {
				Long.parseLong(L.get(k).substring(a - 1, b));
				erro = null;
			} catch (Exception ex) {
				erro = String.format(
						"Posições %03d a %03d: Valor esperado - valores numéricos; Valores encontrados - %s", a, b,
						L.get(k).substring(a - 1, b));
				contErros += 1;

			}
		} else if (tipo == "name") {
			if (L.get(k).substring(a - 1, b) == StringUtils.repeat(" ", L.get(k).substring(a - 1, b).length())) {
				erro = String.format(
						"Posições %03d a %03d: Valore esperado - valores alfanuméricos; Valores encontrados - %s", a, b,
						L.get(k).substring(a - 1, b));
				contErros += 1;
			}
		}
		resultadoVerificacaoConteudo.add(erro);
	}

	/**
	 * Método que recebe dois índices a e b para delimitar uma String em uma linha
	 * de um arquivo e retorna true caso se trate de valor numérico, e false caso
	 * não
	 * 
	 * @param k
	 * @param a
	 * @param b
	 * @return
	 */
	public boolean verificaTipoDigit(int k, int a, int b) {
		try {
			Long.parseLong(L.get(k).substring(a - 1, b));
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	/**
	 * Método que verifica se uma string definida pelo parâmetro nome está contida
	 * em parte de uma linha de um] arquivo, parte esta delimitada pelos índices a e
	 * b
	 * 
	 * @param k
	 * @param a
	 * @param b
	 * @param nome
	 * @return
	 */
	public void verificaNome(int k, int a, int b, String nome) {
		String erro;

		if (!L.get(k).substring(a - 1, b).toUpperCase().contains(nome.toUpperCase())) {
			erro = String.format("Posições %03d a %03d: Valor esperado - %s; Valor encontrado - %s", a, b, nome,
					L.get(k).substring(a - 1, b));
			contErros += 1;
		} else
			erro = null;

		resultadoVerificacaoConteudo.add(erro);
	}

	/**
	 * Método que recebe como parâmetros os índices a e b, e verifica se uma Data
	 * não contém valores inconsistentes
	 * 
	 * @param k
	 * @param a
	 * @param b
	 * @return
	 */
	public void verificaData(int k, int a, int b) {
		String erro;

		if (!Arrays.asList("01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12")
				.contains(L.get(k).substring(a + 1, a + 3))) {
			erro = String.format("Posições %03d a %03d: Valor esperado - entre 01 e 12; Valor encontrado - %s", a + 2,
					a + 3, L.get(k).substring(a + 1, a + 3));
			contErros += 1;
		} else if (Arrays.asList("01", "03", "05", "07", "08", "10", "12").contains(L.get(k).substring(a + 1, a + 3))
				&& Short.parseShort(L.get(k).substring(a - 1, a + 1)) > 31
				|| Short.parseShort(L.get(k).substring(a - 1, a + 1)) == 0) {
			erro = String.format("Posições %03d a %03d: Valor esperado - entre 01 e 31; Valor encontrado - %s", a - 1, a,
					L.get(k).substring(a - 1, a + 1));
			contErros += 1;
		} else if (Arrays.asList("02", "04", "06", "09", "11").contains(L.get(k).substring(a + 1, a + 3))
				&& Short.parseShort(L.get(k).substring(a - 1, a + 1)) > 31
				|| Short.parseShort(L.get(k).substring(a - 1, a + 1)) == 0) {
			erro = String.format("Posições %03d a %03d: Valor esperado - entre 01 e 30; Valor encontrado - %s", a - 1, a,
					L.get(k).substring(a - 1, a + 1));
			contErros += 1;
		} else
			erro = null;

		resultadoVerificacaoConteudo.add(erro);
	}

	/**
	 * Método que recebe como parâmetros os índices a e b, e verifica se um horário
	 * não contém valores inconsistentes
	 * 
	 * @param k
	 * @param a
	 * @param b
	 * @return
	 */
	public void verificaHora(int k, int a, int b) {
		String erro;

		if (Short.parseShort(L.get(k).substring(a - 1, a + 1)) > 24) {
			erro = String.format("Posições %03d a %03d: Valor esperado - entre 00 e 24; Valor encontrado - %s", a, a + 1,
					L.get(k).substring(a - 1, a + 1));
			contErros += 1;
		} else if (Short.parseShort(L.get(k).substring(a + 1, a + 3)) > 59) {
			erro = String.format("Posições %03d a %03d: Valor esperado - entre 00 e 59; Valor encontrado - %s", a + 2,
					a + 3, L.get(k).substring(a + 1, a + 3));
			contErros += 1;
		} else if (Short.parseShort(L.get(k).substring(a + 3, a + 5)) > 59) {
			erro = String.format("Posições %03d a %03d: Valor esperado - entre 00 e 59; Valor encontrado - %s", a + 4,
					a + 5, L.get(k).substring(a + 3, a + 5));
			contErros += 1;
		} else
			erro = null;

		resultadoVerificacaoConteudo.add(erro);
	}

	/**
	 * Método que valida um código de barras constante em uma String pertencente a
	 * uma linha de um arquivo, delimitada pelos índices a e b
	 * 
	 * @param k
	 * @param a
	 * @param b
	 * @return
	 */
	public void verificaCodBarras(int k, int a, int b) {
		String erro;

		String A = L.get(k).substring(a - 1, b);
		try {
			Long.parseLong(L.get(k).substring(a - 1, b));
			String B = A.substring(0, 4) + "0" + A.substring(5, 44);
			long S = Long.parseLong(StringUtils.repeat(B.charAt(0), 4) + StringUtils.repeat(B.charAt(1), 3)
					+ StringUtils.repeat(B.charAt(2), 2) + StringUtils.repeat(B.charAt(3), 9));
			for (int i = 13; i < 53; i += 8)
				for (int j = 9; j >= 1; j--)
					S += Long.parseLong(StringUtils.repeat(B.charAt(i - j), j));
			short d = (short) (11 - S % 11);
			if (d == 0 || d == 10 || d == 11)
				d = 1;
			B = A.substring(0, 4) + Short.toString(d) + A.substring(5, 44);
			if (Short.toString(d).equals(A.substring(4, 5))) {
				erro = String.format("Posições %03d a %03d: Valor esperado - %s - Valor encontrado: %s", a, b, B, A);
				contErros += 1;
			} else
				erro = null;
		} catch (NumberFormatException e) {
			erro = String.format("Posições %03d a %03d: Valor esperado - Valores numéricos; Valor encontrado - %s", a, b,
					L.get(k).substring(a - 1, b));
			contErros += 1;
		}

		resultadoVerificacaoConteudo.add(erro);
	}
	
	
	/**
	 * Método que chama o método de mesmo nome da classe ConvenenteCore
	 * 
	 * @param k
	 * @param a
	 * @param b
	 */
	public void verificaAGCONTA(int k, int a, int b) {
		String erro;
		try {
			erro = convenenteCore.verificaAGCONTA(k, a, b);
			if (erro != null) {
				resultadoVerificacaoConteudo.add(erro);
				contErros += 1;
			}
		} catch (NullPointerException e) {
			erro = null;
		}
		
	}
	
	/**
	 * Método que chama o método de mesmo nome da classe ConvenenteCore
	 * 
	 * @param k
	 * @param a
	 * @param b
	 */
	public void verificaCNPJ(int k, int a, int b) {
		String erro;
		try {
			erro = convenenteCore.verificaCNPJ(k, a, b);
			if (erro != null) {
				resultadoVerificacaoConteudo.add(erro);
				contErros += 1;
			}
		} catch (NullPointerException e) {
			erro = null;
		}	
	}
	
	/**
	 * Método que chama o método de mesmo nome da classe ConvenenteCore
	 * 
	 * @param k
	 * @param a
	 * @param b
	 */
	public void verificaCPF(int k, int a, int b) {
		String erro;
		
		try {
			erro = convenenteCore.verificaCPF(k, a, b);
			if (erro != null) {
				resultadoVerificacaoConteudo.add(erro);
				contErros += 1;
			}
		} catch (NullPointerException e) {
			erro = null;
		}
	}

	public ArrayList<String> cnab240() {

		@SuppressWarnings("unused")
		Integer HA = 0, HLAB = 0, HLJ = 0, HLON = 0, A = 0, B = 0, J = 0, J52 = 0, O = 0, N = 0, N1 = 0, N2 = 0, N3 = 0,
				N4 = 0, TLAB = 0, TLJ = 0, TA = 0;
		@SuppressWarnings("unused")
		Integer somaA = 0, somaB = 0, somaJ = 0, somaON = 0;
		Integer contHL = 0, contDetalheAB = 0, contDetalheJ = 0, contDetalheON = 0;
		Integer contDetalheABT = 0, contDetalheJT = 0, contDetalheONT = 0;
		long cont1 = 0, cont2 = 0;
		StringBuilder HL = new StringBuilder();

		String nrInsc = String.format("%014d", convenente.getNrInsc());
		String nome = StringUtils.rightPad(convenente.getNome(), 30, " ");
		String banco = String.format("%03d", convenente.getBanco());
		String ag = String.format("%05d", convenente.getAg());
		String agDv = convenente.getAgDv().toUpperCase();
		String conta = String.format("%012d", convenente.getConta());
		String contaDv = convenente.getContaDv().toUpperCase();
		String convenio = StringUtils.rightPad(convenente.getConvenio(), 20, " ");
		String logradouro = StringUtils.rightPad(convenente.getLogradouro(), 30, " ");
		String numero = String.format("%05d", convenente.getNumero());
		String complemento = StringUtils.rightPad(convenente.getComplemento(), 15, " ");
		String cidade = StringUtils.rightPad(convenente.getCidade(), 20, " ");
		String estado = StringUtils.rightPad(convenente.getEstado(), 2, " ");
		String cep = String.format("%05d", convenente.getCep());
		String cepComp = StringUtils.rightPad(convenente.getCepComp(), 3, " ");

		// Início da validação
		for (int x = 0; x < L.size() / 2; x++) {
			// Registro Header de Arquivo
			if (L.get(x).charAt(7) == '0') {
				HA = x;
				resultadoVerificacaoConteudo.add("\nLinha " +String.format("%06d", x+1) +": Registro Header de Arquivo");
				cont1 = contErros;
				compara(x, 1, 3, banco);
				compara(x, 4, 7, "0000");
				compara(x, 9, 17, "         ");
				compara(x, 18, 18, new String[] { "1", "2" });

				if (L.get(x).charAt(17) == '1' && !L.get(x).substring(18, 32).equals(nrInsc))
					compara(x, 19, 32, nrInsc);
				if (L.get(x).charAt(17) == '2' && !L.get(x).substring(18, 32).equals(nrInsc))
					compara(x, 19, 32, nrInsc);

				compara(x, 33, 52, convenio);
				compara(x, 53, 57, ag);
				compara(x, 58, 58, agDv);
				compara(x, 59, 70, conta);
				compara(x, 71, 71, contaDv);
				compara(x, 73, 102, nome);
				verificaTipo(x, 103, 132, "name");
				compara(x, 133, 142, "          ");
				compara(x, 143, 143, "1");
				verificaData(x, 144, 151);
				verificaHora(x, 152, 157);
				verificaTipo(x, 158, 163, "digit");
				compara(x, 172, 191, new String[] { "                    ", "00000000000000000000" });
				compara(x, 212, 240, new String[] { "                             ", "           CSP000            " });
				cont2 = contErros;
				if (cont1 == cont2)
					resultadoVerificacaoConteudo.remove("\nLinha " +String.format("%06d", x+1) +": Registro Header de Arquivo");
			}
			// Registro Header de Lote
			if (L.get(x).charAt(7) == '1') {
				contHL += 1;
				if (Arrays.asList("20", "30", "98").contains(L.get(x).substring(9, 11)) && Arrays
						.asList("01", "02", "03", "04", "05", "10", "41", "43").contains(L.get(x).substring(11, 13))) {
					HLAB = x;
					HL.replace(0, HL.length(), "AB");
					somaA = 0;
					contDetalheAB = 0;
					resultadoVerificacaoConteudo.add("\nLinha " +String.format("%06d", x+1) +": Registro Header de Lote A/B");
					cont1 = contErros;
					compara(x, 1, 3, banco);
					compara(x, 4, 7, StringUtils.leftPad(contHL.toString(), 4, "0"));
					compara(x, 9, 9, "C");
				} else if (Arrays.asList("20", "98").contains(L.get(x).substring(9, 11))
						&& Arrays.asList("30", "31").contains(L.get(x).substring(11, 13))) {
					HLJ = x;
					HL.replace(0, HL.length(), "J");
					somaJ = 0;
					contDetalheJ = 0;
					resultadoVerificacaoConteudo.add("\nLinha " +String.format("%06d", x+1) +": Registro Header de Lote J");
					cont1 = contErros;
					compara(x, 1, 3, banco);
					compara(x, 4, 7, StringUtils.leftPad(contHL.toString(), 4, "0"));
					compara(x, 9, 9, "C");
				} else if (L.get(x).substring(9, 11).equals("98") && Arrays
						.asList("11", "16", "17", "18", "21", "22", "23", "24").contains(L.get(x).substring(11, 13))) {
					HLON = x;
					HL.replace(0, HL.length(), "ON");
					somaON = 0;
					contDetalheON = 0;
					resultadoVerificacaoConteudo.add("\nLinha " +String.format("%06d", x+1) +": Registro Header de Lote O/N");
					cont1 = contErros;
					compara(x, 1, 3, banco);
					compara(x, 4, 7, StringUtils.leftPad(contHL.toString(), 4, "0"));
					compara(x, 9, 9, "C");
					compara(x, 14, 16, "010");
				}
				compara(x, 17, 17, " ");
				compara(x, 18, 18, new String[] { "1", "2" });
				if (L.get(x).charAt(17) == '1' && L.get(x).substring(18, 32) != nrInsc)
					compara(x, 19, 32, nrInsc);
				if (L.get(x).charAt(17) == '2' && L.get(x).substring(18, 32) != nrInsc)
					compara(x, 19, 32, nrInsc);
				compara(x, 33, 52, convenio);
				compara(x, 53, 57, ag);
				compara(x, 58, 58, agDv);
				compara(x, 59, 70, conta);
				compara(x, 71, 71, contaDv);
				compara(x, 73, 102, nome);
				verificaTipo(x, 103, 142, "name");
				compara(x, 143, 172, logradouro);
				compara(x, 173, 177, numero);
				compara(x, 178, 192, complemento);
				compara(x, 193, 212, cidade);
				compara(x, 213, 217, cep);
				compara(x, 218, 220, cepComp);
				compara(x, 221, 222, estado);
				compara(x, 231, 240, "          ");
				cont2 = contErros;
				if (cont1 == cont2) {
					resultadoVerificacaoConteudo.remove("\nLinha " +String.format("%06d", x+1) +": Registro Header de Lote A/B");
					resultadoVerificacaoConteudo.remove("\nLinha " +String.format("%06d", x+1) +": Registro Header de Lote J");
					resultadoVerificacaoConteudo.remove("\nLinha " +String.format("%06d", x+1) +": Registro Header de Lote O/N");
				}
			}

			// Registro Detalhe Segmento A:
			if (L.get(x).charAt(7) == '3' && L.get(x).charAt(13) == 'A') {
				A = x;
				contDetalheAB += 1;
				contDetalheABT += 1;
				resultadoVerificacaoConteudo.add("\nLinha " +String.format("%06d", x+1) +": Registro Detalhe segmento A");
				cont1 = contErros;
				compara(x, 1, 3, banco);
				compara(x, 4, 7, L.get(HLAB).substring(3, 7));
				compara(x, 9, 13, StringUtils.leftPad(contDetalheAB.toString(), 5, "0"));
				compara(x, 14, 14, "A");
				compara(x, 15, 15, new String[] { "0", "9" });
				compara(x, 16, 17, new String[] { "00", "99" });
				if (L.get(HLAB).substring(11, 13) == "03")
					compara(x, 18, 20, new String[] { "700", "018" });
				else if (Arrays.asList("41", "43").contains(L.get(HLAB).substring(11, 13)))
					compara(x, 18, 20, "018");
				else if (L.get(HLAB).substring(11, 13) == "01")
					compara(x, 18, 20, "000");
				verificaTipo(x, 21, 23, "digit");
				verificaAGCONTA(x, 24, 29);
				verificaAGCONTA(x, 30, 42);
				compara(x, 43, 43, " ");
				verificaTipo(x, 44, 73, "alnum");
				verificaTipo(x, 74, 85, "alnum");
				verificaData(x, 94, 101);
				compara(x, 102, 104, "BRL");
				compara(x, 105, 119, "000000000000000");
				verificaTipo(x, 120, 134, "digit");
				if (verificaTipoDigit(x, 120, 134))
					somaA = somaA + Integer.parseInt(L.get(x).substring(119, 134));
				compara(x, 135, 154, "                    ");
				compara(x, 155, 162, "00000000");
				compara(x, 163, 177, "000000000000000");
				verificaTipo(x, 178, 217, "alnum");
				compara(x, 218, 219, new String[] { "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11",
						"12", "13", "16", "17", "18", "19" });
				compara(x, 220, 224, new String[] { "00001", "00002", "00003", "00004", "00005", "00006", "00007",
						"00008", "00009", "00010", "00011", "00012", "00013", "00014", "00015", "00016", "00017",
						"00018", "00019", "00020", "00021", "00022", "00023", "00024", "00025", "00026", "00027",
						"00028", "00029", "00030", "00031", "00032", "00033", "00034", "00035", "00036", "00037",
						"00038", "00039", "00040", "00041", "00042", "00043", "00044", "00045", "00047", "00050",
						"00097", "00100", "00101", "00103", "00104", "00107", "00108", "00109", "00110", "00111",
						"00112", "00113", "00114", "00117", "00123", "00124", "00131", "00132", "00139", "00200",
						"00201", "00202", "00203", "00204", "00205", "00206", "00207", "00208", "00209", "00300",
						"00301", "00302", "00303", "00400", "00500", "00501", "00502", "00503", "00504", "00505",
						"00506", "00507", "00508", "00509", "00510", "00511", "00512", "00513", "00514", "00515",
						"00516", "00517", "00518", "00519", "00520", "99999" });
				verificaTipo(x, 225, 226, "alnum");
				compara(x, 227, 229, "   ");
				compara(x, 230, 230, new String[] { "0", "5" });
				compara(x, 231, 240, "          ");
				cont2 = contErros;
				if (cont1 == cont2) 
					resultadoVerificacaoConteudo.remove("\nLinha " +String.format("%06d", x+1) +": Registro Detalhe segmento A");
			}

			// Registro Detalhe Segmento B:
			if (L.get(x).charAt(7) == '3' && L.get(x).charAt(13) == 'B') {
				B = x;
				contDetalheAB += 1;
				contDetalheABT += 1;
				resultadoVerificacaoConteudo.add("\nLinha " +String.format("%06d", x+1) +": Registro Detalhe segmento B");
				cont1 = contErros;
				compara(x, 1, 3, banco);
				compara(x, 4, 7, L.get(HLAB).substring(3, 7));
				compara(x, 9, 13, StringUtils.leftPad(contDetalheAB.toString(), 5, "0"));
				compara(x, 14, 14, "B");
				compara(x, 15, 17, "   ");
				compara(x, 18, 18, new String[] { "0", "1", "2", "3", "9" });
				if (L.get(x).charAt(17) == '1')
					verificaCPF(x, 19, 32);
				else if (L.get(x).charAt(17) == '2')
					verificaCNPJ(x, 19, 32);
				compara(x, 226, 226, new String[] { "0", "5" });
				compara(x, 233, 240, "        ");
				cont2 = contErros;
				if (cont1 == cont2)
					resultadoVerificacaoConteudo.remove("\nLinha " +String.format("%06d", x+1) +": Registro Detalhe segmento B");
			}

			// Registro Detalhe Segmento J:
			if (L.get(x).charAt(7) == '3' && L.get(x).charAt(13) == 'J' && L.get(x).substring(17, 19) != "52") {
				J = x;
				contDetalheJ += 1;
				contDetalheJT += 1;
				resultadoVerificacaoConteudo.add("\nLinha " +String.format("%06d", x+1) +": Registro Detalhe segmento J");
				cont1 = contErros;
				compara(x, 1, 3, banco);
				compara(x, 4, 7, L.get(HLJ).substring(3, 7));
				compara(x, 9, 13, StringUtils.leftPad(contDetalheJ.toString(), 5, "0"));
				compara(x, 15, 15, new String[] { "0", "9" });
				compara(x, 16, 17, new String[] { "00", "99" });
				verificaCodBarras(x, 18, 61);
				verificaTipo(x, 62, 91, "name");
				verificaData(x, 92, 99);
				verificaTipo(x, 100, 114, "digit");
				verificaTipo(x, 115, 129, "digit");
				compara(x, 130, 144, "000000000000000");
				verificaData(x, 145, 152);
				verificaTipo(x, 153, 167, "digit");
				if (verificaTipoDigit(x, 153, 167))
					somaJ += Integer.parseInt(L.get(x).substring(152, 167));
				compara(x, 168, 182, "000000000000000");
				verificaTipo(x, 183, 202, "alnum");
				verificaTipo(x, 203, 22, "alnum");
				compara(x, 223, 224, "09");
				compara(x, 225, 230, "      ");
				compara(x, 231, 240, "          ");
				cont2 = contErros;
				if (cont1 == cont2)
					resultadoVerificacaoConteudo.remove("\nLinha " +String.format("%06d", x+1) +": Registro Detalhe segmento J");
			}

			// Registro Detalhe Segmento J52:
			if (L.get(x).charAt(7) == '3' && L.get(x).charAt(13) == 'J' && L.get(x).substring(17, 19) == "52") {
				J52 = x;
				contDetalheJ += 1;
				contDetalheJT += 1;
				resultadoVerificacaoConteudo.add("\nLinha " +String.format("%06d", x+1) +": Registro Detalhe segmento J52");
				cont1 = contErros;
				compara(x, 1, 3, banco);
				compara(x, 4, 7, L.get(HLJ).substring(3, 7));
				compara(x, 9, 13, StringUtils.leftPad(contDetalheJ.toString(), 5, "0"));
				compara(x, 15, 15, " ");
				compara(x, 16, 17, new String[] { "00", "99" });
				compara(x, 20, 20, new String[] { "1", "2" });
				if (L.get(x).charAt(19) == '1')
					verificaCPF(x, 21, 35);
				else if (L.get(x).charAt(19) == '2')
					verificaCNPJ(x, 21, 35);
				verificaTipo(x, 36, 75, "name");
				compara(x, 76, 76, new String[] { "1", "2" });
				if (L.get(x).charAt(75) == '1')
					verificaCPF(x, 77, 91);
				else if (L.get(x).charAt(75) == '2')
					verificaCNPJ(x, 77, 91);
				verificaTipo(x, 92, 131, "name");
				compara(x, 132, 132, new String[] { "0", "1", "2" });
				if (L.get(x).charAt(131) == '0') {
					compara(x, 133, 147, "000000000000000");
					compara(x, 148, 187, "                                        ");
				} else if (L.get(x).charAt(131) == '1') {
					verificaCPF(x, 133, 147);
					verificaTipo(x, 133, 147, "name");
				} else if (L.get(x).charAt(131) == '2') {
					verificaCNPJ(x, 133, 147);
					verificaTipo(x, 133, 147, "name");
					compara(x, 188, 240, "                                                     ");
					cont2 = contErros;
					if (cont1 == cont2)
						resultadoVerificacaoConteudo.remove("\nLinha " +String.format("%06d", x+1) +": Registro Detalhe segmento J52");
				}
			}

			// Registro Detalhe Segmento O:
			if (L.get(x).charAt(7) == '3' && L.get(x).charAt(13) == 'O') {
				O = x;
				contDetalheON += 1;
				contDetalheONT += 1;
				resultadoVerificacaoConteudo.add("\nLinha " +String.format("%06d", x+1) +": Registro Detalhe segmento O");
				cont1 = contErros;
				compara(x, 1, 3, banco);
				compara(x, 4, 7, L.get(HLON).substring(3, 7));
				compara(x, 9, 13, StringUtils.leftPad(contDetalheON.toString(), 5, "0"));
				compara(x, 15, 15, "0");
				compara(x, 16, 17, "00");
				verificaTipo(x, 62, 91, "name");
				verificaData(x, 92, 99);
				verificaData(x, 100, 107);
				verificaTipo(x, 108, 122, "digit");
				if (verificaTipoDigit(x, 108, 122))
					somaON += Integer.parseInt(L.get(x).substring(107, 122));
				verificaTipo(x, 123, 142, "name");
				verificaTipo(x, 143, 162, "name");
				compara(x, 163, 230, StringUtils.repeat(" ", 68));
				compara(x, 231, 240, "          ");
				cont2 = contErros;
				if (cont1 == cont2)
					resultadoVerificacaoConteudo.remove("\nLinha " +String.format("%06d", x+1) +": Registro Detalhe segmento O");
			}

			// Registro Detalhe Segmento N:
			if (L.get(x).charAt(7) == '3' && L.get(x).charAt(13) == 'N') {
				if (L.get(x).substring(132, 134) == "17") {
					N1 = x;
					contDetalheON += 1;
					contDetalheONT += 1;
					resultadoVerificacaoConteudo.add("\nLinha " +String.format("%06d", x+1) +": Registro Detalhe segmento N1 - GPS");
					cont1 = contErros;

				} else if (L.get(x).substring(132, 134) == "16") {
					N2 = x;
					contDetalheON += 1;
					contDetalheONT += 1;
					resultadoVerificacaoConteudo.add("\nLinha " +String.format("%06d", x+1) +": Registro Detalhe segmento N2 - DARF");
					cont1 = contErros;

				} else if (L.get(x).substring(132, 134) == "17") {
					N3 = x;
					contDetalheON += 1;
					contDetalheONT += 1;
					resultadoVerificacaoConteudo.add("\nLinha " +String.format("%06d", x+1) +": Registro Detalhe segmento N3 - DARF Simples");
					cont1 = contErros;

				} else if (L.get(x).substring(132, 134) == "22") {
					N3 = x;
					contDetalheON += 1;
					contDetalheONT += 1;
					resultadoVerificacaoConteudo.add("\nLinha " +String.format("%06d", x+1) +": Registro Detalhe segmento N4 - GARE SP");
					cont1 = contErros;

				} else if (L.get(x).substring(132, 134) == "21") {
					N4 = x;
					contDetalheON += 1;
					contDetalheONT += 1;
					resultadoVerificacaoConteudo.add("\nLinha " +String.format("%06d", x+1) +": Registro Detalhe segmento N8 - DARJ");
					cont1 = contErros;
				}
				compara(x, 1, 3, banco);
				compara(x, 4, 7, L.get(HLON).substring(3, 7));
				compara(x, 9, 13, StringUtils.leftPad(contDetalheON.toString(), 5, "0"));
				compara(x, 15, 15, "0");
				compara(x, 16, 17, "00");
				verificaTipo(x, 18, 37, "name");
				verificaTipo(x, 38, 57, "name");
				verificaTipo(x, 58, 87, "name");
				verificaData(x, 88, 95);
				verificaTipo(x, 96, 110, "digit");
				if (verificaTipoDigit(x, 96, 110))
					somaON += Integer.parseInt(L.get(x).substring(95, 110));
				if (L.get(x).substring(132, 134) == "17") {
					verificaTipo(x, 111, 116, "digit");
					compara(x, 117, 118, new String[] { "01", "02", "03", "04", "06", "07", "08", "09" });
					if (L.get(x).substring(116, 118) == "01")
						verificaCNPJ(x, 119, 123);
					else if (L.get(x).substring(116, 118) == "02")
						verificaCPF(x, 119, 132);
					else
						verificaTipo(x, 119, 132, "digit");
					compara(x, 135, 136,
							new String[] { "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12" });
					verificaTipo(x, 137, 140, "digit");
					verificaTipo(x, 141, 155, "digit");
					verificaTipo(x, 156, 170, "digit");
					verificaTipo(x, 171, 185, "digit");
					compara(x, 186, 230, StringUtils.repeat(" ", 45));
				} else if (L.get(x).substring(132, 134) == "16") {
					verificaTipo(x, 111, 116, "digit");
					compara(x, 117, 118, new String[] { "01", "02", "03", "04", "06", "07", "08", "09" });
					if (L.get(x).substring(116, 118) == "01")
						verificaCNPJ(x, 119, 123);
					else if (L.get(x).substring(116, 118) == "02")
						verificaCPF(x, 119, 132);
					else
						verificaTipo(x, 119, 132, "digit");
					verificaData(x, 135, 142);
					verificaTipo(x, 143, 159, "digit");
					verificaTipo(x, 160, 174, "digit");
					verificaTipo(x, 175, 189, "digit");
					verificaTipo(x, 190, 204, "digit");
					verificaData(x, 205, 212);
					compara(x, 213, 230, StringUtils.repeat(" ", 18));
				} else if (L.get(x).substring(132, 134) == "18") {
					verificaTipo(x, 111, 116, "digit");
					compara(x, 117, 118, new String[] { "01", "02", "03", "04", "06", "07", "08", "09" });
					if (L.get(x).substring(116, 118) == "01")
						verificaCNPJ(x, 119, 123);
					else if (L.get(x).substring(116, 118) == "02")
						verificaCPF(x, 119, 132);
					else
						verificaTipo(x, 119, 132, "digit");
					verificaData(x, 135, 142);
					verificaTipo(x, 143, 157, "digit");
					verificaTipo(x, 158, 164, "digit");
					verificaTipo(x, 165, 179, "digit");
					verificaTipo(x, 180, 194, "digit");
					verificaTipo(x, 195, 209, "digit");
					compara(x, 210, 230, StringUtils.repeat(" ", 21));
				} else if (L.get(x).substring(132, 134) == "22") {
					verificaTipo(x, 111, 116, "digit");
					compara(x, 117, 118, new String[] { "01", "02", "03", "04", "06", "07", "08", "09" });
					if (L.get(x).substring(116, 118) == "01")
						verificaCNPJ(x, 119, 123);
					else if (L.get(x).substring(116, 118) == "02")
						verificaCPF(x, 119, 132);
					else
						verificaTipo(x, 119, 132, "digit");
					verificaData(x, 135, 142);
					verificaTipo(x, 143, 154, "digit");
					verificaTipo(x, 155, 167, "digit");
					compara(x, 168, 169,
							new String[] { "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12" });
					verificaTipo(x, 170, 173, "digit");
					verificaTipo(x, 174, 186, "digit");
					verificaTipo(x, 187, 201, "digit");
					verificaTipo(x, 202, 215, "digit");
					verificaTipo(x, 216, 229, "digit");
					compara(x, 230, 230, " ");
				} else if (L.get(x).substring(132, 134) == "21") {
					verificaTipo(x, 111, 116, "digit");
					compara(x, 117, 118, new String[] { "01", "02", "03", "04", "06", "07", "08", "09" });
					if (L.get(x).substring(116, 118) == "01")
						verificaCNPJ(x, 119, 123);
					else if (L.get(x).substring(116, 118) == "02")
						verificaCPF(x, 119, 132);
					else
						verificaTipo(x, 119, 132, "digit");
					verificaTipo(x, 133, 140, "name");
					verificaTipo(x, 141, 156, "digit");
					verificaTipo(x, 157, 171, "digit");
					verificaTipo(x, 172, 186, "digit");
					verificaTipo(x, 187, 201, "digit");
					verificaData(x, 202, 216);
					verificaData(x, 217, 224);
					compara(x, 225, 226,
							new String[] { "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12" });
					verificaTipo(x, 227, 230, "digit");
				}
				compara(x, 231, 240, "          ");
				cont2 = contErros;
				if (cont1 == cont2) {
					resultadoVerificacaoConteudo.remove("\nLinha " +String.format("%06d", x+1) +": Registro Detalhe segmento N1 - GPS");
					resultadoVerificacaoConteudo.remove("\nLinha " +String.format("%06d", x+1) +": Registro Detalhe segmento N2 - DARF");
					resultadoVerificacaoConteudo.remove("\nLinha " +String.format("%06d", x+1) +": Registro Detalhe segmento N3 - DARF Simples");
					resultadoVerificacaoConteudo.remove("\nLinha " +String.format("%06d", x+1) +": Registro Detalhe segmento N4 - GARE SP");
					resultadoVerificacaoConteudo.remove("\nLinha " +String.format("%06d", x+1) +": Registro Detalhe segmento N8 - DARJ");
				}
			}

			// Registro Trailer de Lote:
			if (L.get(x).charAt(7) == '5' && HL.toString().equals("AB")) {
				TLAB = x;
				resultadoVerificacaoConteudo.add("\nLinha " +String.format("%06d", x+1) +": Registro Trailer de Lote A/B");
				cont1 = contErros;
				compara(x, 1, 3, banco);
				compara(x, 4, 7, L.get(HLAB).substring(3, 7));
				compara(x, 9, 17, "         ");
				compara(x, 18, 23, StringUtils.leftPad(((Integer) (contDetalheAB + 2)).toString(), 6, "0"));
				compara(x, 24, 41, StringUtils.leftPad(somaA.toString(), 18, "0"));
				compara(x, 42, 59, "000000000000000000");
				compara(x, 60, 65, "000000");
				compara(x, 66, 230,
						"                                                                                                                                                                     ");
				compara(x, 231, 240, "          ");
			} else if (L.get(x).charAt(7) == '5' && HL.toString().equals("J")) {
				TLJ = x;
				resultadoVerificacaoConteudo.add("\nLinha " +String.format("%06d", x+1) +": Registro Trailer de Lote J");
				cont1 = contErros;

				compara(x, 1, 3, banco);
				compara(x, 4, 7, L.get(HLJ).substring(3, 7));
				compara(x, 9, 17, "         ");
				compara(x, 18, 23, StringUtils.leftPad(((Integer) (contDetalheAB + 2)).toString(), 6, "0"));
				compara(x, 24, 41, StringUtils.leftPad(somaJ.toString(), 18, "0"));
				compara(x, 42, 59, "000000000000000000");
				compara(x, 60, 65, "000000");
				compara(x, 66, 230,
						"                                                                                                                                                                     ");
				compara(x, 231, 240, "          ");
			} else if (L.get(x).charAt(7) == '5' && HL.toString().equals("ON")) {
				TLJ = x;
				resultadoVerificacaoConteudo.add("\nLinha " +String.format("%06d", x+1) +": Registro Trailer de Lote O/N");
				cont1 = contErros;
				compara(x, 1, 3, banco);
				compara(x, 4, 7, L.get(HLON).substring(3, 7));
				compara(x, 9, 17, "         ");
				compara(x, 18, 23, StringUtils.leftPad(((Integer) (contDetalheON + 2)).toString(), 6, "0"));
				compara(x, 24, 41, StringUtils.leftPad(somaON.toString(), 18, "0"));
				compara(x, 42, 59, "000000000000000000");
				compara(x, 60, 65, "000000");
				compara(x, 66, 230,
						"                                                                                                                                                                     ");
				compara(x, 231, 240, "          ");
			}
			cont2 = contErros;
			if (cont1 == cont2) {
				resultadoVerificacaoConteudo.remove("\nLinha " +String.format("%06d", x+1) +": Registro Trailer de Lote A/B");
				resultadoVerificacaoConteudo.remove("\nLinha " +String.format("%06d", x+1) +": Registro Trailer de Lote J");
				resultadoVerificacaoConteudo.remove("\nLinha " +String.format("%06d", x+1) +": Registro Trailer de Lote O/N");
			}

			// Registro Trailer de Arquivo:
			if (L.get(x).charAt(7) == '9') {
				TA = x;
				resultadoVerificacaoConteudo.add("\nLinha " +String.format("%06d", x+1) +": Registro Trailer de Arquivo");
				cont1 = contErros;
				compara(x, 1, 3, banco);
				compara(x, 4, 7, "9999");
				compara(x, 9, 17, "         ");
				compara(x, 18, 23, StringUtils.leftPad(contHL.toString(), 6, "0"));
				compara(x, 24, 29, StringUtils.leftPad(
						((Integer) (contDetalheABT + contDetalheJT + contDetalheONT + 2 * contHL + 2)).toString(), 6,
						"0"));
				compara(x, 30, 35, "000000");
				compara(x, 36, 240,
						"                                                                                                                                                                                                             ");
				cont2 = contErros;
				if (cont1 == cont2)
					resultadoVerificacaoConteudo.remove("\nLinha " +String.format("%06d", x+1) +": Registro Trailer de Arquivo");
			}
		}

		// Verificação final de erros
		if (contErros == 0)
			resultadoVerificacaoConteudo.add("\nO arquivo não contém erros de conteúdo.");

		// Retorno do método
		return resultadoVerificacaoConteudo;
	}

	/**
	 * @return
	 */
	public long getContErros() {
		return contErros;
	}

	public Convenente getConvenente() {
		return convenente;
	}
}
