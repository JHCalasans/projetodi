package br.com.motorapido.bo;


import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import br.com.minhaLib.excecao.excecaobanco.ExcecaoBanco;
import br.com.minhaLib.excecao.excecaonegocio.ExcecaoNegocio;
import br.com.motorapido.dao.IFuncionarioDAO;
import br.com.motorapido.dao.IFuncionarioPerfilDAO;
import br.com.motorapido.dao.IMotoristaDAO;
import br.com.motorapido.dao.IPerfilDAO;
import br.com.motorapido.entity.Funcionario;
import br.com.motorapido.entity.FuncionarioPerfil;
import br.com.motorapido.entity.Motorista;
import br.com.motorapido.util.FuncoesUtil;

public class FuncionarioBO extends MotoRapidoBO {
	
	private static FuncionarioBO instance;

	private FuncionarioBO() {

	}

	public static FuncionarioBO getInstance() {
		if (instance == null)
			instance = new FuncionarioBO();

		return instance;
	}
	
	public Funcionario salvarFuncionario(Funcionario funcionario, int codPerfil) throws ExcecaoNegocio {
		EntityManager em = emUtil.getEntityManager();
		EntityTransaction transaction = em.getTransaction();
		try {
			transaction.begin();
			IFuncionarioDAO funcionarioDAO = fabricaDAO.getPostgresFuncionarioDAO();
			IPerfilDAO perfilDAO = fabricaDAO.getPostgresPerfilDAO();
			IFuncionarioPerfilDAO funcionarioPerfilDAO = fabricaDAO.getPostgresFuncionarioPerfilDAO();
			funcionario.setDataCriacao(new Date());
			funcionario.setSenha(FuncoesUtil.criptografarSenha(funcionario.getSenha()));
			funcionario = funcionarioDAO.save(funcionario, em);
			FuncionarioPerfil funcPerfil = new FuncionarioPerfil();
			funcPerfil.setFuncionario(funcionario);
			funcPerfil.setAtivo(true);
			funcPerfil.setDataCriacao(new Date ());
			funcPerfil.setPerfil(perfilDAO.findById(codPerfil, em));
			funcPerfil = funcionarioPerfilDAO.save(funcPerfil, em);		
			
			emUtil.commitTransaction(transaction);
			return funcionario;
		}catch (Exception e) {
			emUtil.rollbackTransaction(transaction);
			throw new ExcecaoNegocio("Falha ao tentar gravar funcionário.", e);
		} finally {
			emUtil.closeEntityManager(em);
		}
	}
	
	public Funcionario obterFuncionarioPorLoginSenha(String login, String senha) throws ExcecaoNegocio {
		EntityManager em = emUtil.getEntityManager();
		EntityTransaction transaction = em.getTransaction();
		Funcionario result = null;
		try {
			transaction.begin();
			IFuncionarioDAO funcionarioDAO = fabricaDAO.getPostgresFuncionarioDAO();

			List<Funcionario> lista = funcionarioDAO.obterporLoginSenha(login, FuncoesUtil.criptografarSenha(senha), em);
			if (lista != null && lista.size() > 0)
				result = lista.get(0);

			emUtil.commitTransaction(transaction);
			return result;
		} catch (ExcecaoBanco e) {
			emUtil.rollbackTransaction(transaction);
			throw new ExcecaoNegocio("Falha ao tentar obter funcionário pelo Login.", e);
		} catch (Exception e) {
			emUtil.rollbackTransaction(transaction);
			throw new ExcecaoNegocio("Falha ao tentar criptografar senha.", e);
		}		finally {
			emUtil.closeEntityManager(em);
		}
	}
	
	

}
