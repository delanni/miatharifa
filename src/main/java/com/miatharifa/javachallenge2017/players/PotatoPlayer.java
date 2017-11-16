package com.miatharifa.javachallenge2017.players;

import com.miatharifa.javachallenge2017.game.GameModel;
import com.miatharifa.javachallenge2017.game.PlayerModel;
import com.miatharifa.javachallenge2017.models.*;

import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static com.miatharifa.javachallenge2017.models.Command.CommandLabel.*;

public class PotatoPlayer extends AbstractPlayer {
    private static final Logger logger = Logger.getLogger(PotatoPlayer.class.toString());
    private static final double OVERCOUNT_MULTIPLIER = 1.5;
    private static final double ESTIMATED_PLANET_LIFETIME = 1000.0;
    private static final Integer CONQUEST_SCALER = 5;
    private static final Integer HANG_ON_DISTANCE = 50;


    public PotatoPlayer(CommanderInterface commanderInterface) {
        super(commanderInterface);
    }

    @Override
    public void updateStateRoundStart(GameModel gameModel) {
        // Don't use this, because the earliest your command will take place is going to be the end of round state

        this.gameModel = gameModel;
    }

    @Override
    public void updateStateRoundEnd(GameModel gameModel) {
        List<Command> proposedCommands = new ArrayList<>();

        List<MovingArmy> movingEnemyArmies = getMovingEnemyArmies(gameModel.map.movingArmies);
        List<StationedArmy> attackingEnemyArmies = getStationedEnemyArmies(gameModel.map.stationedArmies);
        List<StationedArmy> myArmies = getMyStationedArmies(gameModel.map.stationedArmies);

        // Defend && Mark Evacuate
        System.out.println("- DEFEND & MARK EVAC");
        List<StationedArmy> armiesToEvacuate = new ArrayList<>();
        ArrayList<StationedArmy> movableArmies = new ArrayList<>(myArmies.stream().filter(this::isMoveable).collect(Collectors.toList()));
        attackingEnemyArmies.forEach(attackingEnemy -> {
            Optional<StationedArmy> myAttackedArmy = myArmies.stream().filter(x -> x.planet == attackingEnemy.planet).findFirst();
            if (myAttackedArmy.isPresent()) {
                StationedArmy myAttackedArmy_ = myAttackedArmy.get();
                int myIncomingArmies = myAttackedArmy_.planet.movingArmies.stream()
                        .filter(x -> x.owner.equals(PlayerModel.NAME) && x.getPosition().distance(myAttackedArmy_.planet.getPosition()) < HANG_ON_DISTANCE)
                        .mapToInt(x -> x.size).sum();
                int enemyCountForMe = myAttackedArmy_.planet.getEnemyCountFor(PlayerModel.NAME);
                int myPresence = (int) (myIncomingArmies + myAttackedArmy_.size);
                if (myPresence > enemyCountForMe) {
                    // stay because we wait for incoming
                    System.out.println("Decided to defend " + myAttackedArmy_.size + " against " + enemyCountForMe + " + " + attackingEnemy.size);
                    proposedCommands.add(new Command(myAttackedArmy_, myAttackedArmy_.planet, myAttackedArmy_.size).of(DEFEND, "Defending against " + enemyCountForMe + " + " + attackingEnemy.size));
                    movableArmies.remove(myAttackedArmy_);
                } else if (myAttackedArmy_.size > this.gameModel.minMovableArmySize) {
                    // evacuate because they are coming or they overpower
                    System.out.println("Decided to evacuate " + myAttackedArmy_.size + " against " + enemyCountForMe + " + " + attackingEnemy.size);
                    armiesToEvacuate.add(myAttackedArmy_);
                } else {
                    System.out.println("Units " + myAttackedArmy_.size + " stuck on planet " + myAttackedArmy_.planet.planetID);
                }
            }
        });

        // Intercept Or Evac
        System.out.println("- INTERCEPT");
        List<RescueSquad> rescueSquads = new ArrayList<>();
        for (MovingArmy enemyArmy : movingEnemyArmies) {
            Planet targetPlanet = enemyArmy.targetPlanet;
            List<MovingArmy> arrivingEnemies = targetPlanet.movingArmies.stream().filter(x -> !x.owner.equals(PlayerModel.NAME)).collect(Collectors.toList());
            int totalArrivingEnemy = (int) (arrivingEnemies.stream().mapToInt(x -> x.size).sum() * OVERCOUNT_MULTIPLIER);
            int enemiesAlreadyThere = targetPlanet.stationedArmies.stream().filter(x -> !x.owner.equals(PlayerModel.NAME)).mapToInt(x -> x.size.intValue()).sum();
            int myArrivingUnits = targetPlanet.movingArmies.stream().filter(x -> x.owner.equals(PlayerModel.NAME)).mapToInt(x -> x.size).sum();
            int requiredArmySize = enemiesAlreadyThere + totalArrivingEnemy - myArrivingUnits;
            if (requiredArmySize <= 0) continue;
            double distance = enemyArmy.getPosition().distance(targetPlanet.getPosition());
            List<StationedArmy> myArmiesCloseEnough = movableArmies.stream().filter(x -> x.planet.getDistance(targetPlanet) <= distance * 0.8).collect(Collectors.toList());
            if (sumArmySize(myArmiesCloseEnough) > requiredArmySize) {
                System.out.println("Decided to intercept totalSqad of " + requiredArmySize + "@" + distance + " with " + Arrays.toString(myArmiesCloseEnough.stream().map(x -> x.planet.getPosition().distance(targetPlanet.getPosition())).toArray()));
                rescueSquads.add(new RescueSquad(targetPlanet, requiredArmySize, myArmiesCloseEnough, sumDistance(myArmiesCloseEnough, targetPlanet)));
            } else {
                // Maybe evac?
                targetPlanet.getArmiesFor(PlayerModel.NAME).ifPresent(myArmy -> {
                    if (myArmy.size < arrivingEnemies.stream().filter(x -> x.getPosition().distance(x.targetPlanet.getPosition()) < HANG_ON_DISTANCE).mapToInt(x -> x.size).sum()) {
                        armiesToEvacuate.add(myArmy);
                    }
                });
            }
        }

        while (!rescueSquads.isEmpty()) {
            rescueSquads.sort(Comparator.comparingDouble(o -> o.distance));
            RescueSquad bestRescueSquad = rescueSquads.get(0);
            rescueSquads.remove(bestRescueSquad);
            int armiesRequired = bestRescueSquad.armySize;
            for (StationedArmy army : bestRescueSquad.rescuingArmies) {
                rescueSquads.removeIf(rescueSquad -> rescueSquad == bestRescueSquad || rescueSquad.rescuingArmies.stream().anyMatch(squad -> squad.planet == army.planet));
                int armiesSent = (int) Math.min(armiesRequired, army.size);
                if (army.planet != bestRescueSquad.planet) {
                    movableArmies.remove(army);
                    proposedCommands.add(new Command(army, bestRescueSquad.planet, armiesSent).of(INTERCEPT));
                }
                armiesRequired -= armiesSent;
                if (armiesRequired == 0) {
                    break;
                }
            }
        }

        // Conquer
//        for (StationedArmy army: movableArmies) {
//            if (getRequiredOwnership(army.planet) > 0.0) {
////                movableArmies.remove(army);
//            } else {
//                List<Planet> closestPlanets = this.gameModel.map.getClosestUnownedPlanets(army.planet, PlayerModel.NAME, 100);
////                if (army.size > this.gameModel.minMovableArmySize * 2 && closestPlanets.size() > 2) {
////                    closestPlanets.sort(Comparator.comparingDouble(o -> -getPlanetConquerValue(o, army.planet, army.size / 2)));
////                    proposedCommands.add(new Command(army, closestPlanets.get(0), (int) (army.size / 2)));
////                    proposedCommands.add(new Command(army, closestPlanets.get(1), (int) (army.size / 2)));
////                } else
//                    if (closestPlanets.size() >= 1) {
//                    closestPlanets.sort(Comparator.comparingDouble(o -> getPlanetConquerValue(o, army.planet, army.size)));
//                    proposedCommands.add(new Command(army, closestPlanets.get(0), army.size));
//                }
//            }
//        }

        // Escape if wasn't used
        armiesToEvacuate.forEach(a -> {
            System.out.println("Finding new home for " + a.size + " units from " + a.planet.planetID);
            List<Planet> closestOwnPlanets = this.gameModel.map.getClosestOwnPlanets(a.planet);
            if (closestOwnPlanets.size() > 0) {
                Planet newTarget = closestOwnPlanets.get(0);
                proposedCommands.add(new Command(a, newTarget, a.size).of(ESCAPE, "Evacuating units"));
            }
        });

//        movableArmies.forEach(army -> {
//            List<Planet> closestPlanets = this.gameModel.map.getClosestUnownedPlanets(army.planet, PlayerModel.NAME, 4);
//            int size = army.size.intValue();
//            List<Integer[]> allBucketSizes = getAllBucketSizes(closestPlanets.size(), size / CONQUEST_SCALER, (int) Math.ceil(this.gameModel.minMovableArmySize / CONQUEST_SCALER));
//            List<Double> distributionValues = allBucketSizes.stream().map(distribution -> {
//                double sumValue = 0.0;
//                for (int i = 0; i < distribution.length; i++) {
//                    Planet targetPlanet = closestPlanets.get(i);
//                    int unitsSent = distribution[i];
//                    sumValue += getPlanetConquerValue(targetPlanet, army.planet, unitsSent);
//                }
//                return sumValue;
//            }).collect(Collectors.toList());
//
//            double maxValue = Double.NEGATIVE_INFINITY;
//            int maxIndex = 0;
//            for (int i = 0; i < distributionValues.size(); i++) {
//                if (maxValue < distributionValues.get(i)) {
//                    maxValue = distributionValues.get(i);
//                    maxIndex = i;
//                }
//            }
//
//            Integer[] optimalConquerDistribution = allBucketSizes.get(maxIndex);
//            for (int i = 0; i < optimalConquerDistribution.length; i++) {
//                if (optimalConquerDistribution[i] * CONQUEST_SCALER > this.gameModel.minMovableArmySize) {
//                    proposedCommands.add(new Command(army, closestPlanets.get(i), optimalConquerDistribution[i] * CONQUEST_SCALER));
//                }
//            }
//        });

        // Test & Execute commands
        proposedCommands.sort(Comparator.comparingInt(x -> x.label.ordinal()));
        if (proposedCommands.size() > 0) {
            System.out.println("Proposals: " + Arrays.toString(proposedCommands.toArray()));
            List<Command> unsentCommands = this.safeSendCommands(proposedCommands);
            System.out.println("Unsent:" + Arrays.toString(unsentCommands.toArray()));
        }
    }

