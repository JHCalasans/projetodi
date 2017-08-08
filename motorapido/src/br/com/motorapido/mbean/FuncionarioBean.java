package br.com.motorapido.mbean;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
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
import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;

import com.google.gson.Gson;

import br.com.minhaLib.excecao.excecaobanco.ExcecaoBanco;
import br.com.minhaLib.excecao.excecaonegocio.ExcecaoNegocio;
import br.com.minhaLib.util.excecao.MsgUtil;
import br.com.motorapido.bo.FuncionarioBO;
import br.com.motorapido.bo.PerfilBO;
import br.com.motorapido.entity.Funcionario;
import br.com.motorapido.entity.Perfil;
import br.com.motorapido.enums.ParametroEnum;
import br.com.motorapido.util.EnderecoCep;
import br.com.motorapido.util.ExcecoesUtil;
import br.com.motorapido.util.FuncoesUtil;

@ManagedBean(name = "funcionarioBean")
@ViewScoped
public class FuncionarioBean extends SimpleController {

	private static final long serialVersionUID = 4283639145824708144L;

	private Funcionario funcionario;

	private UploadedFile foto;

	private StreamedContent streamFoto;

	private int codPerfil;

	private List<Perfil> listaPerfis;

	private String cnh;

	private Date vencimentoCNH;

	private String cep;

	private UploadedFile docCriminais;

	@PostConstruct
	public void carregar() {
		try {
			funcionario = new Funcionario();
			listaPerfis = PerfilBO.getInstance().obterPerfisAtivos();
			codPerfil = 2;

		} catch (Exception e) {
			ExcecoesUtil.TratarExcecao(e);
		}
	}

	private void limparCampos() {
		funcionario = new Funcionario();
		cnh = null;
		vencimentoCNH = null;
		docCriminais = null;
		foto = null;
		codPerfil = 2;
	}

	public void fileUploadAction(FileUploadEvent event) {
		setFoto(event.getFile());
	}
	
	public void DocCriminaisUploadAction(FileUploadEvent event) {
		setDocCriminais(event.getFile());
	}

	public void carregarFotoExibicao() {
		byte[] fotoExibicao = null;
		if (foto != null) {
			fotoExibicao = foto.getContents();
			streamFoto = new DefaultStreamedContent(new ByteArrayInputStream(fotoExibicao), "image/*");
		}
	}

	public void gerarSenha() {
		int qtdeMaximaCaracteres = 8;
		String[] caracteres = { "a", "1", "b", "2", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f", "g",
				"h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", "A", "B",
				"C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W",
				"X", "Y", "Z" };

		StringBuilder senha = new StringBuilder();

		for (int i = 0; i < qtdeMaximaCaracteres; i++) {
			int posicao = (int) (Math.random() * caracteres.length);
			senha.append(caracteres[posicao]);
		}

		funcionario.setSenha(senha.toString());
	}

