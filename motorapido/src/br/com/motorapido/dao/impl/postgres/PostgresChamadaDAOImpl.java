package br.com.motorapido.dao.impl.postgres;

import javax.persistence.PersistenceContext;

import br.com.minhaLib.dao.impl.GenericDAOImpl;
import br.com.motorapido.dao.IChamadaDAO;
import br.com.motorapido.entity.Chamada;


@PersistenceContext(unitName = "postgresPU")
public class PostgresChamadaDAOImpl  extends GenericDAOImpl<Chamada, Integer>
implements IChamadaDAO{

	PostgresChamadaDAOImpl() {
		super();
	}

}
