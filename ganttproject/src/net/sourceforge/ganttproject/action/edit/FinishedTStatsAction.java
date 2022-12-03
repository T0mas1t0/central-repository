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

import net.sourceforge.ganttproject.action.GPAction;
import net.sourceforge.ganttproject.gui.UIUtil;
import net.sourceforge.ganttproject.undo.GPUndoListener;

import javax.swing.event.UndoableEditEvent;
import java.awt.event.ActionEvent;

public class FinishedTStatsAction extends GPAction implements GPUndoListener {

    public FinishedTStatsAction(IconSize size) {
        super("stats", size);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
    //TODO

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
        FinishedTStatsAction result = new FinishedTStatsAction(IconSize.MENU);
        result.setFontAwesomeLabel(UIUtil.getFontawesomeLabel(result));
        return result;
    }
}
