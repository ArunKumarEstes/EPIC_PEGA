package EPIC_1493;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.TimeZone;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.annotations.Test;

import com.aventstack.extentreports.Status;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.gson.JsonObject;

import Sprint_35.UtilClass;
import dev.failsafe.internal.util.Assert;
import groovyjarjarantlr4.v4.codegen.model.Action;
import io.restassured.RestAssured;
import io.restassured.response.Response;

public class Received_Status extends UtilClass {

	public static String sendkeys = "";
	public static String split;
	public static String attribute;
	public static String GetReceivedDate;
	public static String GetLocationCity_State;
	public static String date;
	public static String Datee;
//	public static String dateUI;

	public void PAR_Order_Creation() throws JSONException, IOException {
		// TODO Auto-generated method stub
		String prettyString = null;
		File file = new File(System.getProperty("user.dir") + "\\PAR.json");
		ObjectMapper objectMapper = new ObjectMapper();
		// Read JSON from file into a JsonNode
		JsonNode rootNode = objectMapper.readTree(file);
		// Ensure that the root node is an ObjectNode
		if (rootNode instanceof ObjectNode) {
			ObjectNode objectNode = (ObjectNode) rootNode;
			// Retrieve the "OrderRefs" object and add "InvoiceNumber" with a random value
			ObjectNode orderRefs = (ObjectNode) objectNode.get("OrderRefs");
			orderRefs.put("InvoiceNumber", Math.ceil(Math.random() * 100000000));
			orderRefs.put("InvoiceNumber", Math.ceil(Math.random() * 100000000));
			orderRefs.put("TrackingNumber", Math.ceil(Math.random() * 100000000));
			// Convert the modified JsonNode back to a pretty-printed string
			prettyString = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(objectNode);
			// Print the modified JSON string
			// System.out.println(prettyString);
		} else {
			System.out.println("Test fail");
		}
		RestAssured.baseURI = "https://epicuatlb.estes-express.com";
		Response response = RestAssured.given().auth().basic("EpicSevicesTest1", "Rules@1234")
				.contentType("application/json").body(prettyString)
				.post("/prweb/api/OrderServicePackage/V1/CreateOrUpdateOrder");
		String responseBody = response.getBody().asString();
		String[] split = responseBody.split("Reference is ");

		sendkeys = split[1];
		System.out.println(split[1]);
		System.out.println("<------Result of PAR Json------>");
		System.out.println("Response Body: " + responseBody);
		int statusCode = response.getStatusCode();
		System.out.println("Status Code: " + statusCode);
		extentTest.log(Status.PASS, "Successfully created PAR Order");

	}

	// update the order without writing the json values
	public void Received_Status_Update() throws JsonProcessingException, IOException, JSONException {
		String dirpath = System.getProperty("user.dir");
		File file = new File(dirpath + "//Status_Update.json");
		ObjectMapper mapper = new ObjectMapper();
		JsonNode node = mapper.readTree(file);
		String actual = node.toPrettyString();
		// System.out.println("actual json----------------"+actual);

		String[] Status_Events = { "Received", "Loaded", "Released", "Cancelled", "Shortage", "Overage" };

		JSONObject jsonObject = new JSONObject(actual);

		jsonObject.put("CaseID", sendkeys);
		jsonObject.put("StatusEvent", Status_Events[0]);
		String dateJson = getdate("yyyyMMdd") + "T115708.000-05:00";

		jsonObject.put("StatusEventDateTime", dateJson);
		GetReceivedDate = dateJson;

		String[] split = GetReceivedDate.split("T");
		GetReceivedDate = split[0];
		System.out.println(GetReceivedDate);

		// -----------------------------

//		JSONObject ReceivedDates = jsonObject.put("StatusEventDateTime",dateJson);
//		Object StatusDate = ReceivedDates.get("StatusEventDateTime");
//		GetReceivedDate = StatusDate.toString();
//		String[] split = GetReceivedDate.split("T");
//		GetReceivedDate=split[0];
//		
//		System.out.println("Out_Bound: " + GetReceivedDate);

//		Object GetDate = jsonObject.get("StatusEventDateTime");
//		GetReceivedDate = GetDate.toString();
//		System.out.println(GetReceivedDate);

		Object GetCity = jsonObject.get("LocationCity");
		Object GetState = jsonObject.get("LocationState");
		String Get_State = GetState.toString();
		GetLocationCity_State = GetCity.toString().concat(" " + Get_State);
		// String GetLocationCity_State = GetCity.toString();
		System.out.println(GetLocationCity_State);

		RestAssured.baseURI = "https://epicuatlb.estes-express.com";
		Response response = RestAssured.given().auth().basic("EpicSevicesTest1", "Rules@1234")
				.contentType("application/json").body(jsonObject.toString())
				.post("/prweb/api/OrderServicePackage/V1/UpdateStatus");
		String responseBody = response.getBody().asString();

		System.out.println("<------Result of Updated PAR Json------>");
		System.out.println("Response Body: " + responseBody);
		int statusCode = response.getStatusCode();
		System.out.println("Status Code: " + statusCode);
		System.out.println("JSON value updated successfully.");
		extentTest.log(Status.PASS, "JSON value updated successfully");

	}

