package testTheBay;

import java.util.concurrent.TimeUnit;
import org.junit.Assert;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;

import java.io.*;
import jxl.*; 
  
public class testTheBay {
	public static void main(String[] args) throws InterruptedException{
		System.setProperty("webdriver.chrome.driver","C:\\Program Files\\chromedriver.exe");
	    WebDriver driver = new ChromeDriver();	    
	    driver.manage().window().maximize();
	    driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS); 
	    driver.get("http://www.thebay.com");	    
	    
	    //WebElement firstMenu = driver.findElement(By.xpath("//a[@href='http://www.thebay.com/webapp/wcs/stores/servlet/en/thebay/search/womens-apparel']")); 
	    WebElement firstMenu = driver.findElement(By.xpath("//ul[@id='main_nav']/li[1]/a"));
	    	    
	    //WebElement firstSubMenu = driver.findElement(By.xpath("//a[contains(@href, '/webapp/wcs/stores/servlet/en/thebay/search/womens-apparel/new-arrivals-women')]"));
	    WebElement firstSubMenu = driver.findElement(By.xpath("//div[@id='men2']/div[1]/ul[1]/li/a"));	    
	    Actions action = new Actions(driver);
	    action.moveToElement(firstMenu).perform();
	    Thread.sleep(5000);	    
	    action.click(firstSubMenu).perform();
      
	    // parameterizing test using data from Excel 2003  for the brand name  
	    int i;
        Sheet sheet;
        Workbook book;
        Cell cell1,cell2;              
	    
        try { 
        	                  
            book= Workbook.getWorkbook(new File("data\\WomenNewSelect.xls"));
            sheet = book.getSheet(0);            
            int rows = sheet.getRows(); 
            
            i=0;
            while(i < rows) 	
            {
            	sheet=book.getSheet(0); //get the first sheet
                cell1=sheet.getCell(0,i); //get the object of ID of the selected brand
                cell2=sheet.getCell(1,i); //get the object of selected brand name
                String brandName = cell2.getContents(); //get the text content of the brand name 
                
               if("".equals(cell1.getContents())==true)   //if no ID object
                    break;               
                	
                new Select(driver.findElement(By.name("select"))).selectByVisibleText(brandName);  
        	    Assert.assertEquals(brandName, driver.findElement(By.xpath("//div[@id='right_con']/h1")).getText());
        	    System.out.println("Selection of " + brandName +" is verfied.");
        	    Thread.sleep(5000);               
                i++;        
            }
            book.close(); 
            System.out.println("----------");//a splitting line for exception
        }    
      catch(Exception e){System.out.println(e);}
      System.out.println("----------");//a splitting line for exception
      System.out.println("Testing of WOMEN>NEW ARRIVALS>SHOP BY BRAND runs over");     
	
	}
}
