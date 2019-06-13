package io.github.keheck.csminecraft.objectholder;

public class MapCoordinateHolder
{
    private static final int[] bounds = new int[6];
    private static final int[] terrSpawn = new int[5];
    private static final int[] couterrSpawn = new int[5];
    private static final int[] bombA = new int[5];
    private static final int[] bombB = new int[5];

    public static int[] get(String name)
    {
        switch(name)
        {
            case "bounds":
                return bounds;
            case "TSpawn":
                return terrSpawn;
            case "CTSpawn":
                return couterrSpawn;
            case "bombA":
                return bombA;
            case "bombB":
                return bombB;
            default:
                return bounds;
        }
    }
}
