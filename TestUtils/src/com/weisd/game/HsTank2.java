package com.weisd.game;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.EventListener;

public class HsTank2 extends JFrame {
	HsTank2(String title) {
		this.setTitle(title);
		this.setSize(608, 630);
		this.setLocation(300, 100);
		this.setBackground(Color.WHITE);

		MyTank mp = new MyTank();
		this.add(mp);

		this.addKeyListener(mp);

		new Thread(mp).start();
	}

	public static void main(String[] args) {

		HsTank2 h = new HsTank2("坦克大战(版本1.0)");

		h.setVisible(true);
	}
}

// 主战坦克
class MyTank extends JPanel implements KeyListener, Runnable {

	int x = 280, y = 280;// 坦克的初始位置
	int op = 1;// 坦克的移动方向
	int color = 0;
	int tankspeed = 8;// 坦克的速度
	int tankbullet = 8;// 坦克的子弹速度
	int tankfbullet = 4;// 敌军的子弹速度
	int shengming = 100;// 生命
	int fenshu = 0;
	int nandu = 5; // 设置游戏难度

	// 子弹
	int dx = 295, dy = 295;
	int dx1 = 295, dy1 = -10;
	int dx2 = 600, dy2 = 295;
	int dx3 = 295, dy3 = 600;
	int dx4 = -10, dy4 = 295;

	// 敌军坦克
	int num = 10;// 敌军坦克数量,不能修改
	int[] xf = new int[num];
	int[] yf = new int[num];
	int[] opf = new int[num];
	int[] dxf = new int[num];
	int[] dyf = new int[num];
	int[] dxf1 = new int[num];
	int[] dyf1 = new int[num];
	int[] dxf2 = new int[num];
	int[] dyf2 = new int[num];
	int[] dxf3 = new int[num];
	int[] dyf3 = new int[num];
	int[] dxf4 = new int[num];
	int[] dyf4 = new int[num];

	// 构造函数，初始化敌军坦克的位置和状态
	MyTank() {
		for (int i = 0; i < num; i++) {
			xf[i] = (int) (Math.random() * 560);
			yf[i] = (int) (Math.random() * 560);
			dxf[i] = xf[i] + 15;
			dyf[i] = yf[i] + 15;
		}

		for (int i = 0; i < num; i++) {
			dxf1[i] = 295;
			dyf1[i] = -10;
			dxf2[i] = 600;
			dyf2[i] = 295;
			dxf3[i] = 295;
			dyf3[i] = 600;
			dxf4[i] = -10;
			dyf4[i] = 295;
		}
	}

