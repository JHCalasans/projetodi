package br.com.motorapido.mbean;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Date;
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
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;

import com.google.gson.Gson;

import br.com.minhaLib.excecao.excecaonegocio.ExcecaoNegocio;
import br.com.minhaLib.util.excecao.MsgUtil;
import br.com.motorapido.bo.FuncionarioBO;
import br.com.motorapido.bo.MotoristaBO;
import br.com.motorapido.entity.Funcionario;
import br.com.motorapido.entity.Motorista;
import br.com.motorapido.util.EnderecoCep;
import br.com.motorapido.util.ExcecoesUtil;
import br.com.motorapido.util.FuncoesUtil;

@ManagedBean(name = "motoristaBean")
@ViewScoped
public class MotoristaBean extends SimpleController{


	private static final long serialVersionUID = -247976915991325625L;
	
	
	private Motorista motorista;

	private UploadedFile foto;

	private StreamedContent streamFoto;

	private String cnh;

	private Date vencimentoCNH;

	private String cep;

	private UploadedFile docCriminais;
	
	private String nomePesquisa;

	private String cpfPesquisa;
	
	private List<Motorista> listaMotoristas;

	@PostConstruct
	public void carregar() {
		try {
			motorista = new Motorista();
			pesquisarMotorista();

		} catch (Exception e) {
			ExcecoesUtil.TratarExcecao(e);
		}
	}
	
	public void pesquisarMotorista() {
		try {
			listaMotoristas = MotoristaBO.getInstance().obterMotoristas(nomePesquisa, cpfPesquisa);
		} catch (ExcecaoNegocio e) {
			ExcecoesUtil.TratarExcecao(e);
		}
	}
	
	public void salvarMotorista() {
		if(docCriminais == null){
			addMsg(FacesMessage.SEVERITY_ERROR, "Documentos Criminais não Anexados.");
			return;
		}
		
		try {
			
			motorista.setFoto(foto.getContents());
			motorista.setSenha(FuncoesUtil.gerarSenha());
			MotoristaBO.getInstance().salvarMotorista(motorista);
			limparCampos();
			addMsg(FacesMessage.SEVERITY_INFO, "Motorista cadastrado com sucesso.");

		} catch (ExcecaoNegocio e) {
			ExcecoesUtil.TratarExcecao(e);
		}
	}
	
	private void limparCampos() {
		motorista = new Motorista();
		cnh = null;
		vencimentoCNH = null;
		docCriminais = null;
		foto = null;
	}
	
	public void fileUploadAction(FileUploadEvent event) {
		setFoto(event.getFile());
	}
	
	public void DocCriminaisUploadAction(FileUploadEvent event) {
		setDocCriminais(event.getFile());
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
				this.getMotorista().setCep(this.getCep());
				this.getMotorista().setCidadeResidencia(end.getLocalidade());
				this.getMotorista().setLogradouro(end.getLogradouro());
				this.getMotorista().setBairro(end.getBairro());

				switch (end.getUf()) {
				case "RO":
					this.getMotorista().setEstadoResidencia("Rondônia");
					break;
				case "AC":
					this.getMotorista().setEstadoResidencia("Acre");
					break;
				case "AM":
					this.getMotorista().setEstadoResidencia("Amazonas");
					break;
				case "RR":
					this.getMotorista().setEstadoResidencia("Roraima");
					break;
				case "PA":
					this.getMotorista().setEstadoResidencia("Pará");
					break;
				case "AP":
					this.getMotorista().setEstadoResidencia("Amapá");
					break;
				case "TO":
					this.getMotorista().setEstadoResidencia("Tocantins");
					break;
				case "MA":
					this.getMotorista().setEstadoResidencia("Maranhão");
					break;
				case "PI":
					this.getMotorista().setEstadoResidencia("Piauí");
					break;
				case "CE":
					this.getMotorista().setEstadoResidencia("Ceará");
					break;
				case "RN":
					this.getMotorista().setEstadoResidencia("Rio Grande do Norte");
					break;
				case "PB":
					this.getMotorista().setEstadoResidencia("Paraíba");
					break;
				case "PE":
					this.getMotorista().setEstadoResidencia("Pernambuco");
					break;
				case "AL":
					this.getMotorista().setEstadoResidencia("Alagoas");
					break;
				case "SE":
					this.getMotorista().setEstadoResidencia("Sergipe");
					break;
				case "BA":
					this.getMotorista().setEstadoResidencia("Bahia");
					break;
				case "MG":
					this.getMotorista().setEstadoResidencia("Minas Gerais");
					break;
				case "ES":
					this.getMotorista().setEstadoResidencia("Espírito Santo");
					break;
				case "RJ":
					this.getMotorista().setEstadoResidencia("Rio De Janeiro");
					break;
				case "SP":
					this.getMotorista().setEstadoResidencia("São Paulo");
					break;
				case "PR":
					this.getMotorista().setEstadoResidencia("Paraná");
					break;
				case "SC":
					this.getMotorista().setEstadoResidencia("Santa Catarina");
					break;
				case "RS":
					this.getMotorista().setEstadoResidencia("Rio Grande Do Sul");
					break;
				case "MS":
					this.getMotorista().setEstadoResidencia("Mato Grosso Do Sul");
					break;
				case "MT":
					this.getMotorista().setEstadoResidencia("Mato Grosso");
					break;
				case "GO":
					this.getMotorista().setEstadoResidencia("Goiás");
					break;
				case "DF":
					this.getMotorista().setEstadoResidencia("Distrito Federal");
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

	public Motorista getMotorista() {
		return motorista;
	}

	public void setMotorista(Motorista motorista) {
		this.motorista = motorista;
	}

	public UploadedFile getFoto() {
		return foto;
	}

	public void setFoto(UploadedFile foto) {
		this.foto = foto;
	}

	public StreamedContent getStreamFoto() {
		return streamFoto;
	}

	public void setStreamFoto(StreamedContent streamFoto) {
		this.streamFoto = streamFoto;
	}

	public String getCnh() {
		return cnh;
	}

	public void setCnh(String cnh) {
		this.cnh = cnh;
	}

	public Date getVencimentoCNH() {
		return vencimentoCNH;
	}

	public void setVencimentoCNH(Date vencimentoCNH) {
		this.vencimentoCNH = vencimentoCNH;
	}

	public String getCep() {
		return cep;
	}

	public void setCep(String cep) {
		this.cep = cep;
	}

	public UploadedFile getDocCriminais() {
		return docCriminais;
	}

	public void setDocCriminais(UploadedFile docCriminais) {
		this.docCriminais = docCriminais;
	}

	@Override
	public String salvoSucesso() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getNomePesquisa() {
		return nomePesquisa;
	}

	public void setNomePesquisa(String nomePesquisa) {
		this.nomePesquisa = nomePesquisa;
	}

	public String getCpfPesquisa() {
		return cpfPesquisa;
	}

	public void setCpfPesquisa(String cpfPesquisa) {
		this.cpfPesquisa = cpfPesquisa;
	}

	public List<Motorista> getListaMotoristas() {
		return listaMotoristas;
	}

	public void setListaMotoristas(List<Motorista> listaMotoristas) {
		this.listaMotoristas = listaMotoristas;
	}

}
