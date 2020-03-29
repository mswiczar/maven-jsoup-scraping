package mav;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Scrapping {
	
	public static final String url = "https://www.embrujojeans.com/marca-embrujo.html?&evento=&precioMenorReal=&precioMayorReal=&ofertas=&menu=&dato_a_buscar=&cate=&filtro_marca=&filtro_color=&filtro_talle=&filtro_categoria=&filtro_subcategoria=&filtro_tercercategoria=&aplicarPrecios=&mantenerPrecios=&page=";
	public static ArrayList<HashMap<String,String> > listItems = new ArrayList<HashMap<String,String>>() ;

	public static void main(String[] args) {

				HashMap<String,String >myHashItem;
				if (getStatusConnectionCode(url) == 200) 
				{
					for (int i = 0; i<30;i++)
					{
						System.out.print( "->" +i+ "\n");
						System.out.print( "-------------------------------------------\n");
						Document document = getHtmlDocument(url+i);
						Elements entradas = document.select("div.d1");
						System.out.println("Cantidad de Productos: "+entradas.size()+"\n");
						if (entradas.size()==0)
						{
							break;
						}
						// Paseo cada una de las entradas	
						for (Element elem : entradas) 
						{
						
							String imagen = elem.getElementsByAttribute("srcset").attr("srcset");
							String titulo = elem.getElementsByClass("title").text();
							String sku = elem.getElementsByClass("sku").text();
							String price = elem.getElementsByClass("price").text();
							String ficha = elem.getElementsByAttribute("href").attr("href");
							if (titulo.length()>0)
							{
								
								myHashItem = new HashMap<String,String >();
								myHashItem.put("Titulo",titulo);
								myHashItem.put("SKU",sku);
								myHashItem.put("Precio",price);
								myHashItem.put("Imagen",imagen);
								myHashItem.put("Ficha",ficha);
								
								
								listItems.add(myHashItem);
								
								System.out.print("Titulo: " + titulo + " | ");
								System.out.print("SKU: "  + sku+ " | ");
								System.out.print("Precio: "  +price + " | ");
								System.out.print("Imagen: "  +imagen + " | ");
								System.out.print("Ficha: "  +ficha + " | ");
								System.out.print("\n");

							}
						}
					}
						
				}
				else
				{
					System.out.println("El Status Code no es OK es: "+getStatusConnectionCode(url));
				}
	}
	
	
	public static int getStatusConnectionCode(String url) 
	{
		Response response = null;
		try {
			response = Jsoup.connect(url).userAgent("Mozilla/5.0").timeout(100000).ignoreHttpErrors(true).execute();
		} catch (IOException ex) {
			System.out.println("IOException al obtener el Status Code: " + ex.getMessage());
		}
		return response.statusCode();
	}
	
	
	public static Document getHtmlDocument(String url) 
	{
		Document doc = null;
		try {
			doc = Jsoup.connect(url).userAgent("Mozilla/5.0").timeout(100000).get();
		} catch (IOException ex) {
			System.out.println("IOException al obtener el HTML de la página" + ex.getMessage());
		}
		return doc;
	}
}



