package org.languagetool.gui;

import org.junit.Test;
import org.languagetool.JLanguageTool;
import org.languagetool.rules.RuleMatch;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.io.IOException;
import java.util.List;
import java.util.ResourceBundle;

import static org.junit.Assert.assertTrue;


public class FunctionalModel {
  @Test
  public void testLanguageToolCheck() throws IOException {
    ResourceBundle messages = JLanguageTool.getMessageBundle();
    String inputSentence = "This is a example input to to show you how LanguageTool works.";
    JTextArea jTextArea = new JTextArea(inputSentence);
    UndoRedoSupport undoRedoSupport = new UndoRedoSupport(jTextArea, messages);
    LanguageToolSupport ltSupport = new LanguageToolSupport(new JFrame(), jTextArea, undoRedoSupport);

    List<RuleMatch> rules = ltSupport.getLanguageTool().check(inputSentence);
    assertTrue(rules.size() == 2);
  }


}

