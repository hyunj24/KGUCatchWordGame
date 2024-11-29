package game;

import javax.swing.*;
import javax.swing.plaf.basic.BasicButtonUI;
import java.awt.*;

public class Button extends BasicButtonUI {
    @Override
    public void paint(Graphics g, JComponent c) {
    	super.paint(g, c);
        AbstractButton b = (AbstractButton) c;
        Graphics2D g2 = (Graphics2D) g.create();
        b.setOpaque(false);  // 버튼의 배경색을 투명하게 설정
        b.setBackground(new Color(255,0,0,0));
        
        //버튼에 이미지 입히기
        Image brick=new ImageIcon("imgs/brick.png").getImage();
        g2.drawImage(brick, 0, 0, b.getWidth(), b.getHeight(), b);
        // 텍스트 색상과 스타일 설정
        g2.setFont(new Font("돋움", Font.BOLD, 30));
        g2.setColor(Color.black);
        
        FontMetrics fm = g2.getFontMetrics();
        int x = (b.getWidth() - fm.stringWidth(b.getText())) / 2;
        int y = (b.getHeight() + fm.getAscent()) / 2 - 4;
        g2.drawString(b.getText(), x, y);

        g2.dispose();
    }
}