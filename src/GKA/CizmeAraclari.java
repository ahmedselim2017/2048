package GKA;

import java.awt.*;
import java.awt.font.TextLayout;
import java.awt.geom.Rectangle2D;

public class CizmeAraclari {

    private CizmeAraclari(){}

    public static int mesajGenisligi(String mesaj, Font font ,Graphics2D g){
        g.setFont(font);
        Rectangle2D sinir=g.getFontMetrics().getStringBounds(mesaj,g);
        return (int) sinir.getWidth();
    }

    public static int mesajYuksekligi(String mesaj,Font font,Graphics2D g){
        g.setFont(font);
        if(mesaj.length()==0)
            return 0;
        TextLayout duzen=new TextLayout(mesaj,font,g.getFontRenderContext());
        return (int) duzen.getBounds().getHeight();
    }

}
