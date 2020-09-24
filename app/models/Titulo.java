
package models;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import play.db.jpa.GenericModel;


@Entity
@Table(name = "titulo")
public class Titulo extends GenericModel{

	@Id
	@GeneratedValue
	public Integer tit_codigo;
	public Integer emp_codigo;
	public Integer ped_codigo;
	public Integer car_codigo;
	public Integer tit_cliente;
	public Integer tit_fornecedor;
	public Integer tit_parceiro;
	public Integer tit_pagamento;
	public Character tipo_cedente;
	public Character tipo_titulo;
	public String tit_tipo;
	public String tit_data_emissao;
	public String tit_data_vencimento;
	public String tit_data_liquidacao;
	public String tit_data_cancelamento;
	public Double tit_valor;
	public String tit_forma;
	public Float tit_valor_total;
	public Float tit_desconto;
	public String tit_estado;

}