	// 主面版
	public void paint(Graphics g) {
		super.paint(g);
		this.setBackground(Color.WHITE);

		g.setColor(Color.red);
		g.drawString("生命:", 10, 20);
		g.fillRect(50, 10, shengming * 5, 10);
		g.drawRect(50, 10, 500, 10);
		g.drawString("得分:  " + fenshu, 10, 40);

		if (op == 1) {
			g.setColor(Color.red);
			g.fillRect(x, y, 40, 40);

			switch (color % 6) {
			case 0:
				g.setColor(Color.blue);
				break;
			case 1:
				g.setColor(Color.yellow);
				break;
			case 2:
				g.setColor(Color.red);
				break;
			case 3:
				g.setColor(Color.orange);
				break;
			case 4:
				g.setColor(Color.green);
				break;
			case 5:
				g.setColor(Color.black);
				break;
			}
			g.fillOval(x - 5, y - 5, 10, 10);
			g.fillOval(x - 5, y + 5, 10, 10);
			g.fillOval(x - 5, y + 15, 10, 10);
			g.fillOval(x - 5, y + 25, 10, 10);
			g.fillOval(x - 5, y + 35, 10, 10);
			g.fillOval(x + 35, y - 5, 10, 10);
			g.fillOval(x + 35, y + 5, 10, 10);
			g.fillOval(x + 35, y + 15, 10, 10);
			g.fillOval(x + 35, y + 25, 10, 10);
			g.fillOval(x + 35, y + 35, 10, 10);

			g.setColor(Color.black);
			g.fillRect(x + 15, y - 20, 10, 40);
			switch (color % 20) {
			case 0:
				g.setColor(Color.white);
				break;
			case 1:
				g.setColor(Color.white);
				break;
			case 2:
				g.setColor(Color.white);
				break;
			case 3:
				g.setColor(Color.white);
				break;
			case 4:
				g.setColor(Color.white);
				break;
			case 5:
				g.setColor(Color.white);
				break;
			case 6:
				g.setColor(Color.white);
				break;
			case 7:
				g.setColor(Color.white);
				break;
			case 8:
				g.setColor(Color.white);
				break;
			case 9:
				g.setColor(Color.white);
				break;

			case 10:
				g.setColor(Color.black);
				break;
			case 11:
				g.setColor(Color.black);
				break;
			case 12:
				g.setColor(Color.black);
				break;
			case 13:
				g.setColor(Color.black);
				break;
			case 14:
				g.setColor(Color.black);
				break;
			case 15:
				g.setColor(Color.black);
				break;
			case 16:
				g.setColor(Color.black);
				break;
			case 17:
				g.setColor(Color.black);
				break;
			case 18:
				g.setColor(Color.black);
				break;
			case 19:
				g.setColor(Color.black);
				break;
			}
			g.fillOval(x + 5, y + 30, 10, 10);
			g.fillOval(x + 25, y + 30, 10, 10);

		}

		if (op == 2) {
			g.setColor(Color.green);
			g.fillRect(x, y, 40, 40);

			switch (color % 6) {
			case 0:
				g.setColor(Color.blue);
				break;
			case 1:
				g.setColor(Color.yellow);
				break;
			case 2:
				g.setColor(Color.red);
				break;
			case 3:
				g.setColor(Color.orange);
				break;
			case 4:
				g.setColor(Color.green);
				break;
			case 5:
				g.setColor(Color.black);
				break;
			}
			g.fillOval(x - 5, y - 5, 10, 10);
			g.fillOval(x + 5, y - 5, 10, 10);
			g.fillOval(x + 15, y - 5, 10, 10);
			g.fillOval(x + 25, y - 5, 10, 10);
			g.fillOval(x + 35, y - 5, 10, 10);

			g.fillOval(x - 5, y + 35, 10, 10);
			g.fillOval(x + 5, y + 35, 10, 10);
			g.fillOval(x + 15, y + 35, 10, 10);
			g.fillOval(x + 25, y + 35, 10, 10);
			g.fillOval(x + 35, y + 35, 10, 10);

			g.setColor(Color.black);
			g.fillRect(x + 20, y + 15, 40, 10);
			switch (color % 20) {
			case 0:
				g.setColor(Color.white);
				break;
			case 1:
				g.setColor(Color.white);
				break;
			case 2:
				g.setColor(Color.white);
				break;
			case 3:
				g.setColor(Color.white);
				break;
			case 4:
				g.setColor(Color.white);
				break;
			case 5:
				g.setColor(Color.white);
				break;
			case 6:
				g.setColor(Color.white);
				break;
			case 7:
				g.setColor(Color.white);
				break;
			case 8:
				g.setColor(Color.white);
				break;
			case 9:
				g.setColor(Color.white);
				break;
			case 10:
				g.setColor(Color.black);
				break;
			case 11:
				g.setColor(Color.black);
				break;
			case 12:
				g.setColor(Color.black);
				break;
			case 13:
				g.setColor(Color.black);
				break;
			case 14:
				g.setColor(Color.black);
				break;
			case 15:
				g.setColor(Color.black);
				break;
			case 16:
				g.setColor(Color.black);
				break;
			case 17:
				g.setColor(Color.black);
				break;
			case 18:
				g.setColor(Color.black);
				break;
			case 19:
				g.setColor(Color.black);
				break;
			}
			g.fillOval(x, y + 5, 10, 10);
			g.fillOval(x, y + 25, 10, 10);

		}
		if (op == 3) {
			g.setColor(Color.blue);
			g.fillRect(x, y, 40, 40);

			switch (color % 6) {
			case 0:
				g.setColor(Color.blue);
				break;
			case 1:
				g.setColor(Color.yellow);
				break;
			case 2:
				g.setColor(Color.red);
				break;
			case 3:
				g.setColor(Color.orange);
				break;
			case 4:
				g.setColor(Color.green);
				break;
			case 5:
				g.setColor(Color.black);
				break;
			}
			g.fillOval(x - 5, y - 5, 10, 10);
			g.fillOval(x - 5, y + 5, 10, 10);
			g.fillOval(x - 5, y + 15, 10, 10);
			g.fillOval(x - 5, y + 25, 10, 10);
			g.fillOval(x - 5, y + 35, 10, 10);

			g.fillOval(x + 35, y - 5, 10, 10);
			g.fillOval(x + 35, y + 5, 10, 10);
			g.fillOval(x + 35, y + 15, 10, 10);
			g.fillOval(x + 35, y + 25, 10, 10);
			g.fillOval(x + 35, y + 35, 10, 10);

			g.setColor(Color.black);
			g.fillRect(x + 15, y + 20, 10, 40);
			switch (color % 20) {
			case 0:
				g.setColor(Color.white);
				break;
			case 1:
				g.setColor(Color.white);
				break;
			case 2:
				g.setColor(Color.white);
				break;
			case 3:
				g.setColor(Color.white);
				break;
			case 4:
				g.setColor(Color.white);
				break;
			case 5:
				g.setColor(Color.white);
				break;
			case 6:
				g.setColor(Color.white);
				break;
			case 7:
				g.setColor(Color.white);
				break;
			case 8:
				g.setColor(Color.white);
				break;
			case 9:
				g.setColor(Color.white);
				break;
			case 10:
				g.setColor(Color.black);
				break;
			case 11:
				g.setColor(Color.black);
				break;
			case 12:
				g.setColor(Color.black);
				break;
			case 13:
				g.setColor(Color.black);
				break;
			case 14:
				g.setColor(Color.black);
				break;
			case 15:
				g.setColor(Color.black);
				break;
			case 16:
				g.setColor(Color.black);
				break;
			case 17:
				g.setColor(Color.black);
				break;
			case 18:
				g.setColor(Color.black);
				break;
			case 19:
				g.setColor(Color.black);
				break;
			}
			g.fillOval(x + 5, y, 10, 10);
			g.fillOval(x + 25, y, 10, 10);

		}
		if (op == 4) {
			g.setColor(Color.yellow);
			g.fillRect(x, y, 40, 40);

			switch (color % 6) {
			case 0:
				g.setColor(Color.blue);
				break;
			case 1:
				g.setColor(Color.yellow);
				break;
			case 2:
				g.setColor(Color.red);
				break;
			case 3:
				g.setColor(Color.orange);
				break;
			case 4:
				g.setColor(Color.green);
				break;
			case 5:
				g.setColor(Color.black);
				break;
			}
			g.fillOval(x - 5, y - 5, 10, 10);
			g.fillOval(x + 5, y - 5, 10, 10);
			g.fillOval(x + 15, y - 5, 10, 10);
			g.fillOval(x + 25, y - 5, 10, 10);
			g.fillOval(x + 35, y - 5, 10, 10);
			g.fillOval(x - 5, y + 35, 10, 10);
			g.fillOval(x + 5, y + 35, 10, 10);
			g.fillOval(x + 15, y + 35, 10, 10);
			g.fillOval(x + 25, y + 35, 10, 10);
			g.fillOval(x + 35, y + 35, 10, 10);
			g.setColor(Color.black);
			g.fillRect(x - 20, y + 15, 40, 10);
			switch (color % 20) {
			case 0:
				g.setColor(Color.white);
				break;
			case 1:
				g.setColor(Color.white);
				break;
			case 2:
				g.setColor(Color.white);
				break;
			case 3:
				g.setColor(Color.white);
				break;
			case 4:
				g.setColor(Color.white);
				break;
			case 5:
				g.setColor(Color.white);
				break;
			case 6:
				g.setColor(Color.white);
				break;
			case 7:
				g.setColor(Color.white);
				break;
			case 8:
				g.setColor(Color.white);
				break;
			case 9:
				g.setColor(Color.white);
				break;
			case 10:
				g.setColor(Color.black);
				break;
			case 11:
				g.setColor(Color.black);
				break;
			case 12:
				g.setColor(Color.black);
				break;
			case 13:
				g.setColor(Color.black);
				break;
			case 14:
				g.setColor(Color.black);
				break;
			case 15:
				g.setColor(Color.black);
				break;
			case 16:
				g.setColor(Color.black);
				break;
			case 17:
				g.setColor(Color.black);
				break;
			case 18:
				g.setColor(Color.black);
				break;
			case 19:
				g.setColor(Color.black);
				break;
			}
			g.fillOval(x + 30, y + 5, 10, 10);
			g.fillOval(x + 30, y + 25, 10, 10);
		}
		g.setColor(Color.black);
		g.fillOval(dx, dy, 10, 10);
		g.fillOval(dx1, dy1, 10, 10);
		g.fillOval(dx2, dy2, 10, 10);
		g.fillOval(dx3, dy3, 10, 10);
		g.fillOval(dx4, dy4, 10, 10);

		for (int i = 0; i < num; i++) {
			if (opf[i] == 1) {
				g.fillRect(xf[i], yf[i], 40, 40);

				g.fillOval(xf[i] - 5, yf[i] - 5, 10, 10);
				g.fillOval(xf[i] - 5, yf[i] + 5, 10, 10);
				g.fillOval(xf[i] - 5, yf[i] + 15, 10, 10);
				g.fillOval(xf[i] - 5, yf[i] + 25, 10, 10);
				g.fillOval(xf[i] - 5, yf[i] + 35, 10, 10);

				g.fillOval(xf[i] + 35, yf[i] - 5, 10, 10);
				g.fillOval(xf[i] + 35, yf[i] + 5, 10, 10);
				g.fillOval(xf[i] + 35, yf[i] + 15, 10, 10);
				g.fillOval(xf[i] + 35, yf[i] + 25, 10, 10);
				g.fillOval(xf[i] + 35, yf[i] + 35, 10, 10);

				g.fillRect(xf[i] + 15, yf[i] - 20, 10, 40);

				g.fillOval(xf[i] + 5, yf[i] + 30, 10, 10);
				g.fillOval(xf[i] + 25, yf[i] + 30, 10, 10);
			}
			if (opf[i] == 2) {

				g.fillRect(xf[i], yf[i], 40, 40);
				g.fillOval(xf[i] - 5, yf[i] - 5, 10, 10);
				g.fillOval(xf[i] + 5, yf[i] - 5, 10, 10);
				g.fillOval(xf[i] + 15, yf[i] - 5, 10, 10);
				g.fillOval(xf[i] + 25, yf[i] - 5, 10, 10);
				g.fillOval(xf[i] + 35, yf[i] - 5, 10, 10);
				g.fillOval(xf[i] - 5, yf[i] + 35, 10, 10);
				g.fillOval(xf[i] + 5, yf[i] + 35, 10, 10);
				g.fillOval(xf[i] + 15, yf[i] + 35, 10, 10);
				g.fillOval(xf[i] + 25, yf[i] + 35, 10, 10);
				g.fillOval(xf[i] + 35, yf[i] + 35, 10, 10);
				g.fillRect(xf[i] + 20, yf[i] + 15, 40, 10);
				g.fillOval(xf[i], yf[i] + 5, 10, 10);
				g.fillOval(xf[i], yf[i] + 25, 10, 10);

			}
			if (opf[i] == 3) {
				g.fillRect(xf[i], yf[i], 40, 40);
				g.fillOval(xf[i] - 5, yf[i] - 5, 10, 10);
				g.fillOval(xf[i] - 5, yf[i] + 5, 10, 10);
				g.fillOval(xf[i] - 5, yf[i] + 15, 10, 10);
				g.fillOval(xf[i] - 5, yf[i] + 25, 10, 10);
				g.fillOval(xf[i] - 5, yf[i] + 35, 10, 10);
				g.fillOval(xf[i] + 35, yf[i] - 5, 10, 10);
				g.fillOval(xf[i] + 35, yf[i] + 5, 10, 10);
				g.fillOval(xf[i] + 35, yf[i] + 15, 10, 10);
				g.fillOval(xf[i] + 35, yf[i] + 25, 10, 10);
				g.fillOval(xf[i] + 35, yf[i] + 35, 10, 10);
				g.fillRect(xf[i] + 15, yf[i] + 20, 10, 40);
				g.fillOval(xf[i] + 5, yf[i], 10, 10);
				g.fillOval(xf[i] + 25, yf[i], 10, 10);

			}
			if (opf[i] == 4) {
				g.fillRect(xf[i], yf[i], 40, 40);

				g.fillOval(xf[i] - 5, yf[i] - 5, 10, 10);
				g.fillOval(xf[i] + 5, yf[i] - 5, 10, 10);
				g.fillOval(xf[i] + 15, yf[i] - 5, 10, 10);
				g.fillOval(xf[i] + 25, yf[i] - 5, 10, 10);
				g.fillOval(xf[i] + 35, yf[i] - 5, 10, 10);
				g.fillOval(xf[i] - 5, yf[i] + 35, 10, 10);
				g.fillOval(xf[i] + 5, yf[i] + 35, 10, 10);
				g.fillOval(xf[i] + 15, yf[i] + 35, 10, 10);
				g.fillOval(xf[i] + 25, yf[i] + 35, 10, 10);
				g.fillOval(xf[i] + 35, yf[i] + 35, 10, 10);
				g.fillRect(xf[i] - 20, yf[i] + 15, 40, 10);
				g.fillOval(xf[i] + 30, yf[i] + 5, 10, 10);
				g.fillOval(xf[i] + 30, yf[i] + 25, 10, 10);
			}
			g.fillOval(dxf1[i], dyf1[i], 10, 10);
			g.fillOval(dxf2[i], dyf2[i], 10, 10);
			g.fillOval(dxf3[i], dyf3[i], 10, 10);
			g.fillOval(dxf4[i], dyf4[i], 10, 10);
		}
	}

