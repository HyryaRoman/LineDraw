package org.java_run.linedraw;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class DataSaver {
    //Save data in file
    //Returns true if action is successfully
    public static boolean save(String path,Data data){
        try(ObjectOutputStream out=new ObjectOutputStream(new FileOutputStream(path))){
            out.writeObject(data);
            out.flush();
            return true;
        }catch (Exception e){
            e.fillInStackTrace();
            return false;
        }
    }
    //Load data from file
    //Returns null if action is UNSUCCESSFULLY
    public static Data load(String path){
        try(ObjectInputStream in=new ObjectInputStream(new FileInputStream(path))){
            Object obj=in.readObject();
            return (obj instanceof Data)?((Data)obj):null;
        }catch (Exception e){
            e.fillInStackTrace();
            return null;
        }
    }
}
