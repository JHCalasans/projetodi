package br.com.motorapido.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import br.com.minhaLib.dao.Entidade;

public class ValorParametro extends Entidade{


	private static final long serialVersionUID = 3703325605944630332L;
	
	public final static String esquema = "projeto";

	public final static String nomeTabela = "valor_parametro";


	@Id
	@Column(name = "cod_valor_parametro", nullable = false)
	private Integer codigo;


	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "cod_parametro", nullable = false)
	private Parametro parametro;


	@Column(name = "vl_parametro", nullable = false)
	private String valor;


	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}


	public Integer getCodigo() {
		return codigo;
	}


	public void setParametro(Parametro parametro) {
		this.parametro = parametro;
	}


	public Parametro getParametro() {
		return parametro;
	}


	public void setValor(String valor) {
		this.valor = valor;
	}


	public String getValor() {
		return valor;
	}

	@Override
	public Integer getIdentificador() {
		return codigo;
	}

	@Override
	public String toString() {
		return "ValorParametro [codigo=" + codigo + ", parametro=" + (parametro != null ? parametro.getChave() : "null")
				+ ", valor=" + valor + "]";
	}

}
