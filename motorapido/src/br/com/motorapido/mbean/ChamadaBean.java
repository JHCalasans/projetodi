package br.com.motorapido.mbean;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import br.com.motorapido.entity.Chamada;

@ManagedBean(name = "chamadaBean")
@ViewScoped
public class ChamadaBean extends SimpleController {
	

	private static final long serialVersionUID = -3771944142798009548L;
	
	private Chamada chamada;
	


	@Override
	public String salvoSucesso() {
		return null;
	}

}
