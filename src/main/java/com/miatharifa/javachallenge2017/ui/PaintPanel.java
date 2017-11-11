package com.miatharifa.javachallenge2017.ui;

import com.miatharifa.javachallenge2017.game.PlayerModel;
import com.miatharifa.javachallenge2017.models.*;
import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import java.awt.*;
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

	private static volatile GameState gameState = null;
	private static volatile GameDescription description = null;

	private final int width;
	private final int height;
	private final int fontHeightInPixel = 28;
	private final int marginFromXEndInPixels = 200;
	private long paintStartTime;
	private int lineOffset = 0;
	private Graphics2D imageForStatistics;

	PaintPanel(int x, int y) {
		this.width = x;
		this.height = y;
		setBackground(Color.WHITE);
		this.setFocusable(true);
		this.grabFocus();


	}

	@Override
	public void paintComponent(Graphics g) {
		this.paintStartTime = System.currentTimeMillis();
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

		if (description != null) {

			// Draw planets
			description.planets.forEach(planet -> {
                g2d.setColor(ISLAND_SAND);
                PlanetState state =  gameState.getPlanetStatesMap().get(planet.planetID);
				g2d.translate(planet.x, height - planet.y);
				g2d.fillOval(
						(int) (-planet.radius),
						(int) (-planet.radius),
						(int) (planet.radius * 2),
						(int) (planet.radius * 2));
                g2d.translate(-planet.x, -(height - planet.y));

                g2d.setColor(Color.BLACK);
                g2d.drawString(planet.planetID.toString(), planet.x, height - planet.y);
                g2d.drawString(state.owner + "[" + state.ownershipRatio + "]", planet.x, height - planet.y+15);
                String armies = StringUtils.join(
                        state.stationedArmies.stream()
                        .map(s->s.owner + "[" + s.size + "]")
                        .collect(Collectors.toList()),
                        ", ");
                g2d.drawString(armies, planet.x, height - planet.y+30);
			});

//			// Draw ships
//			session.myShips.forEach(ship -> {
//				drawImage.translate(ship.position.getX(), height - ship.position.getY());
//				drawImage.rotate(Math.toRadians(90 - ship.rotation));
//
//				if(!ship.usingExtendedSonar){
//					drawImage.setColor(SONAR_SEAWEED);
//					drawImage.fillOval(
//							(-session.mapConfiguration.sonarRange),
//							(-session.mapConfiguration.sonarRange),
//							(session.mapConfiguration.sonarRange * 2),
//							(session.mapConfiguration.sonarRange * 2));
//				} else {
//					drawImage.setColor(EXTENDED_SONAR_SPONGEBOB);
//					drawImage.fillOval(
//							(-session.mapConfiguration.extendedSonarRange),
//							(-session.mapConfiguration.extendedSonarRange),
//							(session.mapConfiguration.extendedSonarRange * 2),
//							(session.mapConfiguration.extendedSonarRange * 2));
//				}
//
//				drawImage.setColor(MIATHARIFA_COLOR);
//				drawImage.fillOval(
//						(int) (-ship.r),
//						(int) (-ship.r),
//						(int) (ship.r * 2),
//						(int) (ship.r * 2));
//				drawImage.setColor(GRASS_GREEN);
//				drawImage.fillPolygon(makeTriangleX(ship.r), makeTriangleY(ship.r), 3);
//				drawImage.rotate(Math.toRadians(-(90 - ship.rotation)));
//				drawImage.translate(-ship.position.getX(), -(height - ship.position.getY()));
//
//				drawImage.setColor(MIATHARIFA_COLOR);
//				if (!ship.futurePositions.isEmpty()) {
//					ship.futurePositions.forEach(p -> drawImage.fillOval((int) p.x - 2, (int) (height - (p.y - 2)), 4, 4));
//				}
//				if (!ship.futureTorpedoPositions.isEmpty()) {
//					ship.futureTorpedoPositions.forEach(p -> drawImage.fillOval((int) p.x - 2, (int) (height - (p.y - 2)), 4, 4));
//				}
//			});
//
//			// Draw line for ship's next position
//            commands.forEach(
//                    command -> {
//                        g2d.setColor(Color.BLACK);
//                        Planet planetFrom = description.planetsMap().get(command.moveFrom);
//                        Planet planetTo = description.planetsMap().get(command.moveTo);
//                        g2d.drawLine(
//                                toIntExact(planetFrom.x),
//                                toIntExact(height - planetFrom.y),
//                                toIntExact(planetTo.x),
//                                toIntExact(height - planetTo.y));
//
//
////                        // Draw travelling units
////                        g2d.setColor(LILA_TRAVELLING_UNIT);
////                        double fulltime = description.fullTimeToTravel(planetFrom, planetTo);
////                        g2d.translate(description.mapSizeX, height - ship.position.getY());
////				        g2d.fillOval(
////						    (-session.mapConfiguration.torpedoExplosionRadius),
////						    (-session.mapConfiguration.torpedoExplosionRadius),
////						    (session.mapConfiguration.torpedoExplosionRadius * 2),
////						    (session.mapConfiguration.torpedoExplosionRadius * 2));
//                    });

			// Draw state of planets
            gameState.planetStates.forEach(planetState -> {
                // Draw moving armies
                planetState.movingArmies.forEach(movingArmy -> {
                    double x = movingArmy.x;
                    double y = movingArmy.y;
                    g2d.translate(x, height - y);
                    g2d.setColor(colors.get(movingArmy.owner));
                    g2d.fillOval(
                            -10,
                            -10,
                            20,
                            20);
                    g2d.setColor(Color.BLACK);
                    g2d.drawString(movingArmy.size.toString(), 0, 0);
                    g2d.translate(-x, -(height - y));
                });
//				double x = planetState.movingArmies;
//				double y = torpedo.position.getY();
//				drawImage.translate(x, height - y);
//
//				drawImage.setColor(LILA_TRAVELLING_UNIT);
//				drawImage.fillOval(
//						(-session.mapConfiguration.torpedoExplosionRadius),
//						(-session.mapConfiguration.torpedoExplosionRadius),
//						(session.mapConfiguration.torpedoExplosionRadius * 2),
//						(session.mapConfiguration.torpedoExplosionRadius * 2));
//
//				drawImage.setColor(FOS_LILA);
//				drawImage.fillOval(
//						-5,
//						-5,
//						10,
//						10);
//				drawImage.translate(-x, -(height - y));
			});
//
//			session.map.enemyShips.forEach(ship -> {
//				drawImage.translate(ship.position.getX(), height - ship.position.getY());
//				drawImage.rotate(Math.toRadians(90 - ship.rotation));
//
//				drawImage.setColor(ENEMY_SHIP);
//				drawImage.fillOval(
//						(int) (-ship.r),
//						(int) (-ship.r),
//						(int) (ship.r * 2),
//						(int) (ship.r * 2));
//				drawImage.setColor(GRASS_GREEN);
//				drawImage.fillPolygon(makeTriangleX(ship.r), makeTriangleY(ship.r), 3);
//				drawImage.rotate(Math.toRadians(-(90 - ship.rotation)));
//				drawImage.translate(-ship.position.getX(), -(height - ship.position.getY()));
//			});

			// Draw Statistics
			drawStatistics(g2d);
		}
	}

	private int[] makeTriangleY(double r) {
		return new int[]{
				0, 0, (int) -r
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

		printLine("gameLength: " + description.gameLength / 1000 + " sec");
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
		printLine("timeElapsed: " + gameState.timeElapsed / 1000 + " sec");
//		printLine("remainingPlayers: " + gameState.remainingPlayers);

		printLine("Standings:");
		for (Standing standing : gameState.standings) {
			printLine("   " + standing.userID + " str=[" + standing.strength + "] sco=[" + standing.score + "]");
		}
	}

	void init(GameDescription description) {
		PaintPanel.description = description;

		Random random = new Random();
        PaintPanel.colors = PaintPanel.description.players.stream()
                .filter(i->!i.userName.equals(PlayerModel.NAME))
                .collect(Collectors.toMap(item -> item.userID, item -> {
            int r = random.nextInt(155) + 100;
            int g = random.nextInt(155) + 100;
            int b = random.nextInt(155) + 100;
            return new Color(r, g, b);
        }));
        colors.put(PlayerModel.NAME, MIATHARIFA_COLOR);
    }

	void refresh(GameState gameState) {
		PaintPanel.gameState = gameState;
		this.invalidate();
	}

    void sendCommand(Command command) {
        PaintPanel.commands.add(command);
    }
}