	public void salvarFuncionario() {
		try {
			funcionario.setFoto(foto.getContents());
			gerarSenha();
			if (Integer.parseInt(FuncoesUtil.getParam(ParametroEnum.COD_PERFIL_MOTORISTA.getCodigo())) == codPerfil
					&& docCriminais == null)
				addMsg(FacesMessage.SEVERITY_ERROR, "Documentos Criminais não Anexados.");
			else {
				FuncionarioBO.getInstance().salvarFuncionario(funcionario, codPerfil, cnh, vencimentoCNH,
						docCriminais == null ? null : docCriminais.getContents());
				limparCampos();
				addMsg(FacesMessage.SEVERITY_INFO, "Funcionário cadastrado com sucesso.");
			}

		} catch (ExcecaoNegocio | NumberFormatException | ExcecaoBanco e) {
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
				this.getFuncionario().setCep(this.getCep());
				this.getFuncionario().setCidadeResidencia(end.getLocalidade());
				this.getFuncionario().setLogradouro(end.getLogradouro());
				this.getFuncionario().setBairro(end.getBairro());

				switch (end.getUf()) {
				case "RO":
					this.getFuncionario().setEstadoResidencia("Rondônia");
					break;
				case "AC":
					this.getFuncionario().setEstadoResidencia("Acre");
					break;
				case "AM":
					this.getFuncionario().setEstadoResidencia("Amazonas");
					break;
				case "RR":
					this.getFuncionario().setEstadoResidencia("Roraima");
					break;
				case "PA":
					this.getFuncionario().setEstadoResidencia("Pará");
					break;
				case "AP":
					this.getFuncionario().setEstadoResidencia("Amapá");
					break;
				case "TO":
					this.getFuncionario().setEstadoResidencia("Tocantins");
					break;
				case "MA":
					this.getFuncionario().setEstadoResidencia("Maranhão");
					break;
				case "PI":
					this.getFuncionario().setEstadoResidencia("Piauí");
					break;
				case "CE":
					this.getFuncionario().setEstadoResidencia("Ceará");
					break;
				case "RN":
					this.getFuncionario().setEstadoResidencia("Rio Grande do Norte");
					break;
				case "PB":
					this.getFuncionario().setEstadoResidencia("Paraíba");
					break;
				case "PE":
					this.getFuncionario().setEstadoResidencia("Pernambuco");
					break;
				case "AL":
					this.getFuncionario().setEstadoResidencia("Alagoas");
					break;
				case "SE":
					this.getFuncionario().setEstadoResidencia("Sergipe");
					break;
				case "BA":
					this.getFuncionario().setEstadoResidencia("Bahia");
					break;
				case "MG":
					this.getFuncionario().setEstadoResidencia("Minas Gerais");
					break;
				case "ES":
					this.getFuncionario().setEstadoResidencia("Espírito Santo");
					break;
				case "RJ":
					this.getFuncionario().setEstadoResidencia("Rio De Janeiro");
					break;
				case "SP":
					this.getFuncionario().setEstadoResidencia("São Paulo");
					break;
				case "PR":
					this.getFuncionario().setEstadoResidencia("Paraná");
					break;
				case "SC":
					this.getFuncionario().setEstadoResidencia("Santa Catarina");
					break;
				case "RS":
					this.getFuncionario().setEstadoResidencia("Rio Grande Do Sul");
					break;
				case "MS":
					this.getFuncionario().setEstadoResidencia("Mato Grosso Do Sul");
					break;
				case "MT":
					this.getFuncionario().setEstadoResidencia("Mato Grosso");
					break;
				case "GO":
					this.getFuncionario().setEstadoResidencia("Goiás");
					break;
				case "DF":
					this.getFuncionario().setEstadoResidencia("Distrito Federal");
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

	public Funcionario getFuncionario() {
		return funcionario;
	}

	public void setFuncionario(Funcionario funcionario) {
		this.funcionario = funcionario;
	}

	public UploadedFile getFoto() {
		return foto;
	}

	public void setFoto(UploadedFile foto) {
		this.foto = foto;
	}

	public int getCodPerfil() {
		return codPerfil;
	}

	public void setCodPerfil(int codPerfil) {
		this.codPerfil = codPerfil;
	}

	public List<Perfil> getListaPerfis() {
		return listaPerfis;
	}

	public void setListaPerfis(List<Perfil> listaPerfis) {
		this.listaPerfis = listaPerfis;
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

	public UploadedFile getDocCriminais() {
		return docCriminais;
	}

	public void setDocCriminais(UploadedFile docCriminais) {
		this.docCriminais = docCriminais;
	}

	public String getCep() {
		return cep;
	}

	public void setCep(String cep) {
		this.cep = cep;
	}

	public StreamedContent getStreamFoto() {
		carregarFotoExibicao();
		return streamFoto;
	}

	public void setStreamFoto(StreamedContent streamFoto) {
		this.streamFoto = streamFoto;
	}
}
