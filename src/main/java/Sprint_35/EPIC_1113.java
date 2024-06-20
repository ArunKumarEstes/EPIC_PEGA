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

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.annotations.Listeners;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

//import com.aventstack.extentreports.Status;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import io.restassured.RestAssured;
import io.restassured.response.Response;
@Listeners(TestListener.class)
public class EPIC_1113 extends UtilClass {

	public static String sendkeys = "";
	public static String split;
	public static String attribute;
	public static String SearchBox_Text = "OrderServicePackage";

	public EPIC_1113(WebDriver driver) {
		PageFactory.initElements(driver, this);
	}

	@FindBy(xpath = "//iframe[@name='PegaGadget0Ifr']")
	public static WebElement frameName;

	@FindBy(id = "loginText2")
	public static WebElement ssoLogin;

	@FindBy(className = "table-row")
	public static WebElement code;

	@FindBy(id = "idTxtBx_SAOTCC_OTC")
	public static WebElement send;

	@FindBy(id = "idSubmit_SAOTCC_Continue")
	public static WebElement click;

	@FindBy(xpath = "//input[@name='$PpyDisplayHarness$ppySearchText']")
	public static WebElement ClickDevStudioSearchBox;

	@FindBy(xpath = "//i[@class='pi pi-search-2']")
	public static WebElement ClickDevStudioSearchIcon;

	// **** Searching Order Service Package(Order Creation) in Dev Studio Search****

	@FindBy(xpath = "//a[contains(text(),'OrderServicePackage Services ProcessData')]")
	public static WebElement ClickOrderServicePackage;

	@FindBy(xpath = "//button[contains(text(),'Actions')]")
	public static WebElement ServicePageActions;

	@FindBy(xpath = "(//span[contains(text(),'Run')])[2]")
	public static WebElement ServicePageRun;

	@FindBy(xpath = "//input[@value='EnterText']")
	public static WebElement SupplySOAPCheckBox;

	@FindBy(xpath = "//textarea[@name='$PpySimulationDataPage$ppyRequestTextData']")
	public static WebElement SupplySOAPTextBox;

	@FindBy(xpath = "//div//span[contains(text(),'Execute')]")
	public static WebElement ExecuteClick;

	@FindBy(xpath = "//div[contains(@class,'launch-portals')]/descendant::a")
	public static WebElement LaunchPortal;

	@FindBy(xpath = "//span[contains(text(),'WareHouse UserPortal')]")
	public static WebElement warehouse;

	public static void frameSwitch() {
		driver.switchTo().frame(frameName);

	}

	public void DFDScanTool_API() throws JsonProcessingException, IOException {
		String prettyString = null;
		File file = new File(System.getProperty("user.dir") + "\\DFD_ExceptionHandling.json");
		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode rootNode = objectMapper.readTree(file);

		if (rootNode instanceof ObjectNode) {
			ObjectNode objectNode = (ObjectNode) rootNode;
			ObjectNode orderRefs = (ObjectNode) objectNode.get("OrderRefs");
			// orderRefs.put("BOL", Math.ceil(Math.random() * 100000000));
			orderRefs.put("TrackingNumber", Math.ceil(Math.random() * 100000000));
			orderRefs.put("InvoiceNumber", Math.ceil(Math.random() * 100000000));

			prettyString = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(objectNode);

			RestAssured.baseURI = "https://epicuatlb.estes-express.com";
			Response response = RestAssured.given().auth().basic("EpicSevicesTest1", "Rules@1234")
					.contentType("application/json").body(prettyString)
					.post("/prweb/api/OrderServicePackage/V1/CreateOrUpdateOrder");
			String responseBody = response.getBody().asString();
			String[] split = responseBody.split("Reference is ");

			sendkeys = split[1];
			System.out.println(sendkeys);
			System.out.println("<------Result of DFD Json------>");
			System.out.println("Response Body: " + responseBody);
			int statusCode = response.getStatusCode();
			System.out.println("Status Code: " + statusCode);
		}
	}

	public void XML_Write() {
		try {
			// Load the XML document
			File xmlFile = new File("C:\\Users\\kumarar\\Documents\\Old VDI files\\eclipse-workspace\\Sprint_35\\ExceptionHandling.xml");
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(xmlFile);

			// Normalize the document to ensure proper structure
			doc.getDocumentElement().normalize();

			NodeList extensionNodes = doc.getElementsByTagName("Extension");
			for (int i = 0; i < extensionNodes.getLength(); i++) {
				Element extension = (Element) extensionNodes.item(i);
				// Find the element with the attribute "Name" equal to "UDOLEGID"
				String name = extension.getAttribute("Name");
				if (name.equals("UDOLEGID")) {
					// Change the value of the "Value" attribute
					extension.setAttribute("Value", sendkeys);
				}
			}

			// Write the updated document back to the same file
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(xmlFile);
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.transform(source, result);

			System.out.println("XML file updated successfully.");

		} catch (ParserConfigurationException | SAXException | IOException | TransformerException e) {
			e.printStackTrace();
		}

	}

	public void PEGALogin() throws InterruptedException {
		ssoLogin.click();
		waits(code);
		code.click();

		// ****Scanner class to handle OTP****
		String scanner = scanner();
		send.sendKeys(scanner);
		waits(click);
		click.click();
	}

	public void DevStudioSearchBox() throws Exception {
		Await();
		ClickDevStudioSearchBox.click();
		ClickDevStudioSearchBox.sendKeys(SearchBox_Text);
		ClickDevStudioSearchIcon.click();
		Await();
		ClickOrderServicePackage.click();
		Await();
		frameSwitch();
		ServicePageActions.click();
		Await();
		ServicePageRun.click();
		Await();
	}

	public void SOAPServicePopup() throws Exception {
		Await();
		Windows();
		Await();
		SupplySOAPCheckBox.click();
		Await();
		SupplySOAPTextBox.clear();
		Await();

	}

	public void ClickExecute() throws Exception {
		Await();
		ScrollDown2();
		ExecuteClick.click();
		Await();
		// Windows();
		String parentWindow = driver.getWindowHandle();
		driver.switchTo().window(parentWindow);

	}

	public void LaunchWarehousePortal() throws InterruptedException {
		waits(LaunchPortal);
		LaunchPortal.click();
		waits(warehouse);
		warehouse.click();
		Await();
		Windows();
	}
}
