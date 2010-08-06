package org.seamoo.competition

import org.seamoo.entities.Member
import org.seamoo.utils.TimeProvider
import org.springframework.beans.factory.annotation.Autowired
import org.yaml.snakeyaml.Yaml
import java.io.InputStream
import org.seamoo.daos.MemberDao
import scala.collection.JavaConversions._
import java.util.HashMap
import scala.collection.mutable.Map

class BotManager {
  @Autowired
  var timeProvider: TimeProvider = _
  @Autowired
  var memberDao: MemberDao = _
  var bots = Map[String, Member]()
  var botLastUsed = Map[String, Long]()
  val BOT_MAX_IDLE_TIME = 10 * 1000 // 10 seconds

  def getFirstAvailableBot(): Member = {
    for((botId, bot) <- bots){
      if (!botLastUsed.contains(botId) || (botLastUsed(botId) + BOT_MAX_IDLE_TIME < timeProvider.getCurrentTimeStamp)) {
        botLastUsed += (botId -> timeProvider.getCurrentTimeStamp)
        return bot
      }
    }
    null
  }

  def loadBots(is: InputStream) {
    var allData = (new Yaml).load(is)
    var map = allData.asInstanceOf[HashMap[String, Any]]
    for ((key, d) <- map) {
      val data = d.asInstanceOf[HashMap[String, Any]]
      val openId: String = data("openId").toString
      var bot = memberDao findByOpenId openId
      if (bot == null) {
        bot = new Member
        bot.setOpenId(openId)
        bot.setDisplayName(data("displayName").toString)
        memberDao.persist(bot)
      }
      bots += (openId -> bot)
    }
  }

  // inform that the bot is still used
  def touchBot(bot: Member) {
    botLastUsed(bot.getOpenId) = timeProvider.getCurrentTimeStamp
  }

  // inform that the bot has finished its mission
  def leaveBot(bot: Member) {
    botLastUsed(bot.getOpenId) = 0    
  }
}
