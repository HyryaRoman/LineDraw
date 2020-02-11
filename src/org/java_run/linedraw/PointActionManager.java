package org.java_run.linedraw;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class PointActionManager {
    //array of last actions
    private List<PointAction> pointActions;
    private int active_id;

    //setup array
    public PointActionManager(){
        pointActions=new ArrayList<>();
        active_id=0;
    }

    //add point
    public void add(Data data,Point point){
        if(pointActions.size()>=active_id){
            for (int i = 0; i < pointActions.size() - active_id; i++) {
                pointActions.remove(i);
            }
        }
        PointAction action=new PointAction();
        action.execute(data,point);
        pointActions.add(action);
        active_id=pointActions.indexOf(action)+1;
    }

    //undo
    public void undo(Data data){
        pointActions.get(active_id-1).unexecute(data);
        active_id--;
    }

    //redo
    public void redo(Data data){
        pointActions.get(active_id).reexecute(data);
        active_id++;
    }

    //action class
    public class PointAction {
        //point
        private Point point;

        //execute
        public void execute(Data data, Point point) {
            this.point=point;
            data.addPoint(point);
        }

        //reexecute
        public void reexecute(Data data) {
            data.addPoint(point);
        }

        //unexecute
        public void unexecute(Data data){
            data.removePoint(point);
        }
    }
}
