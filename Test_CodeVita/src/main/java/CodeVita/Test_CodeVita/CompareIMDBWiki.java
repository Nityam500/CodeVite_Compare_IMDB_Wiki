package CodeVita.Test_CodeVita;


import java.time.Duration;
import java.time.LocalDate;
import org.testng.Assert;
import org.testng.annotations.Test;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.github.bonigarcia.wdm.WebDriverManager;

public class CompareIMDBWiki {
	
	
	static ChromeDriver openURL(String URL) {    
		WebDriverManager.chromedriver().setup();
		ChromeDriver driver = new ChromeDriver();
		driver.manage().window().maximize();
		driver.get(URL);
		return driver;
	}
	
	
	static String filterIMDBDate(String str){
		DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("MMMM dd, yyyy");  //Input format
		LocalDate date = LocalDate.parse(str, inputFormatter);   //Parse input string
		
		DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd"); //Output format
		String output = date.format(outputFormatter); //convert final date to string
		
		return output;
	}
	
	
	static String getIMDBInfo(String movieName) throws InterruptedException {
		ChromeDriver driver = openURL("https://www.imdb.com/");
		
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
		
		WebElement search = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='suggestion-search']")));
		search.sendKeys(movieName);
		
		WebElement searchButton = driver.findElement(By.xpath("//*[@id='iconContext-magnify']"));
		searchButton.click();
		
		WebElement findMovieName = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[contains(text(),'"+movieName+"')]")));
		findMovieName.click();
		
		String IMDBreleaseDate1 = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@data-testid='title-details-releasedate']/div/ul/li/a"))).getAttribute("innerHTML");
		
		int startIndex = IMDBreleaseDate1.indexOf("(");
		String IMDBreleaseDate = IMDBreleaseDate1.substring(0, startIndex).trim();
		
		String IMDBreleaseCountry = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@data-testid='title-details-origin']/div/ul/li/a"))).getAttribute("innerHTML");
		
		driver.quit();
		
		return IMDBreleaseDate+"|"+IMDBreleaseCountry;
	}
	
	
	static String getWikiInfo(String movieName) throws InterruptedException {
		movieName = movieName.replaceAll(" ", "_");
		
		ChromeDriver driver = openURL("https://en.wikipedia.org/wiki/"+movieName);
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
		
		WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@class='bday dtstart published updated']")));
		List<WebElement> WikiReleaseDateList = driver.findElements(By.xpath("//*[@class='bday dtstart published updated']"));
		
		String WikiReleaseDate = WikiReleaseDateList.get(WikiReleaseDateList.size()-1).getAttribute("innerHTML");
		String WikiReleaseCountry = driver.findElement(By.xpath("//*[contains(text(),\"Country\")]/following-sibling::td[1]")).getAttribute("innerHTML");
		
		driver.quit();
		
		return WikiReleaseDate+"|"+WikiReleaseCountry;
	}
	
	
	@Test
	public void compareResult() throws InterruptedException{
		String movieName = "Pushpa: The Rise";
		String IMDBInfo =  getIMDBInfo(movieName),wikiInfo = getWikiInfo(movieName);
		
		String[] IMDB = IMDBInfo.split("\\|");
		String[] Wiki = wikiInfo.split("\\|");
		
		String IMDBdate = filterIMDBDate(IMDB[0]), IMDBCountry = IMDB[1];
		String Wikidate = Wiki[0], WikiCountry =Wiki[1];
		
		Assert.assertEquals(IMDBdate,Wikidate,"Date Mismatch");
		Assert.assertEquals(IMDBCountry,WikiCountry,"CountryMismatch");
		
	}
}
