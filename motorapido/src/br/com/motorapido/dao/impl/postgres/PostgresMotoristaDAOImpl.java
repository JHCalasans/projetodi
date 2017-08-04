package br.com.motorapido.dao.impl.postgres;

import javax.persistence.PersistenceContext;

import br.com.minhaLib.dao.impl.GenericDAOImpl;
import br.com.motorapido.dao.IMotoristaDAO;
import br.com.motorapido.entity.Motorista;

@PersistenceContext(unitName = "postgresPU")
public class PostgresMotoristaDAOImpl  extends GenericDAOImpl<Motorista, Integer>
implements IMotoristaDAO{


}
