#version 120

uniform sampler2D tex0;
uniform sampler2D tex1;

uniform sampler2D texFractal0;
varying vec2 texFractal0Coords;

const int GL_LINEAR = 9729;
const int GL_EXP = 2048;

uniform float ticks;
uniform int worldTime;
uniform vec2 pixelSize;

uniform int fogMode;
uniform int fogEnabled;
uniform int lightmapEnabled;
uniform int texture2DEnabled;
uniform vec4 overrideColor;
uniform int useScreenTexCoords;

uniform int fractal0TexIndex;

uniform float redshrooms;
uniform float brownshrooms;
uniform vec4 harmoniumColor;

uniform float desaturation;
uniform float colorIntensification;

uniform vec3 playerPos;

varying vec3 normalVector;
varying vec4 projGLPos;

uniform int glLightEnabled;
uniform float glLightAmbient;
uniform vec3 glLightPos0;
uniform vec2 glLightStrength0;
uniform vec3 glLightPos1;
uniform vec2 glLightStrength1;

uniform float depthMultiplier;

uniform int doShadows;
uniform sampler2D texShadow;
uniform mat4 inverseViewMatrix;
uniform mat4 sunMatrix;
uniform vec2 sunDepthRange;
uniform float shadowBias;

vec3 getRotatedColor(vec3 color, float rot)
{
	vec3 returnColor = vec3(0);
	
	for(int i = 0; i < 3; i++)
	{
        float colorAffected = mod(float(i) + rot * 3.0, 3.0);
        
        int col1 = int(floor(colorAffected));
        
        returnColor[col1] += color[i] * (1.0 - (colorAffected - float(col1)));
        returnColor[int(mod(float(col1 + 1), 3.0))] += color[i] * (colorAffected - float(col1));
	}
    
	return returnColor;
}

vec3 getIntensifiedColor(vec3 color)
{
    float s = 2.0;
    float cR = 0.3086;
    float cG = 0.6084;
    float cB = 0.0820;
    
    float rr = (1.0 - s) * cR + s;
    float rg = (1.0 - s) * cG;
    float rb = (1.0 - s) * cB;
    float gr = (1.0 - s) * cR;
    float gg = (1.0 - s) * cG + s;
    float gb = (1.0 - s) * cB;
    float br = (1.0 - s) * cR;
    float bg = (1.0 - s) * cG;
    float bb = (1.0 - s) * cB + s;
    
    vec3 rVec = vec3(color.r * rr + color.g * rg + color.b * rb, color.r * gr + color.g * gg + color.b * gb, color.r * br + color.g * bg + color.b * bb);
    
    return clamp(rVec * rVec * 10.0, 0.0, 1.0);
}

vec3 getDesaturatedColor(vec3 color)
{
    float cR = 0.3086;
    float cG = 0.6084;
    float cB = 0.0820;

    float brightness = (color.r * cR + color.g * cG + color.b * cB);
    
    return vec3(brightness);
}

float linearize(float value, float zNear, float zFar)
{
    return (2 * zNear) / (zFar + zNear - value * (zFar - zNear));
}

float delinearize(float value, float zNear, float zFar)
{
    return ((zFar + zNear) - ((2.0 * zNear) / value)) / (zFar - zNear);
}

