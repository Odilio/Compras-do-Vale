package models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import play.db.jpa.Blob;
import play.db.jpa.GenericModel;

@Entity
@Table(name="endereco")
public class Endereco extends GenericModel{

	@Id
	@GeneratedValue
	public Integer end_codigo;
	public Integer usu_codigo;
	public String end_nome_completo;
	public String end_nome;
	public String end_endereco1;
	public String end_endereco2;
	public String end_email;
	public String end_cidade;
	public String end_estado;
	public String end_cep;
	public String end_celular;
	public String end_info_adicional;
	public Integer car_codigo; //codigo do pedido 
	
}
