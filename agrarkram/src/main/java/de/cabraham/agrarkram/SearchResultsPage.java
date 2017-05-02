package de.cabraham.agrarkram;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class SearchResultsPage {
  
  
  static PageResult processSearchResultsPage(WebDriver m_driver) {
    PageResult pageResult = new PageResult();
    
    //find page X of Y
    final int nCurPage = Integer.valueOf(m_driver.findElement(By.xpath("//input[@class='listNavTxtPage']")).getAttribute("value"));
    final String strSeiteVonX = m_driver.findElement(By.xpath("//*[input[@class='listNavTxtPage']]")).getText();
    final Matcher m = Pattern.compile("Seite\\s+von\\s+(\\d+)").matcher(strSeiteVonX);
    m.find();
    final int nTotalPages = Integer.valueOf(m.group(1));
    
    
    List<WebElement> lstBegButtons = m_driver.findElements(By.xpath("//button[@class='linkBeg']"));
    int nCount = lstBegButtons.size();
    //Log.debug("buttons on page: "+nCount);
    int n = 0;
    while(n<nCount) {
      if(n>0){
        //after the first button we changed the current website so the ids of the old list are not valid anymore
        lstBegButtons = m_driver.findElements(By.xpath("//button[@class='linkBeg']"));
      }
      Log.debug("Button "+(n+1)+" of "+nCount);
      //click the entry, parse it, then go back to the search result page
      WebElement btn = lstBegButtons.get(n);
      btn.click();
      final DetailedResult singleResult = SingleResultPage.parseSingleResultPage(m_driver);
      pageResult.lstResult.add(singleResult);
      m_driver.navigate().back();
      if(n==nCount-1) {//end reached
        pageResult.bButWaitTheresMore = nCurPage<nTotalPages;
        return pageResult;
      }
      n++;
      
    }
    return pageResult;
  }

}