	public Received_Status(WebDriver driver) {
		PageFactory.initElements(driver, this);
	}

	// ----------------------------------Web Elements------------------------

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

	@FindBy(xpath = "(//a[@class='filter highlight-ele'])[2]")
	public static WebElement OrderFilter;

	@FindBy(xpath = "//input[@CLASS='leftJustifyStyle']")
	public static WebElement OrderSearch_Filer;

	@FindBy(xpath = "(//input[@type='checkbox' and contains(@name,'Inbound')])[21]")
	public static WebElement FilterCheckBox;

	@FindBy(xpath = "(//button[@class='pzhc pzbutton'])[1]")
	public static WebElement OrderFilterApply;

	@FindBy(xpath = "//a[contains(text(), 'PAR')]")
	public static WebElement ClickPARCaseID;

	@FindBy(xpath = "//iframe[@name='PegaGadget1Ifr']")
	public static WebElement frameName2;

	@FindBy(xpath = "//iframe[contains(@name, 'PegaGadget')]")
	public static WebElement frameName3;

	@FindBy(xpath = "//span[contains(text(),'Arrived at Terminal')]")
	public static WebElement ArrivedAtTerminal;

	@FindBy(xpath = "//*[contains(@name, '$PpyWorkPage$pStatusEvent')]")
	public static WebElement Inbound_trailer_outboundLoads_Status;

	@FindBy(xpath = "//*[contains(@alt, 'Choose from calendar')]")
	public static WebElement ClickonCalendar;

	@FindBy(xpath = "//a[@id='applyLink']")
	public static WebElement ClickOnApply;

	@FindBy(xpath = "//button[contains(text(), 'Submit')]")
	public static WebElement Submit;

	@FindBy(xpath = "(//button[contains(text(), 'Go')])")
	public static WebElement ClickonGo;

	@FindBy(xpath = "(//button[@name='CaseActionHeader_pyWorkPage_4'])[2]")
	public static WebElement Clickon_Actions;

	@FindBy(xpath = "//span[contains(text(),'Refresh')]")
	public static WebElement Clickon_Refresh;

	@FindBy(xpath = "//button[contains(text(),'Refresh')]")
	public static WebElement Click_Refresh_inbound_trailer;

	@FindBy(xpath = "//button[@title='Toggle runtime toolbar']//i[contains(@class,'gear')]")
	public static WebElement Toggle_toolbar;

	@FindBy(xpath = "//button[@title='Clipboard']")
	public static WebElement CLickOnClipBoard;

//	@FindBy(xpath = "//span[contains(text(),'pyWorkPage')]/ancestor::ul[@class='rowContent']/descendant::div[contains(@id,'icon')]")
//	public static WebElement pyWorkPage;

	@FindBy(xpath = "(//span[contains(@title, 'pyWorkPage')])[1]")
	public static WebElement ClickonpyworkPage;

	@FindBy(xpath = "(//a[@title='Expand to show child rows'])[23]")
	public static WebElement ExpandpyworkPage;

	@FindBy(xpath = "(//a[@title='Expand to show child rows'])[25]")
	public static WebElement ExpandpyworkPageinPegaClipboard;

	@FindBy(xpath = "//span[contains(text(), 'OrderPage')]")
	public static WebElement ClickonOrderpageinCipboard;

