package models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import play.db.jpa.GenericModel;

@Entity
@Table(name = "cart")
public class Cart extends GenericModel{

		@Id
		@GeneratedValue
		public Integer car_codigo;
		public Integer ped_codigo;
		public Integer pro_codigo;
		public Integer usu_codigo;
		public Integer par_codigo;
		public String car_titulo;
		public int car_codigo_afiliado;
		public String car_status;
		public boolean car_checkout;
		public boolean car_finalizado;
		public String car_referencia ;
		public String car_caminho_ima;
		public Integer car_quantidade;
		public double car_preco;
		public String ped_data;
}
