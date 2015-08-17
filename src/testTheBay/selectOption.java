package testTheBay;

import java.util.concurrent.TimeUnit;
import java.util.List;

import org.junit.Assert;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;


public class selectOption {

	public static void main(String[] args) throws InterruptedException {
		
		System.setProperty("webdriver.chrome.driver","C:\\Program Files\\chromedriver.exe");
	    WebDriver driver = new ChromeDriver();	    
	    driver.manage().window().maximize();
	    driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS); 
	    driver.get("http://www.thebay.com");	    
	    
	    WebElement firstMenu = driver.findElement(By.xpath("//a[@href='http://www.thebay.com/webapp/wcs/stores/servlet/en/thebay/search/womens-apparel']")); 	    
	    WebElement firstSubMenu = driver.findElement(By.xpath("//div[@id='men2']/div[1]/ul[1]/li/a"));	    
	    Actions action = new Actions(driver);
	    action.moveToElement(firstMenu).perform();
	    Thread.sleep(5000);	    
	    action.click(firstSubMenu).perform();
      
	    // Parameterizing test    using the data retrieved from <option>      for the brand name selection
	    // WOMEN>NEW ARRIVALS>SHOP BY BRAND
	    
        List<WebElement> elementOption = driver.findElements(By.xpath("//div[@id='left_select']/select/option"));   
        int num = elementOption.size();
        System.out.println("Total brands: "+ num);
       
        int i;
       for (i=1; i<num; i++)
        {
    	 WebElement firstMenu2 = driver.findElement(By.xpath("//a[@href='http://www.thebay.com/webapp/wcs/stores/servlet/en/thebay/search/womens-apparel']"));
    	 
    	 
   	     WebElement firstSubMenu2 = driver.findElement(By.xpath("//div[@id='men2']/div[1]/ul[1]/li/a"));
  
   	     Thread.sleep(5000);
         Actions action2 = new Actions(driver);
         Thread.sleep(5000);
   	     action2.moveToElement(firstMenu2).perform();
    
   	     action2.click(firstSubMenu2).perform();
   	   
     	List<WebElement> elementOption2 = driver.findElements(By.xpath("//div[@id='left_select']/select/option"));
    	
    
        elementOption2.get(i).click();
        Thread.sleep(5000);
        
        String sBrandName = driver.findElements(By.xpath("//div[@id='left_select']/select/option")).get(i).getText();
        String brandName = sBrandName.trim();
        String h1Text = driver.findElement(By.xpath("//div[@id='right_con']/h1")).getText();
             
        System.out.println(i);  
        System.out.println(brandName); 
        System.out.println(driver.getCurrentUrl());
        Assert.assertEquals(brandName,h1Text);
        
        driver.navigate().back();    
        Thread.sleep(5000);
        System.out.println(driver.getCurrentUrl());
        
        }
       System.out.println("==========");  
	   System.out.println("Testing of WOMEN>NEW ARRIVALS>SHOP BY BRAND runs over");
	}
}
