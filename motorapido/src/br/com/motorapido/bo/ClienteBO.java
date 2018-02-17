package br.com.motorapido.bo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import org.hibernate.criterion.MatchMode;

import br.com.minhaLib.excecao.excecaonegocio.ExcecaoNegocio;
import br.com.motorapido.dao.IClienteDAO;
import br.com.motorapido.entity.Cliente;

public class ClienteBO extends MotoRapidoBO {

	private static ClienteBO instance;

	private ClienteBO() {

	}

	public static ClienteBO getInstance() {
		if (instance == null)
			instance = new ClienteBO();

		return instance;
	}

	public List<Cliente> obterClientes(String nome, String cel, Integer codigo) throws ExcecaoNegocio {
		EntityManager em = emUtil.getEntityManager();
		EntityTransaction transaction = em.getTransaction();
		List<Cliente> lista = null;
		try {
			transaction.begin();
			IClienteDAO clienteDAO = fabricaDAO.getPostgresClienteDAO();
			Cliente cliente = new Cliente();
			if (codigo != null){
				cliente.setCodigo(codigo);
				lista = new ArrayList<Cliente>();
				lista.add(clienteDAO.findById(codigo,em));
			}else {
				cliente.setAtivo("S");
				cliente.setNome(nome == "" ? null : nome == " " ? null : nome);
				cliente.setCelular(cel == "" ? null : cel == " " ? null : cel);
				 lista = clienteDAO.findByExample(cliente, MatchMode.ANYWHERE ,em, clienteDAO.BY_NOME_ASC);
			}
			emUtil.commitTransaction(transaction);
			return lista;
		} catch (Exception e) {
			emUtil.rollbackTransaction(transaction);
			throw new ExcecaoNegocio("Falha ao tentar obter clientes.", e);
		} finally {
			emUtil.closeEntityManager(em);
		}
	}

	public Cliente obterClientePorCodigo(Integer codigo) throws ExcecaoNegocio {
		EntityManager em = emUtil.getEntityManager();
		EntityTransaction transaction = em.getTransaction();
		try {
			transaction.begin();
			IClienteDAO clienteDAO = fabricaDAO.getPostgresClienteDAO();
			Cliente cliente = clienteDAO.findById(codigo, em);
			emUtil.commitTransaction(transaction);
			return cliente;
		} catch (Exception e) {
			emUtil.rollbackTransaction(transaction);
			throw new ExcecaoNegocio("Falha ao tentar obter cliente.", e);
		} finally {
			emUtil.closeEntityManager(em);
		}
	}

	public Cliente salvarCliente(Cliente cliente) throws ExcecaoNegocio {
		EntityManager em = emUtil.getEntityManager();
		EntityTransaction transaction = em.getTransaction();
		try {
			transaction.begin();
			IClienteDAO clienteDAO = fabricaDAO.getPostgresClienteDAO();
			cliente.setDataCriacao(new Date());
			cliente.setAtivo("S");
			cliente = clienteDAO.save(cliente, em);

			emUtil.commitTransaction(transaction);
			return cliente;
		} catch (Exception e) {
			emUtil.rollbackTransaction(transaction);
			throw new ExcecaoNegocio("Falha ao tentar gravar cliente.", e);
		} finally {
			emUtil.closeEntityManager(em);
		}
	}

}
