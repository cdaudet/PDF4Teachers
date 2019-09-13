package fr.themsou.render;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;

import fr.themsou.panel.MainScreen;

public class PDFRender {
	
	public Image[] render(File file, int startPage, int maxPages){
		
		try{
			
			PDDocument doc = PDDocument.load(file);
				
			System.out.println("Start render");
			long time = System.currentTimeMillis();
			
			PDFRenderer pdfRenderer = new PDFRenderer(doc);
			
			if(doc.getNumberOfPages() < maxPages) maxPages = doc.getNumberOfPages() - 1;
			
			int pageCounter = startPage;
			Image[] images = new Image[maxPages - startPage + 1];
			
			for(@SuppressWarnings("unused") PDPage page : doc.getPages()){
				
				if(pageCounter > maxPages) continue;
				
			    BufferedImage bimg = pdfRenderer.renderImageWithDPI(pageCounter, 200, ImageType.RGB);
			    images[pageCounter] = (Image) bimg;
			    
			    
			    
			    pageCounter ++;
			}
			doc.close();
			
			System.out.println("-> Finished in " + (System.currentTimeMillis() - time) + "ms");
			
			return images;
			
		}catch(Exception | NoSuchMethodError e){
			System.out.println(e.getMessage());
			MainScreen.status = 2;
			return null;
		}
		
		
		
	}

}
