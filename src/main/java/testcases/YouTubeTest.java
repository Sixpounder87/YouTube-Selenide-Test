package testcases;

import static com.codeborne.selenide.Condition.appear;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.WebDriverRunner.closeWebDriver;
import static com.codeborne.selenide.WebDriverRunner.setWebDriver;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import fw.ReadPropertyData;

public final class YouTubeTest {

	private static final long LONG_TIMEOUTmillis = 180000;
	private static final String VIDEO_TITLE = "Yury Kosterin. Test video for 4th lesson QA automation shool";

	private WebDriver driver;
	private final String username = ReadPropertyData.getUsername();
	private final String password = ReadPropertyData.getUserPassword();

	@BeforeClass
	public void preconditions() {
		System.setProperty("webdriver.chrome.driver",
				ReadPropertyData.getDriverPath());
		driver = new ChromeDriver();
		setWebDriver(driver);
	}

	@Test
	public void authenticate() {
		open(ReadPropertyData.getBaseUrl());
		$(".yt-uix-button-primary").click();
		$("#Email").val(username).pressEnter();
		$("#Passwd").val(password).pressEnter();
		$(".yt-masthead-user-icon").click();
		$("div.yt-uix-clickcard-card-body > div").should(appear);
		$("div.yt-uix-clickcard-card-body > div > a")
				.shouldHave(text(username));
	}

	@Test
	public void uploadVideo() {
		$("#upload-btn").click();
		$("#upload-prompt-box > input[type='file']").sendKeys(
				ReadPropertyData.getVideoFilePath());
		$("#upload-item-0 span.progress-bar-text-done").waitUntil(visible,
				LONG_TIMEOUTmillis);
		$(
				"#upload-item-0 fieldset.metadata-two-column label:nth-child(1) input")
				.val(VIDEO_TITLE);
		$("#upload-item-0 div.upload-state-bar > div.metadata-actions button")
				.click();

		// String reference = $("div.upload-state-bar input[value]").getValue();

		// Sleep 30 seconds to wait the video is available in YouTube search
		try {
			Thread.sleep(45000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		$("#masthead-search-term").val(VIDEO_TITLE);
		$("#search-btn").click();
		$(".item-section h3 > a[title='" + VIDEO_TITLE + "']")
				.shouldBe(visible);
	}

	@AfterClass
	public void postconditions() {
		closeWebDriver();
	}
}