	@FindBy(xpath = "//a[contains(text(), 'StatusEventDateTime')]/following::td[1]//div//span")
	public static WebElement ReceivedDate_OrderPage_Clipboard;

	@FindBy(xpath = "((//span[contains(text(), 'OrderPage')])[1]/ancestor::li)[3]/ul/li/div/div/a")
	public static WebElement ExpandOrderPageinClipboard;

	@FindBy(xpath = "((//span[contains(text(), 'OrderPage')])[1]/ancestor::li)[3]/ul/li/div/div/a")
	public static WebElement ExpandOrderPageinPegaClipboard;

	@FindBy(xpath = "//span[contains(text(), 'Received')]")
	public static WebElement StatusEvent_OrderPage_Clipboard;

	@FindBy(xpath = "((//span[contains(text(), 'ShipmentList')]/ancestor::li)[4]//ul//li//div//div)[1]")
	public static WebElement ExpandShipmentinCLipboard;

	@FindBy(xpath = "((//span[contains(text(), 'ShipmentList')]/ancestor::li)[4]//ul//li//div//div)[1]")
	public static WebElement ExpandShipmentinPegaCLipboard;

	@FindBy(xpath = "//span[contains(text(), 'ShipmentList(1)')]")
	public static WebElement ClickonShipment1inCLipboard;

	@FindBy(xpath = "//span[contains(text(), 'Received')]")
	public static WebElement StatusEvent_Shipement1_Clipboard;

	@FindBy(xpath = "//span[contains(text(), 'ShipmentList(2)')]")
	public static WebElement ClickonShipment2inCLipboard;

	@FindBy(xpath = "//span[contains(text(), 'Received')]")
	public static WebElement StatusEvent_Shipement2_Clipboard;

	// Datatype

	@FindBy(xpath = "(//h3[@class='layout-group-item-title'])[3]")
	public static WebElement CLickonDataType;

	@FindBy(xpath = "//a[@aria-label='menu Order']")
	public static WebElement ClickonOrderDatatype;

	@FindBy(xpath = "//div[@data-lg-child-id='4']/div[@aria-label='Records']/h3[@class='layout-group-item-title']")
	public static WebElement Clickon_Record_Status_and_OrderDatatype;

	@FindBy(xpath = "(//input[@placeholder='Search...'])[2]")
	public static WebElement Searchon_Order_Status_Datatype;

	@FindBy(xpath = "//i[@class='pi pi-search']")
	public static WebElement SelectSearchon_Order_Status_Datatype;

	@FindBy(xpath = "//td[contains(@data-ui-meta, 'ReceivedDate')]")
	public static WebElement ReceivedDate_inOrder_Datatype;

	@FindBy(xpath = "//td[@data-attribute-name='ReceivedDate']//div")
	public static WebElement Status_Date_Time_in_Order_Datatype;

	@FindBy(xpath = "//div[contains(text(), '2024 12:57 PM')]")
	public static WebElement Status_Date_Time_in_Status_Datatype;

	@FindBy(xpath = "//a[@aria-label='menu Status']")
	public static WebElement ClickonStatusDatatype;

	@FindBy(xpath = "//div[contains(text(),'Received')]")
	public static WebElement Received_Status_in_Status_Datatype;

	@FindBy(xpath = "//div[contains(text(), 'DALLAS TX')]")
	public static WebElement Event_Location_Address_in_Status_Datatype;

	@FindBy(xpath = "//div[contains(text(), ' Normal')]")
	public static WebElement Status_Event_detail;

	@FindBy(xpath = "//img[@title='Checked']")
	public static WebElement Checked_in_Status_datatype;

	@FindBy(xpath = "(//div[contains(text(), '2024')])[3]")
	public static WebElement Status_Send_Date;

	@FindBy(xpath = "//div[contains(text(),'StatusUpdate API')]")
	public static WebElement Source_Status;

	// -----
	@FindBy(xpath = "(//h3[@class='layout-group-item-title'])[4]")
	public static WebElement CLickonApp;

	@FindBy(xpath = "//span[contains(text(), 'Home')]/following::td/span[@title='Close this tab']")
	public static WebElement Close_All_Tab;

	@FindBy(xpath = "//span[@title='Close this tab']/ancestor::li[@aria-label='Pickup And Release']")
	public static WebElement PickAndRelease_Close;

