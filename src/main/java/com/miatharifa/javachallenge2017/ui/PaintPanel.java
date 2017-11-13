package com.miatharifa.javachallenge2017.ui;

import com.miatharifa.javachallenge2017.game.GameModel;
import com.miatharifa.javachallenge2017.game.PlayerModel;
import com.miatharifa.javachallenge2017.models.*;
import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Arc2D;
import java.awt.geom.Ellipse2D;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

class PaintPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    private static List<Command> commands = new LinkedList<>();
    private static Map<String, Color> colors;
    private final Color ISLAND_SAND = new Color(145, 141, 77);
    private final Color OCEAN_BLUE = new Color(119, 207, 255);
    private final Color MIATHARIFA_COLOR = new Color(205, 27, 17);
    private final Color SONAR_SEAWEED = new Color(65, 255, 70, 62);
    private final Color EXTENDED_SONAR_SPONGEBOB = new Color(236, 208, 114, 62);
    private final Color GRASS_GREEN = new Color(143, 255, 40, 206);
    private final Color FOS_LILA = new Color(153, 0, 153);
    private final Color LILA_TRAVELLING_UNIT = new Color(238, 139, 205, 100);

    private final Color ENEMY_SHIP = new Color(240, 217, 65);

    private static volatile GameModel gameModel = null;

    private final int width;
    private final int height;
    private final int fontHeightInPixel = 28;
    private final int marginFromXEndInPixels = 200;
    private int lineOffset = 0;
    private Graphics2D imageForStatistics;

    PaintPanel(int x, int y) {
        this.width = x;
        this.height = y;
        setBackground(Color.WHITE);
        this.setFocusable(true);
        this.grabFocus();


    }

    private Color planetColor(String owner) {
        if (owner == null)
            return ISLAND_SAND;
        else
            return colors.get(owner);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        double scaler = (double) this.getWidth() / (double) width;
        g2d.scale(scaler, scaler);

        g.setFont(new Font("TimesRoman", Font.PLAIN, 14));

        g2d.setColor(OCEAN_BLUE);
        g2d.fillRect(0, 0, width, height);

        // Segedvonalak
        for (int i = 0; i <= width; i += 100) {
            g2d.setColor(Color.LIGHT_GRAY);
            g2d.drawLine(i, 0, i, height);
            g2d.setColor(Color.DARK_GRAY);
            g2d.drawString("" + i, i, height);
        }
        for (int i = 0; i <= height; i += 100) {
            g2d.setColor(Color.LIGHT_GRAY);
            g2d.drawLine(0, i, width, i);
            g2d.setColor(Color.DARK_GRAY);
            if (i != height)
                g2d.drawString("" + (height - i), 0, i + 14);
        }

        // Draw planets
        gameModel.map.planets.values().forEach(planet -> {
            g2d.setColor(Color.BLACK);
            g2d.translate(planet.x, height - planet.y);
            Shape circle = new Ellipse2D.Double(
                    (int) (-planet.radius),
                    (int) (-planet.radius),
                    (int) (planet.radius * 2),
                    (int) (planet.radius * 2));
            g2d.draw(circle);
            g2d.translate(-planet.x, -(height - planet.y));

            g2d.setColor(planetColor(planet.owner));
            Arc2D arc = new Arc2D.Double(
                    planet.x - planet.radius,
                    height - planet.y - planet.radius,
                    planet.radius * 2,
                    planet.radius * 2,
                    90,
                    planet.ownershipRatio * 360,
                    Arc2D.PIE);
            g2d.fill(arc);

            g2d.setColor(Color.BLACK);
            g2d.drawString(planet.planetID.toString(), planet.x, height - planet.y);
            g2d.drawString(planet.owner + "[" + String.format("%.2f", planet.ownershipRatio) + "]",
                    planet.x,
                    height - planet.y + 15);
            String armies = StringUtils.join(
                    planet.stationedArmies.stream()
                            .map(s -> s.owner + "[" + s.size + "]")
                            .collect(Collectors.toList()),
                    ", ");
            g2d.drawString(armies, planet.x, height - planet.y + 30);
        });


        // Draw moving armies
        gameModel.map.planets.values().forEach(planet -> {
            AffineTransform transform = g2d.getTransform();
            planet.movingArmies.forEach(movingArmy -> {
                g2d.setTransform(transform);

                g2d.setColor(colors.get(movingArmy.owner));
                g2d.drawLine(planet.x.intValue(), height - planet.y.intValue(), movingArmy.x.intValue(), height - movingArmy.y.intValue());

                double x = movingArmy.x;
                double y = movingArmy.y;
                double tetha = Math.atan2(planet.x - x, planet.y - y);
                g2d.translate(x, height - y);
                g2d.rotate(tetha);
                g2d.fillPolygon(makeTriangle(10 + Math.sqrt(movingArmy.size.doubleValue())));
                g2d.setColor(Color.BLACK);
                g2d.drawString(movingArmy.size.toString(), 0, 0);
            });
            g2d.setTransform(transform);
        });

        // Draw Statistics
        drawStatistics(g2d);

    }

    private Polygon makeTriangle(double size) {
        return new Polygon(makeTriangleX(size), makeTriangleY(size), 3);
    }

    private int[] makeTriangleY(double r) {
        return new int[]{
                0, 0, (int) (-r * 1.5)
        };
    }

    private int[] makeTriangleX(double r) {
        return new int[]{
                (int) -r / 2, (int) r / 2, 0
        };
    }

    private void printLine(String str) {
        imageForStatistics.drawString(str, width - marginFromXEndInPixels, (fontHeightInPixel * ++lineOffset));
    }

    private void drawStatistics(Graphics2D drawImage) {
        drawImage.setColor(Color.BLACK);
        lineOffset = 0;
        imageForStatistics = drawImage;

        printLine("gameLength: " + gameModel.gameLength / 1000 + " sec");
//		printLine("map: " + description.mapSizeX + " x " + description.mapSizeY);
//		printLine("commandSchedule: " + description.commandSchedule + " ms");
//		printLine("internalSchedule: " + description.internalSchedule + " ms");
//		printLine("broadcastSchedule: " + description.broadcastSchedule + " ms");
//		printLine("minMovableArmySize: " + description.minMovableArmySize);
//		printLine("movementSpeed: " + description.movementSpeed  + "/sec");
//		printLine("battleSpeed: " + description.battleSpeed);
//		printLine("captureSpeed: " + description.captureSpeed);
//		printLine("unitCreateSpeed: " + description.unitCreateSpeed);
//		printLine("planetExponent: " + description.planetExponent);
//		printLine("battleExponent: " + description.battleExponent);
//		printLine("Players: " + description.players.size());
//		for (Player player : description.players) {
//			printLine("     [" + player.userID + "] [" + player.userName + "]");
//		}
//		printLine("gameStatus: " + gameState.gameStatus);
        printLine("timeElapsed: " + gameModel.timeElapsed / 1000 + " sec");
//		printLine("remainingPlayers: " + gameState.remainingPlayers);

        printLine("Standings:");
        for (Standing standing : gameModel.standings) {
            printLine("   " + standing.userID + " str=[" + standing.strength + "] sco=[" + standing.score + "]");
        }
    }

    void init(GameModel gameModel) {
        PaintPanel.gameModel = gameModel;

        Random random = new Random();
        PaintPanel.colors = PaintPanel.gameModel.players.values().stream()
                .filter(i -> !i.userName.equals(PlayerModel.NAME))
                .collect(Collectors.toMap(item -> item.userID, item -> {
                    int r = random.nextInt(155) + 100;
                    int g = random.nextInt(155) + 100;
                    int b = random.nextInt(155) + 100;
                    return new Color(r, g, b);
                }));
        colors.put(PlayerModel.NAME, MIATHARIFA_COLOR);
    }

    void refresh(GameModel gameModel) {
        PaintPanel.gameModel = gameModel;
        this.invalidate();
    }

    void sendCommand(Command command) {
        PaintPanel.commands.add(command);
    }
}