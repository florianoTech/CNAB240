/*   Desenvolvido por Daniel Floriano
 */
package br.com.cnab240.core;

import java.util.ArrayList;

import org.apache.commons.lang3.StringUtils;

/**
 * @author Daniel Floriano
 * 
 *         Classe que cont�m os m�todos respons�veis pela avalia��o de dados de
 *         convenentes e d�gitos de ag�ncia, conta, CPF e CNPJ
 */
public class ConvenenteCore {

	// Lendo o arquivo de entrada e criando o arquivo de sa�da
	private ArrayList<String> L;
	

	public ConvenenteCore() {
	}
	
	/**
	 * @param l
	 */
	public ConvenenteCore(ArrayList<String> l) {
		super();
		L = l;
	}

	/**
	 * @param k
	 */
	public boolean verificaAGCONTA(String k) {
		int cont = 0;

		// Declara��o de vari�veis
		int m, d, S;
		String num, M;
		StringBuilder N;

		// Tratamento de d�gito 'X'
		if (k.endsWith("X") || k.endsWith("x"))
			num = k.substring(0, k.length() - 1) + "0";
		else
			num = k;

		// Tratamento de vari�veis auxiliares
		N = new StringBuilder(num.substring(0, num.length() - 1));
		M = N.reverse().toString();
		S = 0;

		// Verifica��o do tipo de dado de entrada
		try {
			Long.parseLong(N.toString());
		} catch (Exception ex) {
			cont++;
			return false;
		}

		// C�lculo do DV
		for (int i = 1; i < N.length() + 1; i++) {
			m = Integer.parseInt(M.substring(i - 1, i));
			S += m * (i + 1);
		}
		d = 11 - S % 11;
		if (d == 10 || d == 11)
			d = 0;

		// Gera��o de sa�da em caso de n�o valida��o do DV
		if (!Integer.toString(d).equals(num.substring(num.length() - 1, num.length()))) 
			cont++;
		
		if (cont>0)
			return false;
		else
			return true;
	}

	/**
	 * @param k
	 * @param a
	 * @param b
	 * @return
	 */
	public String verificaAGCONTA(int k, int a, int b) {

		// Declara��o de vari�veis
		int m, d, S;
		String num, M;
		StringBuilder aux, N;
		String erro;

		// Tratamento de d�gito X
		aux = new StringBuilder(L.get(k).substring(a - 1, b));
		if (aux.toString().endsWith("X") || aux.toString().endsWith("x"))
			num = L.get(k).substring(a - 1, b - 1) + "0";
		else
			num = L.get(k).substring(a - 1, b);

		// Tratamento de vari�veis auxiliares
		N = new StringBuilder(num.substring(0, num.length() - 1));
		M = N.reverse().toString();
		S = 0;

		// Verifica��o do tipo de dado de entrada
		try {
			Long.parseLong(N.toString());
		} catch (Exception ex) {
			erro = String.format("Posi��es %03d a %03d: Valor esperado - Valores num�ricos; Valor encontrado - %s", a - 2,
					b, N);
		}

		// C�lculo do DV
		for (int i = 1; i < N.length() + 1; i++) {
			m = Integer.parseInt(M.substring(i - 1, i));
			S += m * (i + 1);
		}
		d = 11 - S % 11;
		if (d == 10 || d == 11)
			d = 0;

		// Gera��o de sa�da em caso de n�o valida��o do DV
		if (!Integer.toString(d).equals(num.substring(num.length() - 1, num.length()))) {
			erro = String.format("Posi��es %03d a %03d: Valor esperado - %s; Valor encontrado - %s", a, b,
					num.substring(0, num.length() - 1) + Integer.toString(d), num);
		} else
			erro = null;

		return erro;
	}

	/**
	 * @param k
	 */
	public boolean verificaCNPJ(String k) {
		
		int cont = 0;

		// Verifica��o do tipo de dado de entrada
		try {
			Long.parseLong(k);
		} catch (Exception ex) {
			cont ++;
			return false;
		}

		// Declara��o de vari�veis
		int m, d, S;
		String CNPJ, rev;
		StringBuilder N;
		CNPJ = StringUtils.leftPad(k, 14, "0");

		// Tratamento de vari�veis
		N = new StringBuilder(CNPJ.substring(0, CNPJ.length() - 2));
		rev = N.reverse().toString();
		N.reverse();

		// C�lculo do DV

		for (int i = 0; i < 2; i++) {
			S = 0;
			for (int x = 1; x < 9; x++) {
				m = Integer.parseInt(N.reverse().substring(x - 1, x));
				S += m * (x + 1);
				N.reverse();
			}
			if (N.length() > 8)
				for (int y = 1; y < N.length() - 7; y++) {
					m = Integer.parseInt(rev.substring(y - i + 7, y - i + 8));
					S += m * (y + 1);
				}
			d = 11 - S % 11;
			if (d == 10 || d == 11)
				d = 0;
			N.append(d);
		}

		// Gera��o de sa�da em caso de n�o valida��o do DV
		if (!N.toString().equals(CNPJ)) 
			cont++;
		
		if (cont>0)
			return false;
		else 
			return true;
	}

