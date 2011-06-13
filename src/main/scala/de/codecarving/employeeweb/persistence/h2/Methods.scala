package de.codecarving.employeeweb
package persistence
package h2

import spiritrecord.SpiritMethods
import net.liftweb.mapper._

class SpiritEntryMethods[T] extends SpiritMethods[T] {

  import model.records.SpiritEntry


  def delete_!(inst: T): Boolean = {
    val in = inst.asInstanceOf[SpiritEntry]

    BackendEntry.findAll(
      By(BackendEntry.id,in.id.value)
    ).map(_.delete_!)

    //TODO Comments should actually not be deleted here! Need an idea?!
    BackendEntryComments.findAll.filter(_.entryId == in.id.value).map(_.delete_!)
    true
  }

  def save(inst: T): Boolean = {

    val in = inst.asInstanceOf[SpiritEntry]
    lazy val be = BackendEntry.create
    be.user.set(in.user.value)
    be._id_.set(in.id.value)
    be.displayName.set(in.displayName.value)
    be.semester.set(in.semester.value.mkString(";"))
    be.crdate.set(in.crdate.value)
    be.expires.set(in.expires.value)
    be.news.set(in.news.value)
    be.subject.set(in.subject.value)
    be.save
    true
  }

  def update(inst: T): Boolean = true

  /**
   * Building a List[SpiritRecord[_]] out of the H2 database.
   */
  def findAll(): List[T] = {

    lazy val be = BackendEntry.findAll

    be map { b =>
      val se = SpiritEntry.createRecord
      se.id.set(b.id.toString().toInt)
      se.user.set(b.user)
      se.displayName.set(b.displayName)
      se.crdate.set(b.crdate)
      se.expires.set(b.expires)
      se.news.set(b.news)
      se.semester.set(b.semester.split(";").toList)
      se.subject.set(b.subject)
      se.asInstanceOf[T]
    }
  }
}

class SpiritEntryCommentsMethods[T] extends SpiritMethods[T] {

  import model.records.SpiritEntryComments

  def delete_!(inst: T): Boolean = {

    BackendEntryComments.findAll(
      By(BackendEntryComments.id,inst.asInstanceOf[SpiritEntryComments].id.value)
    ).map(_.delete_!)
    true
  }

  def save(inst: T): Boolean = {
    val in = inst.asInstanceOf[SpiritEntryComments]
    lazy val be = BackendEntryComments.create
    be.user.set(in.user.value)
    be.entryId.set(in.id.value)
    be.crdate.set(in.crdate.value)
    be.comment.set(in.comment.value)
    be.save
    true
  }

  def update(inst: T): Boolean = true

  def findAll(): List[T] = {
    lazy val bec = BackendEntryComments.findAll
    bec map { b =>
      lazy val sec = SpiritEntryComments.createRecord
      sec.user.set(b.user)
      sec.id.set(b.entryId)
      sec.comment.set(b.comment)
      sec.asInstanceOf[T]
    }
  }
}

class SpiritPollMethods[T] extends SpiritMethods[T] {

  import model.records.SpiritPoll

  def delete_!(inst: T): Boolean = {
    val in = inst.asInstanceOf[SpiritPoll]

    BackendPoll.findAll(
      By(BackendPoll.title,in.title.value)
    ).map(_.delete_!)

    BackendPollAnswers.findAll(
      By(BackendPollAnswers.title,in.title.value)
    ).map(_.delete_!)

    true
  }

  def save(inst: T): Boolean = {
    val in = inst.asInstanceOf[SpiritPoll]
    lazy val bp = BackendPoll.create
    bp.title.set(in.title.value)
    bp.answerCount.set(in.answerCount.value)
    bp.expires.set(in.expires.value)
    bp.user.set(in.user.value)
    bp.displayName.set(in.displayName.value)
    bp.save
    true
  }

  def update(inst: T): Boolean = true

  def findAll(): List[T] = {
    lazy val bp = BackendPoll.findAll
    bp map { b =>
      lazy val sp = SpiritPoll.createRecord
      sp.title.set(b.title)
      sp.answerCount.set(b.answerCount)
      sp.expires.set(b.expires)
      sp.user.set(b.user)
      sp.displayName(b.displayName)
      sp.asInstanceOf[T]
    }
  }
}

class SpiritPollAnswerMethods[T] extends SpiritMethods[T] {

  import model.records.SpiritPollAnswers

