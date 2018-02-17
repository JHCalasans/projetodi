package br.com.motorapido.mbean;

import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import br.com.minhaLib.excecao.excecaonegocio.ExcecaoNegocio;
import br.com.minhaLib.mbean.AbstractUsuarioLogadoBean;
import br.com.motorapido.bo.ClienteBO;
import br.com.motorapido.entity.Chamada;
import br.com.motorapido.entity.Cliente;
import br.com.motorapido.util.ExcecoesUtil;

@ManagedBean(name = "chamadaBean")
@ViewScoped
public class ChamadaBean extends SimpleController {

	private static final long serialVersionUID = -3771944142798009548L;

	private Chamada chamada;

	private Integer codPesquisa;

	private List<Cliente> listaClientesDialog;
	
	private String nomePesquisa;
	
	private Cliente cliente;

	@Override
	public String salvoSucesso() {
		return null;
	}

	public void pesquisarClienteDialog() {
		try {
			listaClientesDialog = ClienteBO.getInstance().obterClientes(nomePesquisa, null, codPesquisa);
		} catch (ExcecaoNegocio e) {
			ExcecoesUtil.TratarExcecao(e);
		}
	}
	
	public void vincularCliente(Cliente cliente){
		this.cliente = cliente;
	}

	public Integer getCodPesquisa() {
		return codPesquisa;
	}

	public void setCodPesquisa(Integer codPesquisa) {
		this.codPesquisa = codPesquisa;
	}

	public List<Cliente> getListaClientesDialog() {
		return listaClientesDialog;
	}

	public void setListaClientesDialog(List<Cliente> listaClientesDialog) {
		this.listaClientesDialog = listaClientesDialog;
	}
	
	public String getNomePesquisa() {
		return nomePesquisa;
	}

	public void setNomePesquisa(String nomePesquisa) {
		this.nomePesquisa = nomePesquisa;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

}
