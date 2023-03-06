package org.languagetool.gui;
import org.junit.Assert;
import org.junit.Test;
import org.languagetool.JLanguageTool;
import org.languagetool.Language;
import org.languagetool.language.identifier.LanguageIdentifier;
import org.languagetool.language.identifier.LanguageIdentifierService;
import org.languagetool.rules.RuleMatch;
import org.languagetool.tools.Tools;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class FunctionalTestRevised {
  JFrame jf = new JFrame();
  JTextArea textArea = new JTextArea();
  LanguageIdentifier langIdentifier = LanguageIdentifierService.INSTANCE.getDefaultLanguageIdentifier(0, null, null, null);
  UndoRedoSupport undoRedo = new UndoRedoSupport(textArea, JLanguageTool.getMessageBundle());
  LanguageToolSupport ltSupport = new LanguageToolSupport(jf, textArea, undoRedo);

  @Test //0
  public void testLanguageToolCheckForNoText() throws IOException {
    List<RuleMatch> rules = ltSupport.getLanguageTool().check("");
    assertTrue(rules.size() == 0);
  }

  @Test //1
  public void testSetTextFromDefault(){
    textArea.setText("Today's whether is bad");
    assertEquals(ltSupport.getTextComponent().getText(), "Today's whether is bad");
  }

  @Test //2
  public void testLanguageChange() {
    textArea.setText("Aujourd'hui est-ce mauvais");
    Language newLang = langIdentifier.detectLanguage(textArea.getText());
    ltSupport.setLanguage(newLang);
    assertEquals(newLang, ltSupport.getLanguage());
  }

  @Test //2.5
  public void testDisableRuleAtReady() {
    ltSupport.disableRule("ENGLISH_WORD_REPEAT_RULE"); // disables rule
    Set<String> disabledRules = ltSupport.getLanguageTool().getDisabledRules();
    assertTrue(disabledRules.contains("ENGLISH_WORD_REPEAT_RULE")); // should be in disabled rules set because it was disabled
  }

  @Test //2.5
  public void testEnableRuleAtReady() {
    ltSupport.enableRule("ENGLISH_WORD_REPEAT_RULE");
    Set<String> disabledRules = ltSupport.getLanguageTool().getDisabledRules();
    assertTrue(!disabledRules.contains("ENGLISH_WORD_REPEAT_RULE")); // should not be in disabled rules set because it was not disabled
  }

  @Test //3
  public void testLanguageToolCheck() throws IOException {
    textArea.setText("This is a example input to to show you how LanguageTool works.");
    List<RuleMatch> rules = ltSupport.getLanguageTool().check("This is a example input to to show you how LanguageTool works.");
    assertTrue(rules.size() == 2);
  }

  @Test //6
  public void testDisableRuleAtFinish() {
    ltSupport.disableRule("ENGLISH_WORD_REPEAT_RULE"); // disables rule
    Set<String> disabledRules = ltSupport.getLanguageTool().getDisabledRules();
    assertTrue(disabledRules.contains("ENGLISH_WORD_REPEAT_RULE")); // should be in disabled rules set because it was disabled
  }

  @Test //6
  public void testEnableRuleAtFinish() {
    ltSupport.enableRule("ENGLISH_WORD_REPEAT_RULE");
    Set<String> disabledRules = ltSupport.getLanguageTool().getDisabledRules();
    assertTrue(!disabledRules.contains("ENGLISH_WORD_REPEAT_RULE")); // should not be in disabled rules set because it was not disabled
  }

  @Test //3.5
  public void testLanguageToolCheckFromFinish() throws IOException {
    List<RuleMatch> rules = ltSupport.getLanguageTool().check("This is a example input to to show you how LanguageTool works.");
    assertTrue(rules.size() == 2);
  }

  @Test //4
  public void testSetTextFromResult(){
    textArea.setText("Second test content");
    assertEquals(ltSupport.getTextComponent().getText(), "Second test content");
  }

  @Test
  public void testUpdateHighLights() throws IOException, NoSuchMethodException, NoSuchFieldException, InvocationTargetException, IllegalAccessException {
    Class ltClass = ltSupport.getClass();
    Method updateHighLights = ltClass.getDeclaredMethod("updateHighlights", List.class);
    updateHighLights.setAccessible(true);
    Field documentSpans = ltClass.getDeclaredField("documentSpans");
    documentSpans.setAccessible(true);

    List<RuleMatch> rules = ltSupport.getLanguageTool().check("This is a example input to to show you how LanguageTool works.");
    updateHighLights.invoke(ltSupport, new Object[]{rules});

    List SpanList = (List) documentSpans.get(ltSupport);
    assertEquals(SpanList.size(), rules.size());
  }

  @Test //5
  public void testDisableRuleFunction() {
    textArea.setText("This is is a test");
    ltSupport.disableRule("ENGLISH_WORD_REPEAT_RULE"); // disables rule
    Set<String> disabledRules = ltSupport.getLanguageTool().getDisabledRules();

    assertTrue(disabledRules.contains("ENGLISH_WORD_REPEAT_RULE")); // should be in disabled rules set because it was disabled
  }

  @Test //5
  public void testEnableRuleFunction() {
    textArea.setText("This is is a test");
    Set<String> disabledRules = ltSupport.getLanguageTool().getDisabledRules();

    assertTrue(!disabledRules.contains("ENGLISH_WORD_REPEAT_RULE")); // should not be in disabled rules set because it was not disabled
  }

}