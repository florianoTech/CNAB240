package br.com.cnab240.dao;


import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import br.com.cnab240.entity.Convenente;

public class Teste {

	public static void main(String[] args) throws Exception {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("Convenente");
		EntityManager em = emf.createEntityManager();
		ConvenenteDAO dao = new ConvenenteDAO(em);
		
		Convenente convenente = new Convenente();
		convenente.setTpInsc((short)1);
		convenente.setNrInsc(Long.parseLong("5"));
		
		System.out.println(dao.verificaExistencia(convenente.getTpInsc(), convenente.getNrInsc()));

	}
}
