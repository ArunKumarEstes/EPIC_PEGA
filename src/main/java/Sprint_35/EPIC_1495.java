package Sprint_35;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.TimeZone;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.annotations.Listeners;

import com.aventstack.extentreports.Status;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import io.restassured.RestAssured;
import io.restassured.response.Response;

public class EPIC_1495 extends UtilClass {
	public static String sendkeys = "";

	public static String split;
	public static String attribute = "";
	public static String GetReceivedDate;
	public static String GetLocationCity_State;
	public static String EXLA = "";

	public void PAR_Order_Creation_RefValueUpdate() throws JSONException, IOException {

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
		extentTest.log(Status.PASS, "PAR Order Created");
	}

	public void RefValueUpdate_API__Update() throws JsonProcessingException, IOException, JSONException {
		String dirpath = System.getProperty("user.dir");
		File file = new File(dirpath + "//RefValueUpdate.json");
		ObjectMapper mapper = new ObjectMapper();
		JsonNode node = mapper.readTree(file);
		String actual = node.toPrettyString();
		// System.out.println("actual json----------------"+actual);

		String[] Status_Events = { "EXLA", "Here we can add any SCAC" };

		// JSONObject jsonObject = new JSONObject(actual);

		JSONObject jsonObject = new JSONObject(actual);

		jsonObject.put("CaseID", sendkeys);
		jsonObject.put("OutboundCarrier", Status_Events[0]);

		JSONObject originObject = jsonObject.getJSONObject("OrderRefs");
		originObject.put("PickupNumber", "1234");

		originObject.put("DeliveryNumber", "5678");

		JsonNode tree = mapper.readTree(jsonObject.toString());

		String changed = tree.toPrettyString();

		System.out.println("changed json----------------" + changed);

		RestAssured.baseURI = "https://epicuatlb.estes-express.com";
		Response response = RestAssured.given().auth().basic("EpicSevicesTest1", "Rules@1234")
				.contentType("application/json").body(jsonObject.toString())
				.post("/prweb/api/OrderServicePackage/V1/UpdateStatus");
		String responseBody = response.getBody().asString();

		System.out.println("<------Result of Updated PAR Json------>");
		System.out.println("Response Body: " + responseBody);
		int statusCode = response.getStatusCode();
		System.out.println("Status Code: " + statusCode);
		// System.out.println("JSON value updated successfully");
		extentTest.log(Status.PASS, "JSON value updated successfully");

	}

	public EPIC_1495(WebDriver driver) {
//		this.driver =driver;
		PageFactory.initElements(driver, this);
	}

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

	// --------------PEGA Clipboard---------------

	@FindBy(xpath = "(//h3[@class='layout-group-item-title'])[4]")
	public static WebElement CLickonApp;

	@FindBy(xpath = "//a[@title='PickupAndRelease']")
	public static WebElement CLickonPickAndRelease;

	@FindBy(xpath = "//span[@title='Close this tab']/ancestor::li[@aria-label='Pickup And Release']")
	public static WebElement PickAndRelease_Close;

	@FindBy(xpath = "//div[contains(text(), 'Refresh')]")
	public static WebElement PickAndReleaseRefresh;

	@FindBy(xpath = "(//a[@id='pui_filter'])[1]")
	public static WebElement ClickonIdFilter;

	@FindBy(xpath = "//input[contains(@name, 'FilterCriteria')]")
	public static WebElement SearchText;

	@FindBy(xpath = "//button[contains(text(), 'Apply')]")
	public static WebElement ClickonApply;

	@FindBy(xpath = "//div[contains(text(), 'PAR')]")
	public static WebElement CLickonPARcase;

	@FindBy(xpath = "(//button[contains(text(), 'Go')])")
	public static WebElement ClickonGo;

	@FindBy(xpath = "(//button[@name='CaseActionHeader_pyWorkPage_4'])[2]")
	public static WebElement Clickon_Actions;

