/*
GanttProject is an opensource project management tool. License: GPL3
Copyright (C) 2005-2011 Dmitry Barashev, GanttProject Team

This program is free software; you can redistribute it and/or
modify it under the terms of the GNU General Public License
as published by the Free Software Foundation; either version 3
of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package net.sourceforge.ganttproject.action.edit;

import net.sourceforge.ganttproject.IGanttProject;
import net.sourceforge.ganttproject.action.GPAction;
import net.sourceforge.ganttproject.gui.UIUtil;
import net.sourceforge.ganttproject.resource.HumanResource;
import net.sourceforge.ganttproject.undo.GPUndoListener;
import net.sourceforge.ganttproject.task.ResourceAssignment;

import javax.swing.event.UndoableEditEvent;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import  java.util.List;

public class FinishedTStatsAction extends GPAction implements GPUndoListener {

    private final IGanttProject myProject;
    private final List<ResourceAssignment> resourceAssignments = new ArrayList<>();

    public FinishedTStatsAction(IGanttProject project, IconSize size) {
        super("stats", size);
        myProject = project;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        List<HumanResource> resources = myProject.getHumanResourceManager().getResources();
        for (HumanResource r: resources) {
            for (ResourceAssignment a: r.getAssignments()) {
                System.out.println(a.getTask().getName());
            }
        }


        System.out.println(myProject.getHumanResourceManager().getResources());

    }

    @Override
    public void undoableEditHappened(UndoableEditEvent e) {
        updateTooltip();
    }

    @Override
    public void undoOrRedoHappened() {
        updateTooltip();
    }

    @Override
    protected String getLocalizedName() {
        return "";
    }

    @Override
    protected String getIconFilePrefix() {
        return "stats_";
    }

    @Override
    public FinishedTStatsAction asToolbarAction() {
        FinishedTStatsAction result = new FinishedTStatsAction(myProject, IconSize.MENU);
        result.setFontAwesomeLabel(UIUtil.getFontawesomeLabel(result));
        return result;
    }
}
