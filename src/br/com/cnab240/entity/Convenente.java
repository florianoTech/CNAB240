package br.com.cnab240.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "CONVENENTE")
public class Convenente implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "TPINSC")
	private short tpInsc;
	
	@Id
	@Column(name = "NRINSC")
	private long nrInsc;

	@Column(name = "NOME")
	private String nome;
	
	@Column(name = "BANCO")
	private short banco;
	
	@Column(name = "AG")
	private int ag;
	
	@Column(name = "AG_DV")
	private String agDv;
	
	@Column(name = "CONTA")
	private long conta;
	
	@Column(name = "CONTA_DV")
	private String contaDv;
	
	@Column(name = "CONVENIO")
	private String convenio;
	
	@Column(name = "LOGRADOURO")
	private String logradouro;
	
	@Column(name = "NUMERO")
	private int numero;
	
	@Column(name = "COMPLEMENTO")
	private String complemento;
	
	@Column(name = "CIDADE")
	private String cidade;
	
	@Column(name = "ESTADO")
	private String estado;
	
	@Column(name = "CEP")
	private int cep;
	
	@Column(name = "CEP_COMP")
	private String cepComp;
	
	public short getTpInsc() {
		return tpInsc;
	}
	
	public void setTpInsc(short tpInsc) {
		this.tpInsc = tpInsc;
	}
	
	public long getNrInsc() {
		return nrInsc;
	}
	
	public void setNrInsc(long nrInsc) {
		this.nrInsc = nrInsc;
	}
	
	public String getNome() {
		return nome;
	}
	
	public void setNome(String nome) {
		this.nome = nome;
	}
	
	public short getBanco() {
		return banco;
	}
	
	public void setBanco(short banco) {
		this.banco = banco;
	}
	
	public int getAg() {
		return ag;
	}
	
	public void setAg(int ag) {
		this.ag = ag;
	}
	
	public String getAgDv() {
		return agDv;
	}
	
	public void setAgDv(String agDv) {
		this.agDv = agDv;
	}
	
	public long getConta() {
		return conta;
	}
	
	public void setConta(long conta) {
		this.conta = conta;
	}
	
	public String getContaDv() {
		return contaDv;
	}
	
	public void setContaDv(String contaDv) {
		this.contaDv = contaDv;
	}
	
	public String getConvenio() {
		return convenio;
	}
	
	public void setConvenio(String convenio) {
		this.convenio = convenio;
	}
	
	public String getLogradouro() {
		return logradouro;
	}
	
	public void setLogradouro(String logradouro) {
		this.logradouro = logradouro;
	}
	
	public int getNumero() {
		return numero;
	}
	
	public void setNumero(int numero) {
		this.numero = numero;
	}
	
	public String getComplemento() {
		return complemento;
	}
	
	public void setComplemento(String complemento) {
		this.complemento = complemento;
	}
	
	public String getCidade() {
		return cidade;
	}
	
	public void setCidade(String cidade) {
		this.cidade = cidade;
	}
	
	public String getEstado() {
		return estado;
	}
	
	public void setEstado(String estado) {
		this.estado = estado;
	}
	
	public int getCep() {
		return cep;
	}
	
	public void setCep(int cep) {
		this.cep = cep;
	}
	
	public String getCepComp() {
		return cepComp;
	}
	
	public void setCepComp(String cepComp) {
		this.cepComp = cepComp;
	}
}
