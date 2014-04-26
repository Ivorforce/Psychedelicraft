#version 120

uniform sampler2D tex0;

uniform float ticks;

uniform float brownshrooms;
uniform float colorIntensification;
uniform float desaturation;

void main()
{
	gl_FragColor = texture2D(tex0, gl_TexCoord[0].st);

	if(brownshrooms > 0.0)
	{
		gl_FragColor.rgb = mix(gl_FragColor.rgb, getRotatedColor(gl_FragColor.rgb, mod(ticks, 300.0) / 300.0), brownshrooms / 2.0);
	}

	if (colorIntensification != 0.0)
		gl_FragColor.rgb = mix(gl_FragColor.rgb, getIntensifiedColor(gl_FragColor.rgb), colorIntensification);

	if (desaturation != 0.0)
		gl_FragColor.rgb = mix(gl_FragColor.rgb, getDesaturatedColor(gl_FragColor.rgb), desaturation);
}
