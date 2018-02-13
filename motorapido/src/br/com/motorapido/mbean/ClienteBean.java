package br.com.motorapido.mbean;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.impl.client.HttpClients;

import com.google.gson.Gson;

import br.com.minhaLib.excecao.excecaonegocio.ExcecaoNegocio;
import br.com.minhaLib.util.excecao.MsgUtil;
import br.com.motorapido.bo.ClienteBO;
import br.com.motorapido.bo.FuncionarioBO;
import br.com.motorapido.entity.Cliente;
import br.com.motorapido.entity.Funcionario;
import br.com.motorapido.util.EnderecoCep;
import br.com.motorapido.util.ExcecoesUtil;

@ManagedBean(name = "clienteBean")
@ViewScoped
public class ClienteBean extends SimpleController {

	private static final long serialVersionUID = 443599057678219606L;

	private Cliente cliente;

	private String cep;

	private String nomePesquisa;

	private String celPesquisa;

	private List<Cliente> listaClientes;

	@PostConstruct
	public void carregar() {
		if (getFacesContext().isPostback()) {
			return;
		}
		try {
			String codClienteStr = (String) getRequestParam("codCliente");
			String consultar = (String) getRequestParam("consultaParam");
			if (codClienteStr != null) {
				Integer codCliente = Integer.valueOf(codClienteStr);
				carregarCliente(codCliente);
				cep = cliente.getCep();
			} else if (consultar != null && (consultar.equals("true") || consultar.equals("true?"))) {
				pesquisarCliente();

			} else {
				cliente = new Cliente();
			}
		} catch (Exception e) {
			ExcecoesUtil.TratarExcecao(e);
		}
	}

	private void carregarCliente(Integer codigo) {
		try {

			cliente = ClienteBO.getInstance().obterClientePorCodigo(codigo);
		} catch (Exception e) {
			ExcecoesUtil.TratarExcecao(e);
		}
	}

	public void validarCep() {
		if (this.getCep() != null && this.getCep().replace("-", "").replace("_", "").length() == 8) {
			try {

				HttpClient httpClient = HttpClients.custom().build();

				HttpUriRequest request = RequestBuilder.get()
						.setUri("http://www.viacep.com.br/ws/" + this.getCep().replace("-", "") + "/json/")
						.setHeader("accept", "application/json").build();

				HttpResponse response = httpClient.execute(request);

				if (response.getStatusLine().getStatusCode() != 200) {
					throw new RuntimeException(
							"Failed : HTTP error code : " + response.getStatusLine().getStatusCode());
				}

				BufferedReader br = new BufferedReader(new InputStreamReader((response.getEntity().getContent())));
				String output;
				String json = "";
				while ((output = br.readLine()) != null) {
					json += output + "\n";
				}

				Gson gson = new Gson();
				EnderecoCep end = gson.fromJson(json, EnderecoCep.class);
				this.getCliente().setCep(this.getCep());
				this.getCliente().setCidadeResidencia(end.getLocalidade());
				this.getCliente().setLogradouro(end.getLogradouro());
				this.getCliente().setBairro(end.getBairro());

				switch (end.getUf()) {
				case "RO":
					this.getCliente().setEstadoResidencia("Rondônia");
					break;
				case "AC":
					this.getCliente().setEstadoResidencia("Acre");
					break;
				case "AM":
					this.getCliente().setEstadoResidencia("Amazonas");
					break;
				case "RR":
					this.getCliente().setEstadoResidencia("Roraima");
					break;
				case "PA":
					this.getCliente().setEstadoResidencia("Pará");
					break;
				case "AP":
					this.getCliente().setEstadoResidencia("Amapá");
					break;
				case "TO":
					this.getCliente().setEstadoResidencia("Tocantins");
					break;
				case "MA":
					this.getCliente().setEstadoResidencia("Maranhão");
					break;
				case "PI":
					this.getCliente().setEstadoResidencia("Piauí");
					break;
				case "CE":
					this.getCliente().setEstadoResidencia("Ceará");
					break;
				case "RN":
					this.getCliente().setEstadoResidencia("Rio Grande do Norte");
					break;
				case "PB":
					this.getCliente().setEstadoResidencia("Paraíba");
					break;
				case "PE":
					this.getCliente().setEstadoResidencia("Pernambuco");
					break;
				case "AL":
					this.getCliente().setEstadoResidencia("Alagoas");
					break;
				case "SE":
					this.getCliente().setEstadoResidencia("Sergipe");
					break;
				case "BA":
					this.getCliente().setEstadoResidencia("Bahia");
					break;
				case "MG":
					this.getCliente().setEstadoResidencia("Minas Gerais");
					break;
				case "ES":
					this.getCliente().setEstadoResidencia("Espírito Santo");
					break;
				case "RJ":
					this.getCliente().setEstadoResidencia("Rio De Janeiro");
					break;
				case "SP":
					this.getCliente().setEstadoResidencia("São Paulo");
					break;
				case "PR":
					this.getCliente().setEstadoResidencia("Paraná");
					break;
				case "SC":
					this.getCliente().setEstadoResidencia("Santa Catarina");
					break;
				case "RS":
					this.getCliente().setEstadoResidencia("Rio Grande Do Sul");
					break;
				case "MS":
					this.getCliente().setEstadoResidencia("Mato Grosso Do Sul");
					break;
				case "MT":
					this.getCliente().setEstadoResidencia("Mato Grosso");
					break;
				case "GO":
					this.getCliente().setEstadoResidencia("Goiás");
					break;
				case "DF":
					this.getCliente().setEstadoResidencia("Distrito Federal");
					break;
				}

			} catch (Exception e) {
				e.printStackTrace();
				MsgUtil.updateMessage(FacesMessage.SEVERITY_ERROR, "Não foi possível buscar informações do CEP!.", "");
			}
		} else {
			MsgUtil.updateMessage(FacesMessage.SEVERITY_ERROR, "CEP inválido!.", "");
		}

	}

