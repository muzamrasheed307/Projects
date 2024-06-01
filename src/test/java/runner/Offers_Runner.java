package runner;


import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;





@CucumberOptions(
        features= {"feature_Offers"} //Path of the Features File
       

        ,plugin={"pretty","html:target/cucumber/Offers.html",
        "testng:test_output/cucumber.xml","rerun:test_output/rerun.txt"}//To generate different types of report ( "timeline:target/cucumber/")

        //"pretty","html:test_output/cucumber-html-report","json:test_output/cucumber.json","json:target/cucumber/cucumber.json",
        //        "testng:test_output/cucumber.xml","rerun:test_output/rerun.txt"

        , glue={"stepDefinition"} //Step Definition

       // , strict=false //To check if any step is not defined in the step definition file.
        , monochrome = true //Display the console output in a readable format.

        //, tags={"@Smoke"}
        //, tags={"@Reg"}
        // , tags={"@Smoke,@Reg"}

)
public class Offers_Runner extends AbstractTestNGCucumberTests{
}