  def delete_!(inst: T): Boolean = {

    val in = inst.asInstanceOf[SpiritPollAnswers]

    BackendPollAnswers.findAll(
      By(BackendPollAnswers.title,in.title.value)
    ).map(_.delete_!)
    true
  }

  def save(inst: T): Boolean = {
    val in = inst.asInstanceOf[SpiritPollAnswers]
    lazy val bpa = BackendPollAnswers.create
    bpa.title.set(in.title.value)
    bpa.answer.set(in.answer.value)
    bpa.votes.set(in.votes.value)
    bpa.save
    true
  }

  def update(inst: T): Boolean = true

  def findAll(): List[T] = {
    lazy val bpa = BackendPollAnswers.findAll
    bpa map { b =>
      lazy val spa = SpiritPollAnswers.createRecord
      spa.title.set(b.title)
      spa.answer.set(b.answer)
      spa.votes.set(b.votes)
      spa.asInstanceOf[T]
    }
  }
}

class SpiritTalkAllocatorMethods[T] extends SpiritMethods[T] {

  import model.records.SpiritTalkAllocator

  def delete_!(inst: T): Boolean = {
    val in = inst.asInstanceOf[SpiritTalkAllocator]

    BackendTalkAllocator.findAll(
      By(BackendTalkAllocator.title,in.title.value)
    ).map(_.delete_!)

    BackendTalkAllocatorTalks.findAll(
      By(BackendTalkAllocatorTalks.allocatorTitle,in.title.value)
    ).map(_.delete_!)

    true
  }

  def save(inst: T): Boolean = {
    val in = inst.asInstanceOf[SpiritTalkAllocator]
    lazy val bta = BackendTalkAllocator.create
    bta.user.set(in.user.value)
    bta.displayName.set(in.displayName.value)
    bta.title.set(in.title.value)
    bta.description.set(in.description.value)
    bta.released.set(in.released.value)
    bta.expires.set(in.expires.value)
    bta.save
    true
  }

  def update(inst: T): Boolean = true

  def findAll(): List[T] = {
    lazy val bta = BackendTalkAllocator.findAll
    bta map { b =>
      lazy val sta = SpiritTalkAllocator.createRecord
      sta.user.set(b.user)
      sta.displayName.set(b.displayName)
      sta.title.set(b.title)
      sta.description.set(b.description)
      sta.released.set(b.released)
      sta.expires.set(b.expires)
      sta.asInstanceOf[T]
    }
  }
}

class SpiritTalkAllocatorTalkMethods[T] extends SpiritMethods[T] {

  import model.records.SpiritTalkAllocatorTalks

  def delete_!(inst: T): Boolean = {
    val in = inst.asInstanceOf[SpiritTalkAllocatorTalks]
    BackendTalkAllocatorTalks.findAll(
      By(BackendTalkAllocatorTalks.talkTitle,in.talkTitle.value)
    ).map(_.delete_!)
    true
  }

  def save(inst: T): Boolean = {
    val in = inst.asInstanceOf[SpiritTalkAllocatorTalks]
    lazy val btat = BackendTalkAllocatorTalks.create
    btat.talkTitle.set(in.talkTitle.value)
    btat.allocatorTitle.set(in.allocatorTitle.value)
    btat.description.set(in.description.value)
    btat.assigned.set(in.assigned.value)
    btat.speakers.set(in.speakers.value.mkString(";"))
    btat.save
    true
  }

  def update(inst: T): Boolean = {
    val in = inst.asInstanceOf[SpiritTalkAllocatorTalks]
    val btat = BackendTalkAllocatorTalks.find(
      By(BackendTalkAllocatorTalks.talkTitle,in.talkTitle.value)
    ).openOr(BackendTalkAllocatorTalks.create)

    btat.talkTitle.set(in.talkTitle.value)
    btat.allocatorTitle.set(in.allocatorTitle.value)
    btat.description.set(in.description.value)
    btat.assigned.set(in.assigned.value)
    btat.speakers.set(in.speakers.value.mkString(";"))
    btat.save
  }

  def findAll(): List[T] = {
    lazy val btat = BackendTalkAllocatorTalks.findAll
    btat map { b =>
      lazy val stat = SpiritTalkAllocatorTalks.createRecord
      stat.talkTitle.set(b.talkTitle)
      stat.allocatorTitle.set(b.allocatorTitle)
      stat.description.set(b.description)
      stat.assigned.set(b.assigned)
      stat.speakers.set(b.speakers.split(";").toList)
      stat.asInstanceOf[T]
    }
  }
}
