package org.example;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.Duration;

public class FinishColorPickerTest {

    private WebDriver driver;
    private WebDriverWait wait;
    private final String BASE_URL = "http://192.168.10.35:5050/";

    // ==================== ВСЕ ЛОКАТОРЫ ЧЕРЕЗ XPATH ====================
    // 1. Радиокнопки выбора цвета
    private final By RADIO_SILVER = By.xpath("//input[@type='radio' and @name='hardware_finish' and @value='Silver']");
    private final By RADIO_SPACE_BLACK = By.xpath("//input[@type='radio' and @name='hardware_finish' and @value='Space Black']");
    private final By RADIO_STARLIGHT = By.xpath("//input[@type='radio' and @name='hardware_finish' and @value='Starlight']");

    // 2. Карточки выбора цветов (label)
    private final By CARD_SILVER = By.xpath("//label[.//span[text()='Silver']]");
    private final By CARD_SPACE_BLACK = By.xpath("//label[.//span[text()='Space Black']]");
    private final By CARD_STARLIGHT = By.xpath("//label[.//span[text()='Starlight']]");

    // 3. Элементы внутри карточек (свотчи и текст)
    private final By SWATCH_SILVER = By.xpath("//span[contains(@class, 'bg-[#e3e4e8]')]");
    private final By SWATCH_SPACE_BLACK = By.xpath("//span[contains(@class, 'bg-[#e3e4e8]')]");
    private final By SWATCH_STARLIGHT = By.xpath("//span[contains(@class, 'bg-[#ede6d8]')]");
    private final By TEXT_SILVER = By.xpath("//label[.//input[@value='Silver']]//span[@class='font-medium']");
    private final By TEXT_SPACE_BLACK = By.xpath("//label[.//input[@value='Space Black']]//span[@class='font-medium']");
    private final By TEXT_STARLIGHT = By.xpath("//label[.//input[@value='Starlight']]//span[@class='font-medium']");

    // 4. Текстовые подписи под превью монитора
    private final By FINISH_BADGE = By.xpath("//span[contains(@class, 'finish-badge-label')]");
    private final By MEMORY_BADGE = By.xpath("//span[contains(@class, 'memory-badge-label')]");

    // 5. SVG-элементы рендера монитора
    private final By SVG_CHASSIS = By.xpath("//*[local-name()='svg']//*[contains(@class, 'svg-chassis-fill')]");
    private final By SVG_STAND = By.xpath("//*[local-name()='svg']//*[contains(@class, 'svg-stand-fill')]");

    // 6. Цена, кнопка Reset и память 32GB
    private final By LIVE_PRICE = By.xpath("//div[contains(@class, 'live-price-display')]");
    private final By RESET_BUTTON = By.xpath("//button[text()='Reset']");
    private final By CARD_MEMORY_32GB = By.xpath("//label[.//input[@value='32GB']]");

    @BeforeMethod
    public void setUp() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");
        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        driver.get(BASE_URL);
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    /**
     * TC-FIN-01: Состояние по умолчанию при загрузке страницы
     */
    @Test(description = "TC-FIN-01: Состояние по умолчанию при загрузке страницы")
    public void testTC_FIN_01_DefaultState() {
        WebElement radioSilver = wait.until(ExpectedConditions.presenceOfElementLocated(RADIO_SILVER));
        WebElement radioSpaceBlack = driver.findElement(RADIO_SPACE_BLACK);
        WebElement radioStarlight = driver.findElement(RADIO_STARLIGHT);

        // 1. Проверка радиокнопок
        Assert.assertTrue(radioSilver.isSelected(), "По умолчанию должен быть выбран Silver!");
        Assert.assertFalse(radioSpaceBlack.isSelected(), "Space Black не должен быть выбран по умолчанию!");
        Assert.assertFalse(radioStarlight.isSelected(), "Starlight не должен быть выбран по умолчанию!");

        // 2. Проверка текста подписи под изображением
        WebElement finishBadge = driver.findElement(FINISH_BADGE);
        WebElement memoryBadge = driver.findElement(MEMORY_BADGE);
        Assert.assertEquals(finishBadge.getText().trim(), "Silver Finish");
        Assert.assertEquals(memoryBadge.getText().trim(), "16GB Unified Memory");

        // 3. Проверка цвета превью монитора (SVG fill для Silver)
        WebElement chassis = driver.findElement(SVG_CHASSIS);
        Assert.assertEquals(chassis.getAttribute("fill"), "#e3e4e8", "Цвет корпуса монитора должен соответствовать Silver (#e3e4e8)");

        // 4. Проверка базовой цены
        WebElement price = driver.findElement(LIVE_PRICE);
        Assert.assertEquals(price.getText().trim(), "$1,599");
    }

