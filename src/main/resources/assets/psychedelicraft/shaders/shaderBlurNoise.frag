#version 120

uniform sampler2D tex0;
uniform float totalAlpha;

uniform vec2 pixelSize;

uniform float seed;
uniform float strength;

float randomFromSeed(float aSeed)
{
	return fract(mod(aSeed * 12374.123814, 18034.805912));
}

float randomFromVec(vec2 aVec)
{
    return fract(sin(dot(aVec.xy, vec2(12.9898,78.233))) * 43758.5453);
}

void main()
{
	vec2 newTexCoords = floor((gl_TexCoord[0].st) / pixelSize) * pixelSize;
	newTexCoords.t += (mod(randomFromSeed(seed), 1.0) - 0.5) * strength * 0.04;
	
    gl_FragColor = texture2D(tex0, newTexCoords);
    vec4 newColor = gl_FragColor;
    
    float blurChance = strength * 0.01;
	   
	for (float f = -strength * 40.0; f < strength * 40.0 + 0.5; f += 1.0)
	{
		if (f != 0.0)
		{
            vec2 bTexCoords = vec2(newTexCoords.s, newTexCoords.t + f * pixelSize.y);
            
			if (bTexCoords.t > 0.0 && bTexCoords.t < 1.0)
			{
				float randomOne = randomFromVec(vec2(bTexCoords.s, bTexCoords.t + seed));
				
				if (randomOne < blurChance)
				{
					newColor = mix(newColor, texture2D(tex0, bTexCoords), 1.0 / (f * f * 0.004 + 1.0));
				}					
			}
		}
	}
    
    gl_FragColor = mix(gl_FragColor, newColor, totalAlpha);
}
