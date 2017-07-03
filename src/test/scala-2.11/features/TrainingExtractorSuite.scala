package features

import org.scalatest.FunSuite

/**
  * Test the TrainingSuite against some example training data from SectLabel. These tests verify
  * particular manually selected features of the input data, intended to cover all the
  * information that should be extracted from the training data.
  */
class TrainingExtractorSuite extends FunSuite {
  val paras = TrainingExtractor.extract(getClass.getResourceAsStream("train.txt")).get

  test("First paragraph should be title") {
    assert(paras(0).tag.get === Title)
  }

  test("First paragraph should not be numbered") {
    assert(paras(0).numberHint === NumberOther)
  }

  test("First paragraph should not be email or web address") {
    assert(paras(0).netHint === NetOther)
  }

  test("First paragraph should have >= 5 words") {
    assert(paras(0).length === 5)
  }

  test("First paragraph should have second-largest font size") {
    assert(paras(0).fontSize === RelativeSize(1))
  }

  test("First paragraph should be bold") {
    assert(paras(0).isBold)
  }

  test("First paragraph should not be italic") {
    assert(!paras(0).isItalic)
  }

  test("First paragraph should not be bulletted") {
    assert(!paras(0).isBullet)
  }

  test("First paragraph should not be same as previous") {
    assert(!paras(0).isSameAsPrevious)
  }

  test("Paragraph 7 should be a section header") {
    assert(paras(7).tag.get === SectionHeader)
  }

  test("Paragraph 7 should have length 1") {
    assert(paras(7).length === 1)
  }

  test("Paragraph 8 contains an italic line so should be italic") {
    assert(paras(8).isItalic)
  }

  test("Paragraph 9 should be a section header") {
    assert(paras(9).tag.get == SectionHeader)
  }

  test("Paragraph 12 should be the common font size") {
    assert(paras(12).fontSize === Common)
  }

  test("Paragraph 12 should have the same format as previous") {
    assert(paras(12).isSameAsPrevious)
  }

  test("Paragraph 21 should be a possible subsection") {
    assert(paras(21).numberHint === PossibleSubsection)
  }

  test("Paragraph 21 should have a length >= 5") {
    // This paragraph is made up of two lines, each of which has a length of less than 5; this
    // test thus checks that the two lines are being treated as one paragraph, and the length is
    // being calculated correctly.
    assert(paras(21).length === 5)
  }

  test("Paragraph 21 should not have same format as previous") {
    assert(!paras(21).isSameAsPrevious)
  }
}