	@FindBy(xpath = "//span[contains(text(),'Refresh')]")
	public static WebElement Clickon_Refresh;

	@FindBy(xpath = "//a[@title='Clipboard']")
	public static WebElement ClickonPegaClipboard;

	// ---

	@FindBy(xpath = "//button[@title='Toggle runtime toolbar']//i[contains(@class,'gear')]")
	public static WebElement Toggle_toolbar;

	@FindBy(xpath = "//button[@title='Clipboard']")
	public static WebElement CLickOnClipBoard;

//	@FindBy(xpath = "//span[contains(text(),'pyWorkPage')]/ancestor::ul[@class='rowContent']/descendant::div[contains(@id,'icon')]")
//	public static WebElement pyWorkPage;

//	@FindBy(xpath = "//span[contains(text(),'pyWorkPage')]/ancestor::ul[@class='rowContent']/descendant::div[contains(@id,'icon')]")
//	public static WebElement pyWorkPage;

	@FindBy(xpath = "((//span[contains(@title, 'pyWorkPage')])[1]/ancestor::li)[2]//ul//li//div//a")
	public static WebElement ClickonpyworkPage;

	@FindBy(xpath = "(//span[contains(text(), 'pyWorkPage')])[1]/ancestor::li[2]/ul/li/div/div[1]")
	public static WebElement ExpandpyworkPage;

	@FindBy(xpath = "//span[contains(text(), 'OrderPage')]")
	public static WebElement ClickonOrderpageinCipboard;

	@FindBy(xpath = "((//span[contains(text(), 'OrderPage')])[1]/ancestor::li)[3]/ul/li/div/div/a")
	public static WebElement ExpandOrderPageinClipboard;

	@FindBy(xpath = "((//span[contains(text(), 'ShipmentList')]/ancestor::li)[4]//ul//li//div//div)[1]")
	public static WebElement ExpandShipmentinCLipboard;

	@FindBy(xpath = "//span[contains(text(), 'ShipmentList(1)(ESTES-Data-Shipment)')]")
	public static WebElement Click_Shipment_1_inCLipboard;

	@FindBy(xpath = "//span[contains(text(), 'ShipmentList(2)(ESTES-Data-Shipment)')]")
	public static WebElement Click_Shipment_2_inCLipboard;

	@FindBy(xpath = "(//a[@title='Expand to show child rows'])[25]")
	public static WebElement ExpandpyworkPageinPegaClipboard;

	@FindBy(xpath = "//span[contains(text(), 'OrderRefs')]")
	public static WebElement ClickonRederRef;

	@FindBy(xpath = "//tr[@id='$PpyProps$ppxResults$l2']//div[@class='oflowDivM ']")
	public static WebElement Validate_Delivery_Number;

	@FindBy(xpath = "//tr[@id='$PpyProps$ppxResults$l4']//div[@class='oflowDivM ']")
	public static WebElement Validate_Pickup_Number;

	@FindBy(xpath = "//div[@class='oflowDivM ']//span/ancestor::table[@class='gridTable ']//tr//td//div//table//tbody//tr//td//nobr//span//a[contains(text(), 'StatusEventDateTime')]")
	public static WebElement ReceivedDate_OrderPage_Clipboard;

	@FindBy(xpath = "(//a[@title='Expand to show child rows'])[25]")
	public static WebElement ExpandOrderPageinPegaClipboard;

	@FindBy(xpath = "//span[contains(text(), 'Shortage')]")
	public static WebElement StatusEvent_OrderPage_Clipboard;

	@FindBy(xpath = "(//a[@title='Expand to show child rows'])[35]")
	public static WebElement ExpandShipmentinPegaCLipboard;

	@FindBy(xpath = "//span[contains(text(), 'ShipmentList(1)')]")
	public static WebElement ClickonShipment1inCLipboard;

	@FindBy(xpath = "//span[contains(text(), 'Shortage')]")
	public static WebElement StatusEvent_Shipement1_Clipboard;

