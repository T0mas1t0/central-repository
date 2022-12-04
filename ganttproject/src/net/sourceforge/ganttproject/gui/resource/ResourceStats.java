/*
GanttProject is an opensource project management tool. License: GPL3
Copyright (C) 2011 Dmitry Barashev

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
package net.sourceforge.ganttproject.gui.resource;

import net.sourceforge.ganttproject.IGanttProject;
import net.sourceforge.ganttproject.resource.HumanResource;
import net.sourceforge.ganttproject.task.ResourceAssignment;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.plaf.basic.BasicScrollBarUI;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.text.DecimalFormat;

public class ResourceStats {

    private JFrame frame;

    private GPanel[] infoPanel;

    private int currentPanel;

    private JTable table;

    private JScrollPane tableSP;

    private Border br;

    private Container container;

    private IGanttProject project;

    public ResourceStats(IGanttProject project) {
        this.project = project;
        initFrameWithContainer();
        initResourceTable();
        initScrollPane();
        initInfoPanel();
    }

    class Slice {
        double value;
        Color color;

        public Slice(double value, Color color) {
            this.value = value;
            this.color = color;
        }
    }

    class GPanel extends JPanel {

        private HumanResource resource;

        private int participTasks;

        private int participConclTasks;

        private int nTasks;

        public GPanel(HumanResource resource) {
            this.resource = resource;
            this.participTasks = 0;
            this.participConclTasks = 0;
            this.nTasks = 0;
        }

        private Slice[] getSlices() {
            nTasks = project.getTaskManager().getTasks().length;
            ResourceAssignment[] assignments = resource.getAssignments().clone();
            for(ResourceAssignment ra: assignments) {
                if(ra.getTask().getCompletionPercentage() == 100)
                    participConclTasks++;
                else
                    participTasks++;
            }

            Slice[] slices = new Slice[3];
            slices[0] = new Slice(participConclTasks, Color.GREEN);
            slices[1] = new Slice(participTasks, Color.ORANGE);
            slices[2] = new Slice(nTasks - participConclTasks - participTasks, Color.RED);
            return slices;
        }

        @Override
        public void paintComponent(Graphics g) {
            drawPie((Graphics2D) g,
                    new Rectangle(frame.getWidth() / 3, 0, frame.getWidth() - frame.getWidth() / 2, frame.getHeight() - 45),
                    getSlices());
        }

        void drawPie(Graphics2D g, Rectangle area, Slice[] slices) {
            g.setColor(frame.getBackground());
            g.fillRect(0,0, frame.getWidth(), frame.getHeight());
            int x = frame.getWidth()/10;
            int y = frame.getHeight()/3;
            g.setColor(Color.RED);
            g.fillRect(x - 12, y - 13, 10,10);
            g.setColor(Color.ORANGE);
            g.fillRect(x - 12, y + 5, 10,10);
            g.setColor(Color.GREEN);
            g.fillRect(x - 12, y + 27, 10,10);
            g.setColor(Color.BLACK);
            DecimalFormat df = new DecimalFormat("###.#");
            g.drawString("Tarefas n\u00E3o participadas " + df.format(100.0 * (nTasks - participConclTasks - participTasks) / nTasks) + "%", x ,y);
            g.drawString("Tarefas participadas e n\u00E3o conclu\u00EDdas " + df.format(100.0 * participTasks / nTasks) + "%",x,y + 20);
            g.drawString("Tarefas participadas e conclu\u00EDdas " + df.format(100.0 * participConclTasks/ nTasks) + "%",x, y + 40);


            double total = 0.0D;
            for (Slice slice : slices) {
                total += slice.value;
            }
            double curValue = 0.0D;
            int startAngle = 0;
            for (Slice slice : slices) {
                startAngle = (int) (curValue * 360 / total);
                int arcAngle = (int) (slice.value * 360 / total);
                g.setColor(slice.color);
                g.fillArc(area.x, area.y, area.width, area.height,
                        startAngle, arcAngle);
                curValue += slice.value;
            }
        }


    }

    private void initInfoPanel() {
        HumanResource[] resources = project.getHumanResourceManager().getResourcesArray().clone();
        infoPanel = new GPanel[resources.length];
        for(int i = 0; i < infoPanel.length; i++) {
            infoPanel[i] = new GPanel(resources[i]);
            infoPanel[i].setBounds(frame.getWidth() / 10, 0, frame.getWidth() - frame.getWidth() / 10, frame.getHeight() - 45);
            infoPanel[i].setBorder(br);
            container.add(infoPanel[i]);
            infoPanel[i].setVisible(false);
        }
    }

    private void initScrollPane() {
        tableSP = new JScrollPane(table);
        tableSP.setBounds(0, 0, frame.getWidth() / 10, frame.getHeight());
        tableSP.setBorder(br);
        tableSP.getVerticalScrollBar().setUI(new BasicScrollBarUI() {
            @Override
            protected void configureScrollBarColors() {
                this.thumbColor = new Color(50, 155, 148);
            }
        });
        container.add(tableSP);
    }

    private void initResourceTable() {
        HumanResource[] resources = project.getHumanResourceManager().getResourcesArray().clone();
        String[][] data = new String[resources.length][1];
        for (int i = 0; i < resources.length; i++) {
            data[i][0] = resources[i].getId() + ": " + resources[i].getName();
        }

        table = new JTable(data, new String[]{"Resources"}) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }

            public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                Component returnComp = super.prepareRenderer(renderer, row, column);
                Color alternateColor = new Color(0.807f, 0.807f, 0.807f);
                Color whiteColor = Color.WHITE;
                if (!returnComp.getBackground().equals(getSelectionBackground())) {
                    Color bg = (row % 2 != 0 ? alternateColor : whiteColor);
                    returnComp.setBackground(bg);
                    bg = null;
                }
                return returnComp;
            }
        };
        table.getTableHeader().setResizingAllowed(false);
        table.getTableHeader().setReorderingAllowed(false);
        table.getTableHeader().setOpaque(false);
        table.getTableHeader().setBackground(new Color(50, 155, 148));
        table.getTableHeader().setFont(new Font("Dialog", Font.PLAIN, 14));
        table.setFont(new Font("Dialog", Font.PLAIN, 12));
        table.setRowHeight(25);
        table.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int row = table.rowAtPoint(evt.getPoint());
                int col = table.columnAtPoint(evt.getPoint());
                for (GPanel gp : infoPanel)
                    gp.setVisible(false);
                if (row >= 0 && col == 0) {
                    currentPanel = row;

                    infoPanel[currentPanel].setVisible(true);
                }
                tableSP.setBounds(0, 0, frame.getWidth() / 10, frame.getHeight() - 45);
                tableSP.setBorder(br);
                if(infoPanel.length > 0) {
                    infoPanel[currentPanel].setBounds(frame.getWidth() / 10, 0, frame.getWidth() - frame.getWidth() / 10, frame.getHeight());
                    infoPanel[currentPanel].setBorder(br);
                }
            }
        });
    }

        private void initFrameWithContainer() {
            frame = new JFrame();
            frame.setLayout(null);
            frame.setSize(1500, 750);
            frame.setTitle("Estat\u00EDsticas dos Recursos");
            frame.setIconImage(new ImageIcon(getClass().getResource("/icons/stats_16.png")).getImage());
            frame.setLocationRelativeTo(null);
            frame.addComponentListener(new ComponentAdapter() {
                public void componentResized(ComponentEvent componentEvent) {
                    tableSP.setBounds(0, 0, frame.getWidth() / 10, frame.getHeight() - 45);
                    tableSP.setBorder(br);
                    if(infoPanel.length > 0) {
                        infoPanel[currentPanel].setBounds(frame.getWidth() / 10, 0, frame.getWidth() - frame.getWidth() / 10, frame.getHeight());
                        infoPanel[currentPanel].setBorder(br);
                    }
                }
            });

            br = BorderFactory.createLineBorder(Color.black);
            container = frame.getContentPane();
        }

        public void show() {
            frame.setVisible(true);
        }

    }