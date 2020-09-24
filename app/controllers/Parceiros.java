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
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import org.apache.commons.io.IOUtils;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;
import org.joda.time.DateTime;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.microtripit.mandrillapp.lutung.MandrillApi;
import com.microtripit.mandrillapp.lutung.model.MandrillApiError;
import com.microtripit.mandrillapp.lutung.view.MandrillMessage;
import com.microtripit.mandrillapp.lutung.view.MandrillMessageStatus;
import com.microtripit.mandrillapp.lutung.view.MandrillUserInfo;
import com.microtripit.mandrillapp.lutung.view.MandrillMessage.Recipient;

import models.*;
import notifiers.Notifier;
public class Parceiros extends Application {
	
		@Before
		static void checkUser() {
			
			if (connected() == null) {
				flash.error("Please log in first");
				Application.logar("");
			}
			 if(connected().usu_role != 2 && connected().usu_role != 4 )
				 Application.logar("");
		}
		
		 public static Connection conn;
		 
		 public static void index() {
			 List<Pedido> pedidos = new ArrayList<Pedido>();
			 Pedido a = new Pedido();
			 try {
					Class.forName("org.postgresql.Driver");
					 conn = DriverManager.getConnection("jdbc:postgresql://107.170.0.146/vendasdb?user=vendas&password=vendas99337903");
					//Statement stm = conn.createStatement();
					PreparedStatement preparedStatement  = conn.prepareStatement(" SELECT ped_codigo,ped_quantidade,ped_valor FROM  pedido WHERE EXTRACT(MONTH FROM datapedido) = ?");
					preparedStatement.setInt(1, 2);
					ResultSet rs = preparedStatement.executeQuery();
					
					while (rs.next()) {
						a.ped_codigo = rs.getInt("ped_codigo");	
						a.ped_data = rs.getString("ped_data");
						a.ped_quantidade = rs.getInt("ped_quantidade");
						a.ped_valor = rs.getString("ped_valor");	
						
						pedidos.add(a); 
						a = new Pedido();
					}
					
				}catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			 render(pedidos);
		    }