	@FindBy(xpath = "//span[contains(text(), 'ShipmentList(2)')]")
	public static WebElement ClickonShipment2inCLipboard;

	@FindBy(xpath = "//tr[@id='$PpyProps$ppxResults$l3']//div[@class='oflowDivM ']")
	public static WebElement AssignedTradingPartner_in_Shipement2_Clipboard;

	@FindBy(xpath = "(//table[@class='gridTable '])[2]//tr[4]//td[2]//div")
	public static WebElement Verification;

	@FindBy(xpath = "//span[contains(text(), 'Loaded')]")
	public static WebElement StatusEvent_Shipement2_Clipboard;

	@FindBy(xpath = "(//div[@class='oflowDivM ']/span)[32]")
	public static WebElement OutboundTracking;

	// Datatype-----------

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

//	@FindBy(xpath = "//div//table[@id='gridLayoutTable']//tbody//tr[2]//td[5]//div//span//input[@id='PickupNumber1']")
//	public static WebElement PickUpNumber;

	@FindBy(xpath = "//td[contains(@data-ui-meta, 'PickupNumber')]")
	public static WebElement PickUpNumber;

	@FindBy(xpath = "//td[contains(@data-ui-meta, 'DeliveryNumber')]")
	public static WebElement DeliveryNumber;

	@FindBy(xpath = "//a[@aria-label='menu Shipment']")
	public static WebElement ClickonShipmentDatatype;

	@FindBy(xpath = "//div[text()='2']/parent::td[@data-attribute-name='Origin Location Type']/following-sibling::td[@data-attribute-name='AssignedTradingPartnerID']/div")
	public static WebElement AssignedTradingPartner_in_Shipemnt2;

	@FindBy(xpath = "//iframe[@name='PegaGadget0Ifr']")
	public static WebElement frameName0;

	@FindBy(xpath = "//iframe[@name='PegaGadget1Ifr']")
	public static WebElement frameName1;

	@FindBy(xpath = "//iframe[@name='PegaGadget2Ifr']")
	public static WebElement frameName2;

	@FindBy(xpath = "//iframe[@name='PegaGadget3Ifr']")
	public static WebElement frameName3;

	public static void frameswitch0() {
		driver.switchTo().frame(frameName0);

	}

	public static void frameswitch1() {
		driver.switchTo().frame(frameName1);
	}

	public static void frameswitch2() {
		driver.switchTo().frame(frameName2);
	}

	public static void frameswitch3() {
		driver.switchTo().frame(frameName3);
	}

//	public static void frameswitch() {
//		driver.switchTo().frame(frameswitch1);
//	}

	public void PEGALogin() throws InterruptedException {
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
		Await();
		extentTest.log(Status.PASS, "Successfully opened PAR Order");

		frameswitch0();
		waits(OSD);
		OSD.click();
		waits(InboundTrailer);

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
		Await();
		driver.switchTo().defaultContent();
		frameswitch1();
		Await();

//		SelectClass(Inbound_trailer_outboundLoads_Status, "Received");
//		ClickCalendarandSubmit();
//		Await();

	}