    private double sumArmySize(List<StationedArmy> myArmiesCloseEnough) {
        double sum = 0.0;
        for (StationedArmy x : myArmiesCloseEnough) {
            sum += x.size;
        }
        return sum;
    }

    private double sumDistance(List<StationedArmy> myArmiesCloseEnough, Planet targetPlanet) {
        double sum = 0.0;
        for (StationedArmy x : myArmiesCloseEnough) {
            sum += x.planet.getDistance(targetPlanet);
        }
        return sum;
    }

    private boolean isMoveable(StationedArmy x) {
        return x.size >= this.gameModel.minMovableArmySize;
    }

    private List<StationedArmy> getMyStationedArmies(List<StationedArmy> armies) {
        return armies.stream().filter(x -> Objects.equals(x.owner, PlayerModel.NAME)).collect(Collectors.toList());
    }


    private List<MovingArmy> getMovingEnemyArmies(List<MovingArmy> armies) {
        return armies.stream().filter(x -> !Objects.equals(x.owner, PlayerModel.NAME)).collect(Collectors.toList());
    }

    private List<StationedArmy> getStationedEnemyArmies(List<StationedArmy> armies) {
        return armies.stream().filter(x -> !Objects.equals(x.owner, PlayerModel.NAME)).collect(Collectors.toList());
    }