	public void keyTyped(KeyEvent e) {

	}

	// 键盘控制坦克的移动，发弹
	public void keyPressed(KeyEvent e) {
		color++;
		if (e.getKeyCode() == KeyEvent.VK_UP) {
			op = 1;
			y = y - tankspeed;
			dy = dy - tankspeed;
			if (y <= 0) {
				y = y + tankspeed;
				dy = dy + tankspeed;
			}
		}
		if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
			op = 2;
			x = x + tankspeed;
			dx = dx + tankspeed;
			if (x >= 560) {
				x = x - tankspeed;
				dx = dx - tankspeed;
			}
		}
		if (e.getKeyCode() == KeyEvent.VK_DOWN) {
			op = 3;
			y = y + tankspeed;
			dy = dy + tankspeed;
			if (y >= 560) {
				y = y - tankspeed;
				dy = dy - tankspeed;
			}
		}
		if (e.getKeyCode() == KeyEvent.VK_LEFT) {
			op = 4;
			x = x - tankspeed;
			dx = dx - tankspeed;
			if (x <= 0) {
				x = x + tankspeed;
				dx = dx + tankspeed;
			}
		}

		if (e.getKeyCode() == KeyEvent.VK_SPACE) {
			if (op == 1) {
				dx1 = dx;
				dy1 = dy;
			}
			if (op == 2) {
				dx2 = dx;
				dy2 = dy;
			}
			if (op == 3) {
				dx3 = dx;
				dy3 = dy;
			}
			if (op == 4) {
				dx4 = dx;
				dy4 = dy;
			}
		}