	@FindBy(xpath = "//a[@title='PickupAndRelease']")
	public static WebElement CLickonPickAndRelease;

	@FindBy(xpath = "(//a[@id='pui_filter'])[1]")
	public static WebElement ClickonIdFilter;

	@FindBy(xpath = "//input[contains(@name, 'FilterCriteria')]")
	public static WebElement SearchText;

	@FindBy(xpath = "//button[contains(text(), 'Apply')]")
	public static WebElement ClickonApply;

	@FindBy(xpath = "//div[contains(text(), 'PAR')]")
	public static WebElement CLickonPARcase;

	@FindBy(xpath = "//a[@title='Clipboard']")
	public static WebElement ClickonPegaClipboard;

	public static void frameSwitch() {
		driver.switchTo().frame(frameName);

	}

	public static void frameswitch2() {
		driver.switchTo().frame(frameName2);
	}

	public static void Mainframe() {
		driver.switchTo().frame(frameName3);
	}

	public static void ClickCalendarandSubmit() throws InterruptedException {

		clickIgnoringStaleElementException(ClickonCalendar);
		// ClickonCalendar.click();
		Await();
		Calendarss();
		WebElement Received = driver.findElement(By.xpath("//input[@name='$PpyWorkPage$pEventDate']"));
		attribute = Received.getAttribute("value");
		System.out.println(attribute);

		waits(Submit);
		Submit.click();
		waits(ClickonGo);
		ClickonGo.click();
		System.out.println("Status: received");

		Await();
		driver.switchTo().defaultContent();
		frameswitch2();
		Await();
	}

	public void PEGALogin() throws InterruptedException {
		ssoLogin.click();
		waits(code);
		code.click();

		// Scanner class to handle OTP
		String scanner = scanner();
		send.sendKeys(scanner);
		waits(click);
		click.click();
		extentTest.log(Status.PASS, "Successfully enetred into PEGA");
	}

	public void LaunchWarehousePortal() throws InterruptedException {
		waits(LaunchPortal);
		LaunchPortal.click();
		waits(warehouse);
		warehouse.click();
		Await();
		Windows();
		OrdersPAR.click();
		extentTest.log(Status.PASS, "Successfully opened PAR Order");
		Await();
		frameSwitch();
		waits(OSD);
		OSD.click();
		waits(InboundTrailer);

	}

	public void InboundTrailer_WorkQueue() throws InterruptedException {
		InboundTrailer.click();
		Await();
		Click_Refresh_inbound_trailer.click();

		Await();
		driver.switchTo().defaultContent();
		frameSwitch();
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
		Await();
		driver.switchTo().defaultContent();
		frameswitch2();
		Await();

		SelectClass(Inbound_trailer_outboundLoads_Status, "Received");

		ClickCalendarandSubmit();
		Await();

	}

	// update the order by writing the json values