void main()
{
    if (texture2DEnabled == 1)
    {
        if (useScreenTexCoords == 1)
            gl_FragColor = texture2D(tex0, (gl_FragCoord.xy + gl_TexCoord[0].st) * pixelSize);
        else
            gl_FragColor = texture2D(tex0, gl_TexCoord[0].st);
    }
    else
    {
        gl_FragColor = vec4(1.0, 1.0, 1.0, 1.0);
    }
    
    gl_FragColor *= gl_Color;
    gl_FragColor *= overrideColor;
    
    if (lightmapEnabled == 1)
    {
        float projectedVisibility = 1.0;
        if (doShadows == 1)
        {
            vec2 screenSize = 1.0 / pixelSize;
            vec4 worldCoords = inverseViewMatrix * projGLPos;
            
            vec4 shadowCoord = sunMatrix * worldCoords;
            vec3 ndc_shadowCoord = shadowCoord.xyz / shadowCoord.w;
            vec2 shadowTexCoord = (ndc_shadowCoord.xy + 1.0) * 0.5;
            
            if (shadowTexCoord.x <= 1.0 && shadowTexCoord.x >= 0.0 && shadowTexCoord.y <= 1.0 && shadowTexCoord.y >= 0.0)
            {
                float shadowDepth = texture2D(texShadow, shadowTexCoord).z;
                float calcShadowDepth = (ndc_shadowCoord.z + 1.0) * 0.5;
                
                if (linearize(calcShadowDepth, sunDepthRange.x, sunDepthRange.y) > linearize(shadowDepth, sunDepthRange.x, sunDepthRange.y) + shadowBias)
                {
                    projectedVisibility = 0.5;
                }
            }
        }

        vec2 lMCoords = gl_TexCoord[1].st;
        lMCoords += 8.0;
        lMCoords /= 256.0;
        
        lMCoords = clamp(lMCoords, 0.0, 1.0);
        
        vec4 lighting = texture2D(tex1, lMCoords);
        gl_FragColor.rgb *= min(lighting.rgb, vec3(projectedVisibility));
    }
    
    if (glLightEnabled == 1)
    {
        float glLightColor = glLightAmbient;
        
        float angle0 = acos((normalVector.x * glLightPos0.x + normalVector.y * glLightPos0.y + normalVector.z * glLightPos0.z) / (length(normalVector) * length(glLightPos0)));
        if (angle0 < 0.0) angle0 = -angle0;
        if (angle0 > 3.1415926) angle0 = 3.1415926;
        glLightColor = glLightColor + (3.1415926 - angle0) * glLightStrength1[0];

        float angle1 = acos((normalVector.x * glLightPos1.x + normalVector.y * glLightPos1.y + normalVector.z * glLightPos1.z) / (length(normalVector) * length(glLightPos1)));
        if (angle1 < 0.0) angle1 = -angle1;
        if (angle1 > 3.1415926) angle1 = 3.1415926;
        glLightColor = glLightColor + (3.1415926 - angle1) * glLightStrength1[1];
        
        gl_FragColor.rgb = gl_FragColor.rgb * clamp(glLightColor, glLightAmbient, 1.0);
    }
    
    gl_FragColor = clamp(gl_FragColor, 0.0, 1.0);
    
    if (fogEnabled == 1)
    {
        if (fogMode == GL_EXP)
        {
            gl_FragColor.rgb = mix(gl_FragColor.rgb, gl_Fog.color.rgb, 1.0 - clamp(exp(-gl_Fog.density * gl_FogFragCoord), 0.0, 1.0));
        }
        else if (fogMode == GL_LINEAR)
        {
            gl_FragColor.rgb = mix(gl_FragColor.rgb, gl_Fog.color.rgb, clamp((gl_FogFragCoord - gl_Fog.start) * gl_Fog.scale, 0.0, 1.0));
        }
    }
    
    if(brownshrooms > 0.0)
    {
        vec4 fractalColor = texture2D(texFractal0, texFractal0Coords);
        float avg = (fractalColor.r + fractalColor.g + fractalColor.b) / 3.0;
        gl_FragColor.rgb = mix(gl_FragColor.rgb, getRotatedColor(gl_FragColor.rgb, avg * brownshrooms), brownshrooms);
    }
    if(redshrooms > 0.0)
    {
        float r = (sin((gl_FogFragCoord - ticks) / 5.0) - 0.4) * redshrooms;
        
        if(r > 0.0)
            gl_FragColor.r += r;
    }
    
    if(redshrooms > 0.0)
    {
        gl_FragColor.rgb = mix(gl_FragColor.rgb, getRotatedColor(gl_FragColor.rgb, mod(ticks + gl_FogFragCoord, 50.0) / 50.0), clamp(redshrooms * 1.5, 0.0, 1.0));
    }
    if(brownshrooms > 0.0)
    {
        gl_FragColor.rgb = mix(gl_FragColor.rgb, getRotatedColor(gl_FragColor.rgb, mod(ticks, 300.0) / 300.0), brownshrooms / 2.0);
    }

    if (colorIntensification != 0.0)
        gl_FragColor.rgb = mix(gl_FragColor.rgb, getIntensifiedColor(gl_FragColor.rgb), colorIntensification);

    if (desaturation != 0.0)
        gl_FragColor.rgb = mix(gl_FragColor.rgb, getDesaturatedColor(gl_FragColor.rgb), desaturation);

    if(harmoniumColor.a > 0.0)
    {
        vec3 c1 = gl_FragColor.rgb;
        vec3 c2 = harmoniumColor.rgb;
        
        float distR = sqrt((c1.r - c2.r) * (c1.r - c2.r));
        float distG = sqrt((c1.g - c2.g) * (c1.g - c2.g));
        float distB = sqrt((c1.b - c2.b) * (c1.b - c2.b));
        
        float dist = clamp((distR + distG + distB), 0.0, 1.0);
        for (int i = 0; i < 4; i++)
            dist *= dist;
        float harmonizeStrength = dist * 3.0;
        
        float disk = (sin((gl_FogFragCoord - ticks) * 0.1434234) - 0.4) * 0.8;
        harmonizeStrength += disk;

        float disk2 = (sin((gl_FogFragCoord - ticks) * -0.12313) - 0.2) * 0.8;
        harmonizeStrength += disk2;

        float disk3 = sin((gl_FogFragCoord - ticks) * -0.051233) * 0.2 * sin(ticks * 0.1321334);
        harmonizeStrength += disk3;

        harmonizeStrength = clamp(harmonizeStrength, 0.0, 3.0);
        
        vec3 harmonizedColor;
        
        if (harmonizeStrength < 1.0)
        {
            harmonizedColor = mix(harmoniumColor.rgb, vec3(0.5), harmonizeStrength); // Max of 1.0
        }
        else
        {
            harmonizedColor = mix(vec3(0.5), vec3(1.0) - harmoniumColor.rgb, (harmonizeStrength - 1.0) * 0.5); // Max of 2.0
        }
        
        gl_FragColor.rgb = mix(gl_FragColor.rgb, harmonizedColor, harmoniumColor.a);
    }

    gl_FragColor = clamp(gl_FragColor, 0.0, 1.0);
    gl_FragDepth = gl_FragCoord.z * depthMultiplier;
}
