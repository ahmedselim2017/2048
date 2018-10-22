package GKA;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class Fayans {
    public static final int GENISLIK=80;
    public static final int YUKSEKLIK=80;
    public static final int KAYDIRMA_HIZI=20;
    public static final int KAVIS_GENISLIK=7;
    public static final int KAVIS_YUKSEKLIK=7;

    private int deger;
    private BufferedImage resim;
    private Color arkaplanRengi;
    private Color yaziRengi;
    private Font font;
    private int x;
    private int y;
    private Nokta kaydir;
    private boolean birlesebilirMi=true;

    private boolean baslangıçAnimasyonu=true;
    private double boyutIlkAnimasyon=0.1;
    private BufferedImage baslangicResmi;

    private boolean birlestirmeAnimasyonu=false;
    private double boyutBirlestirAnimasyon=1.2;
    private BufferedImage birlestirmeResmi;
    private AffineTransform gecis;


    public Fayans(int deger,int x,int y){
        this.deger=deger;
        this.setX(x);
        this.setY(y);
        kaydir=new Nokta(x,y);
        resim=new BufferedImage(GENISLIK,YUKSEKLIK,BufferedImage.TYPE_INT_ARGB);
        baslangicResmi=new BufferedImage(GENISLIK,YUKSEKLIK,BufferedImage.TYPE_INT_ARGB);
        birlestirmeResmi=new BufferedImage(GENISLIK*2,YUKSEKLIK*2,BufferedImage.TYPE_INT_ARGB);
        resmiCiz();
    }


    private void resmiCiz(){
        Graphics2D g=(Graphics2D) resim.getGraphics();
        switch (deger){
            case 2:
                arkaplanRengi=new Color(0xeee4da);
                yaziRengi=new Color(120, 110, 100);
                break;
            case 4:
                arkaplanRengi=new Color(0xede0c8);
                yaziRengi=new Color(120, 110, 100);
                break;
            case 8:
                arkaplanRengi=new Color(0xf2b179);
                yaziRengi=Color.white;
                break;
            case 16:
                arkaplanRengi=new Color(0xf59563);
                yaziRengi=Color.white;
                break;
            case 32:
                arkaplanRengi=new Color(0xf67c5f);
                yaziRengi=Color.white;
                break;
            case 64:
                arkaplanRengi=new Color(0xf65e3b);
                yaziRengi=Color.white;
                break;
            case 128:
                arkaplanRengi=new Color(0xedcf72);
                yaziRengi=Color.white;
                break;
            case 256:
                arkaplanRengi=new Color(0xedcc61);
                yaziRengi=Color.white;
                break;
            case 512:
                arkaplanRengi=new Color(0xedc850);
                yaziRengi=Color.white;
                break;
            case 1024:
                arkaplanRengi=new Color(0xedc53f);
                yaziRengi=Color.white;
                break;
            case 2048:
                arkaplanRengi=new Color(0xedc22e);
                yaziRengi=Color.white;
                break;
            default:
                arkaplanRengi=new Color(0xed702e);
                yaziRengi=Color.white;
                break;
        }

        g.setColor(new Color(0,0,0,0));
        g.fillRect(0,0,GENISLIK,YUKSEKLIK);

        g.setColor(arkaplanRengi);
        g.fillRoundRect(0,0,GENISLIK,YUKSEKLIK,KAVIS_GENISLIK,KAVIS_YUKSEKLIK);


        g.setColor(yaziRengi);

        if(deger<=64)
            font=Oyun.font.deriveFont(36f);
        else
            font=Oyun.font;
        g.setFont(font);

        int cizX=GENISLIK/2-CizmeAraclari.mesajGenisligi(""+deger,font,g)/2;
        int cizY=YUKSEKLIK/2+CizmeAraclari.mesajYuksekligi(""+deger,font,g)/2;
        g.drawString(""+deger,cizX,cizY);
        g.dispose();
    }

    public void guncelle(){
        Graphics2D g2d;
        if(baslangıçAnimasyonu){
            gecis=new AffineTransform();
            gecis.translate(GENISLIK/2-boyutIlkAnimasyon*GENISLIK/2,YUKSEKLIK/2 -boyutIlkAnimasyon*YUKSEKLIK/2);
            gecis.scale(boyutIlkAnimasyon,boyutIlkAnimasyon);
            g2d=(Graphics2D) baslangicResmi.getGraphics();
            g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION,RenderingHints.VALUE_INTERPOLATION_BICUBIC);
            g2d.setColor(new Color(206, 193, 179));
            g2d.fillRect(0,0,GENISLIK,YUKSEKLIK);
            g2d.drawImage(resim,gecis,null);
            boyutIlkAnimasyon+=0.1;
            g2d.dispose();

            if(boyutIlkAnimasyon>=1)
                baslangıçAnimasyonu=false;
        }
        else if(isBirlestirmeAnimasyonu()){
            gecis=new AffineTransform();
            gecis.translate(GENISLIK/2-boyutBirlestirAnimasyon*GENISLIK/2,YUKSEKLIK/2 -boyutBirlestirAnimasyon*YUKSEKLIK/2);
            gecis.scale(boyutBirlestirAnimasyon,boyutBirlestirAnimasyon);
            g2d=(Graphics2D) birlestirmeResmi.getGraphics();
            g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION,RenderingHints.VALUE_INTERPOLATION_BICUBIC);
            g2d.fillRect(0,0,GENISLIK,YUKSEKLIK);
            g2d.drawImage(resim,gecis,null);
            boyutBirlestirAnimasyon-=0.05;
            g2d.dispose();

            if(boyutBirlestirAnimasyon<=1)
                setBirlestirmeAnimasyonu(false);
        }
    }

    public void render(Graphics2D g){
        if(baslangıçAnimasyonu)
            g.drawImage(baslangicResmi,x,y,null);
        else if(isBirlestirmeAnimasyonu())
            g.drawImage(birlestirmeResmi,(int)(x+GENISLIK/2-boyutBirlestirAnimasyon*GENISLIK/2),(int)(y+YUKSEKLIK/2-boyutBirlestirAnimasyon*YUKSEKLIK/2),null);
        else
            g.drawImage(resim, x, y,null);
    }

    public int degerAl(){
        return deger;
    }

    public void setDeger(int deger){
        this.deger=deger;
        resmiCiz();
    }


    public boolean birlesebilirMi() {
        return birlesebilirMi;
    }

    public void setBirlesebilirMi(boolean birlesebilirMi) {
        this.birlesebilirMi = birlesebilirMi;
    }

    public Nokta getKaydir() {
        return kaydir;
    }

    public void setKaydir(Nokta kaydir) {
        this.kaydir = kaydir;
    }


    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public boolean isBirlestirmeAnimasyonu() {
        return birlestirmeAnimasyonu;
    }

    public void setBirlestirmeAnimasyonu(boolean birlestirmeAnimasyonu) {
        this.birlestirmeAnimasyonu = birlestirmeAnimasyonu;
    }
}