    /**
     * TC-FIN-02: Выбор цвета «Space Black»
     */
    @Test(description = "TC-FIN-02: Выбор цвета Space Black")
    public void testTC_FIN_02_SelectSpaceBlack() {
        driver.findElement(CARD_SPACE_BLACK).click();

        WebElement radioSpaceBlack = driver.findElement(RADIO_SPACE_BLACK);
        WebElement radioSilver = driver.findElement(RADIO_SILVER);

        Assert.assertTrue(radioSpaceBlack.isSelected(), "Space Black должен стать выбранным!");
        Assert.assertFalse(radioSilver.isSelected(), "Silver должен стать неактивным!");

        // Проверка SVG превью (Space Black: chassis #2b2c30, stand #1f2023)
        Assert.assertEquals(driver.findElement(SVG_CHASSIS).getAttribute("fill"), "#2b2c30");
        Assert.assertEquals(driver.findElement(SVG_STAND).getAttribute("fill"), "#1f2023");

        // Проверка текстовой подписи
        Assert.assertEquals(driver.findElement(FINISH_BADGE).getText().trim(), "Space Black Finish");
        Assert.assertEquals(driver.findElement(LIVE_PRICE).getText().trim(), "$1,599");
    }

    /**
     * TC-FIN-03: Выбор цвета «Starlight»
     */
    @Test(description = "TC-FIN-03: Выбор цвета Starlight")
    public void testTC_FIN_03_SelectStarlight() {
        driver.findElement(CARD_STARLIGHT).click();

        WebElement radioStarlight = driver.findElement(RADIO_STARLIGHT);
        WebElement radioSilver = driver.findElement(RADIO_SILVER);

        Assert.assertTrue(radioStarlight.isSelected(), "Starlight должен стать выбранным!");
        Assert.assertFalse(radioSilver.isSelected(), "Silver должен сброситься!");

        // Проверка SVG превью (Starlight: chassis #ede6d8, stand #ddd4c3)
        Assert.assertEquals(driver.findElement(SVG_CHASSIS).getAttribute("fill"), "#ede6d8");
        Assert.assertEquals(driver.findElement(SVG_STAND).getAttribute("fill"), "#ddd4c3");

        // Проверка текстовой подписи
        Assert.assertEquals(driver.findElement(FINISH_BADGE).getText().trim(), "Starlight Finish");
    }

