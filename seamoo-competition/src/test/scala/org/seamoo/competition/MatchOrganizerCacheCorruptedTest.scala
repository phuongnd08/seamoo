package org.seamoo.competition

import org.powermock.core.classloader.annotations.PrepareForTest
import org.seamoo.test.PowerMockedGaeBddScenario
import org.testng.annotations.Test
import org.scalatest.testng.TestNGSuite

@PrepareForTest(value = Array(classOf[EntityFactory]))
class MatchOrganizerCacheCorruptedTest extends PowerMockedGaeBddScenario(new MatchOrganizerSteps) with TestNGSuite {
  @Test
  override def runScenario() {
  	super.runScenario()
  }

}
