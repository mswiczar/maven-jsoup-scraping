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
	public static final String base_url = "https://www.embrujojeans.com/";
    public static final int maxIteraccion = 1;
	public static ArrayList<HashMap<String,String> > listItems = new ArrayList<HashMap<String,String>>() ;

	public static void  updateWithImagesPriceAndSize()
	{
		
		for (HashMap<String,String> elemento: listItems)
		{
				System.out.print( "-------------------------------------------\n");
			    System.out.println(elemento.get("SKU"));
			  	Document document = getHtmlDocument(base_url+"/"+elemento.get("Ficha"));
				
				//talle y color
				//gridcart-wrapp
				//attribute -<div class="attribute">
				//<input type="text" class="m-cantidad" data-codigo="9076/5-bl" data-color="#ffffff|Blanco" data-talle="Talle Único" data-stock="3" data-precio="310" placeholder="0">

			  	Elements entradas = document.select("div.gridcart-wrapp");
				System.out.println("Cantidad Talle y Color: "+entradas.size()+"\n");
				
				String Talles="";
				String Colores="";
				
				for (Element elem : entradas) 
				{
					//talle
					//color
					String Talle = elem.getElementsByTag("input").attr("data-talle");
					String Color = elem.getElementsByTag("input").attr("data-color");
					if (Color.length()>0)
					{
				        String[] auxArray = Color.split("|");
				        if (auxArray.length > 1)
				        {
							//myHashItem.put("id_external",auxArray[1]);
				        }
					}
					if (Talles.length()==0)
					{
						Talles = Talles + Talle;
					}
					else
					{
						Talles = Talles + "," Talle;
					}
					elemento.put("Talles",Talles);
					elemento.put("Colores",Colores);
					
					System.out.print("Talles: " + Talles + " | ");
					System.out.print("Colores: "  + Colores+ " | ");
					System.out.print("\n");
					
					
				}
				
				
			  
			  
				
				//imagenes hasta 3 

				//class b1
				//t1
				//<img src="productos/1574776834/250w_01_1583408990.jpg">
			  	//entradas = document.select("div.d2.t1");
				//System.out.println("Cantidad Images: "+entradas.size()+"\n");
			  
			  
			  
		}
		
		
		
	}
	
	
	public static void main(String[] args) 
	{

				HashMap<String,String >myHashItem;
				if (getStatusConnectionCode(url) == 200) 
				{
					for (int i = 0; i<maxIteraccion;i++)
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
								myHashItem.put("ImagenWebp",imagen);
								myHashItem.put("Ficha",ficha);
						        String[] auxArray = ficha.split("-");
						        if (auxArray.length > 1)
						        {
									myHashItem.put("id_external",auxArray[1]);
						        }
						        
								
								listItems.add(myHashItem);
								
								System.out.print("Titulo: " + titulo + " | ");
								System.out.print("SKU: "  + sku+ " | ");
								System.out.print("Precio: "  +price + " | ");
								System.out.print("ImagenWebp: "  +imagen + " | ");
								System.out.print("Ficha: "  +ficha + " | ");
								System.out.print("External_id: "  +myHashItem.get("id_external") + " | ");
								System.out.print("\n");

								
							}
						}
					}
					updateWithImagesPriceAndSize();
						
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



