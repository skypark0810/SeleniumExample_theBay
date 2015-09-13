package testTheBay;

import java.sql.*;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;
 
public class dataTesting{
	public static void main(String[] args) throws InterruptedException{
		
		try { 
		       //*CREATE THE DATABASE test_thebay
		        Connection conn_forDB = null;
		        conn_forDB = DriverManager.getConnection("jdbc:mysql://localhost:3306/","root","guo");
		        Statement stmt_createDB = conn_forDB.createStatement();
		        stmt_createDB.execute("CREATE DATABASE IF NOT EXISTS test_thebay");       
		        conn_forDB.close();
		        stmt_createDB.close();
		        
		        //*CREATE THE TABLE woman_newarrivals_brand 
		        Connection conn = null;       
		        Class.forName("com.mysql.jdbc.Driver").newInstance();
		        conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/test_thebay","root","guo");  
		        Statement stmt = conn.createStatement();
		        DatabaseMetaData md = conn.getMetaData(); 
		        ResultSet res = md.getTables(null,null,"%",null);  
		        while (res.next())
		       { 
		        	String table_name = res.getString(3);
		            if(new String("woman_newarrivals_brand").equals(table_name)) 
		        	{
		            stmt.executeUpdate("DROP TABLE woman_newarrivals_brand");
		        	}
		        }       
		        stmt.executeUpdate("CREATE TABLE woman_newarrivals_brand(id int NOT NULL,brandName char(50) NOT NULL,brandUrl char(255),UNIQUE(id,brandName))");             

		        conn.close();
		        stmt.close();       
		        res.close();
		        } 
		        catch(SQLException se){System.out.println(se);}
		        catch(Exception e){System.out.println(e);} 
		 
		System.setProperty("webdriver.chrome.driver","C:\\Program Files\\chromedriver.exe");
	    WebDriver driver = new ChromeDriver();	    
	    driver.manage().window().maximize();
	    driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS); 
	    driver.get("http://www.thebay.com");	    
	    
	    //WebElement firstMenu = driver.findElement(By.xpath("//a[@href='http://www.thebay.com/webapp/wcs/stores/servlet/en/thebay/search/womens-apparel']")); 
	    WebElement firstMenu = driver.findElement(By.xpath("//ul[@id='main_nav']/li[1]/a"));	    
	    Thread.sleep(3000);
	    //WebElement firstSubMenu = driver.findElement(By.xpath("//a[contains(@href, '/webapp/wcs/stores/servlet/en/thebay/search/womens-apparel/new-arrivals-women')]"));
	    WebElement firstSubMenu = driver.findElement(By.xpath("//div[@id='men2']/div[1]/ul[1]/li/a"));	 
	    Thread.sleep(3000);
	    Actions action = new Actions(driver);
	    action.moveToElement(firstMenu).perform();
	    Thread.sleep(3000);	    
	    action.click(firstSubMenu).perform(); 
      
	    // *Parameterizing test    using the data retrieved from <option>      for the brand name selection
	    // WOMEN>NEW ARRIVALS>SHOP BY BRAND
	    System.out.println("---------Starting to retrieve the data from <option>-----------");
        List<WebElement> elementOption = driver.findElements(By.xpath("//div[@id='left_select']/select/option"));   
        int num_option = elementOption.size();
        int num_brand = num_option -1; //The first option "Select a Brand" is not a brand name.
        System.out.println("Total brands: "+ num_brand);       
	   
        try {
	    	  
        int i;
        for (i=1; i<=num_brand; i++)
         {
         WebElement firstMenu2 = driver.findElement(By.xpath("//a[@href='http://www.thebay.com/webapp/wcs/stores/servlet/en/thebay/search/womens-apparel']"));
         WebElement firstSubMenu2 = driver.findElement(By.xpath("//div[@id='men2']/div[1]/ul[1]/li/a"));
         Thread.sleep(2000);
         Actions action2 = new Actions(driver);
         Thread.sleep(2000);
      	 action2.moveToElement(firstMenu2).perform();
      	 action2.click(firstSubMenu2).perform();
         List<WebElement> elementOption2 = driver.findElements(By.xpath("//div[@id='left_select']/select/option"));
         elementOption2.get(i).click(); 
         Thread.sleep(2000);
         
         String sBrandName = driver.findElements(By.xpath("//div[@id='left_select']/select/option")).get(i).getText();
         //String sBrandName = elementOption2.get(i).getText();
         String brand = sBrandName.trim(); 
         String h1Text = driver.findElement(By.xpath("//div[@id='right_con']/h1")).getText();
         Thread.sleep(2000);      
            
         String brandUrl = driver.getCurrentUrl();     
         brand.equals(h1Text); 
         
         System.out.println(i);  
         System.out.println(brand); 
         System.out.println(brandUrl); 
         driver.navigate().back();
         
         //*Store the data retrieved from <option> into the table woman_newarrivals_brand
         Connection conn = null;
         Class.forName("com.mysql.jdbc.Driver").newInstance();
         conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/test_thebay","root","guo");  
         Statement stmt = conn.createStatement();
         String sql = "INSERT INTO woman_newarrivals_brand values(" 
                      + i + ",'" + brand +"','"+ brandUrl +"')";
         //System.out.println(sql);             
         stmt.executeUpdate(sql);
         
         conn.close();
         stmt.close();
         }
        }
        catch(Exception e){System.out.println(e.toString());}                
        
	    //*Compare the data between two tables ( which one is woman_newarrivals_brand and 
        // the another one is used to store the original record of business requirement.
	    try{
	    System.out.println("----------Starting to compare the data---------------"); 
	    Connection conn = null; 
        Class.forName("com.mysql.jdbc.Driver").newInstance();  
        conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/test_thebay","root","guo");     
        Statement stmt = conn.createStatement();
        String sql = "SELECT W.id, W.brandName"
        		     + " FROM woman_newarrivals_brand W, origin_wn_brand O" 
                     + " where W.brandName = O.brandName";
        //System.out.println(sql);
        ResultSet res = stmt.executeQuery(sql);        
        res.first();
        res.last();
        int record_equal = res.getRow();
        System.out.println("Total records equaled between two tables are " + record_equal);
        
        if (record_equal!=3)  {System.out.println("The verification of Brand option is failed.");}
        else   {System.out.println("The verification of Brand option is passed");}
        
        conn.close();
        stmt.close();
        res.close();
	    }
	    catch(Exception e){System.out.println(e);} 
	   
	   driver.close();
       System.out.println("----------");
       System.out.println("Testing of WOMEN>NEW ARRIVALS>SHOP BY BRAND runs over");  
         
	
	}
}