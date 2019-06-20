package io.github.keheck.csminecraft.repeats;

import io.github.keheck.csminecraft.util.Numeric;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;

public class RepeatingBoundaryMarker extends RepeatingBase
{
    private int[] bounds;
    private Particle particle;
    private World world;
                                                                            // 0,  1,  2,  3,  4,  5
                                                                            // x1, y1, z1, x2, y2, z2
    public RepeatingBoundaryMarker(JavaPlugin plugin, Particle particle, World world, int[] bounds)
    {
        super(plugin, 0, 10, -1);

        Numeric.sortCoordinates(bounds);

        this.bounds = bounds;
        this.particle = particle;
        this.world = world;
    }

    @Override
    public void run()
    {
        //Spawns the x-oriented lines
        if(bounds[0] < bounds[3])
        {
            for(int i = bounds[0]; i < bounds[3]+1; i++)
            {
                world.spawnParticle(particle, i, bounds[1], bounds[2], 5, .1, .1, .1, .001);
                world.spawnParticle(particle, i, bounds[1], bounds[5], 5, .1, .1, .1, .001);
                world.spawnParticle(particle, i, bounds[4], bounds[2], 5, .1, .1, .1, .001);
                world.spawnParticle(particle, i, bounds[4], bounds[5], 5, .1, .1, .1, .001);
            }
        }
        else
        {
            for(int i = bounds[0]; i > bounds[3]-1; i--)
            {
                world.spawnParticle(particle, i, bounds[1], bounds[2], 5, .1, .1, .1, .001);
                world.spawnParticle(particle, i, bounds[1], bounds[5], 5, .1, .1, .1, .001);
                world.spawnParticle(particle, i, bounds[4], bounds[2], 5, .1, .1, .1, .001);
                world.spawnParticle(particle, i, bounds[4], bounds[5], 5, .1, .1, .1, .001);
            }
        }

        //Spawns the y-oriented lines
        if(bounds[1] < bounds[4])
        {
            for(int i = bounds[1]; i < bounds[4]+1; i++)
            {
                world.spawnParticle(particle, bounds[0], i, bounds[2], 5, .1, .1, .1, .001);
                world.spawnParticle(particle, bounds[0], i, bounds[5], 5, .1, .1, .1, .001);
                world.spawnParticle(particle, bounds[3], i, bounds[2], 5, .1, .1, .1, .001);
                world.spawnParticle(particle, bounds[3], i, bounds[5], 5, .1, .1, .1, .001);
            }
        }
        else
        {
            for(int i = bounds[1]; i > bounds[4]-1; i--)
            {
                world.spawnParticle(particle, bounds[0], i, bounds[2], 5, .1, .1, .1, .001);
                world.spawnParticle(particle, bounds[0], i, bounds[5], 5, .1, .1, .1, .001);
                world.spawnParticle(particle, bounds[3], i, bounds[2], 5, .1, .1, .1, .001);
                world.spawnParticle(particle, bounds[3], i, bounds[5], 5, .1, .1, .1, .001);
            }
        }

        //Spawns the z-oriented lines
        if(bounds[2] < bounds[5])
        {
            for(int i = bounds[2]; i < bounds[5]+1; i++)
            {
                world.spawnParticle(particle, bounds[0], bounds[1], i, 5, .1, .1, .1, .001);
                world.spawnParticle(particle, bounds[0], bounds[4], i, 5, .1, .1, .1, .001);
                world.spawnParticle(particle, bounds[3], bounds[1], i, 5, .1, .1, .1, .001);
                world.spawnParticle(particle, bounds[3], bounds[4], i, 5, .1, .1, .1, .001);
            }
        }
        else
        {
            for(int i = bounds[2]; i > bounds[5]-1; i--)
            {
                world.spawnParticle(particle, bounds[0], bounds[1], i, 5, .1, .1, .1, .001);
                world.spawnParticle(particle, bounds[0], bounds[4], i, 5, .1, .1, .1, .001);
                world.spawnParticle(particle, bounds[3], bounds[1], i, 5, .1, .1, .1, .001);
                world.spawnParticle(particle, bounds[3], bounds[4], i, 5, .1, .1, .1, .001);
            }
        }
    }

    public int[] getBounds() { return bounds; }

    public static void lowToHigh(int[] coords)
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
    }
}