		this.repaint();
	}

	public void keyReleased(KeyEvent e) {

	}

	public void run() {

		for (int a = 0; a < 60000; a++) {
			dy1 = dy1 - tankbullet;
			dx2 = dx2 + tankbullet;
			dy3 = dy3 + tankbullet;
			dx4 = dx4 - tankbullet;

			for (int i = 0; i < num; i++) {
				dyf1[i] = dyf1[i] - tankfbullet;
				dxf2[i] = dxf2[i] + tankfbullet;
				dyf3[i] = dyf3[i] + tankfbullet;
				dxf4[i] = dxf4[i] - tankfbullet;
			}

			// 判断是否被击中
			for (int i = 0; i < num; i++) {
				if (dyf1[i] < y + 38 && dyf1[i] > y + 8 && dxf1[i] - x > -10
						&& dxf1[i] - x < 40) {
					System.out.println("被1击中");
					dxf1[i] = dxf[i];
					dyf1[i] = dyf[i];
					shengming = shengming - nandu;
				}

				if (dxf2[i] > x + 2 && dxf2[i] < x + 32 && dyf2[i] - y > -10
						&& dyf2[i] - y < 40) {
					System.out.println("被2击中");
					dxf2[i] = dxf[i];
					dyf2[i] = dyf[i];
					shengming = shengming - nandu;
				}

				if (dyf3[i] > y + 2 && dyf3[i] < y + 32 && dxf3[i] - x > -10
						&& dxf3[i] - x < 40) {
					System.out.println("被3击中");
					dxf3[i] = dxf[i];
					dyf3[i] = dyf[i];
					shengming = shengming - nandu;
				}

				if (dxf4[i] > x + 8 && dxf4[i] < x + 38 && dyf4[i] - y > -10
						&& dyf4[i] - y < 40) {
					System.out.println("被4击中");
					dxf4[i] = dxf[i];
					dyf4[i] = dyf[i];
					shengming = shengming - nandu;
				}
			}

			// 判断是否击中敌军
			for (int i = 0; i < num; i++) {
				if (dy1 < yf[i] + 38 && dy1 > yf[i] + 8 && dx1 - xf[i] > -10
						&& dx1 - xf[i] < 40) {
					System.out.println("1击中");
					fenshu = fenshu + 100;
					xf[i] = (int) (Math.random() * 560);
					yf[i] = (int) (Math.random() * 560);
				}

				if (dx2 > xf[i] + 2 && dx2 < xf[i] + 32 && dy2 - yf[i] > -10
						&& dy2 - yf[i] < 40) {
					System.out.println("2击中");
					fenshu = fenshu + 100;
					xf[i] = (int) (Math.random() * 560);
					yf[i] = (int) (Math.random() * 560);
				}

				if (dy3 > yf[i] + 2 && dy3 < yf[i] + 32 && dx3 - xf[i] > -10
						&& dx3 - xf[i] < 40) {
					System.out.println("3击中");
					fenshu = fenshu + 100;
					xf[i] = (int) (Math.random() * 560);
					yf[i] = (int) (Math.random() * 560);
				}

				if (dx4 > xf[i] + 8 && dx4 < xf[i] + 38 && dy4 - yf[i] > -10
						&& dy4 - yf[i] < 40) {
					System.out.println("4击中");
					fenshu = fenshu + 100;
					xf[i] = (int) (Math.random() * 560);
					yf[i] = (int) (Math.random() * 560);
				}

				dxf[i] = xf[i] + 15;
				dyf[i] = yf[i] + 15;
			}

			// 坦克的移动
			for (int i = 0; i < num; i++) {

				switch (opf[i]) {
				case 1: {
					yf[i]--;
					dyf[i]--;

					for (int s = 0; s < num; s++) {
						if (yf[i] <= 0) {
							yf[i]++;
							dyf[i]++;
						}
					}
					break;
				}

				case 2: {
					xf[i]++;
					dxf[i]++;

					for (int s = 0; s < num; s++) {
						if (xf[i] >= 560) {
							xf[i]--;
							dxf[i]--;
						}
					}
					break;
				}

				case 3: {
					yf[i]++;
					dyf[i]++;

					for (int s = 0; s < num; s++) {
						if (yf[i] >= 560) {
							yf[i]--;
							dyf[i]--;

						}
					}
					break;
				}

				case 4: {
					xf[i]--;
					dxf[i]--;

					for (int s = 0; s < num; s++) {
						if (xf[i] <= 0) {
							xf[i]++;
							dxf[i]++;
						}
					}

					break;
				}
				}

			}

			try {
				Thread.sleep(20);
			} catch (Exception e) {
				e.printStackTrace();
			}

			// 坦克的开火
			if (a % 50 == 5) {
				if (Math.random() > 0.5) {
					for (int i = 0; i < 2; i++) {
						if (opf[i] == 1) {
							dxf1[i] = dxf[i];
							dyf1[i] = dyf[i];
						}
						if (opf[i] == 2) {
							dxf2[i] = dxf[i];
							dyf2[i] = dyf[i];
						}
						if (opf[i] == 3) {
							dxf3[i] = dxf[i];
							dyf3[i] = dyf[i];
						}
						if (opf[i] == 4) {
							dxf4[i] = dxf[i];
							dyf4[i] = dyf[i];
						}
					}
				}

			}

			if (a % 50 == 15) {
				if (Math.random() > 0.5) {
					for (int i = 2; i < 4; i++) {
						if (opf[i] == 1) {
							dxf1[i] = dxf[i];
							dyf1[i] = dyf[i];
						}
						if (opf[i] == 2) {
							dxf2[i] = dxf[i];
							dyf2[i] = dyf[i];
						}
						if (opf[i] == 3) {
							dxf3[i] = dxf[i];
							dyf3[i] = dyf[i];
						}
						if (opf[i] == 4) {
							dxf4[i] = dxf[i];
							dyf4[i] = dyf[i];
						}
					}
				}

			}

			if (a % 50 == 25) {
				if (Math.random() > 0.5) {
					for (int i = 4; i < 6; i++) {
						if (opf[i] == 1) {
							dxf1[i] = dxf[i];
							dyf1[i] = dyf[i];
						}
						if (opf[i] == 2) {
							dxf2[i] = dxf[i];
							dyf2[i] = dyf[i];
						}
						if (opf[i] == 3) {
							dxf3[i] = dxf[i];
							dyf3[i] = dyf[i];
						}
						if (opf[i] == 4) {
							dxf4[i] = dxf[i];
							dyf4[i] = dyf[i];
						}
					}
				}

			}

			if (a % 50 == 35) {
				if (Math.random() > 0.5) {
					for (int i = 6; i < 8; i++) {
						if (opf[i] == 1) {
							dxf1[i] = dxf[i];
							dyf1[i] = dyf[i];
						}
						if (opf[i] == 2) {
							dxf2[i] = dxf[i];
							dyf2[i] = dyf[i];
						}
						if (opf[i] == 3) {
							dxf3[i] = dxf[i];
							dyf3[i] = dyf[i];
						}
						if (opf[i] == 4) {
							dxf4[i] = dxf[i];
							dyf4[i] = dyf[i];
						}
					}
				}

			}

			if (a % 50 == 45) {
				if (Math.random() > 0.5) {
					for (int i = 8; i < 10; i++) {
						if (opf[i] == 1) {
							dxf1[i] = dxf[i];
							dyf1[i] = dyf[i];
						}
						if (opf[i] == 2) {
							dxf2[i] = dxf[i];
							dyf2[i] = dyf[i];
						}
						if (opf[i] == 3) {
							dxf3[i] = dxf[i];
							dyf3[i] = dyf[i];
						}
						if (opf[i] == 4) {
							dxf4[i] = dxf[i];
							dyf4[i] = dyf[i];
						}
					}
				}

			}

			// 坦克的随机移动
			if (a % 50 == 1) {
				for (int i = 0; i < 2; i++) {
					if (Math.random() > 0.5) {
						if (Math.random() > 0.5) {
							opf[i] = 1;
						} else {
							opf[i] = 2;
						}
					} else {
						if (Math.random() > 0.5) {
							opf[i] = 3;
						} else {
							opf[i] = 4;
						}
					}

				}
			}
			if (a % 50 == 11) {
				// 坦克的随机移动
				for (int i = 2; i < 4; i++) {
					if (Math.random() > 0.5) {
						if (Math.random() > 0.5) {
							opf[i] = 1;
						} else {
							opf[i] = 2;
						}
					} else {
						if (Math.random() > 0.5) {
							opf[i] = 3;
						} else {
							opf[i] = 4;
						}
					}

				}
			}
			if (a % 50 == 21) {
				// 坦克的随机移动
				for (int i = 4; i < 6; i++) {
					if (Math.random() > 0.5) {
						if (Math.random() > 0.5) {
							opf[i] = 1;
						} else {
							opf[i] = 2;
						}
					} else {
						if (Math.random() > 0.5) {
							opf[i] = 3;
						} else {
							opf[i] = 4;
						}
					}

				}
			}
			if (a % 50 == 31) {
				// 坦克的随机移动
				for (int i = 6; i < 8; i++) {
					if (Math.random() > 0.5) {
						if (Math.random() > 0.5) {
							opf[i] = 1;
						} else {
							opf[i] = 2;
						}
					} else {
						if (Math.random() > 0.5) {
							opf[i] = 3;
						} else {
							opf[i] = 4;
						}
					}

				}
			}
			if (a % 50 == 41) {
				// 坦克的随机移动
				for (int i = 8; i < 10; i++) {
					if (Math.random() > 0.5) {
						if (Math.random() > 0.5) {
							opf[i] = 1;
						} else {
							opf[i] = 2;
						}
					} else {
						if (Math.random() > 0.5) {
							opf[i] = 3;
						} else {
							opf[i] = 4;
						}
					}

				}
			}
			// 重画
			if (shengming <= 0) {
				// 弹出player1胜利对话框
				JOptionPane.showMessageDialog(null, "你结束了!!!", "Game Over !",
						JOptionPane.ERROR_MESSAGE);
				// 结束游戏
				System.exit(0);
			}
			this.repaint();
		}

	}
}