    /**
     * TC-FIN-04: Переключение между всеми цветами по кругу
     */
    @Test(description = "TC-FIN-04: Взаимоисключающий выбор (Single Selection) и переключение по кругу")
    public void testTC_FIN_04_CycleThroughColors() {
        // 1. Выбрать Space Black
        driver.findElement(CARD_SPACE_BLACK).click();
        Assert.assertTrue(driver.findElement(RADIO_SPACE_BLACK).isSelected());
        Assert.assertFalse(driver.findElement(RADIO_SILVER).isSelected());
        Assert.assertFalse(driver.findElement(RADIO_STARLIGHT).isSelected());
        Assert.assertEquals(driver.findElement(FINISH_BADGE).getText().trim(), "Space Black Finish");

        // 2. Выбрать Starlight
        driver.findElement(CARD_STARLIGHT).click();
        Assert.assertTrue(driver.findElement(RADIO_STARLIGHT).isSelected());
        Assert.assertFalse(driver.findElement(RADIO_SILVER).isSelected());
        Assert.assertFalse(driver.findElement(RADIO_SPACE_BLACK).isSelected());
        Assert.assertEquals(driver.findElement(FINISH_BADGE).getText().trim(), "Starlight Finish");

        // 3. Вернуться к Silver
        driver.findElement(CARD_SILVER).click();
        Assert.assertTrue(driver.findElement(RADIO_SILVER).isSelected());
        Assert.assertFalse(driver.findElement(RADIO_SPACE_BLACK).isSelected());
        Assert.assertFalse(driver.findElement(RADIO_STARLIGHT).isSelected());
        Assert.assertEquals(driver.findElement(FINISH_BADGE).getText().trim(), "Silver Finish");
    }

    /**
     * TC-FIN-05: Кликабельная область карточки выбора цвета
     */
    @Test(description = "TC-FIN-05: Кликабельная область карточки цвета (рамка, свотч, текст)")
    public void testTC_FIN_05_ClickableCardAreas() {
        // 1. Клик на цветовой круг (swatch) внутри Space Black
        driver.findElement(SWATCH_SPACE_BLACK).click();
        Assert.assertTrue(driver.findElement(RADIO_SPACE_BLACK).isSelected(), "Клик по свотчу должен выбирать Space Black!");

        // 2. Клик на текст 'Starlight' внутри карточки Starlight
        driver.findElement(TEXT_STARLIGHT).click();
        Assert.assertTrue(driver.findElement(RADIO_STARLIGHT).isSelected(), "Клик по тексту названия должен выбирать Starlight!");

        // 3. Клик на пустое пространство карточки Silver (со смещением от края)
        WebElement cardSilver = driver.findElement(CARD_SILVER);
        new Actions(driver).moveToElement(cardSilver, 5, 5).click().perform();
        Assert.assertTrue(driver.findElement(RADIO_SILVER).isSelected(), "Клик по пустому месту карточки должен выбирать Silver!");

        // Проверка курсора (cursor: pointer)
        String cursorStyle = cardSilver.getCssValue("cursor");
        Assert.assertEquals(cursorStyle, "pointer", "Курсор на карточке должен быть pointer!");
    }

    /**
     * TC-FIN-06: Сохранение выбранного цвета при изменении объема памяти (Memory)
     */
    @Test(description = "TC-FIN-06: Сохранение выбранного цвета при изменении объема памяти")
    public void testTC_FIN_06_PersistColorOnMemoryChange() {
        // Выбираем Space Black
        driver.findElement(CARD_SPACE_BLACK).click();
        Assert.assertTrue(driver.findElement(RADIO_SPACE_BLACK).isSelected());

        // Меняем память на 32GB (+$400)
        driver.findElement(CARD_MEMORY_32GB).click();

        // Проверяем, что цвет не сбросился
        Assert.assertTrue(driver.findElement(RADIO_SPACE_BLACK).isSelected(), "Цвет Space Black должен сохраниться!");
        Assert.assertEquals(driver.findElement(SVG_CHASSIS).getAttribute("fill"), "#2b2c30");

        // Проверяем комбинированную подпись и пересчёт цены ($1,599 + $400 = $1,999)
        Assert.assertEquals(driver.findElement(FINISH_BADGE).getText().trim(), "Space Black Finish");
        Assert.assertEquals(driver.findElement(MEMORY_BADGE).getText().trim(), "32GB Unified Memory");
        Assert.assertEquals(driver.findElement(LIVE_PRICE).getText().trim(), "$1,999");
    }

