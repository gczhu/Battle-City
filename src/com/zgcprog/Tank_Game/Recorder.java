package com.zgcprog.Tank_Game;

import java.io.*;
import java.util.Vector;
import java.util.logging.Level;

/**
 * @author 祝广程
 * @version 1.0
 */
public class Recorder {
    private static int level = 1;
    private static int score = 0;

    private static int high_score = 0;
    private static int HP;
    private static int enemyTankNum;
    private static BufferedWriter bw = null;
    private static BufferedWriter bw2 = null;
    private static BufferedReader br = null;
    private static String recordFile = "src\\gameInfo.txt";

    private static String scoreFile = "src\\highScore.txt";
    private static Vector<EnemyTank> enemyTanks = null;
    private static MyTank hero = null;
    private static Vector<Node> nodes = new Vector<>();

    public static Vector<Node> readInfo() {
        try {
            br = new BufferedReader(new FileReader(recordFile));
            level = Integer.parseInt(br.readLine());
            score = Integer.parseInt(br.readLine());
            HP = Integer.parseInt(br.readLine());
            enemyTankNum = Integer.parseInt(br.readLine());
            String line = "";
            while ((line = br.readLine()) != null) {
                String[] pos = line.split(" ");
                Node node = new Node(Integer.parseInt(pos[0]), Integer.parseInt(pos[1]), Integer.parseInt(pos[2]));
                nodes.add(node);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return nodes;
    }

    public static void saveInfo() {
        try {
            HP = hero.getHP();
            enemyTankNum = enemyTanks.size();
            bw = new BufferedWriter(new FileWriter(recordFile));
            bw.write(level + "\r\n");
            bw.write(score + "\r\n");
            bw.write(HP + "\r\n");
            bw.write(enemyTankNum + "\r\n");
            String tankInfo = hero.getX() + " " + hero.getY() + " " + hero.getDirect();
            bw.write(tankInfo + "\r\n");
            for (int i = 0; i < enemyTanks.size(); i++) {
                EnemyTank enemyTank = enemyTanks.get(i);
                if (enemyTank.getHP() > 0) {
                    tankInfo = enemyTank.getX() + " " + enemyTank.getY() + " " + enemyTank.getDirect();
                    bw.write(tankInfo + "\r\n");
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (bw != null) {
                try {
                    bw.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public static void saveHighScore(){
        if (score > high_score) {
            try {
                bw2 = new BufferedWriter(new FileWriter(scoreFile));
                bw2.write(score + "\r\n");
            } catch (IOException e) {
                throw new RuntimeException(e);
            } finally {
                if (bw2 != null) {
                    try {
                        bw2.close();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
    }

    public static void setEnemyTanks(Vector<EnemyTank> enemyTanks) {
        Recorder.enemyTanks = enemyTanks;
    }

    public static void setHero(MyTank hero) {
        Recorder.hero = hero;
    }

    public static int getScore() {
        return score;
    }

    public static void setScore(int score) {
        Recorder.score = score;
    }

    public static void addScore() {
        score += 10;
    }

    public static int getHP() {
        return HP;
    }

    public static void setHP(int HP) {
        Recorder.HP = HP;
    }

    public static int getEnemyTankNum() {
        return enemyTankNum;
    }

    public static void setEnemyTankNum(int enemyTankNum) {
        Recorder.enemyTankNum = enemyTankNum;
    }

    public static String getRecordFile() {
        return recordFile;
    }

    public static int getLevel() {
        return level;
    }

    public static void setLevel(int level) {
        Recorder.level = level;
    }

    public static void addLevel() {
        level++;
    }

    public static int getHigh_score() {
        return high_score;
    }

    public static void setHigh_score(int high_score) {
        Recorder.high_score = high_score;
    }
}
