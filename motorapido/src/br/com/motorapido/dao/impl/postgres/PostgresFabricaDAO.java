package br.com.motorapido.dao.impl.postgres;

import org.apache.log4j.Logger;


import br.com.motorapido.dao.FabricaDAO;
import br.com.motorapido.dao.IParametroDAO;
import br.com.motorapido.dao.IValorParametroDAO;

final class PostgresFabricaDAO extends FabricaDAO {

	private static Logger log = Logger.getLogger(PostgresFabricaDAO.class);
	static PostgresFabricaDAO instance;

	static {
		log.info("Instanciando e registrando como f�brica principal.");
		instance = new PostgresFabricaDAO();
		instance.registerAsMain();
	}

	private static PostgresGenericDAOImplBO postgresGenericDAOImplBO;
	private static PostgresFuncionarioDAOImpl postgresFuncionarioDAOImpl;
	private static PostgresPerfilDAOImpl postgresPerfilDAOImpl;
	private static PostgresFuncionarioPerfilDAOImpl postgresFuncionarioPerfilDAOImpl;
	private static PostgresMotoristaDAOImpl postgresMotoristaDAOImpl;
	private static PostgresParametroDAOImpl postgresParametroDAOImpl;
	private static PostgresValorParametroDAOImpl postgresValorParametroDAOImpl;


	private PostgresFabricaDAO() {

	}

	public static void registerMeAsMain() {
		getInstance().registerAsMain();
	}

	protected static PostgresFabricaDAO getInstance() {
		return instance;
	}

	protected static PostgresFabricaDAO getNewInstance() {
		return new PostgresFabricaDAO();
	}

	public PostgresGenericDAOImplBO getGenericDAOImplBO() {
		if (postgresGenericDAOImplBO == null) {
			postgresGenericDAOImplBO = new PostgresGenericDAOImplBO();
		}
		return postgresGenericDAOImplBO;
	}
	
	@Override
	public PostgresFuncionarioDAOImpl getPostgresFuncionarioDAO() {
		if (postgresFuncionarioDAOImpl == null) {
			postgresFuncionarioDAOImpl = new PostgresFuncionarioDAOImpl();
		}
		return postgresFuncionarioDAOImpl;
	}
	
	@Override
	public PostgresPerfilDAOImpl getPostgresPerfilDAO() {
		if (postgresPerfilDAOImpl == null) {
			postgresPerfilDAOImpl = new PostgresPerfilDAOImpl();
		}
		return postgresPerfilDAOImpl;
	}
	
	@Override
	public PostgresFuncionarioPerfilDAOImpl getPostgresFuncionarioPerfilDAO() {
		if (postgresFuncionarioPerfilDAOImpl == null) {
			postgresFuncionarioPerfilDAOImpl = new PostgresFuncionarioPerfilDAOImpl();
		}
		return postgresFuncionarioPerfilDAOImpl;
	}
	
	@Override
	public PostgresMotoristaDAOImpl getPostgresMotoristaDAO() {
		if (postgresMotoristaDAOImpl == null) {
			postgresMotoristaDAOImpl = new PostgresMotoristaDAOImpl();
		}
		return postgresMotoristaDAOImpl;
	}


	@Override
	public IParametroDAO getPostgresParametroDAO() {
		if (postgresParametroDAOImpl == null) {
			postgresParametroDAOImpl = new PostgresParametroDAOImpl();
		}
		return postgresParametroDAOImpl;
	}

	@Override
	public IValorParametroDAO getPostgresValorParametroDAO() {
		if (postgresValorParametroDAOImpl == null) {
			postgresValorParametroDAOImpl = new PostgresValorParametroDAOImpl();
		}
		return postgresValorParametroDAOImpl;
	}


	
}
