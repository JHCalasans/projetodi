package br.com.motorapido.dao.impl.postgres;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import br.com.minhaLib.dao.impl.GenericDAOImpl;
import br.com.minhaLib.excecao.excecaobanco.ExcecaoBanco;
import br.com.motorapido.dao.IVeiculoDAO;
import br.com.motorapido.entity.Veiculo;

@PersistenceContext(unitName = "postgresPU")
public class PostgresVeiculoDAOImpl extends GenericDAOImpl<Veiculo, Integer>
implements IVeiculoDAO{

	@Override
	public List<Veiculo> obterVeiculosPorMotorista(Integer codMotorista, EntityManager em) throws ExcecaoBanco {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("codMotorista", codMotorista);
		return findByNamedQueryAndNamedParams("Veiculo.obterVeiculosPorMotorista", params, em);
		
	}

}