    private double getPlanetConquerValue(Planet targetPlanet, Planet originPlanet, double armySize) {
        if (targetPlanet.movingArmies.stream().anyMatch(x -> PlayerModel.NAME.matches(x.owner))) return -100000000;
        if (targetPlanet.stationedArmies.size() == 1 && targetPlanet.stationedArmies.get(0).owner.equals(PlayerModel.NAME))
            return -100000000;
        double ownershipRequired = getRequiredOwnership(targetPlanet);
        if (ownershipRequired < 0.01) return -10000000;
        double timeToFlyThere = targetPlanet.getDistance(originPlanet) / this.gameModel.movementSpeed;
        double enemiesWhenArriving = targetPlanet.getEnemyCountFor(PlayerModel.NAME) + getUnitGrowth(targetPlanet, timeToFlyThere);
        if (enemiesWhenArriving > armySize) return -1000000000;
        double timeToCapture = getTimeToCapture(enemiesWhenArriving, armySize, ownershipRequired, targetPlanet.radius);
        double timeToFlyAndCapture = timeToFlyThere + timeToCapture;
        double value = getUnitGrowth(targetPlanet, ESTIMATED_PLANET_LIFETIME) - getUnitGrowth(targetPlanet, timeToFlyAndCapture);
        return value;
    }

