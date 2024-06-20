package Sprint_35;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.codehaus.jettison.json.JSONObject;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.aventstack.extentreports.Status;
import com.fasterxml.jackson.core.JsonProcessingException;
//import com.aventstack.extentreports.Status;
//import com.aventstack.extentreports.Status;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import io.restassured.RestAssured;
import io.restassured.response.Response;

@Listeners(TestListener.class)
public class EPIC_1489 extends UtilClass {

	public static String sendkeys = "";
	public static String split;
	public static String attribute;

	@FindBy(xpath = "//iframe[@name='PegaGadget1Ifr']")
	public static WebElement frameName2;

	@FindBy(id = "loginText2")
	public static WebElement ssoLogin;

	@FindBy(className = "table-row")
	public static WebElement code;

	@FindBy(id = "idTxtBx_SAOTCC_OTC")
	public static WebElement send;

	@FindBy(id = "idSubmit_SAOTCC_Continue")
	public static WebElement click;

	@FindBy(xpath = "//div[contains(@class,'launch-portals')]/descendant::a")
	public static WebElement LaunchPortal;

	@FindBy(xpath = "//span[contains(text(),'WareHouse UserPortal')]")
	public static WebElement warehouse;

	@FindBy(xpath = "//li[@title='Orders PAR']")
	public static WebElement OrdersPAR;

	@FindBy(xpath = "//iframe[@name='PegaGadget0Ifr']")
	public static WebElement frameName;

	@FindBy(xpath = "//h3[contains(text(),'OS&D')]")
	public static WebElement OSD;

	@FindBy(xpath = "//h3[contains(text(),'Inbound Trailer')]")
	public static WebElement InboundTrailer;

	@FindBy(xpath = "//input[@CLASS='leftJustifyStyle']")
	public static WebElement OrderSearch_Filer;

	@FindBy(xpath = "(//input[@type='checkbox' and contains(@name,'Inbound')])[21]")
	public static WebElement FilterCheckBox;

	@FindBy(xpath = "//th[@aria-label='Order ID']//a[@class='filter highlight-ele']")
	public static WebElement OrderFilter;

	@FindBy(xpath = "(//button[@class='pzhc pzbutton'])[1]")
	public static WebElement OrderFilterApply;

	@FindBy(xpath = "//a[contains(text(), 'PAR')]")
	public static WebElement ClickPARCaseID;

	@FindBy(xpath = "//button[@title='Toggle runtime toolbar']//i[contains(@class,'gear')]")
	public static WebElement Toggle_toolbar;

	@FindBy(xpath = "//button[@title='Clipboard']")
	public static WebElement CLickOnClipBoard;

	@FindBy(xpath = "(//span[contains(@title, 'pyWorkPage')])[1]")
	public static WebElement ClickonpyworkPage;

	@FindBy(xpath = "(//a[@title='Expand to show child rows'])[29]")
	public static WebElement ExpandpyworkPage;

	@FindBy(xpath = "//span[contains(text(), 'OrderPage')]")
	public static WebElement ClickonOrderpageinCipboard;

	@FindBy(xpath = "(//a[@title='Expand to show child rows'])[29]")
	public static WebElement ExpandOrderPageinClipboard;

	@FindBy(xpath = "(//a[@title='Expand to show child rows'])[37]")
	public static WebElement ExpandShipmentinCLipboard;

	@FindBy(xpath = "//span[contains(text(), 'ShipmentList(2)')]")
	public static WebElement ClickonShipment2inCLipboard;

	@FindBy(xpath = "//span[contains(text(),'Scan Tool Order API')]")
	public static WebElement ScanToolStatus_Validation;

	public EPIC_1489(WebDriver driver) {
		PageFactory.initElements(driver, this);
	}

	public static void frameSwitch() {
		driver.switchTo().frame(frameName);

	}

