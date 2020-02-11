package org.java_run.linedraw;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.UndoableEditEvent;
import javax.swing.undo.UndoManager;
import javax.swing.undo.UndoableEdit;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;

public class LineDraw {
    //user interface
    private UI ui;
    //points data
    private Data data;
    //undo-Redo manager
    private PointActionManager pam;
    //File name filter
    FilenameFilter paf;
    //export background flag
    private boolean exportBackground=false;
    //Unsaved changes flag;
    private boolean have_unsaved_changes=false;
    //setup LineDraw
    public LineDraw(){
        ui=new UI();
        data=new Data();
        pam=new PointActionManager();
        paf=new FilenameFilter() {
            public boolean accept(File dir, String name) {
                return name.endsWith(".paf");//Point Array File
            }
        };
    }
    //main cycle manipulations------------------------
    public void run(String[] args){
        //setup UI
        ui.addListener(this::event);
        ui.start();
        if(args.length>0) {
            data = DataSaver.load(args[0]);
            if (data == null) {
                JOptionPane.showMessageDialog(ui.getFrame(), "Failed to open a file!", "ERROR", JOptionPane.ERROR_MESSAGE);
                data = new Data();
            }else {
                ui.update(data.getPoints());
                ui.setBackgroundColor(data.getBackground_color());
                ui.setLineColor(data.getLine_color());
            }
        }
    }
    public void stop(){
        unsaved_actions_check();
        ui.stop();
        System.runFinalization();
        System.exit(0);
    }
    //------------------------------------------------

    //Data manipulations==============================
    public void add(Point point){
        pam.add(data,point);
        ui.update(data.getPoints());
        have_unsaved_changes=true;
    }//add NEW point
    public void redo(){
        pam.redo(data);
        ui.update(data.getPoints());
        have_unsaved_changes=true;
    }//redo action
    public void undo(){
        pam.undo(data);
        ui.update(data.getPoints());
        have_unsaved_changes=true;
    }//undo action
    //================================================

    //catch events
    public void event(String event,Object object){
        switch (event){
            case "[WINDOW_CLOSING]"://on window close
                stop();
                break;
            case "_new"://new file
                newFile();
                break;
            case "_open"://open file
                openFile();
                break;
            case "_save"://save file
                saveFile();
                break;
            case "_exp"://export
                exportFile();
                break;
            case "_exit"://exit
                exit();
                break;
            case "_undo"://undo
                undo();
                break;
            case "_redo"://redo
                redo();
                break;
            case "_slc"://select line color
                slc();
                break;
            case "_sbc"://select back color
                sbc();
                break;
            case "_eb"://export back
                eb(((boolean)object));
                break;
            case "[MOUSE_PRESSED]"://renderers mouse pressed
                add(((MouseEvent)object).getPoint());
                break;
            default://another action
                break;
        }
    }

    public void newFile(){
        unsaved_actions_check();
        data=new Data();
        ui.update(data.getPoints());
        ui.setBackgroundColor(data.getBackground_color());
        ui.setLineColor(data.getLine_color());
    }
    public void openFile(){
        unsaved_actions_check();
        FileDialog f=new FileDialog(ui.getFrame(),"Open",FileDialog.LOAD);
        f.setVisible(true);
        String file=f.getFile();
        String directory=f.getDirectory();
        if(file!=null&directory!=null){
            String path=directory+file;
            data=DataSaver.load(path.endsWith(".paf")?path:path+".paf");
            if(data==null){
                JOptionPane.showMessageDialog(ui.getFrame(),"Failed to save a file!","ERROR",JOptionPane.ERROR_MESSAGE);
                data=new Data();
            }else{
                have_unsaved_changes=false;
            }
        }
        ui.update(data.getPoints());
        ui.setBackgroundColor(data.getBackground_color());
        ui.setLineColor(data.getLine_color());
    }
    public void saveFile(){
        FileDialog f=new FileDialog(ui.getFrame(),"Save",FileDialog.SAVE);
        f.setFile("untitled.paf");
        f.setVisible(true);
        String file=f.getFile();
        String directory=f.getDirectory();
        if(file!=null&directory!=null){
            String path=directory+file;
            if(!DataSaver.save(path.endsWith(".paf")?path:path+".paf",data)){
                JOptionPane.showMessageDialog(ui.getFrame(),"Failed to save a file!","ERROR",JOptionPane.ERROR_MESSAGE);
            }else{
                have_unsaved_changes=false;
            }
        }
    }
    public void exportFile(){
        FileDialog f=new FileDialog(ui.getFrame(),"Export",FileDialog.SAVE);
        f.setFile("untitled.png");
        f.setVisible(true);
        String file=f.getFile();
        String directory=f.getDirectory();
        if(file!=null&directory!=null){
            String path=directory+file;
            path=path.endsWith(".png")?path:path+".png";
            try {
                ImageIO.write(ui.getImage(exportBackground),"png",new File(path));
            } catch (IOException e) {
                JOptionPane.showMessageDialog(ui.getFrame(),"Failed to export to image","ERROR",JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    public void exit(){
        stop();
    }
    public void slc(){//select line color
        Color sel_col=JColorChooser.showDialog(null,"Choose line color",data.getLine_color());
        if(sel_col==null)return;
        data.setLine_color(sel_col);
        ui.setLineColor(sel_col);
        ui.update(data.getPoints());
        have_unsaved_changes=true;
    }
    public void sbc(){//select back color
        Color sel_col=JColorChooser.showDialog(null,"Choose line color",data.getBackground_color());
        if(sel_col==null)return;
        data.setBackground_color(sel_col);
        ui.setBackgroundColor(sel_col);
        ui.update(data.getPoints());
        have_unsaved_changes=true;
    }
    public void eb(boolean b){//export background
        this.exportBackground=b;
    }
    //if have unsaved actions
    public void unsaved_actions_check(){
        if(have_unsaved_changes){
            int action=JOptionPane.showConfirmDialog(ui.getFrame(),"You have unsaved actions.\nYou can save it now.","Unsaved actions",JOptionPane.YES_NO_OPTION,JOptionPane.WARNING_MESSAGE);
            if(action==JOptionPane.NO_OPTION)return;
            saveFile();
        }
    }
}
