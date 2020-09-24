package models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import play.db.jpa.Blob;
import play.db.jpa.GenericModel;

@Entity
@Table(name="usuario")
public class Usuario extends GenericModel{

	@Id
	@GeneratedValue
	public int usu_codigo;
	public String usu_nome_completo;
	public String usu_nome;
	@Transient
	public Blob usu_ima;
	public String usu_caminho_ima;
	public String usu_senha;
	public int usu_role; // 1 - usuario / 2 - parceiro / 3 - admin /  4 - revendedor / - 5 entregas
	public String usu_email;
	public String usu_telefone;
	public String usu_data_cadastro;
	
	
}
