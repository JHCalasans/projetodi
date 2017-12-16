package br.com.motorapido.bo;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import br.com.minhaLib.excecao.excecaonegocio.ExcecaoNegocio;
import br.com.motorapido.dao.IAreaDAO;
import br.com.motorapido.dao.ICoordenadasAreaDAO;
import br.com.motorapido.dao.IVeiculoDAO;
import br.com.motorapido.entity.Area;
import br.com.motorapido.entity.CoordenadasArea;
import br.com.motorapido.entity.Motorista;
import br.com.motorapido.entity.Veiculo;

public class VeiculoBO extends MotoRapidoBO {
	
	private static VeiculoBO instance;

	private VeiculoBO() {

	}

	public static VeiculoBO getInstance() {
		if (instance == null)
			instance = new VeiculoBO();

		return instance;
	}
	
	public Veiculo salvarVeiculo(Veiculo veiculo) throws ExcecaoNegocio {
		EntityManager em = emUtil.getEntityManager();
		EntityTransaction transaction = em.getTransaction();
		try {
			transaction.begin();
			IVeiculoDAO veiculoDAO = fabricaDAO.getPostgresVeiculoDAO();
			veiculo.setFlgAtivo("S");
			veiculo.setDataCadastro(new Date());
			veiculo = veiculoDAO.save(veiculo, em);
			emUtil.commitTransaction(transaction);
			return veiculo;
		} catch (Exception e) {
			emUtil.rollbackTransaction(transaction);
			throw new ExcecaoNegocio("Falha ao tentar gravar veículo.", e);
		} finally {
			emUtil.closeEntityManager(em);
		}
	}
	
	public List<Veiculo> obterVeiculosPorMotorista(Integer codMotorista) throws ExcecaoNegocio {
		EntityManager em = emUtil.getEntityManager();
		EntityTransaction transaction = em.getTransaction();
		List<Veiculo> retorno = null;
		try {
			transaction.begin();
			IVeiculoDAO veiculoDAO = fabricaDAO.getPostgresVeiculoDAO();	
			retorno = veiculoDAO.obterVeiculosPorMotorista(codMotorista, em);
			emUtil.commitTransaction(transaction);			
		} catch (Exception e) {
			emUtil.rollbackTransaction(transaction);
			throw new ExcecaoNegocio("Falha ao tentar obter veículos.", e);
		} finally {
			emUtil.closeEntityManager(em);
		}
		return retorno;
	}
	
	public List<Veiculo> obterVeiculosByExample(Veiculo veiculo) throws ExcecaoNegocio {
		EntityManager em = emUtil.getEntityManager();
		EntityTransaction transaction = em.getTransaction();
		List<Veiculo> retorno = null;
		try {
			transaction.begin();
			IVeiculoDAO veiculoDAO = fabricaDAO.getPostgresVeiculoDAO();	
			retorno = veiculoDAO.findByExample(veiculo, em);
			emUtil.commitTransaction(transaction);			
		} catch (Exception e) {
			emUtil.rollbackTransaction(transaction);
			throw new ExcecaoNegocio("Falha ao tentar obter veículos.", e);
		} finally {
			emUtil.closeEntityManager(em);
		}
		return retorno;
	}
	
	public void excluirVeiculo(Veiculo veiculo) throws ExcecaoNegocio {
		EntityManager em = emUtil.getEntityManager();
		EntityTransaction transaction = em.getTransaction();
		try {
			transaction.begin();
			IVeiculoDAO veiculoDAO = fabricaDAO.getPostgresVeiculoDAO();	
			veiculo.setFlgAtivo("N");
			veiculo.setDataDesativacao(new Date());
			veiculoDAO.save(veiculo, em);
			emUtil.commitTransaction(transaction);
			
		} catch (Exception e) {
			emUtil.rollbackTransaction(transaction);
			throw new ExcecaoNegocio("Falha ao tentar remover veículo.", e);
		} finally {
			emUtil.closeEntityManager(em);
		}

	}
	

}
