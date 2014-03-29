#version 120

uniform sampler2D tex0;

uniform vec2 pixelSize;
uniform int vertical;
uniform float totalAlpha;

void main()
{
    gl_FragColor = texture2D(tex0, gl_TexCoord[0].st);
    vec4 newColor = gl_FragColor;
    vec4 bloomColor = vec4(0.0);

    float xMul = (vertical == 0) ? (pixelSize.x * 3.0) : 0.0;
    float yMul = (vertical == 1) ? (pixelSize.y * 3.0) : 0.0;
    
    float colorInfluence = 15.0 / (5.0 + newColor.r + newColor.g + newColor.b);
    colorInfluence = colorInfluence * colorInfluence;
    
    for(float i = -1.0; i < 2.0; i += 2.0)
    {
        bloomColor += texture2D(tex0, clamp(vec2(gl_TexCoord[0].s + 1.0 * i * xMul, gl_TexCoord[0].t + 1.0 * i * yMul), 0.0, 1.0)) * 0.028 * colorInfluence;
        bloomColor += texture2D(tex0, clamp(vec2(gl_TexCoord[0].s + 2.0 * i * xMul, gl_TexCoord[0].t + 2.0 * i * yMul), 0.0, 1.0)) * 0.02 * colorInfluence;
        bloomColor += texture2D(tex0, clamp(vec2(gl_TexCoord[0].s + 3.0 * i * xMul, gl_TexCoord[0].t + 3.0 * i * yMul), 0.0, 1.0)) * 0.016 * colorInfluence;
        bloomColor += texture2D(tex0, clamp(vec2(gl_TexCoord[0].s + 4.0 * i * xMul, gl_TexCoord[0].t + 4.0 * i * yMul), 0.0, 1.0)) * 0.012 * colorInfluence;
    }
    
    newColor.rgb += bloomColor.rgb * bloomColor.rgb * bloomColor.rgb;
        
    gl_FragColor = mix(gl_FragColor, newColor, totalAlpha);
}
