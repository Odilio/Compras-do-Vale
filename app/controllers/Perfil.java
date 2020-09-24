package controllers;

import play.*;
import play.cache.Cache;
import play.data.validation.Valid;
import play.db.jpa.Blob;
import play.i18n.Lang;
import play.i18n.Messages;
import play.libs.Crypto;
import play.libs.Images;
import play.libs.Mail;
import play.libs.OAuth2;
import play.modules.paginate.ValuePaginator;
import play.mvc.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import org.apache.commons.io.IOUtils;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;

import models.*;
public class Perfil extends Application {
		@Before
		static void checkUser() {
			
			if (connected() == null) {
				flash.error("Por Favor, efetue seu login primeiro");
				Application.logar("");
			}
		}
		
		 public static void index() {
			 long qtdPagamento = Pedido.count("byPed_statusAndUsu_codigo", "status1",renderArgs.get("user", Usuario.class).usu_codigo);
			 long qtdEnviados = Pedido.count("byPed_statusAndUsu_codigo", "status3",renderArgs.get("user", Usuario.class).usu_codigo);
			 long qtdParaEnvio = Pedido.count("byPed_statusAndUsu_codigo", "status2",renderArgs.get("user", Usuario.class).usu_codigo);
			 long qtdCancelados = Pedido.count("byPed_statusAndUsu_codigo", "status5",renderArgs.get("user", Usuario.class).usu_codigo);
			 long qtdFinalizado = Pedido.count("byPed_statusAndUsu_codigo", "status4",renderArgs.get("user", Usuario.class).usu_codigo);
			 render(qtdPagamento,qtdEnviados,qtdParaEnvio,qtdFinalizado,qtdCancelados);
		    }

		 public static void mostrarPedido(Integer codigo) {
			 Pedido pedido = Pedido.find("byPed_codigo", codigo).first();	
			 pedido.ped_status = Messages.get(pedido.ped_status);
			 List<Cart> cart = Cart.find("byPed_codigo", pedido.ped_codigo).fetch();
			 Endereco endereco = Endereco.find("byUsu_codigo", pedido.usu_codigo).first();
				render(pedido,cart,endereco);
			}
		 
		 
		 public static void listaProdutos() {
			 List<Produto> listaProdutos = Produto.all().fetch();	
			 	
			 render(listaProdutos);
		    }
	
		  public static void listaPedidos() {
				List<Cart> listaProdutos = Cart.all().fetch();
				ValuePaginator lista = new ValuePaginator(listaProdutos);
				lista.setPageSize(7);
				
				render(lista);
			}
		  
		  public static void listaVendas() {
				List<Cart> listaProdutos = Cart.all().fetch();
				ValuePaginator lista = new ValuePaginator(listaProdutos);
				lista.setPageSize(7);
				
				render(lista);
			}
 
    
    public static void pedidos() {
    	 DecimalFormat fmt = new DecimalFormat("0.00");
    	List<Pedido> listaProdutos = Pedido.find("byUsu_codigo", renderArgs.get("user", Usuario.class).usu_codigo).fetch();
  		for (Pedido pedido : listaProdutos) {
  			pedido.ped_valor = pedido.ped_valor;
			pedido.ped_status = Messages.get(pedido.ped_status);
		}
  		ValuePaginator lista = new ValuePaginator(listaProdutos);
  		lista.setPageSize(7);
  		
  		render(lista);
  	}
    
    public static void itensPedidos(Integer codigo) {
		List<Cart> listaProdutos = Cart.find("byPed_codigo", codigo).fetch();
		for (Cart pedido : listaProdutos) {
			pedido.car_status = Messages.get(pedido.car_status);
		}
		ValuePaginator lista = new ValuePaginator(listaProdutos);
		lista.setPageSize(12);
		
		render(lista);
	}

    
    public static void aguardandoPagamento() {
		List<Pedido> listaProdutos = Pedido.find("byPed_statusAndUsu_codigo", "status1",renderArgs.get("user", Usuario.class).usu_codigo).fetch();
		for (Pedido pedido : listaProdutos) {
			pedido.ped_status = Messages.get(pedido.ped_status);
		}
		ValuePaginator lista = new ValuePaginator(listaProdutos);
		lista.setPageSize(7);
		System.out.println(Messages.get("status1"));
		render(lista);
	}
    
    public static void aguardandoEnvio() {
    	List<Pedido> listaProdutos = Pedido.find("byPed_statusAndUsu_codigo", "status2",renderArgs.get("user", Usuario.class).usu_codigo).fetch();
  		for (Pedido pedido : listaProdutos) {
			pedido.ped_status = Messages.get(pedido.ped_status);
		}
  		ValuePaginator lista = new ValuePaginator(listaProdutos);
  		lista.setPageSize(7);
  		
  		render(lista);
  	}
    
    public static void enviados() {
    	List<Pedido> listaProdutos = Pedido.find("byPed_statusAndUsu_codigo", "status3",renderArgs.get("user", Usuario.class).usu_codigo).fetch();
  		for (Pedido pedido : listaProdutos) {
			pedido.ped_status = Messages.get(pedido.ped_status);
		}
  		ValuePaginator lista = new ValuePaginator(listaProdutos);
  		lista.setPageSize(7);
  		
  		render(lista);
  	}
    
    public static void concluidos() {
    	List<Pedido> listaProdutos = Pedido.find("byPed_statusAndUsu_codigo", "status4",renderArgs.get("user", Usuario.class).usu_codigo).fetch();
  		for (Pedido pedido : listaProdutos) {
			pedido.ped_status = Messages.get(pedido.ped_status);
		}
  		ValuePaginator lista = new ValuePaginator(listaProdutos);
  		lista.setPageSize(7);
  		
  		render(lista);
  	}
    
    public static void pagamento(Integer codigo) {
    	 Pedido pedido = Pedido.find("byPed_codigo", codigo).first();
    	 List<Cart> cart = Cart.find("byPed_codigo", pedido.ped_codigo).fetch();
 			pedido.ped_status = Messages.get(pedido.ped_status);
 		
	    	render(pedido,cart);
    }
    
    public static void cancelar() {
     Integer codigo = params.get("ped_codigo", Integer.class);
   	 Pedido ped  = Pedido.find("byPed_codigo", codigo).first();
   	 ped.delete();
     aguardandoPagamento();
   }
    
    
}