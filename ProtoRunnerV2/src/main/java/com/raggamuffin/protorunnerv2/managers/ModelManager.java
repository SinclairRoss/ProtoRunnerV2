package com.raggamuffin.protorunnerv2.managers;

// Author: Sinclair Ross
// Date:   14/08/2016

import android.content.Context;

import com.raggamuffin.protorunnerv2.R;
import com.raggamuffin.protorunnerv2.renderer.ModelType;

import java.util.ArrayList;

public class ModelManager
{
    private ArrayList<float[]> m_ModelVertices;

    public ModelManager(Context context)
    {
        m_ModelVertices = new ArrayList<>(ModelType.values().length);
        LoadModels(context);
    }

    public float[] GetVertices(ModelType type)
    {
        return m_ModelVertices.get(type.ordinal());
    }

    private void LoadModels(Context context)
    {
        for(ModelType type : ModelType.values())
        {
            float[] vertices = ReadVertices(type, context);
            m_ModelVertices.add(type.ordinal(), vertices);
        }
    }

    private float[] ReadVertices(ModelType type, Context context)
    {
        switch (type)
        {
            case FloorPanel:
                return ReadFloatArrayFromResource(R.string.runner_vertices, context);
            case RadarFragment:
                return ReadFloatArrayFromResource(R.string.runner_vertices, context);
            case Runner:
                return ReadFloatArrayFromResource(R.string.runner_vertices, context);
            case Bit:
                return ReadFloatArrayFromResource(R.string.bit_vertices, context);
            case Byte:
                return ReadFloatArrayFromResource(R.string.byte_vertices, context);
            case Mine:
                return ReadFloatArrayFromResource(R.string.mine_vertices, context);
            case Missile:
                return ReadFloatArrayFromResource(R.string.missile_vertices, context);
            case Dummy:
                return ReadFloatArrayFromResource(R.string.dummy_vertices, context);
            case Carrier:
                return ReadFloatArrayFromResource(R.string.carrier_vertices, context);
            case EngineDrone:
                return ReadFloatArrayFromResource(R.string.enginedrone_vertices, context);
            case WeaponDrone:
                return ReadFloatArrayFromResource(R.string.weapondrone_vertices, context);
            case ThreePointStar:
                return ReadFloatArrayFromResource(R.string.three_point_star, context);
            case ShieldBearer:
                return ReadFloatArrayFromResource(R.string.shieldbearer_vertices, context);
            case Trail:
                return ReadFloatArrayFromResource(R.string.runner_vertices, context);
            case LaserPointer:
                return ReadFloatArrayFromResource(R.string.runner_vertices, context);
            case PlasmaPulse:
                return ReadFloatArrayFromResource(R.string.runner_vertices, context);
            case ParticleLaser:
                return ReadFloatArrayFromResource(R.string.runner_vertices, context);
            case PlasmaShot:
                return ReadFloatArrayFromResource(R.string.runner_vertices, context);
            case RailSlug:
                return ReadFloatArrayFromResource(R.string.runner_vertices, context);
            case Ring:
                return ReadFloatArrayFromResource(R.string.runner_vertices, context);
            case StandardPoint:
                return ReadFloatArrayFromResource(R.string.runner_vertices, context);
            case Skybox:
                return ReadFloatArrayFromResource(R.string.runner_vertices, context);
            case Nothing:
                return ReadFloatArrayFromResource(R.string.runner_vertices, context);
        }

        return null;
    }

    private float[] ReadFloatArrayFromResource(int resource, Context context)
    {
        String raw = context.getString(resource);
        raw = raw.replaceAll("\\s","");
        String[] rawArray = raw.split(",");

        int numValues = rawArray.length;
        float[] array = new float[numValues];

        for(int i = 0; i < numValues; i ++)
        {
            array[i] = Float.parseFloat(rawArray[i]);
        }

        return array;
    }
}