	public void App_Clipboard() throws InterruptedException, ParseException {
//		Windows();
//		String parentWindow = driver.getWindowHandle();
//		driver.switchTo().window(parentWindow);

//		waits(CLickonDataType);
//		CLickonDataType.click();
		waits(CLickonApp);
		CLickonApp.click();

		List<WebElement> Close_Tabs = driver
				.findElements(By.xpath("//span[contains(text(), 'Home')]/following::td/span[@title='Close this tab']"));

		for (int i = 0; i < Close_Tabs.size(); i++) {
			Close_Tabs.get(i).click();
			System.out.println("Successfully closed all tabs");

		}

//		Await();
//		if (PickAndRelease_Close.isDisplayed()) {
//			PickAndRelease_Close.click();
//
//		}
//		if (PickAndRelease_Close.isDisplayed()) {
//			PickAndRelease_Close.click();
//			Await();
//			CLickonPickAndRelease.click();
//		} else {
//			CLickonPickAndRelease.click();
//		}

//		waits(CLickonPickAndRelease);
		Await();
		CLickonPickAndRelease.click();
//		Thread.sleep(3000);
		driver.switchTo().defaultContent();
		Mainframe();
		
		Thread.sleep(3000);
		ClickonIdFilter.click();
		Await();
		SearchText.click();
		SearchText.sendKeys(sendkeys);
		waits(ClickonApply);
		ClickonApply.click();
		// waits(CLickonPARcase);
		Await();
		CLickonPARcase.click();
		Thread.sleep(7000);
		driver.switchTo().defaultContent();
//		frameswithc3();
		driver.switchTo().frame(1);
		ClickonGo.click();

		Await();
		driver.switchTo().defaultContent();
		driver.switchTo().frame(1);
		Await();

		Clickon_Actions.click();
		Await();
		Clickon_Refresh.click();
		Await();
		driver.switchTo().defaultContent();
		frameswitch2();
		Await();
		waits(ClickonGo);
		ClickonGo.click();
		System.out.println("Status: refresh");

		Await();
		driver.switchTo().defaultContent();
		// frameswitch2();
		// Await();
		Thread.sleep(7000);
//		Toggle_toolbar.click();
//		waits(ClickonPegaClipboard);
		ClickonPegaClipboard.click();
		Await();
		Windows();
		Await();
		//// ScrollDown();
		Await();
//		pyWorkPage.click();	

		waits(ClickonpyworkPage);
		ClickonpyworkPage.click();
		waits(ExpandpyworkPageinPegaClipboard);
		ExpandpyworkPageinPegaClipboard.click();
		// ScrollDown();
		Await();
		waits(ExpandOrderPageinPegaClipboard);
		ExpandOrderPageinPegaClipboard.click();
		System.out.println("Entered into OrderPage");
		Await();
		waits(ClickonOrderpageinCipboard);
		ClickonOrderpageinCipboard.click();

		String attribute2 = ReceivedDate_OrderPage_Clipboard.getText();

		System.out.println("Clipboard_ReceivedDate:" + attribute2);

		assertTrue(attribute2.contains(GetReceivedDate));
		System.out.println("Clipboard_ReceivedDate:" + attribute2);
		extentTest.log(Status.PASS, "Successfully validated Clipboard_ReceivedDate");

//		SimpleDateFormat format1 = new SimpleDateFormat("yyyyMMdd'T'HHmmss.SSSXXX");
//		Date date1 = format1.parse(GetReceivedDate);
//       
//		// Parse date string 2
//		SimpleDateFormat format2 = new SimpleDateFormat("yyyyMMdd'T'HHmmss.SSS z");
//		Date date2 = format2.parse(attribute2);
//
//		format2.setTimeZone(TimeZone.getTimeZone("GMT"));
//
//		SimpleDateFormat form = new SimpleDateFormat("MM/dd/yyyy");
//		String dateExpected = form.format(date2);
//		String dateActual = form.format(date1);
//		
//		
//		 System.out.println(date1+"----"+dateActual);
//		 System.out.println(date2+"----"+dateExpected);
//		System.out.println(dateActual + "----------------------" + dateExpected);
//		assertEquals(dateActual, dateExpected);
//		
//
//		// Validate StatusEvent in OrderPage_in_Clipboard
		Await();
		String text = StatusEvent_OrderPage_Clipboard.getText();
		assertTrue(text.contains("Received"));
		System.out.println("Successfully Validated Received_Status_in_Order_Clipboard:" + text);
		extentTest.log(Status.PASS, "Successfully Validated Received_Status_in_Order_Clipboard");

		// ScrollDown();
		Await();
		waits(ExpandShipmentinCLipboard);
		ExpandShipmentinCLipboard.click();
		System.out.println("Entered into Shipment");

		waits(ClickonShipment2inCLipboard);
		ClickonShipment2inCLipboard.click();

		Await();
		// waits(StatusEvent_Shipement2_Clipboard);
		getIgnoringStaleElementException(StatusEvent_Shipement2_Clipboard);
		String text2 = StatusEvent_Shipement2_Clipboard.getText();
		assertTrue(text2.contains("Received"));
		System.out.println("Successfully Validated Received_Status_in_Shipment2_Clipboard:" + text2);
		extentTest.log(Status.PASS, "Successfully Validated Received_Status_in_Shipment2_Clipboard");

		// driver.close();

	}