	public boolean validarEmail() {

		try {
			Funcionario funcio = new Funcionario();
			funcio.setEmail(cliente.getEmail());
			List<Funcionario> lista = FuncionarioBO.getInstance().obterFuncionariosExample(funcio);
			if (lista != null && lista.size() > 0
					&& (cliente.getCodigo() == null || cliente.getCodigo() != lista.get(0).getCodigo())) {
				MsgUtil.updateMessage(FacesMessage.SEVERITY_ERROR, "Email já cadastrado na base de dados!.", "");
				return false;
			}
			return true;
		} catch (ExcecaoNegocio e) {
			ExcecoesUtil.TratarExcecao(e);
			return false;
		}

	}

	public void pesquisarCliente() {
		try {
			listaClientes = ClienteBO.getInstance().obterClientes(nomePesquisa, celPesquisa);
		} catch (ExcecaoNegocio e) {
			ExcecoesUtil.TratarExcecao(e);
		}
	}

	public String navegarAlteracao(int codCliente) {
		String url = "alterarCliente.proj?faces-redirect=true&codCliente=" + codCliente;
		return url;
	}

	public void salvarCliente() {

		if (!validarEmail())
			return;
		try {
			ClienteBO.getInstance().salvarCliente(cliente);
			addMsg(FacesMessage.SEVERITY_INFO, "Cliente cadastrado com sucesso.");

		} catch (ExcecaoNegocio e) {
			ExcecoesUtil.TratarExcecao(e);
		}
	}

	@Override
	public String salvoSucesso() {
		return null;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public String getCep() {
		return cep;
	}

	public void setCep(String cep) {
		this.cep = cep;
	}

	public String getNomePesquisa() {
		return nomePesquisa;
	}

	public void setNomePesquisa(String nomePesquisa) {
		this.nomePesquisa = nomePesquisa;
	}

	public String getCelPesquisa() {
		return celPesquisa;
	}

	public void setCelPesquisa(String celPesquisa) {
		this.celPesquisa = celPesquisa;
	}

	public List<Cliente> getListaClientes() {
		return listaClientes;
	}

	public void setListaClientes(List<Cliente> listaClientes) {
		this.listaClientes = listaClientes;
	}

}
