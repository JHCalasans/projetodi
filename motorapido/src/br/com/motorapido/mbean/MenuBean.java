package br.com.motorapido.mbean;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.primefaces.model.menu.DefaultMenuItem;
import org.primefaces.model.menu.DefaultMenuModel;
import org.primefaces.model.menu.MenuItem;
import org.primefaces.model.menu.MenuModel;

import br.com.motorapido.bo.PerfilMenuBO;
import br.com.motorapido.entity.PerfilMenu;
import br.com.motorapido.util.ExcecoesUtil;



@ManagedBean(name = "menuBean")
@ViewScoped
public class MenuBean extends SimpleController implements Serializable {


	private static final long serialVersionUID = -2168554630566444675L;
	private MenuModel model;

	public MenuBean() {

	}
	
	public MenuModel getModel() {
		try {
			if (model == null) {
				model = new DefaultMenuModel();
				//Funcionario funcionario =  getFuncionarioLogado();				
				List<PerfilMenu> perfisMenus = PerfilMenuBO.getInstance().obterMenusPorPerfil(getFuncionarioLogado().getPerfil().getCodigo());
				MenuItem menuItem = null;
				for(PerfilMenu perfilMenu: perfisMenus){
					menuItem =  new DefaultMenuItem(perfilMenu.getMenu().getNome(), null, perfilMenu.getMenu().getUrl());
					getModel().addElement(menuItem);
				}
				
				
			}
		} catch (Exception ex) {
			ExcecoesUtil.TratarExcecao(ex);
			model = null;
		}
		return model;
	}
	
	public void setModel(MenuModel model) {
		this.model = model;
	}

	@PostConstruct
	public void init() {
		
	}

	@Override
	public String salvoSucesso() {
		// TODO Auto-generated method stub
		return null;
	}

}