		 public static void addProduto(Produto produto) {
				Usuario ze = renderArgs.get("user", Usuario.class);
		
				int eixo_x = 420;
				int eixo_y = 360;
				
				/*if (produto.find("byPro_referencia", produto.pro_referencia).first() != null){
					flash.error("Referencia já existe!");
					cadastrarProduto(new Produto()) ;
				}*/
				
			    Parceiro parceiro = Parceiro.find("byUsu_codigo", ze.usu_codigo).first();	
				
			    produto.pro_email_produtor = parceiro.par_email;
			    produto.pro_nome_produtor = parceiro.par_nome;
				produto.pro_status = "inativo";
				produto.pro_cod_usuario = ze.usu_codigo;
				produto.pro_nome_usuario = ze.usu_nome_completo;
				DateTime dat = new DateTime();
				if (produto.pro_data_hora == null)
					produto.pro_data_hora = dat.toDate();
					produto.pro_ultima_atualizacao = dat.toDate();
				try {
				if (produto.pro_imagem != null) {
					
						File outputFile = Play.getFile("/public/images/itens/" + produto.pro_referencia  + "num1" + ".jpg");
						Images.resize(produto.pro_imagem.getFile(), outputFile, eixo_x, eixo_y);
						produto.pro_caminho_ima  = "/public/images/itens/" + produto.pro_referencia + "num1" + ".jpg";
						File outputFile4 = Play.getFile("/public/images/itens-thumb/" + produto.pro_referencia  + "num1 (Copiar)" + ".jpg");
						
						Images.resize(produto.pro_imagem.getFile(), outputFile4, 195, 167);
					
						produto.pro_caminho_ima_thumb  ="/public/images/itens-thumb/" + produto.pro_referencia + "num1 (Copiar)" + ".jpg";
					
		 		}
			
				if (produto.pro_imagem2 != null) {
					File outputFile2 = Play.getFile("/public/images/itens/" + produto.pro_referencia  + "num2" + ".jpg");
					Images.resize(produto.pro_imagem2.getFile(), outputFile2, eixo_x, eixo_y);
					produto.pro_caminho_ima2  ="/public/images/itens/" + produto.pro_referencia + "num2" + ".jpg";
			
				}
			
				if (produto.pro_imagem3 != null) {
					File outputFile3 = Play.getFile("/public/images/itens/" + produto.pro_referencia  + "num3" + ".jpg");
					Images.resize(produto.pro_imagem3.getFile(), outputFile3, eixo_x, eixo_y);
					produto.pro_caminho_ima3  = "/public/images/itens/" + produto.pro_referencia + "num3" + ".jpg";
		
				}
				}catch (Exception e) {
					// TODO: handle exception
				}
				int size = produto.pro_titulo.length();
				if (size > 24 )
					produto.pro_titulo_reduzido = produto.pro_titulo.substring(0,24);
				else 
					produto.pro_titulo_reduzido = produto.pro_titulo;
				produto.save();
				DepositoMercadoria deposito = DepositoMercadoria.find("byDpm_codigo_mercadoria",	produto.pro_codigo).first();
			    if(deposito == null) {
			    	deposito = new DepositoMercadoria();
			    	deposito.dpm_cod_deposito = produto.pro_cod_produtor;
			    	deposito.dpm_cod_parceiro = produto.pro_cod_usuario;
			    	deposito.dpm_referencia = produto.pro_referencia;
			    	deposito.dpm_codigo_mercadoria = produto.pro_codigo;
			    	deposito.dpm_quantidade = produto.pro_quantidade_estoque;
			    	deposito.save();
			    }
			    
			    ItemTabelaPreco itemTabelaPreco = ItemTabelaPreco.find("byItp_cod_produto",	produto.pro_codigo).first();
			    if(itemTabelaPreco == null) {
			    	itemTabelaPreco = new ItemTabelaPreco();
			    	itemTabelaPreco.itp_cod_produto = produto.pro_codigo;
			    	itemTabelaPreco.itp_preco = produto.pro_preco;
			    	itemTabelaPreco.itp_tabela_preco = 1;
			    	itemTabelaPreco.itp_cod_usuario = ze.usu_codigo;
			    	itemTabelaPreco.save();
			    }
			    
			    produto.pro_valor_premio = produto.pro_preco / produto.pro_porcentagem_premio;
			    //adiciona produto a loja
			    ProdutoLoja produtoloja = new ProdutoLoja();
			    produtoloja.pro_caminho_ima_thumb = produto.pro_caminho_ima_thumb;
			    produtoloja.pro_categoria = produto.pro_categoria;
			    produtoloja.pro_cod_produtor = produto.pro_cod_usuario;
			    produtoloja.pro_cod_produto = produto.pro_codigo;
			    produtoloja.pro_preco = produto.pro_preco;
			    produtoloja.pro_titulo_reduzido = produto.pro_titulo_reduzido;
			    produtoloja.pro_valor_premio = produto.pro_valor_premio;
			    produtoloja.pro_prioridade = 1;
			    
			    produtoloja.save();
				Cache.set("produto_" + produto.pro_codigo, produto, "200mn");
				
				List<Produto> produtos = Produto.find("order by pro_prioridade asc").fetch();
			    Cache.set("produtos", produtos);
			    
				cadastrarProduto(new Produto()) ;
				
				}
			
		 
		 public static void editarProduto(Produto produto) {
				
				cadastrarProduto(produto);
			}
		 
		 public static void editarPerfil() {
				int codigoParceiro =  renderArgs.get("user", Usuario.class).usu_codigo;
			    Parceiro parceiro = Parceiro.find("byUsu_codigo", codigoParceiro).first();	
				render(parceiro);
			}
	
		 public static void atualizarParceiro(Parceiro parceiro) {
				
				Usuario usuParceiro = new Usuario();
				usuParceiro.usu_email = parceiro.par_email;
				usuParceiro.usu_nome_completo = parceiro.par_nome;
				usuParceiro.usu_nome = parceiro.par_login;
				Crypto a = new Crypto();
		        String senha = a.encryptAES(parceiro.par_senha);
				usuParceiro.usu_senha = senha;
				
				if (parceiro.par_imagem != null) {
					File outputFile = Play.getFile("/public/images/logoparceiros/" + parceiro.par_codigo  + "num1" + ".jpg");
					Images.resize(parceiro.par_imagem.getFile(), outputFile, 500, 500);
					parceiro.par_caminho_ima  = "/public/images/logoparceiros/" + parceiro.par_codigo + "num1" + ".jpg";
			
		 		}
				
				usuParceiro.save();		
				parceiro.save();
				admin();
			}
		 
