package org.java_run.linedraw;

import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Data implements Serializable {
    //List of saved points
    private List<Point> points;
    private Color line_color;
    private Color back_color;

    //Setup points list on Data creation
    public Data(){
        points=new ArrayList<>();
        line_color=new Color(0,0,0);
        back_color=new Color(0,0,0);
    }

    //Add point
    //Returns true if action is successfully
    public boolean addPoint(Point point){
        try {
            this.points.add(point);
            return true;
        }catch (Exception e){
            e.fillInStackTrace();
            return false;
        }
    }

    //Removes last point
    //Returns true if action is successfully
    public boolean removePoint(Point point){
        try {
            this.points.remove(point);
            return true;
        }catch (Exception e){
            e.fillInStackTrace();
            return false;
        }
    }

    //Set background color
    public void setBackground_color(Color back_color) {
        this.back_color = back_color;
    }

    //Set line color
    public void setLine_color(Color line_color) {
        this.line_color = line_color;
    }

    //Returns list of points
    public List<Point> getPoints(){
        return new ArrayList<>(points);
    }

    //Returns back color
    public Color getBackground_color() {
        return back_color;
    }

    //Returns line color
    public Color getLine_color() {
        return line_color;
    }
}
