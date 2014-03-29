#version 120

uniform sampler2D tex0;
uniform sampler2D tex1;
uniform sampler2D tex2;

uniform vec2 pixelSize;
uniform int vertical;
uniform float totalAlpha;

void main()
{
    gl_FragColor = texture2D(tex0, gl_TexCoord[0].st);
    vec4 newColor = gl_FragColor * 0.2;

    float textureDepth = texture2D(tex2, gl_TexCoord[0].st).r;
    textureDepth -= 0.985;
    if (textureDepth > 0.0)
    {
        textureDepth *= 66.6667;
    }
    else
    {
        textureDepth *= 1.5;
    }
    for (int i = 0; i < 1; i++)
    {
        textureDepth = textureDepth * textureDepth;
    }
    textureDepth -= 0.01;
    textureDepth *= 1.0;
    textureDepth = clamp(textureDepth, 0.0, 2.0);

    if (textureDepth > 0.0)
    {
        float xMul = (vertical == 0) ? pixelSize.x : 0.0;
        float yMul = (vertical == 1) ? pixelSize.y : 0.0;
        
        xMul *= textureDepth;
        yMul *= textureDepth;
        
        for(float i = -1.0; i < 2.0; i += 2.0)
        {
            newColor += texture2D(tex0, vec2(gl_TexCoord[0].s + 1.0 * i * xMul, gl_TexCoord[0].t + 1.0 * i * yMul)) * 0.15;
            newColor += texture2D(tex0, vec2(gl_TexCoord[0].s + 2.0 * i * xMul, gl_TexCoord[0].t + 2.0 * i * yMul)) * 0.11;
            newColor += texture2D(tex0, vec2(gl_TexCoord[0].s + 3.0 * i * xMul, gl_TexCoord[0].t + 3.0 * i * yMul)) * 0.09;
            newColor += texture2D(tex0, vec2(gl_TexCoord[0].s + 4.0 * i * xMul, gl_TexCoord[0].t + 4.0 * i * yMul)) * 0.05;
        }
        
        gl_FragColor = mix(gl_FragColor, newColor, totalAlpha);        
    }
}