		 public static void cadastrarProduto(Produto produto) {
			 	int codigoParceiro =  renderArgs.get("user", Usuario.class).usu_codigo;
				 List<GrupoProduto> listaGrupos = GrupoProduto.all().fetch();
				render(produto,codigoParceiro,listaGrupos);
			}

		 public static void mostrarCliente(Integer codigo) {
			 Endereco endereco = Endereco.find("byUsu_codigo", codigo).first();
				render(endereco);
			}
		
		 
		 public static void catalogo(Produto produto) {
			 List<Produto> listaProdutos = Cache.get("produtos", List.class);
			    if(listaProdutos == null) {
			    	listaProdutos = Produto.find("order by pro_prioridade asc").fetch();
			        Cache.set("produtos", listaProdutos);
			    }
			 	
			 render(listaProdutos);
			}
		 
		 public static void produtosLoja() {
			 List<Produto> listaProdutos = Cache.get("produtos", List.class);
			    if(listaProdutos == null) {
			    	listaProdutos = Produto.find("order by pro_prioridade asc").fetch();
			        Cache.set("produtos", listaProdutos);
			    }
			 	
			 render(listaProdutos);
			}
		 
		 public static void addProdutoLola(ProdutoLoja produtoloja) {
			if (produtoloja.find("byPro_cod_produto", produtoloja.pro_cod_produto).first() != null){
				flash.error("Referencia já existe!");
				produtosLoja() ;
			}
			
			 produtoloja.pro_prioridade = 2;
			 produtoloja.save();
			 minhaLoja();
			}
		 
		 public static void catalogoTabela(Produto produto) {
			 List<Produto> listaProdutos = Cache.get("produtos", List.class);
			    if(listaProdutos == null) {
			    	listaProdutos = Produto.find("order by pro_prioridade asc").fetch();
			        Cache.set("produtos", listaProdutos);
			    }
			 	
			 render(listaProdutos);
			}
		 public static void mostrarPedido(Integer codigo) {
			 Pedido pedido = Pedido.find("byPed_codigo", codigo).first();
			 pedido.ped_status = Messages.get(pedido.ped_status);
			 List<Cart> cart = Cart.find("byPed_codigo", pedido.ped_codigo).fetch();
				render(pedido,cart);
			}
		 
		 public static void minhaLoja() {
			 List<ProdutoLoja> listaProdutos = ProdutoLoja.find("byPro_cod_produtor", renderArgs.get("user", Usuario.class).usu_codigo).fetch();
			 Parceiro parceiro = Parceiro.find("byUsu_codigo", renderArgs.get("user", Usuario.class).usu_codigo).first();
			 render(listaProdutos,parceiro);
			}
		 
