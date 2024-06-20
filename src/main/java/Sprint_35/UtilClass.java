package Sprint_35;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Calendar;
import java.util.Scanner;
import java.util.Set;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

import io.github.bonigarcia.wdm.WebDriverManager;
@Listeners(TestListener.class)
public class UtilClass {

	public static WebDriver driver;
	private static Scanner scanner;
	public static ExtentReports reports;
	public static ExtentTest extentTest;

	@BeforeClass
	public void extentReportInitialization() {
		String path = System.getProperty("user.dir");
		ExtentSparkReporter reporter = new ExtentSparkReporter(path + "/Test Report/testReport_Sprint35.html");
		reports = new ExtentReports();
		reports.attachReporter(reporter);
	}

	@BeforeMethod
	public static void BrowserLaunch() {
	//	WebDriverManager.chromedriver().setup();
		driver = new ChromeDriver();
		driver.get("https://epicuatlb.estes-express.com/prweb/PRAuth/app/epic/vQbSpdBva3zBDbMlMRMsHQ*/!STANDARD");
		driver.manage().window().maximize();

	}

//	@AfterMethod
//	public void browser() {
//		driver.close();
//		driver.quit();
//	}
	public static void init(Object page) {
		PageFactory.initElements(driver, page);
	}

	public static String scanner() {
		scanner = new Scanner(System.in);
		String otp = scanner.nextLine();
		System.out.println(otp + " is the received OTP");
		return otp;

	}

	public static void Windows() {
		String handle = driver.getWindowHandle();
		Set<String> allwindow = driver.getWindowHandles();

		for (String eachwindow : allwindow) {
			if (!eachwindow.equals(handle)) {
				driver.switchTo().window(eachwindow);
			}
		}

	}

	public static void waits(WebElement element) {

		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
		wait.until(ExpectedConditions.visibilityOf(element));
	}

	public static void SelectClass(WebElement status, String Dropdown) {
		try {
			Select SelectStatus = new Select(status);
			SelectStatus.selectByVisibleText(Dropdown);
		} catch (StaleElementReferenceException e) {
			Select SelectStatus = new Select(status);
			SelectStatus.selectByVisibleText(Dropdown);
		}
	}

	public static void SelectClass_Reson(WebElement status2, String Reason_Dropdown) {
		Select SelectStatus2 = new Select(status2);
		SelectStatus2.selectByVisibleText(Reason_Dropdown);

	}

	public static void Calendarss() {
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat format = new SimpleDateFormat("d");
		String date = format.format(calendar.getTime());
		WebElement CalClick = driver.findElement(By.xpath("//a[text()= '" + date + "']"));
		CalClick.click();

	}

	public static void ScrollDown() {
		JavascriptExecutor ch = (JavascriptExecutor) driver;
		ch.executeScript("window.scrollBy(0,220)", " ");
		ch.executeScript("window.scrollBy(0,220)");
	}

//	public static void awaitility(WebElement element) {
//		Awaitility.await().atMost(Duration.ofSeconds(5)).untilAsserted(() -> Assert.assertTrue(element.isDisplayed()));
//	}

	public static void Await() throws InterruptedException {
		Thread.sleep(3000);
	}

	public static void clickIgnoringStaleElementException(WebElement element) {
		int attempt = 0;
		while (attempt < 3) {
			try {
				element.click();
				break;
			} catch (StaleElementReferenceException e) {
				attempt++;
			}
		}
	}

	public static void getIgnoringStaleElementException(WebElement element) {
		int attempt = 0;
		while (attempt < 3) {
			try {
				element.getText();
				break;
			} catch (StaleElementReferenceException e) {
				attempt++;
			}
		}
	}

	public String getdate(String format) {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat form = new SimpleDateFormat(format);
		return form.format(cal.getTime());
	}

	public static void ScrollDown2() {
		JavascriptExecutor ch = (JavascriptExecutor) driver;
		ch.executeScript("window.scrollBy(0,50)", " ");
		ch.executeScript("window.scrollBy(0,50)");
	}

	public static void sendKeysJavascript(WebDriver driver, WebElement textArea, String filePath) {

		try {
			// Read the content of the XML file
			String fileContent = new String(Files.readAllBytes(Paths.get(filePath)));
			System.out.println(fileContent);

			// Paste the XML content into the text area using JavaScript executor
			JavascriptExecutor executor = (JavascriptExecutor) driver;
			executor.executeScript("arguments[0].value = arguments[1];", textArea, fileContent);

			// Sleep for some time to observe the result (for demonstration purposes)
			Thread.sleep(120000); // 2 minutes
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
	}
}
