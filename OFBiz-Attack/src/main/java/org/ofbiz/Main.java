package org.ofbiz;

import org.ofbiz.listener.CmdExecuteListener;
import org.ofbiz.listener.MemshellInjectListener;
import org.ofbiz.listener.VulCheckListener;

import javax.swing.*;
import javax.swing.plaf.basic.BasicTabbedPaneUI;
import java.awt.*;
import java.util.Enumeration;

public class Main extends JFrame {
    private static final String WELCOME = "[+] Welcome to OFBiz Attack Tool\n";
    private static final String AUTHOR = "[+] Author: Nivia\n";

    private JTextArea outputTextArea;

    public Main() {
        setTitle("OFBiz Attack Tool");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setUI(new CustomTabbedPaneUI());

        JPanel outputPanel = new JPanel(new BorderLayout());
        outputTextArea = new JTextArea(20, 30);
        JScrollPane scrollPane = new JScrollPane(outputTextArea);
        outputPanel.add(scrollPane, BorderLayout.CENTER);

        outputTextArea.append(WELCOME);
        outputTextArea.append(AUTHOR);
        outputTextArea.append("[+] Check first\n");

        tabbedPane.addTab("Vul Check", createCheckPage());
        tabbedPane.addTab("CMD Execute", createCmdPage());
        tabbedPane.addTab("Memshell Inject", createMemshellPage());

        add(tabbedPane, BorderLayout.CENTER);
        add(outputPanel, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private JPanel createCheckPage() {
        JPanel pagePanel = new JPanel();
        pagePanel.setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        pagePanel.add(inputPanel, BorderLayout.NORTH);

        JLabel urlLabel = new JLabel("Target Url:");
        inputPanel.add(urlLabel);

        JTextField urlText = new JTextField(30);
        inputPanel.add(urlText);

        JButton checkButton = new JButton("Check");
        checkButton.addActionListener(new VulCheckListener(urlText, outputTextArea));
        inputPanel.add(checkButton);

        return pagePanel;
    }

    private JPanel createCmdPage() {
        JPanel pagePanel = new JPanel();
        pagePanel.setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        pagePanel.add(inputPanel, BorderLayout.NORTH);

        JLabel urlLabel = new JLabel("Command:");
        inputPanel.add(urlLabel);

        JTextField cmdText = new JTextField(30);
        inputPanel.add(cmdText);

        JButton executeButton = new JButton("Execute");
        executeButton.addActionListener(new CmdExecuteListener(cmdText, outputTextArea, VulCheckListener.getTextField()));
        inputPanel.add(executeButton);

        return pagePanel;
    }

    private JPanel createMemshellPage() {
        JPanel pagePanel = new JPanel();
        pagePanel.setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.X_AXIS));
        pagePanel.add(inputPanel, BorderLayout.NORTH);

        ButtonGroup buttonGroup = new ButtonGroup();

        JRadioButton cmdMemshellButton = new JRadioButton("CMD (Visit /webtools/*)");
        JRadioButton behinderMemshellButton = new JRadioButton("Behinder (Default key)");

        buttonGroup.add(cmdMemshellButton);
        buttonGroup.add(behinderMemshellButton);

        inputPanel.add(cmdMemshellButton);
        inputPanel.add(behinderMemshellButton);

        JButton injectButton = new JButton("Inject");

        injectButton.addActionListener(new MemshellInjectListener(buttonGroup, outputTextArea, VulCheckListener.getTextField()));
        inputPanel.add(injectButton);

        return pagePanel;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Main::new);
    }

    static class CustomTabbedPaneUI extends BasicTabbedPaneUI {
        private static final Color selectedTabColor = Color.WHITE;
        private static final Color selectedTabTitleColor = Color.BLACK;

        @Override
        protected void paintTabBackground(Graphics g, int tabPlacement, int tabIndex, int x, int y, int w, int h, boolean isSelected) {
            if (isSelected) {
                g.setColor(selectedTabColor);
                g.fillRect(x, y, w, h);
            } else {
                super.paintTabBackground(g, tabPlacement, tabIndex, x, y, w, h, isSelected);
            }
        }

        @Override
        protected void paintText(Graphics g, int tabPlacement, Font font, FontMetrics metrics, int tabIndex, String title, Rectangle textRect, boolean isSelected) {
            if (isSelected) {
                g.setColor(selectedTabTitleColor);
            } else {
                g.setColor(tabPane.getForegroundAt(tabIndex));
            }

            super.paintText(g, tabPlacement, font, metrics, tabIndex, title, textRect, isSelected);
        }
    }
}