package org.java_run.linedraw;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public //Point renderer class
class Renderer extends JPanel {
    //List of points to render
    private java.util.List<Point> points;
    //Size of panel
    private int width, height;
    //Color of lines
    private Color line_color;
    private Color back_color;
    //set line color
    public void setLineColor(Color color){
        line_color=color;
    }
    //set background color
    public void setBackColor(Color color){
        back_color=color;
    }

    public void paint(Graphics g) {
        super.paint(g);
        g.setColor(back_color);
        g.fillRect(0,0,width,height);
        paintLines(g);
    }

    //paint only lines (needs for image export)
    public void paintLines(Graphics g) {
        g.setColor(line_color);
        Point _current;
        Point _next;
        for (int i = 0; i < points.size() - 1; i++) {
            _current = points.get(i);
            _next = points.get(i + 1);
            g.drawLine(_current.x, _current.y, _next.x, _next.y);
        }
    }

    //resize panel
    public void resizeRenderer(int width, int height) {
        this.width = width;
        this.height = height;
    }

    //update list of points to render
    public void updateRenderer(List<Point> points) {
        this.points = points;
    }

    //Create panel
    public Renderer() {
        width = 0;
        height = 0;
        points = new ArrayList<Point>();
        back_color=new Color(0,0,0);
        line_color=new Color(255,255,255);
    }
}

