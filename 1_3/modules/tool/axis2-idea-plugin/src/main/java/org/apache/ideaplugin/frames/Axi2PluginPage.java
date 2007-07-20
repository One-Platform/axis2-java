/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.ideaplugin.frames;

import com.intellij.openapi.project.Project;
import org.apache.axis2.tools.wizardframe.CodegenFrame;
import org.apache.axis2.tools.wizardframe.CodegenFrame;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Author: Deepal Jayasinghe
 * Date: Sep 24, 2005
 * Time: 10:41:41 AM
 */
public class Axi2PluginPage extends JFrame implements ActionListener {
    ButtonGroup cbg;
    JRadioButton service;
    JRadioButton codeGen;
    JButton butOK;
    JButton butCancle;
    JPanel imglbl;
    Project project;
    CodegenFrame codegenFrame;

    private int defaultCloseOperation;

    public Axi2PluginPage() {
        setBackground(Color.white);
        Dimension dim = getPreferredSize();
        setSize(dim);
        setBounds(200, 200, dim.width, dim.height);
        setBounds(200, 200, dim.width, dim.height);
        Axi2PluginPageLayout customLayout = new Axi2PluginPageLayout();

        setFont(new Font("Helvetica", Font.PLAIN, 12));
        getContentPane().setLayout(customLayout);
        setTitle("Axis2 Plugin");
        cbg = new ButtonGroup();

        /*Create a service archive Radio Button  */

        service = new JRadioButton("Axis2 Aervice Archive", true);
        service.setToolTipText("Hepls package classes, libs and WSDLs to create a archive that can be deployed in Axis2");
        cbg.add(service);
        getContentPane().add(service);

        /*Create a Code code generation Radio Button */

        codeGen = new JRadioButton("Axis2 Code Generator", false);
        codeGen.setToolTipText("you can generate a WSDL from a java source file or java code from a WSDL ");
        cbg.add(codeGen);
        getContentPane().add(codeGen);



        butOK = new JButton("OK");
        butOK.addActionListener(this);
        getContentPane().add(butOK);

        butCancle = new JButton("Cancel");
        butCancle.addActionListener(this);
        getContentPane().add(butCancle);

        imglbl = new LogoPage();
        getContentPane().add(imglbl);

    }



    public void setDefaultCloseOperation(int operation) {
        if (operation != DO_NOTHING_ON_CLOSE &&
                operation != HIDE_ON_CLOSE &&
                operation != DISPOSE_ON_CLOSE &&
                operation != EXIT_ON_CLOSE) {
            throw new IllegalArgumentException("defaultCloseOperation must be one of: DO_NOTHING_ON_CLOSE, HIDE_ON_CLOSE, DISPOSE_ON_CLOSE, or EXIT_ON_CLOSE");
        }
        if (this.defaultCloseOperation != operation) {
            if (operation == EXIT_ON_CLOSE) {
                SecurityManager security = System.getSecurityManager();
                if (security != null) {
                    security.checkExit(0);
                }
            }
            int oldValue = this.defaultCloseOperation;
            this.defaultCloseOperation = operation;
            firePropertyChange("defaultCloseOperation", oldValue, operation);
        }
    }

    public void showUI() {
        pack();
        this.setVisible(true);
        show();
    }

    public void setProject(Project project) {

        this.project = project;
    }


    public void actionPerformed(ActionEvent e) {
        Object obj = e.getSource();
        if (obj == butCancle) {
            this.hide();
            setVisible(false);
        } else if (obj == butOK) {
            this.hide();
            setVisible(false);
            if (codeGen.isSelected()) {
                codegenFrame = new CodegenFrame();
                codegenFrame.setProject(project);
		codegenFrame.setVisible(true);
                codegenFrame .setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            }else{
                ServiceArciveFrame window = new ServiceArciveFrame();
                window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                window.setResizable(false);
                window.setTitle("Service Archive creation");
                window.show();
            }
        }

    }

    public JComponent getRootComponent() {
        return this.getRootPane();
    }
}

class Axi2PluginPageLayout implements LayoutManager {

    public Axi2PluginPageLayout() {
    }

    public void addLayoutComponent(String name, Component comp) {
    }

    public void removeLayoutComponent(Component comp) {
    }

    public Dimension preferredLayoutSize(Container parent) {
        Dimension dim = new Dimension(0, 0);

        Insets insets = parent.getInsets();
        dim.width = 320 + insets.left + insets.right;
        dim.height = 264 + insets.top + insets.bottom;

        return dim;
    }

    public Dimension minimumLayoutSize(Container parent) {
        return new Dimension(0, 0);
    }

    public void layoutContainer(Container parent) {
        Insets insets = parent.getInsets();
        Component c;
        c = parent.getComponent(0);
        if (c.isVisible()) {
            c.setBounds(insets.left + 24, insets.top + 104, 296, 24);
        }
        c = parent.getComponent(1);
        if (c.isVisible()) {
            c.setBounds(insets.left + 24, insets.top + 136, 296, 24);
        }
        c = parent.getComponent(2);
        if (c.isVisible()) {
            c.setBounds(insets.left + 130, insets.top + 210, 80, 24);
        }
        c = parent.getComponent(3);
        if (c.isVisible()) {
            c.setBounds(insets.left + 215, insets.top + 210, 80, 24);
        }
        c = parent.getComponent(4);
        if (c.isVisible()) {
            c.setBounds(insets.left, insets.top, 320, 80);
        }
    }
}
