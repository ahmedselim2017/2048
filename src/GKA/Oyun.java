package GKA;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;

public class Oyun extends JPanel implements KeyListener,Runnable {

    private static final long serialVersionUID=1L;

    public static final int GENISLIK=400;
    public static final int YUKSEKLIK=630;
    public static final Font font=new Font("Bebas Neue Regular",Font.PLAIN,28);
    private Thread oyun;
    private boolean calisiyorMu;
    private BufferedImage resim=new BufferedImage(GENISLIK,YUKSEKLIK,BufferedImage.TYPE_INT_RGB);

    private long baslamaSaati;
    private boolean ayarla;
    private OyunTahtasi tahta;

    public Oyun(){
        setFocusable(true);
        setPreferredSize(new Dimension(GENISLIK,YUKSEKLIK));
        addKeyListener(this);

        tahta=new OyunTahtasi(GENISLIK/2-OyunTahtasi.TAHTA_GENISLIK/2,YUKSEKLIK-OyunTahtasi.TAHTA_YUKSEKLIK-10);
    }

    private void guncelle(){

        tahta.guncelle();
        Klavye.guncelle();
    }

    private void render(){
        Graphics2D g=(Graphics2D) resim.getGraphics();
        g.setColor(new Color(0xfaf9ee));
        g.fillRect(0,0,GENISLIK,YUKSEKLIK);
        tahta.render(g);
        g.dispose();
        Graphics2D g2d=(Graphics2D) getGraphics();
        g2d.drawImage(resim,0,0,null);
        g.dispose();

    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        Klavye.tusBasildi(e);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        Klavye.tusBirakildi(e);
    }

    @Override
    public void run() {
        int fps=0, guncellemeler=0;
        long fpsZamanlayici=System.currentTimeMillis();
        double guncellemeArasiNS=1000000000.0/60;


        //nanosaniye cinsinden son guncelleme
        double sonra=System.nanoTime();
        double islenmeyen=0;


        while(calisiyorMu){

            boolean renderlanmaliMi=false;
            double simdi=System.nanoTime();
            islenmeyen+=(simdi-sonra)/guncellemeArasiNS;
            sonra=simdi;


            //guncelleme sırası
            while (islenmeyen>=1){
                guncellemeler++;
                guncelle();
                islenmeyen--;
                renderlanmaliMi=true;
            }

            //Renderlama
            if(renderlanmaliMi){
                fps++;
                render();
                renderlanmaliMi=false;
            }
            else {
                try {
                    Thread.sleep(1);
                }
                catch (Exception h){
                    h.printStackTrace();
                }
            }
        }

        //FPS Sayaci
        if(System.currentTimeMillis()-fpsZamanlayici>1000){
            System.out.printf("%d fps %d guncellemeler",fps,guncellemeler);
            System.out.println();
            fps=0;
            guncellemeler=0;
            fpsZamanlayici+=1000;
        }

    }

    public synchronized void baslat(){
        if(calisiyorMu)
            return;

        calisiyorMu=true;
        oyun=new Thread(this,"oyun");
        oyun.start();
    }

    public synchronized void durdur(){
        if(!calisiyorMu)
            return;

        calisiyorMu=false;
        System.exit(0);
    }

}
