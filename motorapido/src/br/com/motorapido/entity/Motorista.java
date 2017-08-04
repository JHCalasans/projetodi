package br.com.motorapido.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import br.com.minhaLib.dao.Entidade;

@Entity
@Table(name = Motorista.nomeTabela, schema = Motorista.esquema, catalog = "diego")
public class Motorista extends Entidade{


	private static final long serialVersionUID = 8604601906743979251L;
	
	public final static String esquema = "diego";
	public final static String nomeTabela = "motorista";
	
	@Id
	@Column(name = "cod_motorista", nullable = false)
	@SequenceGenerator(name = "motorista_cod_motorista_seq", sequenceName = "diego.motorista_cod_motorista_seq", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "motorista_cod_motorista_seq")
	private Integer codigo;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cod_funcionario", nullable = false, referencedColumnName = "cod_funcionario")
	private Funcionario funcionario;
	
	@Column(name = "num_cnh", nullable = false)
	private String cnh;
	
	@Column(name = "dt_vencimento_cnh", nullable = false)
	private Date dataVencimentoCNH;
	
	@Column(name = "documentos_criminais", nullable = false)
	private byte[] docCriminais;
	
	@Column(name = "flg_disponivel", nullable = false)
	private boolean disponivel;
	

	@Override
	public Serializable getIdentificador() {
		return getCodigo();
	}


	public Integer getCodigo() {
		return codigo;
	}


	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}


	public Funcionario getFuncionario() {
		return funcionario;
	}


	public void setFuncionario(Funcionario funcionario) {
		this.funcionario = funcionario;
	}


	public String getCnh() {
		return cnh;
	}


	public void setCnh(String cnh) {
		this.cnh = cnh;
	}


	public Date getDataVencimentoCNH() {
		return dataVencimentoCNH;
	}


	public void setDataVencimentoCNH(Date dataVencimentoCNH) {
		this.dataVencimentoCNH = dataVencimentoCNH;
	}


	public byte[] getDocCriminais() {
		return docCriminais;
	}


	public void setDocCriminais(byte[] docCriminais) {
		this.docCriminais = docCriminais;
	}


	public boolean isDisponivel() {
		return disponivel;
	}


	public void setDisponivel(boolean disponivel) {
		this.disponivel = disponivel;
	}

}
