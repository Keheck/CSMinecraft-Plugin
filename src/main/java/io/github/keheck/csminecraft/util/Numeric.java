package io.github.keheck.csminecraft.util;

import org.bukkit.Location;

public class Numeric
{
    private Numeric() {}

    public static boolean between(double min, double val, double max) { return min <= val && val < max; }

    public static double dist(double a, double b) { return Math.abs(a-b); }

    public static boolean between(Location edge1, Location val, Location edge2)
    {
        double edge1X = edge1.getX();
        double edge1Y = edge1.getY();
        double edge1Z = edge1.getZ();

        double valX = val.getX();
        double valY = val.getY();
        double valZ = val.getZ();

        double edge2X = edge2.getX();
        double edge2Y = edge2.getY();
        double edge2Z = edge2.getZ();

        boolean betweenX;
        boolean betweenY;
        boolean betweenZ;

        if(edge1X < edge2X)
            betweenX = between(edge1X-1, valX, edge2X+1);
        else
            betweenX = between(edge2X-1, valX, edge1X+1);

        if(edge1Y < edge2Y)
            betweenY = between(edge1Y-1, valY, edge2Y+1);
        else
            betweenY = between(edge2Y-1, valY, edge1Y+1);

        if(edge1Z < edge2Z)
            betweenZ = between(edge1Z-1, valZ, edge2Z+1);
        else
            betweenZ = between(edge2Z-1, valZ, edge1Z+1);

        return betweenX && betweenY && betweenZ;
    }

    public static int[] sortCoordinates(int[] coords)
    {
        for(int i = 0; i < 3; i++)
        {
            if(coords[i] > coords[i+3])
            {
                int cache = coords[i];
                coords[i] = coords[i+3];
                coords[i+3] = cache;
            }
        }

        return coords;
    }
}
