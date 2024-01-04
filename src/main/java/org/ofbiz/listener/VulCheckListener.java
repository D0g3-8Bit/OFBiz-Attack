package org.ofbiz.listener;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import static org.ofbiz.util.Check.*;

public class VulCheckListener implements ActionListener {
    private static JTextField textField;
    private JTextArea outputTextArea;
    public static boolean hasCheck = false;
    public static boolean has_cve_2023_49070 = false;

    public VulCheckListener(JTextField textField, JTextArea outputTextArea) {
        this.textField = textField;
        this.outputTextArea = outputTextArea;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String url = textField.getText();

        if(!isValidHttpUrl(url)){
            JOptionPane.showMessageDialog(textField.getParent(), "URL format is not valid!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            if(has_CVE_2023_49070(url)){
                hasCheck = true;
                has_cve_2023_49070 = true;
                outputTextArea.append("[+] has CVE-2023-49070, Attack!\n");
                outputTextArea.append("[+] has CVE-2023-51467, Attack!\n");
                return;
            }
            else {
                hasCheck = false;
                outputTextArea.append("[-] no CVE-2023-49070 :(\n");
            }

            if (has_CVE_2023_51467(url)){
                hasCheck = true;
                outputTextArea.append("[+] has CVE-2023-51467, Attack!\n");
            }
            else {
                hasCheck = false;
                outputTextArea.append("[-] no CVE-2023-51467 :(\n");
            }


        } catch (Exception ex) {

        }
    }

    public static JTextField getTextField(){
        return textField;
    }

}
