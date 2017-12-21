/*
 * IzPack - Copyright 2001-2008 Julien Ponge, All Rights Reserved.
 *
 * http://izpack.org/
 * http://izpack.codehaus.org/
 *
 * Copyright 2002 Jan Blok
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.izforge.izpack.panels.dependency;

import com.izforge.izpack.api.data.InstallData;
import com.izforge.izpack.api.data.Panel;
import com.izforge.izpack.api.resource.Resources;
import com.izforge.izpack.installer.console.AbstractTextConsolePanel;
import com.izforge.izpack.installer.console.ConsolePanel;
import com.izforge.izpack.installer.panel.PanelView;

import java.util.ArrayList;

/**
 * HTML Licence Panel console helper
 */
public class DependencyConsolePanel extends AbstractTextConsolePanel
{

    private InstallData installData;
    private Resources resources;
    private Panel metaData;

    /**
     * Constructs an <tt>DependencyConsolePanel</tt>.
     *
     * @param panel     the parent panel/view. May be {@code null}
     * @param resources the resources
     */
    public DependencyConsolePanel(PanelView<ConsolePanel> panel, Resources resources, InstallData installData, Panel metaData)
    {
        super(panel);
        this.installData = installData;
        this.resources = resources;
        this.metaData = metaData;
    }

    /**
     * Returns the text to display.
     *
     * @return the text. A <tt>null</tt> indicates failure
     */
    @Override
    protected String getText()
    {

        final String dependencyId = DependencyPanelUtils.getId(metaData);
        final String dependencySite = DependencyPanelUtils.getDependencySite(installData, dependencyId);
        final ArrayList<String> dependencyList = DependencyPanelUtils.getDependencies(installData, dependencyId);
        final ArrayList<DependencyPanelTest> dependencyTests = DependencyPanelUtils.getDependencyTests(installData, dependencyId);
        String dependencyText = DependencyPanelUtils.getDependencyText(dependencyId, resources);

        if (DependencyPanelUtils.isDependencySatisfied(dependencyList, dependencyTests)) {
            return null;
            // @todo skip panel
        }

        dependencyText += "You must install this dependency from " + dependencySite +
            " and add it to your system path or the product may not work correctly!\n";

        return dependencyText;
    }

}
