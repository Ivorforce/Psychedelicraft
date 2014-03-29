#version 120

uniform sampler2D tex0;

uniform vec2 pixelSize;
uniform int vertical;
uniform float totalAlpha;
uniform vec3 bloomColor;

float influenceFromColor(vec3 color1, vec3 color2)
{
    vec3 distCol = sqrt((color1 - color2) * (color1 - color2));
    
    float influence = 1.0 - (distCol.r + distCol.g + distCol.g) * 2.0;
    
    return clamp(influence, 0.0, 1.0);
}

void main()
{
    gl_FragColor = texture2D(tex0, gl_TexCoord[0].st);
    vec4 newColor = gl_FragColor;
    float bloomInfluence;

    float xMul = (vertical == 0) ? (pixelSize.x * 3.0) : 0.0;
    float yMul = (vertical == 1) ? (pixelSize.y * 3.0) : 0.0;
        
    for(float i = -1.0; i < 2.0; i += 2.0)
    {
        bloomInfluence += influenceFromColor(texture2D(tex0, clamp(vec2(gl_TexCoord[0].s + 1.0 * i * xMul, gl_TexCoord[0].t + 1.0 * i * yMul), 0.0, 1.0)).rgb, bloomColor) * 0.028 * 2.0;
        bloomInfluence += influenceFromColor(texture2D(tex0, clamp(vec2(gl_TexCoord[0].s + 2.0 * i * xMul, gl_TexCoord[0].t + 2.0 * i * yMul), 0.0, 1.0)).rgb, bloomColor) * 0.02 * 2.0;
        bloomInfluence += influenceFromColor(texture2D(tex0, clamp(vec2(gl_TexCoord[0].s + 3.0 * i * xMul, gl_TexCoord[0].t + 3.0 * i * yMul), 0.0, 1.0)).rgb, bloomColor) * 0.016 * 2.0;
        bloomInfluence += influenceFromColor(texture2D(tex0, clamp(vec2(gl_TexCoord[0].s + 4.0 * i * xMul, gl_TexCoord[0].t + 4.0 * i * yMul), 0.0, 1.0)).rgb, bloomColor) * 0.012 * 2.0;
    }
    
    newColor.rgb = mix(newColor.rgb, bloomColor, clamp(bloomInfluence, 0.0, 1.0));
        
    gl_FragColor = mix(gl_FragColor, newColor, totalAlpha);
}
