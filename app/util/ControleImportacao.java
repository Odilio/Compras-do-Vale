package util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.LinkedList;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import models.Produto;
import play.db.jpa.Blob;


public class ControleImportacao {
	// Entidade entidade;

	public  void importarProdutos(Blob doc){
		Produto produto = new Produto();
		FileInputStream file = null;
		
			try {
				file = new	FileInputStream(doc.getFile());
				HSSFWorkbook workbook = new HSSFWorkbook(file);
				HSSFSheet sheet = workbook.getSheetAt(0);
				Cell codigo = null;
				Cell preco = null;
				Iterator<Row> rowIterator = sheet.iterator();
				while (rowIterator.hasNext()) {
					Row row = rowIterator.next();
					produto.pro_referencia = row.getCell(0).toString();
					produto.pro_descricao = row.getCell(1).toString();
					produto.pro_categoria = row.getCell(2).toString();
					produto.pro_titulo = row.getCell(3).toString();
					produto.pro_cor = row.getCell(4).toString();
					produto.pro_estilo = row.getCell(5).toString();
					produto.pro_genero = row.getCell(6).toString();
					System.out.println("COD: " + produto.pro_referencia + " -- Preco: " + produto.pro_descricao);
							//+ row.getCell(1).getNumericCellValue());
					/*LinkedList<Object> l = new LinkedList<>();
					l.add(preco.getNumericCellValue());
					l.add(codigo.toString());*/
					
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		
		
		
	}
	
	


	/*public void cadastraProdutos(FileUploadEvent event) {
		ELContext elContext = FacesContext.getCurrentInstance().getELContext();
		ControleProduto cProd = (ControleProduto) FacesContext
				.getCurrentInstance().getApplication().getELResolver()
				.getValue(elContext, null, "controleProduto");
		try {
			InputStream file = event.getFile().getInputstream();
			HSSFWorkbook workbook = new HSSFWorkbook(file);
			HSSFSheet sheet = workbook.getSheetAt(0);

			int qtdLinhas = sheet.getPhysicalNumberOfRows();
			int linhaAtual = 1;

			Iterator<Row> rowIterator = sheet.iterator();
			// pula o cabe�alho
			Row row = rowIterator.next();
			while (rowIterator.hasNext()) {
				linhaAtual++;
				System.out.println("Linha: " + linhaAtual + " de " + qtdLinhas);
				row = rowIterator.next();
				Produto p = new Produto(row.getCell(1).toString(), row.getCell(
						2).toString(), row.getCell(7).getNumericCellValue(),
						row.getCell(3).toString(), row.getCell(4).toString(),
						row.getCell(5).toString(), row.getCell(6)
								.getNumericCellValue(), Math.round(row.getCell(
								0).getNumericCellValue())
								+ ".jpg");
				cProd.setEntidade(p);

				try {
					cProd.jpa.salvar(cProd.getEntidade());
				} catch (Exception e) {
					cProd.trataErro(e);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		cProd.enviaMensagemUsuario("Produtos Cadastrados com sucesso!",
				FacesMessage.SEVERITY_INFO);
	}*/

	public String importaPrecos() {

		return "/importacao/precos.jsf";

	}

	public String importaProdutos() {

		return "/importacao/produtos.jsf";

	}

	public String layouts() {
		return "/importacao/layouts.jsf";

	}
}
