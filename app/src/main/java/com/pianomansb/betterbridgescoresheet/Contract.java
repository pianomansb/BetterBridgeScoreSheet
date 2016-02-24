package com.pianomansb.betterbridgescoresheet;

/**
 * Created by pianomansb on 2/1/16.
 */
public class Contract {
    public static final int CLUBS =0;
    public static final int DIAMONDS =1;
    public static final int HEARTS =2;
    public static final int SPADES =3;
    public static final int NOTRUMP =4;
    public static final int NORTH =5;
    public static final int EAST =6;
    public static final int SOUTH =7;
    public static final int WEST =8;
    public static final int NOT_DOUBLED =9;
    public static final int DOUBLED =10;
    public static final int REDOUBLED =11;


    private int suit;
    private int level;
    private int declarer = NORTH;
    private int doubled;

    public int getDoubled() {
        return doubled;
    }

    public void setDoubled(int doubled) {
        this.doubled = doubled;
    }

    public static int getNextSeat(int seat) {
        switch (seat) {
            case NORTH:
                return EAST;
            case EAST:
                return SOUTH;
            case SOUTH:
                return WEST;
            default:
                return NORTH;
        }
    }

    public static String toString(int staticConstId) {
        switch (staticConstId) {
            case 0:
                return "\u2663";
            case 1:
                return "\u2666";
            case 2:
                return "\u2665";
            case 3:
                return "\u2660";
            case 4:
                return "NT";
            case 5:
                return "North";
            case 6:
                return "East";
            case 7:
                return "South";
            case 8:
                return "West";
            case 10:
                return "x";
            case 11:
                return "xx";
            default:
                return "";
        }
    }

    public int getDeclarer() {
        return declarer;
    }

    public void setDeclarer(int declarer) {
        this.declarer = declarer;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getSuit() {
        return suit;
    }

    public void setSuit(int suit) {
        this.suit = suit;
    }
}