    private double getTimeToCapture(double enemiesArmySize, double yourArmySize, double ownershipRequired, Integer radius) {
        if (enemiesArmySize > yourArmySize) {
            return Double.MAX_VALUE;
        }
        double ticks = 0;
        while (enemiesArmySize > 1.0) {
            double totalArmyOnPlanet = enemiesArmySize + yourArmySize;
            double youLoseThisMany = this.gameModel.battleSpeed * Math.pow(enemiesArmySize, this.gameModel.battleExponent) / totalArmyOnPlanet;
            double theyLoseThisMany = this.gameModel.battleSpeed * Math.pow(yourArmySize, this.gameModel.battleExponent) / totalArmyOnPlanet;
            enemiesArmySize -= theyLoseThisMany;
            yourArmySize -= youLoseThisMany;
            ticks += 1;
        }
        double ownershipRatioChangeInOneTick = yourArmySize * this.gameModel.captureSpeed / Math.pow(radius, this.gameModel.planetExponent);
        ticks += ownershipRequired / ownershipRatioChangeInOneTick;
        return ticks;
    }

    private double getUnitGrowth(Planet planet, double overTime) {
        return overTime * Math.pow(planet.radius, this.gameModel.planetExponent) * this.gameModel.unitCreateSpeed;
    }

    private double getRequiredOwnership(Planet targetPlanet) {
        if (PlayerModel.NAME.equals(targetPlanet.owner)) {
            if (targetPlanet.ownershipRatio.intValue() == 1) return 0.0;
            else return (1.0 - targetPlanet.ownershipRatio);
        } else {
            return 1.0 + targetPlanet.ownershipRatio;
        }
    }


    @Override
    public void initPlayer(GameDescription gameDescription, GameModel gameMap) {

    }

    private class RescueSquad {
        public final Planet planet;
        public final int armySize;
        public final List<StationedArmy> rescuingArmies;
        public final double distance;

        public RescueSquad(Planet targetPlanet, int armySize, List<StationedArmy> myRescuingArmies, double distance) {
            this.planet = targetPlanet;
            this.armySize = armySize;
            this.rescuingArmies = myRescuingArmies;
            this.distance = distance;
        }
    }
}
