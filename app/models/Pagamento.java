package models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import play.db.jpa.GenericModel;

@Entity
@Table(name="pagamento")
public class Pagamento extends GenericModel{

	@Id
	@GeneratedValue
	public int pag_codigo;
	public int pro_codigo;
	public String pag_nome;
	public String pag_valor;
	public String pag_cidade;
	public String pag_bairro;
	public String pag_cep;
	public String pag_uf;
	public String pag_cpf;
	public String pag_ddd;
	public String pag_telefone;
	public String pag_endereco;
	public String pag_numero;
	public String pag_complemento;
}
