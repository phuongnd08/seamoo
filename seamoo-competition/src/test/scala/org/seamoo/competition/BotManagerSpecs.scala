package org.seamoo.competition

import org.seamoo.persistence.test.LocalAppEngineTest
import org.springframework.core.io.DefaultResourceLoader
import org.seamoo.daos.MemberDao
import org.seamoo.entities.Member

import org.specs.Specification
import org.specs.mock.Mockito
import org.mockito.Matchers._
import java.io.InputStream
import org.seamoo.test.MockedTimeProvider

object BotManagerSpecs extends Specification with Mockito {
  var memberDao: MemberDao = null
  var botMgr: BotManager = null
  var is: InputStream = null
  var timeProvider: MockedTimeProvider = null

  "it" should {
    doBefore {
      LocalAppEngineTest.staticSetUp
      is = (new DefaultResourceLoader).getResource("classpath:bots.yaml").getInputStream
      memberDao = mock[MemberDao]
      botMgr = new BotManager
      botMgr.memberDao = memberDao
      timeProvider = new MockedTimeProvider
      botMgr.timeProvider = timeProvider
    }

    doAfter {
      LocalAppEngineTest.staticTearDown
    }

    "loadBots" >> {
      "do not create bots if exists" in {
        memberDao.findByOpenId(anyString) returns mock[Member]
        botMgr loadBots is
        botMgr.bots("http://seamoo.com/bot/apollo") mustNot beNull
        botMgr.bots("http://seamoo.com/bot/demeter") mustNot beNull
        there was no(memberDao).persist(any[Member])
        // memberDao.persist(any[Member]) wasnt called
      }

      "create bots if not exists" in {
        memberDao.findByOpenId(anyString) returns null
        botMgr loadBots is
        botMgr.bots("http://seamoo.com/bot/apollo") mustNot beNull
        botMgr.bots("http://seamoo.com/bot/demeter") mustNot beNull
        there was two(memberDao).persist(any[Member])
        // memberDao.persist(any[Member]) was called
      }
    }

    "getFirstAvailableBot" >> {
      var (apollo, demeter) = (mock[Member], mock[Member])
      doBefore{
        memberDao.findByOpenId("http://seamoo.com/bot/apollo") returns apollo
        memberDao.findByOpenId("http://seamoo.com/bot/demeter") returns demeter 
        botMgr loadBots is
      }
      "return the first added bot" in {
        botMgr.getFirstAvailableBot() must be equalTo (apollo)
        botMgr.getFirstAvailableBot() must be equalTo (null)
      }
      
      "return the idle bot" in {
        timeProvider setCurrentTimeStamp 1 
        botMgr.getFirstAvailableBot()
        timeProvider setCurrentTimeStamp (10*1000 + 2)
        botMgr.getFirstAvailableBot() must be equalTo (apollo)
      }
      
      "return the left bot" in {
        botMgr.getFirstAvailableBot() must be equalTo (apollo)
        apollo.getOpenId returns "http://seamoo.com/bot/apollo"
        botMgr.leaveBot(apollo)
        botMgr.getFirstAvailableBot() must be equalTo (apollo)
      }
    }
  }
}