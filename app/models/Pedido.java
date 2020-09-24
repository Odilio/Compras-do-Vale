package models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import play.db.jpa.GenericModel;

@Entity
@Table(name = "pedido")
public class Pedido extends GenericModel{
	
		@Id
		@GeneratedValue
		public Integer ped_codigo;
		public Integer usu_codigo;
		public Integer ped_codigo_digitador;
		public String ped_nome_usuario;
		public String ped_valor;
		public String ped_frete;
		public String ped_tipo_frete;
		public String ped_valor_itens;
		public String ped_forma;
		public String ped_status;
		public String ped_estado;
		public boolean ped_checkout;
		public Integer ped_quantidade ;
		public String ped_data;
		public String ped_rastreamento;
		public String ped_data_confirma;
}
