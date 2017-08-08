package br.com.motorapido.dao;

public abstract class FabricaDAO {

	private static FabricaDAO instance;

	
	public abstract IFuncionarioDAO getPostgresFuncionarioDAO();
	
	public abstract IPerfilDAO getPostgresPerfilDAO();
	
	public abstract IFuncionarioPerfilDAO getPostgresFuncionarioPerfilDAO();
	
	public abstract IMotoristaDAO getPostgresMotoristaDAO();
	
	public abstract IParametroDAO getPostgresParametroDAO();
	
	public abstract IValorParametroDAO getPostgresValorParametroDAO();



	/**
	 * Para utilizar de fato a classe {@link FabricaDAO}, uma implementação
	 * sua deve se registrar como sendo a instância principal usando
	 * {@code instancia.registerAsMain()}.
	 */
	protected final void registerAsMain() {
		FabricaDAO.instance = this;
	}

	public static FabricaDAO getFabricaDAO() {
		return instance;
	}

}