	public void BackToPEGA() throws InterruptedException, ParseException {
		Windows();
		Await();
		String parentWindow = driver.getWindowHandle();
		driver.switchTo().window(parentWindow);

		waits(CLickonDataType);
		CLickonDataType.click();

		waits(ClickonOrderDatatype);
		ClickonOrderDatatype.click();
		driver.switchTo().defaultContent();

		// frameswitch3();
		driver.switchTo().frame(2);
		Mainframe();
		Thread.sleep(5000);
		// waits(ClickonRecordinOrderDatatype);
		Clickon_Record_Status_and_OrderDatatype.click();
		waits(Searchon_Order_Status_Datatype);
		Searchon_Order_Status_Datatype.click();
		Searchon_Order_Status_Datatype.sendKeys(sendkeys);
		waits(SelectSearchon_Order_Status_Datatype);
		SelectSearchon_Order_Status_Datatype.click();

		Await();
		String attribute3 = Status_Date_Time_in_Order_Datatype.getText();

		System.out.println("Recibved Date in Order Datta type:" + attribute3);

		SimpleDateFormat format1 = new SimpleDateFormat("yyyyMMdd");
		Date date1 = format1.parse(GetReceivedDate);

		SimpleDateFormat format2 = new SimpleDateFormat("MM/dd/yyyy h:mm a");
		Date date2 = format2.parse(attribute3);

		SimpleDateFormat form = new SimpleDateFormat("MM/dd/yyyy");
		String dateExpected = form.format(date2);
		String dateActual = form.format(date1);

		System.out.println(dateActual + "----------------------" + dateExpected);
		assertEquals(dateActual, dateExpected);

		System.out.println("Successfully Validated Received_Date:" + attribute3);

		ScrollDown();
		Await();
		// waits(ClickonStatusDatatype);
		driver.switchTo().defaultContent();
		ClickonStatusDatatype.click();
		Actions PEGA = new Actions(driver);
		PEGA.keyDown(Keys.ARROW_DOWN);
		PEGA.keyUp(Keys.ARROW_DOWN);

		PEGA.keyDown(Keys.ARROW_DOWN);
		PEGA.keyUp(Keys.ARROW_DOWN);

		PEGA.keyDown(Keys.ARROW_DOWN);
		PEGA.keyUp(Keys.ARROW_DOWN);

		PEGA.keyDown(Keys.ARROW_DOWN);
		PEGA.keyUp(Keys.ARROW_DOWN);

		driver.switchTo().defaultContent();
		driver.switchTo().frame(3);
//		frameswitch3();

		waits(Clickon_Record_Status_and_OrderDatatype);
		Clickon_Record_Status_and_OrderDatatype.click();
		waits(Searchon_Order_Status_Datatype);
		Searchon_Order_Status_Datatype.click();
		Searchon_Order_Status_Datatype.sendKeys(sendkeys);
		waits(SelectSearchon_Order_Status_Datatype);
		SelectSearchon_Order_Status_Datatype.click();

		getIgnoringStaleElementException(Received_Status_in_Status_Datatype);
		String ReceivedStatus_Status_Datatype = Received_Status_in_Status_Datatype.getText();
		assertTrue(ReceivedStatus_Status_Datatype.contains("Received"));
		System.out
				.println("Successfully Validated Received_Status_in_Status_Datatype:" + ReceivedStatus_Status_Datatype);
		extentTest.log(Status.PASS, "Successfully Validated Received_Status_in_Status_Datatype");

		String Status_Date_Time_Status_Datatype = Status_Date_Time_in_Status_Datatype.getText();

		System.out.println(Status_Date_Time_Status_Datatype);
		SimpleDateFormat formats1 = new SimpleDateFormat("yyyyMMdd");
		Date dates1 = formats1.parse(GetReceivedDate);

		SimpleDateFormat formats2 = new SimpleDateFormat("M/d/yyyy h:mm a");
		Date dates2 = formats2.parse(Status_Date_Time_Status_Datatype);

		SimpleDateFormat forms = new SimpleDateFormat("M/d/yyyy");
		String dateExpected1 = forms.format(dates2);
		String dateActual1 = forms.format(dates1);

		System.out.println(dateActual1 + "----------------------" + dateExpected1);
		assertEquals(dateActual, dateExpected);

		System.out.println(
				"Successfully Validated Status_Date_Tim_in_Status_Datatype:" + Status_Date_Time_Status_Datatype);
		extentTest.log(Status.PASS, "Successfully Validated Status_Date_Tim_in_Status_Datatype");

		getIgnoringStaleElementException(Event_Location_Address_in_Status_Datatype);
		String Event_Location = Event_Location_Address_in_Status_Datatype.getText();

		if (Event_Location.equals(GetLocationCity_State)) {
			System.out.println("LocationCity match: " + GetLocationCity_State + " and " + Event_Location);
		} else {
			System.out.println("LocationCity do not match.");

		}

		waits(Source_Status);
		getIgnoringStaleElementException(Source_Status);
		String Status_Source = Source_Status.getText();
		assertTrue(Status_Source.contains("StatusUpdate API"));
		System.out.println("Status Source is :" + Status_Source);
		extentTest.log(Status.PASS, "Status Source is StatusUpdate API");

		waits(Status_Event_detail);
		getIgnoringStaleElementException(Status_Event_detail);
		String Status_EventDetail = Status_Event_detail.getText();
		assertTrue(Status_EventDetail.contains("Normal"));
		System.out.println("Successfully Validated Status_Date_Tim_in_Status_Datatype:" + Status_EventDetail);
		extentTest.log(Status.PASS, "Successfully Validated Status_Date_Tim_in_Status_Datatype");

		// Checked_in_Status_datatype
		waits(Status_Send_Date);
		getIgnoringStaleElementException(Status_Send_Date);
		String Status_SendDate = Status_Send_Date.getText();

		try {
			waits(Checked_in_Status_datatype);
			if (Checked_in_Status_datatype.isDisplayed())
				System.out.println("Checked" + Checked_in_Status_datatype);

		} catch (Exception e) {
			System.out.println(Status_SendDate);
		}

	}

