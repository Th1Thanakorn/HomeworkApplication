package com.thana.hwapp;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;

public class HomeworkApplication extends Canvas implements Runnable {

    private static final long serialVersionUID = 1L;

    public static final int WIDTH = 960;
    public static final int HEIGHT = 800;
    public static final String TITLE = "Homework Application";
    public static final Dimension SIZE = new Dimension(WIDTH, HEIGHT);

    public static final String BG_HEX = "#222222";

    private Thread thread;
    private boolean running;

    public HomeworkApplication() {
    }

    public static void main(String[] args) {
        HomeworkApplication app = new HomeworkApplication();
        JFrame frame = new JFrame(TITLE);
        frame.setPreferredSize(SIZE);
        frame.setMaximumSize(SIZE);
        frame.setMinimumSize(SIZE);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.add(app);
        frame.setVisible(true);

        app.start();
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
        graphics.dispose();
        strategy.show();
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
