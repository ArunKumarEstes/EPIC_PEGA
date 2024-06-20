package Runner_Sprint_35;

import java.io.IOException;
import java.text.ParseException;

import org.codehaus.jettison.json.JSONException;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.fasterxml.jackson.core.JsonProcessingException;

import EPIC_1493.Cancelled_Status;
import EPIC_1493.Loaded_Status;
import EPIC_1493.Overage_Status;
import EPIC_1493.POM;
import EPIC_1493.Received_Status;
import EPIC_1493.Released_Status;
import EPIC_1493.Shortage_Status;
import Sprint_35.EPIC_1113;
import Sprint_35.EPIC_1489;
import Sprint_35.EPIC_1495;

public class Runner extends Sprint_35.UtilClass {
//	@BeforeMethod
//	public static void ChromeLaunch() {
//		Sprint_35.UtilClass.BrowserLaunch();
//
//	}

////	@Test(priority = 0)
//	public void PEGALogin() throws Exception {
//		POM login = new POM(driver);
//		login.PAR_Order();
////		login.PEGALogin();
////		login.LaunchWarehousePortal();
////		login.InboundTrailer_WorkQueue();		
////		login.App();
////		login.InboundTrailer_WorkQueue2();
////		login.Input_json();
////		login.Status_Update();
//		 login.BackToPEGA();
////		login.Orderprocess_AfterUpdate();
////		login.Outbound_Loads();
////		login.Outbound_Loads2();
//	}

	@Test(priority = 1)
	public void Received_Status_Update_1493() throws Exception {
		Received_Status login = new Received_Status(driver);
		login.PAR_Order_Creation();
		login.Received_Status_Update();
		login.PEGALogin();
//		login.LaunchWarehousePortal();
//		login.InboundTrailer_WorkQueue();	
//		login.ReceivedStatus_EPIC_Clipboard_validation();

		login.App_Clipboard();
		login.BackToPEGA();

	}

	@Test(priority = 2)
	public void Loaded_Status_Update_1493() throws Exception {
		Loaded_Status login = new Loaded_Status(driver);
		login.PAR_Order_Creation();
		login.Loaded_Status_Update();
		login.PEGALogin();
//	login.LaunchWarehousePortal();
//	login.InboundTrailer_WorkQueue();		
		login.App_Clipboard();
//	login.Input_json();
		login.BackToPEGA();
//	login.LoadedStatus_EPIC_Clipboard_validation();
	}

	@Test(priority = 3)
	public void Released_Status_update_1493() throws Exception {
		Released_Status login = new Released_Status(driver);
		login.PAR_Order_Creation();
//	    login.LaunchWarehousePortal();
//	    login.InboundTrailer_WorkQueue();
		login.Received_Status_Update();
		login.Released_Status_Update();
		login.PEGALogin();
		login.App_Clipboard();
		login.BackToPEGA();
//      login.Released_Status_EPIC_Clipboard_validation();

	}

	@Test(priority = 4)
	public static void Shortage_Status_update_1493()
			throws JsonProcessingException, IOException, JSONException, InterruptedException, ParseException {
		Shortage_Status login = new Shortage_Status(driver);
		login.PAR_Order_Creation();
//		login.LaunchWarehousePortal();
//		login.InboundTrailer_WorkQueue();
		login.Shortage_Status_Update();
		login.PEGALogin();
		login.App_Clipboard();
		login.BackToPEGA();
	}

	@Test(priority = 5)
	public static void Overage_Status_update_1493()
			throws JsonProcessingException, IOException, JSONException, InterruptedException, ParseException {
		Overage_Status login = new Overage_Status(driver);
		login.PAR_Order_Creation();
//		login.LaunchWarehousePortal();
//		login.InboundTrailer_WorkQueue();
		login.Overage_Status_Update();
		login.PEGALogin();
		login.App_Clipboard();
		login.BackToPEGA();
	}

	@Test(priority = 6)
	public static void Cancelled_Status_update_1493()
			throws JsonProcessingException, IOException, JSONException, InterruptedException, ParseException {
		Cancelled_Status login = new Cancelled_Status(driver);
		login.PAR_Order_Creation();
//		login.LaunchWarehousePortal();
//		login.InboundTrailer_WorkQueue();
		login.Cancelled_Status_Update();
		login.PEGALogin();
		login.App_Clipboard();
		login.BackToPEGA();
	}

	@Test(priority = 7)
	public void Runner_1495() throws Exception {
		EPIC_1495 login = new EPIC_1495(driver);
	login.PAR_Order_Creation_RefValueUpdate();
		login.RefValueUpdate_API__Update();
		login.PEGALogin();
		login.LaunchWarehousePortal();
		login.InboundTrailer_WorkQueue();
		login.LoadedStatus_EPIC_Clipboard_validation();
	//	login.App_Clipboard();
		login.BackToPEGA();

	}

	@Test(priority = 8)
	public void Runner_1113() throws Exception {
		EPIC_1113 login = new EPIC_1113(driver);
		login.DFDScanTool_API();
		login.XML_Write();
		login.PEGALogin();
		login.DevStudioSearchBox();
		login.SOAPServicePopup();
		WebElement textArea = driver
				.findElement(By.xpath("//textarea[@name='$PpySimulationDataPage$ppyRequestTextData']"));
		// Specify the file path
		String filePath = "C:\\Users\\kumarar\\Documents\\EPIC Sprint\\Sprint - 35\\Sprint-35\\ExceptionHandling.xml";

		// Call the sendKeysJavascript method with the appropriate arguments
		sendKeysJavascript(driver, textArea, filePath);
		login.ClickExecute();
		login.LaunchWarehousePortal();

	}

	@Test(priority = 9)
	public void Runner_1489() throws Exception {
		EPIC_1489 login = new EPIC_1489(driver);
		login.Order_Creation();
		Await();
		login.PEGALogin();
		login.LaunchWarehousePortal();
	//	login.OrdersPAR();
		login.InboundTrailer_WorkQueue();
	//	login.Clipboard_Validation();

	}
}
