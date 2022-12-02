/*
GanttProject is an opensource project management tool.
Copyright (C) 2002-2011 Dmitry Barashev, GanttProject Team

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
package net.sourceforge.ganttproject.action.task;

import net.sourceforge.ganttproject.action.GPAction;
import net.sourceforge.ganttproject.gui.UIFacade;
import net.sourceforge.ganttproject.gui.UIUtil;
import net.sourceforge.ganttproject.task.Task;
import net.sourceforge.ganttproject.task.TaskManager;
import net.sourceforge.ganttproject.task.TaskSelectionManager;

import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Date;
import java.util.List;

public class TaskDelayedFitlerAction extends TaskActionBase {

    public TaskDelayedFitlerAction(TaskManager taskManager, TaskSelectionManager selectionManager, UIFacade uiFacade) {
        super("task.delayedFilter", taskManager, selectionManager, uiFacade, null);
    }

    @Override
    protected String getIconFilePrefix() {
        return "filter_";
    }

    @Override
    protected boolean isEnabled(List<Task> selection) {
        return (selection.size() != 0);
    }

    @Override
    protected void run(List<Task> selection) throws Exception {
        for(Task t: getTaskManager().getTasks()) { //Reset Color
            t.setColor(getTaskManager().getTaskDefaultColorOption().getValue());
            if(t.isSupertask())
                t.setColor(Color.BLACK);
        }
        for(Task t: getTaskManager().getTasks()) {
            double complete = t.getCompletionPercentage()/100.0;
            int progressDays = (int)(complete * t.getDuration().getLength());
            Date progressDate = t.getStart().getTime();
            progressDate.setDate(progressDate.getDate() + progressDays);
            if(progressDate.before(new Date()) && new Date().compareTo(t.getStart().getTime()) >= 0 && progressDate.getDay() != new Date().getDay()) { //Delayed task
                t.setColor(Color.RED);
            }
        }
    }

    @Override
    public GPAction asToolbarAction() {
        final TaskDelayedFitlerAction result = new TaskDelayedFitlerAction(getTaskManager(), getSelectionManager(), getUIFacade());
        result.setFontAwesomeLabel(UIUtil.getFontawesomeLabel(result));
        this.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if ("enabled".equals(evt.getPropertyName())) {
                    result.setEnabled((Boolean)evt.getNewValue());
                }
            }
        });
        result.setEnabled(this.isEnabled());
        return result;
    }
}