	public void Order_Creation() throws Exception {

		String dirpath = System.getProperty("user.dir");
		File file = new File(dirpath + "//PAR.json");
		ObjectMapper mapper = new ObjectMapper();
		JsonNode node = mapper.readTree(file);
		String actual = node.toPrettyString();
		JSONObject jsonObject = new JSONObject(actual);
		JSONObject orderRefs = jsonObject.getJSONObject("OrderRefs");

		// ObjectNode orderRefs = (ObjectNode) objectNode.get("OrderRefs");
//		double ceil = Math.ceil(Math.random() * 100000000);
//		BOL_Order = Double.toString(ceil);
		orderRefs.put("BOL", Math.ceil(Math.random() * 100000000));
		orderRefs.put("TrackingNumber", Math.ceil(Math.random() * 100000000));
		orderRefs.put("InvoiceNumber", Math.ceil(Math.random() * 100000000));

		RestAssured.baseURI = "https://epicuatlb.estes-express.com";
		Response response = RestAssured.given().auth().basic("EpicSevicesTest1", "Rules@1234")
				.contentType("application/json").body(jsonObject.toString())
				.post("/prweb/api/OrderServicePackage/V1/CreateOrUpdateOrder");
		String responseBody = response.getBody().asString();
		String[] split = responseBody.split("Reference is ");

		sendkeys = split[1];
		System.out.println(sendkeys);
		System.out.println("<------Result of PAR Json------>");
		System.out.println("Response Body: " + responseBody);
		int statusCode = response.getStatusCode();
		System.out.println("Status Code: " + statusCode);
		extentTest.log(Status.PASS, "Order Created");

	}

	public void PEGALogin() throws InterruptedException {
		Await();
		ssoLogin.click();
		waits(code);
		code.click();

		// Scanner class to handle OTP
		String scanner = scanner();
		send.sendKeys(scanner);
		waits(click);
		click.click();
		extentTest.log(Status.PASS, "Entering PEGA");

	}

	public void LaunchWarehousePortal() throws InterruptedException {
		waits(LaunchPortal);
		LaunchPortal.click();
		waits(warehouse);
		warehouse.click();
		Await();
		Windows();
		OrdersPAR.click();
		extentTest.log(Status.PASS, "Entering Orders PAR");
		Await();
		frameSwitch();
		waits(OSD);
		OSD.click();
		waits(InboundTrailer);

	}

	public void OrdersPAR() throws Exception {
		Await();
		OrdersPAR.click();
		extentTest.log(Status.PASS, "Entering Orders PAR");
		Await();
		frameSwitch();
		waits(OSD);
		OSD.click();
		waits(InboundTrailer);
		Await();
		Thread.sleep(9000);

	}

	public void InboundTrailer_WorkQueue() throws InterruptedException {
		InboundTrailer.click();
		Await();
		OrderFilter.click();
		waits(OrderSearch_Filer);
		OrderSearch_Filer.sendKeys(sendkeys);
//		waits(FilterCheckBox);
//		FilterCheckBox.click();
		waits(OrderFilterApply);
		OrderFilterApply.click();
		Await();

		ClickPARCaseID.click();
		extentTest.log(Status.PASS, "Order filtered");
		Await();
		driver.switchTo().defaultContent();
		// driver.switchTo().frame(frameName2);

		Await();
		// Inbound_trailer_outboundLoads_Status.click();
		// driver.switchTo().defaultContent();
	}

	public void Clipboard_Validation() throws Exception {
		Thread.sleep(7000);
		Toggle_toolbar.click();
		waits(CLickOnClipBoard);
		CLickOnClipBoard.click();
		extentTest.log(Status.PASS, "Entering into Clipboard");
		Await();
		Windows();
		Await();
		ScrollDown();
		Await();
//				pyWorkPage.click();	

		waits(ClickonpyworkPage);
		ClickonpyworkPage.click();
		Await();
//				waits(ExpandpyworkPage);
//				ExpandpyworkPage.click();
		// ScrollDown();
//				Await();
//				waits(ExpandOrderPageinClipboard);
//				ExpandOrderPageinClipboard.click();

		// System.out.println("Entered into OrderPage");
		// ScrollDown();
//				Await();
//				waits(ExpandShipmentinCLipboard);
//				ExpandShipmentinCLipboard.click();
//				System.out.println("Entered into Shipment");

//				waits(ClickonShipment2inCLipboard);
//				ClickonShipment2inCLipboard.click();

		String text = ScanToolStatus_Validation.getText();
		System.out.println(text);
		Assert.assertTrue(text.contains("Scan Tool Order API Success"));
		System.out.println("Successfully Validated Status:" + text);
		extentTest.log(Status.PASS, "Successfully validated Scan Tool Order API");

//	String text = ArrivedAtTerminal.getText();
//		assertFalse(text.contains("PICKUP SCHEDULED"));
//		System.out.println("Successfully Validated Status:" + text);
//		SelectClass(Inbound_trailer_outboundLoads_Status, "Received");
// 
//		clickIgnoringStaleElementException(ClickonCalendar);
//		// ClickonCalendar.click();
//		Await();
//		Calendarss();
// 
//		waits(Submit);
//		Submit.click();
//		waits(ClickonGo);
//		ClickonGo.click();
//		System.out.println("Status: received");
// 
//		Await();
//		driver.switchTo().defaultContent();
//		frameswitch2();
//		Await();

	}

}