		 public static void admin() {
			 Usuario ze = renderArgs.get("user", Usuario.class);
			 Double valorTotal = 0.0;
			 Double valorCustoTotal = 0.0;
			 Double valorAReceber = 0.0;
			 Double valorRecebido = 0.0;
			 Integer quantidade = 0;
			 DecimalFormat fmt = new DecimalFormat("0.00");
			 List<Produto> listaProdutos = Produto.find("byPro_cod_produtor", ze.usu_codigo).fetch();	
			 for (Produto produto : listaProdutos) {
				 quantidade = quantidade + produto.pro_quantidade_estoque;
				 valorTotal = valorTotal + (produto.pro_preco * produto.pro_quantidade_estoque);
				// valorCustoTotal = valorCustoTotal + produto.pro_preco;
				 valorCustoTotal = valorCustoTotal + (produto.pro_preco_compra * produto.pro_quantidade_estoque);
				 
			}
			 	List<Titulo> titulos = Titulo.find("byTit_parceiro", ze.usu_codigo).fetch();	
			 for (Titulo titulo : titulos) {
				 if (titulo.tit_estado.equalsIgnoreCase("aberto")) 
				     valorAReceber += titulo.tit_valor;
				 if (titulo.tit_estado.equalsIgnoreCase("confirmado")) 
					 valorRecebido += titulo.tit_valor;
			}
			valorTotal = Double.parseDouble(fmt.format(valorTotal).replace(",", "."));
			//valorCustoTotal = Double.parseDouble(fmt.format(valorCustoTotal));
			valorAReceber = valorAReceber - valorRecebido;
			//valorAReceber = Double.parseDouble(fmt.format(valorAReceber));
			
			long qtdVendas = Cart.count("byUsu_codigoAndCar_finalizado",	renderArgs.get("user", Usuario.class).usu_codigo, true);
			long qtdProdutos = Produto.count("byPro_cod_produtor", ze.usu_codigo);
			long qtdPedidos = Cart.count("byPar_codigo", ze.usu_codigo);
				render(qtdProdutos,qtdPedidos,quantidade,valorTotal,valorCustoTotal,qtdVendas,valorAReceber,valorRecebido);
		    }
		 
		 public static void listaProdutos() {
			 Usuario ze = renderArgs.get("user", Usuario.class);
			 List<Produto> listaProdutos = Produto.find("byPro_cod_usuario", ze.usu_codigo).fetch();	
			 	
			 render(listaProdutos);
		    }
		 
		 public static void listaDepositos() {
			 Usuario ze = renderArgs.get("user", Usuario.class);
			 List<Deposito> listaProdutos = Deposito.find("byDep_parceiro", ze.usu_codigo).fetch();	
			 	
			 render(listaProdutos);
		    }
		 
		 public static void listaDepositoMercadoria() {
			 Usuario ze = renderArgs.get("user", Usuario.class);
			 List<DepositoMercadoria> lista = DepositoMercadoria.find("byDpm_cod_parceiro", ze.usu_codigo).fetch();	
			 ValuePaginator listaProdutos = new ValuePaginator(lista);
			 listaProdutos.setPageSize(12);
			 render(listaProdutos);
		    }
		 
