package org.java_run.linedraw;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class UI {
    //Window of program
    private JFrame frame;
    //Point renderer
    private Renderer renderer;
    //UI event manager
    private EventManager em;
    //image update timer
    private Timer timer;

    //on UI creation setup frame and renderer
    public UI() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
        frame = new JFrame();
        frame.setTitle("LineDraw");
        frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                em.notify("[WINDOW_CLOSING]",e);
            }
        });
        renderer = new Renderer();
        em = new EventManager();
        timer=new Timer(1000/60,(e)->
        {
            renderer.resizeRenderer(frame.getWidth(),frame.getHeight());
            renderer.repaint();
        });
        resize(750,500);
        setup();
    }

    //setup UI
    public void setup() {
        JMenuBar menuBar = new JMenuBar();

        //file menu----------------------------------------------------------------
        JMenu file = new JMenu("File");

        //new file=================================================================
        {JMenuItem _new = new JMenuItem("New project");
        _new.addActionListener((e) -> {
            em.notify("_new", e);
        });
        _new.setAccelerator(KeyStroke.getKeyStroke('N', KeyEvent.CTRL_DOWN_MASK));
        file.add(_new);}
        //=========================================================================

        //open file================================================================
        {JMenuItem _open = new JMenuItem("Open project");
        _open.addActionListener((e) -> {
            em.notify("_open", e);
        });
        _open.setAccelerator(KeyStroke.getKeyStroke('O', KeyEvent.CTRL_DOWN_MASK));
        file.add(_open);}
        //=========================================================================

        //save file================================================================
        {JMenuItem _save = new JMenuItem("Save project");
        _save.addActionListener((e) -> {
            em.notify("_save", e);
        });
        _save.setAccelerator(KeyStroke.getKeyStroke('S', KeyEvent.CTRL_DOWN_MASK));
        file.add(_save);}
        //=========================================================================

        //separator================================================================
        {JSeparator _s = new JSeparator();
        file.add(_s);}
        //=========================================================================

        //export================================================================
        {JMenuItem _exp = new JMenuItem("Export");
        _exp.addActionListener((e) -> {
            em.notify("_exp", e);
        });
        _exp.setAccelerator(KeyStroke.getKeyStroke('E', KeyEvent.CTRL_DOWN_MASK));
        file.add(_exp);}
        //=========================================================================

        //separator================================================================
        {JSeparator _s = new JSeparator();
        file.add(_s);}
        //=========================================================================

        //exit=====================================================================
        {JMenuItem _exit = new JMenuItem("Exit");
        _exit.addActionListener((e) -> {
            em.notify("_exit", e);
        });
        file.add(_exit);}
        //=========================================================================

        menuBar.add(file);
        //-------------------------------------------------------------------------


        //edit menu----------------------------------------------------------------
        JMenu edit = new JMenu("Edit");

        //undo=====================================================================
        {JMenuItem _undo = new JMenuItem("Undo");
        _undo.addActionListener((e) -> {
            em.notify("_undo", e);
        });
        _undo.setAccelerator(KeyStroke.getKeyStroke('Z', KeyEvent.CTRL_DOWN_MASK));
        edit.add(_undo);}
        //=========================================================================

        //redo=====================================================================
        {JMenuItem _redo = new JMenuItem("Redo");
        _redo.addActionListener((e) -> {
            em.notify("_redo", e);
        });
        _redo.setAccelerator(KeyStroke.getKeyStroke('Z', KeyEvent.CTRL_DOWN_MASK+KeyEvent.SHIFT_DOWN_MASK));
        edit.add(_redo);}
        //=========================================================================

        //separator================================================================
        {JSeparator _s = new JSeparator();
        edit.add(_s);}
        //=========================================================================

        //select line color=================================================================
        {JMenuItem _slc = new JMenuItem("Select line color");
        _slc.addActionListener((e) -> {
            em.notify("_slc", e);
        });
        edit.add(_slc);}
        //=========================================================================

        //select background color================================================================
        {JMenuItem _sbc = new JMenuItem("Select background color");
        _sbc.addActionListener((e) -> {
            em.notify("_sbc", e);
        });
        edit.add(_sbc);}
        //=========================================================================

        //export background================================================================
        {JCheckBoxMenuItem _eb = new JCheckBoxMenuItem("Export background");
        _eb.addActionListener((e) -> {
            em.notify("_eb", _eb.getState());
        });
        edit.add(_eb);}
        //=========================================================================

        menuBar.add(edit);
        //-------------------------------------------------------------------------

        //add menu to frame
        frame.setJMenuBar(menuBar);

        //setup click listener
        renderer.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                em.notify("[MOUSE_PRESSED]",e);
            }
        });

        //add renderer panel to frame
        frame.setContentPane(renderer);
    }

    //start UI
    public void start(){
        SwingUtilities.invokeLater(()->{
            frame.setVisible(true);
            timer.start();
        });
    }

    //stop UI
    public void stop(){
        timer.stop();
        frame.setVisible(false);
    }

    //Add listener
    //Returns true if action is successfully
    public boolean addListener(EventListener listener) {
        return em.addListener(listener);
    }

    //Removes listener
    //Returns true if action is successfully
    public boolean removeListener(EventListener listener) {
        return em.removeListener(listener);
    }

    //paint only lines (needs for image export)
    public void paintImage(Graphics g, boolean with_back) {
        if (with_back) {
            renderer.paint(g);
        } else {
            renderer.paintLines(g);
        }
    }

    //resize panel
    public void resize(int width, int height) {
        frame.setSize(width, height);
    }

    //update list of points to render
    public void update(List<Point> points) {
        renderer.updateRenderer(points);
    }

    //set lines color
    public void setLineColor(Color color) {
        renderer.setLineColor(color);
    }

    //set back color
    public void setBackgroundColor(Color color) {
        renderer.setBackColor(color);
    }
    //return frame
    public JFrame getFrame(){
        return frame;
    }
    //exporting image
    public BufferedImage getImage(boolean withBackground){
        BufferedImage buf=new BufferedImage(renderer.getWidth(),renderer.getHeight(),BufferedImage.TYPE_INT_ARGB);
        paintImage(buf.createGraphics(),withBackground);
        return buf;
    }
}
