//*********************************************************
// �̹����� Affine Translation �ϱ� ���� BufferesImage��
// ��ȯ�ϱ� ���� ��ƿ��Ƽ Ŭ����
//*********************************************************

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.net.URL;

public class Utilities {
  private static final Component sComponent = new Component() {};
  private static final MediaTracker sTracker = new MediaTracker(sComponent);
  private static int sID = 0;
  
  public static boolean waitForImage(Image image) {
    int id;
    synchronized(sComponent) { id = sID++; }
    sTracker.addImage(image, id);
    try { sTracker.waitForID(id); }
    catch (InterruptedException ie) { return false; }
    if (sTracker.isErrorID(id)) return false;
    return true;
  }    

  public static BufferedImage makeBufferedImage(Image image) {
    return makeBufferedImage(image, BufferedImage.TYPE_INT_RGB);
  }
  // BufferedImage ��ȯ
  public static BufferedImage makeBufferedImage(Image image, int imageType) {
    if (waitForImage(image) == false) return null;

    BufferedImage bufferedImage = new BufferedImage(
        image.getWidth(null), image.getHeight(null),
        imageType);
    Graphics2D g2 = bufferedImage.createGraphics();
    g2.drawImage(image, null, null);
    return bufferedImage;
  }
}
