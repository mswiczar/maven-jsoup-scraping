package mav;

import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.json.JSONArray;



public class Scrapping {
	
	//	public static final String url = "https://www.benka.com.ar/productos/page/";
	//	public static final String base_url = "https://www.benka.com.ar/";
	
	public static final String url = "https://www.coccolati.com.ar/productos/page/";
	public static final String base_url = "https://www.coccolati.com.ar/";

	
	public static final int maxIteraccion = 11;
	public static ArrayList<HashMap<String,String> > listItems = new ArrayList<HashMap<String,String>>() ;

	
	public static void save2File()
	{
		JSONArray jsArray = new JSONArray(listItems);
		StringWriter out = new StringWriter();		
		jsArray.write(out);
		
		FileWriter fw;
		try {
			fw = new FileWriter("./coccolati.json");
			fw.write(out.toString());
			fw.close();
			System.out.print( "------END------------\n");

		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	public static void  updateWithImagesPriceAndSize()
	{
		int total = 0;
		
		for (HashMap<String,String> elemento: listItems)
		{
				total++;
				System.out.print( "-------" + total  +"------------------------------------\n");
			    System.out.println(elemento.get("SKU"));
			  	Document document = getHtmlDocument(base_url+elemento.get("Ficha"));
			    System.out.println(base_url+elemento.get("Ficha"));
				
				//talle y color
				//gridcart-wrapp
				//attribute -<div class="attribute">
				//<input type="text" class="m-cantidad" data-codigo="9076/5-bl" data-color="#ffffff|Blanco" data-talle="Talle Único" data-stock="3" data-precio="310" placeholder="0">


			  	Elements entradas = document.select("div.gridcart-wrapone");
				System.out.println("Cantidad Talle y Color: "+entradas.size()+"\n");
				
				String TalleColorStock =""; 
				
				for (Element elem : entradas) 
				{
					//talle
					//color
					
					
				  	Elements entradasInput = elem.getElementsByTag("input");
					for (Element elemInput : entradasInput) 
					{
						String Talle = elemInput.attr("data-talle");
						String Color = elemInput.attr("data-color");
						String Stock = elemInput.attr("data-stock");

						if (TalleColorStock.length()==0)
						{
							TalleColorStock = TalleColorStock + Talle +  " :: " + Color +  " :: " + Stock ;
						}
						else
						{
							TalleColorStock = TalleColorStock + "," + Talle +  " :: " + Color +  " :: " + Stock ;
						}
					}
					
				}
				
				elemento.put("TallesColorStock",TalleColorStock);

				
				System.out.print("TallesColorStock: " + TalleColorStock );
				System.out.print("\n");
				
				
				
				//imagenes hasta 3 
				//class b1
				//t1
				//<img src="productos/1574776834/250w_01_1583408990.jpg">
			  	//entradas = document.select("div.d2.t1");
				//System.out.println("Cantidad Images: "+entradas.size()+"\n");
				
				
			  	Elements entradasImagenes = document.select("div.d2");
				System.out.println("Cantidad Imagenes: "+entradasImagenes.size()+"\n");
				

				String Imagen1 ="";
				String Imagen2 ="";
				String Imagen3 ="";

				try
				{
					Imagen1 =entradasImagenes.get(0).getElementById("img01").attr("href");
					Imagen2 =entradasImagenes.get(0).getElementById("img02").attr("href");
					Imagen3 =entradasImagenes.get(0).getElementById("img03").attr("href");
				}
				catch (Exception e)
				{
					
				}
				
				elemento.put("Imagen1",Imagen1);
				elemento.put("Imagen2",Imagen2);
				elemento.put("Imagen3",Imagen3);
				
				System.out.print("Imagen1: "  +Imagen1 + " | ");
				System.out.print("Imagen2: "  +Imagen2 + " | ");
				System.out.print("Imagen3: "  +Imagen3 + " | ");
				System.out.print("\n");
				
				
		}
	}
	
	
	public static void main(String[] args) 
	{
				System.out.print( "------START------------\n");

				HashMap<String,String >myHashItem;
				if (getStatusConnectionCode(url) == 200) 
				{
					for (int i = 1; i<maxIteraccion;i++)
					{
						System.out.print( "->" +i+ "\n");
						System.out.print( "-------------------------------------------\n");
						
						
						Document document = getHtmlDocument(url+i+"/?sort_by=alpha-ascending");
						//Document document = getHtmlDocument(url);
						//p-relative overflow-none
						Elements entradas = document.select("div.item");
						System.out.println("Cantidad de Productos: "+entradas.size()+"\n");
						if (entradas.size()==0)
						{
							break;
						}
						for (Element elem : entradas) 
						{

							//String imagen = elem.getElementsByAttribute("srcset").attr("srcset");
							//String titulo = elem.getElementsByClass("title").text();

							String titulo = elem.getElementsByAttribute("title").attr("title");
							String precio =  elem.getElementsByClass("item-price").text();	
							String imagen =  elem.getElementsByAttribute("src").attr("src");
							String url =  elem.getElementsByAttribute("href").attr("href");

							precio = precio.replace(",00", "");
							precio = precio.replace("$", "");
							precio = precio.replace(".", "");

							imagen = imagen.replace("-100-0.jpg", "-640-0.jpg");
							imagen = "https:"+imagen;
							
							System.out.print( "->" + titulo+ "\n");
							System.out.print( "->->" + precio+ "\n");
							System.out.print( "->->->" + imagen+ "\n");

							myHashItem = new HashMap<String,String >();
								myHashItem.put("Titulo",titulo);
								myHashItem.put("Precio",precio);
								myHashItem.put("Imagen",imagen);
								myHashItem.put("URL",url);
							listItems.add(myHashItem);
							
							
							
							/*
							https://d26lpennugtm8s.cloudfront.net/stores/001/053/068/products/foto-10-5-20-14-39-591-55121930a4402056e315892996277827-100-0.jpg
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
							*/
						}
						
					}
					//updateWithImagesPriceAndSize();
					save2File();	
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



