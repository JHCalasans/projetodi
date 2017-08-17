package br.com.motorapido.mbean;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import br.com.minhaLib.excecao.excecaonegocio.ExcecaoNegocio;
import br.com.motorapido.bo.PerfilBO;
import br.com.motorapido.dao.IPerfilDAO;
import br.com.motorapido.entity.Perfil;
import br.com.motorapido.util.ExcecoesUtil;

@ManagedBean(name = "perfilBean")
@ViewScoped
public class PerfilBean extends SimpleController {

	private static final long serialVersionUID = -2110020096388279301L;

	private Perfil perfil;

	private Perfil perfilExclusao;

	private String ativo;

	private String acesso;

	private List<Perfil> listaPerfil;

	@PostConstruct
	public void carregar() {
		try {
			String codPerfilStr = (String) getRequestParam("codPerfil");
			if (codPerfilStr != null) {
				Integer codPerfil = Integer.valueOf(codPerfilStr);
				carregarPerfil(codPerfil);
			} else {
				perfil = new Perfil();
				setListaPerfil(PerfilBO.getInstance().obterPerfis(perfil.getDescricao(), null, null));
			}

		} catch (Exception e) {
			ExcecoesUtil.TratarExcecao(e);
		}
	}

	private void carregarPerfil(Integer codPerfil) {
		try {
			IPerfilDAO perfilDAO = getFabrica().getPostgresPerfilDAO();
			perfil = perfilDAO.findById(codPerfil);
		} catch (Exception e) {
			ExcecoesUtil.TratarExcecao(e);
		}
	}

	public void salvarPerfil() {
		try {
			PerfilBO.getInstance().salvarPerfil(perfil);
			addMsg(FacesMessage.SEVERITY_INFO, "Perfil cadastrado com sucesso.");
			perfil = new Perfil();
		} catch (ExcecaoNegocio e) {
			ExcecoesUtil.TratarExcecao(e);
		}
	}
	
	public void alterarPerfil() {
		try {
			PerfilBO.getInstance().alterarPerfil(perfil);
			addMsg(FacesMessage.SEVERITY_INFO, "Perfil alterado com sucesso.");
		} catch (ExcecaoNegocio e) {
			ExcecoesUtil.TratarExcecao(e);
		}
	}

	public void pesquisarPerfil() {
		try {
			listaPerfil = PerfilBO.getInstance().obterPerfis(perfil.getDescricao(), ativo, acesso);
		} catch (ExcecaoNegocio e) {
			ExcecoesUtil.TratarExcecao(e);
		}
	}

	public String navegarAlteracao(int codPerfil) {
		String url = "alterarPerfil.tjse?faces-redirect=true&codPerfil=" + codPerfil;
		return url;
	}

	public Perfil getPerfil() {
		return perfil;
	}

	public void setPerfil(Perfil perfil) {
		this.perfil = perfil;
	}

	public String getAtivo() {
		return ativo;
	}

	public void setAtivo(String ativo) {
		this.ativo = ativo;
	}

	public String getAcesso() {
		return acesso;
	}

	public void setAcesso(String acesso) {
		this.acesso = acesso;
	}

	public List<Perfil> getListaPerfil() {
		return listaPerfil;
	}

	public void setListaPerfil(List<Perfil> listaPerfil) {
		this.listaPerfil = listaPerfil;
	}

	public Perfil getPerfilExclusao() {
		return perfilExclusao;
	}

	public void setPerfilExclusao(Perfil perfilExclusao) {
		this.perfilExclusao = perfilExclusao;
	}

}
