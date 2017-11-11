package com.miatharifa.javachallenge2017.ui;

import com.miatharifa.javachallenge2017.game.GameModel;
import com.miatharifa.javachallenge2017.models.*;
import org.apache.commons.lang3.StringUtils;

import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.JPanel;

class PaintPanel extends JPanel {
	private static final long serialVersionUID = 1L;

	private final Color ISLAND_SAND = new Color(145, 141, 77);
	private final Color OCEAN_BLUE = new Color(119, 207, 255);
	private final Color HORNPUB_HEAT = new Color(205, 27, 17);
	private final Color SONAR_SEAWEED = new Color(65, 255, 70, 62);
	private final Color EXTENDED_SONAR_SPONGEBOB = new Color(236, 208, 114, 62);
	private final Color GRASS_GREEN = new Color(143, 255, 40, 206);
	private final Color FOS_LILA = new Color(153, 0, 153);
	private final Color LILA_TORPEDO_EXPLOSION_RADIUS = new Color(238, 139, 205, 100);

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
		Graphics2D drawImage = (Graphics2D) g;

		double scaler = (double) this.getWidth() / (double) width;
		drawImage.scale(scaler, scaler);

		g.setFont(new Font("TimesRoman", Font.PLAIN, 14));

		drawImage.setColor(OCEAN_BLUE);
		drawImage.fillRect(0, 0, width, height);

		// Segedvonalak
		for (int i = 0; i <= width; i += 100) {
			drawImage.setColor(Color.LIGHT_GRAY);
			drawImage.drawLine(i, 0, i, height);
			drawImage.setColor(Color.DARK_GRAY);
			drawImage.drawString("" + i, i, height);
		}
		for (int i = 0; i <= height; i += 100) {
			drawImage.setColor(Color.LIGHT_GRAY);
			drawImage.drawLine(0, i, width, i);
			drawImage.setColor(Color.DARK_GRAY);
			if (i != height)
				drawImage.drawString("" + (height - i), 0, i + 14);
		}

		if (description != null) {

			// Draw planets
			drawImage.setColor(ISLAND_SAND);
			description.planets.forEach(planet -> {
                PlanetState state =  this.gameState.getPlanetStatesMap().get(planet.planetID);
				drawImage.translate(planet.x, height - planet.y);
				drawImage.fillOval(
						(int) (-planet.radius),
						(int) (-planet.radius),
						(int) (planet.radius * 2),
						(int) (planet.radius * 2));
                drawImage.translate(-planet.x, -(height - planet.y));

                drawImage.setColor(Color.BLACK);
                drawImage.drawString(planet.planetID.toString(), planet.x, height - planet.y);
                drawImage.drawString(state.owner + "[" + state.ownershipRatio + "]", planet.x, height - planet.y+15);
                String armies = StringUtils.join(
                        state.stationedArmies.stream()
                        .map(s->s.owner + "[" + s.size + "]")
                        .collect(Collectors.toList()),
                        ", ");
                drawImage.drawString(armies, planet.x, height - planet.y+30);
                drawImage.setColor(ISLAND_SAND);
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
//				drawImage.setColor(HORNPUB_HEAT);
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
//				drawImage.setColor(HORNPUB_HEAT);
//				if (!ship.futurePositions.isEmpty()) {
//					ship.futurePositions.forEach(p -> drawImage.fillOval((int) p.x - 2, (int) (height - (p.y - 2)), 4, 4));
//				}
//				if (!ship.futureTorpedoPositions.isEmpty()) {
//					ship.futureTorpedoPositions.forEach(p -> drawImage.fillOval((int) p.x - 2, (int) (height - (p.y - 2)), 4, 4));
//				}
//			});
//
////			//draw line for ship's next position
////			drawImage.setColor(Color.BLACK);
////			session.myShips
////					.stream()
////					.filter(ship -> !ship.nextPositions.isEmpty())
////					.forEach(
////							ship -> drawImage.drawLine((int) ship.position.getX(),
////									(int) (height - ship.position.getY()),
////									(int) ship.nextPositions.get(0).getX(),
////									(int) (height - ship.nextPositions.get(0).getY())));
//
//			// Draw Torpedos
//			session.map.torpedos.forEach(torpedo -> {
//				double x = torpedo.position.getX();
//				double y = torpedo.position.getY();
//				drawImage.translate(x, height - y);
//
//				drawImage.setColor(LILA_TORPEDO_EXPLOSION_RADIUS);
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
//			});
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
			drawStatistics(drawImage);
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
	}

	void refresh(GameState gameState) {
		PaintPanel.gameState = gameState;
		this.invalidate();
	}
}