		 public static void listaTitulos(List<Titulo> listaProdutos) {
			 Usuario ze = renderArgs.get("user", Usuario.class);
			 	listaProdutos = Titulo.find("byTit_parceiro", ze.usu_codigo).fetch();	
			 	
				System.out.println("to na lista a  " + listaProdutos.size());
				ValuePaginator lista = new ValuePaginator(listaProdutos);
				lista.setPageSize(12);
			 render(lista);
		    }
	
		 
		 public static void cancelarTitulo(Titulo titulo) {
		     Titulo ped  = Titulo.find("byTit_codigo", titulo.tit_codigo).first();
		   	 ped.tit_estado = "cancelado";
		   	 ped.save();
		   	listaTitulos(null);
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
		   		 
		   public static void movimentacoes(Integer codigo) {
				List<Cart> listaProdutos = Cart.find("byPro_codigo", codigo).fetch();
				for (Cart pedido : listaProdutos) {
					pedido.car_status = Messages.get(pedido.car_status);
				}
				ValuePaginator lista = new ValuePaginator(listaProdutos);
				lista.setPageSize(12);
				
				render(lista);
			}
		   
		  public static void listaPedidos() {
			  Usuario ze = renderArgs.get("user", Usuario.class);
			  List<Cart> listaProdutos = Cart.find("byPar_codigo", ze.usu_codigo).fetch();
			  	
				ValuePaginator lista = new ValuePaginator(listaProdutos);
				lista.setPageSize(7);
				
				render(lista);
			}
		  
		  public static void aguardandoPagamento() {
				List<Pedido> listaProdutos = Pedido.find("byPed_status", "status1").fetch();
				ValuePaginator lista = new ValuePaginator(listaProdutos);
				lista.setPageSize(7);
				System.out.println(Messages.get("status1"));
				render(lista);
			}
		    
		    public static void aguardandoEnvio() {
		  		List<Cart> listaProdutos = Cart.find("byCar_status", "status2").fetch();
		  		ValuePaginator lista = new ValuePaginator(listaProdutos);
		  		lista.setPageSize(7);
		  		
		  		render(lista);
		  	}
		    
		    public static void enviados() {
		  		List<Cart> listaProdutos = Cart.find("byCar_status", "status3").fetch();
		  		ValuePaginator lista = new ValuePaginator(listaProdutos);
		  		lista.setPageSize(7);
		  		
		  		render(lista);
		  	}
		  			
    public static void pedidos() {
    	List<Cart> listaCarts = Cart.find("usu_codigo = ?1 order by ped_codigo desc",
    			renderArgs.get("user", Usuario.class).usu_codigo).fetch();
    	for (Cart pedido : listaCarts) {
			pedido.car_status = Messages.get(pedido.car_status);
		}
		ValuePaginator lista = new ValuePaginator(listaCarts);
		lista.setPageSize(7);
		
		render(lista);
	}

    
	
	public static void criarFornecedor() {
		List<Fornecedor> fornecedores = Fornecedor.all().fetch();
		render(fornecedores);
	}
	
	public static void addFornecedor(Fornecedor fornecedor) {
		fornecedor.save();
		criarFornecedor() ;
	}
	
	public static void criarDeposito() {
		List<Deposito> depositos = Deposito.all().fetch();
		render(depositos);
	}
	
	public static void cadastrarParceiro() {
		List<Parceiro> parceiros = Parceiro.all().fetch();
		ValuePaginator lista = new ValuePaginator(parceiros);
		lista.setPageSize(12);
		render(lista);
	}
	
	
	public static void criarPedido() {
		Pedido pedido = Pedido.find("usu_codigo = ?1 and ped_checkout = ?2", renderArgs.get("user", Usuario.class).usu_codigo, false).first();
		List<Cart> lista = new ArrayList();
		if(pedido != null) {
			lista = Cart.find("ped_codigo = ? ", pedido.ped_codigo).fetch();
			render(lista,pedido);
		}
		
		pedido = new Pedido();
		Date data = new Date();
	    pedido.ped_data = formatador.format(data);
	    pedido.usu_codigo = renderArgs.get("user", Usuario.class).usu_codigo;
 	    pedido.ped_nome_usuario = renderArgs.get("user", Usuario.class).usu_nome_completo;
 	    pedido.ped_estado = "aberto";
 	    pedido.ped_checkout = false;
 	    pedido.save();
		render(lista,pedido);
	}
	
	public static void criarTransferencia() {
		Pedido pedido = Pedido.find("usu_codigo = ?1 and ped_checkout = ?2", renderArgs.get("user", Usuario.class).usu_codigo, false).first();
		List<Cart> lista = new ArrayList();
		if(pedido != null) {
			lista = Cart.find("ped_codigo = ? ", pedido.ped_codigo).fetch();
			render(lista,pedido);
		}
		
		pedido = new Pedido();
		Date data = new Date();
	    pedido.ped_data = formatador.format(data);
 	    pedido.usu_codigo = renderArgs.get("user", Usuario.class).usu_codigo;
 	    pedido.ped_nome_usuario = renderArgs.get("user", Usuario.class).usu_nome_completo;
 	    pedido.ped_estado = "aberto";
 	    pedido.ped_checkout = false;
 	    pedido.save();
		render(lista,pedido);
	}
	
	
	public static void addItemPedido(String rev_texto,String pro_quantidade) {
		Pedido pedido = Pedido.find("usu_codigo = ?1 and ped_checkout = ?2", renderArgs.get("user", Usuario.class).usu_codigo, false).first();
        if(pedido == null) {
        	pedido = new Pedido();
        	Date data = new Date();
		    pedido.ped_data = formatador.format(data);
	 	    pedido.usu_codigo = renderArgs.get("user", Usuario.class).usu_codigo;
	 	    pedido.ped_nome_usuario = renderArgs.get("user", Usuario.class).usu_nome_completo;
	 	    pedido.ped_estado = "aberto";
	 	    pedido.ped_checkout = false;
	 	    pedido.save();
        }
		rev_texto = "%"+rev_texto+"%";
		Produto produto = Produto.find("pro_referencia like ?1", rev_texto).first();
		produto.pro_quantidade = Integer.parseInt(pro_quantidade);
		Cart cart = new Cart();
		cart.car_referencia = produto.pro_referencia;
		cart.car_preco = produto.pro_preco;
		cart.car_caminho_ima = produto.pro_caminho_ima;
		cart.car_titulo = produto.pro_titulo;
		cart.car_quantidade = produto.pro_quantidade;
		cart.par_codigo= produto.pro_cod_produtor;
		cart.usu_codigo = renderArgs.get("user", Usuario.class).usu_codigo;
		cart.ped_codigo= pedido.ped_codigo;
		cart.save();
		renderJSON(cart);
	}
	
	  public static void cancelarItem(Integer codigo) {
		   	 Cart cart  = Cart.find("byCar_codigo", codigo).first();
		   	 cart.delete();
		     criarPedido();
		   }
	  
	  public static void confirmaTitulo(Titulo titulo) {
			Date data = new Date();
			 DecimalFormat fmt = new DecimalFormat("0.00");
			Titulo tituloAtual = Titulo.find("byTit_codigo", titulo.tit_codigo).first();
			 Pedido pedidoAtual = Pedido.find("byPed_codigo", titulo.ped_codigo).first();
			 pedidoAtual.ped_forma = titulo.tit_forma;
			 pedidoAtual.ped_data_confirma = formatador.format(data);
			 pedidoAtual.ped_estado = "confirmado";
		 	 pedidoAtual.ped_status = "status2";
		 	 pedidoAtual.ped_checkout = true;
			 pedidoAtual.save();
	 		 
			 tituloAtual.tit_forma = titulo.tit_forma;
			 tituloAtual.tit_data_liquidacao = formatador.format(data);
			 tituloAtual.tit_estado = "confirmado";
			 
			 titulo.save(); 
			 if (titulo.tit_forma.equalsIgnoreCase("Paypal"))
			 {
				 Titulo pagar = new Titulo();
				 
				 pagar.tit_data_emissao = formatador.format(data);
				 pagar.tit_cliente = pedidoAtual.usu_codigo;
				 pagar.tit_forma = pedidoAtual.ped_forma;
				 pagar.ped_codigo = pedidoAtual.ped_codigo;
				 pagar.tit_valor = Double.parseDouble(fmt.format((tituloAtual.tit_valor * 0.07) )) ;
				 pagar.tit_tipo = "pagar";
				 pagar.tit_estado = "confirmado";
					 
				 pagar.save();
			 }
			 
			 Cart cart = Cart.find("byCar_codigo", titulo.car_codigo).first();
			 DepositoMercadoria dpm = DepositoMercadoria.find("byDpm_codigo_mercadoria", cart.pro_codigo).first();
			 dpm.dpm_quantidade  = dpm.dpm_quantidade - cart.car_quantidade;
			 dpm.save();
			 
			 listaTitulos(null);
		   }
	  
	  
	  
	  public static void confirmacaoPedido(Pedido pedido) {
			 Date data = new Date();
		     Pedido pedidoAtual = Pedido.find("byPed_codigo", pedido.ped_codigo).first();
			 pedidoAtual.ped_forma = pedido.ped_forma;
			 pedido.ped_data_confirma = formatador.format(data);
		 	 pedido.ped_estado = "confirmado";
		 	 pedido.ped_status = "status1";
		 	 pedido.ped_checkout = true;
		 	 pedidoAtual.ped_quantidade = (int) Cart.count("byPed_codigo",	pedido.ped_codigo);
			 pedidoAtual.save();
	 			
			 DecimalFormat fmt = new DecimalFormat("0.00");
			 
			 Titulo pagar = new Titulo();
			 Titulo receber = new Titulo();
			 
			 pagar.tit_data_emissao = formatador.format(data);
			 receber.tit_data_emissao = formatador.format(data);
			 
			 pagar.tit_cliente = pedido.usu_codigo;
			 receber.tit_cliente = pedido.usu_codigo;
			 
			 pagar.tit_parceiro = pedido.usu_codigo;
			 receber.tit_parceiro = pedido.usu_codigo;
			 
			 pagar.tit_forma = pedido.ped_forma;
			 receber.tit_forma = pedido.ped_forma;
			
			 pagar.tit_valor =  Double.parseDouble(pedido.ped_valor.replace(",", "."))/10 * 9;
			 receber.tit_valor = Double.parseDouble(pedido.ped_valor.replace(",", "."))/10 ;
			
			 pagar.ped_codigo = pedido.ped_codigo;
			 receber.ped_codigo = pedido.ped_codigo;
			 
			 pagar.tit_tipo = "pagar";
			 receber.tit_tipo = "receber";
			 
			 pagar.tit_estado = "aberto";
			 receber.tit_estado = "aberto";
			 
			 pagar.save();
			 receber.save();
			 
	    	 criarPedido();
		   }
	  
	  
	  public static void confirmacaoPagamento(Integer codigo) {
			 Pedido pedido = Pedido.find("byPed_codigo", codigo).first();
				
			 //debitar itens dos estoque após confirmar
			 /*List<Cart> listaProdutos = Cart.find("byPed_codigo", codigo).fetch();
	    	    Produto pro = Produto.find("byPro_codigo ", "").first();*/
	 			pedido.ped_status = Messages.get(pedido.ped_status);
		     render(pedido);
		   }
	  
	  public static void buscaTitulo(Titulo titulo) {
			List<Titulo> titulos = Titulo.all().fetch();
			System.out.println("tit_estado:" + titulo.tit_estado);
			System.out.println("mudou a  " + titulos.size());
			listaTitulos(titulos);
		
		   }
	  
	  public static void listarGrupoProduto() {
			List<GrupoProduto> grupoProduto = GrupoProduto.all().fetch();
	       
			renderJSON(grupoProduto);
		}
	  
	  public static void statusProdutoEntregue(String rev_text) {
		  String arr []  = rev_text.split("-");
		  Pedido pedido = null;
		  for (String string : arr) {
			  pedido =  Pedido.find("byPed_codigo", Integer.parseInt(string)).first();
			  if (pedido != null){
				  System.out.println(" entrou aqui ");
				  pedido.ped_status = "status4"  ;
				  pedido.save();
			  }
		}
		  
		   }
	
	  public static void statusProdutoCancelado(String rev_text) {
		  String arr []  = rev_text.split("-");
		  Pedido pedido = null;
		  for (String string : arr) {
			  pedido =  Pedido.find("byPed_codigo", Integer.parseInt(string)).first();
			  if (pedido != null){
				  System.out.println(" entrou aqui ");
				  pedido.ped_status = "status5"  ;
				  pedido.save();
			  }
		}
		  
		   }
	 
	  
	  public static void statusProdutoParaEnvio(String rev_text) {
		  String arr []  = rev_text.split("-");
		  Pedido pedido = null;
		  for (String string : arr) {
			  pedido =  Pedido.find("byPed_codigo", Integer.parseInt(string)).first();
			  if (pedido != null){
				  pedido.ped_status = "status2"  ;
				  pedido.save();
			  }
		}
		  
		   }
	
	  
	  public static void statusProdutoEnviado(String rev_text) {
		  String arr []  = rev_text.split("-");
		  Pedido pedido = null;
		  for (String string : arr) {
			  pedido =  Pedido.find("byPed_codigo", Integer.parseInt(string)).first();
			  if (pedido != null){
				  pedido.ped_status = "status3"  ;
				  pedido.save();
			  }
		}
		  
		   }
	
	  public static void statusProdutoAguardandoPagamento(String rev_text) {
		  String arr []  = rev_text.split("-");
		  Pedido pedido = null;
		  for (String string : arr) {
			  pedido =  Pedido.find("byPed_codigo", Integer.parseInt(string)).first();
			  if (pedido != null){
				  pedido.ped_status ="status1" ;
				  pedido.save();
			  }
		}
		  
		   }
	  
		public static void productDetails(Integer codigo ) {
			Produto produto = Cache.get("produto_" + codigo, Produto.class);
		    if(produto == null) {
			 produto = Produto.find("byPro_codigo",
					codigo).first();
			 Cache.set("produto_" + codigo, produto, "30mn");
		    }
		    render(produto);
		}
}