    /**
     * TC-FIN-07: Сброс выбранного цвета по кнопке «Reset» в шапке
     */
    @Test(description = "TC-FIN-07: Сброс выбранного цвета по кнопке Reset в шапке")
    public void testTC_FIN_07_ResetColor() {
        // Выбираем Starlight
        driver.findElement(CARD_STARLIGHT).click();
        Assert.assertTrue(driver.findElement(RADIO_STARLIGHT).isSelected());

        // Нажимаем Reset в правом верхнем углу
        driver.findElement(RESET_BUTTON).click();

        // Проверяем сброс к состоянию по умолчанию
        Assert.assertTrue(driver.findElement(RADIO_SILVER).isSelected(), "После Reset активным должен стать Silver!");
        Assert.assertEquals(driver.findElement(FINISH_BADGE).getText().trim(), "Silver Finish");
        Assert.assertEquals(driver.findElement(SVG_CHASSIS).getAttribute("fill"), "#e3e4e8");
    }

    /**
     * TC-FIN-08: Управление выбором цвета с клавиатуры (Accessibility / A11y)
     */
    @Test(description = "TC-FIN-08: Управление выбором цвета с клавиатуры")
    public void testTC_FIN_08_KeyboardNavigation() {
        WebElement radioSilver = driver.findElement(RADIO_SILVER);

        // Переводим фокус на радиокнопку Silver и нажимаем стрелку вправо
        radioSilver.sendKeys(Keys.ARROW_RIGHT);

        // После нажатия стрелки вправо фокус и выбор переходят на следующий цвет (Space Black)
        WebElement radioSpaceBlack = driver.findElement(RADIO_SPACE_BLACK);
        Assert.assertTrue(radioSpaceBlack.isSelected(), "Стрелка вправо должна переключить на Space Black!");
        Assert.assertEquals(driver.findElement(FINISH_BADGE).getText().trim(), "Space Black Finish");
        Assert.assertEquals(driver.findElement(SVG_CHASSIS).getAttribute("fill"), "#2b2c30");

        // Нажимаем стрелку вправо ещё раз -> переход к Starlight
        radioSpaceBlack.sendKeys(Keys.ARROW_RIGHT);
        WebElement radioStarlight = driver.findElement(RADIO_STARLIGHT);
        Assert.assertTrue(radioStarlight.isSelected(), "Вторая стрелка вправо должна переключить на Starlight!");
        Assert.assertEquals(driver.findElement(FINISH_BADGE).getText().trim(), "Starlight Finish");

        // Нажимаем стрелку влево -> возврат к Space Black
        radioStarlight.sendKeys(Keys.ARROW_LEFT);
        Assert.assertTrue(driver.findElement(RADIO_SPACE_BLACK).isSelected(), "Стрелка влево должна вернуть на Space Black!");
    }

    /**
     * TC-FIN-09: Визуальное соответствие цветовых индикаторов (UI)
     */
    @Test(description = "TC-FIN-09: Визуальное соответствие цветовых индикаторов и текста")
    public void testTC_FIN_09_VisualCheckSwatches() {
        // Проверяем наличие границы (border) у цветовых кружков
        WebElement silverSwatch = driver.findElement(SWATCH_SILVER);
        WebElement starlightSwatch = driver.findElement(SWATCH_STARLIGHT);

        Assert.assertNotNull(silverSwatch.getCssValue("border-top-width"), "У свотча Silver должна быть рамка!");
        Assert.assertNotNull(starlightSwatch.getCssValue("border-top-width"), "У свотча Starlight должна быть рамка!");

        // Проверяем текст названий цветов
        WebElement labelSilver = driver.findElement(TEXT_SILVER);
        WebElement labelSpaceBlack = driver.findElement(TEXT_SPACE_BLACK);
        WebElement labelStarlight = driver.findElement(TEXT_STARLIGHT);

        Assert.assertEquals(labelSilver.getText().trim(), "Silver");
        Assert.assertEquals(labelSpaceBlack.getText().trim(), "Space Black");
        Assert.assertEquals(labelStarlight.getText().trim(), "Starlight");
    }
}
