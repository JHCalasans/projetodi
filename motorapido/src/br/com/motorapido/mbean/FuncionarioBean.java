package br.com.motorapido.mbean;

import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.primefaces.model.UploadedFile;

import br.com.minhaLib.excecao.excecaonegocio.ExcecaoNegocio;
import br.com.minhaLib.util.excecao.MsgUtil;
import br.com.motorapido.bo.FuncionarioBO;
import br.com.motorapido.bo.PerfilBO;
import br.com.motorapido.entity.Funcionario;
import br.com.motorapido.entity.Perfil;
import br.com.motorapido.util.ExcecoesUtil;

@ManagedBean(name="funcionarioBean")
@ViewScoped
public class FuncionarioBean extends SimpleController{

	private static final long serialVersionUID = 4283639145824708144L;

	private Funcionario funcionario;
	
	private UploadedFile foto;
	
	private int codPerfil;
	
	private List<Perfil> listaPerfis;
	
	private String cnh;
	
	private Date vencimentoCNH;
	
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
	
	private void limparCampos(){
		funcionario = new Funcionario();
		cnh = null;
		vencimentoCNH = null;
		docCriminais = null;
		foto = null;
		codPerfil = 2;
	}
	
	public void gerarSenha() {
		int qtdeMaximaCaracteres = 8;
		String[] caracteres = { "a", "1", "b", "2", "4", "5", "6", "7", "8", "9", "a", "b", "c",
				"d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t",
				"u", "v", "w", "x", "y", "z", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K",
				"L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};

		StringBuilder senha = new StringBuilder();

		for (int i = 0; i < qtdeMaximaCaracteres; i++) {
			int posicao = (int) (Math.random() * caracteres.length);
			senha.append(caracteres[posicao]);
		}
		
		funcionario.setSenha(senha.toString());
	}
	
	public void salvarFuncionario(){
		try {
			funcionario.setFoto(foto.getContents());
			FuncionarioBO.getInstance().salvarFuncionario(funcionario, codPerfil, cnh, vencimentoCNH, docCriminais.getContents());
			addMsg(FacesMessage.SEVERITY_ERROR, "Funcionário cadastrado com sucesso.");
			limparCampos();
		} catch (ExcecaoNegocio e) {
			ExcecoesUtil.TratarExcecao(e);
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
}
