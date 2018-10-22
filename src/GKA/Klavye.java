package GKA;

import java.awt.event.KeyEvent;

public class Klavye {

    public static boolean[] basildiMi=new boolean[256];
    public static boolean[] onceki=new boolean[256];

    private Klavye(){}

    public static void guncelle(){
        onceki[KeyEvent.VK_RIGHT]=basildiMi[KeyEvent.VK_RIGHT];
        onceki[KeyEvent.VK_LEFT]=basildiMi[KeyEvent.VK_LEFT];
        onceki[KeyEvent.VK_UP]=basildiMi[KeyEvent.VK_UP];
        onceki[KeyEvent.VK_DOWN]=basildiMi[KeyEvent.VK_DOWN];

    }

    public static void tusBasildi(KeyEvent o){
        basildiMi[o.getKeyCode()]=true;
    }

    public static void tusBirakildi(KeyEvent o){
        basildiMi[o.getKeyCode()]=false;
    }


    public static boolean yaz(int tus){
        return !basildiMi[tus] && onceki[tus];
    }

}
