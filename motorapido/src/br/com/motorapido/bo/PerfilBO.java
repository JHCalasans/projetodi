package br.com.motorapido.bo;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import br.com.minhaLib.excecao.excecaonegocio.ExcecaoNegocio;
import br.com.motorapido.dao.IPerfilDAO;
import br.com.motorapido.entity.Perfil;

public class PerfilBO extends MotoRapidoBO {

	private static PerfilBO instance;

	private PerfilBO() {

	}

	public static PerfilBO getInstance() {
		if (instance == null)
			instance = new PerfilBO();

		return instance;
	}

	public List<Perfil> obterPerfisAtivos() throws ExcecaoNegocio {
		EntityManager em = emUtil.getEntityManager();
		EntityTransaction transaction = em.getTransaction();
		try {
			transaction.begin();
			IPerfilDAO perfilDAO = fabricaDAO.getPostgresPerfilDAO();
			Perfil perfil = new Perfil();
			perfil.setAtivo(true);
			List<Perfil> result = perfilDAO.findByExample(perfil, em, perfilDAO.BY_DSC_ASC);
			emUtil.commitTransaction(transaction);
			return result;
		} catch (Exception e) {
			emUtil.rollbackTransaction(transaction);
			throw new ExcecaoNegocio("Falha ao tentar obter perfis ativos.", e);
		} finally {
			emUtil.closeEntityManager(em);
		}
	}

}
