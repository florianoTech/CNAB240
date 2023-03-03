package br.com.cnab240.programa;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import org.apache.commons.lang3.StringUtils;

import br.com.cnab240.core.ArquivoCore;
import br.com.cnab240.core.ConteudoCore;
import br.com.cnab240.core.ConvenenteCore;
import br.com.cnab240.entity.Convenente;

/**
 * @author Daniel Floriano Classe a ser utilizada para cria��o de GUI
 */
public class Teste {
	public static void main(String[] args) throws IOException {

		// Definindo vari�veis para arquivos de entrada e sa�da
		String caminhoEntrada = "C:\\Users\\cejw2\\Desktop\\CNAB240.txt";
		String caminhoSaida = "C:\\Users\\cejw2\\Desktop";
		String nomeArquivoSaida = "Resultado da An�lise.txt";
		long contErros = 0;
		long cnpj = Long.parseLong("08996956000105");
		String nome = "DAIDO IND CORR DA AMAZONIA";
		short banco = 001;
		int ag = 3358;
		String agDv = "8";
		long conta = 5349;
		String contaDv = "x";
		String convenio = "943411";
		String logradouro = "ABC";
		int numero = 123456;
		String complemento = "";
		String cidade = "RIO DE JANEIRO";
		int cep = 99999;
		String cepComp = "999";
		String estado= "RJ";
		
		// Instanciando objetos
		ArquivoCore arquivo = new ArquivoCore(caminhoEntrada, caminhoSaida, nomeArquivoSaida, contErros);
		ArrayList<String> L = arquivo.leArquivoEntrada();
		
		ConvenenteCore convenenteCore= new ConvenenteCore(L);

		Convenente convenente = new Convenente();
		convenente.setNrInsc(cnpj);
		convenente.setNome(nome);
		convenente.setBanco(banco);
		convenente.setAg(ag);
		convenente.setAgDv(agDv);
		convenente.setConta(conta);
		convenente.setContaDv(contaDv);
		convenente.setConvenio(convenio);
		convenente.setLogradouro(logradouro);
		convenente.setNumero(numero);
		convenente.setComplemento(complemento);
		convenente.setCidade(cidade);
		convenente.setEstado(estado);
		convenente.setCep(cep);
		convenente.setCepComp(cepComp);
		ConteudoCore conteudo = new ConteudoCore(L, contErros, convenente, convenenteCore);

		// Criando arquivo de sa�da
		PrintWriter gravarArq = arquivo.criaArquivoSaida();
		
		// In�cio da verifica��o de estrutura
		gravarArq.println(StringUtils.repeat("*", 46) + " Verficica��o de estrutura " + StringUtils.repeat("*", 46) + "\n");

		// Verifica��o de estrutura
		ArrayList<String> verificacaoEstrutural = arquivo.verificaEstrutura();

		for (int i = 0; i < verificacaoEstrutural.size(); i++)
			gravarArq.println(verificacaoEstrutural.get(i));

		// pulando uma linha para in�cio de verifica��o de conte�do
		gravarArq.println("\n" + StringUtils.repeat("*", 48) + " Verfica��o de conte�do " + StringUtils.repeat("*", 47));

		// Verifica��o de conte�do
		ArrayList<String> verificacaoConteudo = conteudo.cnab240();
		for (int i = 0; i < verificacaoConteudo.size(); i++)
			if (verificacaoConteudo.get(i) != null)
				gravarArq.println(verificacaoConteudo.get(i));

		gravarArq.println("\n" + StringUtils.repeat("*", 119));
		gravarArq.println("\nArquivo 'Resultado da an�lise' gerado.");  
		gravarArq.println("\nErros de estrutura: " + arquivo.getContErros() + "\nErros de conte�do : " + conteudo.getContErros());
		gravarArq.close();
	}
}
