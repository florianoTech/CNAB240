package br.com.cnab240.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import br.com.cnab240.entity.Convenente;

public class ConvenenteDAO {
	private EntityManager em;

	public ConvenenteDAO(EntityManager em) {
		this.em = em;
	}

	public void incluir(Convenente convenente) throws Exception {
		try {
			em.getTransaction().begin();
			em.persist(convenente);
			em.getTransaction().commit();
		} catch (Exception e) {
			e.getMessage();
		}
	}

	public void alterar(Convenente convenente) {
		try {
			em.getTransaction().begin();
			em.merge(convenente);
			em.getTransaction().commit();
		} catch (Exception e) {
			e.getMessage();
		}
	}

	public void remover(Convenente convenente) {
		try {
			em.getTransaction().begin();
			convenente = em.find(Convenente.class, convenente);
			em.remove(convenente);
			em.getTransaction().commit();
		} catch (Exception e) {
			e.getMessage();
		}
	}

	@SuppressWarnings("unchecked")
	public List<Convenente> listarConvenentes() throws Exception {

		List<Convenente> lista = new ArrayList<Convenente>();
		try {
			em.getTransaction().begin();
			Query query = em.createQuery("SELECT c FROM Convenente as c");
			lista = query.getResultList();
			em.getTransaction().commit();
			return lista;
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}

	@SuppressWarnings("unchecked")
	public ArrayList<String> listarLabel() throws Exception {
		ArrayList<String> listaLabel = new ArrayList<String>();
		List<Convenente> lista = new ArrayList<Convenente>();
		try {
			em.getTransaction().begin();
			Query query = em.createQuery("SELECT c FROM Convenente as c");
			lista = query.getResultList();
			em.getTransaction().commit();

			for (int i = 0; i < lista.size(); i++) {
				listaLabel.add(lista.get(i).getNrInsc() + " " + lista.get(i).getNome());
			}
			return listaLabel;
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}

	@SuppressWarnings("unchecked")
	public boolean verificaExistencia(short tpInsc, long nrInsc) throws Exception {
		List<Convenente> lista = new ArrayList<Convenente>();
		try {
			em.getTransaction().begin();
			Query query = em.createQuery(
					"SELECT c " + "FROM Convenente as c " + "WHERE c.tpInsc =: tpInsc " + "AND c.nrInsc =: nrInsc");
			query.setParameter("tpInsc", tpInsc);
			query.setParameter("nrInsc", nrInsc);
			lista = query.getResultList();
			em.getTransaction().commit();
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
		if (lista.size() == 0)
			return false;
		else
			return true;
	}
}