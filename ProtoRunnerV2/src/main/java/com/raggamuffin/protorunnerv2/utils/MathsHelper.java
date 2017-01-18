package com.raggamuffin.protorunnerv2.utils;

import java.util.Random;

public class MathsHelper 
{
	public final static double PI_OVER_2 = Math.PI / 2;
	
	private static final Random Rand = new Random();
	
	// Linear Interpolation.
	public static double Lerp(double Amount, double Min, double Max)
	{
		return (Min * (1.0f - Amount) + (Max * Amount));
	}
	
	// Normalise a value in range 0 - 1.
	public static double Normalise(double val, double min, double max)
	{
		return Clamp((val - min) / (max - min),0.0, 1.0);
	}
	
	// Normalise a value in range -1 - 1.
	public static double SignedNormalise(double Amount, double Min, double Max)
	{
		return Clamp((Normalise(Amount, Min, Max) - 0.5) * 2, -1.0, 1.0);
	}

    public static double Max(double... args)
    {
        double max = Double.MIN_VALUE;

        for(double d : args)
        {
            if(d > max)
			{
				max = d;
			}
        }

        return max;
    }

    public static double Min(double... args)
    {
        double min = Double.MAX_VALUE;

        for(double d : args)
        {
            if(d < min)
                min = d;
        }

        return min;
    }

	// Clamp a value between two values.
	public static double Clamp(double amount, double min, double max)
	{
		if(amount > max)
			return max;

		if(amount < min)
			return min;

		return amount;
	}

	public static long Clamp(long amount, long min, long max)
	{
		if(amount > max)
			return max;

		if(amount < min)
			return min;

		return amount;
	}

    public static int ClampInt(int amount, int min, int max)
    {
        if(amount > max)
            return max;

        if(amount < min)
            return min;

        return amount;
    }

    public static double Wrap(double amount, double min, double max)
    {
        return (amount % (max - min)) + min;
    }
	
	// Returns a random double within in range Min - Max.
	public static double RandomDouble(double min, double max)
	{
		return (Rand.nextDouble() * (max - min)) + min;
	}

	public static float RandomFloat(float min, float max)
	{
		return (Rand.nextFloat() * (max - min)) + min;
	}

    // Returns a random double between the min-max range with a bias.
    // Higher bias returns lower average.
    public static double BiasedRandomDouble(double min, double max, double bias)
    {
        return (Math.pow(Rand.nextDouble(), bias) * (max - min)) + min;
    }
	
	// Returns a random int within the range Min -Max.
	public static int RandomInt(int Min, int Max)
	{
		return Rand.nextInt((Max - Min)) + Min;
	}
	
	public static boolean RandomBoolean()
	{
		return Rand.nextBoolean();
	}

	public static double FastInverseSqrt(float x)
	{
		return (double)Float.intBitsToFloat(0x5f3759d5 - (Float.floatToIntBits(x) >> 1));
	}
}
