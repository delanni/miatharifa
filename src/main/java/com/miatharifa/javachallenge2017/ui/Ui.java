package com.miatharifa.javachallenge2017.ui;

import com.miatharifa.javachallenge2017.game.GameModel;
import com.miatharifa.javachallenge2017.models.GameDescription;
import com.miatharifa.javachallenge2017.models.GameState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static java.lang.Math.toIntExact;

import java.awt.*;

import javax.swing.JFrame;

public class Ui {
    private static Logger log = LoggerFactory.getLogger(Ui.class);
    private static MainPaint frame = null;

    static class MainPaint extends JFrame {
        private static final long serialVersionUID = 1L;

        private PaintPanel paintPanel = null;

        MainPaint(int x, int y) {
            setTitle("test paint");
            setSize(x + 50, y + 80);

            paintPanel = new PaintPanel(x, y);
            add(paintPanel, BorderLayout.CENTER);

            setVisible(true);
        }

        void refresh(GameState gameState) {
            paintPanel.refresh(gameState);
            repaint();
        }

        void init(GameDescription description) {
            paintPanel.init(description);
            repaint();
        }
    }

    public static void refresh(GameState gameState) {
        frame.refresh(gameState);
        log.info("UI refreshed");
    }

    private static boolean started = false;

    public boolean started() {
        return started;
    }

    public static void init(GameDescription description) {
        if(frame == null) {
            EventQueue.invokeLater(() -> {
                frame = new MainPaint(toIntExact(description.mapSizeX), toIntExact(description.mapSizeY));
                frame.setVisible(true);
            });
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                log.error("Sleep interrupted", e);
            }
            log.info("UI initialized");
        }
        frame.init(description);
        started = true;
    }
}