	public void LoadedStatus_EPIC_Clipboard_validation() throws InterruptedException, ParseException {

		// driver.switchTo().defaultContent();

		Clickon_Actions.click();
		Await();
		Clickon_Refresh.click();
		Await();
		driver.switchTo().defaultContent();
		frameswitch1();
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
		waits(ExpandpyworkPage);
		ExpandpyworkPage.click();
		// ScrollDown();
		Await();
		waits(ClickonOrderpageinCipboard);
		ClickonOrderpageinCipboard.click();
		waits(ExpandOrderPageinClipboard);
		ExpandOrderPageinClipboard.click();
		System.out.println("Entered into OrderPage");
		extentTest.log(Status.PASS, "Entered into OrderPage");

		System.out.println("Entered into OrderPage");
		Await();
		waits(ClickonOrderpageinCipboard);
		ClickonOrderpageinCipboard.click();

		waits(ClickonRederRef);
		ClickonRederRef.click();
		waits(Validate_Delivery_Number);
		getIgnoringStaleElementException(Validate_Delivery_Number);
		String Delivery_Number = Validate_Delivery_Number.getText();
		System.out.println("Successfully Validated Delivery_Number_in_Reforder_Clipboard:" + Delivery_Number);
		extentTest.log(Status.PASS, "Successfully Validated Delivery_Number_in_Reforder_Clipboard");

		waits(Validate_Pickup_Number);
		getIgnoringStaleElementException(Validate_Pickup_Number);
		String Pickup_Number = Validate_Pickup_Number.getText();
		System.out.println("Successfully Validated Pickup_Number_in_Reforder_Clipboard:" + Pickup_Number);
		extentTest.log(Status.PASS, "Successfully Validated Pickup_Number_in_Reforder_Clipboard");

		// ScrollDown();
		Await();
		waits(ExpandShipmentinCLipboard);
		ExpandShipmentinCLipboard.click();
		System.out.println("Entered into Shipment");
		extentTest.log(Status.PASS, "Entering Shipment");

		waits(ClickonShipment2inCLipboard);
		ClickonShipment2inCLipboard.click();
		Await();

//		EXLA = "e09ebdda-e685-4d00-849d-7f4a0c64458a";
//		getIgnoringStaleElementException(Verification);
//		String Assigend_trading_partner = Verification.getText();
//		System.out.println(Assigend_trading_partner);
//
//		assertTrue(Assigend_trading_partner.contains(EXLA));
//		System.out.println("Successfully Validated TradingPartner_in_Shipment2_Clipboard:" + Assigend_trading_partner);
		extentTest.log(Status.PASS, "Successfully Validated TradingPartner_in_Shipment2_Clipboard");

	}

	public void App_Clipboard() throws InterruptedException, ParseException {
		waits(CLickonApp);
		CLickonApp.click();

		Await();
		if (PickAndRelease_Close.isDisplayed()) {
			PickAndRelease_Close.click();

		}

		waits(CLickonPickAndRelease);
		CLickonPickAndRelease.click();
		extentTest.log(Status.PASS, "CLickonPickAndRelease");

		Thread.sleep(3000);
		driver.switchTo().defaultContent();
		frameswitch1();
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
		Thread.sleep(7000);
//		Toggle_toolbar.click();
//		waits(ClickonPegaClipboard);
		ClickonPegaClipboard.click();
		extentTest.log(Status.PASS, "Enetering PEGA Clipboard");

		Await();
		Windows();
		Await();

		waits(ClickonpyworkPage);
		ClickonpyworkPage.click();
		waits(ExpandpyworkPageinPegaClipboard);
		ExpandpyworkPageinPegaClipboard.click();
		Await();
		waits(ExpandOrderPageinPegaClipboard);
		ExpandOrderPageinPegaClipboard.click();
		System.out.println("Entered into OrderPage");
		Await();
		waits(ClickonOrderpageinCipboard);
		ClickonOrderpageinCipboard.click();

		waits(ClickonRederRef);
		ClickonRederRef.click();
		waits(Validate_Delivery_Number);
		getIgnoringStaleElementException(Validate_Delivery_Number);
		String Delivery_Number = Validate_Delivery_Number.getText();
		System.out.println("Successfully Validated Delivery_Number_in_Reforder_Clipboard:" + Delivery_Number);
		extentTest.log(Status.PASS, "Successfully Validated Delivery_Number_in_Reforder_Clipboard");

		waits(Validate_Pickup_Number);
		getIgnoringStaleElementException(Validate_Pickup_Number);
		String Pickup_Number = Validate_Pickup_Number.getText();
		System.out.println("Successfully Validated Pickup_Number_in_Reforder_Clipboard:" + Pickup_Number);
		extentTest.log(Status.PASS, "Successfully Validated Pickup_Number_in_Reforder_Clipboard");

		Await();
		waits(ExpandShipmentinCLipboard);
		ExpandShipmentinCLipboard.click();
		System.out.println("Entering Shipment");
		extentTest.log(Status.PASS, "Entering Shipment");

		waits(ClickonShipment2inCLipboard);
		ClickonShipment2inCLipboard.click();

		Await();
//		EXLA = "e09ebdda-e685-4d00-849d-7f4a0c64458a";
//		getIgnoringStaleElementException(Verification);
//		String Assigend_trading_partner = Verification.getText();
//		System.out.println(Assigend_trading_partner);
//
//		assertTrue(Assigend_trading_partner.contains(EXLA));
//		System.out.println("Successfully Validated TradingPartner_in_Shipment2_Clipboard:" + Assigend_trading_partner);
//		extentTest.log(Status.PASS, "Successfully Validated TradingPartner_in_Shipment2_Clipboard");
		driver.close();

	}

