package br.com.motorapido.dao.impl.postgres;

import javax.persistence.PersistenceContext;

import br.com.minhaLib.dao.impl.GenericDAOImpl;
import br.com.motorapido.dao.IPerfilDAO;
import br.com.motorapido.entity.Perfil;

@PersistenceContext(unitName = "postgresPU")
public class PostgresPerfilDAOImpl  extends GenericDAOImpl<Perfil, Integer>
implements IPerfilDAO{

	

}