	/**
	 * @param k
	 * @param a
	 * @param b
	 * @return
	 */
	public String verificaCNPJ(int k, int a, int b) {

		// Declara��o de vari�veis
		int m, d, S;
		String CNPJ, rev;
		StringBuilder N;
		CNPJ = L.get(k).substring(b - 14, b);
		String erro;

		// Tratamento de vari�veis
		N = new StringBuilder(CNPJ.substring(0, CNPJ.length() - 2));
		rev = N.reverse().toString();
		N.reverse();

		// Verifica��o do tipo de dado de entrada
		try {
			Long.parseLong(N.toString());
		} catch (Exception ex) {
			erro = String.format("Posi��es %03d a %03d: Valor esperado - Valores num�ricos; Valor encontrado - %s", b - 13,
					b, CNPJ);
		}

		// C�lculo do DV
		for (int i = 0; i < 2; i++) {
			S = 0;
			for (int x = 1; x < 9; x++) {
				m = Integer.parseInt(N.reverse().substring(x - 1, x));
				S += m * (x + 1);
				N.reverse();
			}
			if (N.length() > 8)
				for (int y = 1; y < N.length() - 7; y++) {
					m = Integer.parseInt(rev.substring(y - i + 7, y - i + 8));
					S += m * (y + 1);
				}
			d = 11 - S % 11;
			if (d == 10 || d == 11)
				d = 0;
			N.append(d);
		}

		// Gera��o de sa�da em caso de n�o valida��o do DV
		if (!N.toString().equals(CNPJ)) {
			erro = String.format("Posi��es %03d a %03d: Valor esperado - %s; Valor encontrado - %s", b - 13, b, N, CNPJ);
		} else
			erro = null;

		return erro;
	}

	/**
	 * @param k
	 */
	public boolean verificaCPF(String k) {
		int cont = 0;

		// Verifica��o do tipo de dado de entrada
		try {
			Long.parseLong(k);
		} catch (Exception ex) {
			cont++;
			return false;
		}

		// Declara��o de vari�veis
		int m, d, S;
		String CPF;
		StringBuilder N;
		CPF = StringUtils.leftPad(k, 11, "0");

		// Tratamento de vari�veis
		N = new StringBuilder(CPF.substring(0, CPF.length() - 2));

		// C�lculo do DV
		S = 0;
		for (int i = 1; i < 10; i++) {
			m = Integer.parseInt(N.toString().substring(i - 1, i));
			S += m * i;
		}
		d = S % 11;
		if (d == 10 || d == 11)
			d = 0;
		N.append(d);
		S = 0;
		for (int i = 0; i < 10; i++) {
			m = Integer.parseInt(N.toString().substring(i, i + 1));
			S += m * i;
		}
		d = S % 11;
		if (d == 10 || d == 11)
			d = 0;
		N.append(d);

		// Gera��o de sa�da em caso de n�o valida��o do DV
		if (!N.toString().equals(CPF))
			cont++;
		
		if (cont >0)
			return false;
		else
			return true;
	}

	/**
	 * @param k
	 * @param a
	 * @param b
	 * @return
	 */
	public String verificaCPF(int k, int a, int b) {

		// Declara��o de vari�veis
		int m, d, S;
		String CPF;
		StringBuilder N;
		CPF = L.get(k).substring(b - 11, b);
		String erro;

		// Verifica��o do tipo de dado de entrada
		try {
			Long.parseLong(CPF);
		} catch (Exception ex) {
			erro = String.format("Posi��es %03d a %03d: Valor esperado - Valores num�ricos; Valor encontrado - %s", b - 10,
					b, CPF);
		}

		// Tratamento de vari�veis
		N = new StringBuilder(CPF.substring(0, CPF.length() - 2));

		// C�lculo do DV
		S = 0;
		for (int i = 1; i < 10; i++) {
			m = Integer.parseInt(N.toString().substring(i - 1, i));
			S += m * i;
		}
		d = S % 11;
		if (d == 10 || d == 11)
			d = 0;
		N.append(d);
		S = 0;
		for (int i = 0; i < 10; i++) {
			m = Integer.parseInt(N.toString().substring(i, i + 1));
			S += m * i;
		}
		d = S % 11;
		if (d == 10 || d == 11)
			d = 0;
		N.append(d);

		// Gera��o de sa�da em caso de n�o valida��o do DV
		if (!N.toString().equals(CPF)) {
			erro = String.format("Posi��es %03d a %03d: Valor esperado - %s; Valor encontrado - %s", b - 10, b, N, CPF);
		} else
			erro = null;

		return erro;
	}
}
