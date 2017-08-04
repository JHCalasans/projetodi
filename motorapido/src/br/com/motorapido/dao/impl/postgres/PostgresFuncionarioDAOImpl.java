package br.com.motorapido.dao.impl.postgres;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import br.com.minhaLib.dao.impl.GenericDAOImpl;
import br.com.minhaLib.excecao.excecaobanco.ExcecaoBanco;
import br.com.motorapido.dao.IFuncionarioDAO;
import br.com.motorapido.entity.Funcionario;

@PersistenceContext(unitName = "postgresPU")
public class PostgresFuncionarioDAOImpl  extends GenericDAOImpl<Funcionario, Integer>
implements IFuncionarioDAO{

	@Override
	public List<Funcionario> obterporLoginSenha(String login, String senha, EntityManager em) throws ExcecaoBanco {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("login", login.toLowerCase());
		params.put("senha", senha);
		return findByNamedQueryAndNamedParams("Funcionario.obterPorLoginSenha", params, em);
	}

}
