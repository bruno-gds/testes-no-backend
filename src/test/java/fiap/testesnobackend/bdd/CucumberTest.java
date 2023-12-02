package fiap.testesnobackend.bdd;

import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;

/**
 * @author Bruno Gomes Damascena dos santos (bruno-gds) < brunog.damascena@gmail.com >
 * Date: 30/11/2023
 * Project Name: testes-no-backend
 */

@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("features")
public class CucumberTest {
}
