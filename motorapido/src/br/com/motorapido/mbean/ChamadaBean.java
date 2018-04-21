package br.com.motorapido.mbean;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.primefaces.event.TabChangeEvent;

import br.com.minhaLib.excecao.excecaonegocio.ExcecaoNegocio;
import br.com.motorapido.bo.ChamadaBO;
import br.com.motorapido.bo.ClienteBO;
import br.com.motorapido.bo.EnderecoClienteBO;
import br.com.motorapido.bo.LocalBO;
import br.com.motorapido.entity.Chamada;
import br.com.motorapido.entity.Cliente;
import br.com.motorapido.entity.EnderecoCliente;
import br.com.motorapido.entity.Local;
import br.com.motorapido.util.ExcecoesUtil;

@ManagedBean(name = "chamadaBean")
@ViewScoped
public class ChamadaBean extends SimpleController {

	private static final long serialVersionUID = -3771944142798009548L;

	private Chamada chamada;

	private Integer codPesquisa;

	private List<Cliente> listaClientesDialog;

	private String nomePesquisa;

	private String nomeLocalPesquisa;
	
	private String numCelPesquisa;

	private Cliente cliente;

	private EnderecoCliente enderecoClienteOrigem;

	private Local enderecoClienteDestino;

	private List<EnderecoCliente> enderecosDoCliente;

	private List<Local> locais;

	private Boolean isDestino = false;

	private Local localOrigem;
	
	private Boolean destinoFechado = true;

	@PostConstruct
	public void carregar() {
		if (getFacesContext().isPostback()) {
			return;
		}
		try {
			limparCampos();

		} catch (Exception e) {
			ExcecoesUtil.TratarExcecao(e);
		}
	}

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
	
	public void pesquisarClientePorCelular() {
		try {
			cliente = ClienteBO.getInstance().obterClientePorCelular(numCelPesquisa);
			if(cliente == null) 
				 addMsg(FacesMessage.SEVERITY_WARN, "Cliente não encontrado.");
			else
				vincularCliente(cliente);
			
		} catch (ExcecaoNegocio e) {
			ExcecoesUtil.TratarExcecao(e);
		}
	}

	public void adicionarChamada() {
		try {
			ChamadaBO.getInstance().iniciarChamada(getChamada(), enderecoClienteOrigem, enderecoClienteDestino,
					localOrigem, getFuncionarioLogado());
		} catch (ExcecaoNegocio e) {
			ExcecoesUtil.TratarExcecao(e);
		}
	}

	public void limparCampos() {
		cliente = null;
		enderecoClienteDestino = new Local();
		enderecoClienteOrigem = new EnderecoCliente();
		localOrigem = null;
		chamada = new Chamada();
	}

	public void alterarTab(TabChangeEvent event) {
		if (event.getTab().getTitle().equals("EndereÃ§os Do Cliente") && cliente != null && cliente.getCodigo() != null)
			pesquisarEnderecosCliente(cliente);
	}

	public void pesquisarLocalDialog() {
		try {
			locais = LocalBO.getInstance().obterLocal(nomeLocalPesquisa);
		} catch (ExcecaoNegocio e) {
			ExcecoesUtil.TratarExcecao(e);
		}
	}

	public void pesquisarEnderecosCliente(Cliente cliente) {
		try {
			enderecosDoCliente = EnderecoClienteBO.getInstance().obterEnderecosPorCliente(cliente);
			if (enderecosDoCliente != null && enderecosDoCliente.size() > 0)
				setEnderecoClienteOrigem(enderecosDoCliente.get(0));
		} catch (ExcecaoNegocio e) {
			ExcecoesUtil.TratarExcecao(e);
		}
	}

	public void vincularCliente(Cliente cliente) {
		this.cliente = cliente;
		pesquisarEnderecosCliente(cliente);
	}

	public void vincularEnderecoCliente(EnderecoCliente enderecoCliente) {
		setEnderecoClienteOrigem(enderecoCliente);
	}

	public void vincularLocal(Local local) {
		if (isDestino) {
			setEnderecoClienteDestino(local);
			setDestinoFechado(false);
		}else {
			setEnderecoClienteOrigem(converterLocalParaEnderecoCliente(local));
			localOrigem = local;
		}
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

	public List<EnderecoCliente> getEnderecosDoCliente() {
		return enderecosDoCliente;
	}

	public void setEnderecosDoCliente(List<EnderecoCliente> enderecosDoCliente) {
		this.enderecosDoCliente = enderecosDoCliente;
	}

	public EnderecoCliente getEnderecoClienteOrigem() {
		return enderecoClienteOrigem;
	}

	public void setEnderecoClienteOrigem(EnderecoCliente enderecoClienteOrigem) {
		this.enderecoClienteOrigem = enderecoClienteOrigem;
	}

	public Chamada getChamada() {
		return chamada;
	}

	public void setChamada(Chamada chamada) {
		this.chamada = chamada;
	}

	public String getNomeLocalPesquisa() {
		return nomeLocalPesquisa;
	}

	public void setNomeLocalPesquisa(String nomeLocalPesquisa) {
		this.nomeLocalPesquisa = nomeLocalPesquisa;
	}

	public List<Local> getLocais() {
		return locais;
	}

	public void setLocais(List<Local> locais) {
		this.locais = locais;
	}

	private EnderecoCliente converterLocalParaEnderecoCliente(Local local) {
		EnderecoCliente enderecoTemp = new EnderecoCliente();
		enderecoTemp.setBairro(local.getBairro());
		enderecoTemp.setCep(local.getCep());
		enderecoTemp.setCidade(local.getCidade());
		enderecoTemp.setComplemento(local.getComplemento());
		enderecoTemp.setEstado(local.getEstado());
		enderecoTemp.setLogradouro(local.getLogradouro());
		enderecoTemp.setNumero(local.getNumero());
		enderecoTemp.setLatitude(local.getLatitude());
		enderecoTemp.setLongitude(local.getLongitude());
		return enderecoTemp;
	}

	public Local getEnderecoClienteDestino() {
		return enderecoClienteDestino;
	}

	public void setEnderecoClienteDestino(Local enderecoClienteDestino) {
		this.enderecoClienteDestino = enderecoClienteDestino;
	}

	public Boolean getIsDestino() {
		return isDestino;
	}

	public void setIsDestino(Boolean isDestino) {
		this.isDestino = isDestino;
	}

	public Local getLocalOrigem() {
		return localOrigem;
	}

	public void setLocalOrigem(Local localOrigem) {
		this.localOrigem = localOrigem;
	}

	public String getNumCelPesquisa() {
		return numCelPesquisa;
	}

	public void setNumCelPesquisa(String numCelPesquisa) {
		this.numCelPesquisa = numCelPesquisa;
	}

	public Boolean getDestinoFechado() {
		return destinoFechado;
	}

	public void setDestinoFechado(Boolean destinoFechado) {
		this.destinoFechado = destinoFechado;
	}

}
