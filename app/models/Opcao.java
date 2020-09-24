package models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import play.db.jpa.Blob;
import play.db.jpa.GenericModel;

@Entity
@Table(name="opcao")
public class Opcao extends GenericModel{

	@Id
	@GeneratedValue
	public int opc_codigo;
	public String opc_nome;
	public String opc_grupo;
	
	
}
