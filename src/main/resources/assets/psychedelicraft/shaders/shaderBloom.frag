#version 120

uniform sampler2D tex0;

uniform vec2 pixelSize;
uniform int vertical;
uniform float totalAlpha;

void main()
{
    gl_FragColor = texture2D(tex0, gl_TexCoord[0].st);
    vec4 newColor = gl_FragColor;
    vec3 bloomColor = vec3(0.0);

    float xMul = (vertical == 0) ? (pixelSize.x * 3.0) : 0.0;
    float yMul = (vertical == 1) ? (pixelSize.y * 3.0) : 0.0;
    
    float colorInfluence = 15.0 / (5.0 + newColor.r + newColor.g + newColor.b);
    colorInfluence = colorInfluence * colorInfluence;
    
    for(float i = -1.0; i < 2.0; i += 2.0)
    {
        bloomColor += texture2D(tex0, clamp(gl_TexCoord[0].st + vec2(1.0 * i * xMul, 1.0 * i * yMul), 0.0, 1.0)).rgb * 0.028 * colorInfluence;
        bloomColor += texture2D(tex0, clamp(gl_TexCoord[0].st + vec2(2.0 * i * xMul, 2.0 * i * yMul), 0.0, 1.0)).rgb * 0.020 * colorInfluence;
        bloomColor += texture2D(tex0, clamp(gl_TexCoord[0].st + vec2(3.0 * i * xMul, 3.0 * i * yMul), 0.0, 1.0)).rgb * 0.016 * colorInfluence;
        bloomColor += texture2D(tex0, clamp(gl_TexCoord[0].st + vec2(4.0 * i * xMul, 4.0 * i * yMul), 0.0, 1.0)).rgb * 0.012 * colorInfluence;
    }
    
    newColor.rgb += bloomColor * bloomColor * bloomColor;
        
    gl_FragColor = mix(gl_FragColor, newColor, totalAlpha);
}
