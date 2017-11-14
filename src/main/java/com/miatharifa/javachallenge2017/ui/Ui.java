package com.miatharifa.javachallenge2017.ui;

import com.miatharifa.javachallenge2017.game.GameModel;
import com.miatharifa.javachallenge2017.models.Command;
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

        MainPaint(GameModel _gameModel) {
            int x = _gameModel.map.mapSizeX;
            int y = _gameModel.map.mapSizeY;

            setTitle("test paint");
            setSize(x, y);

            paintPanel = new PaintPanel(x, y);
            add(paintPanel, BorderLayout.CENTER);

            setVisible(true);
            this.init(_gameModel);
        }

        void refresh(GameModel gameModel) {
            paintPanel.refresh(gameModel);
            repaint();
        }

        void init(GameModel description) {
            paintPanel.init(description);
            repaint();
        }

        void sendCommand(Command command)  {
            paintPanel.sendCommand(command);
            repaint();
        }
    }

    public static void refresh(GameModel gameModel) {
        frame.refresh(gameModel);
    }

    private static boolean started = false;

    public boolean started() {
        return started;
    }

    public static void init(GameModel gameModel) {
        if(frame == null) {
            EventQueue.invokeLater(() -> {
                frame = new MainPaint(gameModel);
                frame.setVisible(true);
            });
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                log.error("Sleep interrupted", e);
            }
            log.info("UI initialized");
        }
        started = true;
    }


    public static void sendCommand(Command command) {
        frame.sendCommand(command);
    }
}