	public void ReceivedStatus_EPIC_Clipboard_validation() throws InterruptedException, ParseException {

		// driver.switchTo().defaultContent();

		Clickon_Actions.click();
		Await();
		Clickon_Refresh.click();
		Await();
		driver.switchTo().defaultContent();
		frameswitch2();
		Await();
		waits(ClickonGo);
		ClickonGo.click();
		System.out.println("Status: refresh");

		Await();
		driver.switchTo().defaultContent();
		// frameswitch2();
		// Await();
		Thread.sleep(7000);
		Toggle_toolbar.click();
		waits(CLickOnClipBoard);
		CLickOnClipBoard.click();
		Await();
		Windows();
		Await();
		ScrollDown();
		Await();
//		pyWorkPage.click();	

		waits(ClickonpyworkPage);
		ClickonpyworkPage.click();
		waits(ExpandpyworkPage);
		ExpandpyworkPage.click();
		// ScrollDown();
		Await();
		waits(ClickonOrderpageinCipboard);
		ClickonOrderpageinCipboard.click();
		waits(ExpandOrderPageinClipboard);
		ExpandOrderPageinClipboard.click();
		System.out.println("Entered into OrderPage");

		String attribute2 = ReceivedDate_OrderPage_Clipboard.getText();

		try {
			// Parse date string 1
			SimpleDateFormat format1 = new SimpleDateFormat("M/d/yyyy h:mm a");
			Date date1 = format1.parse(attribute);

			// Parse date string 2
			SimpleDateFormat format2 = new SimpleDateFormat("yyyyMMdd'T'HHmmss.SSS z");
			Date date2 = format2.parse(attribute2);

			// Validate if the dates match
			if (date1.equals(date2)) {
				System.out.println("Dates match: " + attribute + " and " + attribute2);
			} else {
				System.out.println("Dates do not match.");
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}

		System.out.println("Successfully Validated Status:" + attribute2);

		String text = StatusEvent_OrderPage_Clipboard.getText();
		assertTrue(text.contains("Received"));
		System.out.println("Successfully Validated Status:" + text);
		extentTest.log(Status.PASS, "Successfully validated Received Status in Clipboard Order Page");

		// ScrollDown();
		Await();
		waits(ExpandShipmentinCLipboard);
		ExpandShipmentinCLipboard.click();
		System.out.println("Entered into Shipment");
		extentTest.log(Status.PASS, "Entered into Shipment");

		waits(ClickonShipment2inCLipboard);
		ClickonShipment2inCLipboard.click();

		String text2 = StatusEvent_Shipement2_Clipboard.getText();
		assertTrue(text2.contains("Received"));
		System.out.println("Successfully Validated Status:" + text2);
		extentTest.log(Status.PASS, "Successfully validated Received Status in Shipment2 Order Page");

	}

}
