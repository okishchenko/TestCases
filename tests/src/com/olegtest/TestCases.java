package com.olegtest;

import org.openqa.selenium.support.ui.*;


import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.Assert;

public class TestCases {
    WebDriver driver;
    
    public WebElement getLinkEnter(String locatorName) {
        return  driver.findElement(By.xpath(locatorName));
    }
    public boolean isElementPresent(String locatorName) {
    	  return driver.findElements(By.xpath(locatorName)).size() > 0;
    	}
    public void elementClick (String locatorName) {
    	Assert.assertTrue(isElementPresent(locatorName));
    	WebElement element = driver.findElement(By.xpath(locatorName));
        element.click();        
    }
    public void elementSelect (String itemForSelect,String locatorName) {
    	Select select = new Select(driver.findElement(By.xpath(locatorName)));
    	select.selectByVisibleText(itemForSelect);
    }
    public void checkMessage(String errorMessage, String locatorNasme){
    	WebElement error=driver.findElement(By.xpath(locatorNasme));
    	Assert.assertEquals(errorMessage, error.getText());
    }
    public void fillField(String text, String locatorName){
    	WebElement testField=driver.findElement(By.xpath(locatorName));
        Assert.assertTrue(testField.isEnabled());
        testField.sendKeys(text);
    }
    public void signIn(String login, String pass){
    	elementClick("//a[@href='/login']");
    	fillField(login,"//input[@id='j_username']");
    	fillField(pass,"//input[@id='j_password']");
    	elementClick("//button[@type='submit']");
    }
    

@Before
public void setUp(){
    driver=new FirefoxDriver();
    driver.get("http://gioia-profiterole.rhcloud.com/");
    driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
}

@After
public void tearDown(){
    driver.quit();
}

@Test
public void testAuthorizationPositive(){
    //click link Login
    elementClick("*//a[@href=\"/login\"]");
	//click forgot password link
    elementClick("*//a[@href=\"/forgotPassword\"]");
    //enter email
    fillField("olegkishchenko@gmail.com", "//input[@id=\"email\"]");
    //click button "Next"
    elementClick("//button[@class=\"btn\"]");
    //verify messages are present
    Assert.assertTrue(driver.findElement(By.xpath("//h4[text()=\"На Ваш Email отправлено сообщение со ссылкой для восстановления пароля. \"]")).isDisplayed());
    Assert.assertTrue(driver.findElement(By.xpath("//h4[text()=\" Пожалуйста, проверьте Вашу почту! \"]")).isDisplayed());
    //navigate to home page
    elementClick("//a[@href=\"/index\"]");
    //click Login link
    elementClick("*//a[@href=\"/login\"]");
    //fill login field
    WebElement fieldLogin=driver.findElement(By.id("j_username"));
    Assert.assertTrue(fieldLogin.isEnabled());
    fieldLogin.sendKeys("OlegK");
    //fill pass field
    WebElement fieldPass=driver.findElement(By.id("j_password"));
    Assert.assertTrue(fieldPass.isEnabled());
    fieldPass.sendKeys("password");
    //click button Submit
    elementClick("//button[@type=\"submit\"]");
    
    //verify link Login is not present but link Logout is present
    Assert.assertFalse(isElementPresent("*//a[@href=\"/login\"]"));
    Assert.assertTrue(isElementPresent("*//a[@href=\"/j_spring_security_logout\"]"));
    									 
    //click link Logout
    elementClick("*//a[@href=\"/j_spring_security_logout\"]");
    
    //verify link Login\Logout
    Assert.assertFalse(isElementPresent("*//a[@href=\"/j_spring_security_logout\"]"));
    Assert.assertTrue(isElementPresent("*//a[@href=\"/login\"]"));
}
@Test
public void testAuthorizationNegative(){
	//fill form using valid login and invalid password
	signIn("OlegK","invalid_password");
	elementClick("//button[@type='submit']");
  
    //verify error message
    checkMessage("Вы ввели неверный логин и/или пароль","//div[@id=\"passwordOrLoginError\"]");
	
	//fill form using invalid login and valid password
    signIn("invalid_login","password");
	elementClick("//button[@type='submit']");
    
    //verify error message
    checkMessage("Вы ввели неверный логин и/или пароль","//div[@id=\"passwordOrLoginError\"]");
    
  	
  	//"forgot password" form with invalid data
  	elementClick("*//a[@href=\"/login\"]");
  	elementClick("*//a[@href=\"/forgotPassword\"]");
  	fillField("unvalid@gmail.com", "//input[@id=\"email\"]");
  	elementClick("//button[@type='submit']");
    
    //check error message
  	checkMessage("Пользователь с данным e-mail адрессом не зарегистрирован.", "//span[@class='error']");
  	}
@Test
public void testCreateRecipePositive(){
	//authorization
	signIn("OlegK","password");
	
	//create recipe
	elementClick("//li[2]/a[@class='dropdown']");
	elementClick("//a[@href='/addRecipes']");
	//fill form
	fillField("Soup","//input[@class='add-recipe-title']");
	fillField("шампиньонов","//textarea [@class='forinputs']");
	fillField("1","//input[@name='cookingTimeHours']");
	fillField("15","//input[@name='cookingTimeMinutes']");
	fillField("10","//input[@name='quantityOfDish']");
	elementSelect("Украинская","//select[@name='cuisineId']");
	elementSelect("Первое","//select[@name='categoryId']");
	elementClick("//input[@value='острый']");
	fillField("соль","//input[@id='ingredientsNameList[0]']");
	elementClick("//input[@id='ingredientsCountList[0]']");
	driver.findElement(By.xpath("//input[@id='ingredientsCountList[0]']")).clear();
	fillField("15","//input[@id='ingredientsCountList[0]']");
	elementSelect("г","//select[@id='ingredientsTypeList[0]']");
	elementSelect("сложный","//select[@id='complexity']");
	fillField("бла бла бла","//textarea[@class='stepTextarea forinputs']");
	elementClick("//button[@id='OK']");	
}
@Test
public void testCheckRecipe(){
	//authorization
	signIn("OlegK","password");
	//check 	
	elementClick("//li[2]/a[@class='dropdown']");
	elementClick("//a[@href='/allOfRecipes']");
	elementClick("//div[@class='btn']//label[text()='Украинская']");
	elementClick("//div[@id='accordion2']/div[2]//a[@class]");
	elementClick("//div[text()='Soup'][1]");
	
}
@Test
public void testCreateRecipeNegative(){
	//authorization
	signIn("OlegK","password");
	
	elementClick("//li[2]/a[@class='dropdown']");
	elementClick("//a[@href='/addRecipes']");
	fillField("omelette","//input[@class='add-recipe-title']");
	elementClick("//button[@id='OK']");
	Assert.assertTrue(driver.findElement(By.xpath("//span[text()='Поле не должно быть пустым']")).isDisplayed());
	Assert.assertTrue(driver.findElement(By.xpath("//span[text()='Длина описания рецепта должна быть от 1 до 3000 символов.']")).isDisplayed());
	Assert.assertTrue(driver.findElement(By.xpath("//span[text()='Время приготовления должно быть указано.']")).isDisplayed());
	Assert.assertTrue(driver.findElement(By.xpath("//span[text()='Если не указаны часы времени приготовления, корректное значение для минут лежит в диапазоне от 6 минут до 59 минут (целые)']")).isDisplayed());
	Assert.assertTrue(driver.findElement(By.xpath("//span[text()='Вес готового блюда должнен быть указан (целое число), но не менее 25% и не более общего веса всех входящих в рецепт ингредиентов.']")).isDisplayed());
	Assert.assertTrue(driver.findElement(By.xpath("//span[text()='Кухня должна быть выбрана.']")).isDisplayed());
	Assert.assertTrue(driver.findElement(By.xpath("//span[text()='Категория должна быть выбрана.']")).isDisplayed());
	Assert.assertTrue(driver.findElement(By.xpath("//span[text()='Название ингредиента должно быть выбрано.']")).isDisplayed());
	Assert.assertTrue(driver.findElement(By.xpath("//span[text()='Количество ингредиента должно быть указано.']")).isDisplayed());
	Assert.assertTrue(driver.findElement(By.xpath("//span[text()='Единица измерения ингредиента должна быть выбрана.']")).isDisplayed());
	Assert.assertTrue(driver.findElement(By.xpath("//span[text()='Сложность приготовления блюда должна быть указана.']")).isDisplayed());
	Assert.assertTrue(driver.findElement(By.xpath("//span[text()='Длина описания шага должна быть от 1 до 3000 символов.']")).isDisplayed());
}
@Test
public void testCreateMenu(){
	//authorization
	signIn("OlegK","password");
	
	elementClick("//li[1]/a[@class='dropdown']");
	elementClick("//a[@href='/menu']");
	elementClick("//button[@value='#breakfast']");
	elementClick("//img[@alt='Украинская']");
	elementClick("//a[text()='Первое']");
	elementClick("//div[@class='btn draggable recepies_btn ui-draggable'][7]//label[text()='Soup  ']");
	//driver.switchTo().window(arg0);
	
}
}


