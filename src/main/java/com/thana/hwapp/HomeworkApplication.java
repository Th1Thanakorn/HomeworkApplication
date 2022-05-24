package com.thana.hwapp;

import com.thana.hwapp.utils.TimingUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.font.TextAttribute;
import java.awt.image.BufferStrategy;
import java.util.HashMap;
import java.util.Map;

public class HomeworkApplication extends Canvas implements Runnable {

    private static final long serialVersionUID = 1L;
    private static final String[] DOTS = new String[]{"O o o", "o O o", "o o O"};

    public static final int WIDTH = 960;
    public static final int HEIGHT = 800;
    public static final String TITLE = "Homework Application";
    public static final Dimension SIZE = new Dimension(WIDTH, HEIGHT);

    public static final String BG_HEX = "#222222";
    public static final String fontPlain = "Segoe UI";
    public static final Map<TextAttribute, Integer> fontAttributes = new HashMap<TextAttribute, Integer>() {{
        this.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
    }};

    private Thread thread;
    private boolean running;
    private int particularDotTime = 0;

    public HomeworkApplication() {
    }

    public static void main(String[] args) throws Exception {
        HomeworkApplication app = new HomeworkApplication();
        JFrame frame = new JFrame(TITLE);
        CommonHelper.initButtons(frame);
        frame.setPreferredSize(SIZE);
        frame.setMaximumSize(SIZE);
        frame.setMinimumSize(SIZE);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.add(app);
        frame.setVisible(true);
        frame.setLayout(null);

        app.start();

        HomeworkGetter.tryGet();
        HomeworkGetter.getHomeworkList();
    }

    public synchronized void start() {
        if (running) return;
        this.running = true;
        this.thread = new Thread(this);
        this.thread.start();
    }

    public synchronized void stop() {
        if (!running) return;
        try {
            this.thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
            System.exit(0);
        }
    }

    private void tick() {
        CommonHelper.tickField();
    }

    private void render() {
        BufferStrategy strategy = this.getBufferStrategy();
        if (strategy == null) {
            this.createBufferStrategy(3);
            return;
        }
        Graphics graphics = strategy.getDrawGraphics();
        graphics.setColor(Color.decode(BG_HEX));
        graphics.fillRect(0, 0, WIDTH, HEIGHT);

        Font plainFont = new Font("Segoe UI", Font.PLAIN, 20);
        Font boldFont = new Font("Segoe UI", Font.BOLD, 20);
        Font smallPlainFont = new Font("Segoe UI", Font.PLAIN, 16);
        Font headerPlainFont = new Font("Segoe UI", Font.BOLD, 21).deriveFont(fontAttributes);

        if (HomeworkGetter.finished) {
            if (CommonHelper.hasLogin()) {
                // Reset Components
                CommonHelper.studentId.setVisible(false);

                int i = 0;
                for (String date : HomeworkGetter.DATE_AND_HOMEWORKS.keySet()) {
                    graphics.setColor(Color.WHITE);
                    graphics.setFont(headerPlainFont);
                    graphics.drawString(date, 8, 32 + i * 28);
                    i++;
                    for (String homework : HomeworkGetter.DATE_AND_HOMEWORKS.get(date)) {
                        graphics.setColor(Color.WHITE);
                        graphics.setFont(plainFont);
                        graphics.drawString(homework, 8, 32 + i * 28);
                        i++;
                    }
                    i += 8;
                }
            }
            else {
                CommonHelper.studentId.setVisible(true);
                String text = "Please login using your student id";
                graphics.setColor(Color.WHITE);
                graphics.setFont(boldFont);
                graphics.drawString(text, WIDTH / 2 - graphics.getFontMetrics().stringWidth(text) / 2, HEIGHT / 2 - graphics.getFontMetrics().getHeight() / 2);
            }
        }
        else {
            String text = "Loading";
            String dot = DOTS[this.particularDotTime];
            graphics.setColor(Color.WHITE);
            graphics.setFont(boldFont);
            graphics.drawString(text, WIDTH / 2 - graphics.getFontMetrics().stringWidth(text) / 2, HEIGHT / 2 - graphics.getFontMetrics().getHeight() / 2);
            graphics.setColor(Color.WHITE);
            graphics.setFont(smallPlainFont);
            graphics.drawString(dot, WIDTH / 2 - graphics.getFontMetrics().stringWidth(dot) / 2, HEIGHT / 2 - graphics.getFontMetrics().getHeight() / 2 + 20);
        }
        graphics.dispose();
        strategy.show();

        if (TimingUtil.mod(500)) {
            this.particularDotTime = (this.particularDotTime + 1) % 3;
        }
    }

    @Override
    public void run() {
        long lastTime = System.nanoTime();
        double amountOfTicks = 60.0D;
        double ns = 1000000000 / amountOfTicks;
        double delta = 0;
        long timer = System.currentTimeMillis();
        while (running) {
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;
            while (delta >= 1) {
                tick();
                delta--;
            }
            if (running) render();
            if (System.currentTimeMillis() - timer > 1000) {
                timer += 1000;
            }
        }
        this.stop();
    }
}