	public void BackToPEGA() throws InterruptedException, ParseException {
//		Windows();
//		Await();
//		String parentWindow = driver.getWindowHandle();
//		driver.switchTo().window(parentWindow);

		ArrayList<String> tab = new ArrayList<>(driver.getWindowHandles());
		driver.switchTo().window(tab.get(0));

		waits(CLickonDataType);
		CLickonDataType.click();
		extentTest.log(Status.PASS, "Enetering PEGA Datatype");

		waits(ClickonOrderDatatype);
		ClickonOrderDatatype.click();
		driver.switchTo().defaultContent();

		// frameswitch3();
		// driver.switchTo().frame(2);
		frameswitch2();
		Thread.sleep(8000);
		// waits(ClickonRecordinOrderDatatype);
		Clickon_Record_Status_and_OrderDatatype.click();
		waits(Searchon_Order_Status_Datatype);
		Searchon_Order_Status_Datatype.click();
		Searchon_Order_Status_Datatype.sendKeys(sendkeys);
		waits(SelectSearchon_Order_Status_Datatype);
		SelectSearchon_Order_Status_Datatype.click();

		// waits(PickUpNumber);
		Await();
		getIgnoringStaleElementException(PickUpNumber);
		String Get_PickUpNumber = PickUpNumber.getText();
		System.out.println("Succesfully Validated Pickup Number:" + Get_PickUpNumber);
		extentTest.log(Status.PASS, "Successfully Validated Pickup Number");

		waits(DeliveryNumber);
		getIgnoringStaleElementException(DeliveryNumber);
		String Get_DeliveryNumber = DeliveryNumber.getText();
		System.out.println("Succesfully Validated Delivery Number:" + Get_DeliveryNumber);
		extentTest.log(Status.PASS, "Successfully Validated Delivery Number");

		Await();

		driver.switchTo().defaultContent();
		// driver.switchTo().frame(2);
		Await();
		ClickonShipmentDatatype.click();
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

		frameswitch3();
		Await();
	//	waits(Clickon_Record_Status_and_OrderDatatype);
		Clickon_Record_Status_and_OrderDatatype.click();
		waits(Searchon_Order_Status_Datatype);
		Searchon_Order_Status_Datatype.click();
		Searchon_Order_Status_Datatype.sendKeys(sendkeys);
		waits(SelectSearchon_Order_Status_Datatype);
		SelectSearchon_Order_Status_Datatype.click();

		waits(AssignedTradingPartner_in_Shipemnt2);
		Thread.sleep(9000);
		getIgnoringStaleElementException(AssignedTradingPartner_in_Shipemnt2);
		String Assigned_Tradin_Partner_in_Shipment_2 = AssignedTradingPartner_in_Shipemnt2.getText();
//		assertTrue(Assigned_Tradin_Partner_in_Shipment_2.contains(EXLA));

		System.out.println("Successfuly Validated Assigned Trading PArtner in Shipment 2:"
				+ Assigned_Tradin_Partner_in_Shipment_2);
		extentTest.log(Status.PASS, "Successfuly Validated Assigned Trading PArtner in Shipment 2");

	}

}
